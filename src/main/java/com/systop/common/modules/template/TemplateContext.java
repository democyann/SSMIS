package com.systop.common.modules.template;

import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

/**
 * Context used when rendering templates.
 * @author Sam Lee
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class TemplateContext {

  /**
   * Template contains common information.
   */
  private Template template;

  /**
   * Template渲染所需的各种数据，例如，可以在freeMarker中这样写：
   * 
   * <pre>
   *   ${ctx.getParams().datas}
   * </pre>
   */
  private Map params = new HashMap();

  /**
   * Template渲染所需的Request参数.
   */
  private HttpServletRequest request;

  /**
   * Template渲染所需的ServletContext参数.
   */
  private ServletContext servletContext;

  /**
   * 渲染Template所需的<code>Writer</code>
   */
  private Writer writer;

  /**
   * Web应用程序路径，例如：http://localhost:8080/web/
   */
  private String basePath;

  /**
   * @return the params
   */
  public Map getParams() {
    return params;
  }

  /**
   * @param params the params to set
   */
  public void setParams(Map params) {
    this.params = params;
  }

  /**
   * 向<code>params</code>中添加一个参数数据
   * @param name 参数名称
   * @param value 参数值
   */
  public void addParameter(String name, Object value) {
    if (params == null) {
      params = new HashMap();
    }

    params.put(name, value);
  }

  /**
   * 添加多个数据
   * @param parameters Map contains parameters
   */
  public void addParameters(Map parameters) {
    if (params == null) {
      params = new HashMap();
    }

    params.putAll(parameters);
  }

  /**
   * 根据指定的名称得到一个参数，如果该参数不存在，返回空字符串
   */
  public Object getParameter(String name) {
    if (params == null || !params.containsKey(name)) {
      return StringUtils.EMPTY;
    }

    return params.get(name);
  }

  /**
   * @return the request
   */
  public HttpServletRequest getRequest() {
    return request;
  }

  /**
   * @param request the request to set
   */
  public void setRequest(HttpServletRequest request) {
    this.request = request;
  }

  /**
   * @return the servletContext
   */
  public ServletContext getServletContext() {
    return servletContext;
  }

  /**
   * @param servletContext the servletContext to set
   */
  public void setServletContext(ServletContext servletContext) {
    this.servletContext = servletContext;
  }

  /**
   * @return the basePath
   */
  public String getBasePath() {
    return basePath;
  }

  /**
   * @param basePath the basePath to set
   */
  public void setBasePath(String basePath) {
    this.basePath = basePath;
  }

  /**
   * @return the writer
   */
  public Writer getWriter() {
    return writer;
  }

  /**
   * @param writer the writer to set
   */
  public void setWriter(Writer writer) {
    this.writer = writer;
  }

  /**
   * @return the template
   */
  public Template getTemplate() {
    return template;
  }

  /**
   * @param template the template to set
   */
  public void setTemplate(Template template) {
    this.template = template;
  }
}
