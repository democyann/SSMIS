package com.systop.common.modules.security.user.service.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.systop.common.modules.security.acegi.cache.AcegiCacheManager;
import com.systop.common.modules.security.user.model.User;

/**
 * 修改或删除用户的时候，同步缓存的监听器
 * @author Sam Lee
 *
 */
@Component
public class AcegiCacheSyncListener implements UserChangeListener {
  /**
   * <code>AcegiCacheManager</code>用于同步缓存
   */
  private AcegiCacheManager acegiCacheManager;
  
  @Autowired
  public void setAcegiCacheManager(AcegiCacheManager acegiCacheManager) {
    this.acegiCacheManager = acegiCacheManager;
  }
  /**
   * @see UserChangeListener#onRemove(User)
   */
  public void onRemove(User user) {
    // 同步缓存
    if (acegiCacheManager != null) {
      acegiCacheManager.onUserChanged(null, user.getUsername());
    }
  }
  
  /**
   * @see UserChangeListener#onSave(User, User)
   */
  public void onSave(User newUser, User oldUser) {
    if (acegiCacheManager != null) {
      acegiCacheManager.onUserChanged(newUser, oldUser.getUsername());
    }
  }

}
