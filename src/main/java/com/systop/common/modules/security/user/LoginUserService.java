package com.systop.common.modules.security.user;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.systop.common.modules.dept.model.Dept;
import com.systop.common.modules.security.user.model.Role;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.dao.hibernate.BaseHibernateDao;

/**
 * 获取当前登录用户的一些信息
 * 
 * @author catstiger@gmail.com
 * 
 */
@Component
public class LoginUserService {
	@Autowired
	@Qualifier("baseHibernateDao")
	private BaseHibernateDao dao;

	/**
	 * 返回当前登录用户
	 */
	public User getLoginUser(HttpServletRequest request) {
		return UserUtil.getPrincipal(request);
	}

	/**
	 * 返回当前登录用户所在部门
	 */
	public Dept getLoginUserDept(HttpServletRequest request) {
		User user = getLoginUser(request);
		if (user == null) {
			return null;
		}
		user = dao.get(User.class, user.getId());
		Dept dept = user.getDept();
		return dept;
	}


	/**
	 *得到当前登录用户所在部门的路径
	 */
	public String getLoginUserPath(HttpServletRequest request) {
		Dept dept = getLoginUserDept(request);
		if (dept == null) {
			return null;
		}
		StringBuffer buf = new StringBuffer(200).append(dept.getName());
		while (dept.getParentDept() != null) {
			dept = dept.getParentDept();
			buf.insert(0, "\\").insert(0, dept.getName());
		}
		return buf.toString();
	}

	/**
	 * 判断当前登陆用户是否具有某个角色
	 */
	@SuppressWarnings("unchecked")
	public boolean isGranted(HttpServletRequest request, String roleName) {
		User user = getLoginUser(request);
		List<Role> roleList = dao.query("select r from Role r join r.users u "
				+ "where u.id=?", user.getId());
		for (Role role : roleList) {
			if (StringUtils.equals(role.getName(), roleName)) {
				return true;
			}
		}
		return false;
	}
}
