package com.systop.core.dao.support;

import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.lang.StringUtils;

@SuppressWarnings("rawtypes")
public class DaoHelper {
	/**
	 * 根据Collection中的数据，构造一个in条件。例如，'a','b'或102,103
	 * 
	 * @param c
	 *            给定Collection
	 * @return 返回IN查询字句或EMPTY String
	 */
	public static String buildInCriteria(Collection c) {
		if (c == null || c.isEmpty()) {
			return StringUtils.EMPTY;
		}

		int index = 0;
		StringBuffer buffer = new StringBuffer();
		for (Iterator itr = c.iterator(); itr.hasNext();) {
			index++; // 在最前面计数是为了判断的时候方便
			Object o = itr.next();
			// 忽略null
			if (o == null) {
				continue;
			}

			if (o instanceof Number) { // 数字类型不加'号
				buffer.append(o.toString()).append(
						(index == c.size()) ? "" : ",");
			} else {
				buffer.append("'").append(o.toString())
						.append((index == c.size()) ? "'" : "',");
			}
		}
		// 剔除最后一个","，仅在最后一个元素为null的情况下发生
		if (buffer.toString().endsWith(",")) {
			buffer.deleteCharAt(buffer.lastIndexOf(","));
		}
		return buffer.toString();
	}

	private DaoHelper() {

	}
}
