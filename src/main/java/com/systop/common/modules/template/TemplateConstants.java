package com.systop.common.modules.template;

import com.systop.core.Constants;
import com.systop.core.util.ResourceBundleUtil;

public final class TemplateConstants {
  
  /**
   * Freemark模板中dataModel的名字
   */
  public static final String DEFAULT_FREEMARKER_DATAMODEL_NAME = 
    ResourceBundleUtil
      .getString(Constants.RESOURCE_BUNDLE, "global.freemarker.dataModelName", "data");

  /**
   * 缺省的模板目录名称
   */
  public static final String DEFAULT_TEMPLATE_DIR = ResourceBundleUtil
      .getString(Constants.RESOURCE_BUNDLE, "global.templates.defaultDir",
          "classpath:templates");

  /**
   * 缺省的模板主题
   */
  public static final String DEFAULT_TEMPLATE_THEME = ResourceBundleUtil
      .getString(Constants.RESOURCE_BUNDLE, "global.freemarker.defaultTheme", "simple");

  /**
   * ConfigurationName of jconfig
   */
  public static final String DEFAULT_CONFIGURATION_NAME = ResourceBundleUtil
  .getString(Constants.RESOURCE_BUNDLE, "global.jconfig.configurationName", "application");

  private TemplateConstants() {

  }
}
