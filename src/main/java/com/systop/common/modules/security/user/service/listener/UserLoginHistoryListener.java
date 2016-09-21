package com.systop.common.modules.security.user.service.listener;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.systop.common.modules.security.acegi.listener.UserLoginListener;
import com.systop.common.modules.security.user.model.User;
import com.systop.common.modules.security.user.service.UserManager;

@Service
public class UserLoginHistoryListener implements UserLoginListener {
	/**
	 * 用户管理对象
	 */
	private UserManager userManager;

	/**
	 * @param userManager
	 *          the userManager to set
	 */
	@Autowired
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	@Override
	public void loginFailed(HttpServletRequest request) {
		// TODO Auto-generated method stub

	}

	@Override
	public void loginSuccessed(User user, HttpServletRequest request) {
		// TODO Auto-generated method stub
		userManager.addUserLoginHistory(user, request.getRemoteAddr());
	}
}
