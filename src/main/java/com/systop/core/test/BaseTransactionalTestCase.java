package com.systop.core.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit38.AbstractTransactionalJUnit38SpringContextTests;

/**
 * 带有事务支持的单元测试基类，包括以下特征：<br>
 * <ul>
 *   <li>提供dataSource和simpleJdbcTemplate对象以方便子类使用(必须在context定义
 *       <code>javax.jdbc.DataSource</code>)。</li>
 *   <li>子类可以直接使用<code>logger</code>输出日志。</li>
 *   <li>如果需要使用不同的applicationContext，子类要重新声明<code>@ContextConfiguration</code>。</li>
 *   <li>子类定义的属性通过在setter上注明@Autowire就可以直接获得spring中的bean.</li>
 * </ul>
 * 
 * @author Sam Lee
 * 
 */
@ContextConfiguration(locations = { "classpath*:applicationContext-*.xml" })
public class BaseTransactionalTestCase extends AbstractTransactionalJUnit38SpringContextTests {
  /**
   * 为子类提供Logger支持
   */
  protected Logger logger = LoggerFactory.getLogger(getClass());

}
