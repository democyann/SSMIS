package com.systop.common.modules.security.acegi.cache;

import java.util.List;

import com.systop.common.modules.security.acegi.resourcedetails.ResourceDetails;
import com.systop.common.modules.security.user.model.Permission;
import com.systop.common.modules.security.user.model.Resource;
import com.systop.common.modules.security.user.model.Role;
import com.systop.common.modules.security.user.model.User;

/**
 * cache管理
 *
 * @author cac
 */
public interface AcegiCacheManager {


	/**
	 * 初始化userCache
	 */
	void initUserCache();

	/**
	 * 初始化resourceCache
	 */
	void initResourceCache();

	/**
	 * 刷新resourceCache
	 */
	void refreshResourceCache();

	/**
	 * 获取所有的url资源
	 */
	List<String> getUrlResStrings();

	/**
	 * 获取所有的Funtion资源
	 */
	List<String> getFunctions();

	/**
	 * 获取所有的Component资源
	 */
	List<String> getComponents();

	/**
	 * 根据资源串获取资源
	 */
	ResourceDetails getResourceFromCache(String resString);
  
  /**
   * 当修改用户的时候调用本方法，以同步缓存
   * @param user 修改后的用户,如果为<code>null</code>则表示从缓存删除
   * @param oldUsername 修改之前的用户名
   */
  void onUserChanged(User user, String oldUsername);
  /**
   * 当修改角色的时候，调用本方法，以同步缓存
   */
  void onRoleChanged(Role role);
  /**
   * 当角色被删除的时候，同步缓存中的用户和权限
   * @param role 被删除的角色
   */
  void beforeRoleRemoved(Role role);
  
  /**
   * 当修改权限的时候，调用本方法，以同步缓存.
   */
  void onPermissionChanged(Permission perm);
  /**
   * 权限被删除的时候，同步资源缓存和用户缓存
   * @param perm 被删除的权限
   */
  void beforePermissionRemoved(Permission perm);
  /**
   *  当修改资源的时候，调用本方法，以同步缓存
   * @param res 修改后的资源，如果为<code>null</code>则表示从缓存删除
   * @param oldResString 修改之前的资源string
   */
  void onResourceChanged(Resource res, String oldResString);
}
