package com.systop.core.util;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

/**
 * String Utility Class This is used to encode passwords programmatically
 * 
 * <p>
 * <a h ref="StringUtil.java.html"><i>View Source</i></a>
 * </p>
 * 
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public final class StringUtil {
	// ~ Static fields/initializers ==============================
	/**
	 * prevent from initializing.
	 */
	private StringUtil() {

	}

	/**
	 * EMPTY_STRING
	 */
	public final static String EMPTY_STRING = "";


	/**
	 * 取得完整类名的最后一截，也就是类本身名称。例如，输入java.lang.Object,则返回Object
	 */
	public static String unqualify(String qualifiedName) {
		return qualifiedName.substring(qualifiedName.lastIndexOf(".") + 1);
	}

	/**
	 * 例如，输入java.lang.Object,则返回java.lang
	 */
	public static String qualifier(String qualifiedName) {
		int loc = qualifiedName.lastIndexOf(".");
		return (loc < 0) ? "" : qualifiedName.substring(0, loc);
	}

	/**
	 * 组成完整类名
	 */
	public static String qualify(String prefix, String name) {
		if (name == null || prefix == null) {
			throw new NullPointerException();
		}
		return new StringBuffer(prefix.length() + name.length() + 1).append(
				prefix).append('.').append(name).toString();
	}

	/**
	 * 组成完整类名
	 */
	public static String[] qualify(String prefix, String[] names) {
		if (prefix == null) {
			return names;
		}
		int len = names.length;
		String[] qualified = new String[len];
		for (int i = 0; i < len; i++) {
			qualified[i] = qualify(prefix, names[i]);
		}
		return qualified;
	}

	/**
	 * 去除字符串中的&lt;和&gt;标记.
	 * @param src 给定源字符串
	 * @return 去除tags后的字符串。
	 */
	public static String stripTags(String src) {
		if (StringUtils.isBlank(src)) {
			return src;
		}
		return src.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
	}
  /**
   * 解析以comma分隔的字符串，并去掉其中不必要的字符，最后以Set返回
   * 
   */
  @SuppressWarnings({ "rawtypes", "unchecked" })
  public static Set parseCommaSplitedString(String string) {
    final Set results = new HashSet();
    final String[] splited = org.springframework.util.StringUtils
        .commaDelimitedListToStringArray(string);

    for (int i = 0; i < splited.length; i++) {
      String sub = splited[i];

      // Remove the role's whitespace characters without depending on JDK 1.4+
      // Includes space, tab, new line, carriage return and form feed.
      String trimed = sub.trim(); // trim, don't use spaces, as per SEC-378
      trimed = StringUtils.replace(trimed, "\t", "");
      trimed = StringUtils.replace(trimed, "\r", "");
      trimed = StringUtils.replace(trimed, "\n", "");
      trimed = StringUtils.replace(trimed, "\f", "");

      results.add(trimed);
    }

    return results;
  }
  
  /**
   * 判断字符串是否为字母或数字组合
   * @param str 需要判断的字符串
   * @return 是否匹配 true,false
   * @deprecated instead of 
   * {@link org.apache.commons.lang.StringUtils.isAlphanumeric(String)}
   */
  public static boolean isAlphanumeric(String str) {
    Pattern pattern = Pattern.compile("^[A-Za-z0-9]+$");
    Matcher isNum = pattern.matcher(str);
    if (!isNum.matches()) {
      return false;
    }
    return true;
  }
  
  /**
   * 将一个数字转化为字符串，并且在前面按照一定的长度补0，比如，serial=9, length=3
   * 则返回"009"
   * @param serial 给定的整数
   * @param length 返回字符串的长度
   */
  public static String zeroPadding(Integer serial, int length) {
    if (serial == null) {
      return StringUtils.leftPad("", length, '0');
    }

    String serialStr = serial.toString();
    return StringUtils.leftPad(serialStr, length, '0');
  }

  /**
   * 从给定的Serial字符串中取得数字，例如，给出"00089",则返回98,如果不能转换，则返回null
   * @param serial
   * @return
   */
  public static Integer getNumFromSerial(String serial) {
    if (StringUtils.isBlank(serial) || !StringUtils.isNumeric(serial)) {
      return null;
    }
    return Integer.valueOf(serial);
  }
  
  public static final String GETTER_PREFIX = "get";

  public static final String SETTER_PREFIX = "set";

  public static final int ACCESSOR_PREFIX_LENGTH = "get".length();
  /**
   * 将一个字符串转换为JavaBean的setter方法,同时去掉字符串中_号和-号， 并将这两个符号后面的第一个字符转换为大写。
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
   * 将一个字符串转换为JavaBean的getter方法,同时去掉字符串中_号和-号， 并将这两个符号后面的第一个字符转换为大写。
   */
  public static String parseGetter(String str) {
    String propertyName = parseProperty(str);
    if (StringUtils.isBlank(propertyName)) {
      return StringUtils.EMPTY;
    }
    // 如果以set开头，并且后面紧跟一个大写字母，则直接返回
    if (isSetter(propertyName)) {
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
    return s.indexOf(SETTER_PREFIX) == 0
        && Character.isUpperCase(s.charAt(ACCESSOR_PREFIX_LENGTH));
  }

  /**
   * 判断一个String是否是一个合法的javaBean的getter方法的名字。
   */
  public static boolean isGetter(String s) {
    return s.indexOf(GETTER_PREFIX) == 0
        && Character.isUpperCase(s.charAt(ACCESSOR_PREFIX_LENGTH));
  }
  /**
   * 转换访问器方法名字为对应的属性的名字。同时删除其中的"_"和"-"。
   */
  public static String accessor2Property(String accessorName) {
    Assert.hasText(accessorName);
    Assert.isTrue(isGetter(accessorName) || isSetter(accessorName));

    String temp = accessorName
        .substring(ACCESSOR_PREFIX_LENGTH, accessorName.length());
    return parseProperty(temp);
  }

  /**
   * 将一个字符串解析为属性的名字。例如：<br>
   * 
   * <pre>
   * user_name -&gt;userName;
   * username -&gt; username;
   * userName -&gt; userName;
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
    while (propertyName.endsWith("-")) {// 删除位于尾部的-号
      propertyName = propertyName.substring(0, propertyName.length() - 1);
    }
    // 删除所有的-号，并将后面的字符转换为大写
    int signIndex = 0;
    while ((signIndex = propertyName.indexOf("-")) >= 0) {
      if (signIndex >= 0) {
        String c = String.valueOf(propertyName.charAt(signIndex + 1));
        propertyName = propertyName.replaceAll("-" + c, c.toUpperCase());
      }
    }
    // 
    if (Character.isUpperCase(propertyName.charAt(0))) {
      StringBuffer buf = new StringBuffer(propertyName.substring(0, 1)
          .toLowerCase()).append(propertyName.subSequence(1, propertyName
          .length()));
      propertyName = buf.toString();
    }
    return propertyName;
  }


  
}
