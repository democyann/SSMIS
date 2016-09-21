package com.systop.common.modules.security.user.service.listener;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.systop.common.modules.security.acegi.listener.UserLoginListener;
import com.systop.common.modules.security.user.model.User;
import com.systop.common.modules.security.user.service.UserManager;
/**
 * 简单的记录用户登录IP和登录时间等信息
 * @author Sam Lee
 *
 */
@Service
public class SimpleUserLoginListener implements UserLoginListener {
  /**
   * 用户管理对象
   */
  private UserManager userManager;

  /**
   * @param userManager the userManager to set
   */
  @Autowired
  public void setUserManager(UserManager userManager) {
    this.userManager = userManager;
  }

  /**
   * @see UserLoginListener#loginFailed(HttpServletRequest)
   */
  public void loginFailed(HttpServletRequest request) {
    ; 
  }

  /**
   * @see UserLoginListener#loginSuccessed(User, HttpServletRequest)
   */
  public void loginSuccessed(User user, HttpServletRequest request) {

    userManager.updateLoginInformation(user, request.getRemoteAddr());
  }

}
