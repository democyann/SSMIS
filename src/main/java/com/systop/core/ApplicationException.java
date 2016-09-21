package com.systop.core;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * 业务异常基类.带有错误代码与错误信息. 用户在生成异常时既可以直接赋予错误代码与错误信息. 
 * 也可以只赋予错误代码与错误信息参数.如ErrorCode=ORDER.LACK_INVENTORY ,
 * errorArg=without EJB 系统会从errors.properties中查出 ORDER.LACK_INVENTORY=
 * Book <{0}> lack of inventory 最后返回错误信息为 Book 《without EJB》 lack of 
 * inventory.
 * 
 * <b>important:</b><br/>
 * <code>ApplicationException</code>是一个CheckedException，所有caller都必须
 * catch这个异常。所以，只有在下面情况下才可以extends<code>ApplicationException</code>
 * <ul>
 *   <li>确定调用者有能力处理这个Exception.</li>
 *   <li>所有调用者都必须处理这个Exception，换句话说，
 *   Exception相当于方法的另外一个返值。</li>
 *   <li>抛出(throws)这个异常，不会造成调用者太大的代码上的负担。</li>
 *   <li>以后不会修改这个异常。</li>
 * </ul>
 * @author calvin
 */
@SuppressWarnings("serial")
public class ApplicationException extends RuntimeException {
  
  /**
   * Logger
   */
  protected final Log log = LogFactory.getLog(ApplicationException.class); 
  /**
   * The default error code.
   */
  public static final String UNKNOW_ERROR = "UNKNOW_ERROR";
  /**
   * 错误代码,默认为未知错误.
   */
  private final String errorCode;

  /**
   * 错误信息中的参数.
   */
  private final String[] errorArgs;

  /**
   * 兼容纯错误信息，不含error code,errorArgs的情况.
   */
  private final String errorMessage;

  /**
   * 错误信息的i18n ResourceBundle. 
   * 默认Properties文件名定义于{@link Constants#ERROR_BUNDLE_KEY}
   */
  private static final ResourceBundle RB = ResourceBundle.getBundle(
      Constants.BUNDLE_KEY, LocaleContextHolder.getLocale());

  /**
   * @see RuntimeException#RuntimeException()
   */
  public ApplicationException() {
    super();
    errorCode = UNKNOW_ERROR;
    errorArgs = null;
    errorMessage = null;
  }

  /**
   * @see RuntimeException#RuntimeException(String)
   * @param errorCode 错误代码，是错误属性文件（eg.errors.properties）中的一个entry
   * @param errorArgs 错误信息中的参数
   */
  public ApplicationException(final String errorCode, 
      final String[] errorArgs) {
    this.errorCode = errorCode;
    this.errorArgs = errorArgs;
    this.errorMessage = null;
  }

  /**
   * 
   * @param errorCode  错误代码，是错误属性文件（eg.errors.properties）中的一个entry
   * @param errorArg  错误信息中的参数
   */
  public ApplicationException(final String errorCode, final String errorArg) {
    this.errorCode = errorCode;
    this.errorArgs = new String[] {errorArg};
    this.errorMessage = null;
  }

  /**
   * 
   * @param errorCode 错误代码，是错误属性文件（eg.errors.properties）中的一个entry
   * @param errorArgs 错误信息中的参数
   * @param cause the Thorwable of the exception
   */
  public ApplicationException(final String errorCode, final String[] errorArgs,
      final Throwable cause) {
    super(cause);
    this.errorCode = errorCode;
    this.errorArgs = errorArgs;
    this.errorMessage = null;
  }

  /**
   * 
   * @param errorCode 错误代码，是错误属性文件（eg.errors.properties）中的一个entry
   * @param errorArg 错误信息中的参数
   * @param cause  the Thorwable of the exception
   */
  public ApplicationException(final String errorCode, final String errorArg,
      final Throwable cause) {
    super(cause);
    this.errorCode = errorCode;
    this.errorArgs = new String[] {errorArg};
    this.errorMessage = null;
  }

  /**
   * @see RuntimeException#RuntimeException(String)
   */
  public ApplicationException(final String errorMessage) {
    this.errorMessage = errorMessage;
    errorCode = UNKNOW_ERROR;
    errorArgs = null;
  }
  /**
   * @see RuntimeException#RuntimeException(Throwable)
   */
  public ApplicationException(Throwable cause) {
    super(cause);
    this.errorCode = null;
    this.errorArgs = new String[] {};
    this.errorMessage = null;
  }
  /**
   * @see RuntimeException#RuntimeException(String, Throwable)
   */
  public ApplicationException(final String message, Throwable cause) {
    super(cause);
    this.errorCode = null;
    this.errorArgs = null;
    this.errorMessage = message;
  }

  /**
   * 获得出错信息. 读取i18N properties文件中Error Code对应的message,
   * 并组合参数获得i18n的出错信息.
   * 
   * @return 格式化后的错误信息
   */
  public final String getMessage() {
    // 如果errorMessage不为空,直接返回出错信息.
    if (errorMessage != null) {
      return errorMessage;
    }
    // 否则用errorCode查询Properties文件获得出错信息
    String message = null;
    try {
      if(errorCode != null) {
        message = RB.getString(errorCode);
      }
    } catch (MissingResourceException mse) {
      message = "ErrorCode is: " + errorCode
          + ", but can't get the message of the Error Code";
    }

    // 将出错信息中的参数代入到出错信息中
    if (errorArgs != null && message != null) {
      message = MessageFormat.format(message, (Object[]) errorArgs);
    }
    
    if(getCause() != null && getCause().getMessage() != null) {
      message = new StringBuffer(200).append(getCause().getMessage())
      .append("\n").append(message).toString();
    }

    return message;

  }

}
