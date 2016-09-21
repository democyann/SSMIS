package com.systop.common.modules.dict.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.systop.common.modules.dict.model.Dicts;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.service.BaseGenericsManager;

/**
 * @ClassName: DictsManager
 * @Description:字典管理模块
 * @author 乔轩
 * @date 2013-5-14 下午05:41:16
 * 
 */

@Service
public class DictsManager extends BaseGenericsManager<Dicts> {

	/**
	 * 
	 * @Title: getDictsByCategory
	 * @Description: 根据类别取得字典表的信息
	 * @author 乔轩
	 * @date 2013-5-16 下午03:45:03
	 * 
	 * @param category
	 * @return
	 */
	public List<Dicts> getDictsByCategory(String category) {
		List<Dicts> dictList = new ArrayList<Dicts>();
		if (StringUtils.isNotBlank(category)) {
			dictList = query("from Dicts d where d.category = ? ", category);
		}
		return dictList;
	}
	
	public Dicts getDictsByName(String name) {
		String hql = "from Dicts d where d.name = ?";
		return findObject(hql, name);
	}

	public Dicts getDictsById(Integer id) {
		String hql = "from Dicts d where d.ksId = ?";
		return findObject(hql, id);
	}

	public Dicts getDictsByCode(String code) {
		String hql = "from Dicts d where d.code = ?";
		return findObject(hql, code);
	}

	/**
	 * 根据类型查询字典表中的企业委托录入数据
	 * 
	 * @author Geng WeiWei
	 * @created 2014-11-3
	 * @return
	 */
	public List<Dicts> selectOfList() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 
	 * @param ids
	 * @return
	 * @Description:TODO 通过id1,id2,id3格式的id字符串获取dicts集合
	 * @author: lizhichao
	 * @date:2014-11-18下午2:58:30
	 */
	public List<Dicts> getDictsByIds(String ids) {
		List<Dicts> dicts = new ArrayList<Dicts>();
		if (StringUtils.isBlank(ids)) {
			return null;
		}
		for (String id : ids.split(",")) {
			if (StringUtils.isNotBlank(id)) {
				dicts.add(get(Integer.parseInt(id)));
			}
		}
		return dicts;
	}

	/**
	 * 
	 * @param ids
	 * @return
	 * @Description:TODO 通过id1,id2,id3格式的id字符串获取name1,name2,name3字符串
	 * @author: WangYaping
	 * @date:2014-11-21上午11:29:05
	 */
	@Transactional
	public String ditcIdsToString(String ids) {
		StringBuffer names = new StringBuffer();
		if (StringUtils.isBlank(ids)) {
			return null;
		}
		for (String id : ids.split(",")) {
			if (StringUtils.isNotBlank(id.trim())) {
				Dicts dicts = get(Integer.parseInt(id.trim()));
				if (dicts != null) {
					names.append(dicts.getName());
					names.append(",");
				}
			}
		}
		return names.toString().substring(0, names.length()-1);
	}

	public String dictNamesToIds(String names) {
		StringBuffer idsStringBuffer = new StringBuffer("");
		if (StringUtils.isNotBlank(names)) {
			for (String name : names.split(",")) {
				Dicts dicts = findObject("from Dicts d where d.name = ?", name);
				if (dicts != null && dicts.getId() != null) {
					idsStringBuffer.append(dicts.getId());
					idsStringBuffer.append(",");
				}
			}
		}
		if(StringUtils.isNotBlank(idsStringBuffer.toString())){
			return idsStringBuffer.toString().substring(0, idsStringBuffer.toString().length()-1);
		}else{
			return idsStringBuffer.toString();
		}

	}
	/**
	 * 通过category 得到所有字典表数据
	 */
	public List<Dicts> getDictByCode(String code){
		String hql = "from Dicts d where d.category = ?";
		return query(hql,code);
	}

	/**
	 * 
	 * @param dicts
	 * @Description:导入时id存在则不做任何操作，不存在则保存,id不存在则插入一条登陆人新建的记录
	 */
	@Transactional
	public void saveByImport(Dicts dicts,User loginUser) {
		String sql = "select id from dict where id = ?";
		Session session = SessionFactoryUtils.getSession(getDao().getHibernateTemplate().getSessionFactory(), true);
		Query query = session.createSQLQuery(sql).setParameter(0, dicts.getId());
		List list = query.list();
		if (list.isEmpty()) {
			dicts.setUser(loginUser);
			save(dicts);
		}
	}

	

	
}