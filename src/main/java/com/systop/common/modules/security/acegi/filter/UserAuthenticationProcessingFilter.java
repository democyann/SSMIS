package com.systop.common.modules.security.acegi.filter;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.acegisecurity.Authentication;
import org.acegisecurity.context.SecurityContext;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.ui.webapp.AuthenticationProcessingFilter;
import org.acegisecurity.userdetails.UserDetails;

import com.systop.common.modules.security.acegi.listener.UserLoginListener;
import com.systop.common.modules.security.user.model.User;
import com.systop.common.modules.security.user.service.UserManager;
import com.systop.common.modules.security.user.service.listener.UserLoginHistoryListener;
import com.systop.core.Constants;
import com.systop.core.util.ReflectUtil;

/**
 * 把User变量放入http session中,key为Constants.USER_IN_SESSION
 * 
 * @author cac,sam
 */
@SuppressWarnings("rawtypes")
public class UserAuthenticationProcessingFilter extends
		AuthenticationProcessingFilter {
	/**
	 * 用户登录监听器，可以保存完整类名或类的实例
	 * 
	 * @see com.systop.common.security.acegi.listener.UserLoginListener
	 */
	private List userLoginListeners;
	/**
	 * UserManager
	 */
	private UserManager userManager;
	/**
	 * 2009-8-19 yj 用户登录历史记录监听器
	 * 
	 * @see com.systop.common.security.acegi.listener.UserLoginHistoryListener
	 */
	private List userLoginHistoryListener;

	/**
	 * @see {@link org.acegisecurity.ui. AbstractProcessingFilter#requiresAuthentication}
	 */
	protected boolean requiresAuthentication(HttpServletRequest request,
			HttpServletResponse response) {
		//创建用户名和密码的cookie			
		String [] jzpwd=request.getParameterValues("jzpwd");	
		String flag = request.getParameter("flag");
		String jz="";
		if(jzpwd!=null)
		{
	    for(int j=0;j<jzpwd.length;j++)
	    {
	    	 jz=jz+jzpwd[j];    	
	    }		    
		}		
		if(jz.equals("yes"))
		{
				String unameOne=request.getParameter("j_username");	
				String uname = null;
				try {
					uname = URLEncoder.encode(unameOne,"UTF-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Cookie cooki; 						
				cooki=new Cookie("username",String.valueOf(uname));//用户ID 			
				cooki.setMaxAge(60*60*24*365);//cookie时间 
		  	    cooki.setPath("/");  //根据个人的不用，在不同功能的路径下创建 
				response.addCookie(cooki);	
				String upwd=request.getParameter("j_password");		
				Cookie cooki1; 
				cooki1=new Cookie("userpwd",String.valueOf(upwd));
				cooki1.setMaxAge(60*60*24*365);//cookie时间 
				cooki1.setPath("/");  //根据个人的不用，在不同功能的路径下创建 
				response.addCookie(cooki1);	
		}else if(null != flag && flag.equals("1")){  //如果没选中记住密码则清除cookie
			Cookie cookie = new Cookie("username", null); 
			cookie.setMaxAge(0);
			cookie.setPath("/");  //根据个人的不用，在不同功能的路径下创建 
			response.addCookie(cookie); 
			Cookie cookie1 = new Cookie("userpwd",null);
			cookie1.setMaxAge(0);
			cookie1.setPath("/");  //根据个人的不用，在不同功能的路径下创建
			response.addCookie(cookie1); 
		}
		
		boolean requiresAuth = super.requiresAuthentication(request, response);
		HttpSession httpSession = null;
		try {
			httpSession = request.getSession(false);
		} catch (IllegalStateException ignored) {
		}
		if (httpSession != null) {
			if (httpSession.getAttribute(Constants.USER_IN_SESSION) == null) {
				if (!requiresAuth) {
					SecurityContext sc = SecurityContextHolder.getContext();
					Authentication auth = sc.getAuthentication();
					if (auth != null
							&& auth.getPrincipal() instanceof UserDetails) {
						UserDetails ud = (UserDetails) auth.getPrincipal();
						User user = userManager.getUser(ud.getUsername(),
								ud.getPassword());
						httpSession.setAttribute(Constants.USER_IN_SESSION,
								user);
						 Cookie[] cookies = request.getCookies(); 	  
						// 更新用户登录信息，例如登录次数等
						fireLoginSuccessedEvents(user, request);
						fireUserLoginHistory(user, request);
					} else {
						fireLoginFailedEvents(request);
					}
				}

			}
		}
		return requiresAuth;
	}

	/**
	 * 执行用户登录成功事件
	 * 
	 */
	private void fireLoginSuccessedEvents(User user, HttpServletRequest request) {
		if (userLoginListeners == null || userLoginListeners.isEmpty()) {
			return;
		}
		for (Iterator itr = userLoginListeners.iterator(); itr.hasNext();) {
			UserLoginListener listener = retrieveUser(itr.next());
			if (listener != null) {
				listener.loginSuccessed(user, request);
			}
		}
	}

	/**
	 * 执行用户登录成功事件
	 * 
	 */
	private void fireLoginFailedEvents(HttpServletRequest request) {
		if (userLoginListeners == null || userLoginListeners.isEmpty()) {
			return;
		}
		for (Iterator itr = userLoginListeners.iterator(); itr.hasNext();) {
			UserLoginListener listener = retrieveUser(itr.next());
			if (listener != null) {
				listener.loginFailed(request);
			}
		}
	}

	/**
	 * 获取用户登录监听器
	 */
	private UserLoginListener retrieveUser(Object obj) {
		if (obj == null) {
			return null;
		}
		if (obj instanceof String) {
			String className = obj.toString();
			obj = ReflectUtil.newInstance(className);
		}

		if (obj instanceof UserLoginListener) {
			return (UserLoginListener) obj;
		} else {
			return null;
		}
	}

	/**
	 * 2010-1-13 yj 记录用户登录历史
	 */
	private void fireUserLoginHistory(User user, HttpServletRequest request) {
		if (userLoginHistoryListener == null
				|| userLoginHistoryListener.isEmpty()) {
			return;
		}
		for (Iterator itr = userLoginHistoryListener.iterator(); itr.hasNext();) {
			UserLoginHistoryListener listener = getUserLoginHistoryListener(itr
					.next());
			if (listener != null) {
				listener.loginSuccessed(user, request);
			}
		}
	}

	/**
	 * 2010-1-13 yj 记录用户登录历史
	 */
	private UserLoginHistoryListener getUserLoginHistoryListener(Object obj) {
		if (obj == null) {
			return null;
		}
		if (obj instanceof String) {
			String className = obj.toString();
			obj = ReflectUtil.newInstance(className);
		}

		if (obj instanceof UserLoginListener) {
			return (UserLoginHistoryListener) obj;
		} else {
			return null;
		}
	}

	/**
	 * @param userLoginListeners
	 *            the userLoginListeners to set
	 */
	public void setUserLoginListeners(List userLoginListeners) {
		this.userLoginListeners = userLoginListeners;
	}

	/**
	 * @param userManager
	 *            the userManager to set
	 */
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public void setUserLoginHistoryListener(List userLoginHistoryListener) {
		this.userLoginHistoryListener = userLoginHistoryListener;
	}

}
