package com.systop.common.modules.security.acegi.acl.voter;

import org.acegisecurity.vote.BasicAclEntryVoter;

import com.systop.common.modules.security.acegi.acl.AclUtils;

/**
 * 扩展了BasicAclEntryVoter，对于拦截方法中的参数仅仅为id的情况作了处理。
 * 
 * @author sshwsfc@gmail.com
 * @see AclUtils#getDomainObjectInstance(Object, Class)
 * @see BasicAclEntryVoter
 */
@SuppressWarnings("rawtypes")
public class IdParameterAclEntryVoter extends BasicAclEntryVoter {

	/**
	 * Domain class
	 */
	private Class domainClass;
	/**
	 * Domain method
	 */
	private String methodDomainObjectClass;

	/**
	 * @see {@link org.acegisecurity.vote.AbstractAclVoter# getDomainObjectInstance}
	 */
	@Override
	protected Object getDomainObjectInstance(Object secureObject) {
		return AclUtils.getDomainObjectInstance(secureObject,
				methodDomainObjectClass, domainClass);
	}

	public Class getDomainClass() {
		return domainClass;
	}

	public void setDomainClass(Class domainClass) {
		this.domainClass = domainClass;
	}

	public String getMethodDomainObjectClass() {
		return methodDomainObjectClass;
	}

	public void setMethodDomainObjectClass(String methodDomainObjectClass) {
		this.methodDomainObjectClass = methodDomainObjectClass;
	}
}
