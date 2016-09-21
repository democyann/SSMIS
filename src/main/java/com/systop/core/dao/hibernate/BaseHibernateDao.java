package com.systop.core.dao.hibernate;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.ArrayUtils;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.impl.CriteriaImpl;
import org.hibernate.impl.CriteriaImpl.Subcriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import com.systop.core.dao.support.Page;
import com.systop.core.util.ReflectUtil;

/**
 * 通用的Hiberante Data Access Object,包括了常用的Hibernate方法并支持分页查询以及自动执行count 的分页查询。 <br>
 * 多数情况下，各个模块不必实现自己的DAO类，使用这个类就可以完成功能。当DAO类存在一定的可复用性的时候，可以 从本类中继承，以便于其他模块使用。 <br>
 * 由于count查询可能对性能造成影响，所以可以在子类中覆盖{@link #count(CriteriaImpl)}或
 * {@link #count(String, Object...)} 方法，以提供更好的性能表现。 <br>
 * BaseHibernateDao对部分方法提供了泛型支持，使用这些方法的时候，不必再进行类型转换,例如：<br>
 * 
 * <pre>
 * User user = dao.get(User.class, id);
 * 
 * <pre>
 * @author Sam Lee
 * @version 2.5
 */
@Repository
// 表示这是一个DAO类，bean id为“baseHibernateDao”
@SuppressWarnings({ "unchecked", "rawtypes" })
public class BaseHibernateDao extends HibernateDaoSupport {
	/**
	 * Logger,子类可以直接使用.
	 */
	protected Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 从数据库中删除持久化实例。参数可能是一个与<code>Session</code>相关的实例，或者是一个
	 * 处于游离(transient)状态的并且具有持久化标识(identifier)的实例。 如果关联的实体被映射为
	 * <code>cascade="delete"</code>，本方法将执行级联操作。
	 * 
	 * @param entity
	 *            the instance to be removed
	 * @throws HibernateException
	 * @see {@link org.hibernate.Session#delete(Object)}
	 */
	public void delete(Object entity) {
		getHibernateTemplate().delete(entity);
	}

	/**
	 * 根据持久化实体的类型和持久化标识符(identifier)，从数据库中删除持久化实例。
	 * 
	 * @param entityClass
	 *            the passed <code>Class</code>.
	 * @param id
	 *            the passed identifer.
	 */
	public void delete(Class entityClass, Serializable id) {
		getHibernateTemplate().delete(get(entityClass, id));
	}

	/**
	 * 为一个处于游离(transient)状态的实例分配标识符(identifier),然后将它持久化(Persist)。 (如果标识符属性被映射为
	 * <code>assigned</code>,则使用现成的标识符.) 如果关联实体关联映射是
	 * <code>cascade="save-update"</code>,本方法将执行级联操作。
	 * 
	 * @param entity
	 *            a transient instance of a persistent class
	 * @return the generated identifier
	 * @throws DataAccessException
	 *             ,HibernateException
	 */
	public Serializable save(Object entity) {
		return getHibernateTemplate().save(entity);
	}

	/**
	 * 保存({@link #save(Object)})或更新({@link #update(Object)})给定的实例，这个操作
	 * 依赖于对"unsaved-value"的检查结果。具体的是：
	 * <ul>
	 * <li>如果对象已经在本session中持久化，不做任何事.</li>
	 * <li>如果与本session管理的对象具有相同持久化标识，则抛出异常.</li>
	 * <li>如果对象没有持久化标识，则调用 {@link org.hibernate.Session#save(Object)}</li>
	 * <li>如果对象的持久化标识表明这是新对象， 则调用{@link org.hibernate.Session#save(Object)}</li>
	 * <li>如果对象的版本信息标识这是一个新对象， 则调用{@link org.hibernate.Session#save(Object)}</li>
	 * <li>否则调用{@link org.hibernate.Session#update(Object)}</li>
	 * </ul>
	 * <p/>
	 * 如果关联属性被映射为<code>cascade="save-update"</code>,本方法将执行级联操作。
	 * 
	 * @see save(Object)
	 * @see update(Object)
	 * @param entity
	 *            a transient or detached instance containing new or updated
	 *            state
	 * @throws DataAccessException
	 *             ,HibernateException
	 */
	public void saveOrUpdate(Object entity) {
		getHibernateTemplate().saveOrUpdate(entity);
	}

	/**
	 * 根据处于分离(detached)状态的实体的identifier，将它持久化。如果处于持久化状态的实例有
	 * 一个identifier与之相同，则抛出异常.
	 * <p/>
	 * 如果关联属性被映射为<code>cascade="save-update"</code>,本方法将执行级联操作。
	 * 
	 * @param entity
	 *            a detached instance containing updated state
	 * @throws DataAccessException
	 *             ,HibernateException
	 */
	public void update(Object entity) {
		getHibernateTemplate().update(entity);
	}

	/**
	 * 复制给定对象到一个拥有相同的identifier的持久化对象。如果根据identifer,没有任何与当前session
	 * 关联的持久化对象，那么将先执行load操作。返回持久化对象的实例。如果给定的实例没有保存(unsaved),
	 * 则保存一个拷贝并且作为一个新的持久化对象返回。但是，给出的对象不会与session建立关联。 如果关联属性被映射为
	 * <code>cascade="merge"</code>,本方法将执行级联操作。 <br>
	 * JSR-220定义了本方法的语意.
	 * 
	 * @param entity
	 *            a detached instance with state to be copied
	 * @return an updated persistent instance
	 */
	public <T> T merge(T entity) {
		return (T) getHibernateTemplate().merge(entity);
	}

	/**
	 * 根据给定的实体类型和持久化标识符（假定它是存在的），返回持久化实例。 <br>
	 * 不要使用这个方法判断一个实例是否存在(此时应该使用<code>get()</code>)。只有当你确定这个实例是存在 的才可以使用这个方法。
	 * 
	 * @param entityClass
	 *            a persistent class
	 * @param id
	 *            a valid identifier of an existing persistent instance of the
	 *            class
	 * @return the persistent instance or proxy
	 * @throws HibernateException
	 */
	public <T> T load(Class<T> entityClass, Serializable id) {
		return (T) getHibernateTemplate().load(entityClass, id);
	}

	/**
	 * 根据给定实体类型，返回改类型实体的所有持久化实例。
	 * 
	 * @param entityClass
	 *            a persistent class
	 * @return
	 */
	public <T> List<T> get(Class<T> entityClass) {
		return getHibernateTemplate().loadAll(entityClass);
	}

	/**
	 * 根据指定的实体类型和持久化标识符，返回它的持久化实例,如果没有这样的持久化实例，则返回null。(如果实例或
	 * 实例的代理已经与session关联，则返回这个实例或代理。)
	 * 
	 * @param entityClass
	 *            a persistent class
	 * @param id
	 *            an identifier
	 * @return a persistent instance or null
	 */
	public <T> T get(Class<T> entityClass, Serializable id) {
		return (T) getHibernateTemplate().get(entityClass, id);
	}

	/**
	 * 将给定的实例从session缓存中清除。实例的的改变将不会与数据库同步。<br>
	 * 如果关联属性被映射为<code>cascade="evict"</code>,本方法将执行级联操作。
	 * 
	 * @param entity
	 *            a persistent instance
	 * @throws DataAccessException
	 *             ,HibernateException
	 */
	public void evict(Object entity) {
		getHibernateTemplate().evict(entity);
	}

	/**
	 * 完全清空session。清除所有加载的实例并且取消所有未决的保存、更新和删除。 <br>
	 * 不会关闭已打开的<code>ScrollableResults</code>的iterators或实例.
	 * 
	 */
	public void clear() {
		getHibernateTemplate().clear();
	}

	/**
	 * Force this session to flush. Must be called at the end of a unit of work,
	 * before commiting the transaction and closing the session (depending on
	 * {@link #setFlushMode flush-mode}, {@link Transaction#commit()} calls this
	 * method).
	 * <p/>
	 * <i>Flushing</i> is the process of synchronizing the underlying persistent
	 * store with persistable state held in memory.
	 * 强制session刷入。必须在一个工作单元的最后，提交时候和关闭session(根据flush-mode,
	 * {@link Transaction#commit()}会调用这个方法)之前调用。
	 * 
	 * @throws DataAccessException
	 *             ,HibernateException Indicates problems flushing the session
	 *             or talking to the database.
	 */
	public void flush() {
		getHibernateTemplate().flush();
	}

	/**
	 * Execute an HQL query, binding a number of values to "?" parameters in the
	 * query string.
	 * 
	 * @param hql
	 *            a query expressed in Hibernate's query language
	 * @param values
	 *            the values of the parameters
	 * @return a {@link List} containing the results of the query execution
	 */
	public List query(String hql, Object... values) {
		if (ArrayUtils.isEmpty(values)) {
			return getHibernateTemplate().find(hql);
		} else {
			return getHibernateTemplate().find(hql, values);
		}
	}

	/**
	 * 绑定参数，并执行HQL查询，返回一个单个的持久化实例。如果根据参数查询得到了多个结果， 则返回{@link List}
	 * 中的第一个元素，如果没有得到任何结果，返回null。
	 * 
	 * @param hql
	 *            a query expressed in Hibernate's query language
	 * @param values
	 *            the values of the parameters
	 * @return the first instance of the results or null.
	 */
	public Object findObject(String hql, Object... values) {
		List list = query(hql, values);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 根据给定的实例和实例对象的属性名称，判断这些属性的值是否和数据库中的重复。如果给定的实例
	 * 的持久化标识符(identifer)表示改对象已经存在(数据库主键与实例的标识符相同)，则排除对象 自身。
	 * 
	 * @param entity
	 *            the given instance.
	 * @param names
	 *            a number of property names.
	 * @return 根据给出的实例和属性名称，如果存在重复，则返回true,否则返回false.
	 */
	public boolean exists(Object entity, String... names) {
		Assert.notEmpty(names);
		Assert.notNull(entity);

		Class entityClass = ClassUtils.getUserClass(entity.getClass());
		Criteria criteria = getSession().createCriteria(entityClass)
				.setProjection(Projections.rowCount());

		try {
			// 循环加入
			for (String name : names) {
				Object property = PropertyUtils.getProperty(entity, name);
				if (property != null) {
					criteria.add(Restrictions.eq(name, property));
				} else {
					criteria.add(Restrictions.isNull(name));
				}
			}

			// 以下代码为了如果是update的情况,排除entity自身.
			// 通过hibernate的MetaData接口取得主键名
			String idPropertyName = getSessionFactory().getClassMetadata(
					entityClass).getIdentifierPropertyName();
			// String idPropertyName = "id";
			if (idPropertyName != null) {
				// 通过反射取得entity的主键值
				Object id = PropertyUtils.getProperty(entity, idPropertyName);
				// 如果id!=null,说明对象已存在,该操作为update,加入排除自身的判断
				if (id != null) {
					criteria.add(Restrictions.not(Restrictions.eq(
							idPropertyName, id)));
				}
			}

		} catch (Exception e) {
			logger.error("Error when reflection on entity, {}", e.getMessage());
			return false;
		}
		return (Integer) criteria.uniqueResult() > 0;
	}

	/**
	 * 根据给定的分页信息(<code>page</code>参数)、HQL、查询参数，执行分页查询。<code>page</code> 对象中的
	 * <code>autoCount</code>属性如果为true，则自动执行{@link #count(String, Object...)}查询。 <br>
	 * 通常的使用方式是这样的：<br>
	 * 
	 * <pre>
	 * pageQuery(new Page(0, 20, true), "from X x where x.id=?", id);
	 * pageQuery(new Page(0, 20), "from X x where x.id=? and x.name=?, id, name);
	 * </pre>
	 * 
	 * @param page
	 *            给出startIndex,pageSize,autoCount属性。
	 * @param hql
	 *            a query expressed in Hibernate's query language
	 * @param args
	 *            the values of the parameters
	 * @return 传入的page参数，它的data和rows属性是被重新赋值的（根据查询结果）。
	 * @see {@link Page#Page(int, int, boolean)}
	 * @see {@link Page#Page(int, int)}
	 */
	public Page pagedQuery(final Page page, String hql, Object... args) {
		Assert.hasText(hql);
		Assert.notNull(page);

		if (page.getAutoCount()) {
			page.setRows(count(hql, args));
			if (page.getRows() <= Page.NO_ROWS_FOUND) {
				return Page.EMPTY_PAGE;
			}
		}
		// 创建查询
		Query query = getSession().createQuery(hql);
		if (args != null) {
			for (int i = 0; i < args.length; i++) {
				query.setParameter(i, args[i]);
			}
		}

		List data = query.setFirstResult(page.getStartIndex())
				.setMaxResults(page.getPageSize()).list();
		page.setData(data);

		return page;
	}

    public <T> List<T> limitQurey(String hql,int size,Object... args) {
        Query query = getSession().createQuery(hql);
        if (args != null) {
            for (int i = 0; i < args.length; i++) {
                query.setParameter(i, args[i]);
            }
        }
        List data = query.setFirstResult(0)
                .setMaxResults(size).list();
        return data;
    }

	/**
	 * 根据给定的分页信息(<code>page</code>参数)和Hibernate criteria对象，执行分页查询。
	 * <code>page</code> 对象中的<code>autoCount</code>属性如果为true，则自动执行
	 * {@link #count(CriteriaImpl)}查询。
	 * 
	 * @param page
	 *            给出startIndex,pageSize,autoCount属性。
	 * @param criteria
	 *            the Hibernate criteria object, which can for example be held
	 *            in an instance variable of a DAO
	 *            传入的page参数，它的data和rows属性是被重新赋值的（根据查询结果）。
	 * @see {@link Page#Page(int, int, boolean)}
	 * @see {@link Page#Page(int, int)}
	 */
	public Page pagedQuery(final Page page, Criteria criteria) {
		if (criteria == null
				|| (!(criteria instanceof CriteriaImpl) && !(criteria instanceof CriteriaImpl.Subcriteria))) {
			throw new IllegalArgumentException(
					""
							+ "'pagedQuery(org.hibernate.criterion.Criteria, int, int)' error,"
							+ " Argument 'criteria' must not be null and it must "
							+ "be instance of CriteriaImpl");
		}
		// 当使用createAlias方法的时候，session将返回Subcriteria的实例，
		// 此时需要通过getParent方法找到CriteriaImpl的实例
		while (criteria instanceof CriteriaImpl.Subcriteria) {
			CriteriaImpl.Subcriteria sc = (Subcriteria) criteria;
			criteria = sc.getParent();
		}
		CriteriaImpl impl = (CriteriaImpl) criteria;

		return executePagedQuery(page, impl);
	}

	/**
	 * 根据给定的分页信息(<code>page</code>参数)和detached Hibernate criteria对象，执行分页查询。
	 * <code>page</code>对象中的<code>autoCount</code>属性如果为true，则自动执行
	 * {@link #count(CriteriaImpl)}查询。
	 * 
	 * @param page
	 *            给出startIndex,pageSize,autoCount属性。
	 * @param criteria
	 *            the detached Hibernate criteria object, which can for example
	 *            be held in an instance variable of a DAO
	 *            传入的page参数，它的data和rows属性是被重新赋值的（根据查询结果）。
	 * @see {@link Page#Page(int, int, boolean)}
	 * @see {@link Page#Page(int, int)}
	 */
	public Page pagedQuery(final Page page, DetachedCriteria criteria) {
		Criteria c = criteria.getExecutableCriteria(getSession());
		CriteriaImpl impl = (CriteriaImpl) c;

		return executePagedQuery(page, impl);
	}

	/**
	 * 执行Criteria分页查询.同时执行Count查询.
	 */
	private Page executePagedQuery(final Page page, CriteriaImpl criteria) {
		// 执行count查询
		if (page.getAutoCount()) {
			page.setRows(count(criteria));
			if (page.getRows() == Page.NO_ROWS_FOUND) {
				return Page.EMPTY_PAGE;
			}
		}

		List data = criteria.setFirstResult(page.getStartIndex())
				.setMaxResults(page.getPageSize()).list();
		page.setData(data);

		return page;
	}

	/**
	 * 根据给出的原始HQL和查询参数，执行count查询，用于分页查询计算总记录数。由于count可能对性能有影响，
	 * 所以子类可以根数据量、索引的实际情况覆盖本方法，以提供更高性能的count查询。 <br>
	 * <h3>关于缺省实现：</h3><br>
	 * 缺省的实现是将原始HQL中的order、fetch等子句删除，并将select子句替换为select count(*),
	 * 然后执行查询。当原始HQL中有Group BY的时候，总记录数就是查询结果的总数量；否则总记录数就是查询结果本身。
	 * 
	 * @param originalHql
	 *            原始的HQL
	 * @param args
	 *            the values of the parameters
	 * @return 总记录数，如果没有符合条件的记录，返回0.
	 */
	protected int count(String originalHql, Object... args) {
		String countQueryString = " select count (*) "
				+ removeSelect(removeOrders(removeFetchs(originalHql)));
		List countList = getHibernateTemplate().find(countQueryString, args);

		int rows = 0;
		if (countList.size() > 1) {
			rows = countList.size(); // Group By的情景...
		} else if (countList.size() == 1) {
			rows = ((Long) countList.get(0)).intValue();
		}

		return rows;
	}

	/**
	 * 根据给出的hibernate Critera的实现，执行分页查询，用于分页查询计算总记录数。
	 * 由于count可能对性能有影响，所以子类可以根数据量、索引的实际情况覆盖本方法，以提供更高性能的count查询。 <br>
	 * <h3>关于缺省实现：</h3><br>
	 * 缺省实现是将criteria中projection和OrderBy条件取出，然后执行count查询。
	 * 完成后再将projection和OrderBy条件 重新设回去。
	 * 
	 * @param originalHql
	 *            原始的HQL
	 * @param args
	 *            the values of the parameters
	 * @return 总记录数，如果没有符合条件的记录，返回0.
	 */
	protected int count(CriteriaImpl criteria) {
		// 先把Projection和OrderBy条件取出来,清空两者来执行Count操作
		Projection projection = criteria.getProjection();
		List<CriteriaImpl.OrderEntry> orderEntries;
		try {
			orderEntries = (List) ReflectUtil.getPrivateProperty(criteria,
					"orderEntries");
			ReflectUtil.setPrivateProperty(criteria, "orderEntries",
					new ArrayList());
		} catch (Exception e) {
			throw new InternalError(" Runtime Exception impossibility throw ");
		}

		// 执行查询
		int rows = (Integer) criteria.setProjection(Projections.rowCount())
				.uniqueResult();

		// 将之前的Projection和OrderBy条件重新设回去
		criteria.setProjection(projection);
		if (projection == null) {
			criteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		}
		try {
			ReflectUtil.setPrivateProperty(criteria, "orderEntries",
					orderEntries);
		} catch (Exception e) {
			throw new InternalError(" Runtime Exception impossibility throw ");
		}

		return rows;
	}

	/**
	 * 去除hql的select 子句，未考虑union的情况
	 */
	private static String removeSelect(String hql) {
		Assert.hasText(hql);
		int beginPos = hql.toLowerCase().indexOf("from");
		Assert.isTrue(beginPos != -1, " hql : " + hql
				+ " must has a keyword 'from'");
		return hql.substring(beginPos);
	}

	/**HibernateTemplate
	 * 去除hql的orderby 子句
	 */
	private static String removeOrders(String hql) {
		Assert.hasText(hql);
		Pattern p = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*",
				Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(hql);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(sb, "");
		}
		m.appendTail(sb);
		return sb.toString();
	}

	/**
	 * 删除HSQL中的fetch
	 */
	private static String removeFetchs(String hql) {
		Assert.hasText(hql);
		return hql.replaceAll("fetch ", "");
	}

    /**
     * 原生sql查询
     * @param page 分页
     * @param sql sql语句
     * @param entity 映射的实体
     * @param args 参数
     * @return
     */
    public Page sqlPageQuery(final Page page,final String sql, final Class entity,final Object... args) {
        List list = getHibernateTemplate().executeFind(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                SQLQuery query = session.createSQLQuery(sql);
                for (int i = 0; i < args.length; i++) {
                    Object arg = args[i];
                    query.setParameter(i, arg);//
                }
                query.addEntity(entity);
                if (page.getAutoCount()) {
                    int size = query.list().size();
                    page.setRows(size);
                    if (page.getRows() <= Page.NO_ROWS_FOUND) {
                        return Page.EMPTY_PAGE;
                    }
                }
                return query.setFirstResult(page.getStartIndex())
                        .setMaxResults(page.getPageSize())
                        .list();
            }
        });
        page.setData(list);
        return page;
    }

    /**
     * 计数
     * @param sql
     * @param entity
     * @param args
     * @return
     */
    protected int sqlCount(final String sql, final Class entity,final Object... args){
        Object o = getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                SQLQuery query = session.createSQLQuery(sql);
                for (int i = 0; i < args.length; i++) {
                    Object arg = args[i];
                    query.setParameter(i, arg);
                }
                query.addEntity(entity);
                return query.list().size();
            }
        });
        return (Integer)o;
    }
}
