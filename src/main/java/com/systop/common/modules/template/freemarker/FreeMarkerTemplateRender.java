package com.systop.common.modules.template.freemarker;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.systop.common.modules.template.Template;
import com.systop.common.modules.template.TemplateConstants;
import com.systop.common.modules.template.TemplateContext;
import com.systop.common.modules.template.TemplateRender;
import com.systop.core.ApplicationException;
import com.systop.core.util.ReflectUtil;

import freemarker.template.Configuration;
import freemarker.template.SimpleHash;
import freemarker.template.TemplateException;

/**
 * FreeMarker Template render
 * @author Sam Lee
 * 
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
@Component
public class FreeMarkerTemplateRender implements TemplateRender {
  /**
   * Log of the FreeMarkerTemplateRender
   */
  private static Logger logger = LoggerFactory.getLogger(FreeMarkerTemplateRender.class);

  /**
   * 用于获得Configruation对象
   */
  private FreeMarkerConfigurationManager configurationManager;

  /**
   * @see {@link TemplateRender#renderTemplate(TemplateContext)}
   */

  public void renderTemplate(TemplateContext templateContext) throws ApplicationException {
    String[] templateDir = parseSpringStyleDir(templateContext);
    configurationManager.setTemplateLoaderPaths(templateDir);
    try {
      Configuration cfg = configurationManager.createConfiguration();
      String templateName = parseTemplateName(templateContext.getTemplate().getTemplateName());
      
      freemarker.template.Template template = cfg.getTemplate(templateName);
      if (template == null) {
        logger.error("Template {} is null", templateName);
        return;
      }

      Map data = null;
      // 写入template的各种属性
      data = ReflectUtil.toMap(templateContext.getTemplate(), null, true);
      if (data == null) {
    	  data = new HashMap();
      }
      // BeanUtils.populate(templateContext.getTemplate(), data);
      logger.debug("Load " + data.size() + " parameters from Template");
      // 写入template的各种数据
      data.putAll(templateContext.getParams());
      logger.debug("Load " + templateContext.getParams().size() + " parameters from TemplateContext");
      logger.debug("total paramters " + data.size());

      SimpleHash freemarkerData = new SimpleHash();
      freemarkerData.put(TemplateConstants.DEFAULT_FREEMARKER_DATAMODEL_NAME, data);

      // 处理Template，得到String类型数据.
      String templateStr = FreeMarkerTemplateUtils.processTemplateIntoString(template,
          freemarkerData);
      
      templateContext.getWriter().write(templateStr);
      templateContext.getWriter().flush();
    } catch (IOException e) {
      e.printStackTrace();
      logger.error("An IOException has occurs. {}", e.getMessage());
      throw new ApplicationException("An IOException has occurs." + e.getMessage());
    } catch (TemplateException e) {
      e.printStackTrace();
      logger.error("An TemplateException has occurs. {}", e.getMessage());
      throw new ApplicationException("An TemplateException has occurs." + e.getMessage());
    }
  }

  /**
   * 解析模板名称，添加.ftl扩展名
   */
  static final String parseTemplateName(String name) {
    if (StringUtils.isBlank(name)) {
      logger.error("Empty template name");
      return StringUtils.EMPTY;
    }
    if (!name.endsWith(".ftl")) {
      name = name + ".ftl";
    }
    return name;
  }

  /**
   * 解析
   * @param ctx
   * @return
   */
  static final String[] parseSpringStyleDir(TemplateContext ctx) {
    if (ctx == null || ctx.getTemplate() == null) {
      logger.error("TemplateContext or Template is null.");
      return null;
    }

    String dir = ctx.getTemplate().getTemplateDir();

    if (dir.startsWith(Template.CLASSPATH_PREFIX)) { // 以classpath:开头
      logger.debug("Load template form ClassPath." + dir);
    } else if (dir.startsWith(Template.WEB_DIR_PREFIX)) { // 以web:开头
      dir = dir.substring(Template.WEB_DIR_PREFIX.length()); // 删除web:前缀
      // 开头补充一个"/"
      if (!dir.startsWith("/") || !dir.startsWith("\\")) {
        dir = "/" + dir;
      }

      dir = ctx.getServletContext().getRealPath(dir);
      dir = "file:" + dir;
      logger.debug("Load template from file:" + dir);
    } else { // 什么开头都没有，则认为以classpath:开头
      dir = "Template.CLASSPATH_PREFIX" + dir;
    }

    return new String[] { dir, dir + "/" + ctx.getTemplate().getTheme() };
  }

  /**
   * @return the configurationManager
   */
  public FreeMarkerConfigurationManager getConfigurationManager() {
    return configurationManager;
  }

  /**
   * @param configurationManager the configurationManager to set
   */
  @Autowired
  public void setConfigurationManager(FreeMarkerConfigurationManager configurationManager) {
    this.configurationManager = configurationManager;
  }

}
