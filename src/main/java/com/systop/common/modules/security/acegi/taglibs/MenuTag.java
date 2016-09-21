package com.systop.common.modules.security.acegi.taglibs;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.systop.common.modules.security.user.UserUtil;
import com.systop.common.modules.security.user.dao.UserDao;
import com.systop.common.modules.security.user.model.Permission;
import com.systop.common.modules.security.user.model.Role;
import com.systop.common.modules.security.user.model.User;

/**
 * @Description 根据用户角色中所有的菜单，决定显示的菜单
 * @author Liang
 * @Date 2013-10-13
 */
@SuppressWarnings({"serial", "unchecked", "rawtypes"})
public class MenuTag extends BodyTagSupport {
	
	private String hasMenu = "";

	public String getHasMenu() {
		return hasMenu;
	}

	public void setHasMenu(String hasMenu) {
		this.hasMenu = hasMenu;
	}

	@Override
	public int doStartTag() throws JspException {
		
			User user = UserUtil.getPrincipal((HttpServletRequest) pageContext
        .getRequest());
			if(user.getId() != null){
				Set<Role> roles = (Set<Role>) loadRoles(user);
				boolean skip = true;
				for(Role r : roles){
					if(r.getName().equals("ROLE_ADMIN")){
						return EVAL_BODY_INCLUDE;
					}
					Set<Permission> permissions = r.getPermissions();
					Set<Permission> ps = new HashSet<Permission>();
					for (Permission permission : permissions) {
						if (permission != null) {
							ps.add(permission);
						}
						Permission parentPer = permission.getParentPermission();//判断一次
						if (parentPer!=null) {//存在父节点
							if(!ps.contains(parentPer)){//父节点没有添加过，进行添加
								ps.add(parentPer);
								
								Permission parentPer2 = parentPer.getParentPermission();//判断二次
								if (parentPer2!=null) {//存在父节点
									if(!ps.contains(parentPer2)){//父节点没有添加过，进行添加
										ps.add(parentPer2);
										
										Permission parentPer3 = parentPer2.getParentPermission();//判断三次
										if (parentPer3!=null) {//存在父节点
											if(!ps.contains(parentPer3)){//父节点没有添加过，进行添加
												ps.add(parentPer3);
											}	
									}
								
								
							}
				}
							}
						}
					}
					
					
					for(Permission p : ps){
						if(p.getDescn().equals(hasMenu)){
							skip = false;
						}
					}
				}
				if(skip){
					return SKIP_BODY;
				}
				return EVAL_BODY_INCLUDE;
			}else{
				return EVAL_BODY_INCLUDE;
			}
	}
	
  /**
   * 返回用户所具有的角色名称
   * @return
   */
/*private Set getPrincipalRoles() {
    User user = UserUtil.getPrincipal((HttpServletRequest) pageContext
        .getRequest());
    if (user == null) {
      return Collections.EMPTY_SET;
    }

    Collection<Role> roles = loadRoles(user);

    if (roles == null || roles.size() == 0) {
      return Collections.EMPTY_SET;
    }

    Set set = new HashSet(roles.size());
    for (Role role : roles) {
      set.add(role.getName());
    }
    return set;
  }*/

/**
 * 加载用户的角色，处理延迟加载问题
 */
private Collection<Role> loadRoles(User user) {
  Set<Role> roles = new HashSet();

  ApplicationContext ctx = WebApplicationContextUtils
      .getWebApplicationContext(pageContext.getServletContext());
  UserDao userDao = (UserDao) ctx.getBean("userDao");
  List<Role> roleList = userDao.query("select r from Role r join r.users u "
      + "where u.id=?", user.getId());
  for (Role role : roleList) {
    roles.add(role);
  }
  return roles;
}
	
	
}
