package com.systop.core.webapp.struts2.converters;

import java.util.Map;

import ognl.DefaultTypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * XWork 2.1.2在对Double、Float类型转换的时候，不能处理<code><=0</code> 的数字，这个BUG在这里： <a href=
 * 'http://jira.opensymphony.com/browse/XW-676'>http://jira.opensymphony.com/browse/XW-676</a
 * > 在下一个版本的XWork中，这个BUG将被修复。
 * 
 * @author Sam
 * 
 */
@SuppressWarnings("rawtypes")
public class DoubleFloatConverter extends DefaultTypeConverter {
	private static Logger logger = LoggerFactory
			.getLogger(DoubleFloatConverter.class);

	/**
	 * @see ognl.DefaultTypeConverter#convertValue(Map, Object, Class)
	 */
	@Override
	public Object convertValue(Map context, Object value, Class toType) {
		logger.debug("Call custom Converter for {}", toType);
		if (value == null) {
			return null;
		}
		Object result = null; // 转换结果
		Object val = null; // 被转换的value
		if (value.getClass().isArray()) { // 因为value是Array，所以需要取出第一个
			if (((Object[]) value).length > 0) {
				val = ((Object[]) value)[0];
			}
		} else {
			val = value;
		}
		// 开始转换
		if (val != null) {
			// String to Double
			if ((toType == Double.class) || (toType == Double.TYPE)) {
				try {
					result = Double.valueOf(val.toString());
				} catch (NumberFormatException e) {
					logger.warn(e.getMessage());
				}
			}
			// String to Float
			else if ((toType == Float.class) || (toType == Float.TYPE)) {
				try {
					result = Float.valueOf(val.toString());
				} catch (NumberFormatException e) {
					logger.warn(e.getMessage());
				}
			}
			// Double to String
			else if (toType == String.class && val.getClass() == Double.class) {
				result = ((Double) val).toString();
			}
			// Float to String
			else if (toType == String.class && val.getClass() == Float.class) {
				result = ((Float) val).toString();
			}
		}

		return result;
	}
}
