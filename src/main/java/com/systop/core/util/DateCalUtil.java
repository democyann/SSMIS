package com.systop.core.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
/**
 * Date和Calendar类型常用方法
 * @author wangyong
 *
 */
public class DateCalUtil {

	/**
	* 将字符串转换成具体的时间（Calendar）
	* @return Calendar 时间 
	* @param str为要格式化字符串
	*/		
	public static Calendar getCalendarByStr(String str) {
		if (str == null) return null;
		Date date = getDateByStr(str);
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.setTime(date);
		return cal;	
	}

	/**
	 * 将Calendar类型转化为字符串类型
	 * @param calendar 要转化的Calendar类型变量
	 * @param hasTime true ：yyyy-MM-dd HH:mm；false：yyyy-MM-dd
	 * @return
	 */
	public static String getCalendarStr(Calendar calendar ,boolean hasTime ) {
		if (calendar == null) return "";
		return getDateStr(calendar.getTime() ,hasTime );
		
	}
	
	/**
	* 将具体的时间转换成字符串
	* @return 转换后的字符串格式为yyyy-MM-dd HH:mm:ss 或　yyyy-MM-dd
	* @param aDate为要格式化的时间
	* @param hasTime :返回的字符串是否包括时间 true:包括时间　false:只有日期
	*/	
	public static String getDateStr(Date aDate,boolean hasTime ) {
		if (aDate == null) return "";
		String dateStr = "";
		if(hasTime)
			dateStr = getDateStrByDefineStyle(aDate,"yyyy-MM-dd HH:mm");
		else
			dateStr = getDateStrByDefineStyle(aDate,"yyyy-MM-dd");
		return dateStr;	
	}
	
	/**
	* 返回由指定字符串转换成的时间，字符串格式类似"2006-2-12 14:22:30" 或"2006-2-12"
	* @return 转换后的时间
	* @param aStr为要转换成时间的字符串
	*/		
	public static Date getDateByStr(String aStr) {
		SimpleDateFormat df =null;
		if (aStr == null) return null;
		if(aStr.indexOf(":")>0 )//如果有时间　
			df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		else //如果没有时间
			df = new SimpleDateFormat("yyyy-MM-dd");
		try
		{
			Date result = df.parse(aStr);
			return result;
		}
		catch (Exception e)
		{
			return null;
		}
	}
	
	/**
	* 将具体的时间根据要求的格式，转换成字符串
	* @param aDate 为要格式化的时间
	* @param style 要转化的格式
	* @return 
	*/	
	public static String getDateStrByDefineStyle(Date aDate,String style){
		if (aDate == null) return "";
		SimpleDateFormat df= new SimpleDateFormat(style);
		String s = df.format(aDate);
		return s;	
	}
	
	/**
	* 将具体的时间转换成字符串
	* @return 转换后的字符串,格式为yyyy-MM-dd HH:mm:ss
	* @param aDate为要格式化的时间
	*/		
	public static String getDateStr(Date aDate) {
		if (aDate == null) return "";
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String s = df.format(aDate);
		return s;	
//		return getDateStr(aDate,true);
	}
}
