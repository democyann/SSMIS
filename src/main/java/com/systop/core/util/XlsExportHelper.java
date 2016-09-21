package com.systop.core.util;

import java.io.File;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import jxl.CellView;
import jxl.Workbook;
import jxl.write.DateFormat;
import jxl.write.Label;
import jxl.write.NumberFormats;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 * Excel文件生成类
 * 
 * @author guoyou
 */
public class XlsExportHelper {
	private String path;
	private String tempPath;
	private Integer index = -1;
	private String xlsName;
	private String[] cellNames;
	private Object[] cellNameLengths;

	// 每个表格的最大容量(文件分割数量)
	private Integer pageContent = 65534;
	//日期格式
	private String datePattern = "yyyy-MM-dd";
	
	private Workbook wb = null;
	private WritableWorkbook wwb = null;
	private WritableSheet ws = null;

	public XlsExportHelper(){
		
	}
	public XlsExportHelper(String path, String tempPath, Integer index,
			String xlsName, String[] cellNames, Object[] cellNameLengths) {
		this.path = path;
		this.tempPath = tempPath;
		this.index = index;
		this.xlsName = xlsName;
		this.cellNameLengths = cellNameLengths;
		this.cellNames = cellNames;
	}

	/**
	 * 生成excel
	 * @author ZhouLihui
	 * @param title	标题
	 * @param columnNames	列名
	 * @param columnLengths	列长
	 * @param datas	二维数组数据
	 * @param os	输出流
	 * @throws Exception
	 */
	public void createExcel(String title, String[] columnNames, Object[] columnLengths ,Object[][] datas ,OutputStream os) throws Exception {		
		this.cellNames = columnNames;
		this.cellNameLengths = columnLengths;
		wwb = Workbook.createWorkbook(os);		
		if (datas.length <= pageContent) {
			ws = wwb.createSheet("sheet0", 0);
			setTableNameAndCellWidth(title);
			setColumnFormat(datas[0]);
			saveDatas(datas);
		} else {
			Object[] objects = splitDatas(datas);
			for (int i = 0; i < objects.length; i++) {
				ws = wwb.createSheet("sheet"+i, i);
				setTableNameAndCellWidth(title);
				setColumnFormat(datas[0]);
				saveDatas((Object[][]) objects[i]);
			}
		}
		wwb.write();
		wwb.close();
	}
	/**
	 * 保存数据
	 * @param datas 二维数组数据
	 * @return excel文件名
	 * @throws Exception
	 */
	public String save(Object[][] datas) throws Exception {
		String name;
		wb = Workbook.getWorkbook(new File(tempPath + "\\temp.xls"));
		if (index == -1) {
			name = xlsName;
		} else {
			name = xlsName + "_" + index;
		}
		wwb = Workbook.createWorkbook(new File(path + "\\" + name + ".xls"), wb);
		ws = wwb.getSheet(0);

		setTableNameAndCellWidth(name);

		setColumnFormat(datas[0]);

		saveDatas(datas);

		wwb.write();
		wwb.close();
		wb.close();

		return name + ".xls";
	}
	
	private void setTableNameAndCellWidth(String name) throws Exception {
		for (int i = 0; i < cellNames.length; i++) {
			Integer width = (Integer) cellNameLengths[i];
			ws.setColumnView(i, width);
			WritableCellFormat cellWcf = new WritableCellFormat();
			cellWcf.setBorder(jxl.format.Border.ALL,
					jxl.format.BorderLineStyle.THIN);
			cellWcf.setBackground(jxl.format.Colour.GRAY_25);
			cellWcf.setAlignment(jxl.format.Alignment.CENTRE);
			cellWcf.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			Label cellName = new Label(i, 1, cellNames[i], cellWcf);
			ws.addCell(cellName);
		}
		ws.setRowView(0, 500);
		ws.mergeCells(0, 0, cellNames.length - 1, 0);
		WritableFont headerWft = new WritableFont(
				WritableFont.createFont("宋体"), 20, WritableFont.BOLD, false);
		WritableCellFormat headerWcf = new WritableCellFormat(headerWft);
		headerWcf.setBorder(jxl.format.Border.ALL,
				jxl.format.BorderLineStyle.THIN);
		headerWcf.setAlignment(jxl.format.Alignment.CENTRE);
		headerWcf.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
		Label headName = new Label(0, 0, name, headerWcf);
		ws.addCell(headName);
	}
	
