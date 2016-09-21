package com.systop.common.modules.security.user.service.init;

import java.util.List;

import com.systop.common.modules.security.user.model.Role;
/**
 * {@link SecurityInitializer}会自动调用{@code SysRolesProvider}
 * 接口的实现类，以获取系统角色。
 * @author SAM
 *
 */
public interface SysRolesProvider {
  List<Role> getSysRoles();
  
}
