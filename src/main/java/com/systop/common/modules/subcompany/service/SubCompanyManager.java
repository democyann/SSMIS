package com.systop.common.modules.subcompany.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.LinkedMap;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.systop.common.modules.dept.DeptConstants;
import com.systop.common.modules.dept.model.Dept;
import com.systop.common.modules.subcompany.model.SubCompany;
import com.systop.core.service.BaseGenericsManager;
/**
 * 公司信息
 * @author WangYaping
 *
 */
@Service
public class SubCompanyManager extends BaseGenericsManager<SubCompany>{
	/**
	 * 查询全部
	 * 
	 * @return
	 */
	public List<SubCompany> queryAll() {
		String sql = "from SubCompany j";
		return query(sql);
	}
	
	
	
	/**
	 * 查到最大的公司id
	 */
	@Transactional
	public int getNogh(){
	
		String hql = "select max(p.id) from SubCompany p";
	    getDao().getHibernateTemplate().clear();
		String id = getDao().findObject(hql).toString().trim();
		int id1 = Integer.parseInt(id);
		return id1;
		
	}
	
	/**
	 * 用于下拉列表公司的数据源
	 */
	@SuppressWarnings("unchecked")
	public Map<Integer, String> getTypeMap(){
		List<SubCompany> items = this.queryAll();
		Map<Integer, String> typeMap = new LinkedMap();
		for (SubCompany sc : items) {
			typeMap.put(sc.getId(), sc.getName());
		}
		return typeMap;
	}
	/**
	 * 公司列表
	 */
	public Map<String,String> getsubMap(){
		Map<String,String> subm =new HashMap<String,String>();
		String hql = "from SubCompany";
		List<SubCompany> subs = query(hql);
		for(SubCompany sub : subs){
			subm.put(sub.getId().toString(),sub.getName());
		}
		return subm;
	}
	/**
	 * 公司 list map
	 */
	public List<Map<String,Object>> getSubListMap(){
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		String hql = "from SubCompany";
		List<SubCompany> subs = query(hql);
		for(SubCompany sub : subs){
			Map<String,Object> subm =new HashMap<String,Object>();
			subm.put("id",sub.getId().toString());
			subm.put("name",sub.getName());
			list.add(subm);
		}
		return list;
	}
	/**
	 * 分公司公司 list map
	 */
	public List<Map<String,Object>> getMinsubListMap(){
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		String hql = "from SubCompany s where s.deptSort='1'";
		List<SubCompany> subs = query(hql);
		for(SubCompany sub : subs){
			Map<String,Object> subm =new HashMap<String,Object>();
			subm.put("id",sub.getId().toString());
			subm.put("name",sub.getName());
			list.add(subm);
		}
		return list;
	}

	/**
	 * @param model
	 * @return
	 */
    public List<SubCompany> selectName(Dept dept) {
	    // TODO Auto-generated method stub
    	String hql = " from SubCompany s where s.name=?";
    	List<SubCompany> list = query(hql,dept.getName());
	    return list;
    }



	/**
	 * 根据分公司Id查出分公司
	 * @param model
	 * @return
	 */
    public List<SubCompany> selectID(Dept dept) {
    	String hql = " from SubCompany s where s.id=?";
    	List<SubCompany> list = query(hql,dept.getSubCompany().getId());
	    return list;
    }



	/**
	 * 通过要添加的name,查看公司表中是否也有此数据
	 * 如果有提示不添加，如果没有添加
	 * @param model
	 * @return
	 */
    public List<SubCompany> selectSubname(Dept dept) {
    	String hql = " from SubCompany s where s.name=?";
    	List<SubCompany> list = query(hql,dept.getName());
	    return list;
    }



	/**
	 * 查询总公司
	  * @author Geng WeiWei
	  * @created 2014-9-11
	  * @return
	 */
    public List<SubCompany> selectSuperCom() {
    	String hql = " from SubCompany s where s.deptSort=?";
    	List<SubCompany> list = query(hql,DeptConstants.TYPE_COMPANY);
	    return list;
    }



	/**
	  * 查询公司集合
      * @author Geng WeiWei
      * @created 2014-12-9
      * @return
     */
    public List<SubCompany> selectOther(String deptSort) {
    	String hql = " from SubCompany s where s.deptSort=?";
    	List<SubCompany> list = query(hql,deptSort);
	    return list;
    }



	/**
	  * 查询所有的公司  where 1=1 and s.deptSort = '1'
      * @author Geng WeiWei
      * @created 2014-12-9
      * @return
     */
    public List<SubCompany> selectCompany() {
    	String hql = " from SubCompany s order by s.deptSort";
    	List<SubCompany> list = query(hql);
	    return list;
    }
	
}
