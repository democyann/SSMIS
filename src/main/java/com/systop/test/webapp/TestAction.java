package com.systop.test.webapp;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.hibernate.criterion.MatchMode;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.systop.core.util.PageUtil;
import com.systop.core.webapp.struts2.action.DefaultCrudAction;
import com.systop.test.model.Test;
import com.systop.test.service.TestManager;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@SuppressWarnings({"serial"})
public class TestAction extends DefaultCrudAction<Test, TestManager> {
	private String userName;
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String index(){
		ServletActionContext.getRequest().setAttribute("pagetitle", "我发布的任务");
		return "index";
	}
	
	public String edit(){
		return "edit";
	}
	public String save(){
		getManager().save(getModel());
		return "success";
	}
	
	public String remove(){
		getManager().remove(getModel());
		return "success";
	}
	
	public String json(){
//		StringBuffer hql= new StringBuffer("from Test where 1=1");
//		List<Object> args= new  ArrayList<Object>();
//		if(userName!=null && !"".equals(userName)){
//			hql.append(" and name like ?");
//			args.add(MatchMode.ANYWHERE.toMatchString(userName));
//		}
//		page = PageUtil.getPage(getPageNo(), 4);
//		page= getManager().pageQuery(page, hql.toString(),args.toArray());
//		
		page=getManager().getList("",getPageNo(),2);
		JSONObject o = JSONObject.fromObject(page);
		try {
			PrintWriter out =ServletActionContext.getResponse().getWriter();
			out.print(o.toString());
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "json";
	}
}
