package com.systop.common.modules.template;


/**
 * A <code>Template</code> is used as a model for rendering output.
 * This object contains basic common template information
 * @author SAM
 *
 */
public class Template {
  /**
   * 位于classpath下的目录的前缀。
   */
  public static final String CLASSPATH_PREFIX = "classpath:";

  /**
   * 位于web下的目录的前缀.
   */
  public static final String WEB_DIR_PREFIX = "web:";
  
  /**
   * The templates directory.
   */
  private String templateDir = TemplateConstants.DEFAULT_TEMPLATE_DIR;
  /**
   * the theme (sub dir) of the template
   */
  private String theme = TemplateConstants.DEFAULT_TEMPLATE_THEME;
  /**
   * the name of the template
   */
  private String templateName;
  /**
   * full constructor
   */
  public Template(String templateDir, String theme, String templateName) {
    if (templateDir != null) {
      this.templateDir = templateDir;
    }
    this.theme = theme;
    this.templateName = templateName;
  }
  
  public Template(String templateName) {
    this.templateName = templateName;
  }
  
  public Template() {
  }
  
  
  /**
   * @param templateName the templateName to set
   */
  public void setTemplateName(String templateName) {
    this.templateName = templateName;
  }

  /**
   * @return the dir
   */
  public String getTemplateDir() {
    return templateDir;
  }
  /**
   * @return the name
   */
  public String getTemplateName() {
    return templateName;
  }
  /**
   * @return the theme
   */
  public String getTheme() {
    return theme;
  }
  
  /**
   * Constructs a string in the format <code>/dir/theme/name</code>.
   * @return a string in the format <code>/dir/theme/name</code>.
   */
  public String toString() {
      return "/" + templateDir + "/" + theme + "/" + templateName;
  }
}
