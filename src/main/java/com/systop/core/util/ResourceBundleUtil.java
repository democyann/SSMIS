package com.systop.core.util;

import java.util.ResourceBundle;

/**
 * ResourceBundle的帮助类
 * @author Sam
 * 
 */
public final class ResourceBundleUtil {
  /**
   * 缺省的资源文件.
   */
  public static final String DEFAULT_BUNDLE = "application"; 
  /**
   * prevent from initializing.
   */
  private ResourceBundleUtil() {
  }

  /**
   * 返回定义在ResourceBundle中的int值
   * @param defaultValue 缺省值，如果出现任何异常，都返回该值
   * @return
   */
  public static int getInt(ResourceBundle rb, String key, int defaultValue) {
    String value = null;
    try {
      value = rb.getString(key);
    } catch (Exception e) {
      return defaultValue;
    }

    if (value == null) {
      return defaultValue;
    }

    try {
      return Integer.parseInt(value);
    } catch (Exception e) {
      return defaultValue;
    }
  }

  /**
   * 返回定义在ResourceBundle中的String值
   * @param defaultValue 缺省值，如果出现任何异常，都返回该值
   * 
   */
  public static String getString(ResourceBundle rb, String key,
      String defaultValue) {
    String value = null;
    try {
      value = rb.getString(key);
    } catch (Exception e) {
      return defaultValue;
    }

    return (value == null) ? defaultValue : value;
  }

  /**
   * 返回定义在ResourceBundle中的boolean值
   * @param defaultValue 缺省值，如果出现任何异常，都返回该值
   * @return
   */
  public static boolean getBoolean(ResourceBundle rb, String key,
      boolean defaultValue) {
    String value = null;
    try {
      value = rb.getString(key);
    } catch (Exception e) {
      return defaultValue;
    }

    if (value == null) {
      return defaultValue;
    }

    return Boolean.valueOf(value);
  }
}
