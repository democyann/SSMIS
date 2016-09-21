package com.systop.core.util;

import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.xwork.StringUtils;
import org.apache.struts2.StrutsStatics;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.systop.common.modules.security.user.UserConstants;
import com.systop.core.Constants;

/**
 * 
 * @Title: LoginInterceptor.java
 * @author LiZhichao
 * @data 2013-8-12
 * @Description: 登陆拦截器
 */
@SuppressWarnings({ "rawtypes", "serial" ,"unused" ,"unchecked"})
public class LoginInterceptor extends AbstractInterceptor {  
	
    public static final String GOING_TO_URL_KEY="GOING_TO";
    
    public String timeout;
      
	@Override  
    public String intercept(ActionInvocation invocation) throws Exception {  
          
        ActionContext actionContext = invocation.getInvocationContext();  
        HttpServletRequest request= (HttpServletRequest) actionContext.get(StrutsStatics.HTTP_REQUEST);  
          
        Map session = actionContext.getSession();
        if (session != null && session.get(Constants.USER_IN_SESSION) != null){  
            return invocation.invoke();
        }
        timeout = "timeout";
        //setGoingToURL(session, invocation);
        return "login";
    }  
  
	/**
	 * 记录用户未登录时的请求连接
	 * @param session
	 * @param invocation
	 */
	private void setGoingToURL(Map session, ActionInvocation invocation){  
        String url = "";  
        String namespace = invocation.getProxy().getNamespace();  
        if (StringUtils.isNotBlank(namespace) && !namespace.equals("/")){  
            url = url + namespace;  
        }  
        String actionName = invocation.getProxy().getActionName();  
        if (StringUtils.isNotBlank(actionName)){  
            url = url + "/" + actionName + ".do";  
        }  
        session.put(GOING_TO_URL_KEY, url);  
    }

	public String getTimeout() {
		return timeout;
	}

	public void setTimeout(String timeout) {
		this.timeout = timeout;
	}  
	
}  