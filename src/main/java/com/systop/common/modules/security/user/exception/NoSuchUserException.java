package com.systop.common.modules.security.user.exception;

import com.systop.core.ApplicationException;

/**
 * 如果某用户不存在，则抛出本异常。
 * @author Sam
 *
 */
@SuppressWarnings("serial")
public class NoSuchUserException extends ApplicationException {

  public NoSuchUserException(String msg) {
    super(msg);
  }

}
