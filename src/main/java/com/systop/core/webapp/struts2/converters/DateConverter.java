package com.systop.core.webapp.struts2.converters;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.conversion.impl.DefaultTypeConverter;
import com.systop.core.util.DateUtil;

public class DateConverter extends DefaultTypeConverter {
	private static Logger logger = LoggerFactory.getLogger(DateConverter.class);

	@SuppressWarnings("rawtypes")
	@Override
	public Object convertValue(Map context, Object val, Class toType) {
		logger.debug("Call custom Converter for {}", toType);
		logger.debug("####Value:{}", val);
		if (val == null) {
			return null;
		}
		Object value = null; // 被转换的value
		if (val.getClass().isArray()) { // 因为value是Array，所以需要取出第一个
			if (((Object[]) val).length > 0) {
				value = ((Object[]) val)[0];
			}
		} else {
			value = val;
		}
		// 挨个转换，哪个能成功就返回哪个
		String[] masks = new String[] { "yyyy", "yyyy-MM", "yyyy-MM-dd",
				"yyyy-MM-dd HH", "yyyy-MM-dd HH:mm", "yyyy-MM-dd HH:mm:ss" };
		// 转换String到Date
		if (Date.class == toType
				&& (value instanceof String && value != null && ((String) value)
						.length() > 0)) {

			for (int i = masks.length - 1; i >= 0; i--) {
				try {
					Date date = DateUtil.convertStringToDate(masks[i],
							value.toString());
					logger.debug("convert to {}",
							DateUtil.convertDateToString(date));
					return date;
				} catch (ParseException ignore) {
				}
			}
		} // 转换Date到String
		else if (toType == String.class && value instanceof Date) {
			String dateStr = DateUtil.getDateTime("yyyy-MM-dd HH:mm:ss",
					(Date) value);

			if (dateStr.endsWith(" 00:00:00")) {
				dateStr = dateStr
						.substring(0, dateStr.lastIndexOf(" 00:00:00"));
			}
			if (dateStr.endsWith(" 00:00")) {
				dateStr = dateStr.substring(0, dateStr.lastIndexOf(" 00:00"));
			}
			if (dateStr.endsWith(" 00")) {
				dateStr = dateStr.substring(0, dateStr.lastIndexOf(" 00"));
			}
			logger.debug(" convert date to str {}", dateStr);
			return dateStr;
		}
		return null;

	}

	public static final void main(String[] args) {
		try {
			Date d = DateUtil.convertStringToDate("yyyy-MM-dd", "2009-08-08");
			System.out.println(d);
			String dateStr = DateUtil.getDateTime("yyyy-MM", d);
			System.out.println(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

}
