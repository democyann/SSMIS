package com.systop.core;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;

import com.systop.core.util.ResourceBundleUtil;

/**
 * 系统常量类.
 * 
 * @author Sam
 * 
 */
public final class Constants {

	// Prevent from initializing.
	private Constants() {
	}

	// YES:1
	public static final String YES = "1";

	// NO:0
	public static final String NO = "0";
	public static final String INIT = "2";

	// 资源文件.
	public static final String BUNDLE_KEY = "application";

	// 资源绑定对象
	public static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
			.getBundle(BUNDLE_KEY);

	/**
	 * 缺省的分页容量
	 */
	public static final int DEFAULT_PAGE_SIZE = 20;

	/**
	 * 第一页的页码,缺省是1
	 */
	public static final int DEFAULT_FIRST_PAGE_NO = 1;

	/**
	 * 状态可用
	 */
	public static final String STATUS_AVAILABLE = "1";

	/** 状态不可用 */
	public static final String STATUS_UNAVAILABLE = "0";

	/** HQL查询语句 */
	public static final String HQL_KEY = "hql";

	/** HQL查询参数 */
	public static final String ARGS_KEY = "args";

	public static final String RE_P_NO_KEY = "re_p_no";

	public static final String RE_P_SIZE_KEY = "re_p_size";

	/**
	 * 用户信息在Session中的名字
	 */
	public static final String USER_IN_SESSION = "userInSession";

	public static final String KINDEDITOR_FILE_ROOT = "/uploads/kindeditor/";
	
	public static final String  FILE_UPLOAD_ROOT = ResourceBundleUtil.getString(RESOURCE_BUNDLE, "file.upload.root", "/uploads");

	public static final String FILE_ATTACH_ROOT = FILE_UPLOAD_ROOT + "/fileattchs/";
	
	// Error code定义
	public static final String NOT_LOGIN = "not_login";
	
	/**
	 * 考勤缺省值（默认出勤天数）
	 */
	public static final String DEFAULT_ATTENDANCE_DAY = ResourceBundleUtil.getString(
	      RESOURCE_BUNDLE, "default.attendance.day", "");

    public static final String SMS_URL = ResourceBundleUtil.getString(
            RESOURCE_BUNDLE, "sms.url", "");
    public static final String SMS_DATA = ResourceBundleUtil.getString(
            RESOURCE_BUNDLE, "sms.data", "");
    
    public static final String ROOT_NAME =  ResourceBundleUtil.getString(
	      RESOURCE_BUNDLE, "rootName", "/business");
    /**
	 * 支付方式（支票、现金、转账）
	 */
	public static final Map<String, String> PAYMETHOD_MAP = new LinkedHashMap<String, String>();

	static {
		PAYMETHOD_MAP.put("支票", "支票");
		PAYMETHOD_MAP.put("现金", "现金");
		PAYMETHOD_MAP.put("转账", "转账");
	}
	/**
	 * 字符串表示的倒休
	 */
	public static final String DX = "DX";
}
