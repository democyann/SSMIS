package com.systop.core.webapp.context;
/**
 * 用于标识"可以从Spring中获取对象"的Object
 * @author Sam
 *
 */
public interface ApplicationContextAwareObject {
  /**
   * 根据指定的名字从ApplicationContext中获取一个Bean
   * @param beanName 给定的名字
   * @return Spring management bean.
   */
  Object getBean(String beanName);
}
