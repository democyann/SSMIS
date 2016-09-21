package com.systop.common.modules.security.user.service.listener;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.systop.common.modules.security.user.model.User;
import com.systop.common.modules.security.user.service.UserManager;

/**
 * session的监听器
 *
 * @author Liang
 * @date 2013-12-12
 */
public class UserSessionListener implements HttpSessionListener {

	@Override
	public void sessionCreated(HttpSessionEvent event) {
	}

	/**
	 * session销毁时更新相关用户的在线状态
	 */
	@Override
	public void sessionDestroyed(HttpSessionEvent event) {
		Integer id = (Integer) event.getSession().getAttribute("userId");
		UserManager userManager = (UserManager) getManager("userManager",event.getSession().getServletContext());
		if(id == null){return;}
		User u = userManager.get(id);
		u.setOl("0");
		userManager.save(u);
		
	}
	
	public Object getManager(String name,ServletContext sc){
		ApplicationContext ctx = WebApplicationContextUtils
				.getWebApplicationContext(sc);
		return ctx.getBean(name);
	}

}
