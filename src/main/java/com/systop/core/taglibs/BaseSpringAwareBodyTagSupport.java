package com.systop.core.taglibs;

import javax.servlet.ServletContext;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.systop.core.webapp.context.ApplicationContextAwareObject;
/**
 * 能够“获取Spring管理的bean”的Taglib的基类.
 * @author Sam
 *
 */
@SuppressWarnings("serial")
public class BaseSpringAwareBodyTagSupport extends BodyTagSupport implements
    ApplicationContextAwareObject {
	
  /**
   * log of the class
   */
	protected Logger logger = LoggerFactory.getLogger(getClass());
  
  /**
   * 从spring中获取一个bean.
   */
  public Object getBean(String beanName) {
    ServletContext servletCtx = pageContext.getServletContext();
    ApplicationContext ctx = WebApplicationContextUtils
        .getWebApplicationContext(servletCtx);
    return ctx.getBean(beanName);
  }
  
}
