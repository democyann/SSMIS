package com.systop.core.util;


import org.dom4j.Attribute;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;


/**
 * dom4工具类
 * @author henry
 */
public final class Dom4jUtil {
  /**
   * cannot be instantiated
   *
   */
  private Dom4jUtil() { 
  }
  
  /**
   * 构造<tt>SAXReader</tt>对象。
   * @param file - XML文档的位置和名称。
   * @return - <tt>SAXReader</tt>对象。
   * 
   */
  public static SAXReader createSAXReader(String file)
  throws DocumentException {
    SAXReader reader = new SAXReader();
    reader.read(file);
    
    return reader;
  }
  
  /**
   * 解析<tt>Element</tt>的一个属性，并返回其转换为<tt>int</tt>类型后的值。
   * 如果解析失败或类型转换失败，则返回缺省值。
   * @param e - 指定的元素
   * @param attribute - 属性名称。
   * @param defaultValue - 缺省值。
   * @return int - 节点的<tt>int</tt>值 
   */
  public static int getInt(Element e, String attribute, int defaultValue) {
    try {
      Attribute att = e.attribute(attribute);
      String attValue = att.getValue();
      int castValue;
      try {
        castValue = Integer.parseInt(attValue);  
      } catch (Exception ex) {
        castValue = defaultValue;
      }
      
      return castValue;
    } catch (NullPointerException npex) {
      return defaultValue;
    }
  }
  
  /**
   * 解析<tt>Element</tt>的Text子节点，并返回其转换为<tt>int</tt>类型后的值。
   * 如果解析失败或类型转换失败，则返回缺省值。例如:<br><pre>
   * <my-element>12</my-element>
   * 则返回12.
   * </pre>
   * @param e - 元素
   * @param defaultValue - 缺省值
   * @return int - 节点的<tt>int</tt>值
   */
  public static int getInt(Element e, int defaultValue) {
    try {
      String eleValue = e.getText();
      int castValue;
      try {
        castValue = Integer.parseInt(eleValue); 
      } catch (Exception ex) {
        castValue = defaultValue;
      }
      
      return castValue;
    } catch (NullPointerException npex) {
      return defaultValue;
    }
  }
  
  /**
   * 解析<tt>Element</tt>的一个属性，并返回其转换为<tt>String</tt>类型后的值。
   * @param e - 指定的元素
   * @param attribute - 属性名称。
   * @param defaultValue - 缺省值。
   * @return String - 节点的<tt>String</tt>值 
   */
  public static String getString(Element e, String attribute,
      String defaultValue) {
    try {
      Attribute att = e.attribute(attribute);
      String attValue = att.getValue();
      return (attValue == null) ? defaultValue : attValue;
    } catch (NullPointerException npex) {
      return defaultValue;
    }
  }
  /**
   * 解析<tt>Element</tt>的一个属性，并返回其转换为<tt>String</tt>类型后的值。
   * @param e - 指定的元素
   * @param defaultValue - 缺省值。
   * @return String - 节点的<tt>int</tt>值 
   */
  public static String getString(Element e, String defaultValue) {
    try {
      String eleValue = e.getText();
      
      return (eleValue == null) ? defaultValue : eleValue;
    } catch (NullPointerException npex) {
      return defaultValue;
    }
  }
  
  /**
   * 解析<tt>Element</tt>的一个属性，并返回其转换为<tt>boolean</tt>类型后的值。
   * 如果解析失败或类型转换失败，则返回缺省值。
   * @param e - 指定的元素
   * @param attribute - 属性名称。
   * @param defaultValue - 缺省值。
   * @return boolean - 节点的<tt>boolean</tt>值 
   */
  public static boolean getBoolean(Element e, String attribute,
      boolean defaultValue) {
    try {
      Attribute att = e.attribute(attribute);
      String attValue = att.getValue();
      boolean castValue = true;
      if (attValue != null) {
        if (attValue.toLowerCase().equals("true")) {
          castValue = true;
        } else if (attValue.toLowerCase().equals("false")) { 
          castValue = false;
        } else {
          castValue = defaultValue;
        }
      }
      
      return castValue;
    } catch (NullPointerException npex) {
      return defaultValue;
    }
  }
  /**
   * 解析<tt>Element</tt>的一个属性，并返回其转换为<tt>boolean</tt>类型后的值。
   * 如果解析失败或类型转换失败，则返回缺省值。
   * @param e - 指定的元素
   * @param defaultValue - 缺省值。
   * @return boolean - 节点的<tt>boolean</tt>值 
   */
  public static boolean getBoolean(Element e, boolean defaultValue) {
    try {
      String attValue = e.getText();
      boolean castValue = true;
      if (attValue != null) {
        if (attValue.toLowerCase().equals("true")) {
          castValue = true;
        } else  if (attValue.toLowerCase().equals("false")) {
          castValue = false;
        } else {
          castValue = defaultValue;
        }
      }
      
      return castValue;
    } catch (NullPointerException npex) {
      return defaultValue;
    }
  }
  
}
