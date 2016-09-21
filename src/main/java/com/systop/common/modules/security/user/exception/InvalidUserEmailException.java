package com.systop.common.modules.security.user.exception;

import com.systop.core.ApplicationException;

/**
 * 如果用户的Email不合法，则抛出该异常
 * @author Sam
 *
 */
@SuppressWarnings("serial")
public class InvalidUserEmailException extends ApplicationException {

  public InvalidUserEmailException(String msg) {
    super(msg);
  }

}
