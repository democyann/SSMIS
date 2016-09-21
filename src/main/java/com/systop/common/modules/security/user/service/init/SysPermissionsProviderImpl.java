package com.systop.common.modules.security.user.service.init;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.systop.common.modules.security.user.model.Permission;

@Component
public class SysPermissionsProviderImpl implements SysPermissionsProvider {

  @Override
  public List<Permission> getSysPermissions() {
    List<Permission> perms = new ArrayList<Permission>(1);
    return perms;
  }

}
