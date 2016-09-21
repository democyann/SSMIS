package com.systop.common.modules.sector.service;

import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.systop.common.modules.sector.model.Sector;
import com.systop.core.service.BaseGenericsManager;

/**
 * 部门管理Manager
 * 
 * @author Sam Lee
 * 
 */
@Service
@SuppressWarnings( {"unchecked","rawtypes"})
public class SectorManager extends BaseGenericsManager<Sector> {
	/**
	 * 用于计算部门编号
	 */
	private SectorSerialNoManager serialNoManager;

	@Autowired(required = true)
	public void setSerialNoManager(SectorSerialNoManager serialNoManager) {
		this.serialNoManager = serialNoManager;
	}
	@Autowired
	private JdbcTemplate jdbcTemplate;
	/**
	 * 保存部门信息
	 * 
	 * @see BaseGenericsManager#save(java.lang.Object)
	 */
	@Override
	@Transactional
	public void save(Sector sector) {
		Assert.notNull(sector);
		logger.debug("Parent sector {}", sector.getParentSector());
		// 查询上级部门并建立双向关联
		Sector parent = sector.getParentSector();
		getDao().evict(parent);
		if (parent != null && parent.getId() != null) {
			logger.debug("Parent Sector Id {}", sector.getParentSector().getId());
			parent = get(sector.getParentSector().getId());
			if (parent != null) {
				parent.getChildSectors().add(sector);
				sector.setParentSector(parent);
			}
		} else {
			sector.setParentSector(null); // 处理parentId为null的情况
		}
		if (sector.getId() == null) {// 新建的部门设置部门编号
			sector.setSerialNo(serialNoManager.getSerialNo(sector));
		}
		// FIXME: Clear the session or it will throw an exception with the
		// message:
		// "dentifier of an instance was altered from xxx to xxx"
		getDao().getHibernateTemplate().clear();
		super.save(sector);
	}

	/**
	 * 删除部门，解除关联关系
	 */

	
	@Override
	@Transactional
	public void remove(Sector sector) {
		Assert.notNull(sector);
		/**
		 * 部门隐藏，与角色关系不存在 //解除部门-角色关联 sector.setRoles(Collections.EMPTY_SET);
		 */
		// 解除父部门关联
		sector.setParentSector(null);
		// 解除子部门关联
		Set<Sector> children = sector.getChildSectors();
		for (Sector child : children) {
			child.setParentSector(null);
		}
		sector.setChildSectors(Collections.EMPTY_SET);

		super.remove(sector);
	}

	/**
	 * 根据部门名称得到部门对应的实体
	 * 
	 * @return
	 */
	public Sector getSectorByName(String name) {
		String hql = "from Sector d where d.name = ? ";
		Sector sector = (Sector) getDao().findObject(hql, name);
		return sector;
	}

	/**
	 * 根据区县ID获得该区县的所有执法部门
	 * 
	 * @param countyId
	 * @return
	 */
	public List<Sector> getEnforcementByCounty(Integer countyId) {
		// FIXME:未添加判断执法部门的条件
		String hql = "from Sector d where d.parentSector.id = ?";
		List<Sector> sectors = query(hql, countyId);
		return sectors;
	}
	
	/**
   * 获取所有部门信息
   * @return
   */

	public Map getSectorsMap() {
		List<Sector> sectorList = query("from Sector d ");
		Map sectorMap = new LinkedHashMap();
		for (Sector sector : sectorList) {
			sectorMap.put(sector.getId(), sector.getName());
		}
		return sectorMap;
	}

	/**
	 * 
	 * @param sector
	 * @Description:导入时id存在则不做任何操作，不存在则保存
	 */
	@Transactional
	public void saveByImport(Sector sector) throws SQLException {
		String sql = "select id from sector where id = ?";
		Session session = SessionFactoryUtils.getSession(getDao().getHibernateTemplate().getSessionFactory(), true);
		Query query = session.createSQLQuery(sql).setParameter(0, sector.getId());
		List list = query.list();
		if (list.isEmpty()) {
			save(sector);
		}
	}
	public List<Sector> getDownSectors(List<Sector> ss ,Sector sector){
		ss.addAll(sector.getChildSectors());
		if(sector.getChildSectors().size() == 0){
			return ss;
		}else{
			for(Sector s : sector.getChildSectors()){
				getDownSectors(ss, s);
			}
		}
		return ss;
	}
	public Sector getTopSector(Sector sector) {
		if (sector.getParentSector() == null) {
			return sector;
		} else {
			return getTopSector(sector.getParentSector());
		}
	}
	
}
