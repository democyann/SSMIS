package com.systop.core.util;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.model.Sheet;
import org.apache.poi.hssf.model.Workbook;
import org.apache.poi.hssf.record.CellValueRecordInterface;
import org.apache.poi.hssf.record.LabelRecord;
import org.apache.poi.hssf.record.LabelSSTRecord;
import org.apache.poi.hssf.record.Record;
import org.apache.poi.hssf.record.RowRecord;
import org.apache.poi.hssf.record.SSTRecord;
import org.apache.poi.hssf.record.UnicodeString;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings({"unchecked", "rawtypes"})
public class XlsMergeUtil {
  private static Logger logger = LoggerFactory.getLogger(XlsMergeUtil.class);

  /**
   * 将多个Xls文件合并为一个，适用于只有一个sheet，并且格式相同的文档
   * @param inputs 输入的Xls文件，第一个XLS文件必须给出足够sheet空间
   * 例如，总共200000行数据，第一个文件至少3个空白sheet
   * @param out 输出文件
   */
  public static void merge(InputStream[] inputs, OutputStream out) {
    if (inputs == null || inputs.length <= 1) {
      throw new IllegalArgumentException("没有传入输入流数组,或只有一个输入流.");
    }

    List<Record> rootRecords = getRecords(inputs[0]);
    Workbook workbook = Workbook.createWorkbook(rootRecords);
    List<Sheet> sheets = getSheets(workbook, rootRecords);
    if(sheets == null || sheets.size() == 0) {
      throw new IllegalArgumentException("第一篇文档的格式错误，必须有至少一个sheet");
    }
    //以第一篇文档的第一个sheet为根，以后的数据都追加在这个sheet后面
    Sheet rootSheet = sheets.get(0); 
    int rootRows = getRowsOfSheet(rootSheet); //记录第一篇文档的行数，以后的行数在此基础上增加
    rootSheet.setLoc(rootSheet.getDimsLoc());
    Map<Integer, Integer> map = new HashMap(10000);
    int sheetIndex = 0;
    
    for (int i = 1; i < inputs.length; i++) { //从第二篇开始遍历
      List<Record> records = getRecords(inputs[i]);
      //达到最大行数限制，换一个sheet
      if(getRows(records) + rootRows >= RowRecord.MAX_ROW_NUMBER) {
        if((++sheetIndex) > (sheets.size() - 1)) {
          logger.warn("第一个文档给出的sheets小于需要的数量，部分数据未能合并.");
          break;
        }
        rootSheet = sheets.get(sheetIndex);
        rootRows = getRowsOfSheet(rootSheet);
        rootSheet.setLoc(rootSheet.getDimsLoc());
        logger.debug("切换Sheet{}", sheetIndex);
      }
      int rowsOfCurXls = 0;
      //遍历当前文档的每一个record
      for (Iterator itr = records.iterator(); itr.hasNext();) {
        Record record = (Record) itr.next();
        if (record.getSid() == RowRecord.sid) { //如果是RowRecord
          RowRecord rowRecord = (RowRecord) record;
          //调整行号
          rowRecord.setRowNumber(rootRows + rowRecord.getRowNumber());
          rootSheet.addRow(rowRecord); //追加Row
          rowsOfCurXls++; //记录当前文档的行数
        }
        //SST记录，SST保存xls文件中唯一的String，各个String都是对应着SST记录的索引
        else if (record.getSid() == SSTRecord.sid) {
          SSTRecord sstRecord = (SSTRecord) record;
          for (int j = 0; j < sstRecord.getNumUniqueStrings(); j++) {
            int index = workbook.addSSTString(sstRecord.getString(j));
            //记录原来的索引和现在的索引的对应关系
            map.put(Integer.valueOf(j), Integer.valueOf(index));
          }
        } else if (record.getSid() == LabelSSTRecord.sid) {
          LabelSSTRecord label = (LabelSSTRecord) record;
          //调整SST索引的对应关系
          label.setSSTIndex(map.get(Integer.valueOf(label.getSSTIndex())));
        }
        //追加ValueCell
        if (record instanceof CellValueRecordInterface) {
          CellValueRecordInterface cell = (CellValueRecordInterface) record;
          int cellRow = cell.getRow() + rootRows;
          cell.setRow(cellRow);
          rootSheet.addValueRecord(cellRow, cell);
        }
      }
      rootRows += rowsOfCurXls;
    }
    
    byte[] data = getBytes(workbook, sheets.toArray(new Sheet[0]));
    write(out, data);
  }

