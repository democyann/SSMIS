package com.systop.common.modules.security.acegi.cache;

import net.sf.ehcache.Ehcache;

import org.acegisecurity.providers.dao.UserCache;
import org.acegisecurity.userdetails.UserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.systop.common.modules.cache.CacheHelper;

/**
 * Implements of <code>UserCache</code>
 * @author Sam
 * 
 */
@Component
public class UserCacheImpl implements UserCache {
  /**
   * Log for the class
   */
  private static Logger logger = LoggerFactory.getLogger(ResourceCacheImpl.class);
  /**
   * Cache instance
   */
  private Ehcache cache;

  /**
   * @see UserCache#getUserFromCache(java.lang.String)
   */
  public UserDetails getUserFromCache(String username) {
    logger.debug("get UserDetails from cache {}", username);
    return (UserDetails) CacheHelper.get(cache, username);
  }

  /**
   * @see UserCache#putUserInCache(UserDetails)
   */
  public void putUserInCache(UserDetails user) {
    if (user != null) {
      logger.debug("put user in cache {}", user.getUsername());
      CacheHelper.put(cache, user.getUsername(), user);
    }
  }

  /**
   * @see UserCache#removeUserFromCache(java.lang.String)
   */
  public void removeUserFromCache(String username) {
    logger.debug("remove user from cache {}", username);
    cache.remove(username);
  }

  /**
   * @return the cache
   */
  public Ehcache getCache() {
    return cache;
  }

  /**
   * @param cache the cache to set
   */
  @Autowired(required = true)
  public void setCache(Ehcache cache) {
    this.cache = cache;
  }

}
