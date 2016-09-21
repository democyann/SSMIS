package com.systop.core.webapp.struts2.validation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.systop.core.util.GenericsUtil;

@SuppressWarnings({"unchecked", "rawtypes"})
public abstract class AbstractValidator<T> implements Validator {
  protected Logger logger = LoggerFactory.getLogger(getClass());

  /** 不为空提示信息 */
  private String VALIDATE_REQUIRED_MESSAGE = "{0}不能为空！";

  /** 字符串长度某范围提示信息 */
  private String VALIDATE_STRING_LENGTH_MIN_MAX_MESSAGE = "{0}长度不正确，请输入指定长度{1}~{2}的内容！";

  /** 字符串长最小提示信息 */
  private String VALIDATE_STRING_LENGTH_MIN_MESSAGE = "{0}长度不正确，请输入最小长度{1}的内容！";

  /** 字符串长度最大提示信息 */
  private String VALIDATE_STRING_LENGTH_MAX_MESSAGE = "{0}长度不正确，请输入最大长度{1}的内容 ！";

  /** 整数验证提示信息 */
  private String VALIDATE_INT_MESSAGE = "{0}必须为整数类型！";

  /** 日期验证提示信息 */
  private String VALIDATE_DATE_MESSAGE = "{0}必须为日期类型！";

  @Override
  public boolean supports(Class clazz) {
    if (clazz == null) {
      return false;
    }
    Class genericClass = GenericsUtil.getGenericClass(getClass(), 0);
    return clazz.equals(genericClass);
  }

  @Override
  public final void validate(Object target, Errors errors) {
    doValidate((T) target, (ActionErrors) errors);
  }

  /**
   * 验证方法,提供泛型支持。
   * @param target model
   * @param errors 错误信息
   */
  public abstract void doValidate(T target, ActionErrors errors);

  /**
   * 验证指定对象不能为null<br/>
   * validateRequired(null, "用户名称", errors);<br/>
   * 错误提示：<b>用户名称不能为空！</b>
   * @param obj 指定对象
   * @param field 字段名
   * @param errors 错误信息
   */
  public void validateRequired(Object obj, String field, ActionErrors errors) {
    if (obj == null) {
      errors.rejectDirectly(VALIDATE_REQUIRED_MESSAGE, field);
    }
  }

  /**
   * 
   * 验证指定字符串不能为""与null<br/>
   * validateRequiredString("", "密码", errors);<br/>
   * 错误提示：<b>密码不能为空</b>
   * @param value 指定字符串
   * @param field 字段名
   * @param errors 错误信息
   */
  public void validateRequiredString(String value, String field,
      ActionErrors errors) {
    if (StringUtils.isBlank(value)) {
      errors.rejectDirectly(VALIDATE_REQUIRED_MESSAGE, field);
    }
  }

  /**
   * 验证指定字符串长度在minLen到maxLen之内或大于minLen或小于maxLen<br/>
   * 1. validateStringLength("你好呀，你叫什么呀", 4, 8, "备注", errors)<br/>
   * 错误提示：<b>备注长度不正确，请输入指定长度4~8的内容！</b><br/>
   * 2. validateStringLength("你好呀", 4, null, "备注", errors)<br/>
   * 错误提示：<b>备注长度不正确，请输入最小长度4的内容！</b><br/>
   * 3. validateStringLength("你好呀，你叫什么呀", null, 8, "备注", errors)<br/>
   * 错误提示：<b>备注长度不正确，请输入最大长度8的内容！</b><br/>
   * @param value 指定字符串
   * @param minLen 最小长度
   * @param maxLen 最大长度
   * @param field 字段名称
   * @param errors 错误信息
   */
  public void validateStringLength(String value, Integer minLen,
      Integer maxLen, String field, ActionErrors errors) {
    if (minLen == null && maxLen == null) {
      return;
    }
    if (StringUtils.isBlank(value)) {
      return;
    }
    
    value = value.trim();

    int minLength = -1;
    int maxLength = -1;
    if (minLen != null && minLen.intValue() > -1) {
      minLength = minLen.intValue();
    }
    if (maxLen != null && maxLen.intValue() > -1) {
      maxLength = maxLen.intValue();
    }

    // minLength与maxLength为-1不进行验证
    if ((minLength > -1 && maxLength > -1)
        && (value.length() < minLength && value.length() > maxLength)) {
      errors.rejectDirectly(VALIDATE_STRING_LENGTH_MIN_MAX_MESSAGE,
          new Object[] { field, minLength, maxLength });
    } else if (minLength > -1 && value.length() < minLength) {
      errors.rejectDirectly(VALIDATE_STRING_LENGTH_MIN_MESSAGE, new Object[] {
          field, minLength });
    } else if (maxLength > -1 && value.length() > maxLength) {
      errors.rejectDirectly(VALIDATE_STRING_LENGTH_MAX_MESSAGE, new Object[] {
          field, maxLen });
    }
  }

