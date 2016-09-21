package com.systop.common.modules.cache;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
/**
 * Cache帮助类，提供简化代码的方法。
 * @author SAM
 *
 */
public final class CacheHelper {
  /**
   * ehcache中装入一个元素
   */
  public static void put(Ehcache cache, Object key, Object value) {
    assert(cache != null);
    
    cache.put(new Element(key, value));
  }
  
  /**
   * ehcache中取出一个对象，如果对象不存在，返回<code>null</code>.
   */
  public static Object get(Ehcache cache, Object key) {
    assert(cache != null);
    Element e = cache.get(key);
    return (e == null) ? null : e.getObjectValue();
  }
  
  private CacheHelper() {
  }
}
