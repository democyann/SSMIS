package com.systop.common.modules.security.user;

import java.security.Principal;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.acegisecurity.Authentication;
import org.acegisecurity.providers.UsernamePasswordAuthenticationToken;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.systop.common.modules.security.user.dao.UserDao;
import com.systop.common.modules.security.user.model.Role;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.ApplicationException;
import com.systop.core.Constants;

/**
 * 用户工具类
 * 
 * @author Sam Lee
 * 
 */
public final class UserUtil {
	/**
	 * 从HttpServletRequest中获得当前登录用户。返回的<code>User</code>
	 * 并未关联HIBERNATE的Session，如果需要获取其角色、部门等关联实体信息，则 必须重新与Session建立关联。
	 * 
	 * @param request 给定<code>HttpServletRequest</code>对象
	 * @return <code>User</code> or <code>null</code> if no user login.
	 */
	public static User getPrincipal(final HttpServletRequest request) {
		Principal principal = request.getUserPrincipal();
		if (principal != null) {
			if (principal instanceof Authentication) {
				Object p = ((UsernamePasswordAuthenticationToken) principal)
						.getPrincipal();
				if (p instanceof User) {
					return (User) p;
				}
			}
		}
		return null;
	}

	/**
	 * 获取当前登录用户
	 * 
	 * @param request
	 * @return
	 */
	public static User getLoginUser(HttpServletRequest request) {
		User user = getPrincipal(request);
		if (user == null) {
			throw new ApplicationException(Constants.NOT_LOGIN, StringUtils.EMPTY);
		}
		return user;
	}

    /**
     * 用户是否拥有角色
     * @param request
     * @param name
     * @return
     */
    public static boolean hasRole(ServletContext context,HttpServletRequest request,String ...name){
        User user = getLoginUser(request);

        ApplicationContext ctx = WebApplicationContextUtils
                .getWebApplicationContext(context);
        UserDao userDao = (UserDao) ctx.getBean("userDao");
        user = userDao.load(User.class, user.getId());

        Set<Role> roles = user.getRoles();
        for (Role role : roles) {
            for (String n : name) {
                if(role.getName().equals(n))
                    return true;
            }
        }
        return false;
    }

    public static boolean hasRole(User user,String ...name){
        Set<Role> roles = user.getRoles();
        for (Role role : roles) {
            for (String n : name) {
                if(role.getName().equals(n))
                    return true;
            }
        }
        return false;
    }



    public static boolean isManager(ServletContext context,HttpServletRequest request){
        return hasRole(context,request,"ROLE_G.MANAGER","ROLE_D.G.MANAGER");
    }

	/**
	 * 私有构造器，防止实例化
	 */
	private UserUtil() {
	}

}
