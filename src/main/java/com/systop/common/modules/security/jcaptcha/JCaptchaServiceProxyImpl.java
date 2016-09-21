package com.systop.common.modules.security.jcaptcha;

import org.acegisecurity.captcha.CaptchaServiceProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.octo.captcha.service.CaptchaService;
import com.octo.captcha.service.CaptchaServiceException;

/**
 * 实现 CaptchaServiceProxy 用于acegi来校验，由spring注入jcaptchaService
 * 
 * @author sshwsfc@gmail.com
 */
public class JCaptchaServiceProxyImpl implements CaptchaServiceProxy {
  /**
   * Log for the class
   */
  protected static Logger logger = LoggerFactory.getLogger(JCaptchaServiceProxyImpl.class);
  /**
   * instance of CaptchaService.
   */
  private CaptchaService jcaptchaService;
  
  /**
   * @see {@link CaptchaServiceProxy#validateReponseForId(String, Object)}
   */
  public boolean validateReponseForId(String id, Object response) {
    logger.debug("validating captcha response");

    try {
      boolean isHuman = jcaptchaService.validateResponseForID(id, response)
          .booleanValue();
      if (isHuman) {
        logger.debug("captcha passed");
      } else {
        logger.warn("captcha failed");
      }
      return isHuman;

    } catch (CaptchaServiceException cse) {
      // fixes known bug in JCaptcha
      logger.warn("captcha validation failed due to exception", cse);
      return false;
    }
  }

  public void setJcaptchaService(CaptchaService jcaptchaService) {
    this.jcaptchaService = jcaptchaService;
  }
}
