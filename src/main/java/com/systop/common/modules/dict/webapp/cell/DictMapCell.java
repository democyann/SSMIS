package com.systop.common.modules.dict.webapp.cell;

import org.ecside.core.TableModel;
import org.ecside.core.bean.Column;
import org.ecside.table.cell.AbstractCell;

import com.systop.common.modules.dict.DictConstants;

/** 
 * @ClassName: DictMap 
 * @Description:字典表index.jsp页面前台转换
 * @author 乔轩 
 * @date 2013-8-30 下午04:30:03 
 *  
 */

public class DictMapCell extends AbstractCell {

	@Override
	protected String getCellValue(TableModel model, Column column) {
		return DictConstants.DICT_MAP.get(column.getValueAsString());
	}

}
