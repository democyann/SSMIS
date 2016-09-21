package com.systop.common.modules.security.jcaptcha;

import java.util.ResourceBundle;

import com.systop.core.util.ResourceBundleUtil;

public class JCaptchaConstants {
  public static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
      .getBundle("com.systop.common.modules.security.jcaptcha.jcaptcha");

  /**
   * captcha 显示的字符
   */
  public static final String CAPTCHA_STRING = ResourceBundleUtil.getString(
      RESOURCE_BUNDLE, "captcha.string", "0123456789");

  /**
   * captcha 最多显示多少个
   */
  public static final int CAPTCHA_MAX_WORDS = ResourceBundleUtil.getInt(
      RESOURCE_BUNDLE, "captcha.maxWords", 5);

  /**
   * captcha 最少显示多少个
   */
  public static final int CAPTCHA_MIN_WORDS = ResourceBundleUtil.getInt(
      RESOURCE_BUNDLE, "captcha.minWords", 4);

  /**
   * captcha 图片宽度
   */
  public static final int CAPTCHA_IMG_WIDTH = ResourceBundleUtil.getInt(
      RESOURCE_BUNDLE, "captcha.imgWidth", 100);

  /**
   * captcha 图片高度
   */
  public static final int CAPTCHA_IMG_HEIGHT = ResourceBundleUtil.getInt(
      RESOURCE_BUNDLE, "captcha.imgHeight", 40);

  /**
   * captcha 最大字体
   */
  public static final int CAPTCHA_MAX_FONT_SIZE = ResourceBundleUtil.getInt(
      RESOURCE_BUNDLE, "captcha.maxFontSize", 18);

  /**
   * captcha 最小字体
   */
  public static final int CAPTCHA_MIN_FONT_SIZE = ResourceBundleUtil.getInt(
      RESOURCE_BUNDLE, "captcha.minFontSize", 16);

}
