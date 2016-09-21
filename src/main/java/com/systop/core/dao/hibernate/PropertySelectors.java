package com.systop.core.dao.hibernate;

import com.systop.core.dao.hibernate.selector.NotEmptyStringPropertySelector;
import com.systop.core.dao.hibernate.selector.NotNullPropertySelector;

/**
 * 列出常用的PropertySelector，便于使用
 * @author Sam Lee
 *
 */
public final class PropertySelectors {
  /**
   * 不包含null和空字符串的PropertySelector
   */
  public static final org.hibernate.criterion.Example.PropertySelector 
    EXCLUDE_BLANK_STRING = new NotNullPropertySelector(
        new NotEmptyStringPropertySelector());
  
  /**
   * 私有构造器，防止实例化
   */
  private PropertySelectors() {
    
  }
}
