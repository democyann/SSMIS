package com.systop.core.dao.hibernate.selector;

import org.hibernate.criterion.Example.PropertySelector;
import org.hibernate.type.Type;

/**
 * 链式PropertySelector，可以组合使用多种属性选择策略。
 * 
 * @author Sam Lee
 * 
 */
@SuppressWarnings("serial")
public abstract class AbstractChainPropertySelector implements PropertySelector {
	/**
	 * 实际的<code>PropertySelectors</code>，由它执行真正的include方法。
	 */
	private PropertySelector selector;

	/**
	 * 空构造器
	 */
	public AbstractChainPropertySelector() {
		this.selector = null;
	}

	/**
	 * 构造器，传入委派对象
	 * 
	 * @param selector
	 */
	public AbstractChainPropertySelector(PropertySelector selector) {
		this.selector = selector;
	}

	/**
	 * @see {@link PropertySelectors#include(Object, String, Type)}
	 */
	public final boolean include(Object propertyValue, String propertyName,
			Type type) {
		if (selector == null) {
			return internalInclude(propertyValue, propertyName, type);
		} else {
			return selector.include(propertyValue, propertyName, type)
					&& internalInclude(propertyValue, propertyName, type);
		}
	}

	/**
	 * 执行实际的选择策略
	 * 
	 * @see {@link PropertySelectors#include(Object, String, Type)}
	 */
	protected abstract boolean internalInclude(Object propertyValue,
			String propertyName, Type type);
}