  static void write(OutputStream out, byte[] data) {
    POIFSFileSystem fs = new POIFSFileSystem();
    // Write out the Workbook stream
    try {
      fs.createDocument(new ByteArrayInputStream(data), "Workbook");
      fs.writeFilesystem(out);
      out.flush();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        out.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  static List<Sheet> getSheets(Workbook workbook, List records) {
    int recOffset = workbook.getNumRecords();
    int sheetNum = 0;

    // convert all LabelRecord records to LabelSSTRecord
    convertLabelRecords(records, recOffset, workbook);
    List<Sheet> sheets = new ArrayList();
    while (recOffset < records.size()) {
      Sheet sh = Sheet.createSheet(records, sheetNum++, recOffset);

      recOffset = sh.getEofLoc() + 1;
      if (recOffset == 1) {
        break;
      }
      sheets.add(sh);
    }
    return sheets;
  }

  static int getRows(List<Record> records) {
    int row = 0;
    for (Iterator itr = records.iterator(); itr.hasNext();) {
      Record record = (Record) itr.next();
      if (record.getSid() == RowRecord.sid) {
        row++;
      }
    }
    return row;
  }
  
  static int getRowsOfSheet(Sheet sheet) {
    int rows = 0;
    sheet.setLoc(0);
    while(sheet.getNextRow() != null) {
      rows++;
    }
    return rows;
  }

  @SuppressWarnings("deprecation")
  static List<Record> getRecords(InputStream input) {
    try {
      POIFSFileSystem poifs = new POIFSFileSystem(input);
      InputStream stream = poifs.getRoot().createDocumentInputStream("Workbook");
      return org.apache.poi.hssf.record.RecordFactory.createRecords(stream);
    } catch (IOException e) {
      logger.error("IO异常：{}", e.getMessage());
      e.printStackTrace();
    }
    return Collections.EMPTY_LIST;
  }

  static void convertLabelRecords(List records, int offset, Workbook workbook) {

    for (int k = offset; k < records.size(); k++) {
      Record rec = (Record) records.get(k);

      if (rec.getSid() == LabelRecord.sid) {
        LabelRecord oldrec = (LabelRecord) rec;

        records.remove(k);
        LabelSSTRecord newrec = new LabelSSTRecord();
        int stringid = workbook.addSSTString(new UnicodeString(oldrec.getValue()));

        newrec.setRow(oldrec.getRow());
        newrec.setColumn(oldrec.getColumn());
        newrec.setXFIndex(oldrec.getXFIndex());
        newrec.setSSTIndex(stringid);
        records.add(k, newrec);
      }
    }
  }

  public static byte[] getBytes(Workbook workbook, Sheet[] sheets) {
    // HSSFSheet[] sheets = getSheets();
    int nSheets = sheets.length;

    // before getting the workbook size we must tell the sheets that
    // serialization is about to occur.
    for (int i = 0; i < nSheets; i++) {
      sheets[i].preSerialize();
    }

    int totalsize = workbook.getSize();
    // pre-calculate all the sheet sizes and set BOF indexes
    int[] estimatedSheetSizes = new int[nSheets];
    for (int k = 0; k < nSheets; k++) {
      workbook.setSheetBof(k, totalsize);
      int sheetSize = sheets[k].getSize();
      estimatedSheetSizes[k] = sheetSize;
      totalsize += sheetSize;
    }
    logger.debug("分配内存{}bytes", totalsize);
    byte[] retval = new byte[totalsize];
    int pos = workbook.serialize(0, retval);

    for (int k = 0; k < nSheets; k++) {
      int serializedSize = sheets[k].serialize(pos, retval);
      if (serializedSize != estimatedSheetSizes[k]) {
        // Wrong offset values have been passed in the call to setSheetBof() above.
        // For books with more than one sheet, this discrepancy would cause excel
        // to report errors and loose data while reading the workbook
        throw new IllegalStateException("Actual serialized sheet size (" + serializedSize
            + ") differs from pre-calculated size (" + estimatedSheetSizes[k] + ") for sheet (" + k
            + ")");
        // TODO - add similar sanity check to ensure that Sheet.serializeIndexRecord() does not
        // write mis-aligned offsets either
      }
      pos += serializedSize;
    }
    return retval;
  }

  public static void main(String[] args) throws Exception {
    final String PATH = "E:\\projects\\java\\ws_0\\export\\data\\";
    InputStream[] inputs = new InputStream[25];
    inputs[0] = new java.io.FileInputStream(PATH + "07_10.xls");
    for(int i = 1; i < 25 ; i++) {
      inputs[i] = new java.io.FileInputStream(PATH + "07_01.xls");
    }
    OutputStream out = new FileOutputStream(PATH + "xx.xls");
    long t1 = System.currentTimeMillis();
    merge(inputs, out);
    System.out.println(System.currentTimeMillis() - t1);
  }

}
