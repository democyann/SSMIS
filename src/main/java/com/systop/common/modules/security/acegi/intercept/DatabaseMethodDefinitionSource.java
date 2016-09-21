package com.systop.common.modules.security.acegi.intercept;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.acegisecurity.ConfigAttributeDefinition;
import org.acegisecurity.ConfigAttributeEditor;
import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.intercept.method.MethodDefinitionSource;
import org.aopalliance.intercept.MethodInvocation;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.CodeSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.systop.common.modules.security.acegi.cache.AcegiCacheManager;
import com.systop.common.modules.security.acegi.resourcedetails.ResourceDetails;

/**
 * 从数据库中查询每一个<code>Method</code>的Resource.
 * @see {@link MethodDefinitionSource}
 * @author sam
 * 
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class DatabaseMethodDefinitionSource implements MethodDefinitionSource {
  /**
   * Log for this
   */
  private static Logger logger = LoggerFactory
      .getLogger(DatabaseMethodDefinitionSource.class);

  /**
   * <code>AcegiCacheManager</code> 用于从缓存中获取用户和资源的数据
   */
  private AcegiCacheManager acegiCacheManager;

  /**
   * 
   * @see {@link org.acegisecurity.intercept.
   *      ObjectDefinitionSource#getAttributes(Object)}
   * @see {@link org.acegisecurity.intercept.method.
   *   AbstractMethodDefinitionSource#getAttributes(Object)}
   * 
   */
  public ConfigAttributeDefinition getAttributes(Object object) {
    if (object == null) {
      return null;
    }

    if (object instanceof MethodInvocation) { //处理AOP方法拦截
      MethodInvocation miv = (MethodInvocation) object;
      return lookupAttributes(miv.getThis().getClass(), miv.getMethod());
    }

    if (object instanceof JoinPoint) { // 处理AspectJ方法拦截
      JoinPoint joinPoint = (JoinPoint) object;
      Class targetClazz = joinPoint.getTarget().getClass();
      String targetMethodName 
        = joinPoint.getStaticPart().getSignature().getName();
      Class[] types = ((CodeSignature) joinPoint.getStaticPart().getSignature())
          .getParameterTypes();

      if (logger.isDebugEnabled()) {
        logger.debug("Target Class: {}", targetClazz);
        logger.debug("Target Method Name: {}", targetMethodName);

        for (int i = 0; i < types.length; i++) {
          if (logger.isDebugEnabled()) {
            logger.debug("Target Method Arg #{}:{} " + i, types[i]);
          }
        }
      }

      try {
        return this.lookupAttributes(targetClazz, targetClazz.getMethod(
            targetMethodName, types));
      } catch (NoSuchMethodException nsme) {
        throw new IllegalArgumentException(
            "Could not obtain target method from JoinPoint: " + joinPoint);
      }
    }

    throw new IllegalArgumentException(
        "Object must be a MethodInvocation or JoinPoint");

  }

  /**
   * @see {@link org.acegisecurity.intercept.
   *      ObjectDefinitionSource#getConfigAttributeDefinitions()}
   */
  public Iterator getConfigAttributeDefinitions() {
    return null;
  }

  /**
   * @see {@link org.acegisecurity.intercept.
   *      ObjectDefinitionSource#supports(Class)}
   */
  public boolean supports(Class clazz) {
    return (MethodInvocation.class.isAssignableFrom(clazz) 
        || JoinPoint.class.isAssignableFrom(clazz));
  }

  /**
   * 从缓存中找到method对应的ResoruceDetails，并且将其关联的GrantedAuthority
   * 组装成ConfigAttributeDefinition
   * @param clazz 被访问的类
   * @param method 被访问类的方法
   * 
   */
  protected ConfigAttributeDefinition lookupAttributes(Class clazz,
      Method method) {
    if (clazz == null || method == null) {
      return null;
    }
    acegiCacheManager.initResourceCache(); //初始化资源缓存
    // 所有定义的function资源string
    List<String> methodStrings = acegiCacheManager.getFunctions();
    if (methodStrings == null || methodStrings.size() == 0) {
      logger.error("There is no function resources found.");
      return null;
    }
        
    Set<GrantedAuthority> auths = new HashSet();

    for (String methodString : methodStrings) {
      if (isMatch(clazz, method, methodString)) { // 如果访问的资源与定义的资源匹配
        // 资源所对应的ResourceDetails对象
        ResourceDetails resourceDetails = acegiCacheManager
            .getResourceFromCache(methodString);
        if (resourceDetails == null) {
          break;
        }
        // 将资源对应的权限保存
        GrantedAuthority[] authorities = resourceDetails.getAuthorities();
        if (authorities == null || authorities.length == 0) {
          break;
        }
        auths.addAll(Arrays.asList(authorities));
      }
    }
    if (auths.size() == 0) {
      logger.debug("Auth of {} is null", method.getName());
      return null;
    }

    ConfigAttributeEditor configAttrEditor = new ConfigAttributeEditor();
    StringBuffer authoritiesStr = new StringBuffer(" ");

    for (GrantedAuthority authority : auths) {
      authoritiesStr.append(authority.getAuthority()).append(",");
    }

    String authStr = authoritiesStr.substring(0, authoritiesStr.length() - 1);
    logger.debug("All authority of Method '{}' is {}", method.getName(), authStr);
    configAttrEditor.setAsText(authStr);
    return (ConfigAttributeDefinition) configAttrEditor.getValue();
  }

  /**
   * Return if the given method name matches the mapped name. The default
   * implementation checks for "xxx" and "xxx" matches.
   */
  public static boolean isMatch(Class clszz, Method mi, String methodString) {
    boolean isMatch = true;
    try {
      int lastDotIndex = methodString.lastIndexOf(".");
      String className = methodString.substring(0, lastDotIndex);
      String methodName = methodString.substring(lastDotIndex + 1);

      // 判断类是否相等
      if (!clszz.getName().equals(className)) {
        isMatch = false;
      }

      // 判断接口是否相等
      Class[] interfaces = clszz.getInterfaces();
      for (int i = 0; i < interfaces.length; i++) {
        Class inf = interfaces[i];
        if (inf.getName().equals(className)) {
          isMatch = true;
        }
      }

      // 判断方法是否相等
      if (isMatch
          && !(mi.getName().equals(methodName)
              || (methodName.endsWith("*") && mi.getName().startsWith(
                  methodName.substring(0, methodName.length() - 1))) 
              || (methodName.startsWith("*") 
                  && mi.getName().endsWith(
                      methodName.substring(1, methodName.length())))
              )
         ) {
        isMatch = false;
      }

    } catch (Exception e) {
      isMatch = false;
    }
    return isMatch;
  }

  /**
   * @param acegiCacheManager the acegiCacheManager to set
   */
  public void setAcegiCacheManager(AcegiCacheManager acegiCacheManager) {
    this.acegiCacheManager = acegiCacheManager;
  }
}
