package com.systop.common.modules.security.acegi.acl;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.acegisecurity.AuthorizationServiceException;
import org.acegisecurity.acl.basic.AclObjectIdentity;
import org.acegisecurity.acl.basic.AclObjectIdentityAware;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.systop.common.modules.security.acegi.acl.domain.AclDomainBuilder;
import com.systop.common.modules.security.acegi.acl.voter.AclDomainClass;

/**
 * @author sshwsfc@gmail.com
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public final class AclUtils {
	/**
	 * 阻止实例化
	 */
	private AclUtils() {
	}

	/**
	 * Log for the class
	 */
	protected static Logger logger = LoggerFactory.getLogger(AclUtils.class);

	/**
	 * 取得domain对象
	 * 
	 * @param secureObject
	 *            拦截的对象，一般是业务对象的方法
	 * @param processDomainObjectClass
	 *            需要处理的doaminClass
	 * @return 根据processDomainObjectClass返回domainObject
	 */
	public static Object getDomainObjectInstance(Object secureObject,
			Class processDomainObjectClass) {
		MethodInvocation invocation = (MethodInvocation) secureObject;

		// Check if this MethodInvocation provides the required argument
		Method method = invocation.getMethod();
		Class[] params = method.getParameterTypes();

		for (int i = 0; i < params.length; i++) {
			if (processDomainObjectClass.isAssignableFrom(params[i])) {
				return invocation.getArguments()[i];
			}
		}

		throw new AuthorizationServiceException("MethodInvocation: "
				+ invocation + " did not provide any argument of type: "
				+ processDomainObjectClass);
	}

	/**
	 * 根据传入的参数构造一个AclDomainBuilder，可以在后期取得AclObjectIdentity，
	 * 指定domainObject首先会看传入的methodDomainObjectClass时候有效，
	 * 有效则调用该方法取得domainClass。无效则察看改方法有无AclDomainClass这个anno，
	 * 有的话则调用取得doaminClass。
	 * <p/>
	 * 该方法假设拦截方法的参数是一个id，无法取得domainClass时使用。
	 * 
	 * @param secureObject
	 *            拦截的对象，一般是业务对象的方法
	 * @param methodDomainObjectClass
	 *            取得domainClass的方法，可为空。
	 * @param defaultDomainClass
	 *            默认的domainClass
	 * @return 返回一个AclDomainBuilder
	 */
	public static AclDomainBuilder getDomainObjectInstance(Object secureObject,
			String methodDomainObjectClass, Class defaultDomainClass) {
		MethodInvocation invocation = (MethodInvocation) secureObject;

		// Check if this MethodInvocation provides the required argument
		Method method = invocation.getMethod();
		Class[] params = method.getParameterTypes();

		Serializable id = null;
		for (int i = 0; i < params.length; i++) {
			if (Serializable.class.isAssignableFrom(params[i])) {
				id = (Serializable) invocation.getArguments()[i];
				break;
			}
		}

		if (id == null) {
			throw new AuthorizationServiceException("MethodInvocation: "
					+ invocation + " did not provide any ID argument.");
		}
		Class domainClass = defaultDomainClass;
		if (domainClass == null) {
			// try to find in method and anno
			if (StringUtils.isNotBlank(methodDomainObjectClass)) {
				Object object = invocation.getThis();
				Class clazz = object.getClass();

				try {
					Method classMethod = clazz.getMethod(
							methodDomainObjectClass, new Class[] {});
					Object result = classMethod.invoke(object, new Object[] {});
					domainClass = (Class) result;
				} catch (Exception e) {
					logger.error(e.getMessage());
					throw new AuthorizationServiceException(
							"MethodInvocation: "
									+ invocation
									+ " provide wrrong domainClassMethod argument : "
									+ methodDomainObjectClass);
				}
			} else if (method.isAnnotationPresent(AclDomainClass.class)) {
				domainClass = method.getAnnotation(AclDomainClass.class)
						.value();
			} else {
				throw new AuthorizationServiceException("MethodInvocation: "
						+ invocation
						+ " did not provide any domainClass argument.");
			}
		}

		return new AclDomainBuilder(domainClass, id);
	}

	/**
	 * 由doaminObject构造成AclObjectIdentity,
	 * AclObjectIdentity是跟数据库中的表对应的数据，作用是描述所有的受保护的domainClass
	 * 
	 * @param domainInstance
	 * @param defaultAclObjectIdentityClass
	 */
	public static AclObjectIdentity obtainIdentity(Object domainInstance,
			Class defaultAclObjectIdentityClass) {
		if (domainInstance instanceof AclObjectIdentityAware) {
			AclObjectIdentityAware aclObjectIdentityAware = (AclObjectIdentityAware) domainInstance;

			logger.debug("domainInstance: {} cast to AclObjectIdentityAware",
					domainInstance);

			return aclObjectIdentityAware.getAclObjectIdentity();
		}

		try {
			Constructor constructor = defaultAclObjectIdentityClass
					.getConstructor(new Class[] { Object.class });

			logger.debug(
					"domainInstance: {} attempting to pass to constructor: {}",
					domainInstance, constructor);

			return (AclObjectIdentity) constructor
					.newInstance(new Object[] { domainInstance });
		} catch (Exception ex) {

			logger.debug("Error attempting construction of {}: {}",
					defaultAclObjectIdentityClass, ex.getMessage());

			if (ex.getCause() != null) {
				logger.debug("Cause: {}", ex.getCause().getMessage());
			}

			return null;
		}
	}
}
