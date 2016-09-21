package com.systop.common.modules.security.user.service.init;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.systop.common.modules.security.user.model.Role;
import com.systop.core.Constants;

@Component
public class SysRolesProviderImpl implements SysRolesProvider {
  /**
   * 普通用户
   */
  public static final Role ROLE_NORMAL = new Role();
  /**
   * 高级用户
   */
  public static final Role ROLE_SENIOR = new Role();
    
  static {
    ROLE_NORMAL.setName("ROLE_NORMAL");
    ROLE_NORMAL.setDescn("普通用户");
    ROLE_NORMAL.setIsSys(Constants.STATUS_AVAILABLE);
    ROLE_SENIOR.setName("ROLE_SENIOR");
    ROLE_SENIOR.setDescn("高级用户");
    ROLE_SENIOR.setIsSys(Constants.STATUS_AVAILABLE);    
  }
  
  @Override
  public List<Role> getSysRoles() {
    List<Role> roles = new ArrayList<Role>(2);
    roles.add(ROLE_NORMAL);
    roles.add(ROLE_SENIOR);
    return roles;
  }

}
