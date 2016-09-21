package com.systop.common.modules.security.acegi.cache;

import java.util.List;

import net.sf.ehcache.Ehcache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.systop.common.modules.cache.CacheHelper;
import com.systop.common.modules.security.acegi.resourcedetails.ResourceDetails;
import com.systop.core.dao.hibernate.BaseHibernateDao;

/**
 * {@link #getResStringsFormCache(String)}方法没有使用缓存，而是将
 * 数据库中的数据直接取出，性能取决于数据和SQL优化。如果使用缓存，则 很难保证数据的同步性，藏数据的处理也是一个问题。
 * 
 * @author sam
 * 
 */
@Component
@SuppressWarnings("rawtypes")
public class ResourceCacheDbImpl implements ResourceCache {
	/**
	 * Log for the class
	 */
	private static Logger logger = LoggerFactory
			.getLogger(ResourceCacheImpl.class);

	@Autowired(required = true)
	@Qualifier("baseHibernateDao")
	private BaseHibernateDao dao;
	/**
	 * Cache 接口
	 */
	private Ehcache ehcache;

	/**
	 * @return the cache
	 */
	public Ehcache getEhache() {
		return ehcache;
	}

	/**
	 * @param cache
	 *            the cache to set
	 */
	@Autowired(required = true)
	public void setEhcache(Ehcache cache) {
		this.ehcache = cache;
	}

	/**
	 * 这个方法用于逐个匹配URL的时候，从数据库取出所有ResString数据。
	 * 这个做法性能较低，但是缺省的实现ResourceCacheImpl经常出问题... 另外，SQL中直接对资源字符串进行降序排列，减少了
	 * {@link DatabaseFilterInvocationDefinitionSource} 排序造成的性能下降。
	 */
	@Override
	public List getResStringsFormCache(String resType) {
		String hql = "select r.resString from Resource r where r.resType=?"
				+ " order by resString desc";
		return dao.query(hql, resType);
	}

	@Override
	public ResourceDetails getResourceFormCache(String resString) {
		logger.debug("get Resource from cache {}", resString);
		return (ResourceDetails) CacheHelper.get(ehcache, resString);
	}

	@Override
	public void putResourceInCache(ResourceDetails resourceDetails) {
		if (resourceDetails != null) {
			logger.info("put Resource in cache {}",
					resourceDetails.getResString());
		}

		CacheHelper.put(ehcache, resourceDetails.getResString(),
				resourceDetails);

	}

	@Override
	public void removeResourceFromCache(String resString) {
		logger.info("remove Resource from cache {}", resString);
		// ResourceDetails rd = (ResourceDetails) CacheHelper.get(ehcache,
		// resString);
		ehcache.remove(resString);
	}

}
