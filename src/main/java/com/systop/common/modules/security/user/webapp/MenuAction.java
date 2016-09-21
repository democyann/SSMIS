package com.systop.common.modules.security.user.webapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.systop.common.modules.security.user.UserConstants;
import com.systop.common.modules.security.user.model.Menu;
import com.systop.common.modules.security.user.model.User;
import com.systop.common.modules.security.user.service.MenuManager;
import com.systop.core.util.RequestUtil;
import com.systop.core.webapp.struts2.action.ExtJsCrudAction;

/**
 * 菜单Action
 * 
 * @author ZhouLihui
 */
@SuppressWarnings("serial")
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class MenuAction extends ExtJsCrudAction<Menu,MenuManager>{

	//当前上级菜单ID
	private Integer parentMenuId;
		
	//封装属性菜单数据
	private List<Map<String, Object>> menus = null;
	
	private boolean authorized = false;
		
	public String menuTree(){
		if (RequestUtil.isJsonRequest(getRequest())) {
			authorized = false;
			menus =this.getMenuTree();
			return "jsonMenu";
		}
		return INDEX;
	}
	
	public String authMenuTree(){
		if (RequestUtil.isJsonRequest(getRequest())) {
			authorized = true;
			menus =this.getMenuTree();
			return "jsonMenu";
		}
		return INDEX;
	}
	
	//封装菜单树 begin
	private List<Map<String, Object>> getMenuTree(){
		List<Menu> tops = getManager().getMenusByParentId(null);
		List<Map<String, Object>> menuList = new ArrayList<Map<String, Object>>();
		for (Menu menu : tops) {
			Map<String, Object> top = toMap(menu);
			top = buildTreeByParent(top);
			if (top != null) {
				menuList.add(top);
			}
		}
		return menuList;
	}
		
	/**
	 * 返回菜单树形列表，每一个菜单用一个Map表示，子菜单用Map的'childNodes'key挂接一个List 
	 * @param parent
	 * @param authorized 是否验证权限
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Map<String, Object> buildTreeByParent(Map<String, Object> parent) {
		if (parent != null) {
			List<Menu> subs = getManager().getMenusByParentId((Integer) parent.get("id"));
			List childs = new ArrayList();
			for (Menu menu : subs) {
				Map<String, Object> child = toMap(menu);				
				child = buildTreeByParent(child); // 递归查询子菜单				
				if (child != null) {
					childs.add(child);
				}
			}
			if (!childs.isEmpty()) {
				parent.put("children", childs);
				parent.put("cls", "folder");
				parent.put("leaf", false);
			} else {
				parent.put("leaf", true);
			}
		}
		return parent;
	}
	
	//封装到Map中
	private Map<String, Object> toMap(Menu menu) {
		HttpServletRequest reqest = getRequest();
		String menuId = null;
		if (reqest != null) {
			menuId = getRequest().getParameter("menuId");
		}
		if(menuId != null && !menuId.equals("") && menuId.equals(menu.getId().toString())){
			return null;
		}
		Map<String, Object> map = null;
		if (menu != null) {
			boolean isAuthorized = this.checkAuthorizedMenu(menu);
			if (!authorized || isAuthorized) {
				map = new HashMap<String, Object>();
				map.put("id", menu.getId());
				map.put("text", menu.getName());
				map.put("descn", menu.getDescn());
				map.put("href", menu.getUrl());
				map.put("checked", isAuthorized);
			}
		}
		return map;
	}
	
	private boolean checkAuthorizedMenu(Menu menu){
		boolean flag = false;
		List<Menu> authorizedMenus = null;
		String permissionId = this.getRequest().getParameter("permissionId");
		if (StringUtils.isNotBlank(permissionId) && StringUtils.isNumeric(permissionId)) {
			//通过权限ID查询
			authorizedMenus = getManager().getAuthorizedMenusByPermissionId(Integer.parseInt(permissionId));
		} else {
			User user = this.getLoginUser();
			if (user != null) {
				//通过用户ID查询
				authorizedMenus = getManager().getAuthorizedMenusByUserId(user.getId());
			}
		}
		if (authorizedMenus != null && !authorizedMenus.isEmpty()) {
			if(authorizedMenus.contains(menu)){
				flag = true;
			}
		}
		return flag;
	}//封装菜单树end
	
	@Override
	public String save(){
		if (StringUtils.isBlank(getModel().getResType()) && StringUtils.isNotBlank(getModel().getResString())) {
			getModel().setResType(UserConstants.RESOURCE_TYPE_URL);
		}
		if (parentMenuId != null) {
			Menu parent = getManager().get(parentMenuId);
			if (parent != null) {
				getModel().setParentMenu(parent);
			}
		}
		if (RequestUtil.isJsonRequest(getRequest())) {
			getManager().save(getModel());
			return JSON;
		}else{
			getManager().save(getModel());
			return "treeIndex";
		}
	}
	
	@Override
	public String remove(){
		if (RequestUtil.isJsonRequest(getRequest())) {
			super.remove();
			return JSON;
		}else{
			super.remove();
			return treeIndex();
		}
	}
	
	public String treeIndex(){
		return "treeIndex";
	}
	@Override
	public String edit(){
		if (parentMenuId != null) {
			Menu parent = getManager().get(parentMenuId);
			if (parent != null) {
				getModel().setParentMenu(parent);
			}
		}
		return super.edit();
	}
	/**
	 * @return the parentMenuId
	 */
	public Integer getParentMenuId() {
		return parentMenuId;
	}

	/**
	 * @param parentMenuId the parentMenuId to set
	 */
	public void setParentMenuId(Integer parentMenuId) {
		this.parentMenuId = parentMenuId;
	}

	/**
	 * @return the menus
	 */
	public List<Map<String, Object>> getMenus() {
		return menus;
	}
}
