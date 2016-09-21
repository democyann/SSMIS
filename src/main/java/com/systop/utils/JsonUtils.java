package com.systop.utils;

import java.util.List;

import net.sf.json.JSONArray;

/**
 * json工具类
 *
 * @author Liang
 * @date 2013-11-12
 */
public class JsonUtils {
	/**
	 * 将符合格式的json字符串转化为字符串集合
	 * @param jsonStr
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<String> getVars(String jsonStr){
		return (List<String>) JSONArray.toCollection(JSONArray.fromObject(jsonStr), String.class);
	}
	
	/**
	 * 将字符串集合转化为json字符串
	 * @param list
	 * @return
	 */
	public static String ListToJsonStr(List<String> list){
		JSONArray jsonStr = JSONArray.fromObject(list);
		return jsonStr.toString();
	}
}