	private void saveDatas(Object[][] datas) throws Exception {
		//System.out.println(datas.length);
		Integer row = 2;
		for (int i = 0; i < datas.length; i++) {
			//System.out.println(i);
			for (int j = 0; j < datas[i].length; j++) {
				if (datas[i][j] instanceof Double) {
					ws.addCell(setDoubleFormat(row, j, (Double) datas[i][j]));
					continue;
				} else if (datas[i][j] instanceof java.util.Date) {
					ws.addCell(setDateFormat(row, j,
							(java.util.Date) datas[i][j]));
					continue;
				} else if (datas[i][j] instanceof Integer
						|| datas[i][j] instanceof String) {
					ws.addCell(setTextFormat(row, j, datas[i][j] == null ? "" : datas[i][j].toString()));
					continue;
				} else {
					ws.addCell(setTextFormat(row, j, datas[i][j] == null ? "" : datas[i][j].toString()));
					continue;
				}
			}
			row++;
		}
	}

	private void setColumnFormat(Object[] columsDatas) throws Exception {
		for (int i = 0; i < columsDatas.length; i++) {
			Integer width = (Integer) cellNameLengths[i];
			CellView cv = new CellView();
			if (columsDatas[i] instanceof java.util.Date) {
				DateFormat dateFormat = new DateFormat(datePattern);
				WritableCellFormat cellWcf = new WritableCellFormat(dateFormat);
				cellWcf.setBorder(jxl.format.Border.ALL,
						jxl.format.BorderLineStyle.THIN);
				cellWcf.setAlignment(jxl.format.Alignment.CENTRE);
				cellWcf.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
				cv.setFormat(cellWcf);
				cv.setSize(width * 265);
				ws.setColumnView(i, cv);

				continue;
			}
			WritableCellFormat cellWcf = new WritableCellFormat(
					NumberFormats.TEXT);
			cellWcf.setBorder(jxl.format.Border.ALL,
					jxl.format.BorderLineStyle.THIN);
			cellWcf.setAlignment(jxl.format.Alignment.CENTRE);
			cellWcf.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			cellWcf.setWrap(true);// 可换行的label样式

			cv.setFormat(cellWcf);
			cv.setSize(width * 265);
			ws.setColumnView(i, cv);
		}
	}
	
	private Label setDoubleFormat(Integer row, Integer cell, Double data)
			throws Exception {
		DecimalFormat df = new DecimalFormat("0.00");
		Label dataLabel = new Label(cell, row, df.format(data));
		return dataLabel;
	}

	private Label setDateFormat(Integer row, Integer cell, java.util.Date data)
			throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat(datePattern);
		String time = sdf.format(data);
		Label dataLabel = new Label(cell, row, time);
		return dataLabel;
	}

	private Label setTextFormat(Integer row, Integer cell, String data)
			throws Exception {
		Label dataLabel = new Label(cell, row, data.toString());
		return dataLabel;
	}
	
	/**
	 * 分割大批量二维数组
	 * 
	 * @param datas	需要分割的数组
	 * @return Object[] 分割后的二维数组，长度：分割的数组
	 * @throws Exception
	 */
	private Object[] splitDatas(Object[][] datas) throws Exception {
		if (datas.length <= pageContent) {
			Object[] object = { datas };
			return object;
		} else {
			Integer fileNumber = datas.length / pageContent;
			System.out.println(fileNumber);
			Object[] object = new Object[fileNumber + 1];
			for (int i = 0; i <= fileNumber; i++) {
				if (i < fileNumber) {
					Object[][] newDatas = new Object[pageContent][datas[0].length];
					Integer begin = i * pageContent;
					Integer end = (i + 1) * pageContent;
					Integer j = 0;
					for (int a = begin; a < end; a++) {
						for (int b = 0; b < datas[a].length; b++) {
							newDatas[j][b] = datas[a][b];
						}
						j++;
					}
					object[i] = newDatas;
					continue;
				} else if (i == fileNumber) {
					Integer begin = i * pageContent;
					Integer end = datas.length;
					Object[][] newDatas = new Object[end - begin][datas[0].length];
					Integer j = 0;
					for (int a = begin; a < end; a++) {
						for (int b = 0; b < datas[a].length; b++) {
							newDatas[j][b] = datas[a][b];
						}
						j++;
					}
					object[i] = newDatas;
					continue;
				}
			}
			return object;
		}
	}
	
	public String getDatePattern() {
		return datePattern;
	}
	public void setDatePattern(String datePattern) {
		this.datePattern = datePattern;
	}
}
