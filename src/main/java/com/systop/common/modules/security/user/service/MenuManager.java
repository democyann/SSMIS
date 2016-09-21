package com.systop.common.modules.security.user.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.systop.common.modules.security.user.model.Menu;
import com.systop.common.modules.security.user.model.Permission;
import com.systop.common.modules.security.user.model.Resource;
import com.systop.common.modules.security.user.model.Role;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.service.BaseGenericsManager;

/**
 * @author ZhouLihui
 */
@Service
public class MenuManager extends BaseGenericsManager<Menu> {

	@Autowired
	private ResourceManager resourceManager;
	
	@Autowired
	private UserManager userManager;
	
	@Autowired
	private PermissionManager permissionManager;
	
	@Override
	@Transactional
	public void remove(Menu menu){
		resourceManager.remove(menu);
	}
	
	@Override
	@Transactional
	public void save(Menu menu){
		resourceManager.save(menu);
	}
	
	/**
	 * 根据parentId获得子菜单
	 * @param parentId
	 * @return
	 */
	public List<Menu> getMenusByParentId(Integer parentId){
		List<Menu> menus = null;
		String hql = null;
		if (parentId == null) {
			hql = "from Menu m where m.parentMenu is null";
			menus = query(hql);
		} else {
			hql = "from Menu m where m.parentMenu.id = ?";
			menus = query(hql, parentId);
		}
		return menus;
	}	
		
	public List<Menu> getAuthorizedMenusByUserId(Integer userId) {
		Assert.notNull(userId);
		List<Menu> menuList = new ArrayList<Menu>();
		User user = userManager.get(userId);
		if (user != null) {
			Set<Role> roles = user.getRoles();
			if (roles != null) {
				for (Role role : roles) {
					Set<Permission> permissions = role.getPermissions();
					if (permissions != null) {
						for (Permission perm : permissions) {
							Set<Resource> resources = perm.getResources();
							if (resources != null) {
								for (Resource resource : resources) {
									if (resource instanceof Menu) {
										menuList.add((Menu) resource);
									}
								}
							}
						}
					}
				}
			}
		}
		return menuList;
	}
	
	public List<Menu> getAuthorizedMenusByPermissionId(Integer permissionId) {
		Assert.notNull(permissionId);
		List<Menu> menuList = new ArrayList<Menu>();
		Permission permission = permissionManager.get(permissionId);
		if (permission != null) {
			Set<Resource> resources = permission.getResources();
			if (resources != null) {
				for (Resource resource : resources) {
					if (resource instanceof Menu) {
						menuList.add((Menu) resource);
					}
				}
			}
		}
		return menuList;
	}
	
}
