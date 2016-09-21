package com.systop.common.modules.security.acegi.acl.afterinvocation;

import java.util.Iterator;
import java.util.List;

import org.acegisecurity.AcegiMessageSource;
import org.acegisecurity.Authentication;
import org.acegisecurity.ConfigAttribute;
import org.acegisecurity.ConfigAttributeDefinition;
import org.acegisecurity.acl.basic.NamedEntityObjectIdentity;
import org.acegisecurity.afterinvocation.AfterInvocationProvider;
import org.acegisecurity.afterinvocation.BasicAclEntryAfterInvocationProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.util.Assert;

import com.systop.common.modules.security.acegi.acl.AclCreator;
import com.systop.common.modules.security.acegi.acl.AclUtils;

/**
 * 当有acl对象被创建的时候，需要创建该对象对应的acl信息。
 * 
 * @author sshwsfc@gmail.com
 * @see AfterInvocationProvider
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class CreatAclEntryAfterInvocationProvider implements
		AfterInvocationProvider, InitializingBean, MessageSourceAware {
	// ~ Static fields/initializers
	// =============================================
	/**
	 * Log for the class
	 */
	protected static Logger logger = LoggerFactory
			.getLogger(BasicAclEntryAfterInvocationProvider.class);

	// ~ Instance fields
	// ========================================================
	/**
	 * Domain class
	 */
	private Class processDomainObjectClass;

	/**
	 * Message
	 */
	protected MessageSourceAccessor messages = AcegiMessageSource.getAccessor();

	/**
	 * Default Acl Object Identity Class
	 */
	private Class defaultAclObjectIdentityClass = NamedEntityObjectIdentity.class;

	/**
	 * 权限
	 */
	private String processConfigAttribute = "AFTER_ACL_CREAT";

	/**
	 * Creators
	 * 
	 * @see Creator
	 */
	private List creators;

	public List getCreators() {
		return creators;
	}

	/**
	 * 创建domain对象acl权限信息的执行者是Creator， 每个不同的creator可能对应不同的domain，或使用不同的创建规则。
	 * 
	 * @param newList
	 */
	public void setCreators(List newList) {
		if ((newList == null) || (newList.size() == 0)) {
			throw new IllegalArgumentException(
					"A list of AfterInvocationProviders is required");
		}

		Iterator iter = newList.iterator();

		while (iter.hasNext()) {
			Object currentObject = null;
			currentObject = iter.next();
			if (!(currentObject instanceof AclCreator)) {
				throw new IllegalArgumentException("AclCreator "
						+ currentObject.getClass().getName()
						+ " must implement AclCreator");
			}
		}

		this.creators = newList;
	}

	/**
	 * Allows selection of the <code>AclObjectIdentity</code> class that an
	 * attempt should be made to construct if the passed object does not
	 * implement <code>AclObjectIdentityAware</code>.
	 * <p/>
	 * <p/>
	 * NB: Any <code>defaultAclObjectIdentityClass</code><b>must</b> provide a
	 * public constructor that accepts an <code>Object</code>. Otherwise it is
	 * not possible for the <code>BasicAclProvider</code> to try to create the
	 * <code>AclObjectIdentity</code> instance at runtime.
	 * 
	 * 
	 * @param defaultAclObjectIdentityClass
	 */
	public void setDefaultAclObjectIdentityClass(
			Class defaultAclObjectIdentityClass) {
		this.defaultAclObjectIdentityClass = defaultAclObjectIdentityClass;
	}

	public Class getDefaultAclObjectIdentityClass() {
		return defaultAclObjectIdentityClass;
	}

	public Class getProcessDomainObjectClass() {
		return processDomainObjectClass;
	}

	public void setProcessDomainObjectClass(Class processDomainObjectClass) {
		this.processDomainObjectClass = processDomainObjectClass;
	}

	/**
	 * @see InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(processConfigAttribute,
				"A processConfigAttribute is mandatory");
		Assert.notNull(messages, "A message source must be set");
		Assert.notNull(processDomainObjectClass,
				"processDomainObjectClass is mandatory");
		Assert.notNull(defaultAclObjectIdentityClass,
				"defaultAclObjectIdentityClass required");

		try {
			defaultAclObjectIdentityClass
					.getConstructor(new Class[] { Object.class });
		} catch (NoSuchMethodException nsme) {
			throw new IllegalArgumentException(
					"defaultAclObjectIdentityClass must provide a "
							+ "constructor that accepts the domain object instance!");
		}
	}

	public String getProcessConfigAttribute() {
		return processConfigAttribute;
	}

	public void setProcessConfigAttribute(String processConfigAttribute) {
		this.processConfigAttribute = processConfigAttribute;
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messages = new MessageSourceAccessor(messageSource);
	}

	/**
	 * @see AfterInvocationProvider
	 */
	public Object decide(Authentication authentication, Object object,
			ConfigAttributeDefinition config, Object returnedObject) {
		Iterator iter = config.getConfigAttributes();

		while (iter.hasNext()) {
			ConfigAttribute attr = (ConfigAttribute) iter.next();
			if (this.supports(attr)) {

				// 取得domain对象，即刚刚创建的对象
				Object domainObject = AclUtils.getDomainObjectInstance(object,
						processDomainObjectClass);

				Iterator cit = this.creators.iterator();
				while (cit.hasNext()) {
					AclCreator creator = (AclCreator) cit.next();
					if (creator.supports(domainObject, returnedObject)) {
						// 创建acl规则信息
						creator.creat(authentication, domainObject, config,
								returnedObject);
						break;
					}
				}

			}
		}

		return returnedObject;
	}

	/**
	 * @see AfterInvocationProvider#supports(ConfigAttribute)
	 */
	public boolean supports(ConfigAttribute attribute) {
		return (attribute.getAttribute() != null)
				&& attribute.getAttribute().equals(getProcessConfigAttribute());
	}

	/**
	 * This implementation supports any type of class, because it does not query
	 * the presented secure object.
	 * 
	 * @param clazz
	 *            the secure object
	 * @return always <code>true</code>
	 */
	public boolean supports(Class clazz) {
		return true;
	}

}
