package com.systop.common.modules.sector;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * 部门常量类
 * @author Sam Lee
 *
 */
public final class SectorConstants {
  /**
   * 顶级部门名称
   */
  public static final String TOP_DEPT_NAME = "合作院校"; 
    
  
  /**
   * 顶级部门ID
   */
  public static final Integer TOP_DEPT_ID = 0;
  
  /**
   * 第一个部门的编号
   */
  public static final String FIRST_SERIAL_NO = "01";
  
  /**
   * 部门类别
   */
  public static final String TYPE_DEPT = "0";

  public static final String TYPE_COLLEGE = "1";
  
  /**
   * private constructor
   */
  private SectorConstants() {
    
  }
  
  /**
	 * 学校类型Map
	 */
	public static final Map<String, String> SCHOOLTYPE_MAP = Collections
			.synchronizedMap(new LinkedHashMap<String, String>());
	static {
		SCHOOLTYPE_MAP.put("1", "专科院校");
		SCHOOLTYPE_MAP.put("2", "本科院校");
	}
}
