package com.systop.common.modules.template;


/**
 * 带有摆放位置信息的Template
 * @author Sam
 * 
 */
public class LayoutTemplate extends Template {
  /**
   * x方向的位置
   */
  private String x;

  /**
   * y方向的位置
   */
  private String y;

  /**
   * 宽度,例如：300px 或 100%
   */
  private String width;

  /**
   * 是否可见，通常是"true" or "false"
   */
  private String visible;
  /**
   * full constructor
   */
  public LayoutTemplate(String dir, String theme, String name, 
      String x, String y, String width, String visible) {
    super(dir, theme, name);
    this.x = x;
    this.y = y;
    this.width = width;
    this.visible = visible;
  }

  /**
   * @return the visible
   */
  public String getVisible() {
    return visible;
  }

  /**
   * @return the width
   */
  public String getWidth() {
    return width;
  }

  /**
   * @return the x
   */
  public String getX() {
    return x;
  }

  /**
   * @return the y
   */
  public String getY() {
    return y;
  }

}
