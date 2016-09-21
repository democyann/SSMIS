package com.systop.common.modules.dept.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.systop.common.modules.dept.DeptConstants;
import com.systop.common.modules.dept.model.Dept;
import com.systop.common.modules.security.user.model.User;
import com.systop.common.modules.subcompany.model.SubCompany;
import com.systop.common.modules.subcompany.service.SubCompanyManager;
import com.systop.core.ApplicationException;
import com.systop.core.service.BaseGenericsManager;

/**
 * 部门管理Manager
 * 
 * @author Sam Lee
 * 
 */
@Service
public class DeptManager extends BaseGenericsManager<Dept> {
	/**
	 * 用于计算部门编号
	 */
	private DeptSerialNoManager serialNoManager;
	
	/**
	 * 公司业务类
	 */
	@Autowired
	private SubCompanyManager subCompanyManager;

	@Autowired(required = true)
	public void setSerialNoManager(DeptSerialNoManager serialNoManager) {
		this.serialNoManager = serialNoManager;
	}

	/**
	 * 保存部门信息
	 * 
	 * @see BaseGenericsManager#save(java.lang.Object)
	 */
	@Transactional
	public void save(Dept dept,Integer companyId) {
		Assert.notNull(dept);
		if (dept.getId() == null) {// 新建的部门设置部门编号
			dept.setSerialNo(serialNoManager.getSerialNo(dept));
		}
		
		if(getDao().exists(dept, "name","parentDept")){
			throw new ApplicationException("部门名称重复 ：" + dept.getName());
		}
		super.save(dept);
	}
	
	/**
	 * 保存部门信息
	 * 
	 * @see BaseGenericsManager#save(java.lang.Object)
	 */
	@Transactional
	public void save2(Dept dept) {
		Assert.notNull(dept);
		if (dept.getId() == null) {// 新建的部门设置部门编号
			dept.setSerialNo(serialNoManager.getSerialNo(dept));
		}
		
		if(getDao().exists(dept, "name","parentDept")){
			throw new ApplicationException("部门名称重复 ：" + dept.getName());
		}
		super.save(dept);
	}
	
	/**
	 * 删除部门，解除关联关系
	 */

	@SuppressWarnings("unchecked")
	@Transactional
	public void delete(Dept dept) {
		
		Assert.notNull(dept);
		/**
		 * 部门隐藏，与角色关系不存在 //解除部门-角色关联 dept.setRoles(Collections.EMPTY_SET);
		 */
		// 解除父部门关联
		dept.setParentDept(null);
		// 解除子部门关联
		Set<Dept> children = dept.getChildDepts();
		for (Dept child : children) {
			child.setParentDept(null);
		}
		dept.setChildDepts(Collections.EMPTY_SET);
		super.remove(dept);
	}

	/**
	 * 根据部门名称得到部门对应的实体
	 * 
	 * @return
	 */
	public Dept getDeptByName(String name) {
		String hql = "from Dept d where d.name = ? and d.subCompany.deptSort = ? ";
		Dept dept = (Dept) getDao().findObject(hql, name, DeptConstants.TYPE_COMPANY);
		return dept;
	}
	
	/**
	 * 根据公司subCompanyId获得部门
	 * @param parentId
	 * @return
	 */
	public List<Dept> getDeptsBySubCompany(Integer subCompanyId){
		StringBuffer sql = new StringBuffer("from Dept d where d.subCompany.id = ? ");
		List<Object> args = new ArrayList<Object>();
		args.add(subCompanyId);
		sql.append("order by d.serialNo ");
		return query(sql.toString(), args.toArray());
	}
	
