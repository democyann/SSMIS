package com.systop.common.modules.mail.service;

import com.systop.common.modules.mail.model.SmtpConfig;

/**
 * The manager of the SMTP config.
 * @author Sam Lee
 *
 */
public interface SmtpConfigManager {
  /**
   * @return the smpt
   */
  public SmtpConfig getSmtpConfig();

  /**
   * @param smpt the smpt to set
   */
  public void setSmtpConfig(SmtpConfig smtp);
  
}
