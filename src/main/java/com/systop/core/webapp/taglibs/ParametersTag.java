package com.systop.core.webapp.taglibs;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Set;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import com.systop.core.util.StringUtil;

/**
 * 用于将ServletRequest对象中的parameters以
 * 
 * <pre>
 * &lt;input type="hidden"...
 * </pre>
 * 
 * 或
 * 
 * <pre>
 * key1=value1&key2=...
 * </pre>
 * 
 * 的形式输出在页面上。通常用于保留本次参数以便于下一次使用。 于struts2的chaining action结合可以实现跨请求的action chain.
 * 
 * @author Sam Lee
 * 
 */
@SuppressWarnings("serial")
public class ParametersTag extends BodyTagSupport {
	private static Logger logger = LoggerFactory.getLogger(ParametersTag.class);
	/**
	 * 输出为input
	 */
	public static final String TYPE_INPUT_TAG = "inputTag";
	/**
	 * 输出为query string
	 */
	public static final String TYPE_QUERY_STRING = "queryString";

	/**
	 * 指定包含的parameters，如果没有指定，则包含全部。可以使用*、?作为通配符。
	 */
	private String includes;
	/**
	 * @see #TYPE_INPUT_TAG
	 * @see #TYPE_QUERY_STRING
	 */
	private String type;

	/**
	 * @see javax.servlet.jsp.tagext.BodyTagSupport#release()
	 */
	@Override
	public void release() {
		includes = null;
		type = null;
		super.release();
	}

	/**
	 * @see javax.servlet.jsp.tagext.BodyTagSupport#doEndTag()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int doEndTag() throws JspException {
		Enumeration<String> keys = pageContext.getRequest().getParameterNames();

		String str = "";

		if (StringUtils.isBlank(type)
				|| StringUtils.equals(TYPE_INPUT_TAG, type)) {
			logger.debug("Build Input Tags.");
			str = buildInputTags(keys);
		} else {
			logger.debug("Build query string.");
			str = buildQueryString(keys);
		}
		try {
			pageContext.getOut().write(str);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return BodyTagSupport.EVAL_PAGE;
	}

	/**
	 * 根据参数构造queryString
	 * 
	 * @param keys
	 *            参数名称
	 */
	private String buildQueryString(Enumeration<String> keys) {
		StringBuffer buf = new StringBuffer();
		while (keys.hasMoreElements()) {
			String key = keys.nextElement();
			logger.debug("Build parameter {}", key);
			String value = pageContext.getRequest().getParameter(key);
			if (StringUtils.isBlank(value)) {
				continue;
			}
			buf.append(key).append("=").append(value);
			if (keys.hasMoreElements()) {
				buf.append("&");
			}
		}
		return buf.toString();
	}

	/**
	 * 根据参数构造Input
	 * 
	 * @param keys
	 *            参数名称
	 */
	private String buildInputTags(Enumeration<String> keys) {
		StringBuffer buf = new StringBuffer();
		while (keys.hasMoreElements()) {
			String key = keys.nextElement();
			if (!isInclude(key)) {
				continue;
			}
			String value = pageContext.getRequest().getParameter(key);
			buf
					.append("<input type='hidden' name='")
					.append(key)
					// .append("'")
					// .append(" id='")
					// .append(key)
					.append("' value='").append(
							(value == null) ? StringUtils.EMPTY : value)
					.append("'/>");
		}

		return buf.toString();
	}

	/**
	 * 判断某个参数是否可以包含。
	 */
	@SuppressWarnings("unchecked")
	private boolean isInclude(String target) {
		if (StringUtils.isBlank(includes)) {
			return true;
		}
		PathMatcher matcher = new AntPathMatcher();
		Set<String> incs = StringUtil.parseCommaSplitedString(includes);
		for (String inc : incs) {
			if (matcher.match(inc, target)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * @return the includes
	 */
	public String getIncludes() {
		return includes;
	}

	/**
	 * @param includes
	 *            the includes to set
	 */
	public void setIncludes(String includes) {
		this.includes = includes;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

}
