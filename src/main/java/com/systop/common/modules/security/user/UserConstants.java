package com.systop.common.modules.security.user;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.systop.common.modules.security.acegi.resourcedetails.ResourceDetails;
import com.systop.common.modules.security.user.model.Role;

/**
 * 用户管理及相关模块的常量类
 * 
 * @author Sam Lee
 * 
 */
public final class UserConstants {
	/**
	 * 在Web目录下存放照片的路径
	 */
	public static final String USER_PHOTO_PATH = "/uploaded/photo/";
	/**
	 * 权限操作－URL
	 */
	public static final String PERMISSION_OPERATION_URL = "target_url";
	/**
	 * 权限操作－函数
	 */
	public static final String PERMISSION_OPERATION_FUNC = "target_function";

	/**
	 * 用户类型－系统用户
	 */
	public static final String USER_TYPE_SYS = "1";

	/**
	 * 用户状态－可用
	 */
	public static final String USER_STATUS_USABLE = "1";

	/**
	 * 性别常量，M-男性
	 */
	public static final String GENT = "M";

	/**
	 * 性别常量，F-女性
	 */
	public static final String LADY = "F";
	
	/**
	 * 在职常量，1-在职
	 */
	public static final String ONJOB = "1";

	/**
	 * 在职常量，0-离岗退休
	 */
	public static final String OFFJOB = "0";
	
	/**
	 * 在职常量，2-离退休
	 */
	public static final String ONOFFJOB = "2";

	/**
	 * 学历，1-博士
	 */
	public static final String USER_DEGREE_DOCTOR = "1";
	/**
	 * 学历，2-硕士
	 */
	public static final String USER_DEGREE_MASTER = "2";
	/**
	 * 学历，3-学士
	 */
	public static final String USER_DEGREE_BACHELOR = "3";
	/**
	 * 学历，4-其他
	 */
	public static final String USER_DEGREE_OTHERS = "4";

	/**
	 * 系统管理员角色
	 */
	public static final String ROLE_ADMIN = "ROLE_ADMIN";


	/**
	 * 系统角色列表
	 */
	public static final List<Role> SYS_ROLES = new ArrayList<Role>();

	private static Role sysRole(String roleName, String descn) {
		Role role = new Role();
		role.setName(roleName);
		role.setDescn(descn);
		role.setIsSys(USER_TYPE_SYS); // 标记为系统角色
		return role;
	}

	static {
		SYS_ROLES.add(sysRole(ROLE_ADMIN, "系统管理员"));
	}

	/**
	 * 权限操作列表
	 */
	public static final Map<String, String> PERMISSION_OPERATIONS = Collections
			.synchronizedMap(new HashMap<String, String>());
	static {
		PERMISSION_OPERATIONS.put(PERMISSION_OPERATION_FUNC, "函数权限");
		PERMISSION_OPERATIONS.put(PERMISSION_OPERATION_URL, "URL权限");
	}

	/**
	 * 性别常量Map
	 */
	public static final Map<String, String> SEX_MAP = Collections
			.synchronizedMap(new LinkedHashMap<String, String>());
	static {
		SEX_MAP.put(GENT, "男");
		SEX_MAP.put(LADY, "女");
	}
	
	/**
	 * 在职常量Map
	 */
	public static final Map<String, String> JOB_MAP = Collections
			.synchronizedMap(new LinkedHashMap<String, String>());
	static {
		JOB_MAP.put(ONJOB, "在岗");
		JOB_MAP.put(OFFJOB, "离岗退休");
		JOB_MAP.put(ONOFFJOB, "离退休");
	}

	/**
	 * 学历常量Map
	 */
	public static final Map<String, String> DEGREE_MAP = Collections
			.synchronizedMap(new LinkedHashMap<String, String>());
	static {
		DEGREE_MAP.put(USER_DEGREE_DOCTOR, "博士");
		DEGREE_MAP.put(USER_DEGREE_MASTER, "硕士");
		DEGREE_MAP.put(USER_DEGREE_BACHELOR, "学士");
		DEGREE_MAP.put(USER_DEGREE_OTHERS, "其他");
	}

	/**
	 * URL资源
	 */
	public static final String RESOURCE_TYPE_URL = ResourceDetails.RES_TYPE_URL;

	/**
	 * 函数资源
	 */
	public static final String RESOURCE_TYPE_FUNCTION = ResourceDetails.RES_TYPE_FUNCTION;

	/**
	 * 权限操作列表
	 */
	public static final Map<String, String> RESOURCE_TYPES = Collections
			.synchronizedMap(new HashMap<String, String>());
	static {
		RESOURCE_TYPES.put(RESOURCE_TYPE_URL, "URL资源");
		RESOURCE_TYPES.put(RESOURCE_TYPE_FUNCTION, "函数资源");
	}
	/**
	 * 缺省密码，在修改的时候，如果输入缺省密码，则表示使用原来的密码。
	 */
	public static final String DEFAULT_PASSWORD = "*********";
	/** 学生用户 */
	public final static String STUDENT= "student";
	 /** 中心端用户 */
		public final static String TEACHER2= "teacher2";
	public static final String TEACHER = "teacher";
	

	/**
	 * 防止实例化
	 */
	private UserConstants() {
	}
}
