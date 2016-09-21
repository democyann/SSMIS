package com.systop.core.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.dispatcher.ng.filter.StrutsPrepareAndExecuteFilter;

public class ExtendStrutsFilter extends StrutsPrepareAndExecuteFilter {

  @Override
  public void doFilter(ServletRequest req, ServletResponse res,FilterChain chain) throws IOException, ServletException {
      HttpServletRequest request = (HttpServletRequest) req;
      String path = request.getRequestURL().toString();
      boolean flag = true;
      for(String url : ExtendFilterConstants.getExtendUrls()){
      	if(path.indexOf(url)!=-1){
      		flag = false;
      		chain.doFilter(req, res);
      		break;
      	}
      }
      if(flag){
      	super.doFilter(req, res, chain);
      }	
  }
}
