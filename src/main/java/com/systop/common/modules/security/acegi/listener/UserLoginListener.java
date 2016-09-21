package com.systop.common.modules.security.acegi.listener;

import javax.servlet.http.HttpServletRequest;

import com.systop.common.modules.security.user.model.User;
/**
 * 用户登录监听器
 * @author Sam Lee
 *
 */
public interface UserLoginListener {
   /**
    * 如果用户成功登录，则执行本方法
    * @param user 登录的用户信息
    * @param request HttpServletRequest
    */
   void loginSuccessed(User user, HttpServletRequest request);
   /**
    * 如果用户登录失败，则执行本方法
    * @param request HttpServletRequest
    */
   void loginFailed(HttpServletRequest request);
}
