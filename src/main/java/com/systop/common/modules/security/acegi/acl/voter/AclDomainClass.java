package com.systop.common.modules.security.acegi.acl.voter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用在拦截方法上，表示该方法处理的domainClass
 * 
 * @author sshwsfc@gmail.com
 */
@SuppressWarnings("rawtypes")
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface AclDomainClass {
	/**
	 * 返回Domain对象的类型
	 */
	public Class value();
}
