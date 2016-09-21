package com.systop.common.modules.dept;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import com.systop.core.Constants;

/**
 * 部门常量类
 * 
 * @author Sam Lee
 * 
 */
public final class DeptConstants {
	/**
	 * 顶级部门名称
	 */
	public static final String TOP_DEPT_NAME = "石家庄瑞海检疫技术服务有限公司";

	/**
	 * 顶级部门ID
	 */
	public static final Integer TOP_DEPT_ID = 0;

	/**
	 * 第一个部门的编号
	 */
	public static final String FIRST_SERIAL_NO = "01";

	/**
	 * 部门类型（总公司）：0
	 */
	public static final String TYPE_COMPANY = Constants.NO;

	/**
	 * 部门类型（分公司）：1
	 */
	public static final String TYPE_SUB_COMPANY = Constants.YES;

	/**
	 * 部门类型描述：总公司
	 */
	public static final String TYPE_DESCN_COMPANY = "总公司";

	/**
	 * 部门类型描述：分公司
	 */
	public static final String TYPE_DESCN_SUB_COMPANY = "分公司";

	/**
	 * 单据类型常量Map
	 */
	public static final Map<String, String> DEPT_TYPE_MAPS = Collections
			.synchronizedMap(new LinkedHashMap<String, String>());
	static {
		DEPT_TYPE_MAPS.put(TYPE_COMPANY, TYPE_DESCN_COMPANY);
		DEPT_TYPE_MAPS.put(TYPE_SUB_COMPANY, TYPE_DESCN_SUB_COMPANY);
	}

	/**
	 * private constructor
	 */
	private DeptConstants() {

	}
	
	//添加公司或者分支机构标志量
	public static final String SERIALNO = "01"; //初始化部门SerialNo字段
	public static final String PARENTCOMPANY ="0";  //总公司
	public static final String  HOMEOFFICE = "1"; //公司本部
	public static final String EMBRANCHMENT = "2"; //分支机构
	public static final String DEPARTMENT ="3"; //部门
	public static final String BRANCH = "4";  //分公司
	public static final String OTHERDEPT = "5"; //和总公司同级的机构
	
}
