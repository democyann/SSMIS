package com.systop.core.webapp.struts2.action;

import java.io.Serializable;
import java.util.Collection;

import org.apache.commons.lang.ArrayUtils;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.apache.struts2.json.annotations.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.validation.Validator;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.systop.core.ApplicationException;
import com.systop.core.dao.support.Page;
import com.systop.core.service.Manager;
import com.systop.core.util.GenericsUtil;
import com.systop.core.util.ReflectUtil;
import com.systop.core.webapp.struts2.validation.ActionErrors;

/**
 * 
 * @author Sam Lee
 * 
 * @param <T>
 *            Action所涉及的Model的类型.
 * @param <M>
 *            Action所使用的Manager的类型
 */
@SuppressWarnings({ "serial", "unchecked", "rawtypes" })
public abstract class AbstractCrudAction<T, M extends Manager> extends
		BaseAction implements Preparable, ModelDriven {
	/**
	 * 定义显示单个实体的页面
	 */
	public static final String VIEW = "view";

	/**
	 * 定义显示实体列表的页面
	 */
	public static final String INDEX = "index";

	/**
	 * Action所使用的Manager类
	 */
	private M manager;

	/**
	 * Action所管理的Entity
	 */
	private T model;

	/**
	 * Action所管理的实体的类型
	 */
	private Class entityClass;

	/**
	 * 用于保存查询结果。
	 */
	protected Collection<T> items;

	/**
	 * 用于对应页面上CheckBox，List等组件的选择值.
	 */
	protected Serializable[] selectedItems;

	/**
	 * 用于保存分页查询结果
	 */
	protected Page page;

	/**
	 * 创建一个新的实体，如果成功，返回index页面，如果失败，返回输入页面.
	 */
	public String create() {
		try {
			manager.create(model);
		} catch (ApplicationException e) {
			addActionError(e.getMessage());
			return INPUT;
		}
		return SUCCESS;
	}

	/**
	 * <B>创建或更新</B>一个实体，如果成功，返回index页面，如果失败，返回输入页面. <code>save()</code>相当于调用
	 * {@link create()}和{@link update()}， 为了简化jsp编码，可以将新建和编辑页面合二为一，此时，
	 * <code>save()</code>方法.
	 * 
	 */
	public String save() {
		Assert.notNull(model);
		return (extractId(model) == null) ? create() : update();
	}

	/**
	 * @see {@link #save()}
	 */
	public String update() {
		try {
			manager.update(model);
		} catch (ApplicationException e) {
			addActionError(e.getMessage());
			return INPUT;
		}
		return SUCCESS;
	}

	/**
	 * 列出实体
	 */
	@SkipValidation
	public String index() {
		page = pageQuery(); // 执行分页查询
		if (page != null) {
			items = page.getData();
			restorePageData(page);
		}
		return INDEX;
	}

	/**
	 * 根据<code>Id</code>查询单个<code>model</code>并定向到显示它的页面.
	 */
	@SkipValidation
	public String view() {
        if(redirectMobile())return "mobile-"+VIEW;
		return VIEW;
	}

	/**
	 * 重定向到编辑页面。为了简化页面编程，将新建和编辑和为一个页面。
	 */
	@SkipValidation
	public String edit() {
		return INPUT;
	}

	/**
	 * 重定向到新建页面。为了简化页面编程，将新建和编辑和为一个页面。
	 */
	@SkipValidation
	public String editNew() {
		return INPUT;
	}

	/**
	 * 如果<code>id != null</code>,则删除ID所代表的Entity， 否则，如果
	 * <code>selectedItems.length > 0</code>则删除 <code>selectedItems</code>
	 * 所代表的所有Entities.
	 * 
	 * @return
	 */
	@SkipValidation
	public String remove() {
		if (ArrayUtils.isEmpty(selectedItems)) {
			if (model != null) {
				Serializable id = extractId(model);
				if (id != null) {
					selectedItems = new Serializable[] { id };
				}
			}
		}
		if (selectedItems != null) {
			for (Serializable id : selectedItems) {
				if (id != null) {
					Object object = getManager().get(convertId(id));
					if (object != null) {
						getManager().remove(object);
					} else {
						logger.debug("试图删除null数据.");
					}
				}
			}

			logger.debug("{} items removed.", selectedItems.length);
		}

		return SUCCESS;
	}

	// Method from Preparable
	/**
	 * @see com.opensymphony.xwork2.Preparable#prepare()
	 */
	public void prepare() {
		if (model == null || extractId(model) == null) {
			model = (T) ReflectUtil.newInstance(getEntityClass());
			logger.debug("model or it's id is null, create new.");
		} else {
			model = (T) manager.get(extractId(model));
		}
	}

	/**
	 * 对编程式验证提供支持。很多时候，声明式验证不能满足需要，比如，涉及数据库
	 * 的验证，此时，编程式验证就派上了用场。为了避免验证逻辑和业务逻辑混合，我们
	 * 使用spring的Validator接口来分离这两类逻辑；子类可以将Validator接口的实现 类注入到Action中。
	 */
	@Override
	public void validate() {
		if (getValidator() != null) {
			ActionErrors errors = new ActionErrors();
			errors.setAction(this);
			errors.setObjectName(getEntityClass().getName());
			getValidator().validate(model, errors);
			logger.debug("Execute the 'validate' method...");
		}
	}

	/**
	 * 从{@link #model}中取得ID
	 * 
	 * @param model
	 *            给定Model
	 * @return 实体对象的ID值
	 */
	protected abstract Serializable extractId(T model);

	/**
	 * 将{@link #id}转换为实际的类型，子类必须根据持久化标识的类型实现，例如:<br>
	 * 
	 * <pre>
	 * protected Serializable convertId(Serializable id) {
	 * 	return (id == nul) ? null : Integer.valueOf(id.toString());
	 * }
	 * </pre>
	 */
	protected abstract Serializable convertId(Serializable id);

	// Protected methods
	@JSON(deserialize = true)
	protected Class getEntityClass() {
		if (entityClass == null) {
			entityClass = GenericsUtil.getGenericClass(getClass(), 0);
		}
		return entityClass;
	}

	/**
	 * 分页查询方法，如果需要分页查询，子类可以覆盖这个方法，实现其分页查询逻辑。 {@link #index()}方法会首先调用
	 * <code>pageQuery()</code>方法，并将 <code>pageQuery()</code>方法返值中的数据返回页面。
	 * 
	 * @see {@link index()}
	 */
	protected abstract Page pageQuery();

	/**
	 * 子类可以实现<code>restorePageData()</code>，以达到“将分页数据重新设置到页面”的目的。 这个方法通常与
	 * {@link #pageQuery()}一起使用。
	 * 
	 * @see {@link index()}
	 */
	protected abstract void restorePageData(Page page);

	/**
	 * 子类可以覆盖这个缺省实现，以提供基于Spring Validator的验证方式
	 */
	public Validator getValidator() {
		return null;
	}

	// Method from ModelDriven
	/**
	 * @see com.opensymphony.xwork2.ModelDriven#getModel()
	 */
	public T getModel() {
		if (model == null) {
			model = (T) ReflectUtil.newInstance(getEntityClass());
		}
		return model;
	}

	// Getters and setters.
	public void setModel(T model) {
		this.model = model;
	}

	@Autowired(required = true)
	public void setManager(M manager) {
		this.manager = manager;
	}

	@JSON(deserialize = true)
	protected M getManager() {
		return manager;
	}

	@JSON(deserialize = true)
	public Collection<T> getItems() {
		return items;
	}
	
	public void setItems(Collection<T> items) {
		this.items = items;
	}


	@JSON(deserialize = true)
	public Serializable[] getSelectedItems() {
		return selectedItems;
	}

	public void setSelectedItems(Serializable[] selectedItems) {
		this.selectedItems = selectedItems;
	}

	public Page getPage() {
		return page;
	}
}
