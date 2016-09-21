package com.systop.core.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 * Hibernate的工具类
 * 
 * @author Sam Lee
 * 
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public final class HibernateUtil {
	/**
	 * 为DetachedCriteria对象添加一个Order条件，setupSort方法主要
	 * 用于配合dojo.ex.FilteringTableEx的排序需要
	 * 
	 * @param criteria
	 *            给定的DetachedCriteria对象
	 * @param sort
	 *            排序字段,第一个元素为排序字段名称，第二个为排序方向（asc,desc）
	 * @return 加入排序条件之后的DetachedCriteria对象
	 */
	public static DetachedCriteria setupSort(DetachedCriteria criteria,
			String[] sort) {
		if (sort == null || sort.length != 2) {
			return criteria;
		}

		if (!StringUtils.isBlank(sort[0])) {
			if (sort[0].indexOf(".") > 0) {
				sort[0] = sort[0].substring(0, sort[0].indexOf("."));
			}
			if ("asc".equals(sort[1])) {
				criteria.addOrder(Order.asc(sort[0]));
			} else if ("desc".equals(sort[1])) {
				criteria.addOrder(Order.desc(sort[0]));
			}
		}
		return criteria;
	}

	/**
	 * 简单的根据<code>filter</code>参数中的属性来设置<code>DetachedCriteria</code>对象 适用于
	 * <code>filter</code>中的key可以正好对应Entity的属性名称，并且查询条件都是eq的情况。
	 * 
	 * @param criteria
	 *            给定的<code>DetachedCriteria</code>对象
	 * @param filter
	 *            给定的查询条件,如果其中一个条件开头以%，则表示使用like查询
	 * @param ignoredValue
	 *            如果查询条件中的value等于ignoredValue之一，则忽略
	 * @param keys
	 *            需要设置的filter中的keys，如果为null，则表示设置全部keys
	 */
	public static DetachedCriteria simpleFilter(DetachedCriteria criteria,
			Map filter, Object[] ignoredValue, String... keys) {
		if (filter == null || filter.isEmpty()) {
			return criteria;
		}
		// 如果没有设定keys，则取所有的keys
		if (keys == null || keys.length == 0) {
			keys = (String[]) filter.keySet().toArray(new String[] {});
		}

		for (String key : keys) {
			Object obj = filter.get(key);
			if (!isIgnored(obj, ignoredValue)) {
				if (obj != null) {
					if (obj instanceof String
							&& obj.toString().indexOf('%') == 0) {
						String value = obj.toString().substring(1);
						criteria.add(Restrictions.like(key, value,
								MatchMode.ANYWHERE));
					} else {
						criteria.add(Restrictions.eq(key, obj));
					}
				} else {
					criteria.add(Restrictions.isNull(key));
				}
			}
		}

		return criteria;
	}

	/**
	 * 判断一个value是否被忽略，如果value等于ignoredValue之一，则返回true;
	 */
	private static boolean isIgnored(Object value, Object[] ignoredValue) {
		if (ignoredValue == null || ignoredValue.length == 0) {
			return false;
		}

		for (int i = 0; i < ignoredValue.length; i++) {
			if (ignoredValue[i] == null) {
				if (value == null) {
					return true;
				}
			} else {
				if (ignoredValue[i].equals(value)) {
					return true;
				}
			}
		}
		return false;
	}

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

	/**
	 * 私有构造方法，防止实例化
	 */
	private HibernateUtil() {

	}

	/**
	 * Main
	 */

	public static void main(String[] args) {
		java.util.List l = new java.util.ArrayList();
		l.add("");
		l.add("xxx");
		l.add("xadf");
		l.add(342);
		l.add(43.23);
		l.add("daf");
		System.out.println(buildInCriteria(l));
		l.add(123.34);
		l.add(null);
		System.out.println(buildInCriteria(l));
	}
}
