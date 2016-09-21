package com.systop.core.service;

import java.io.Serializable;
import java.util.List;

import com.systop.core.dao.hibernate.BaseHibernateDao;

/**
 * 所有Service层的基础接口。提供基本的CRUD能力以及通往DAO层的方法。
 * 
 * @author Sam
 * @version 1.0
 */

public interface Manager<T> {
	/**
	 * @return instance of <code>BaseHibernateDao</code>
	 */
	BaseHibernateDao getDao();

	/**
	 * @see {@link BaseHibernateDao#save(Object)}
	 */
	void create(T entity);

	/**
	 * @see {@link BaseHibernateDao#update(Object)}
	 */
	void update(T entity);

	/**
	 * @see {@link BaseHibernateDao#saveOrUpdate(Object)}
	 */
	void save(T entity);

	/**
	 * @see {@link BaseHibernateDao#delete(Object)}
	 */
	void remove(T entity);

	/**
	 * @see {@link BaseHibernateDao#get(Class, Serializable)}
	 */
	T get(Serializable id);

	/**
	 * @see {@link BaseHibernateDao#get(Class)}
	 */
	List<T> get();
}
