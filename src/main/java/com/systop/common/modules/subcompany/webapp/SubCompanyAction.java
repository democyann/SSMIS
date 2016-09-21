package com.systop.common.modules.subcompany.webapp;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.systop.common.modules.dept.model.Dept;
import com.systop.common.modules.dept.service.DeptManager;
import com.systop.common.modules.security.user.model.User;
import com.systop.common.modules.security.user.service.UserManager;
import com.systop.common.modules.subcompany.model.SubCompany;
import com.systop.common.modules.subcompany.service.SubCompanyManager;
import com.systop.core.util.PageUtil;
import com.systop.core.webapp.struts2.action.DefaultCrudAction;

/**
 * 公司信息
 * @author WangYaping
 * 	
 */

@SuppressWarnings("serial")
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class SubCompanyAction extends DefaultCrudAction<SubCompany, SubCompanyManager>{


	//所有分公司列表
	private List<SubCompany> subCompanyList;
	//公司ID
	private String subID;
	//是否为总公司
	private String isSub;
	//公司名称
	private String subName;
	//是否为公司领导
	private String type;
	//人员Id
	private String userId;
	//公司领导
	private String gsld;
	//公文管理员
	private String gwgly;
	
	private Map<String,Object> subMap;
	
	@Autowired
	private DeptManager deptManager;
	@Autowired
	private UserManager userManger;
	/**
	 * 查询
	 * 
	 * @return
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	public String index() {

		StringBuffer hql = new StringBuffer("from SubCompany p where 1=1 ");
		List<Object> args = new ArrayList<Object>();
		// 获取输入的条件
		String name = getModel().getName();//名称

		//按会议文件名称模糊查询
		if (StringUtils.isNotBlank(name)) {
			hql.append("and p.name like ?  ");
			args.add(MatchMode.ANYWHERE.toMatchString(name));
		}
		hql.append(" order by p.id ");
  
		page = PageUtil.getPage(getPageNo(), getPageSize());
		page = getManager().pageQuery(page, hql.toString(), args.toArray());
		items = page.getData();
		return "index";// 跳转到第一个页面

	}
	/**
	 * 查询全部，重定向，不带值
	 * 
	 * @return
	 */
	public String toAll() {
		return "success";
	}
	/**
	 * 删除的方法
	 * 
	 * @return
	 */
	@Override
	public String remove() {
		String ids = getRequest().getParameter("ids");
		String[] id = ids.split(",");
		for (String id1 : id) {
			int id2 = Integer.parseInt(id1);
			
			List<Dept> jds = deptManager.queryByTypeId(id2);
			if (jds.size()>1) {
				addActionMessage(getManager().get(id2).getName()+",该公司已经使用，不能删除！");
			} else {
				 for(Dept jd:jds){
					deptManager.remove(jd);
				 }
				SubCompany sc = getManager().get(id2);
				getManager().remove(sc);// 删除的方法
				addActionMessage("删除成功！");
			}	
		}
		return "success";
	}

	/**
	 * 保存的方法
	 * 
	 * @return
	 */
	@Override
	public String save() {
		if (getModel().getId() == null) {
		
			super.save();
		    int id = getManager().getNogh();//拿到新增的这条id
		    SubCompany c=getManager().get(id);//拿到最后一条新增的公司信息
			
			Dept d = new Dept();
			d.setName(getModel().getName());
			d.setSubCompany(c);
			d.setSerialNo(id+"");
			deptManager.save(d,new Integer(id));
			
			addActionMessage("添加成功！");// 提示
		} else {
			super.save();
			List<Dept> list1=deptManager.getMaxDeptsBySubCompany(getModel().getId());
       		Dept de= list1.get(0);
       		de.setName(getModel().getName());
			deptManager.save(de,getModel().getId());
			addActionMessage("更新成功！");
		}
		
		

		return "success";
	}

	//在地图在标记分公司
	public String showMap(){
		return "map";
	}
	//在地图显示所有分公司
	public String showAllInMap(){
		StringBuffer hql = new StringBuffer("from SubCompany sc where 1=1 and (sc.canceled is null or sc.canceled = false) ");
		List<Object> args = new ArrayList<Object>();
		if (StringUtils.isNotBlank(getModel().getName())) {
			hql.append("and sc.name like ? ");
			args.add(MatchMode.ANYWHERE.toMatchString(getModel().getName()));
		}
		if (StringUtils.isNotBlank(getModel().getAddress())) {
			hql.append("and sc.address like ? ");
			args.add(MatchMode.ANYWHERE.toMatchString(getModel().getAddress()));
		}
		subCompanyList = getManager().query(hql.toString(), args.toArray());
		return "allMap";
	}
	public String peopleSet(){
		subMap = new HashMap<String,Object>();
		if(StringUtils.isNotBlank(subID)){
			SubCompany sub = getManager().get(Integer.parseInt(subID));
			subID = sub.getId().toString();
			if(sub.getGsld()!=null){
				subMap.put("gsld",sub.getGsld().getName());
				subMap.put("gsldId",sub.getGsld().getId());
			}else{
				subMap.put("gsld","");
				subMap.put("gsldId","");
			}
			if(sub.getGwgly()!=null){
				subMap.put("gwgly",sub.getGwgly().getName());
				subMap.put("gwglyId",sub.getGwgly().getId());
			}else{
				subMap.put("gwgly","");
				subMap.put("gwglyId","");
			}
			subName = sub.getName();
			isSub = sub.getDeptSort();
		}else{
			if(getLoginUser().getDept()!=null && getLoginUser().getDept().getSubCompany()!=null){
				subID = getLoginUser().getDept().getSubCompany().getId().toString();
				if(getLoginUser().getDept().getSubCompany().getGsld()!=null){
					Integer i = getLoginUser().getDept().getSubCompany().getId();
					SubCompany sub = getManager().get(i);
					subMap.put("gsld",sub.getGsld().getName());
					subMap.put("gsldId",sub.getGsld().getId());
				}else{
					subMap.put("gsld","");
					subMap.put("gsldId","");
				}
				if(getLoginUser().getDept().getSubCompany().getGwgly()!=null){
					Integer i = getLoginUser().getDept().getSubCompany().getId();
					SubCompany sub = getManager().get(i);
					subMap.put("gwgly",sub.getGwgly().getName());
					subMap.put("gwglyId",sub.getGwgly().getId());
				}else{
					subMap.put("gwgly","");
					subMap.put("gwglyId","");
				}
				subName = getLoginUser().getDept().getSubCompany().getName();
				isSub = getLoginUser().getDept().getSubCompany().getDeptSort();
			}else{
				SubCompany sub = getManager().get(1);
				subID = sub.getId().toString();
				if(sub.getGsld()!=null){
					subMap.put("gsld",sub.getGsld().getName());
					subMap.put("gsldId",sub.getGsld().getId());
				}else{
					subMap.put("gsld","");
					subMap.put("gsldId","");
				}
				if(sub.getGwgly()!=null){
					subMap.put("gwgly",sub.getGwgly().getName());
					subMap.put("gwglyId",sub.getGwgly().getId());
				}else{
					subMap.put("gwgly","");
					subMap.put("gwglyId","");
				}
				subName = sub.getName();
				isSub = sub.getDeptSort();
			}
		}
		return "peopleSet";
	}
	//查询所有子公司
	public String subCompanyList(){
		subCompanyList = this.getManager().queryAll();
		return "json";
	}
	//得到所有公司
	public Map<String,String> getSubCompanys(){
		Map<String,String> submap = new HashMap<String,String>();
		subCompanyList = this.getManager().queryAll();
		for(SubCompany sub : subCompanyList){
			submap.put(sub.getId().toString(),sub.getName());
		}
		return submap;
	}
	//设置公司人员
	public String updateSubUser(){
		if(StringUtils.isNotBlank(type)){
			if("0".equals(type)){
				SubCompany sub = getManager().get(Integer.parseInt(subID));
				User user = userManger.get(Integer.parseInt(userId));
				sub.setGsld(user);
				getManager().save(sub);
			}else{
				SubCompany sub = getManager().get(Integer.parseInt(subID));
				User user = userManger.get(Integer.parseInt(userId));
				sub.setGwgly(user);
				getManager().save(sub);
			}
		}
		return "json";
	}
	//得到公司的领导和公文管理员
	public String getSubUser(){
		if(StringUtils.isNotBlank(subID)){
			SubCompany sub = getManager().get(Integer.parseInt(subID));
			subMap = new HashMap<String,Object>();
			if(sub.getGsld()!=null){
				subMap.put("gsld",sub.getGsld().getName());
			}else{
				subMap.put("gsld","");
			}
			if(sub.getGwgly()!=null){
				subMap.put("gwgly",sub.getGwgly().getName());
			}else{
				subMap.put("gwgly","");
			}
		}
		return "getSubUser";
	}
	public List<SubCompany> getSubCompanyList() {
		return subCompanyList;
	}
	public String getSubID() {
		return subID;
	}
	public void setSubID(String subID) {
		this.subID = subID;
	}
	public String getIsSub() {
		return isSub;
	}
	public void setIsSub(String isSub) {
		this.isSub = isSub;
	}
	public String getSubName() {
		return subName;
	}
	public void setSubName(String subName) {
		this.subName = subName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getGsld() {
		return gsld;
	}
	public void setGsld(String gsld) {
		this.gsld = gsld;
	}
	public String getGwgly() {
		return gwgly;
	}
	public void setGwgly(String gwgly) {
		this.gwgly = gwgly;
	}
	public Map<String, Object> getSubMap() {
		return subMap;
	}
	public void setSubMap(Map<String, Object> subMap) {
		this.subMap = subMap;
	}
	
	
}
