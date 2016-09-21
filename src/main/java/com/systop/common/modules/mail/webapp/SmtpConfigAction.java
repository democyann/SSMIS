package com.systop.common.modules.mail.webapp;

import org.apache.struts2.interceptor.validation.SkipValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.validator.annotations.IntRangeFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.StringLengthFieldValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;
import com.systop.common.modules.mail.model.SmtpConfig;
import com.systop.common.modules.mail.service.SmtpConfigDatabaseManager;
import com.systop.common.modules.mail.service.SmtpConfigManager;
import com.systop.common.modules.security.user.model.User;
import com.systop.common.modules.security.user.service.UserManager;
import com.systop.core.webapp.struts2.action.BaseAction;

/**
 * SMTP配置Action
 * 
 * @author hsf
 * 
 */
@SuppressWarnings("serial")
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class SmtpConfigAction extends BaseAction {
  private SmtpConfigManager smtpConfigManager;

  private SmtpConfig model;
  
  @Autowired(required = true)
  private UserManager userManager;

  /**
   * 显示SMTP信息
   * @return
   */
  @SkipValidation
  public String view() {
    model = smtpConfigManager.getSmtpConfig();
    return SUCCESS;
  }

  /**
   * 编辑SMTP信息
   * @return
   */
  @SkipValidation
  public String edit() {
    model = smtpConfigManager.getSmtpConfig();
    return SUCCESS;
  }
  /**
   * Jsp访问，用于缺省的SMTP用户名
   */
  public User getAdmin() {
    return userManager.getAdmin();
  }

  /**
   * 保存SMTP信息
   * @return
   */
  @Validations(requiredStrings = {
      @RequiredStringValidator(fieldName = "model.host", message = "主机地址是必须的!"),
      @RequiredStringValidator(fieldName = "model.username", message = "用户名是必须的!"),
      @RequiredStringValidator(fieldName = "model.password", message = "口令是必须的!") }, 
      stringLengthFields = { @StringLengthFieldValidator(fieldName = "model.password", minLength = "3", maxLength = "32", message = "口令应多于3字符", trim = true) }, 
      intRangeFields = { @IntRangeFieldValidator(type = ValidatorType.SIMPLE, fieldName = "model.port", min = "1", max = "65535", message = "端口号必须在1至65535之间！") })
  public String save() {
    User admin = userManager.getAdmin();
    if(admin != null && admin.getEmail().indexOf(model.getUsername()) < 0) {
      StringBuffer actUrl = new StringBuffer("http://");
      StringBuffer errInfo = new StringBuffer();
      actUrl.append(
          getRequest().getLocalAddr()).append(":").append(
              getRequest().getLocalPort()).append(
                  getRequest().getContextPath()).append(
                      "/security/user/index.do");
      errInfo.append("用户名与 ").append("<a href='").append(
          actUrl).append("'>").append("<font color='red'>").append(
              "admin").append("</font>").append("</a>").append(" 账户电子邮件(").append(
                  admin.getEmail()).append(")不符，请修改SMTP配置信息或修改 ").append(
                      "<a href='").append(actUrl).append("'>").append("<font color='red'>").append("admin").append(
                          "</font>").append("</a>").append(" 账户的email地址。");
      addActionError(errInfo.toString());
      return INPUT;
    }
    smtpConfigManager.setSmtpConfig(model);
    return SUCCESS;
  }
  
  /**
   * 验证SMTP是否配置成功
   */
  public String check() {
    if(smtpConfigManager instanceof SmtpConfigDatabaseManager) {
      try {
        ((SmtpConfigDatabaseManager) smtpConfigManager).check();
        render(getResponse(), "SMTP配置成功.", "text/plain");
      } catch(Exception e) {
        e.printStackTrace();
        logger.error(e.getMessage());
        render(getResponse(), "配置失败，" + e.getMessage(), "text/plain");
      }
    }
    return null;
  }

  public SmtpConfig getModel() {
    return model;
  }

  public void setModel(SmtpConfig model) {
    this.model = model;
  }

  /**
   * @param smtpConfigManager the smtpConfigManager to set
   */
  @Autowired
  public void setSmtpConfigManager(SmtpConfigManager smtpConfigManager) {
    this.smtpConfigManager = smtpConfigManager;
  }
}
