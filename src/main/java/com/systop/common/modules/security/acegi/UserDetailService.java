package com.systop.common.modules.security.acegi;

import java.util.List;

import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.GrantedAuthorityImpl;
import org.acegisecurity.providers.dao.UserCache;
import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.systop.common.modules.security.user.model.Permission;
import com.systop.common.modules.security.user.model.User;
import com.systop.common.modules.security.user.service.UserManager;
import com.systop.core.Constants;

/**
 * 从<code>UserManager</code>中获的<code>UserDetail</code>信息.
 * 
 * @author Sam Lee
 * 
 */
@Service

public class UserDetailService implements UserDetailsService {
  /**
   * log for the class
   */
  private static Logger logger = LoggerFactory.getLogger(UserDetailService.class);
  /**
   * Instance of {@link UserManager}
   */
  private UserManager userManager;
  /**
   * <code>UserCache</code>用于从缓存中取得UserDetails
   */
  private UserCache userCache;

  /**
   * @param userCache the userCache to set
   */
  @Autowired
  public void setUserCache(UserCache userCache) {
    this.userCache = userCache;
  }

  /**
   * 根据用户名查询用户信息
   */
  public UserDetails loadUserByUsername(String username) {
    UserDetails userDetails = null;
    if (userCache != null) { // 从缓存中取得UserDetails
      userDetails = userCache.getUserFromCache(username);
      if (userDetails != null) {
        logger.debug("Get user details from cache {}/{}", userDetails.getPassword(), username);
      }

    }
    if (userDetails == null) { // 如果缓存中没有，则查询数据库，然后putInCache
      User user = userManager.findObject("from User u where u.loginId=? and u.status=?", username,
          Constants.STATUS_AVAILABLE);
      if (user == null) {
        return null;
      }
      logger.debug("password of user '{}/{}'", user.getLoginId(), user.getPassword());

      List<Permission> perms = userManager.findPermissionsByUser(user);
      GrantedAuthority[] auths = new GrantedAuthority[perms.size()];
      for (int i = 0; i < perms.size(); i++) {
        auths[i] = new GrantedAuthorityImpl(perms.get(i).getName());
        logger.debug("Auth of user '{}' is '{}'", user.getLoginId(), auths[i].getAuthority());
      }
      user.setAuthorities(auths);

      userDetails = user;

      if (userCache != null) {
		  userCache.putUserInCache(userDetails);
		  logger.debug("Put user in cache {}", username);
	  }
    }
    return userDetails;
  }

  /**
   * @return the userManager
   */
  public UserManager getUserManager() {
    return userManager;
  }

  /**
   * @param userManager the userManager to set
   */
  @Autowired
  public void setUserManager(UserManager userManager) {
    this.userManager = userManager;
  }

}
