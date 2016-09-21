package com.systop.common.modules.security.acegi.cache;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.providers.dao.UserCache;
import org.acegisecurity.userdetails.UserDetails;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.systop.common.modules.security.acegi.resourcedetails.ResourceDetails;
import com.systop.common.modules.security.user.dao.UserDao;
import com.systop.common.modules.security.user.model.Permission;
import com.systop.common.modules.security.user.model.Resource;
import com.systop.common.modules.security.user.model.Role;
import com.systop.common.modules.security.user.model.User;

/**
 * 
 * @author Sam
 * 
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
@Component
public class AcegiCacheManagerImpl implements AcegiCacheManager {
	/**
	 * Log for the class
	 */
	private static Logger logger = LoggerFactory
			.getLogger(AcegiCacheManagerImpl.class);

	/**
	 * Cache of <code>UserDetails</code>
	 */
	@Autowired
	private UserCache userCache;

	/**
	 * Cache of <code>ResourceDetails</code>
	 */
	@Autowired
	private ResourceCache resCache;

	/**
	 * User Manager
	 */
	@Autowired
	private UserDao userDao;

	/**
	 * 资源缓存初始化标记
	 */
	private boolean resourceCacheInitialized = false;

	/**
	 * 用户缓存初始化标记
	 */
	private boolean userCacheInitialized = false;

	/**
	 * @see AcegiCacheManager#onPermissionChanged(Permission)
	 */
	public void onPermissionChanged(Permission perm) {
		logger.debug("权限{}改变。", perm.getName());

		if (perm != null) {

			// 修改权限关联的用户
			List<User> users = userDao.findUsersByPermission(perm);
			for (User user : users) {
				this.doUserInCache(user);
			}
			// Reload, or it will cause a lazy loading error.
			perm = (Permission) userDao.getHibernateTemplate().load(
					Permission.class, perm.getId());
			// 修改权限关联的资源
			Set<Resource> resources = perm.getResources();
			if (resources != null) {
				for (Resource res : resources) {
					this.doResourceInCache(res);
				}
			}
		}
	}

	/**
	 * @see AcegiCacheManager#onRoleChanged(Role)
	 */
	public void onRoleChanged(Role role) {
		if (role == null) {
			return;
		}

		logger.debug("角色{}修改了.", role.getName());

		Set<User> users = role.getUsers();
		if (users != null && users.size() > 0) {
			for (User user : users) {
				this.doUserInCache(user);
			}
		}

		Set<Permission> perms = role.getPermissions();
		if (perms != null && perms.size() > 0) {
			for (Permission perm : perms) {
				this.onPermissionChanged(perm);
			}
		}
	}

	/**
	 * @see AcegiCacheManager#onUserChanged(UserDetails, java.lang.String)
	 */
	public void onUserChanged(User user, String oldUsername) {
		logger.debug("用户{}修改了.", oldUsername);
		UserDetails old = userCache.getUserFromCache(oldUsername);
		if (old != null) {
			userCache.removeUserFromCache(oldUsername);
		}

		if (user != null) {
			this.doUserInCache(user);
		}
		logger.debug("User changed {}", oldUsername);
	}

	/**
	 * 在缓存中放入一个<code>UserDetails</code>对象
	 * 
	 * @param user
	 *            <code>User</code>对象,需要存放User对应的GrantedAuthority
	 */
	private void doUserInCache(User user) {
		List<Permission> perms = userDao.findPermissionsByUser(user);
		// 虽然User实现了UserDetails接口，但是，也要手工传入GrantedAuthority[]
		// 因为User与Permission关联，所以必须转化Permission为GrantedAuthority
		user.setAuthorities(this.perms2Auths(perms));
		logger.debug("Authorities of {} is {}", user.getUsername(),
				user.getAuthorities());
		userCache.putUserInCache(user);
		logger.debug("Modify user in cache {}", user.getLoginId());
	}

	/**
	 * 将一批<code>Permission</code>对象，转化为一个<code>GrantedAuthority</code>数组
	 * 
	 * @param perms
	 *            给定一批<code>Permission</code>对象
	 * @return <code>GrantedAuthority</code>数组
	 */
	private GrantedAuthority[] perms2Auths(List<Permission> perms) {
		if (perms == null || perms.size() == 0) {
			return new GrantedAuthority[] {};
		}
		List auths = new ArrayList(perms.size());
		for (Permission perm : perms) {
			auths.add(perm.toGrantedAuthority());
		}

		return (GrantedAuthority[]) auths.toArray(new GrantedAuthority[0]);
	}

	/**
	 * @see AcegiCacheManager#onResourceChanged(ResourceDetails)
	 */
	public void onResourceChanged(Resource res, String oldResString) {
		if (res != null) {
			logger.debug("资源{}修改了。", res.getResString());
		}

		ResourceDetails old = resCache.getResourceFormCache(oldResString);
		if (old != null) {
			resCache.removeResourceFromCache(oldResString);
		}
		if (res != null) {
			this.doResourceInCache(res);
		}
	}

	/**
	 * 将Resource的Permission对象转化为GrantedAuthority，并存入缓存
	 */
	private void doResourceInCache(Resource res) {
		if (res == null) {
			return;
		}
		Set<Permission> perms = res.getPermissions();
		if (perms != null) {
			List auths = new ArrayList();
			for (Permission perm : perms) {
				auths.add(perm.toGrantedAuthority());
			}
			// Resource实现了ResourceDetails接口，但是需要手工将GrantedAuthority传入
			res.setAuthorities((GrantedAuthority[]) auths
					.toArray(new GrantedAuthority[0]));
		}
		resCache.putResourceInCache(res);
		logger.debug("Modify Resource in cache {}", res.getResString());
	}

	/**
	 * @see AcegiCacheManager#getAuthorityFromCache(java.lang.String)
	 */
	public ResourceDetails getResourceFromCache(String resString) {
		ResourceDetails resDetail = resCache.getResourceFormCache(resString);
		if (resDetail == null) {
			logger.info("Can't get resource form cache {}", resString);
		}
		return resDetail;
	}

	/**
	 * @see AcegiCacheManager#getComponents()
	 */
	public List getComponents() {
		throw new UnsupportedOperationException("unfinished.");
	}

	/**
	 * @see AcegiCacheManager#getFunctions()
	 */
	public List getFunctions() {
		return resCache
				.getResStringsFormCache(ResourceDetails.RES_TYPE_FUNCTION);
	}

	/**
	 * @see AcegiCacheManager#getUrlResStrings()
	 */
	public List getUrlResStrings() {
		return resCache.getResStringsFormCache(ResourceDetails.RES_TYPE_URL);
	}

	/**
	 * @see AcegiCacheManager#initResourceCache()
	 */
	public void initResourceCache() {
		if (!resourceCacheInitialized) {
			List<Resource> resources = userDao.get(Resource.class);
			if (resources != null) {
				for (Resource res : resources) {
					this.doResourceInCache(res);
				}
			}
			resourceCacheInitialized = true;
			logger.debug("Resource Cache initialized.");
		}
	}

	/**
	 * @see AcegiCacheManager#initUserCache()
	 */
	public void initUserCache() {
		if (!userCacheInitialized) {
			List<User> users = userDao.get(User.class);
			for (User user : users) {
				this.doUserInCache(user);
			}
			userCacheInitialized = true;
			logger.debug("User Cache initialized.");
		}
	}

	/**
	 * @see AcegiCacheManager#refreshResourceCache()
	 */
	public void refreshResourceCache() {

	}

	/**
	 * @see AcegiCacheManager#beforePermissionRemoved(Permission)
	 */
	public void beforePermissionRemoved(Permission perm) {
		// 找到Permission对应的User
		List<User> users = userDao.findUsersByPermission(perm);
		logger.debug("{} users have Permission {}" + users.size(),
				perm.getName());

		for (User user : users) {
			// 找到缓存中的User
			User uc = (User) userCache.getUserFromCache(user.getUsername());
			if (uc != null) {
				GrantedAuthority[] auths = this.removeAuthFromArray(
						uc.getAuthorities(), perm.getName());

				uc.setAuthorities(auths);
				userCache.putUserInCache(uc);
				logger.debug("Remove auth {} from User {}", perm.getName(),
						uc.getUsername());
			}
		}
		// 找到Permission对应的Resource
		Set<Resource> resources = perm.getResources();
		logger.debug("{} resources have Permission {}" + users.size(),
				perm.getName());
		if (resources != null) {
			for (Resource res : resources) {
				// 找到缓存中的Resource
				ResourceDetails rc = resCache.getResourceFormCache(res
						.getResString());
				if (rc != null) {
					// 删除缓存中Resource关联的一个权限
					GrantedAuthority[] auths = this.removeAuthFromArray(
							res.getAuthorities(), perm.getName());
					if (rc instanceof Resource) {
						((Resource) rc).setAuthorities(auths);
					}

					resCache.putResourceInCache(rc);
					logger.debug(
							"Remove auth {} from Resource {}" + perm.getName(),
							res.getResString());
				}
			}
		}
	}

	/**
	 * 按照指定名称，从<code>GrantedAuthority</code>数组中删除一个
	 * 
	 * @param auths
	 *            给定的<code>GrantedAuthority</code>数组
	 * @param name
	 *            给定名称
	 * @return 删除后的数组
	 */
	private GrantedAuthority[] removeAuthFromArray(GrantedAuthority[] auths,
			String name) {
		if (auths == null || auths.length == 0) {
			return auths;
		}
		List<GrantedAuthority> list = Arrays.asList(auths);
		int size = list.size();
		for (int i = 0; i < size; i++) {
			if (StringUtils.equals(list.get(i).getAuthority(), name)) {
				list.remove(i);
				break;
			}
		}

		return list.toArray(new GrantedAuthority[] {});
	}

	/**
	 * @see AcegiCacheManager#beforeRoleRemoved(Role)
	 */
	public void beforeRoleRemoved(Role role) {
		Set<User> users = role.getUsers();
		if (users == null || users.size() == 0) {
			return;
		}
		User[] userArray = users.toArray(new User[] {});
		for (int i = 0; i < userArray.length; i++) {
			userArray[i].getRoles().remove(role);
			role.getUsers().remove(userArray[i]);
			userDao.saveOrUpdate(userArray[i]);
			this.onUserChanged(userArray[i], userArray[i].getLoginId());
			logger.debug("Remove role {} from user {}", role.getName(),
					userArray[i].getLoginId());
		}
	}

}
