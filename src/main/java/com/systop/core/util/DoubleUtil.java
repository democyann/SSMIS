package com.systop.core.util;

import org.apache.commons.lang.StringUtils;

/**
 * DoubleUtil
 * @author SongBaoJie
 */
public final class DoubleUtil {
	/**
	 * 将字符串转换为Float型数据格式
	 * 
	 * @param str
	 * @return
	 */
	public static Double parseDouble(String str) {
		Double param = null;
		if (StringUtils.isBlank(str)) {
			return null;
		}
		try {
			if (StringUtils.isNotBlank(str)) {
				str = str.replaceAll(",", "");
				param = Double.valueOf(str);
			}
		} catch (Exception e) {
			return null;
		}
		return param;
	}
}
