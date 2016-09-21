package com.systop.core.filter;

import java.util.ArrayList;
import java.util.List;

/**
 * 不需要struts2过滤的url
 *
 * @author Liang
 * @date 2013-12-4
 */
public class ExtendFilterConstants {
	public static List<String> getExtendUrls(){
		List<String> urls = new ArrayList<String>();
		
		urls.add("/hbcrj/loginseal.do");
		urls.add("/hbcrj/sealimage.do");
		urls.add("/hbcrj/adminseal.do");
		urls.add("/hbcrj/poserver.do");
		return urls;
	}
}
