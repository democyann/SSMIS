package com.systop.common.modules.desktop.webapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import com.systop.common.modules.desktop.DesktopConstants;
import com.systop.common.modules.desktop.model.Desktop;
import com.systop.common.modules.desktop.model.DesktopMenu;
import com.systop.common.modules.desktop.service.DesktopManager;
import com.systop.common.modules.security.user.model.Menu;
import com.systop.common.modules.security.user.model.User;
import com.systop.common.modules.security.user.service.MenuManager;
import com.systop.core.webapp.struts2.action.JsonCrudAction;

@SuppressWarnings("serial")
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class DesktopAction extends JsonCrudAction<Desktop,DesktopManager>{

	@Autowired
	private MenuManager menuManager;
	
	//桌面菜单
	private List<DesktopMenu> desktopMenuList = new ArrayList<DesktopMenu>();
	
	//json返回消息
	private Map<String, String> result = new HashMap<String, String>();
	
	//菜单id
	private Integer menuId;
	
	//桌面菜单id
	private Integer desktopMenuId;
	//排序号
	private Integer serialNo;
	//皮肤
	private String skin;
	
	//添加桌面菜单
	public String addMenuToDesktop() {
		User user = this.getLoginUser();
		if (menuId != null && user != null) {
			Desktop desktop = this.getManager().getDesktopByUserId(user.getId());
			if (desktop == null) {
				desktop = new Desktop();
				desktop.setUser(user);
			}
			Menu menu = menuManager.get(menuId);
			DesktopMenu desktopMenu = new DesktopMenu();
			desktopMenu.setMenu(menu);
			desktopMenu.setDesktop(desktop);
			Set<DesktopMenu> desktopMenus = desktop.getDesktopMenus();
			if(desktopMenus.contains(desktopMenu)){
				result.put("state", "failure");
			}else{
				int size = desktopMenus.size();
				desktopMenu.setSerialNo(size + 1);
				String color = DesktopConstants.MENU_COLORS.get(size % 12);
				desktopMenu.setColor(color);
				desktopMenus.add(desktopMenu);
				this.setModel(desktop);
				super.save();
				result.put("state", "success");
				result.put("color", color);
				result.put("id", ""+desktopMenu.getId());
			}
		} else {
			result.put("state", "failure");
		}
		return JSON;
	}
	
	//加载桌面菜单
	public String loadDesktopMenus(){
		User user = this.getLoginUser();
		if (user != null) {
			Desktop desktop = this.getManager().getDesktopByUserId(user.getId());
			desktopMenuList.addAll( desktop.getDesktopMenus());
		}
		return JSON;
	}
	
	//桌面菜单排序
	@Transactional
	public String sortDesktopMenu(){
		if(desktopMenuId != null){
			DesktopMenu desktopMenu = this.getManager().getDao().get(DesktopMenu.class, desktopMenuId);
			desktopMenu.setSerialNo(serialNo);
			this.getManager().getDao().save(desktopMenu);
		}
		return NONE;
	}
	
	//删除桌面菜单
	@Transactional
	public String removeDesktopMenu(){
		if(desktopMenuId != null){
			this.getManager().getDao().delete(DesktopMenu.class, desktopMenuId);
		}
		return NONE;
	}
	
	//切换皮肤
	public String changeSkin(){
		User user = this.getLoginUser();
		if (user != null && StringUtils.isNotBlank(skin)) {
			Desktop desktop = this.getManager().getDesktopByUserId(user.getId());
			if (desktop == null) {
				desktop = new Desktop();
				desktop.setUser(user);
			}
			desktop.setSkin(skin);
			getManager().save(desktop);
		}
		return NONE;
	}
	
	public List<DesktopMenu> getDesktopMenuList() {
		return desktopMenuList;
	}
	
	public Map<String, String> getResult() {
		return result;
	}

	public Integer getMenuId() {
		return menuId;
	}
	public void setMenuId(Integer menuId) {
		this.menuId = menuId;
	}

	public Integer getDesktopMenuId() {
		return desktopMenuId;
	}
	public void setDesktopMenuId(Integer desktopMenuId) {
		this.desktopMenuId = desktopMenuId;
	}

	public Integer getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(Integer serialNo) {
		this.serialNo = serialNo;
	}

	public String getSkin() {
		return skin;
	}

	public void setSkin(String skin) {
		this.skin = skin;
	}	
}