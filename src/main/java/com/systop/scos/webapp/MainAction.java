package com.systop.scos.webapp;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;
import com.systop.common.modules.desktop.DesktopConstants;
import com.systop.common.modules.desktop.model.Desktop;
import com.systop.common.modules.desktop.service.DesktopManager;
import com.systop.common.modules.security.user.UserUtil;
import com.systop.common.modules.security.user.model.User;
import com.systop.common.modules.security.user.service.UserManager;
import com.systop.core.webapp.struts2.action.BaseAction;

/**
 * 后台首页显示各个功能模块列表信息
 * 
 * @author ShangHua
 */
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Controller
@SuppressWarnings("serial")
public class MainAction extends BaseAction {

	@Autowired
	private UserManager userManager;
	@Autowired
	private DesktopManager desktopManager;
	
	private String passwordFlag;

	private User loginUser;
	private Map<Integer, String>  archiveMap;
	private final Logger log=LoggerFactory.getLogger(MainAction.class);
	private Map<String,Object> jsonMsg = new HashMap<String, Object>();

	public String main() {
		loginUser = UserUtil.getLoginUser(getRequest());
		loginUser = userManager.get(loginUser.getId());
		
		passwordFlag = "0";
		if (loginUser != null && loginUser.getLoginTimes() == 1){
				passwordFlag = "1";
		}
		if (redirectMobile()) {
			return "mobile-main";
		}
		Integer userId = getLoginUser().getId();
		if(userId != 1){
			ActionContext.getContext().getSession().put("userId", getLoginUser().getId());
			User user = getLoginUser();
			user.setOl("1");
			userManager.save(user);
		}
		Desktop desktop = desktopManager.getDesktopByUserId(loginUser.getId());
		if (desktop != null && DesktopConstants.SKIN_JLAYOUT.equals(desktop.getSkin())) {
			return "jlayout";
		} else {
			log.info("登录成功！");
			return "main";
			
		}
	}
	
	public String onlineCount(){
		Long count =	userManager.getOlCount();
		jsonMsg.put("count", count);
		return "msg"; 
	}
	
	public String nodes() {
		loginUser = UserUtil.getLoginUser(getRequest());
		loginUser = userManager.get(loginUser.getId());
		return "nodes";
	}

	public User getLoginUser() {
		return loginUser;
	}

	public void setLoginUser(User loginUser) {
		this.loginUser = loginUser;
	}

	public Map<Integer, String> getArchiveMap() {
		return archiveMap;
	}

	public String getPasswordFlag() {
		return passwordFlag;
	}

	public void setPasswordFlag(String passwordFlag) {
		this.passwordFlag = passwordFlag;
	}

	public Map<String, Object> getJsonMsg() {
		return jsonMsg;
	}

	public void setJsonMsg(Map<String, Object> jsonMsg) {
		this.jsonMsg = jsonMsg;
	}
}
