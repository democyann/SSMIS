package com.systop.core.service;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.systop.core.dao.hibernate.BaseHibernateDao;
import com.systop.core.dao.support.Page;
import com.systop.core.util.GenericsUtil;


/**
 * 使用Hibernate作为持久化框架的实现的Manager基类.
 * 
 * @author Sam Lee
 * @version 3.0
 */
public class BaseGenericsManager<T> implements Manager<T> {
	/**
	 * Log,子类可以直接使用，不必重新声明
	 */
	protected Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * Manager所管理的实体对象类型.
	 */
	protected Class<T> entityClass;

	/**
	 * Manager所使用的Dao对象.
	 */
	private BaseHibernateDao dao;

	/**
	 * @see Manager#getDao()
	 */
	public BaseHibernateDao getDao() {
		return dao;
	}

	@Autowired
	public void setBaseHibernateDao(BaseHibernateDao dao) {
		this.dao = dao;
	}

	/**
	 * @see Manager#create(java.lang.Object)
	 */
	@Transactional
	public void create(T entity) {
		getDao().save(entity);
	}

	/**
	 * @see Manager#get(java.io.Serializable)
	 */
	@Transactional(readOnly = true)
	public T get(Serializable id) {
		return getDao().get(getEntityClass(), id);
	}

	/**
	 * @see Manager#get()
	 */
	@Transactional(readOnly = true)
	public List<T> get() {
		return getDao().get(getEntityClass());
	}

	/**
	 * @see Manager#remove(java.lang.Object)
	 */
	@Transactional
	public void remove(T entity) {
		getDao().delete(entity);
	}

	/**
	 * @see Manager#save(java.lang.Object)
	 * @see BaseHibernateDao#saveOrUpdate(Object).
	 */
	@Transactional
	public void save(T entity) {
		getDao().saveOrUpdate(entity);
	}

	/**
	 * 
	 * @param entity
	 * @param flag true若存在id一样的实体，则进行覆盖；false 若存在一样的实体不进行覆盖
	 */
	@SuppressWarnings({"rawtypes"})
	@Transactional
	public void save(boolean flag,T entity) {
		Object obj = null;
	     Field field;
			try {
				field = getEntityClass().getDeclaredField("id");
				PropertyDescriptor pd = new PropertyDescriptor(field.getName(),getEntityClass());
				Method getMethod = pd.getReadMethod();//获得get方法
				obj = getMethod.invoke(entity);//执行get方法返回一个Object
			} catch (Exception e) {
				e.printStackTrace();
			} 
			
		String id = obj != null ? obj.toString() : null;
		if(StringUtils.isBlank(id) && null != id){
			try {
				Method setId = entity.getClass().getDeclaredMethod("setId", String.class);
				setId.invoke(entity);
			} catch (Exception e) {
				e.printStackTrace();
			}
			getDao().save(entity);
		}else{
	/*		List list =	getDao().getHibernateTemplate().find("select count(*) from " + entityClass.getSimpleName()+" e where e.id = ?",id);
			getDao().clear();
			if(((Long)list.get(0)) > 0){
				if(flag){
					getDao().update(entity);
				}
			}else{
				getDao().save(entity);
			}*/
			Table t = entityClass.getAnnotation(Table.class);
			String tableName = t.name();
			String sql = "select id from "+tableName+" where id = ?";
			Session session = getDao().getHibernateTemplate().getSessionFactory().getCurrentSession();
			Query query = session.createSQLQuery(sql).setParameter(0, id);
			List list = query.list();
			getDao().clear();
			if(!list.isEmpty()){
				if(flag){
					getDao().update(entity);
				}
			}else{
				getDao().save(entity);
			}
		}
	}
	
	/**
	 * @see Manager#update(java.lang.Object)
	 */
	@Transactional
	public void update(T entity) {
		getDao().update(entity);
	}

	/**
	 * @see {@link BaseHibernateDao#findObject(String, Object...)}
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public T findObject(String hql, Object... args) {
		return (T) getDao().findObject(hql, args);
	}

	/**
	 * @see {@link BaseHibernateDao#query(String, Object...)}
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<T> query(String hql, Object... args) {
		return getDao().query(hql, args);
	}

	/**
	 * @see {@link BaseHibernateDao#pagedQuery(Page, DetachedCriteria)}
	 */
	@Transactional(readOnly = true)
	public Page pageQuery(Page page, DetachedCriteria criteria) {
		return getDao().pagedQuery(page, criteria);
	}

	/**
	 * @see {@link BaseHibernateDao#pagedQuery(Page, Criteria)}
	 */
	@Transactional(readOnly = true)
	public Page pageQuery(Page page, Criteria criteria) {
		return getDao().pagedQuery(page, criteria);
	}

	/**
	 * @see {@link BaseHibernateDao#pagedQuery(Page, String, Object...)}
	 */
	@Transactional(readOnly = true)
	public Page pageQuery(Page page, String hql, Object... args) {
		return getDao().pagedQuery(page, hql, args);
	}
    @SuppressWarnings("rawtypes")
	@Transactional(readOnly = true)
	public Page sqlPageQuery(Page page, String hql, Class c,Object... args) {
		return getDao().sqlPageQuery(page,hql,c,args);
	}

	/**
	 * 取得entityClass的函数. JDK1.4不支持泛型的子类可以抛开Class entityClass,重载此函数达到相同效果。
	 */
	@SuppressWarnings("unchecked")
	protected Class<T> getEntityClass() {
		if (entityClass == null) {
			entityClass = GenericsUtil.getGenericClass(getClass());
		}
		return entityClass;
	}
}
