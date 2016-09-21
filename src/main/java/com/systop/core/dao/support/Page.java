package com.systop.core.dao.support;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.struts2.json.annotations.JSON;

import com.systop.core.Constants;

/**
 * 分页对象.包含数据及分页信息.
 * 
 * @author Sam
 */

@SuppressWarnings("rawtypes")
public final class Page implements Serializable {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 231152607479172128L;

	/**
	 * 第一页的索引
	 */
	public static final int FIRST_PAGE_INDEX = 1;

	/**
	 * 空Page对象.
	 */
	public static final Page EMPTY_PAGE = new Page();
	/**
	 * 没有查询到任何数据或不需要自动执行Count操作时，<code>rows</code>的缺省值。
	 */
	public static final int NO_ROWS_FOUND = 0;

	/**
	 * 当前页第一条数据的位置,从0开始
	 */
	private int startIndex;

	/**
	 * 是否自动执行Count查询
	 */
	private boolean autoCount = true;

	/**
	 * 每页的记录数
	 */
	private int pageSize = Constants.DEFAULT_PAGE_SIZE;

	/**
	 * 当前页中存放的记录
	 */
	private List data;

	/**
	 * 总记录数
	 */
	private int rows = NO_ROWS_FOUND;

	/**
	 * 构造方法，只构造空页
	 */
	public Page() {
		this(0, 0, Constants.DEFAULT_PAGE_SIZE, Collections.EMPTY_LIST);
	}

	/**
	 * 构造方法，通常用于执行分页查询前构建分页信息。常见的用法：<br>
	 * 
	 * <pre>
	 * pageQuery(new Page(0, 20, false), &quot;from X&quot;);
	 * pageQuery(new Page(Page.start(pageNo, 20), 20, false), &quot;from X&quot;);
	 * </pre>
	 * 
	 * @param startIndex
	 *            起始行索引，从0开始。
	 * @param pageSize
	 *            本页容量。
	 * @param autoCount
	 *            是否自动执行count查询。
	 * @see {@link com.systop.core.dao.hibernate.BaseHibernateDao#pagedQuery(Page, String, Object...)}
	 */
	public Page(int startIndex, int pageSize, boolean autoCount) {
		this.pageSize = pageSize;
		this.startIndex = startIndex;
		this.autoCount = autoCount;
	}

	/**
	 * 简化的构造方法，相当于{@link #Page(int, int, boolean)}第三个参数为true。
	 * 
	 * @see {@link #Page(int, int, boolean)}
	 */
	public Page(int startIndex, int pageSize) {
		this(startIndex, pageSize, true);
	}

	/**
	 * 默认构造方法
	 * 
	 * @param startIndex
	 *            本页数据在数据库中的起始位置
	 * @param rows
	 *            数据库中总记录条数
	 * @param pageSize
	 *            本页容量
	 * @param data
	 *            本页包含的数据
	 */
	public Page(int startIndex, int rows, int pageSize, List data) {
		this.pageSize = pageSize;
		this.startIndex = startIndex;
		this.rows = rows;
		this.data = data;
	}

	/**
	 * 取数据库中包含的总记录数
	 */
	@JSON(name = "totalProperty")
	public int getRows() {
		return this.rows;
	}

	/**
	 * 取总页数
	 */
	@JSON(serialize = false)
	public int getPages() {
		if (rows % pageSize == 0) {
			return rows / pageSize;
		} else {
			return rows / pageSize + 1;
		}
	}

	/**
	 * @return the autoCount
	 */
	@JSON(serialize = false)
	public boolean getAutoCount() {
		return autoCount;
	}

	/**
	 * 取每页数据容量
	 */
	@JSON(serialize = false)
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * 获取当前页码,如果查询结果为空(Empty List),则总是返回1
	 * 
	 * @return 第一页是1，第二页是2...
	 */
	@JSON(serialize = false)
	public int getPageNo() {
		return (startIndex / pageSize) + 1;
	}

	/**
	 * 是否有下一页
	 */
	@JSON(serialize = false)
	public boolean getHasNextPage() {
		return this.getPageNo() < this.getPages();
	}

	/**
	 * 是否有上一页
	 */
	@JSON(serialize = false)
	public boolean getHasPreviousPage() {
		return (this.getPageNo() > 1);
	}

	/**
	 * 获取任一页第一条数据的位置,startIndex从0开始
	 */
	public static int start(int pageNo, int pageSize) {
		return (pageNo - 1) * pageSize;
	}

	/**
	 * @return the data
	 */
	@JSON(name = "root")
	public List getData() {
		return data;
	}

	public void setData(List data) {
		this.data = data;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.MULTI_LINE_STYLE);
	}

	/**
	 * @param rows
	 *            the rows to set
	 */
	public void setRows(int rows) {
		this.rows = rows;
	}

	/**
	 * @return the startIndex
	 */
	public int getStartIndex() {
		return startIndex;
	}
}
