package com.systop.common.modules.security.acegi.intercept;

import java.util.Iterator;
import java.util.List;

import org.acegisecurity.ConfigAttributeDefinition;
import org.acegisecurity.ConfigAttributeEditor;
import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.intercept.ObjectDefinitionSource;
import org.acegisecurity.intercept.web.AbstractFilterInvocationDefinitionSource;
import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.PatternMatcher;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import com.systop.common.modules.security.acegi.cache.AcegiCacheManager;
import com.systop.common.modules.security.acegi.resourcedetails.ResourceDetails;

/**
 * URL资源调用截获器。URL资源和权限对应关系定义在数据库中，DatabaseFilterInvocationDefinitionSource
 * 类根据数据库中的信息判断当前登录用户是否能够访问URL资源。
 * 
 * @author Sam
 * 
 */
@SuppressWarnings("rawtypes")
public class DatabaseFilterInvocationDefinitionSource extends
		AbstractFilterInvocationDefinitionSource {
	/**
	 * Log for this class
	 */
	private static Logger logger = LoggerFactory
			.getLogger(DatabaseFilterInvocationDefinitionSource.class);
	/**
	 * 比较前是否先转化为小写
	 */
	private boolean convertUrlToLowercaseBeforeComparison = false;
	/**
	 * 是否使用ANT格式的路径定义
	 */
	private boolean useAntPath = false;

	/**
	 * ant格式路径匹配器
	 */
	private PathMatcher pathMatcher = new AntPathMatcher();

	/**
	 * Perl格式路径匹配
	 */
	private PatternMatcher matcher = new Perl5Matcher();
	/**
	 * <code>AcegiCacheManager</code>用于从缓存中获取数据
	 */
	private AcegiCacheManager acegiCacheManager;

	/**
	 * 查询当前URL所对应的角色，并且包装为<code>ConfigAttributeDefinition</code> 对象
	 * 
	 * @see {@link AbstractFilterInvocationDefinitionSource#lookupAttributes(String)}
	 */
	@Override
	public ConfigAttributeDefinition lookupAttributes(String url) {
		/*if (url == null) {
			return null;
		}
		acegiCacheManager.initResourceCache();

		if (isUseAntPath()) {
			// Strip anything after a question mark symbol, as per SEC-161.
			int firstQuestionMarkIndex = url.lastIndexOf("?");

			if (firstQuestionMarkIndex != -1) {
				url = url.substring(0, firstQuestionMarkIndex);
			}
		}

		List<String> urls = acegiCacheManager.getUrlResStrings();
		logger.debug("Total resources {}", urls.size());
		// 注释说明：从数据库中取出数据的时候进行排序，不必在这里排序了2009-6-29 by SAM
		// Collections.sort(urls);
		// Collections.reverse(urls); //倒叙排序

		if (convertUrlToLowercaseBeforeComparison) {
			url = url.toLowerCase();
		}

		GrantedAuthority[] authorities = new GrantedAuthority[0];
		for (String resString : urls) {
			boolean matched = false;
			// 转化为小写，然后比较
			// 匹配的时候，用这个tempStr，但是，获取授权的时候，仍然用resString
			String tempStr = resString;
			if (convertUrlToLowercaseBeforeComparison) {
				tempStr = tempStr.toLowerCase();
			}
			// 检查给定的URL与哪个定义的resString匹配
			if (isUseAntPath()) {
				matched = pathMatcher.match(tempStr, url);
			} else {
				Pattern compiledPattern;
				Perl5Compiler compiler = new Perl5Compiler();
				try {
					compiledPattern = compiler.compile(tempStr,
							Perl5Compiler.READ_ONLY_MASK);
				} catch (MalformedPatternException mpe) {
					throw new IllegalArgumentException(
							"Malformed regular expression: " + tempStr);
				}

				matched = matcher.matches(url, compiledPattern);
			}
			if (matched) { // 如果匹配，则保存其对应的Authority
				logger.debug("{} match {}.", url, resString);
				ResourceDetails rd = acegiCacheManager
						.getResourceFromCache(resString);
				if (rd != null) {
					authorities = rd.getAuthorities();
					break;
				}
			}
		}
		// 转化为ConfigAttributeDefinition对象
		if (authorities.length > 0) {
			StringBuffer authoritiesStr = new StringBuffer("");
			for (int i = 0; i < authorities.length; i++) {
				authoritiesStr.append(authorities[i].getAuthority())
						.append(",");
			}
			String authStr = authoritiesStr.substring(0,
					authoritiesStr.length() - 1);
			ConfigAttributeEditor configAttrEditor = new ConfigAttributeEditor();
			configAttrEditor.setAsText(authStr);
			return (ConfigAttributeDefinition) configAttrEditor.getValue();
		} else {
			logger.debug("Can't find matched resource for {}", url);
		}
*/
		return null;
	}

	/**
	 * @see {@link ObjectDefinitionSource#getConfigAttributeDefinitions()}
	 */
	public Iterator getConfigAttributeDefinitions() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @return the acegiCacheManager
	 */
	public AcegiCacheManager getAcegiCacheManager() {
		return acegiCacheManager;
	}

	/**
	 * @param acegiCacheManager
	 *            the acegiCacheManager to set
	 */
	public void setAcegiCacheManager(AcegiCacheManager acegiCacheManager) {
		this.acegiCacheManager = acegiCacheManager;
	}

	/**
	 * @return the convertUrlToLowercaseBeforeComparison
	 */
	public boolean isConvertUrlToLowercaseBeforeComparison() {
		return convertUrlToLowercaseBeforeComparison;
	}

	/**
	 * @param convertUrlToLowercaseBeforeComparison
	 *            the convertUrlToLowercaseBeforeComparison to set
	 */
	public void setConvertUrlToLowercaseBeforeComparison(
			boolean convertUrlToLowercaseBeforeComparison) {
		this.convertUrlToLowercaseBeforeComparison = convertUrlToLowercaseBeforeComparison;
	}

	/**
	 * @return the matcher
	 */
	public PatternMatcher getMatcher() {
		return matcher;
	}

	/**
	 * @param matcher
	 *            the matcher to set
	 */
	public void setMatcher(PatternMatcher matcher) {
		this.matcher = matcher;
	}

	/**
	 * @return the pathMatcher
	 */
	public PathMatcher getPathMatcher() {
		return pathMatcher;
	}

	/**
	 * @param pathMatcher
	 *            the pathMatcher to set
	 */
	public void setPathMatcher(PathMatcher pathMatcher) {
		this.pathMatcher = pathMatcher;
	}

	/**
	 * @return the useAntPath
	 */
	public boolean isUseAntPath() {
		return useAntPath;
	}

	/**
	 * @param useAntPath
	 *            the useAntPath to set
	 */
	public void setUseAntPath(boolean useAntPath) {
		this.useAntPath = useAntPath;
	}

}
