package com.systop.common.modules.security.acegi.intercept.support;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.acegisecurity.ui.AuthenticationDetailsSourceImpl;

/**
 * 解决 remember me 和 currentSession 冲突问题
 * 
 * @author cac,sam
 */
public class AuthenticationDetailsSourceHelper extends
    AuthenticationDetailsSourceImpl {
  /**
   * @see AuthenticationDetailsSourceImpl#buildDetails(HttpServletRequest)
   */
  public Object buildDetails(HttpServletRequest request) {
    HttpSession session = request.getSession(false);
    if (session == null) {
      session = request.getSession(true);
    }
    return super.buildDetails(request);
  }
}
