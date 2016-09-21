package com.systop.common.modules.security.acegi.acl.domain;

import org.acegisecurity.acl.basic.AclObjectIdentity;
import org.acegisecurity.acl.basic.AclObjectIdentityAware;
import org.acegisecurity.acl.basic.NamedEntityObjectIdentity;

import java.io.Serializable;

/**
 * 实现了AclObjectIdentityAware接口，这样在后期构造AclObjectIdentity
 * 的时候就可以直接调用该接口的getAclObjectIdentity方法取得
 * 
 * @author sshwsfc@gmail.com
 * @see AclObjectIdentityAware
 * @see AclObjectIdentity
 * @see org.acegisecurity.acl.basic.BasicAclProvider
 */

@SuppressWarnings("rawtypes")
public class AclDomainBuilder implements AclObjectIdentityAware {
	/**
	 * Domain Class
	 */
	private Class domainClass;
	/**
	 * Domain object's id
	 */
	private Serializable id;

	/**
	 * Full Constructor
	 */
	public AclDomainBuilder(Class domainClass, Serializable id) {
		this.domainClass = domainClass;
		this.id = id;
	}

	public AclObjectIdentity getAclObjectIdentity() {
		return new NamedEntityObjectIdentity(domainClass.getName(),
				id.toString());
	}

	public Class getDomainClass() {
		return domainClass;
	}

	public void setDomainClass(Class domainClass) {
		this.domainClass = domainClass;
	}

	public Serializable getId() {
		return id;
	}

	public void setId(Serializable id) {
		this.id = id;
	}

}
