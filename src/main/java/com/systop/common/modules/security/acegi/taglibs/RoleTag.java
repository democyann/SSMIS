package com.systop.common.modules.security.acegi.taglibs;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.util.ExpressionEvaluationUtils;

import com.systop.common.modules.security.user.UserUtil;
import com.systop.common.modules.security.user.dao.UserDao;
import com.systop.common.modules.security.user.model.Role;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.util.StringUtil;

/**
 * 用于判断角色的Taglib。Aceig提供了类似的Tag，它的是用于判断权限的。本系统中角色相当于 权限组，根据角色判断更加简单。
 * 
 * @author Sam Lee
 * 
 */

@SuppressWarnings({ "serial", "unchecked", "rawtypes" })
public class RoleTag extends BodyTagSupport {
	/**
	 * 如果当前用户拥有列表中的全部角色，则条件成立
	 */
	private String ifAllGranted = "";
	/**
	 * 如果当前用户拥有列表中的某一个角色，则条件成立
	 */
	private String ifAnyGranted = "";
	/**
	 * 如果当前用户不拥有列表中的任何角色，则条件成立
	 */
	private String ifNotGranted = "";

	/**
	 * @return the ifAllGranted
	 */
	public String getIfAllGranted() {
		return ifAllGranted;
	}

	/**
	 * @param ifAllGranted
	 *            the ifAllGranted to set
	 */
	public void setIfAllGranted(String ifAllGranted) {
		this.ifAllGranted = ifAllGranted;
	}

	/**
	 * @return the ifAnyGranted
	 */
	public String getIfAnyGranted() {
		return ifAnyGranted;
	}

	/**
	 * @param ifAnyGranted
	 *            the ifAnyGranted to set
	 */
	public void setIfAnyGranted(String ifAnyGranted) {
		this.ifAnyGranted = ifAnyGranted;
	}

	/**
	 * @return the ifNotGranted
	 */
	public String getIfNotGranted() {
		return ifNotGranted;
	}

	/**
	 * @param ifNotGranted
	 *            the ifNotGranted to set
	 */
	public void setIfNotGranted(String ifNotGranted) {
		this.ifNotGranted = ifNotGranted;
	}

	/**
	 * @see javax.servlet.jsp.tagext.BodyTagSupport#doStartTag()
	 */
	@Override
	public int doStartTag() throws JspException {
		if (!hasRoleList()) {
			return SKIP_BODY;
		}

		final Set granted = getPrincipalRoles();
		// 处理ifNotGranted的情况
		final String evaledIfNotGranted = ExpressionEvaluationUtils
				.evaluateString("ifNotGranted", ifNotGranted, pageContext);
		if ((null != evaledIfNotGranted) && !"".equals(evaledIfNotGranted)) {
			granted.retainAll(StringUtil
					.parseCommaSplitedString(evaledIfNotGranted));

			if (!granted.isEmpty()) {
				return SKIP_BODY;
			}
		}
		// 处理ifAllGranted
		final String evaledIfAllGranted = ExpressionEvaluationUtils
				.evaluateString("ifAllGranted", ifAllGranted, pageContext);

		if ((null != evaledIfAllGranted) && !"".equals(evaledIfAllGranted)) {
			if (!granted.containsAll(StringUtil
					.parseCommaSplitedString(evaledIfAllGranted))) {
				return SKIP_BODY;
			}
		}
		// 处理ifAnyGranted
		final String evaledIfAnyGranted = ExpressionEvaluationUtils
				.evaluateString("ifAnyGranted", ifAnyGranted, pageContext);

		if ((null != evaledIfAnyGranted) && !"".equals(evaledIfAnyGranted)) {
			granted.retainAll(StringUtil
					.parseCommaSplitedString(evaledIfAnyGranted));

			if (granted.isEmpty()) {
				return SKIP_BODY;
			}
		}

		return EVAL_BODY_INCLUDE;
	}

	/**
	 * 是否给出了Role名称列表
	 */
	private boolean hasRoleList() {
		if (((null == ifAllGranted) || "".equals(ifAllGranted))
				&& ((null == ifAnyGranted) || "".equals(ifAnyGranted))
				&& ((null == ifNotGranted) || "".equals(ifNotGranted))) {
			return false;
		}

		return true;
	}

	/**
	 * 返回用户所具有的角色名称
	 * 
	 * @return
	 */
	private Set getPrincipalRoles() {
		User user = UserUtil.getPrincipal((HttpServletRequest) pageContext
				.getRequest());
		if (user == null) {
			return Collections.EMPTY_SET;
		}

		Collection<Role> roles = loadRoles(user);

		if (roles == null || roles.size() == 0) {
			return Collections.EMPTY_SET;
		}

		Set set = new HashSet(roles.size());
		for (Role role : roles) {
			set.add(role.getName());
		}
		return set;
	}

	/**
	 * 加载用户的角色，处理延迟加载问题
	 */
	private Collection<Role> loadRoles(User user) {
		Set<Role> roles = new HashSet();

		ApplicationContext ctx = WebApplicationContextUtils
				.getWebApplicationContext(pageContext.getServletContext());
		UserDao userDao = (UserDao) ctx.getBean("userDao");
		List<Role> roleList = userDao.query(
				"select r from Role r join r.users u " + "where u.id=?",
				user.getId());
		for (Role role : roleList) {
			roles.add(role);
		}

		return roles;
	}

	/**
	 * 解析以comma分隔的字符串，并去掉其中不必要的字符，最后以Set返回
	 * 
	 * @deprecated use StringUtil.parseCommaSplitedString(String)
	 */

	@SuppressWarnings("unused")
	private Set parseRolesString(String roleString) {
		final Set requiredAuthorities = new HashSet();
		final String[] authorities = StringUtils
				.commaDelimitedListToStringArray(roleString);

		for (int i = 0; i < authorities.length; i++) {
			String authority = authorities[i];

			// Remove the role's whitespace characters without depending on JDK
			// 1.4+
			// Includes space, tab, new line, carriage return and form feed.
			String role = authority.trim(); // trim, don't use spaces, as per
											// SEC-378
			role = StringUtils.replace(role, "\t", "");
			role = StringUtils.replace(role, "\r", "");
			role = StringUtils.replace(role, "\n", "");
			role = StringUtils.replace(role, "\f", "");

			requiredAuthorities.add(role);
		}

		return requiredAuthorities;
	}

	/**
	 * @see javax.servlet.jsp.tagext.BodyTagSupport#release()
	 */
	@Override
	public void release() {
		ifAllGranted = "";
		ifAnyGranted = "";
		ifNotGranted = "";
		super.release();
	}

}
