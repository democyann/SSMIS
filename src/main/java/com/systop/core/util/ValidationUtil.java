package com.systop.core.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

/**
 * 验证工具类
 * @author Sam
 *
 */
public final class ValidationUtil {
  /**
   * 验证Email地址是否合法
   * @param emailAddr 给定的Email地址.
   * @return 如果合法，返回<code>true</code>,否则，返回<code>false</code>
   */
  public static boolean isValidEmail(String emailAddr) {
    String emailAddressPattern = "\\b(^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@([A-Za-z0-9-])+(\\.[A-Za-z0-9-]+)*((\\.[A-Za-z0-9]{2,})|(\\.[A-Za-z0-9]{2,}\\.[A-Za-z0-9]{2,}))$)\\b";
    return validateRegex(emailAddr, emailAddressPattern, true);
  }
  
  /**
   * 正则表达式验证。
   * @param value 被验证字符串， 如果为空字符串或{@code null},则认为不匹配。
   * @param expression 正则表达式， 如果为空字符串或{@code null},则认为不匹配。
   * @param isCaseSensitive 是否忽略大小写
   * @return 如果匹配，返回{@code true},否则返回{@code false}
   */
  public static boolean validateRegex(String value, String expression,
      boolean isCaseSensitive) {
    if (StringUtils.isBlank(value) || StringUtils.isBlank(expression)) {
      return false;
    }

    String compare = ((String) value).trim();
    if (compare.length() == 0) {
      return false;
    }

    Pattern pattern;
    if (isCaseSensitive) {
      pattern = Pattern.compile(expression);
    } else {
      pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
    }

    Matcher matcher = pattern.matcher(compare);
    return matcher.matches();
  }
  /**
   * 防止初始化
   */
  private ValidationUtil() {
  }
}
