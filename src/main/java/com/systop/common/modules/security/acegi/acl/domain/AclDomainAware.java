package com.systop.common.modules.security.acegi.acl.domain;

import java.io.Serializable;

/**
 * 标示哪些doamin需要acl保护
 * 
 * @author sshwsfc@gmail.com
 */
public interface AclDomainAware {

  /**
   * 取得需要保护的domain的id
   */
  public Serializable getId();
}
