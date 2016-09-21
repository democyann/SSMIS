package com.systop.common.modules.security.user.service;

import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.systop.common.modules.security.acegi.cache.AcegiCacheManager;
import com.systop.common.modules.security.user.model.Permission;
import com.systop.common.modules.security.user.model.Resource;
import com.systop.core.ApplicationException;
import com.systop.core.service.BaseGenericsManager;

/**
 * Manager of <code>Resource</code>
 * @author Sam
 */

@Service
@SuppressWarnings({"unchecked", "rawtypes"})
public class ResourceManager extends BaseGenericsManager<Resource> {

  /**
   * <code>AcegiCacheManager</code>用于同步缓存
   */
  private AcegiCacheManager acegiCacheManager;
  
  @Autowired
  private StrutsUrlImportService strutsUrlImportService;

   /**
   * @see BaseManager#remove(java.lang.Object)
   */
  @Override
  @Transactional
  public void remove(Resource res) {
    Assert.notNull(res);
    if (res.getId() == null) {
      return;
    }
    // 删除权限关联
    Set<Permission> permsSet = res.getPermissions();
    Permission[] perms = permsSet.toArray(new Permission[] {});
    logger.debug("Remove the relations between Persmission and Resource");
    for (int i = 0; i < perms.length; i++) {
      perms[i].getResources().remove(res);
      res.getPermissions().remove(perms[i]);
    }

    String old = res.getResString();
    super.remove(res);
    if (acegiCacheManager != null) {
      acegiCacheManager.onResourceChanged(null, old);
    }
  }

  /**
   * @see BaseManager#save(java.lang.Object)
   */
  @Override
  @Transactional
  public void save(Resource res) {
    if (res == null) {
      return;
    }

    if (StringUtils.isNotBlank(res.getResString()) && getDao().exists(res, "resString")) {
      throw new ApplicationException(res.getResString());
    }

    String oldString = null; // 修改之前的resString
    Resource old = res;
    if (old.getId() != null) {
      old = get(old.getId());
      // 因为有Version字段，所以要复制属性
      oldString = old.getResString();
      old.setName(res.getName());
      old.setResType(res.getResType());
      old.setDescn(res.getDescn());
      old.setResString(res.getResString());
    }

    super.save(old); // 更新Resource

    if (oldString == null) {
      oldString = res.getResString();
    }

    if (acegiCacheManager != null) { // 同步缓存
      acegiCacheManager.onResourceChanged(old, oldString);
    }

  }

  /**
   * @see ResourceManager#isNameInUse(java.lang.String)
   */
  public boolean isNameInUse(String name) {
    Resource res = new Resource();
    res.setName(name);
    logger.debug("check for resource name '{}'", name);

    return getDao().exists(res, "name"); // 如果给定name不唯一，则表示正在使用
  }

  /**
   * @see ResourceManager#getByPerm(Permission)
   */
  public Resource[] getByPerm(Permission perm) {
    Set resSet = perm.getResources();
    Resource[] retResources = new Resource[resSet.size()];
    resSet.toArray(retResources);
    return retResources;
  }

  /**
   * 保存解析struts文件后生成的URL资源 遍历所有的struts配置文件,并逐个解析, 将解析后的节点以map的形式保存在List中, 然后将得到的节点内容保存为URL资源
   * @param strutsXmls struts配置文件
   */
  @Transactional
  public void saveResource() {
    strutsUrlImportService.save();
  }

  /**
   * @return the acegeCacheManager
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

}