	/**
	 * 根据公司subCompanyId获得公司首个部门
	 * @param parentId
	 * @return
	 */
	public List<Dept> getMaxDeptsBySubCompany(Integer subCompanyId){
		StringBuffer sql = new StringBuffer("from Dept d where d.subCompany.id = ? and d.parentDept.id is null");
		List<Object> args = new ArrayList<Object>();
		args.add(subCompanyId);
		
		return query(sql.toString(), args.toArray());
	}
	/**
	 * 根据parentId获得子部门
	 * @param parentId
	 * @param dept 
	 * @return
	 */
	public List<Dept> getDeptsByParentId(Integer parentId,Integer companyId, Dept dept) {
		
		StringBuffer sql = new StringBuffer("from Dept d where 1=1 ");
		List<Object> args = new ArrayList<Object>();
        
		if(companyId != null){
			SubCompany subCompany = subCompanyManager.get(companyId);
			if(subCompany.getDeptSort().equals("1")){   //分公司
			sql.append("and d.subCompany.id = ? ");
			args.add(companyId);
			}
		}
		if(null != dept){ //修改
			if(parentId != null){
				sql.append("and d.parentDept.id = ? ");
				args.add(parentId);
			}else{
				if(null != dept.getJgbm() && dept.getJgbm().equals("5")){
				   /* sql.append(" and d.judgement = '5' and d.parentDept.id is null ");*/
					sql.append("and d.jgbm = '5' and d.parentDept.id is null ");
				}else if(this.getDeptSelect(dept).getParentDept() == null && this.getDeptSelect(dept).getJudgement().equals("4")){
					sql.append(" and d.judgement = '4' and d.parentDept.id is null ");	
				}else{
					sql.append(" and d.judgement = '0' and d.parentDept.id is null ");	
				}
			}
			
		}else{  //添加
			if(parentId != null){
				sql.append("and d.parentDept.id = ? ");
				args.add(parentId);
			}else{
				sql.append("and d.parentDept.id is null ");
			}
			
		}
        sql.append("order by d.deptSort ");		
		return query(sql.toString(), args.toArray());
	}
	/**
	 * 查询总公司和秦检公司人员
	 * @author Geng ww
	 * @return
	 */
	public List<Dept> getDeptsSelectRadio(Integer parentId,Integer companyId, Dept dept) {
		StringBuffer sql = new StringBuffer("from Dept d where 1=1 ");
		List<Object> args = new ArrayList<Object>();
        
		if(companyId != null){
			SubCompany subCompany = subCompanyManager.get(companyId);
			if(subCompany.getDeptSort().equals("1")){   //分公司
			sql.append("and d.subCompany.id = ? ");
			args.add(companyId);
			}
		}
		if(null != dept){ //修改
			if(parentId != null){
				sql.append("and d.parentDept.id = ? ");
				args.add(parentId);
			}else{
				if(null != dept.getJgbm() && dept.getJgbm().equals("5")){
				   /* sql.append(" and d.judgement = '5' and d.parentDept.id is null ");*/
					sql.append("and d.jgbm = '5' and d.parentDept.id is null ");
				}else if(this.getDeptSelect(dept).getParentDept() == null && this.getDeptSelect(dept).getJudgement().equals("4")){
					sql.append(" and d.judgement = '4' and d.parentDept.id is null ");	
				}else{
					sql.append(" and d.judgement = '0' and d.parentDept.id is null ");	
				}
			}
			
		}else{  //添加
			if(parentId != null){
				sql.append("and d.parentDept.id = ? ");
				args.add(parentId);
			}else{
				sql.append("and d.parentDept.id is null ");//and d.judgement !='5'
			}
			
		}
        sql.append("order by d.deptSort ");		
		return query(sql.toString(), args.toArray());
	}
	/**
	 * 取得dept的第一级别
	 * @param dept
	 * @return
	 */
	private Dept getDeptSelect(Dept dept) {
		if(dept.getParentDept() == null && dept.getJudgement().equals("4")){
    		return dept;
	    }
			return getDeptSelect(dept.getParentDept());
		}

	/**
	 * 根据parentId获得子部门 
	 * @param parentId
	 * @return
	 */
	public List<Dept> getDeptsByParentId1(Integer parentId,Integer companyId,String fen) {
		
		StringBuffer sql = new StringBuffer("from Dept d where 1=1 ");
		List<Object> args = new ArrayList<Object>();
        
		if(companyId != null){
			sql.append("and d.subCompany.id = ? ");
			args.add(companyId);
		}
		if(!StringUtils.isNotBlank(fen)){
			 sql.append(" and d.judgement != 2");
		}
		if(parentId != null){
			sql.append("and d.parentDept.id = ? ");
			args.add(parentId);
		}
		/*//验证循环节点是否为分支机构
		if(getFenZhiJiGou()!=null){
			List<Dept> depts = getFenZhiJiGou();
			if(parentId == depts.get(0).getId()){
				sql.append(" and d.id = ? ");
				args.add(fen);
			}
		}*/
		sql.append("order by d.serialNo ");
		
		return query(sql.toString(), args.toArray());
	}

	public List<Dept> getDeptList(){
		String hql = "from Dept d where d.id not in (select de.parentDept.id from Dept de where de.parentDept.id is not null)";
		List<Dept> list = query(hql);
		return list;
	}
	
	/**
	   * 获取所有部门信息
	   * @return
	   */

