package com.systop.core.dao.hibernate.selector;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Example.PropertySelector;
import org.hibernate.type.Type;

/**
 * 用于Hibernate Example查询，可以剔除值为<code>null</code>和零长度字符串的属性。
 * @author Sam Lee
 * @see {@link PropertySelectors}
 */
@SuppressWarnings("serial")
public class NotEmptyStringPropertySelector
  extends AbstractChainPropertySelector {
  /**
   * Empty Constructor
   */
  public NotEmptyStringPropertySelector() {
    super();
  }
  /**
   * 可以传入下一个selector的Constructor
   */
  public NotEmptyStringPropertySelector(PropertySelector selector) {
    super(selector);
  }
  /**
   * @see {@link AbstractChainPropertySelector#internalInclude(
   *   Object, String, Type)}
   * @see {@link PropertySelectors#include(Object, String, Type)}
   */
  public boolean internalInclude(Object propertyValue, String propertyName,
      Type type) {
    if (propertyValue == null) {
      return false;
    }
    if ((propertyValue instanceof String)
        && propertyValue.equals(StringUtils.EMPTY)) {
      return false;
    }

    return true;
  }

}
