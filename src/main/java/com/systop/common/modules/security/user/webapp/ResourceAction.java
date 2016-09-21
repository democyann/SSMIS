package com.systop.common.modules.security.user.webapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;
import com.systop.common.modules.security.user.UserConstants;
import com.systop.common.modules.security.user.model.Permission;
import com.systop.common.modules.security.user.model.Resource;
import com.systop.common.modules.security.user.service.PermissionManager;
import com.systop.common.modules.security.user.service.ResourceManager;
import com.systop.core.ApplicationException;
import com.systop.core.dao.support.Page;
import com.systop.core.util.ReflectUtil;
import com.systop.core.webapp.struts2.action.ExtJsCrudAction;

/**
 * 资源Action
 * @author Sam Lee
 */
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@SuppressWarnings({"unchecked", "rawtypes", "serial"})
public class ResourceAction extends
    ExtJsCrudAction<Resource, ResourceManager> {
  /**
   * 为权限增加的资源暂存在Session中名字
   */
  private static final String TEMPLATE_ADDED_RESOURCES 
      = "resources_added_session";

  /**
   * 为权限删除的资源暂存在Session中的名字
   */
  private static final String TEMPLATE_REMOVED_RESOURCES 
      = "resources_removed_session";
  /**
   * Service for permission managing.
   */
  private PermissionManager permissionManager;
  
  /**
   * The target permission that will be assigned resources for.
   */
  private Permission permission = new Permission();
  
 

  /**
   * 返回资源类型列表
   */
  public Map<String, String> getResourceTypes() {
    return UserConstants.RESOURCE_TYPES;
  }

	/**
	 * 按资源名称和字符串执行的查询
	 * @see BaseModelAction#query()
	 */
	@Override
	public String index() {
		if (StringUtils.isBlank(getModel().getName()) 
  			&& StringUtils.isBlank(getModel().getResString())) {
  		items = getManager().query("from Resource");
  	} else {
  		items = getManager().query("from Resource r " 
  				+ "where r.name like ? and r.resString like ?", 
  				new Object[]{MatchMode.ANYWHERE.toMatchString(getModel().getName()),
  					MatchMode.ANYWHERE.toMatchString(getModel().getResString())});
  	}
  	return INDEX;
	}

  /**
   * 解析Struts配置文件内容为url资源,并保存到数据库中
   */
  public void saveUrl() {
    getManager().saveResource();
  }
  
  @Validations(requiredStrings = {
      @RequiredStringValidator(fieldName = "model.name", message = "资源名称是必须的."),
      @RequiredStringValidator(fieldName = "model.resString", message = "资源字符串是必须的."),
      @RequiredStringValidator(fieldName = "model.descn", message = "资源描述是必须的.")
  })
  @Override
  public String save() {
    return super.save();
  }
  //The following methods is used for resources assigning.
  /**
   * 返回所有资源，同时查询指定权限所具有的资源，
   * 并在“所有”资源中标注该权限所拥有的角色selected状态。
   * @param permissionId 指定用户id
   * @return List of Resources.
   */
  public String resourceOfPermission() {
    initPermission();

    // 得到该权限得到资源
    Set<Resource> resourceOfPremission = permission.getResources();
    logger.debug("Permission {} has {} resources.", 
        permission.getId(), resourceOfPremission.size());
    page = new Page(Page.start(getPageNo(), getPageSize()), getPageSize());
    if (StringUtils.isBlank(getModel().getName())) {
      page = getManager().pageQuery(page, getOrderHQL("from Resource r"));
    } else {
      page = getManager().pageQuery(page, getOrderHQL("from Resource r where r.name like ?"),
         "%" + getModel().getName() + "%");
    }

    // 得到所有资源
    List allResources = page.getData();
    // 从session中取得暂存的待分配Resources
    Set templateResourcesAdded = getTemporaryResources(permission,
        TEMPLATE_ADDED_RESOURCES);
    // 从session中取得暂存的待反分配Resources
    Set templateResourcesRemoved = getTemporaryResources(permission,
        TEMPLATE_REMOVED_RESOURCES);
    List mapResources = new ArrayList(allResources.size());
    for (Iterator itr = allResources.iterator(); itr.hasNext();) {
      Resource resource = (Resource) itr.next();
      resource.setChanged(false);

      if (resourceOfPremission.contains(resource)) { // 如果资源已经分配给权限则选中
        resource.setChanged(true);
      }

      // 如果资源暂时分配了则选中
      if (templateResourcesAdded != null
          && templateResourcesAdded.contains(resource.getId())) {
        resource.setChanged(true);
      }
      // 如果资源暂时被删除则不选中
      if (templateResourcesRemoved != null
          && templateResourcesRemoved.contains(resource.getId())) {
        resource.setChanged(false);
      }
     //    转换为Map，防止延迟加载
      Map mapRes = ReflectUtil.toMap(resource, 
          new String[]{"id", "name", "resString"}, true);
      mapRes.put("changed", resource.getChanged());
      mapResources.add(mapRes);
    }
    page.setData(mapResources);
    
    return JSON;
  }

  /**
   * 取消保存权限的资源信息
   */
  public String cancleSavePermissionResources() {
    initPermission();
    clearSession(permission);
    return JSON;
  }
  /**
   * 为指定权限分配资源
   * @param permissionId 权限Id
   */
  public String savePermissionResources() {
    initPermission();

    Set<Integer> resources = getTemporaryResources(permission,
        TEMPLATE_REMOVED_RESOURCES);
    for (Integer resourceId : resources) {
      Resource resource = getManager().get(resourceId);
      if (resource != null) {
        resource.getPermissions().remove(permission);
        permission.getResources().remove(resource);
      }
    }

    resources = getTemporaryResources(permission, TEMPLATE_ADDED_RESOURCES);
    for (Integer resourceId : resources) {
      Resource resource = getManager().get(resourceId);
      if (resource != null) {
        resource.getPermissions().add(permission);
        permission.getResources().add(resource);
      }
    }
    permissionManager.save(permission);
    clearSession(permission);

    return JSON;
  }
  /**
   * 页面选择一个resource
   */
  public String selectResource() {
    if(getModel().getId() == null) {
      return JSON;
    }
    initPermission();
    selectResource(getModel().getId(), permission.getId(), true);
    return JSON;
  }
  
  /**
   * 页面选择一个resource
   */
  public String deselectResource() {
    if(getModel().getId() == null) {
      return JSON;
    }
    initPermission();
    selectResource(getModel().getId(), permission.getId(), false);
    return JSON;
  }
  
  /**
   * Build a hql with order clause
   */
  private String getOrderHQL(String initHQL) {
    if(StringUtils.isBlank(getSortProperty())) {
      return initHQL;
    }
    return new StringBuffer(100).append(initHQL).append(" order by r.")
      .append(getSortProperty()).append(" ").append(getSortDir()).toString();
  }

  /**
   * 将资源Id保存在session中的一个Map对象中，
   * 该对象以Permission实例和ResourceId为key-value.
   * @param permissionId 权限Id
   * @param resourceId 资源Id
   * @param selected 是删除(false)还是添加(true)
   */
  private void selectResource(Integer resourceId, String permissionId,
      boolean selected) {
    if (resourceId == null || permissionId == null) {
      return;
    }
    // 根据Id获得Resource和Permission，其中Permission将作为key保存在Map对象中
    initPermission();
    setModel(getManager().get(getModel().getId()));
    if (permission == null || getModel() == null) {
      return;
    }
    if (selected) {
      temporaryAddResource(permission, getModel());
    } else {
      temporaryRemoveResource(permission, getModel());
    }
  }
  /**
   * 初始<code>permission</code>属性
   */
  private void initPermission() {
    if(permission.getId() == null) {
      throw new ApplicationException("There is no permission ID.");
    }
    permission = permissionManager.get(permission.getId());
    if (permission == null) {
      logger.warn("No permission found." + permission.getId());
      throw new ApplicationException("There is no permission with id " + permission.getId());
    }
  }

  /**
   * 从session中取得某个权限的临时资源，包括待分配的和待反分配的。
   * @param permission 指定的权限
   * @param sessionName Session名字
   */
  private Set getTemporaryResources(Permission permission, String sessionName) {
    Map permissionResources = (Map) getRequest().getSession().getAttribute(
        sessionName);
    if (permissionResources == null) {
      permissionResources = Collections.synchronizedMap(new HashMap());
      getRequest().getSession().setAttribute(sessionName, permissionResources);
    }

    Set resourceIds = (Set) permissionResources.get(permission.getId());

    if (resourceIds == null) {
      resourceIds = Collections.synchronizedSet(new HashSet());
      permissionResources.put(permission.getId(), resourceIds);
    }

    return resourceIds;
  }

  /**
   * 权限授权情况，在Session中暂存一个待分配的Resource Id
   * @param permission 给定权限
   * @param resource 即将分配给权限的资源
   */
  private void temporaryAddResource(Permission permission, Resource resource) {
    // 从Session中取得该权限临时分配的资源Id
    Set resourceIdsAdded = getTemporaryResources(permission,
        TEMPLATE_ADDED_RESOURCES);

    // 如果新分配的Resource还没有保存在数据库中，则暂存这个Resource Id
    if (!permission.getResources().contains(resource)) {
      resourceIdsAdded.add(resource.getId());
      logger.debug("Temporary add resource {} for permission {}",
          resource.getId(), permission.getId());
    }
    // 同步即将删除的角色
    Set resourceIdsRemoved = getTemporaryResources(permission,
        TEMPLATE_REMOVED_RESOURCES);
    resourceIdsRemoved.remove(resource.getId());
  }

  /**
   * 根据授权情况，在Session中暂存一个反分配的permissionId
   * @param permission 给定权限
   * @param resource 即将从权限中删除的资源
   */
  private void temporaryRemoveResource(Permission permission, 
      Resource resource) {
    // 从Session中取得该权限临时反分配的资源Id
    Set resourceIdsRemoved = getTemporaryResources(permission,
        TEMPLATE_REMOVED_RESOURCES);
    // 如果该资源已经分配给了权限，则暂存
    if (permission.getResources().contains(resource)) {
      resourceIdsRemoved.add(resource.getId());
      logger.debug("Temporary remove resource {} for permission {}",
          resource.getId(), permission.getId());
      // 同步即将添加的角色
      Set resourceIdsAdded = getTemporaryResources(permission,
          TEMPLATE_ADDED_RESOURCES);
      resourceIdsAdded.remove(resource.getId());
    }
  }


  /**
   * 清空Session中的有关数据
   */
  private void clearSession(Permission permission) {
    HttpSession session = getRequest().getSession();
    Map map = (Map) session.getAttribute(TEMPLATE_ADDED_RESOURCES);
    if (map.containsKey(permission.getId())) {
      map.remove(permission.getId());
    }
    map = (Map) session.getAttribute(TEMPLATE_REMOVED_RESOURCES);
    if (map.containsKey(permission.getId())) {
      map.remove(permission.getId());
    }
    logger.debug("Session of permission {} cleared.",  permission.getName());
  }

  /**
   * @return the permission
   */
  public Permission getPermission() {
    return permission;
  }

  /**
   * @param permission the permission to set
   */
  public void setPermission(Permission permission) {
    this.permission = permission;
  }
  /**
   * @param permissionManager the permissionManager to set
   */
  @Autowired
  public void setPermissionManager(PermissionManager permissionManager) {
    this.permissionManager = permissionManager;
  }


}
