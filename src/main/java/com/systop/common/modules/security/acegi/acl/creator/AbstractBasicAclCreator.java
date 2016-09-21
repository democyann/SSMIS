package com.systop.common.modules.security.acegi.acl.creator;

import org.acegisecurity.Authentication;
import org.acegisecurity.ConfigAttribute;
import org.acegisecurity.ConfigAttributeDefinition;
import org.acegisecurity.acl.basic.AclObjectIdentity;
import org.acegisecurity.acl.basic.BasicAclExtendedDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.systop.common.modules.security.acegi.acl.AclCreator;

/**
 * 为创建acl信息提供了抽象的实现，仅仅提供了一些属性。
 * 
 * @author sshwsfc@gmail.com
 * @see SimpleAclCreator
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public abstract class AbstractBasicAclCreator implements AclCreator {
  /**
   * Log for the class
   */
  protected Logger logger = LoggerFactory.getLogger(getClass());
  /**
   * 访问ACL表的Dao对象
   */
  protected BasicAclExtendedDao aclDAO;
  
  /**
   * 处理的DomainObject的类
   */
  protected Class processDomainObjectClass;
  /**
   * 处理的返回对象的类
   */
  protected Class processReturnedObjectClass;
  /**
   * 缺省的标识对象的类
   */
  protected Class defaultAclObjectIdentityClass;

  public Class getProcessReturnedObjectClass() {
    return processReturnedObjectClass;
  }

  /**
   * 什么样的返回类型需要处理，可为空
   * 
   * @param processReturnedObjectClass
   */
  public void setProcessReturnedObjectClass(Class processReturnedObjectClass) {
    this.processReturnedObjectClass = processReturnedObjectClass;
  }

  public Class getDefaultAclObjectIdentityClass() {
    return defaultAclObjectIdentityClass;
  }

  /**
   * 默认的AclObjectIdentity
   * 
   * @param defaultAclObjectIdentityClass
   */
  public void setDefaultAclObjectIdentityClass(
      Class defaultAclObjectIdentityClass) {
    this.defaultAclObjectIdentityClass = defaultAclObjectIdentityClass;
  }

  public Class getProcessDomainObjectClass() {
    return processDomainObjectClass;
  }

  /**
   * 什么样的domainClass需要处理
   * 
   * @param processDomainObjectClass
   */
  public void setProcessDomainObjectClass(Class processDomainObjectClass) {
    this.processDomainObjectClass = processDomainObjectClass;
  }

  public BasicAclExtendedDao getAclDAO() {
    return aclDAO;
  }

  public void setAclDAO(BasicAclExtendedDao aclDAO) {
    this.aclDAO = aclDAO;
  }
  
  /**
   * 
   */
  public void afterPropertiesSet() {
    Assert.notNull(aclDAO, "basicAclDao required");
    Assert.notNull(processDomainObjectClass,
        "processDomainObjectClass required");
    Assert.notNull(defaultAclObjectIdentityClass,
        "defaultAclObjectIdentityClass required");
    Assert.isTrue(AclObjectIdentity.class
        .isAssignableFrom(this.defaultAclObjectIdentityClass),
        "defaultAclObjectIdentityClass must implement AclObjectIdentity");

    try {
      defaultAclObjectIdentityClass
          .getConstructor(new Class[] { Object.class });
    } catch (NoSuchMethodException nsme) {
      throw new IllegalArgumentException(
          "defaultAclObjectIdentityClass must provide a constructor "
          + "that accepts the domain object instance!");
    }
  }
  /**
   * @see AclCreator#creat(Authentication, Object,
   *  ConfigAttributeDefinition, Object)
   */
  public abstract void creat(Authentication authentication, Object object,
      ConfigAttributeDefinition config, Object returnedObject);
  /**
   * @see AclCreator#supports(Class)
   */
  public boolean supports(ConfigAttribute attribute) {
    return true;
  }
  /**
   * @see AclCreator#supports(ConfigAttribute)
   */
  public boolean supports(Class clazz) {
    return true;
  }
  /**
   * @see AclCreator#supports(Object, Object)
   */
  public boolean supports(Object domainObject, Object returnedObject) {
    return true;
  }

}
