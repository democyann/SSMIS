package com.systop.common.modules.security.user.service;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.systop.common.modules.security.acegi.cache.AcegiCacheManager;
import com.systop.common.modules.security.user.model.Permission;
import com.systop.common.modules.security.user.model.Resource;
import com.systop.common.modules.security.user.model.Role;
import com.systop.core.ApplicationException;
import com.systop.core.Constants;
import com.systop.core.service.BaseGenericsManager;

/**
 * @author SAM
 * 
 */
@Service
@SuppressWarnings({"unchecked", "rawtypes"})
public class PermissionManager extends BaseGenericsManager<Permission> {
  
  /**
   * 用于同步缓存
   */
  private AcegiCacheManager acegiCacheManager;

  /**
   * @see PermissionManager#getByRole(Role)
   */
  public Permission[] getByRole(Role role) {
    Set permSet = role.getPermissions();
    Permission[] retPerms = new Permission[permSet.size()];
    permSet.toArray(retPerms);
    return retPerms;
  }

  /**
   * @see PermissionManager#isNameInUse(java.lang.String)
   * @deprecated save method has already checked it.
   */
  @Deprecated
  public boolean isNameInUse(String name) {
    Permission perm = new Permission();
    perm.setName(name);
    logger.debug("check for permission name '{}'", name);
    
    return getDao().exists(perm, "name"); // 如果给定name不唯一，则表示正在使用
  }

  /**
   * @see PermissionManager#savePerm(Permission)
   */
  @Override
  @Transactional
  public void save(Permission perm) {
    Assert.notNull(perm);
    
    if (!getDao().exists(perm, "name")) {
      if (perm.getId() != null) { //更新
        Permission old = get(perm.getId());
        BeanUtils.copyProperties(perm, old, new String[]{"resources", "roles"});
        if (perm.getStatus() != null) {
          old.setStatus(perm.getStatus());
        }
        getDao().update(old);
      } else { //新建
        perm.setStatus(Constants.STATUS_AVAILABLE);
        getDao().save(perm);
      }
      if (acegiCacheManager != null) {
        acegiCacheManager.onPermissionChanged(perm);
      }
    } else {
      throw new ApplicationException("Reduplicate permission name ["
          + perm.getName() + "]");
    }
    
  }

  /**
   * @return the acegiCacheManager
   */
  public AcegiCacheManager getAcegiCacheManager() {
    return acegiCacheManager;
  }


  /**
   * @param acegiCacheManager the acegiCacheManager to set
   */
  @Autowired
  public void setAcegiCacheManager(AcegiCacheManager acegiCacheManager) {
    this.acegiCacheManager = acegiCacheManager;
  }

  /**
   * @see BaseManager#remove(java.lang.Object)
   */
  @Override
  @Transactional
  public void remove(Permission perm) {
    Assert.notNull(perm);
    if (perm.getId() == null) {
      return;
    }
    
  //如果是系统许可则不删除
    if(StringUtils.equals(Constants.STATUS_AVAILABLE, perm.getIsSys())) {
      logger.warn("Can't delete system permission.");
      return;
    }
    // 删除与资源的关联关系
    Set<Resource> resSet = perm.getResources();
    Resource[] resources = resSet.toArray(new Resource[] {});
    logger.debug("Remove the relations between Persmission and Resource");
    for (int i = 0; i < resources.length; i++) {
      resources[i].getPermissions().remove(perm);
      perm.getResources().remove(resources[i]);
      //saveObject(resources[i]);
    }
    // 删除与角色的关联关系
    Set<Role> roleSet = perm.getRoles();
    Role[] roles = roleSet.toArray(new Role[] {});
    logger.debug("Remove the relations between Persmission and Roles");
    for (int i = 0; i < roles.length; i++) {
      roles[i].getPermissions().remove(perm);
      perm.getRoles().remove(roles[i]);
      //saveObject(roles[i]);
    }
    // 同步缓存
    if (acegiCacheManager != null) {
      acegiCacheManager.beforePermissionRemoved(perm);
    }    
    
    super.remove(perm);
  }

  /**
   * 通过角色Id查询相应的权限
   * @param roleId
   * @return
   */
public List<Permission> getPermissionsByRoleId(String roleId) {
	List<Permission> perms=null;
	try{
	String hql="select r.permissions from Role r where r.id = ?";
	perms = query(hql, Integer.valueOf(roleId));
	}catch(Exception e){
		e.printStackTrace();
	}
	return perms;
}
/**
 * 拿到一级节点
 * @return
 */
public List<Permission> getTopPermissions() {
	String hql = "from Permission p where p.parentPermission is null order by p.id asc ";
	return query(hql);
}

/**
 * 通过父节点id拿到所有子节点
 * @param id
 * @return
 */
public List<Permission> getPermissionsById(String id) {
	String hql = "from Permission p where p.parentPermission.id = ? order by p.id asc";
	return query(hql, id);
}


}
