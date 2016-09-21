package com.systop.common.modules.security.acegi.cache;

import java.util.Collections;
import java.util.List;

import net.sf.ehcache.Ehcache;

import org.apache.commons.collections.list.TreeList;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.systop.common.modules.cache.CacheHelper;
import com.systop.common.modules.security.acegi.resourcedetails.ResourceDetails;

/**
 * 使用Ehcache实现<code>ResourceCache</code>
 * 
 * @author Sam
 * 
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class ResourceCacheImpl implements ResourceCache, InitializingBean {
	/**
	 * Log for the class
	 */
	private static Logger logger = LoggerFactory
			.getLogger(ResourceCacheImpl.class);

	/**
	 * 所有缓存中的URL资源string
	 */
	private List urlStrings;

	/**
	 * 所有缓存中的function资源string
	 */
	private List funcStrings;

	/**
	 * 缓存中其他的资源string
	 */
	private List otherStrings;

	/**
	 * Cache 接口
	 */
	private Ehcache cache;

	/**
	 * @return the cache
	 */
	public Ehcache getCache() {
		return cache;
	}

	/**
	 * @param cache
	 *            the cache to set
	 */
	@Autowired(required = true)
	public void setCache(Ehcache cache) {
		this.cache = cache;
	}

	/**
	 * @see ResourceCache#getResourceFormCache(java.lang.String)
	 */
	public ResourceDetails getResourceFormCache(String resString) {
		logger.debug("get Resource from cache {}", resString);
		return (ResourceDetails) CacheHelper.get(cache, resString);
	}

	/**
	 * @see ResourceCache#getResStringsFormCache(java.lang.String)
	 */
	public List getResStringsFormCache(String resType) {
		// logger.debug("缓存数据量：{}", String.valueOf(cache.getSize()));
		// logger.debug("从Res缓存取出数据-key:{}", resType);
		List resList = null;
		if (resType != null) {
			if (StringUtils.equals(resType, ResourceDetails.RES_TYPE_FUNCTION)) {
				resList = (List) CacheHelper.get(cache, KEY_OF_FUNCSTRINGS);
			} else if (StringUtils
					.equals(resType, ResourceDetails.RES_TYPE_URL)) {
				resList = (List) CacheHelper.get(cache, KEY_OF_URLSTRINGS);
			} else {
				resList = (List) CacheHelper.get(cache, KEY_OF_OTHERSTRINGS);
			}
		}
		if (resList == null) {
			logger.warn("没有从缓存中取得Resource数据{}",
					String.valueOf(urlStrings.size()));

			return Collections.EMPTY_LIST;
		} else {
			// logger.warn("从缓存中取得Resource数据:{}",
			// String.valueOf(resList.size()));
			return resList;
		}
	}

	/**
	 * @see ResourceCache#putResourceInCache(ResourceDetails)
	 */
	public void putResourceInCache(ResourceDetails resourceDetails) {
		if (resourceDetails != null) {
			logger.debug("put Resource in cache {}",
					resourceDetails.getResString());
		}

		CacheHelper.put(cache, resourceDetails.getResString(), resourceDetails);
		addResString(resourceDetails);
	}

	/**
	 * 向urlStrings或funcStrings或otherStrings加入一个元素
	 * 
	 */
	private void addResString(ResourceDetails rd) {
		logger.info("Add resource string {}", rd.getResString());
		if (rd != null) {
			if (StringUtils.equals(ResourceDetails.RES_TYPE_FUNCTION,
					rd.getResType())) {
				funcStrings.add(rd.getResString());
			} else if (StringUtils.equals(ResourceDetails.RES_TYPE_URL,
					rd.getResType())) {
				urlStrings.add(rd.getResString());
			} else {
				otherStrings.add(rd.getResString());
			}
		}

	}

	/**
	 * 从urlStrings或funcStrings或otherStrings删除一个元素
	 * 
	 */
	private void removeResString(ResourceDetails rd) {
		if (rd == null) {
			return;
		}
		if (StringUtils.equals(ResourceDetails.RES_TYPE_FUNCTION,
				rd.getResType())) {
			funcStrings.remove(rd.getResString());
		} else if (StringUtils.equals(ResourceDetails.RES_TYPE_URL,
				rd.getResType())) {
			urlStrings.remove(rd.getResString());
		} else {
			otherStrings.remove(rd.getResString());
		}
	}

	/**
	 * @see ResourceCache#removeResourceFromCache(java.lang.String)
	 */
	public void removeResourceFromCache(String resString) {
		logger.info("remove Resource from cache {}", resString);
		ResourceDetails rd = (ResourceDetails) CacheHelper
				.get(cache, resString);
		removeResString(rd);
		cache.remove(resString);
	}

	/**
	 * 存放所有urls string的缓存"key"
	 */
	private static final String KEY_OF_URLSTRINGS = "key_urls";

	/**
	 * 存放所有function string的缓存"key"
	 */
	private static final String KEY_OF_FUNCSTRINGS = "key_funcs";

	/**
	 * 存放其他资源 string的缓存"key"
	 */
	private static final String KEY_OF_OTHERSTRINGS = "key_others";

	/**
	 * @see InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		initCache();
	}

	/**
	 * 初始化缓存
	 * 
	 */
	private void initCache() {
		if (cache != null) {
			urlStrings = (List) CacheHelper.get(cache, KEY_OF_URLSTRINGS);
			if (urlStrings == null) {
				urlStrings = Collections.synchronizedList(new TreeList());
				CacheHelper.put(cache, KEY_OF_URLSTRINGS, urlStrings);
				logger.debug("init URL Strings");
			}

			funcStrings = (List) CacheHelper.get(cache, KEY_OF_FUNCSTRINGS);
			if (funcStrings == null) {
				funcStrings = Collections.synchronizedList(new TreeList());
				CacheHelper.put(cache, KEY_OF_FUNCSTRINGS, funcStrings);
				logger.debug("init Function Strings");
			}

			otherStrings = (List) CacheHelper.get(cache, KEY_OF_OTHERSTRINGS);
			if (otherStrings == null) {
				otherStrings = Collections.synchronizedList(new TreeList());
				CacheHelper.put(cache, KEY_OF_OTHERSTRINGS, otherStrings);
				logger.debug("init others Strings");
			}
		}
	}

}