  /**
   * 验证指定字符串是否为Email<br/>
   * validateEmail("sjzwbb.com", errors);<br/>
   * 错误提示：<b>输入正确的Email格式!</b>
   * @param value 指定字符串
   * @param errors 错误信息
   */
  public void validateEmail(String value, ActionErrors errors) {
    // Email的正则表达示
    String emailAddressPattern = "\\b(^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@([A-Za-z0-9-])+(\\.[A-Za-z0-9-]+)*((\\.[A-Za-z0-9]{2,})|(\\.[A-Za-z0-9]{2,}\\.[A-Za-z0-9]{2,}))$)\\b";
    this.validateRegex(value, emailAddressPattern, "输入正确的Email格式!", false,
        errors);
  }

  /**
   * 验证指定字符串符合expression正则表达示<br/>
   * validateRegex("abcdef1234", "\\d", "请输入数字", false, errors);<br/>
   * 错误提示：<b>请输入数字</b>
   * @param value 指定字符串
   * @param expression 正则表达示
   * @param message 提示的错误信息
   * @param isCaseSensitive 验证时是否区分大小写
   * @param errors 错误信息
   */
  public void validateRegex(String value, String expression, String message,
      boolean isCaseSensitive, ActionErrors errors) {
    if (StringUtils.isBlank(value) || StringUtils.isBlank(expression)) {
      return;
    }

    String compare = ((String) value).trim();
    if (compare.length() == 0) {
      return;
    }

    Pattern pattern;
    if (isCaseSensitive) {
      pattern = Pattern.compile(expression);
    } else {
      pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
    }

    Matcher matcher = pattern.matcher(compare);
    if (!matcher.matches()) {
      errors.rejectDirectly("{0}", message);
    }
  }

  /**
   * 验证指定字符串是否为int类型的值<br/>
   * validateInt("123f", "年龄", errors);<br/>
   * 错误提示：<b>年龄必须为整数类型！</b>
   * @param value 指定字符串
   * @param field 字段名称
   * @param errors 错误信息
   */
  public void validateInt(String value, String field, ActionErrors errors) {
    if (StringUtils.isBlank(value)) {
      return;
    }

    String val = ((String) value).trim();
    try {
      Integer.parseInt(val);
    } catch (NumberFormatException e) {
      errors.rejectDirectly(VALIDATE_INT_MESSAGE, field);
    }
  }

  /**
   * 验证指定字符串是否为日期<br/>
   * validateDate("2008361521090", "请输入正确的日期", errors);<br/>
   * 错误提示：<b>请输入正确的日期</b>
   * @param value 指定字符串
   * @param field 字段名称
   * @param errors 错误信息
   */
  public void validateDate(String value, String field, ActionErrors errors) {
    if (StringUtils.isBlank(value)) {
      return;
    }

    String val = ((String) value).trim();
    SimpleDateFormat dateFormat = new SimpleDateFormat();
    try {
      dateFormat.parse(val);
    } catch (ParseException e) {
      errors.rejectDirectly(VALIDATE_DATE_MESSAGE, field);
    }
  }
}