		public Map getDeptsMap() {
			List<Dept> deptList = query("from Dept d ");
			Map deptMap = new LinkedHashMap();
			for (Dept dept : deptList) {
				deptMap.put(dept.getId(), dept.getName());
			}
			return deptMap;
		}
		
		public Dept getSchoolDept(Dept dept){
			if(dept.getDeptType().getCode().equals("school")){
				return dept;
			}else{
				return getSchoolDept(dept.getParentDept());
			}
		}
	/**
	 * 通过公司id拿到一组对象
	 * 
	 * @param id
	 * @return
	 */
	public List<Dept> queryByTypeId(Integer id) {

		StringBuffer hql = new StringBuffer("from Dept p where 1=1 ");
		List<Object> args = new ArrayList<Object>();
		if (id != 0) {
			hql.append("and p.subCompany.id = ? ");
			args.add(id);
		}
		hql.append("order by p.id ");
		return query(hql.toString(), args.toArray());
	}
	
	public String toZtreeList(Dept dept, String deptIds){
		
		String str = new String();
		str = "{id:" + dept.getId() + ",name:'" + dept.getName() + "',";
		if(StringUtils.isNotBlank(deptIds)){			
			/*String[] ids = deptIds.split(",");
			if(Arrays.binarySearch(ids, dept.getId()+"")>=0){
				str += "checked:true,";
			}*/
			if(deptIds.contains("," + dept.getId() + ",")){
				str += "checked:true,";
			}
		}
		if (dept.getParentDept() != null ) {
			str += "pId:" + dept.getParentDept().getId() +"}";
		}else{
			str += "pId:-1}";
		}
		return str;
	}

	/**
	 * @param subCompany
	 * @return
	 */
	/**
	 * @param subCompany
	 * @return
	 */
	@Transactional
    public Integer saveSubid(SubCompany subCompany) {
    	Integer id = 0;
		id = getDao().merge(subCompany).getId();
		return id;
    }

	/**
	 * @param model
	 * @return
	 */
    public List<Dept> selectDp(Dept model) {
	    // TODO Auto-generated method stub
	    return null;
    }

	/**
	 * @param model
	 * @return
	 */
    public List<Dept> select(Dept dept) {
    	String hql = "from Dept d where 1 = 1 and d.subCompany.id=?  order by id desc";
		List<Dept> depts =query(hql,dept.getSubCompany().getId());
		return depts;
    }

	/**
	 * @param loginUser
	 * @return
	 */
	public Map<String,String> getDept(User user){
		Map<String,String> deptm = new HashMap<String,String>();
		String hql = "from Dept d where 1 = 1 and d.parentDept.id=?  order by id desc";
		List<Dept> depts =query(hql,user.getDept().getSubCompany().getId());
		for(Dept d : depts){
			deptm.put(d.getId().toString(),d.getName());
		}
		return deptm;
	}

	/**
	 * 分公司修改为部门---》检查此分公司是否在dept表中有引用，如果有保留此公司，如果没有删除
	 * @param model
	 * @return
	 */
    public List<Dept> selectOther(Dept dept) {
    	String hql = "from Dept d where 1 = 1 and d.subCompany.id=?  order by id desc";
		List<Dept> depts =query(hql,dept.getSubCompany().getId());
		return depts;
    }

	public List<Dept> getAllDeptsBySubCompany(int parseInt) {
		String hql = "from Dept d where d.subCompany.id = ? and d.judgement = 3 and d.jgbm is null";
		List<Object> args = new ArrayList<Object>();
		args.add(parseInt);
		return query(hql, args.toArray());
	}

	/**
	  * @author Geng WeiWei
	  * @created 2014-9-11
	  * @return
	 */
    public List<Dept> selectOther() {
    	String hql = "from Dept d where d.judgement = ?";
    	List<Object> args = new ArrayList<Object>();
		args.add(DeptConstants.PARENTCOMPANY); //
		return query(hql, args.toArray());
    }

	/**
	  * 查询监督部门是否存在如果存在不能重复添加 
	  * @author Geng WeiWei
	  * @created 2014-9-12
	  * @param model
	  * @return
	 */
    public boolean st(Dept dp) {
    	Dept dept = null;
    	boolean flag = true;
    	String hql = "from Dept d where d.judgement = ?";
    	List<Object> args = new ArrayList<Object>();
		args.add(DeptConstants.OTHERDEPT); //
		List<Dept> dpList =  query(hql, args.toArray());
		if(dpList.size() >0){
			 dept = dpList.get(0);
			 if(dp.getId()== dept.getId()){
				 flag = true;
			 }else{
				 flag = false;
			 }
		}
		return flag;
    }

