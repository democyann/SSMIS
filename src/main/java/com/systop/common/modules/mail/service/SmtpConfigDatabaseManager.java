package com.systop.common.modules.mail.service;

import java.util.Date;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.systop.common.modules.mail.MailSender;
import com.systop.common.modules.mail.model.SmtpConfig;
import com.systop.common.modules.security.user.service.UserManager;
import com.systop.core.ApplicationException;
import com.systop.core.dao.hibernate.BaseHibernateDao;
import com.systop.core.service.Definable;

@Service
public class SmtpConfigDatabaseManager implements SmtpConfigManager, Definable {
  private static Logger logger = LoggerFactory.getLogger(SmtpConfigDatabaseManager.class);
  /**
   * 数据源表主键（为了配合Hibernate）
   */
  public static final String PK = "smtp";
  
  @Autowired(required = true)
  @Qualifier("baseHibernateDao")
  private BaseHibernateDao baseHibernateDao;
  
  @Autowired(required = true)
  private UserManager userManager;

  @Autowired(required = true)
  private MailSender mailSender; 
  @Override
  public SmtpConfig getSmtpConfig() {
    return baseHibernateDao.get(SmtpConfig.class, PK);
  }
  
  
  @Override
  @Transactional
  public void setSmtpConfig(SmtpConfig smtp) {
    smtp.setId(PK);
    if(isDefined()) {
      baseHibernateDao.merge(smtp);
    } else {
      baseHibernateDao.save(smtp);
    }
  }
  
  /**
   * 发送测试邮件，检查SMTP是否配置成功，如果没有成功，则抛出ApplicationException
   * 
   */  
  public void check() {
    SmtpConfig cfg = getSmtpConfig();
    if(cfg == null) {
      throw new ApplicationException("SMTP尚未配置。");
    }
    
    MimeMessage message = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
    String fromTo = userManager.getAdmin().getEmail();
    try {
      helper.setTo(fromTo);
      helper.setFrom(fromTo);
      helper.getMimeMessage().addHeader("Content-Type", "text/html; charset=UTF-8");
      helper.setSubject("测试邮件");
      helper.setText("这是测试邮件，请删除。", true);
      helper.setSentDate(new Date());
      mailSender.send(message);
    } catch (Exception e) {
      e.printStackTrace();
      logger.error(e.getMessage());
      StringBuffer msg = new StringBuffer("SMTP配置错误。");
      
      if(fromTo.indexOf(cfg.getUsername()) < 0) {
        msg.append("admin账户email[").append(fromTo).append("]与SMTP配置不符，" +
        		"这可能是导致配置失败的原因。");
      }
      throw new ApplicationException(msg.toString());
    }
  }

  @Override
  public boolean isDefined() {
    return getSmtpConfig() != null;
  }
}
