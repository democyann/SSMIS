package com.systop.core.util;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
/**
 * Utils for HttpServletRequest.
 * @author SAM
 *
 */
public final class RequestUtil {
  private RequestUtil() {
  }

  /**
   * 根据Request信息判断是否是请求一个json对象。Header的Accept属性中如果
   * 包含application/x-json或text/x-json,则表示请求json对象。如果Request
   * 的参数Accept为x-json，也表示请求一个json对象。客户端可以这样设置：
   * <pre>
   * XMLHttpRequest xhr = ...;
   * xhr.setHeader("Accept", "text/x-json;charset=UTF-8");
   * </pre>
   * 如果使用extjs<br>
   * <pre>
   * Ext.Ajax.defaultHeaders = {
    'Accept': 'application/x-json;text/x-json;charset=UTF-8'
    };
   * </pre>
   * 如果使用jquery:<br>
   * <pre>
   * $.ajax({ url:'user/index.do',
   *      data: {'model.name':'sam'},
   *      async: true,
   *      <B>beforeSend: function(xhr) {xhr.setRequestHeader('Accept': 'application/x-json;text/x-json;charset=UTF-8');}</B>
   * });
   * </pre> 
   * 如果不使用Ajax 方式:<br>
   * <pre>
   * your-url.jsp?Accept=x-json&..
   * </pre>
   * @return
   */
  public static boolean isJsonRequest(HttpServletRequest request) {
    String accept = request.getHeader("Accept");
    if(StringUtils.isBlank(accept)) {
      accept = request.getParameter("Accept");
      if(StringUtils.isBlank(accept)) {
        return false;
      }
    }
    accept = accept.toLowerCase();
    return (accept.contains("json"));
  }
}
