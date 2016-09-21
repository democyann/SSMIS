package com.systop.common.modules.mail;

import java.util.Properties;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import com.systop.common.modules.mail.model.SmtpConfig;
import com.systop.common.modules.mail.service.SmtpConfigManager;
/**
 * 发送电子邮件的工具类
 * @author Sam Lee
 *
 */
@Service
public class MailSender extends JavaMailSenderImpl {
  private SmtpConfigManager smtpConfigManager;
  
  /**
   * @param smtpConfigManager the smtpConfigManager to set
   */
  @Autowired
  public void setSmtpConfigManager(SmtpConfigManager smtpConfigManager) {
    this.smtpConfigManager = smtpConfigManager;
  }
  /**
   * Initialize the super-class's properties, should be invoked after
   * <code>smtpConfigManager</code> property set.
   * 
   */
  public void init() {
    if(smtpConfigManager == null) {
      return;
    }
    SmtpConfig cfg = smtpConfigManager.getSmtpConfig();
    BeanUtils.copyProperties(cfg, this, new String[]{"auth"}); 
    Properties javaMailProperties = new Properties();
    javaMailProperties.put("mail.smtp.auth", cfg.getAuth());
    setJavaMailProperties(javaMailProperties);
    setDefaultEncoding("UTF-8");
  }
  /**
   * @see JavaMailSenderImpl#send(MimeMessage)
   */
  @Override
  public void send(MimeMessage mimeMessage) throws MailException {
    init();
    super.send(mimeMessage);
  }
  /**
   * @see JavaMailSenderImpl#send(MimeMessage[])
   */
  @Override
  public void send(MimeMessage[] mimeMessages) throws MailException {
    init();
    super.send(mimeMessages);
  }
  /**
   * @see JavaMailSenderImpl#send(MimeMessagePreparator)
   */
  @Override
  public void send(MimeMessagePreparator mimeMessagePreparator) throws MailException {
    init();
    super.send(mimeMessagePreparator);
  }
  /**
   * @see JavaMailSenderImpl#send(MimeMessagePreparator[])
   */
  @Override
  public void send(MimeMessagePreparator[] mimeMessagePreparators) throws MailException {
    init();
    super.send(mimeMessagePreparators);
  }
  /**
   * @see JavaMailSenderImpl#send(org.springframework.mail.SimpleMailMessage)
   */
  @Override
  public void send(SimpleMailMessage simpleMessage) throws MailException {
    init();
    super.send(simpleMessage);
  }
  /**
   * @see JavaMailSenderImpl#send(org.springframework.mail.SimpleMailMessage[])
   */
  @Override
  public void send(SimpleMailMessage[] simpleMessages) throws MailException {
    init();
    super.send(simpleMessages);
  }
}
