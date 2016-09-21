package com.systop.core.dao.hibernate.selector;

import org.hibernate.criterion.Example.PropertySelector;
import org.hibernate.type.Type;
/**
 * 不选择值为<code>null</code>的属性。
 * 与{@link org.hibernate.criterion.Example.NotNullPropertySelector}
 * 相同
 * @author Sam Lee
 *
 */
@SuppressWarnings("serial")
public class NotNullPropertySelector extends AbstractChainPropertySelector {
  /**
   * Empty Constructor
   */
  public NotNullPropertySelector() {
    super();
  }
  /**
   * 可以传入下一个selector的Constructor
   */
  public NotNullPropertySelector(PropertySelector selector) {
    super(selector);
  }
  /**
   * @see org.hibernate.criterion.Example.NotNullPropertySelector
   */
  @Override
  protected boolean internalInclude(Object propertyValue, String propertyName,
      Type type) {
    return propertyValue != null;
  }

}
