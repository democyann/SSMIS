package com.systop.common.modules.dept.webapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;


import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.systop.common.modules.dept.DeptConstants;
import com.systop.common.modules.dept.model.Dept;
import com.systop.common.modules.dept.service.DeptManager;
import com.systop.common.modules.dept.service.DeptSerialNoManager;
import com.systop.common.modules.dict.model.Dicts;
import com.systop.common.modules.dict.service.DictsManager;
import com.systop.common.modules.security.user.model.User;
import com.systop.common.modules.security.user.service.UserManager;
import com.systop.common.modules.subcompany.model.SubCompany;
import com.systop.common.modules.subcompany.service.SubCompanyManager;
import com.systop.core.ApplicationException;
import com.systop.core.util.RequestUtil;
import com.systop.core.webapp.struts2.action.ExtJsCrudAction;

/**
 * 部门管理Action
 * 
 * @author Sam Lee, NiceLunch,WangYaping
 * 
 */
@SuppressWarnings("serial")
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class DeptAction extends ExtJsCrudAction<Dept, DeptManager> {

	// 部门序列号管理器
	@Autowired
	private DeptSerialNoManager serialNoManager;
	//部门类型
		private DictsManager dictsManager;
	


	// 当前上级部门ID
	private Integer parentId;
	// 用于查询的部门名称
	private String deptName = StringUtils.EMPTY;

	// 封装属性部门数据
	private List<Map<String, Object>> depts = null;

	// 异步请求，返回值
	private Map<String, Object> result;
	
	@Autowired
	private SubCompanyManager subCompanyManager;
	
	private String deptIds;
	
	private String ztreeStr;

	//公司Id
	private String subId;
	
	//部门列表
	private List<Dept> deptList;
	/**
	 * 部门查询。根据指定的上级部门id(通过{@link #parentId}属性)，查询下级部门。 如果{@link #parentId}
	 * 为null,则查询顶级部门（没有上级部门的）
	 */
	@Override
	public String index() {
		return INDEX;
	}

	@Autowired
	private UserManager userManager;

	public User getUser(User user) {
		return userManager.get(user.getId());
	}

	/**
	 * 查询所有公司部门
	 */
	public String allDeptTree() {
		if (RequestUtil.isJsonRequest(getRequest())) {
			List<Dept> tops = getManager().getDeptsByParentId(null, null,null);
			depts = new ArrayList<Map<String, Object>>();
			for (Dept dept : tops) {
				Map<String, Object> top = toMap(dept);
				top = buildAllTreeByParent(top, true);
				depts.add(top);
			}
			return JSON;
		}
		return INDEX;
	}
	
	
	
	/**
	 * 动态得到公司下的所有部门
	 */
	public String queryDeptBySubId(){
		if(StringUtils.isNotBlank(subId)){
			depts = new ArrayList<Map<String, Object>>();
			deptList = getManager().getAllDeptsBySubCompany(Integer.parseInt(subId));
			for(Dept dept : deptList){
				Map<String, Object> top = toMap(dept);
				top = buildAllTreeByParent(top, true);
				depts.add(top);
			}
		}
		return "queryDeptBySubId";
	}
	
	/**
	 * 修改部门审核人
	 */
	public String updateDeptShr(){
		Dept dept = getManager().get(Integer.parseInt(deptIds));
		User user =userManager.get(Integer.parseInt(subId));
		dept.setShr(user);
		getManager().save(dept);
		return "updateDeptShr";
	}
	
	/**
	 * 得到部门审核人
	 */
	public String getDeptShr(String id){
		String names = "";
		if(StringUtils.isNotBlank(id)){
			ApplicationContext ctx = WebApplicationContextUtils
					.getWebApplicationContext(ServletActionContext.getServletContext());
			UserManager um = (UserManager) ctx.getBean("userManager");
			User user = um.get(Integer.parseInt(id));
			names = "["+user.getName()+"]";
		}
		return names;
	}
	
	
	/**
	 * 返回部门树形列表，每一个部门用一个Map表示，子部门用Map的'childNodes'key挂接一个List
	 * 供查询填写修改使用，总公司，子公司都只能看到自己的。
	 * 
	 * @param parent
	 * @param nested
	 *            是否递归
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Map<String, Object> buildAllTreeByParent(Map<String, Object> parent, boolean nested) {
		if (parent != null) {
			List<Dept> subs = getManager().getDeptsByParentId((Integer) parent.get("id"), null,null);
			List childs = new ArrayList();
			for (Dept dept : subs) {
				Map<String, Object> child = toMap(dept);
				if (nested) { // 递归查询子部门
					child = buildAllTreeByParent(child, nested);
				}
				childs.add(child);
			}
			if (!childs.isEmpty()) {
				parent.put("children", childs);
				parent.put("leaf", false);
			} else {
				parent.put("leaf", true);
			}
		}
		return parent;
	}

	
	/**
	 * 供查询填写修改使用，总公司，子公司都只能看到自己的。
	 */
	public String deptTree() {
		if (RequestUtil.isJsonRequest(getRequest())) {
			Integer companyId = null;
			User user = null;
			 String isZg = "0";//是否总公司，默认是
			// 根据登陆用户得到所在公司id
			try {
				user = getUser(getLoginUser());
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (user.getDept() != null) {
				if(user.getDept().getParentDept()!=null){
					parentId = user.getDept().getParentDept().getId();
					if (user.getDept().getSubCompany() != null) {
						companyId = user.getDept().getSubCompany().getId();
						isZg =  user.getDept().getSubCompany().getDeptSort();//1为分公司
					}
					}
			}else{  //有于Admin没有部门筛选出Admin
				List<Dept> dList = this.getManager().selectOther();
				if(dList.size() >0){
					Dept dept = dList.get(0);
					companyId = dept.getSubCompany().getId();
				}
			}
			if(isZg.equals("0")){
				parentId = null;
			}
			List<Dept> tops;
			if(null != getModel() && null != getModel().getId() ){
				 tops = getManager().getDeptsByParentId(null, companyId,getManager().get(getModel().getId()));
			}else{
				 tops = getManager().getDeptsByParentId(null, companyId,null);
			}
			
			depts = new ArrayList<Map<String, Object>>();
			if(null != tops){
			for (Dept dept : tops) {
				Map<String, Object> top = toMap(dept);
				if(null != getModel() && null != getModel().getId() ){
				top = buildTreeByParent(top, true,getModel());
				}else{
				top = buildTreeByParent(top, true,null);	
				}
				depts.add(top);
			}
			}
			
			return JSON;
		}
		return INDEX;
	}

	/**
	 * 返回部门树形列表，每一个部门用一个Map表示，子部门用Map的'childNodes'key挂接一个List
	 * 供查询填写修改使用，总公司，子公司都只能看到自己的。
	 * 
	 * @param parent
	 * @param nested
	 *            是否递归
	 * @param dept1 
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map<String, Object> buildTreeByParent(Map<String, Object> parent, boolean nested, Dept dept1) {
		if (parent != null) {
			Integer companyId = null;
			// 根据登陆用户得到所在公司id
			User user = getUser(getLoginUser());
			if (user.getDept() != null) {
				if (user.getDept().getSubCompany() != null) {
					companyId = user.getDept().getSubCompany().getId();

				}

			}else{  //有于Admin没有部门筛选出Admin
				List<Dept> dList = this.getManager().selectOther();
				if(dList.size() >0){
					Dept dept = dList.get(0);
					companyId = dept.getSubCompany().getId();
				}
			}
			List<Dept> subs = getManager()
					.getDeptsByParentId((Integer) parent.get("id"), companyId,dept1);
			List childs = new ArrayList();
			for (Dept dept : subs) {
				Map<String, Object> child = toMap(dept);
				if (nested) { // 递归查询子部门
					child = buildTreeByParent(child, nested,dept1);
				}
				childs.add(child);
			}
			if (!childs.isEmpty()) {
				parent.put("children", childs);
				parent.put("leaf", false);
			} else {
				parent.put("leaf", true);
			}
		}
		return parent;
	}

	/**
	 * 供查询使用，总公司看到总公司的，子公司看到自己的。
	 */
	@SuppressWarnings("unused")
    public String deptTree1() {
		if (RequestUtil.isJsonRequest(getRequest())) {
			Integer companyId = null;
			User user = null;
			// 根据登陆用户得到所在公司id
			try {

				user = getUser(getLoginUser());
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (user.getDept() != null) {
				if (user.getDept().getSubCompany() != null) {
					companyId = user.getDept().getSubCompany().getId();
				}

			}
			String fen = "";
			if("4".equals(user.getDept().getJudgement())){
				fen = "1";
			}
			List<Dept> deptlist = getManager().selectOther();
			List<Dept> tops = getManager().getDeptsByParentId1(null, companyId,fen);
			depts = new ArrayList<Map<String, Object>>();
			Map<String, Object> top = toMap(tops.get(0));
			top = buildTreeByParent1(top, true);
			depts.add(top);
			return JSON;
		}
		return INDEX;
	}

	/**
	 * 返回部门树形列表，每一个部门用一个Map表示，子部门用Map的'childNodes'key挂接一个List
	 * 供查询使用，总公司看到所有的，子公司看到自己的。
	 * 
	 * @param parent
	 * @param nested
	 *            是否递归
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map<String, Object> buildTreeByParent1(Map<String, Object> parent, boolean nested) {
		if (parent != null) {
			Integer companyId = null;
			// 根据登陆用户得到所在公司id
			String isFenGongSi= "";
			User user = getUser(getLoginUser());
			if (user.getDept() != null) {
				if("4".equals(user.getDept().getJudgement())){
					isFenGongSi = user.getDept().getId().toString();
					companyId = null;
				}else{
					if (user.getDept().getSubCompany() != null) {
						companyId = user.getDept().getSubCompany().getId();
					}
				}
			}
			
			List<Dept> subs = getManager()
					.getDeptsByParentId1((Integer) parent.get("id"), companyId,isFenGongSi);
			List childs = new ArrayList();
			for (Dept dept : subs) {
				Map<String, Object> child = toMap(dept);
				if (nested) { // 递归查询子部门
					child = buildTreeByParent1(child, nested);
				}
				childs.add(child);
			}
			if (!childs.isEmpty()) {
				parent.put("children", childs);
				parent.put("leaf", false);
			} else {
				parent.put("leaf", true);
			}
		}
		return parent;
	}

	/**
	 * 供查询使用，总公司，子公司都只能看到全部的。
	 */
	public String deptTree2() {
		if (RequestUtil.isJsonRequest(getRequest())) {
			List<Dept> tops = getManager().getDeptsByParentId(null, null,null);
			depts = new ArrayList<Map<String, Object>>();
			for (Dept dept : tops) {
				Map<String, Object> top = toMap(dept);
				top = buildTreeByParent2(top, true);
				depts.add(top);
			}
			return JSON;
		}
		return INDEX;
	}
	/**
	 * 单选只显示总公司和秦检公司
	 * @author Geng ww
	 */
	public String deptTreeSeleectRadio(){
		if (RequestUtil.isJsonRequest(getRequest())) {
			List<Dept> tops = getManager().getDeptsSelectRadio(null, null,null);
			depts = new ArrayList<Map<String, Object>>();
			for (Dept dept : tops) {
				Map<String, Object> top = toMap(dept);
				top = buildTreeByParent2(top, true);
				depts.add(top);
			}
			return JSON;
		}
		return INDEX;
	}
	/**
	 * 显示所有的公司----单选
	 */
	public String deptTreeSeleectAllRadio(){
		if (RequestUtil.isJsonRequest(getRequest())) {
			List<Dept> tops = getManager().getDeptsSelectAllRadio(null, null,null);
			depts = new ArrayList<Map<String, Object>>();
			for (Dept dept : tops) {
				Map<String, Object> top = toMap(dept);
				top = buildTreeByParent2(top, true);
				depts.add(top);
			}
			return JSON;
		}
		return INDEX;
	}
	/**
	 * 分组时使用  总公司查看自己 分公司查看自己
	 */
	@SuppressWarnings("unused")
    public String deptTreeWork() {
		if (RequestUtil.isJsonRequest(getRequest())) {
			Integer companyId = null;
			User user = null;
			// 根据登陆用户得到所在公司id
			try {

				user = getUser(getLoginUser());
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (user.getDept() != null) {
				if (user.getDept().getSubCompany() != null) {
					companyId = user.getDept().getSubCompany().getId();
				}

			}
			String fen = "";
			if("4".equals(user.getDept().getJudgement())){
				fen = "1";
			}
			Dept dept = null;
			if("1".equals(user.getDept().getSubCompany().getDeptSort())){
				dept = user.getDept();
			}else{
				List<Dept> deptlist = getManager().selectOther();
				dept = deptlist.get(0);
			}
			List<Dept> tops = getManager().getDeptsByParentId1(null,dept.getSubCompany().getId(),fen);
			depts = new ArrayList<Map<String, Object>>();
			Map<String, Object> top = toMap(tops.get(0));
			top = buildTreeByParent1(top, true);
			depts.add(top);
			return JSON;
		}
		return INDEX;
	}
	/**
	 * 返回部门树形列表，每一个部门用一个Map表示，子部门用Map的'childNodes'key挂接一个List
	 * 供查询使用，总公司，子公司都只能看到全部的。
	 * 
	 * @param parent
	 * @param nested
	 *            是否递归
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map<String, Object> buildTreeByParent2(Map<String, Object> parent, boolean nested) {
		if (parent != null) {
			Integer companyId = null;
			List<Dept> subs = getManager()
					.getDeptsByParentId((Integer) parent.get("id"), companyId,null);
			List childs = new ArrayList();
			for (Dept dept : subs) {
				Map<String, Object> child = toMap(dept);
				if (nested) { // 递归查询子部门
					child = buildTreeByParent2(child, nested);
				}
				childs.add(child);
			}
			if (!childs.isEmpty()) {
				parent.put("children", childs);
				parent.put("leaf", false);
			} else {
				parent.put("leaf", true);
			}
		}
		return parent;
	}

	// 封装到Map中
	private Map<String, Object> toMap(Dept dept) {
		Map<String, Object> map = null;
		if (dept != null) {
			map = new HashMap<String, Object>();
			map.put("id", dept.getId());
			map.put("text", dept.getName());
			/*map.put("jument", dept.getJudgement());*/		
			map.put("code", dept.getCode());
			map.put("descn", dept.getDescn());
			// 类型（0：总公司 1：分公司）
			map.put("type", dept.getType());
			if(dept.getShr()!=null && dept.getShr().getId() !=null){
				map.put("shr",dept.getShr().getName());
				map.put("shrId",dept.getShr().getId());
			}else{
				map.put("shr","");
				map.put("shrId","");
			}
			map.put("typeDescn", dept.getTypeStr());
			if (dept.getParentDept() != null && dept.getParentDept().getId() != null) {
				map.put("companyId", dept.getParentDept().getId().toString());
			} else {
				map.put("companyId", "");

			}
		}
		return map;
	}

	/**
	 * 得到当前部门（通过{@link #parentId}指定）的上级部门.
	 */
	public Dept getParent() {
		if (parentId == null || parentId.equals(DeptConstants.TOP_DEPT_ID)) {
			Dept dept = new Dept();
			dept.setName(DeptConstants.TOP_DEPT_NAME);
			return dept;
		}

		return getManager().get(parentId);
	}

	/**
	 * 异步根据部门ID得到部门
	 */
	public String findDept() {
		Dept dept = getParent();
		result = new HashMap<String, Object>();
		if (dept != null) {
			result.put("id", dept.getId());
			result.put("type", dept.getType());
		}
		return "findDept";
	}

	/**
	 * 根据处室的Id查询此处室是检疫局还是监管部门
	 * @author Geng WeiWei
	 */
	public String findDeptJum(){
		Dept dept = getParent();
		result = new HashMap<String, Object>();
		//判断是监管部门还是检疫局
		if( dept != null && dept.getJgbm()!=null && dept.getJgbm().equals(DeptConstants.OTHERDEPT) && dept.getParentDept() !=null){ //公司为空代表为监管部门
			result.put("fg", true);
		}else{ //公司不为空代表为检疫局
			result.put("fg",false);
		}
		return "findDeptJum";
	}
	
	/**
	 * 根据父类的Id查询此处室是否为第二级机构
	 * @author Geng WeiWei
	 */
	public String selectDeptJum(){
		Dept dept = getParent();
		result = new HashMap<String, Object>();
		//判断是监管部门还是检疫局
		if(dept.getParentDept() == null && dept.getJudgement().equals(DeptConstants.OTHERDEPT)){ //公司为空代表为监管部门
			result.put("fg", true);
		}else if(dept.getParentDept() == null && dept.getJudgement().equals(DeptConstants.PARENTCOMPANY)){ //公司不为空代表为检疫局
			result.put("fg",false);
		}
		return "selectDeptJum";
		
	}
	/**
	 * 覆盖父类，处理父部门ID为{@link DeptConstants#TOP_DEPT_ID}的情况。
     *@author Geng WeiWei 修改	
	 */
	
	@Override
	public String save() {
		try {
			if (parentId != null) {
				getModel().setParentDept(getManager().get(parentId));
				Dept d = getManager().get(parentId);   //父类
				if(d != null && d.getJgbm() != null && !"".equals(d.getJgbm()) && d.getJgbm().equals(DeptConstants.OTHERDEPT) 
						     && !d.getJudgement().equals(DeptConstants.OTHERDEPT)){ 
					if (null != getModel().getJgDept() &&! ",".equals(getModel().getJgDept()) && !"".equals(getModel().getJgDept()) ) {
						getModel().setJgDept(getModel().getJgDept());
						String jgDept = getModel().getJgDept().substring(1,getModel().getJgDept().length()-1);
						Dept dept = getManager().get(new Integer(jgDept));
						if(null != dept && null != dept.getSubCompany()){
							getModel().setSubCompany(dept.getSubCompany()); //为监管部门，把自己所属的公司保存成要监管的公司	
						}else{
							addActionError("添加失败!");
							return INPUT;
						}
						getModel().setJgbm(d.getJgbm()); //表示为监管部门或者监管部门下的子部门
					}else{
						addActionError("请选择监管部门!");
						return INPUT;
					}
				}
				if(d.getJudgement().equals(DeptConstants.HOMEOFFICE)){  //公司本部 1
					if(null == getModel().getId()){ //添加
						if( d.getJgbm() != null && d.getJgbm().equals(DeptConstants.OTHERDEPT)){ //为监管部门 5
							getModel().setJudgement(DeptConstants.DEPARTMENT); //部门
						}else{
							getModel().setJudgement(DeptConstants.DEPARTMENT); //部门
							getModel().setSubCompany(d.getSubCompany());	
						}
					}else{  //修改
						if(getModel().getJudgement().equals(DeptConstants.BRANCH)){  //分公司 --》部门
							if(d.getJgbm() != null && !d.getJgbm().equals(DeptConstants.OTHERDEPT)){  //公司不为空代表为检疫局
								SubCompany sub = getModel().getSubCompany();
							    List<Dept> dtList = getManager().selectOther(getModel());
							    if(dtList.size() == 1){      //有一个引用（也就是修改记录本身）
										getModel().setSubCompany(null);
										getManager().update(getModel());//更新本表
										subCompanyManager.getDao().getHibernateTemplate().clear();
										subCompanyManager.remove(sub);
							}
							    getModel().setSubCompany(d.getSubCompany());
								getModel().setJudgement(DeptConstants.DEPARTMENT); //部门    
						}else{
							    getModel().setJudgement(DeptConstants.DEPARTMENT); //部门    
						}
						}else if(getModel().getJudgement().equals(DeptConstants.EMBRANCHMENT)){
							 getModel().setJudgement(DeptConstants.DEPARTMENT); //部门
						}
					}
				}else if(d.getJudgement().equals(DeptConstants.EMBRANCHMENT)){ //分支机构 2
					if( null == getModel().getId()){   //新增
						if(d.getJgbm() != null && d.getJgbm().equals(DeptConstants.OTHERDEPT)){ //父节点的公司为空代表为监管部门
							getModel().setJudgement(DeptConstants.DEPARTMENT); //部门
						}else{ //父节点不为空代表为检疫局
							getModel().setJudgement(DeptConstants.BRANCH); //分公司
							List<SubCompany> subList = subCompanyManager.selectSubname(getModel());
							if(subList.size() == 0){
								SubCompany subCompany = new SubCompany();
								subCompany.setName(getModel().getName());
								subCompany.setDeptSort("1"); //表示分公司
								subCompanyManager.save(subCompany);
								Integer id = getManager().saveSubid(subCompany);
								SubCompany sub = subCompanyManager.get(id);
								getModel().setSubCompany(sub);
							}
						}
					}else{ //修改
						if(getModel().getJudgement().equals(DeptConstants.DEPARTMENT)){  // 部门--》分公司 ,添加
							if(d.getJgbm() != null && !d.getJgbm().equals(DeptConstants.OTHERDEPT)){ //父节点的公司为空代表为监管部门
								SubCompany subCompany = new SubCompany();
								subCompany.setName(getModel().getName());
								subCompany.setDeptSort("1"); //表示分公司
								subCompanyManager.save(subCompany);
								Integer id = getManager().saveSubid(subCompany);
								SubCompany sub = subCompanyManager.get(id);
								getModel().setSubCompany(sub);
								getModel().setJudgement(DeptConstants.BRANCH); //分公司
							}else{ 
							    //getModel().setJudgement(DeptConstants.EMBRANCHMENT); //分公司	
							}
						}else if(getModel().getJudgement().equals(DeptConstants.BRANCH)){  //分公司--》分公司
							List<SubCompany> subList = subCompanyManager.selectSubname(getModel());  //首先判断修改后的分公司名称在公司表中是否存在
							if(subList.size() <= 0){
	                            if(null !=  getModel().getSubCompany()){
	                            	SubCompany subCompany =subCompanyManager.get( getModel().getSubCompany().getId());
	                            	subCompany.setName(getModel().getName());
	                            	subCompanyManager.getDao().getHibernateTemplate().clear();
	                            	subCompanyManager.update(subCompany);
	                            }
							}
					}else if(getModel().getJudgement().equals(DeptConstants.EMBRANCHMENT)){  //分支机构转部门
						getModel().setJudgement(DeptConstants.DEPARTMENT); //部门
					}else if(getModel().getJudgement().equals(DeptConstants.HOMEOFFICE)){  //公司本部转部门
						getModel().setJudgement(DeptConstants.DEPARTMENT); //部门
					}else if(getModel().getJudgement().equals(DeptConstants.OTHERDEPT)){ //监管转部门
						addActionError("您操作有误,请正确操作!");
						return INPUT;
					}
					}
				}else if(d.getJudgement().equals(DeptConstants.OTHERDEPT)){  //监管部门
					/*if(getModel().getJudgement().equals(DeptConstants.DEPARTMENT)){  // 部门--》监管分支机构 
					}
					if(getModel().getJudgement().equals(DeptConstants.DEPARTMENT)){ //部门---》监管部门
						getModel().setJudgement(DeptConstants.OTHERDEPT);
						getModel().setParentDept(null);
					}*/
				}else{
					if(null == getModel().getId()){ //添加
					      getModel().setSubCompany(getModel().getParentDept().getSubCompany());
					      getModel().setJudgement(DeptConstants.DEPARTMENT); //部门
				     }
					if(null != getModel().getId()){ //修改
						if(getModel().getJudgement().equals(DeptConstants.BRANCH)){  //分公司 --》部门
							List<Dept> dtList = getManager().selectOther(getModel());
							if(dtList.size() == 1){      //有一个引用（也就是修改记录本身）
								for(Dept dept : dtList){
									subCompanyManager.remove(dept.getSubCompany());
								}
							}
						}
						getModel().setJudgement(DeptConstants.DEPARTMENT); //部门
						getModel().setSubCompany(getModel().getParentDept().getSubCompany());
					}
				}
			}else{
				//首先判断数据库中是否存在总公司，如果存在添加Judgement为5的同级部门，如果不存在添加总公司
				List<Dept> deptList = this.getManager().selectOther();
				if(deptList.size()==0 || deptList == null){ //不存在总公司添加总公司
					getModel().setJudgement(DeptConstants.PARENTCOMPANY);  //总公司
				}else{ //如总公司存在添加同级机构
					if(getModel().getId() == null){  //新增
						   boolean flag = this.getManager().selectD();
							if(flag == true){ //新增
								 getModel().setJudgement(DeptConstants.OTHERDEPT);
								 getModel().setJgbm(DeptConstants.OTHERDEPT); //表示为监管部门或者监管部门下的子部门
								 if (null != getModel().getJgDept() &&! ",".equals(getModel().getJgDept())) {
										getModel().setJgDept(getModel().getJgDept());
									}else{
										getModel().setJgDept(null);
								}
							}else{
								//判断第3个表头是否存在
								if(getModel().getId() == null){
									boolean flagThree = this.getManager().selectThree();
									if(flagThree){ //新增
										getModel().setJudgement(DeptConstants.BRANCH); //分公司
										List<SubCompany> subList = subCompanyManager.selectSubname(getModel());
										if(subList.size() == 0){
											SubCompany subCompany = new SubCompany();
											subCompany.setName(getModel().getName());
											subCompany.setDeptSort("1"); //表示分公司
											subCompanyManager.save(subCompany);
											Integer id = getManager().saveSubid(subCompany);
											SubCompany sub = subCompanyManager.get(id);
											getModel().setSubCompany(sub);
										}
									}else{
										addActionError("您操作有误,请正确操作!");
								    	return INPUT;
									}
								}
							}
					}else{ //修改
						    boolean flag = this.getManager().st(getModel());
						    if(flag == true){
						    	getModel().setJudgement(DeptConstants.OTHERDEPT);
						    	getModel().setJgbm(DeptConstants.OTHERDEPT); //表示为监管部门或者监管部门下的子部门
						    	getModel().setJgbm(DeptConstants.OTHERDEPT);
								getModel().setParentDept(null);	
						    }else{
						    	boolean flagThree = this.getManager().sqj(getModel());
								if(flagThree){ //修改
									getModel().setJudgement(DeptConstants.BRANCH); //分公司
									List<SubCompany> subList = subCompanyManager.selectSubname(getModel());
									if(subList.size() == 0){
										SubCompany subCompany = new SubCompany();
										subCompany.setName(getModel().getName());
										subCompany.setDeptSort("1"); //表示分公司
										subCompanyManager.save(subCompany);
										Integer id = getManager().saveSubid(subCompany);
										SubCompany sub = subCompanyManager.get(id);
										getModel().setSubCompany(sub);
									}
						    }else{
						    	addActionError("您操作有误,请正确操作!");
						    	return INPUT;
						    }
						    }
					}				
				}			
			}
			
			if(null != this.getRequest().getParameter("r1")){      //新增
				getModel().setJudgement(this.getRequest().getParameter("r1"));
				getModel().setJgDept(null);
				if (parentId != null) {
					Dept d = getManager().get(parentId);   //父类
					if(d != null && d.getJgbm() != null && d.getJgbm().equals(DeptConstants.OTHERDEPT)){  //5
						getModel().setJgbm(DeptConstants.OTHERDEPT);
						getModel().setSubCompany(d.getSubCompany());
					}
				}
			}
			getManager().getDao().getHibernateTemplate().clear();
			getManager().save2(getModel());
			return SUCCESS;
		} catch (ApplicationException e) {
			addActionError(e.getMessage());
			return INPUT;
		}
	}

	/**
	 * 机构管理模块添加管理部门
	 * @author Geng WeiWei
	 * 2014-09-11
	 */
	/*
	 * ztree返回JSON字符串
	 * 公文分送使用
	 * */
	public String addDeptZtree(){
		List<String> ztree = new ArrayList<String>();
		List<Dept> depts = getManager().query(" from Dept d where d.subCompany.id is not null ");
		if(depts.size() != 0){
			for(Dept dept :depts){
				String temp = getManager().toZtreeList(dept,deptIds);
				ztree.add(temp);
			}
			ztreeStr = "[";
			for(String str : ztree){
				ztreeStr += str + "," ;
			}
			ztreeStr = ztreeStr.substring(0, ztreeStr.length()-1);
			ztreeStr += "]";
		}
		return "ztree";
	}
	/**
	 * 处理parentDept为null的情况
	 */
	@Override
	@SkipValidation
	public String edit() {
		if (getModel().getId() != null) {
			setModel(getManager().get(getModel().getId()));
			/*if (getModel().getParentDept() == null) {
				Dept dept = new Dept(); // 构建一个父部门
				dept.setId(DeptConstants.TOP_DEPT_ID);
				dept.setName(DeptConstants.TOP_DEPT_NAME);
				getModel().setParentDept(dept);
				getManager().getDao().evict(getModel()); // 将dept脱离hibernate
				logger.debug("编辑第一级部门");
			}*/
		}

		return INPUT;
	}

	/**
	 * 重置所有部门编号
	 */
	@SkipValidation
	public String updateSerialNo() {
		serialNoManager.updateAllSerialNo();
		return SUCCESS;
	}

	/**
	 * 删除部门
	 * 
	 * @author WangYaping
	 * @param dept
	 * @return
	 * @Description:
	 */
	@Override
	public String remove() {
		if(getModel().getChildDepts().size()!=0){
			addActionError("该处室含有子级部门，请先删除子级部门！");
			return "success";
		}
		
		List<User> users = userManager.getUsersBydeptId(getModel().getId());
		
		int size2 = users.size();
			if(null != getModel()
					&& null != getModel().getJudgement()
					&& getModel().getJudgement().equals(DeptConstants.BRANCH)){  //删除分公司时
				if(null != getModel().getSubCompany()){
					List<Dept> lt = getManager().select(getModel());
					 if(null != lt && lt.size() >1){  //只有本身引用
						   getManager().getDao().getHibernateTemplate().clear();
							getManager().delete(getModel());// 删除的方法
							addActionMessage("删除成功！");
					 }else{
						 SubCompany sub = getModel().getSubCompany();
						 getManager().getDao().getHibernateTemplate().clear();
						 getManager().delete(getModel());// 删除的方法
						subCompanyManager.getDao().getHibernateTemplate().clear();
						subCompanyManager.remove(sub);
						addActionMessage("删除成功！");
				
				}
			}else{
				if(null !=getModel()){
					if(null != this.getManager().get(getModel().getId())){
						getManager().getDao().getHibernateTemplate().clear();
						getManager().delete(getModel());// 删除的方法
						addActionMessage("删除成功！");
					}
				}
			}
			
		}	
		return "success";
	}

	/*
	 * ztree返回JSON字符串
	 * 机构模块监管部门也调用此方法
	 * */
	public String deptZtree(){
		List<String> ztree = new ArrayList<String>();
		List<Dept> depts = getManager().query(" from Dept d where 1=1 order by d.deptSort");
		if(depts.size() != 0){
			for(Dept dept :depts){
				String temp = getManager().toZtreeList(dept,deptIds);
				ztree.add(temp);
			}
			ztreeStr = "[";
			for(String str : ztree){
				ztreeStr += str + "," ;
			}
			ztreeStr = ztreeStr.substring(0, ztreeStr.length()-1);
			ztreeStr += "]";
		}
		return "ztree";
	}
	
	/**
	 * 在机构管理中选择监管部门时的树
	 * 值显示瑞海的分公司
	 * @author Geng ww
	 * @return
	 */
	public String deptZtreeJg(){
		List<String> ztree = new ArrayList<String>();
		List<Dept> depts = getManager().query(" from Dept d where (d.judgement = '4' or d.judgement='0')  order by d.id desc");
		if(depts.size() != 0){
			for(Dept dept :depts){
				String temp = getManager().toZtreeList(dept,deptIds);
				ztree.add(temp);
			}
			ztreeStr = "[";
			for(String str : ztree){
				ztreeStr += str + "," ;
			}
			ztreeStr = ztreeStr.substring(0, ztreeStr.length()-1);
			ztreeStr += "]";
		}
		return "ztree";
	}
	/*
	 * ztree返回JSON字符串
	 * 公文分送使用
	 * @au
	 * */
	public String deptZtreeFs(){
		List<String> ztree = new ArrayList<String>();
		List<Dept> depts = getManager().query(" from Dept d where d.subCompany.id is not null  and ((d.judgement = '2'  or d.judgement = '1'  or d.judgement = '0' or d.judgement = '4'  )   or ( d.subCompany.id =' "+getLoginUser().getDept().getSubCompany().getId()+"'))  and d.jgbm is null order by d.deptSort");
		//List<Dept> depts = getManager().query(" from Dept d where 1=1 and d.parentDept.is is null and d.judgement != '5' ");
		if(depts.size() != 0){
			for(Dept dept :depts){
				String temp = getManager().toZtreeList(dept,deptIds);
				ztree.add(temp);
			}
			ztreeStr = "[";
			for(String str : ztree){
				ztreeStr += str + "," ;
			}
			ztreeStr = ztreeStr.substring(0, ztreeStr.length()-1);
			ztreeStr += "]";
		}
		return "ztree";
	}
	
	/**
	 * 部门类型MAP
	 *//*
	public Map<String, String> getDeptTypeMap() {
		return DeptConstants.DEPT_TYPE_MAPS;
	}*/
	/*部门类型*/
	public Map<String,String> getDeptTypeMap(){
		List<Dicts> deptList = dictsManager.getDictsByCategory("dept");
		Map deptTypeMap = new LinkedHashMap();
		for(Dicts dict : deptList){
			deptTypeMap.put(dict.getId(), dict.getName());
		}
		return deptTypeMap;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public List<Map<String, Object>> getDepts() {
		return depts;
	}

	public void setDepts(List<Map<String, Object>> depts) {
		this.depts = depts;
	}

	public Map<String, Object> getResult() {
		return result;
	}

	public void setResult(Map<String, Object> result) {
		this.result = result;
	}

	public String getZtreeStr() {
		return ztreeStr;
	}

	public void setZtreeStr(String ztreeStr) {
		this.ztreeStr = ztreeStr;
	}

	public String getDeptIds() {
		return deptIds;
	}

	public void setDeptIds(String deptIds) {
		this.deptIds = deptIds;
	}
	public String getSubId() {
		return subId;
	}

	public void setSubId(String subId) {
		this.subId = subId;
	}

	public List<Dept> getDeptList() {
		return deptList;
	}

	public void setDeptList(List<Dept> deptList) {
		this.deptList = deptList;
	}
	public DictsManager getDictsManager() {
		return dictsManager;
	}

	public void setDictsManager(DictsManager dictsManager) {
		this.dictsManager = dictsManager;
	}
	
}
