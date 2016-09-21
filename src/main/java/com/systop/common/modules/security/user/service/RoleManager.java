package com.systop.common.modules.security.user.service;

import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.systop.common.modules.security.acegi.cache.AcegiCacheManager;
import com.systop.common.modules.security.user.model.Permission;
import com.systop.common.modules.security.user.model.Role;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.ApplicationException;
import com.systop.core.Constants;
import com.systop.core.service.BaseGenericsManager;

/**
 * 角色管理
 * @author sam
 * 
 */
@Service
@SuppressWarnings({"unchecked", "rawtypes"})
public class RoleManager extends BaseGenericsManager<Role> {

  /**
   * <code>AcegiCacheManager</code>用于同步缓存
   */
  private AcegiCacheManager acegiCacheManager;

  /**
   * @see RoleManager#getRolesByUser(User)
   */
  public Role[] getRolesByUser(User user) {
    Set roleSet = user.getRoles();
    Role[] retRoles = new Role[roleSet.size()];
    roleSet.toArray(retRoles);
    return retRoles;
  }

  /**
   * 
   */
  @Override
  @Transactional
  public void save(Role role) {
    Assert.notNull(role);
    if (!getDao().exists(role, "name")) {
      getDao().saveOrUpdate(role);
      if (acegiCacheManager != null) {
        acegiCacheManager.onRoleChanged(role);
      }
    } else {
      throw new ApplicationException("角色名称'" + role.getName() + "'已经存在.");
    }
  }

  /**
   * @return the acegiCacheManager
   */
  public AcegiCacheManager getAcegiCacheManager() {
    return acegiCacheManager;
  }

  /**
   * @see com.systop.common.service.BaseManager#remove(java.lang.Object)
   */
  @Override
  @Transactional
  public void remove(Role role) {
    Assert.notNull(role, "Role to delete must not be null.");
    
    if (role.getId() == null) {
      return;
    }
    //如果是系统角色则不删除
    if(StringUtils.equals(Constants.STATUS_AVAILABLE, role.getIsSys())) {
      logger.warn("Can't delete system role.");
      return;
    }

    // 删除与用户之间的关联关系
    Set<User> userSet = role.getUsers();
    User[] users = userSet.toArray(new User[] {});
    logger.debug("Remove the relations between Role and User");
    for (int i = 0; i < users.length; i++) {
      users[i].getRoles().remove(role);
      role.getUsers().remove(users[i]);
      // saveObject(users[i]);
    }
    // 删除与权限之间的关联关系
    Set<Permission> permSet = role.getPermissions();
    Permission[] perms = permSet.toArray(new Permission[] {});
    logger.debug("Remove the relations between Persmission and Role");
    for (int i = 0; i < perms.length; i++) {
      perms[i].getRoles().remove(role);
      role.getPermissions().remove(perms[i]);
      // saveObject(perms[i]);
    }
    // 同步缓存
    if (acegiCacheManager != null) {
      acegiCacheManager.beforeRoleRemoved(role);
    }

    super.remove(role);
  }
  
  /**
   * 根据角色名称查询角色
 * @param roleName
 * @return
 */
public Role getRoleByName(String roleName){
	  return findObject("from Role r where r.name = ?", roleName);
  }

  /**
   * @param acegiCacheManager the acegiCacheManager to set
   */
  @Autowired
  public void setAcegiCacheManager(AcegiCacheManager acegiCacheManager) {
    this.acegiCacheManager = acegiCacheManager;
  }

}
