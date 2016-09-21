package com.systop.core.service;

/**
 * 给所有可配置项一个统一的接口.
 * @author Sam
 *
 */
public interface Definable {
  /**
   * 如果该配置项已经配置，返回{@code true},否则返回{@code false}
   */
  boolean isDefined();
}
