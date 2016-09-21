package com.systop.common.modules.security.acegi.acl;

import org.acegisecurity.Authentication;
import org.acegisecurity.ConfigAttribute;
import org.acegisecurity.ConfigAttributeDefinition;

/**
 * Creator在domainObject创建的时候创建domainObject的ACL信息.
 * 
 * @author sshwsfc@gmail.com
 */
@SuppressWarnings("rawtypes")
public interface AclCreator {
	/**
	 * 创建一个DomainObject的ACL信息
	 * 
	 * @param authentication
	 *            认证信息，包括了当前用户的属性。{@link Authentication}
	 * @param object
	 *            被访问的DomainObject对象
	 * @param config
	 *            相关的安全属性，比如用户或权限
	 * @param returnedObject
	 *            创建的对象
	 */
	public void creat(Authentication authentication, Object object,
			ConfigAttributeDefinition config, Object returnedObject);

	/**
	 * 是否支持给定的安全属性
	 */
	public boolean supports(ConfigAttribute attribute);

	/**
	 * 是否支持给定的类
	 */
	public boolean supports(Class clazz);

	/**
	 * 是否支持给定的domainObject
	 */
	public boolean supports(Object domainObject, Object returnedObject);
}
