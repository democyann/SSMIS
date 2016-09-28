package com.systop.test.service;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.MatchMode;
import org.springframework.stereotype.Service;

import com.systop.core.dao.support.Page;
import com.systop.core.service.BaseGenericsManager;
import com.systop.core.util.PageUtil;
import com.systop.test.model.Test;

@Service
public class TestManager extends BaseGenericsManager<Test>{
	public Page getList(String userName,int pageNo,int pageSize){
		StringBuffer hql= new StringBuffer("from Test where 1=1");
		List<Object> args= new  ArrayList<Object>();
		if(userName!=null && !"".equals(userName)){
			hql.append(" and name like ?");
			args.add(MatchMode.ANYWHERE.toMatchString(userName));
		}
		Page page= PageUtil.getPage(pageNo, pageSize);
		page=pageQuery(page, hql.toString(),args.toArray());
		
		return page;
	}
}
