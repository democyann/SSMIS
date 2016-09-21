package com.systop.common.modules.security.user.service.listener;

import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.systop.common.modules.security.user.model.Role;
import com.systop.common.modules.security.user.model.User;

/**
 * 更新用户的时候，将关联的角色信息断开
 * @author Sam Lee
 *
 */
@Component
public class UserChangeCascadeListener implements UserChangeListener {
  /**
   * Log
   */
  private static Log logger = LogFactory.getLog(UserChangeCascadeListener.class);
  /**
   * @see UserChangeListener#onRemove(User)
   */
  public void onRemove(User user) {
    Set<Role> roleSet = user.getRoles();
    if (roleSet == null || roleSet.size() < 0) {
      return;
    }
    Role[] roles = roleSet.toArray(new Role[] {});

    for (int i = 0; i < roles.length; i++) {
      roles[i].getUsers().remove(user);
      user.getRoles().remove(roles[i]);
    }
    logger.debug("On User Removed.");
  }
  /**
   * @see UserChangeListener#onSave(User, User)
   */
  public void onSave(User newUser, User oldUser) {
    logger.debug("On saving user.");
  }

}
