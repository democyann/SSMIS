package com.systop.core.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import jxl.Sheet;

import org.apache.commons.lang.StringUtils;

import com.systop.core.util.ReflectUtil;

/**
 * 将excel的字段属性付给对象
 * 
 * @author Sam
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public final class XlsImportHelper {
	/**
	 * 用于反射的map
	 */
	private Map properties = new HashMap();

	public void setProperties(Map properties) {
		this.properties = properties;
	}

	/**
	 * 将excel的字段属性付给对象
	 * 
	 * @param targetObject
	 *            反射的对象
	 * @param sheet
	 *            excel数据源
	 * @param rowIndex
	 *            数据行数
	 */
	public void importCommonProperties(Object targetObject, Sheet sheet,
			int rowIndex) {
		Set<String> keySet = properties.keySet();
		for (String key : keySet) {
			Converter c = (Converter) properties.get(key);
			String cellValue = sheet.getCell(c.getColumnIndex(), rowIndex)
					.getContents();
			ReflectUtil.set(targetObject, key, c.getValue(cellValue));
		}
	}

	/**
	 * 将excel的字段属性付给对象
	 * 
	 * @param map
	 * @param sheet
	 * @param rowIndex
	 */
	public void importCommonProperties(Map map, Sheet sheet, int rowIndex) {
		Set<String> keySet = properties.keySet();
		for (String key : keySet) {
			Converter c = (Converter) properties.get(key);
			if (c.getColumnIndex() != -1) { // 导入列为-1时表示此列不进行导入
				// 导入列超出总列范围不导入
				if ((sheet.getColumns() - 1) >= c.getColumnIndex()) {
					String val = sheet.getCell(c.getColumnIndex(), rowIndex)
							.getContents();
					// 值不为空时
					if (StringUtils.isNotBlank(val)) {
						// 去除字符两边空格
						map.put(key, val.trim());
					}
				}
			}
		}
	}

	/**
	 * 返回字段如('name',20,'sex')
	 */
	public String getMeta() {
		StringBuffer meta = new StringBuffer();
		Set<String> keySet = properties.keySet();
		meta.append("(");
		for (String key : keySet) {
			meta.append(key);
			meta.append(",");
		}
		// 剔除最后一个","，仅在最后一个元素为null的情况下发生
		if (meta.toString().endsWith(",")) {
			meta.deleteCharAt(meta.lastIndexOf(","));
		}
		meta.append(")");
		return meta.toString();
	}

	/**
	 * 构造器
	 */
	public XlsImportHelper() {

	}

	/**
	 * 建立一个转化接口Converter
	 * 
	 * @author sam
	 */
	public static interface Converter {
		/**
		 * 
		 * @param cellValue
		 * @return 返回转换后的值
		 */
		public Object getValue(String cellValue);

		/**
		 * 对应Excle的列
		 */
		public Integer getColumnIndex();
	}

	/**
	 * 将对应的Excle列转换成String格式
	 * 
	 * @author sam
	 */
	public static class StringConverter implements Converter {
		/**
		 * 对应Excle的列
		 */
		private Integer colIndex;

		/**
		 * 构造器
		 * 
		 * @param columnIndex
		 */
		public StringConverter(int columnIndex) {
			colIndex = columnIndex;
		}

		public Integer getColumnIndex() {
			return colIndex;
		}

		/**
		 * 返回转换后的String
		 */
		public Object getValue(String cellValue) {
			return cellValue;
		}

	}

	/**
	 * 将对应的Excle列转换成Integer格式
	 * 
	 * @author sam
	 */
	public static class IntegerConverter implements Converter {
		/**
		 * 对应Excle的列
		 */
		private Integer colIndex;

		/**
		 * 构造器
		 * 
		 * @param columnIndex
		 */
		public IntegerConverter(int columnIndex) {
			colIndex = columnIndex;
		}

		public Integer getColumnIndex() {
			return colIndex;
		}

		/**
		 * 返回转换后的Integer
		 */
		public Object getValue(String cellValue) {
			try {
				return Integer.valueOf(cellValue);
			} catch (NumberFormatException e) {
				return null;
			}
		}
	}

	/**
	 * 将对应的Excle列转换成Double格式
	 * 
	 * @author sam
	 */
	public static class DoubleConverter implements Converter {
		/**
		 * 对应Excle的列
		 */
		private Integer colIndex;

		/**
		 * 构造器
		 * 
		 * @param columnIndex
		 */
		public DoubleConverter(int columnIndex) {
			colIndex = columnIndex;
		}

		public Integer getColumnIndex() {
			return colIndex;
		}

		/**
		 * 返回转换后的Integer
		 */
		public Object getValue(String cellValue) {
			try {
				return Double.valueOf(cellValue);
			} catch (NumberFormatException e) {
				return null;
			}
		}
	}

	/**
	 * 将对应的Excle列转换成Date格式
	 * 
	 * @author sam
	 */
	public static class DateTimeConverter implements Converter {
		/**
		 * 对应的Excel列
		 */
		private Integer colIndex;

		/**
		 * 构造器
		 * 
		 * @param columnIndex
		 */
		public DateTimeConverter(int columnIndex) {
			colIndex = columnIndex;
		}

		public Integer getColumnIndex() {
			return colIndex;
		}

		/**
		 * 返回转换后的日期格式如果格式错误则默认为""
		 */
		@SuppressWarnings("null")
		public Object getValue(String cellValue) {
			if (cellValue == null && cellValue.length() == 0) {
				return "";
			}

			cellValue = cellValue.replace("/", "-");
			return cellValue;
		}
	}

	/**
	 * 判断上传得文件是否是Excel文件，是返回true；否返回false
	 */
	public static boolean isAllowedXls(String dataName) {
		String postfix = dataName.substring(dataName.lastIndexOf(".")); // 获得后缀
		if (postfix.equals(".xls")) {
			return true;
		}
		return false;
	}
}
