package com.systop.common.modules.security.acegi.intercept.support;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.acegisecurity.Authentication;
import org.acegisecurity.concurrent.SessionRegistry;
import org.acegisecurity.ui.logout.LogoutHandler;

/**
 * 简单的将session清空。
 * 
 * @author sshwsfc
 */
public class SessionLogoutHandler implements LogoutHandler {
  /**
   * @see {@link SessionRegistry}
   */
  private SessionRegistry sessionRegistry;

  /**
   * @param request not used (can be <code>null</code>)
   * @param response not used (can be <code>null</code>)
   * @param authentication not used (can be <code>null</code>)
   */
  public void logout(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) {
    HttpSession session = request.getSession(false);
    if (session != null) {
      // 因为使用了concurrentSessionController 在限制用户登陆,
      //所以登出时移除相应的session信息
      sessionRegistry.removeSessionInformation(session.getId());
      request.getSession().invalidate();
    }
  }

  public void setSessionRegistry(SessionRegistry sessionRegistry) {
    this.sessionRegistry = sessionRegistry;
  }

}
