package com.systop.common.modules.security.user.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.systop.common.modules.security.user.model.Permission;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.Constants;
import com.systop.core.dao.hibernate.BaseHibernateDao;

/**
 * User Data Access Object。
 * 
 * @author Sam Lee
 * 
 */
@Repository
public class UserDao extends BaseHibernateDao {
	/**
	 * 获得具有某一<code>Permission</code>的用户
	 * 
	 * @param perm
	 *            给定<code>Permission</code>
	 * @return List of User or empty list.
	 */
	@SuppressWarnings("unchecked")
	public List<User> findUsersByPermission(Permission perm) {
		String hql = "select user from Permission permi "
				+ "inner join permi.roles as role "
				+ "inner join role.users as user where permi.status='"
				+ Constants.STATUS_AVAILABLE + "' and permi=?";

		return query(hql, perm);
	}

	/**
	 * 返回给定用户<code>User</code>所具有的权限（<code>Permission</code>）
	 * 
	 * @param user
	 *            给定用户
	 * @return List of User or empty list.
	 */

	@SuppressWarnings("unchecked")
	public List<Permission> findPermissionsByUser(User user) {
		String hql = "select permi from Permission permi"
				+ " inner join permi.roles as role "
				+ " inner join role.users as user where permi.status='"
				+ Constants.STATUS_AVAILABLE + "' and user=?";

		return query(hql, user);
	}
}
