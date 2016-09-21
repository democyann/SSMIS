package com.systop.common.modules.security.user.service.init;

import java.util.List;

import com.systop.common.modules.security.user.model.Permission;

/**
 * {@link SecurityInitializer}会自动调用{@code SysPermissionsProvider}
 * 接口的实现类，以获取系统许可。
 * @author Sam
 *
 */
public interface SysPermissionsProvider {
  List<Permission> getSysPermissions();
}
