package com.systop.common.modules.security.jcaptcha;

import com.octo.captcha.service.CaptchaServiceException;
import com.octo.captcha.service.image.ImageCaptchaService;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Servlet generates CAPTCHA jpeg images based on the JCAPTCHA package. It's
 * configured via spring, and requires a ImageCaptchaService bean with the
 * id=imageCaptchaService
 * 基于JCAPTCHA生成CAPTCHA jpeg图片的Servlet。它通过Spring进行配置，并且set一个
 * 类型为ImageCaptchaService，id为imageCaptchaService的bean
 * @author Jason Thrasher
 */
@SuppressWarnings("serial")
public class ImageCaptchaServlet extends HttpServlet {
  /**
   * Captcha Service Name
   */
  private String captchaServiceName = "imageCaptchaService";
  /**
   * @see HttpServlet#init(ServletConfig)
   */
  public void init(ServletConfig servletConfig) throws ServletException {
    if (StringUtils.isNotBlank(servletConfig
        .getInitParameter("captchaServiceName"))) {
      captchaServiceName = servletConfig.getInitParameter("captchaServiceName");
    }

    super.init(servletConfig);
  }
  /**
   * @see HttpServlet#doGet()
   */
  protected void doGet(HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse) throws ServletException,
      IOException {

    byte[] captchaChallengeAsJpeg = null;
    // the output stream to render the captcha image as jpeg into
    ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
    try {
      // get the image captcha service defined via the SpringFramework
      ApplicationContext ctx = WebApplicationContextUtils
          .getRequiredWebApplicationContext(getServletContext());
      Object bean = ctx.getBean(captchaServiceName);
      ImageCaptchaService imageCaptchaService = (ImageCaptchaService) bean;

      // get the session id that will identify the generated captcha.
      // the same id must be used to validate the response, the session id
      // is a good candidate!
      String captchaId = httpServletRequest.getSession().getId();
      // call the ImageCaptchaService getChallenge method
      BufferedImage challenge = imageCaptchaService.getImageChallengeForID(
          captchaId, httpServletRequest.getLocale());

      // a jpeg encoder
      JPEGImageEncoder jpegEncoder = JPEGCodec
          .createJPEGEncoder(jpegOutputStream);
      jpegEncoder.encode(challenge);
    } catch (IllegalArgumentException e) {
      httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
      return;
    } catch (CaptchaServiceException e) {
      httpServletResponse
          .sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      return;
    }

    captchaChallengeAsJpeg = jpegOutputStream.toByteArray();

    // flush it in the response
    httpServletResponse.setHeader("Cache-Control", "no-store");
    httpServletResponse.setHeader("Pragma", "no-cache");
    httpServletResponse.setDateHeader("Expires", 0);
    httpServletResponse.setContentType("image/jpeg");
    ServletOutputStream responseOutputStream = httpServletResponse
        .getOutputStream();
    responseOutputStream.write(captchaChallengeAsJpeg);
    responseOutputStream.flush();
    responseOutputStream.close();
  }
}
