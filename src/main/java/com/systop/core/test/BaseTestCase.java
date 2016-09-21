package com.systop.core.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit38.AbstractJUnit38SpringContextTests;
/**
 * AbstractJUnit38SpringContextTests的简写，标注了缺省的@ContextConfiguration，提供
 * logger对象以支持日志。通过@Autowired注解，可以从Spring Context中直接获得bean。
 * @author Sam Lee
 */
@ContextConfiguration(locations = { "classpath*:spring/applicationContext-*.xml" })
public class BaseTestCase extends AbstractJUnit38SpringContextTests {
  /**
   * 为子类提供Logger支持
   */
  protected Logger logger = LoggerFactory.getLogger(getClass());
}
