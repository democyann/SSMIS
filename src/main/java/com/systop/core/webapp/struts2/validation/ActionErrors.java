package com.systop.core.webapp.struts2.validation;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.StrutsConstants;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;
import org.springframework.validation.AbstractErrors;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import com.opensymphony.xwork2.ActionSupport;
import com.systop.core.util.ResourceBundleUtil;

/**
 * {@link org.springframework.validation.Errors}接口的实现类。可以将 给定的错误信息注入到Struts
 * Action类中。
 * 
 * @author Sam Lee
 * 
 */

@SuppressWarnings( { "unchecked", "serial", "rawtypes"})
public class ActionErrors extends AbstractErrors {
	private static Logger logger = LoggerFactory.getLogger(ActionErrors.class);

	public static final String DEFAULT_RESOURCE = ResourceBundleUtil.DEFAULT_BUNDLE;

	private String objectName;
	private ActionSupport action;
	private String strutsXml = "classpath:struts.xml";
	private String strutsProperties = "struts";

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	public void setAction(ActionSupport action) {
		logger.debug("The action is '{}'" + action.getClass());
		this.action = action;
	}

	@Override
	public void addAllErrors(Errors errors) {
		throw new UnsupportedOperationException(
				"The method 'addAllErrors' not implemented yet.");
	}

	@Override
	public List getFieldErrors() {
		Map<String, List<String>> errors = action.getFieldErrors();
		if (errors == null) {
			return Collections.EMPTY_LIST;
		}
		List<FieldError> fieldErrors = new ArrayList(errors.size());
		for (Iterator<String> itr = errors.keySet().iterator(); itr.hasNext();) {
			String field = itr.next();
			fieldErrors.add(new FieldError(objectName, field, null, false,
					errors.get(field).toArray(new String[] {}), null, null));
		}
		return fieldErrors;
	}

	@Override
	public Object getFieldValue(String field) {
		throw new UnsupportedOperationException(
				"The method 'getFieldValue' not implemented yet.");
	}

	@Override
	public List getGlobalErrors() {
		Collection<String> errors = action.getActionErrors();
		if (CollectionUtils.isEmpty(errors)) {
			return Collections.EMPTY_LIST;
		}
		List<ObjectError> oErrors = new ArrayList(errors.size());
		for (Iterator<String> itr = errors.iterator(); itr.hasNext();) {
			oErrors.add(new ObjectError(objectName, itr.next()));
		}
		return oErrors;
	}

	@Override
	public String getObjectName() {
		return objectName;
	}

	@Override
	public void reject(String errorCode, Object[] errorArgs,
			String defaultMessage) {
		if (StringUtils.isBlank(errorCode)) {
			action.addActionError(defaultMessage);
		}
		String msg = getErrorMessage(getResourceBundles(), errorCode,
				defaultMessage);
		msg = MessageFormat.format(msg, errorArgs);
		logger.debug(msg);
		action.addActionError(msg);
	}

	@Override
	public void rejectValue(String field, String errorCode, Object[] errorArgs,
			String defaultMessage) {
		if (StringUtils.isBlank(errorCode)) {
			action.addActionError(defaultMessage);
		}
		String msg = getErrorMessage(getResourceBundles(), errorCode,
				defaultMessage);
		msg = MessageFormat.format(msg, errorArgs);
		logger.debug(msg);
		action.addFieldError(field, msg);
	}

	/**
	 * 不走资源文件，直接输出Action级别信息
	 * 
	 * @param msg
	 *            格式化文本
	 * @param args
	 *            参数
	 * @see {@link MessageFormat#format(String, Object...)}
	 */
	public void rejectDirectly(String msg, Object... args) {
		action.addActionError(MessageFormat.format(msg, args));
	}

	/**
	 * 不走资源文件，直接输出字段级别信息
	 * 
	 * @param field
	 *            字段名
	 * @param msg
	 *            格式化文本
	 * @param args
	 *            参数
	 * @see {@link MessageFormat#format(String, Object...)}
	 */
	public void rejectFieldErrorDirectly(String field, String msg,
			Object... args) {
		action.addFieldError(field, MessageFormat.format(msg, args));
	}

	/**
	 * 从struts.xml或者struts.properties中获得struts.custom.i18n.resources的值，并将它们转换为
	 * ResourceBundle对象数组。
	 */
	private ResourceBundle[] getResourceBundles() {
		SAXReader reader = new SAXReader();
		String i18nResources = null;
		try {
			// 从struts.xml中找到资源文件
			File struts = ResourceUtils.getFile(strutsXml);
			logger.debug("find struts xml '{}'", struts.getAbsolutePath());

			Document doc = reader.read(struts);
			Element i18nEle = (Element) doc
					.selectSingleNode("//struts/constant[@name=\""
							+ StrutsConstants.STRUTS_CUSTOM_I18N_RESOURCES
							+ "\"]");

			i18nResources = i18nEle.attributeValue("value");
			// 从struts.properties中找到资源文件
			if (StringUtils.isBlank(i18nResources)) {
				logger.debug("从struts.properties中寻找资源文件");
				i18nResources = ResourceBundleUtil.getString(ResourceBundle
						.getBundle(strutsProperties),
						StrutsConstants.STRUTS_CUSTOM_I18N_RESOURCES,
						DEFAULT_RESOURCE);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		logger.debug("找到i18n资源文件{}", i18nResources);
		String[] resources = i18nResources.split(",");
		ResourceBundle[] rbs = new ResourceBundle[resources.length];
		for (int i = 0; i < resources.length; i++) {
			rbs[i] = ResourceBundle.getBundle(resources[i]);
		}
		return rbs;
	}

	public void setStrutsXml(String strutsXml) {
		this.strutsXml = strutsXml;
	}

	public void setStrutsProperties(String strutsProperties) {
		this.strutsProperties = strutsProperties;
	}

	/**
	 * 从资源文件中找到相应的信息.
	 * 
	 * @param rbs
	 *            资源文件
	 * @param key
	 *            the key for the desired string
	 * @param defaultMessage
	 *            缺省信息
	 * @return
	 */
	private String getErrorMessage(ResourceBundle[] rbs, String key,
			String defaultMessage) {
		System.out.println("[" + key + "]");
		if (rbs == null || rbs.length == 0 || StringUtils.isBlank(key)) {
			return defaultMessage;
		}
		String value = null;
		for (ResourceBundle rb : rbs) {
			try {
				value = rb.getString(key);
				break;
			} catch (MissingResourceException e) {
			}
		}
		return (StringUtils.isEmpty(value)) ? defaultMessage : value;
	}

}
