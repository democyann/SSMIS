package com.systop.core.model;

import java.io.Serializable;

import javax.persistence.Transient;

/**
 * 域模型的基类。提供了标识对象是否改变/选择的属性。
 * @author Sam Lee
 * @version 2.5
 */
@SuppressWarnings("serial")
public class BaseModel implements Serializable {
  /**
   * 用于标识对象的状态是否改变.
   */
  private transient Boolean changed = Boolean.FALSE;
  
  @Transient
  public Boolean getChanged() {
    return changed;
  }

  public void setChanged(Boolean changed) {
    this.changed = changed;
  } 
}
