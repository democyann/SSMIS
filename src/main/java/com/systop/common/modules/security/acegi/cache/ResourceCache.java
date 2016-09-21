package com.systop.common.modules.security.acegi.cache;

import java.util.List;

import com.systop.common.modules.security.acegi.resourcedetails.ResourceDetails;

/**
 * Cache of <code>ResourceDetails</code>. 为<code>ResourceDetails</code>
 * 对象提供缓存支持的接口。
 * 
 * @author Sam
 * 
 */
@SuppressWarnings("rawtypes")
public interface ResourceCache {
	/**
	 * 根据资源String，从缓存中提取<code>ResourceDetails</code>对象
	 * 
	 * @param resString
	 *            给定资源String
	 * @return <code>ResourceDetails</code>对象，如果没有，返回 <code>null</code>.
	 */
	ResourceDetails getResourceFormCache(String resString);

	/**
	 * 将<code>ResourceDetails</code>对象放入缓存(以resString)为key
	 * 
	 * @param resourceDetails
	 *            给定<code>ResourceDetails</code>对象
	 */
	void putResourceInCache(ResourceDetails resourceDetails);

	/**
	 * 从缓存中删除<code>ResourceDetails</code>对象
	 * 
	 * @param resString
	 *            给定资源string.
	 */
	void removeResourceFromCache(String resString);

	/**
	 * 从缓存中根据资源类别提取所有资源.
	 * 
	 * @param resType
	 *            资源类别
	 * @return 资源string Set，如果没有，返回零长度Set.
	 * @see {@link ResourceDetails#RES_TYPE_FUNCTION}
	 * @see {@link ResourceDetails#RES_TYPE_URL}
	 */
	List getResStringsFormCache(String resType);
}