	/**
	  * 查询监管部门是否存在，如果存在则不能再重复添加
	  * @author Geng WeiWei
	  * @created 2014-9-16
	  * @return
	 */
    public boolean selectD() {
    	boolean flag = true;
    	String hql = "from Dept d where d.judgement = ?";
    	List<Object> args = new ArrayList<Object>();
		args.add(DeptConstants.OTHERDEPT); //监管部门
		List<Dept> dpList =  query(hql, args.toArray()); 
		if(dpList.size() >0){
				 flag = false;
			 }else{
				 flag = true;
			 }
		return flag;
    }

    /**
     * 取子公司列表
     * @return
     */
    public List<Dept> getSubDept(){
    	List<Dept> subDepts = new ArrayList<Dept>();
    	subDepts =  query("from Dept d where d.judgement=4");
    	return subDepts;
    	
    }
    /**
     * 取总公司列表
     * @return
     */
    public List<Dept> getSubCompanyDept(){
    	List<Dept> subDepts = new ArrayList<Dept>();
    	subDepts =  query("from Dept d where d.judgement=0");
    	return subDepts;
    	
    }
    /**
     * 得到子公司和和部门
     */
    public List<Dept> getAllDeptList(){
    	List<Dept> subDepts = new ArrayList<Dept>();
    	subDepts =  query("from Dept d where d.judgement=4 or (d.judgement=3 and d.jgbm is null)");
    	return subDepts;
    	
    }
   /**
    * 判断第三个头是否存在
    * @return
    */
	public boolean selectThree() {
		boolean flag = true;
    	String hql = "from Dept d where d.judgement = ? and d.parentDept is null";
    	List<Object> args = new ArrayList<Object>();
		args.add(DeptConstants.BRANCH); //秦检口岸卫生科技服务有限公司
		List<Dept> dpList =  query(hql, args.toArray()); 
		if(dpList.size() >0){
				 flag = false;
			 }else{
				 flag = true;
			 }
		return flag;
	}

/**
 * 查询秦检部门是否存在
 * @param model
 * @return
 */
public boolean sqj(Dept dp) {
	Dept dept = null;
	boolean flag = true;
	String hql = "from Dept d where d.judgement = ? and d.parentDept is null";
	List<Object> args = new ArrayList<Object>();
	args.add(DeptConstants.BRANCH); //
	List<Dept> dpList =  query(hql, args.toArray());
	if(dpList.size() >0){
		 dept = dpList.get(0);
		 if(dp.getId()== dept.getId()){
			 flag = true;
		 }else{
			 flag = false;
		 }
	}
	return flag;
}

/**
 * 所有公司人员
 * @param object
 * @param object2
 * @param object3
 * @return
 */
public List<Dept> getDeptsSelectAllRadio(Integer parentId,Integer companyId, Dept dept) {
	StringBuffer sql = new StringBuffer("from Dept d where 1=1 ");
	List<Object> args = new ArrayList<Object>();
    
	if(companyId != null){
		SubCompany subCompany = subCompanyManager.get(companyId);
		if(subCompany.getDeptSort().equals("1")){   //分公司
		sql.append("and d.subCompany.id = ? ");
		args.add(companyId);
		}
	}
	if(null != dept){ //修改
		if(parentId != null){
			sql.append("and d.parentDept.id = ? ");
			args.add(parentId);
		}else{
			if(null != dept.getJgbm() && dept.getJgbm().equals("5")){
			   /* sql.append(" and d.judgement = '5' and d.parentDept.id is null ");*/
				sql.append("and d.jgbm = '5' and d.parentDept.id is null ");
			}else if(this.getDeptSelect(dept).getParentDept() == null && this.getDeptSelect(dept).getJudgement().equals("4")){
				sql.append(" and d.judgement = '4' and d.parentDept.id is null ");	
			}else{
				sql.append(" and d.judgement = '0' and d.parentDept.id is null ");	
			}
		}
		
	}else{  //添加
		if(parentId != null){
			sql.append("and d.parentDept.id = ? ");
			args.add(parentId);
		}else{
			sql.append("and d.parentDept.id is null ");
		}
		
	}
    sql.append("order by d.deptSort ");		
	return query(sql.toString(), args.toArray());
}



	/**
	 * 根据部门编码得到部门对应的实体
	 * 
	 * @return
	 */
	public Dept getDeptByCode(String code) {
		String hql = "from Dept d where d.code = ? ";
		Dept dept = (Dept) getDao().findObject(hql, code);
		return dept;
	}



}
