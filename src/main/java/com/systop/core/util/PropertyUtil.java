package com.systop.core.util;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

/**
 * 用于解析Bean属性的工具类。
 * @author Sam Lee
 * 
 */
public final class PropertyUtil {
  /**
   * Getter 方法的前缀
   */
  public static final String GETTER_PREFIX = "get";

  /**
   * Setter 方法的前缀
   */
  public static final String SETTER_PREFIX = "set";

  /**
   * Accessor方法前缀的长度
   */
  public static final int ACCESSOR_PREFIX_LENGTH = "get".length();

  /**
   * 将一个字符串转换为JavaBean的setter方法,同时去掉字符串中_号和-号， 
   * 并将这两个符号后面的第一个字符转换为大写。
   */
  public static String parseSetter(String str) {
    String propertyName = parseProperty(str);
    if (StringUtils.isBlank(propertyName)) {
      return StringUtils.EMPTY;
    }
    // 如果以set开头，并且后面紧跟一个大写字母，则直接返回
    if (isSetter(propertyName)) {
      return propertyName;
    }
    // 在前面加set，并将set后的第一个字符变为大写
    StringBuffer buf = new StringBuffer(SETTER_PREFIX).append(
        propertyName.substring(0, 1).toUpperCase()).append(
        propertyName.substring(1, propertyName.length()));

    return buf.toString();
  }

  /**
   * 将一个字符串转换为JavaBean的getter方法,同时去掉字符串中_号和-号， 
   * 并将这两个符号后面的第一个字符转换为大写。
   */
  public static String parseGetter(String str) {
    String propertyName = parseProperty(str);
    if (StringUtils.isBlank(propertyName)) {
      return StringUtils.EMPTY;
    }
    // 如果以set开头，并且后面紧跟一个大写字母，则直接返回
    if (isGetter(propertyName)) {
      return propertyName;
    }
    // 在前面加set，并将set后的第一个字符变为大写
    StringBuffer buf = new StringBuffer(GETTER_PREFIX).append(
        propertyName.substring(0, 1).toUpperCase()).append(
        propertyName.substring(1, propertyName.length()));

    return buf.toString();
  }

  /**
   * 判断一个String是否是一个合法的javaBean的setter方法的名字。
   */
  public static boolean isSetter(String s) {
    if(s == null) {
      return false;
    }
    return s.indexOf(SETTER_PREFIX) == 0
        && Character.isUpperCase(s.charAt(ACCESSOR_PREFIX_LENGTH));
  }

  /**
   * 判断一个String是否是一个合法的javaBean的getter方法的名字。
   */
  public static boolean isGetter(String s) {
    if(s == null) {
      return false;
    }
    return s.indexOf(GETTER_PREFIX) == 0
        && Character.isUpperCase(s.charAt(ACCESSOR_PREFIX_LENGTH));
  }

  /**
   * 转换访问器方法名字为对应的属性的名字。同时删除其中的"_"和"-"。
   * 例如，如果输入setName,则返回name
   * @param accessorName 必须是一个符合javabean格式的accessor方法名。
   * 否则，accessor2Property将直接返回输入的参数而不做任何转换。
   */
  public static String accessor2Property(String accessorName) {
    Assert.hasText(accessorName);
    if(!isGetter(accessorName) && !isSetter(accessorName)) {
      return accessorName;      
    }

    String temp = accessorName.substring(ACCESSOR_PREFIX_LENGTH, accessorName
        .length());
    return parseProperty(temp);
  }

  /**
   * 将一个字符串解析为属性的名字。例如：<br>
   * 
   * <pre>
   * user_name -&gt;userName;
   * username -&gt; username;
   * userName -&gt; userName;
   * User_NAME-&gt; userName;
   * USERNAME-&gt; uSERNAME;
   * </pre>
   * 
   * @param propertyName String to be parsed.
   * @return
   */
  public static String parseProperty(String propertyName) {
    if (StringUtils.isBlank(propertyName)) {
      return StringUtils.EMPTY;
    }
    // 将_和-统一替换为-,方便以后操作
    propertyName = propertyName.replaceAll("_|-?1", "-");
    while (propertyName.endsWith("-")) { // 删除位于尾部的-号
      propertyName = propertyName.substring(0, propertyName.length() - 1);
    }
    //如果包含"-"则全部转换为小写字母
    if(propertyName.indexOf("-") >= 0) {
      propertyName = propertyName.toLowerCase();
    }
    // 删除所有的-号，并将后面的字符转换为大写
    int signIndex = 0;
    while ((signIndex = propertyName.indexOf("-")) >= 0) {
      if (signIndex >= 0) {
        String c = String.valueOf(propertyName.charAt(signIndex + 1));
        propertyName = propertyName.replaceAll("-" + c, c.toUpperCase());
      }
    }
    //将首字母转为小写
    if (Character.isUpperCase(propertyName.charAt(0))) {
      StringBuffer buf = new StringBuffer(propertyName.substring(0, 1)
          .toLowerCase()).append(propertyName.subSequence(1, propertyName
          .length()));
      propertyName = buf.toString();
    }
    
    return propertyName;
  }
  
  /**
   * 防止构造
   */
  private PropertyUtil() {
    
  }

}
