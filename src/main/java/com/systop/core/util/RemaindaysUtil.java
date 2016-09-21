package com.systop.core.util;

import java.text.DecimalFormat;
import java.util.Date;

/**
 * 用于解析任务剩余天数的工具类。
 * 
 * @author shaozhiyuan
 * 
 */
public final class RemaindaysUtil {


	private RemaindaysUtil() {
	}

	/**
	 * 根据传的规定完成时间来计算天数的方法
	 * 
	 * @return返回计算出来的剩余天数
	 */
	public static double getRemainDays(Date endDate) {
		double lastTime;
		if (endDate != null) {
			lastTime = endDate.getTime() - new Date().getTime();
			lastTime = lastTime / 1000 / 60 / 60 / 24;
			DecimalFormat fnum = new DecimalFormat("##0.0");
			lastTime = Double.parseDouble(fnum.format(lastTime));
		} else {
			lastTime = 0;
		}
		return lastTime;
	}

	

}
