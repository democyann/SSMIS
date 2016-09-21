package com.systop.common.modules.security.user.webapp;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.ecside.table.limit.Limit;
import org.ecside.table.limit.Sort;
import org.ecside.util.RequestUtils;
import org.hibernate.criterion.MatchMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;

import com.systop.common.modules.dept.model.Dept;
import com.systop.common.modules.dept.service.DeptManager;
import com.systop.common.modules.dept.webapp.DeptAction;
import com.systop.common.modules.security.user.UserConstants;
import com.systop.common.modules.security.user.UserUtil;
import com.systop.common.modules.security.user.model.Role;
import com.systop.common.modules.security.user.model.User;
import com.systop.common.modules.security.user.service.RoleManager;
import com.systop.common.modules.security.user.service.UserManager;
import com.systop.core.ApplicationException;
import com.systop.core.Constants;
import com.systop.core.dao.support.Page;
import com.systop.core.util.DateUtil;
import com.systop.core.util.PageUtil;
import com.systop.core.util.ReflectUtil;
import com.systop.core.util.RequestUtil;
import com.systop.core.webapp.struts2.action.ExtJsCrudAction;

/**
 * 用户管理
 * 
 * @author Sam Lee, Nice
 */
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@SuppressWarnings({ "rawtypes", "serial" })
public class UserAction extends ExtJsCrudAction<User, UserManager> {

	// 检测用户返回的结果
	private Map<String, Object> checkResult;

	// 保存用户树形列表
	private List<Map> userTree;

	// 部门ID
	private Integer deptId;
	private Integer deptIdTwo;

	// 角色名称，用于员工选择器
	private String roleName;

	// 是否由自己编辑
	private String editSelf;

	private List<User> auditUser;

	private Date beginDate;

	private Date endDate;
	private boolean notifiedMessage;
	private boolean _notifiedSms;
	private long jinggao = 3;

	private String only;

	
	@Autowired
	private DeptManager deptManager;

	@Autowired
	private RoleManager roleManager;

	@Autowired
	private DeptAction deptAction;
	private Map<String, Object> result;

	private Map<String, Object> buildQuery(User user) {
		user = (user == null) ? new User() : user;
		loadModel();
		// 存放查询参数
		List<Object> args = new ArrayList<Object>();
		StringBuffer hql = new StringBuffer(
				"from User u where  u.name != 'Administrator' and u.status = ? ");
		args.add(getModel().getStatus());
		/**
		 * 按公司查询
		 * */
		User user1 = getUser(getLoginUser());
		if (user1.getDept() != null && user1.getDept().getSubCompany() !=null) {
			if (user1.getDept().getSubCompany().getDeptSort().equals("1")) {
				hql.append(" and u.dept.subCompany.id = ?");
				args.add(user1.getDept().getSubCompany().getId());
			}
		}

		if (StringUtils.isNotBlank(getModel().getName())) {// 根据名称查询
			hql.append(" and u.name like ?");
			args.add(MatchMode.ANYWHERE.toMatchString(getModel().getName()));
		}
		if (StringUtils.isNotBlank(getModel().getLoginId())) {// 根据LoginId查询
			hql.append(" and u.loginId like ?");
			args.add(MatchMode.ANYWHERE.toMatchString(getModel().getLoginId()));
		}
		if (deptId != null && deptId != 0) {// 根据部门查询
			hql.append(" and (u.dept.id = ? or u.dept.parentDept.id = ? or u.partTimeJobDept.id = ?)");
			args.add(deptId);
			args.add(deptId);
			args.add(deptId);
		}

		if (StringUtils.isNotBlank(getModel().getOl())) {
			hql.append(" and u.ol = ?");
			args.add(getModel().getOl());
		}
		Limit limit = RequestUtils.getLimit(getRequest());
		Sort sort = limit.getSort();
		if (StringUtils.isNotBlank(sort.getProperty())) {
			jinggao = 3;
			if (sort.getProperty().equals("position.name")) {
				hql.append(" order by u.position.dept.id" + " "
						+ sort.getSortOrder());
			} else {
				hql.append(" order by " + sort.getProperty() + " "
						+ sort.getSortOrder());
			}

		}
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("hql", hql.toString());
		queryMap.put("args", args);
		return queryMap;
	}

	/**
	 * 得到部门下的所有人员
	 */
	@SuppressWarnings("unchecked")
	public String queryUserByDept() {
		StringBuffer hql = new StringBuffer("from User u where u.dept.id = ? or u.partTimeJobDept.id = ?");
		List<Object> args = new ArrayList<Object>();
		args.add(deptId);
		args.add(deptId);
		if (getModel() != null && StringUtils.isNotBlank(getModel().getName())) {
			hql.append(" and u.name like ?");
			args.add(MatchMode.ANYWHERE.toMatchString(getModel().getName()));
		}
		page = new Page(Page.start(getPageNo(), getPageSize()), getPageSize());
		page = getManager().pageQuery(page, hql.toString(), args.toArray());

		List<User> allUsers = page.getData();

		List mapAssignees = new ArrayList(allUsers.size());
		for (Iterator itr = allUsers.iterator(); itr.hasNext();) {
			User user = (User) itr.next();
			user.setChanged(false);
			// 如果已经分配了此审批人，将审批人勾选
			if (allUsers.contains(user)) {
				user.setChanged(true);
			}
			// 转换为Map，防止延迟加载
			Map mapAssignee = ReflectUtil.toMap(user, new String[] { "id",
					"name" }, true);
			

			if (user.getDept() != null) {
				mapAssignee.put("dept", user.getDept().getName());
			}

			mapAssignee.put("changed", user.getChanged());
			mapAssignees.add(mapAssignee);
		}
		page.setData(mapAssignees);

		return "queryUserByDept";
	}

	/**
	 * 得到公司的所有人员
	 * 
	 * @param t
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String queryUserBySub() {
		StringBuffer hql = new StringBuffer(
				"from User u where u.dept.subCompany.id = ? and u.id != 1");
		List<Object> args = new ArrayList<Object>();
		args.add(deptId);
		if (getModel() != null && StringUtils.isNotBlank(getModel().getName())) {
			hql.append(" and u.name like ?");
			args.add(MatchMode.ANYWHERE.toMatchString(getModel().getName()));
		}
		page = new Page(Page.start(getPageNo(), getPageSize()), getPageSize());
		page = getManager().pageQuery(page, hql.toString(), args.toArray());

		List<User> allUsers = page.getData();

		List mapAssignees = new ArrayList(allUsers.size());
		for (Iterator itr = allUsers.iterator(); itr.hasNext();) {
			User user = (User) itr.next();
			user.setChanged(false);
			// 如果已经分配了此审批人，将审批人勾选
			if (allUsers.contains(user)) {
				user.setChanged(true);
			}
			// 转换为Map，防止延迟加载
			Map mapAssignee = ReflectUtil.toMap(user, new String[] { "id",
					"name" }, true);
			

			if (user.getDept() != null) {
				mapAssignee.put("dept", user.getDept().getName());
			}

			mapAssignee.put("changed", user.getChanged());
			mapAssignees.add(mapAssignee);
		}
		page.setData(mapAssignees);

		return "queryUserBySub";
	}

	public Dept getCSDept(Dept t) {
		if (t.getParentDept() != null
				&& t.getParentDept().getParentDept() == null) {
			return t;
		} else if (t.getParentDept() == null) {
			return t;
		} else {
			return getCSDept(t.getParentDept());
		}

	}

	/**
	 * 删除用户
	 */
	public String delectUser() {
		if (ArrayUtils.isEmpty(selectedItems)) {
			if (getModel() != null) {
				Serializable id = extractId(getModel());
				if (id != null) {
					selectedItems = new Serializable[] { id };
				}
			}
		}
		if (selectedItems != null) {
			for (Serializable id : selectedItems) {
				if (id != null) {
					User user = getManager().get(convertId(id));
					if (user != null) {
						if (!user.getHasRoles()) {
							this.getManager().getDao().getHibernateTemplate().clear();
							getManager().delete(user);
							jinggao = 1;
						} else {
							jinggao = 0;
						}
					} else {
						logger.debug("用户信息不存在.");
					}
				}
			}
		}
		return "delete";
	}

	/**
	 * 初始化用户密码
	 */
	public String rePass() {
		if (StringUtils.isEmpty(only)) {
			if (!ArrayUtils.isEmpty(selectedItems)) {
				for (Serializable id : selectedItems) {
					if (id != null) {
						User user = getManager().get(convertId(id));
						getManager().initPassword(user);
						jinggao = 1;
					}
				}
			} else {
				jinggao = 0;
			}
		} else {
			if (getModel() != null
					&& StringUtils.isNotBlank(getModel().getId().toString())) {
				getManager().initPassword(getModel());
				jinggao = 1;
			} else {
				jinggao = 0;
			}
		}
		return "initpass";
	}

	/**
	 * 公共通讯录用，查询全部集团内的用户
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String show() {
		List<Object> args = new ArrayList<Object>();
		StringBuffer hql = new StringBuffer(
				"from User u where u.isSys = ? and u.status = ?");
		args.add(Constants.NO);
		args.add(UserConstants.USER_STATUS_USABLE);
		if (deptId != null && deptId != 0) {// 根据部门查询
			hql.append(" and (u.dept.id = ? or u.dept.parentDept.id = ?)");
			args.add(deptId);
			args.add(deptId);
		}
		if (StringUtils.isNotBlank(getModel().getLoginId())) {
			hql.append(" and u.loginId like ?");
			args.add(MatchMode.ANYWHERE.toMatchString(getModel().getLoginId()));
		}

		if (StringUtils.isNotBlank(getModel().getName())) {
			hql.append(" and u.name like ?");
			args.add(MatchMode.ANYWHERE.toMatchString(getModel().getName()));
		}
		hql.append(" order by u.name");

		page = PageUtil.getPage(getPageNo(), getPageSize());
		page = getManager().pageQuery(page, hql.toString(), args.toArray());
		items = page.getData();

		return "showUser";
	}

	/**
	 * 用户列表，组装查询条件
	 */
	@SuppressWarnings("unchecked")
	public String index() {
		Map<String, Object> queryMap = buildQuery(getModel());
		String hql = queryMap.get("hql").toString();
		List<Object> args = (List<Object>) queryMap.get("args");
		page = PageUtil.getPage(getPageNo(), getPageSize());
		page = getManager().pageQuery(page, hql.toString(), args.toArray());
		restorePageData(page);
		return INDEX;
	}

	/**
	 * 审批优先级设置查询
	 */
	@SuppressWarnings("unchecked")
	public String indexLevel() {
		StringBuffer hql = new StringBuffer("from User u where u.dept.id = 1 ");
		List<Object> args = new ArrayList<Object>();
		hql.append("order by u.orderId  ");
		page = PageUtil.getPage(getPageNo(), getPageSize());
		page = getManager().pageQuery(page, hql.toString(), args.toArray());
		items = page.getData();
		return "indexLevel";
	}

	/**
	 * 审批优先级设置(向上移动)
	 * 
	 * @return
	 */
	public String upOrder() {
		Integer id = getModel().getId();
		User user1 = getManager().get(id);// 拿到需要向上排序的用户对象
		User user2 = getManager().findObject(
				"from User u where u.orderId = ? ", user1.getOrderId() - 1);// 拿到它上面的用户对象
		user2.setOrderId(user2.getOrderId() + 1);// 把上面向下移动一位
		getManager().save(user2);

		user1.setOrderId(user1.getOrderId() - 1);// 把它向上移动一位
		getManager().save(user1);

		return "toindexLevel";
	}

	/**
	 * 审批优先级设置(向下移动)
	 * 
	 * @return
	 */
	public String downOrder() {
		Integer id = getModel().getId();
		User user1 = getManager().get(id);// 拿到需要向下排序的用户对象
		User user2 = getManager().findObject(
				"from User u where u.orderId = ? ", user1.getOrderId() + 1);// 拿到它下面的用户对象
		user2.setOrderId(user2.getOrderId() - 1);// 把下面向上移动一位
		getManager().save(user2);

		user1.setOrderId(user1.getOrderId() + 1);// 把它向下移动一位
		getManager().save(user1);
		return "toindexLevel";
	}

	/**
	 * 用户列表，组装查询条件
	 */
	@SuppressWarnings("unchecked")
	public String olIndex() {
		getModel().setOl("1");
		getModel().setStatus("1");
		Map<String, Object> queryMap = buildQuery(getModel());
		String hql = queryMap.get("hql").toString();
		List<Object> args = (List<Object>) queryMap.get("args");
		page = PageUtil.getPage(getPageNo(), getPageSize());
		page = getManager().pageQuery(page, hql.toString(), args.toArray());
		restorePageData(page);
		return "olIndex";
	}

	// 对model进行 判断，进行非空处理
	private void loadModel() {
		if (getModel() == null) {
			setModel(new User());
		}
		if (StringUtils.isBlank(getModel().getStatus())) {// 默认查询正常用户[已启用]
			getModel().setStatus(UserConstants.USER_STATUS_USABLE);
		}
	}

	@Override
	public String save() {
		try {
			Assert.notNull(getModel());
			if (deptId == null || deptId == 0) {
				addActionMessage("请选择所属部门");
				setModel(getManager().get(getModel().getId()));
				return INPUT;
			}
			if (editSelf != null && editSelf.equals("1")) {
				getModel().setNotified(Boolean.toString(isNotifiedMessage()));
				getModel().setNotifiedSms(Boolean.toString(is_notifiedSms()));
			}
			getModel().setOl("0");
		/*	getModel().setPartTimeJobDept(deptManager().get(Dept.class, deptIdTwo));*/
			getManager().getDao().getHibernateTemplate().clear();

			// **如果添加的是厅领导的话需设置审批排序编号*//*
			/*
			 * if(deptId==1){ int orderId = 0;//审批排序号 User userMaxOrd =
			 * getManager().findObject(
			 * "from User u where u.orderId =(select  Max(p.orderId) from User p )"
			 * ); orderId = userMaxOrd.getOrderId()+1;
			 * getModel().setOrderId(orderId); }
			 */
			getManager().getDao().getSessionFactory().getCurrentSession()
					.clear();
			getManager().save(getModel(), deptId,deptIdTwo);
			if (editSelf != null && Constants.YES.equals(editSelf)) {
				setModel(getManager().get(getModel().getId()));
				addActionMessage("个人信息修改成功");
				return INPUT;
			}
		} catch (ApplicationException e) {
			addActionError(e.getMessage());
			return INPUT;
		}
		String referrer = getRequest().getHeader("referer");
		if (referrer.contains("from=hr"))
			return "success2";
		else
			return SUCCESS;
	}

	/**
	 * 分公司 用户保存
	 * 
	 * @return
	 */
	public String saveSubUser() {
		try {
			Assert.notNull(getModel());
			if (deptId == null || deptId == 0) {
				addActionMessage("请选择所属部门");
				return "subInput";
			}
			if (editSelf != null && editSelf.equals("1")) {
				getModel().setNotified(Boolean.toString(isNotifiedMessage()));
				getModel().setNotifiedSms(Boolean.toString(is_notifiedSms()));
			}
			Role role = roleManager.getRoleByName("ROLE_ADMIN");
			role.getUsers().add(getModel());
			getModel().getRoles().add(role);
			getManager().getDao().getHibernateTemplate().clear();
			getManager().save(getModel(), deptId,null);
			if (editSelf != null && Constants.YES.equals(editSelf)) {
				addActionMessage("个人信息修改成功");
				return "subInput";
			}
		} catch (ApplicationException e) {
			addActionError(e.getMessage());
			return "subInput";
		}
		String referrer = getRequest().getHeader("referer");
		if (referrer.contains("from=hr"))
			return "success2";
		else
			return "subSuccess";
	}

	/**
	 * 分公司选择用户列表
	 * 
	 * @return
	 */
	public String subIndex() {
		// 存放查询参数
		List<Object> args = new ArrayList<Object>();
		StringBuffer hql = new StringBuffer(
				"select u from User u inner join u.roles r where r.name=? ");
		args.add("ROLE_ADMIN");
		// 分公司 update for 2013-01-06; by: wf

		if (StringUtils.isNotBlank(getModel().getName())) {// 根据名称查询
			hql.append(" and u.name like ?");
			args.add(MatchMode.ANYWHERE.toMatchString(getModel().getName()));
		}
		if (StringUtils.isNotBlank(getModel().getLoginId())) {// 根据LoginId查询
			hql.append(" and u.loginId like ?");
			args.add(MatchMode.ANYWHERE.toMatchString(getModel().getLoginId()));
		}
		hql.append(" order by u.dept.orderId");
		page = PageUtil.getPage(getPageNo(), getPageSize());
		page = getManager().pageQuery(page, hql.toString(), args.toArray());
		restorePageData(page);
		return "indexCompany";
	}

	public String saveLocation() {
		User user = getManager().get(getLoginUser().getId());
		getManager().save(user);
		getSession().setAttribute("positioned", true);
		return null;
	}

	/**
	 * 编辑用户
	 */
	@SkipValidation
	@Override
	public String editNew() {
		getModel().setSex(UserConstants.GENT);
		return INPUT;
	}

	/**
	 * 分公司编辑用户
	 */
	@SkipValidation
	public String subEditNew() {
		getModel().setSex(UserConstants.GENT);
		return "subInput";
	}

	/**
	 * 个信息维护
	 * 
	 * @return
	 */
	public String editSelf() {
		User user = getManager().get(getLoginUser().getId());
		setModel(user);
		if (user.getNotified() == null || user.getNotified().equals("false"))
			notifiedMessage = false;
		else
			notifiedMessage = true;

		if (user.getNotifiedSms() == null
				|| user.getNotifiedSms().equals("false"))
			_notifiedSms = false;
		else
			_notifiedSms = true;

		editSelf = Constants.YES;
		return INPUT;
	}

	/**
	 * 编辑用户，重新调用manager方法，目的是为了加在用户照片
	 */
	public String edit() {
		if (getModel() != null && getModel().getId() != null) {
			setModel(getManager().get(getModel().getId()));
		}
		return INPUT;
	}

	/**
	 * 删除用户照片
	 * 
	 * @return
	 */
	public String removePhoto() {
		getManager().removeUserPhoto(getModel(), getServletContext());
		if (Constants.YES.equals(editSelf)) {
			return "editSelfRemovePhoto";
		}
		return INPUT;
	}

	/**
	 * 修改用户密码
	 * 
	 * @return
	 */
	public String changePassword() {
		try {
			getManager().changePassword(getModel(),
					getRequest().getParameter("oldPassword"));

			return SUCCESS;
		} catch (ApplicationException e) {
			addActionError(e.getMessage());
			return INPUT;
		}
	}

	/**
	 * 启用用户
	 * 
	 * @return
	 */
	public String unsealUser() {
		if (ArrayUtils.isEmpty(selectedItems)) {
			if (getModel() != null) {
				Serializable id = extractId(getModel());
				if (id != null) {
					selectedItems = new Serializable[] { id };
				}
			}
		}
		if (selectedItems != null) {
			for (Serializable id : selectedItems) {
				if (id != null) {
					User user = getManager().get(convertId(id));
					if (user != null) {
						getManager().unsealUser(user);
						jinggao = 1;
					} else {
						logger.debug("用户信息不存在.");
						jinggao = 0;
					}
				}
			}
		}
		return "initpass";
	}

	/**
	 * 禁用用户
	 */
	public String remove() {
		if (ArrayUtils.isEmpty(selectedItems)) {
			if (getModel() != null) {
				Serializable id = extractId(getModel());
				if (id != null) {
					selectedItems = new Serializable[] { id };
				}
			}
		}
		if (selectedItems != null) {
			for (Serializable id : selectedItems) {
				if (id != null) {
					User user = getManager().get(convertId(id));
					if (user != null) {
						getManager().remove(user);
						jinggao = 1;
					} else {
						logger.debug("用户信息不存在.");
						jinggao = 0;
					}
				}
			}
		}
		return "initpass";
	}

	/**
	 * 用户树形列表
	 * 
	 * @return
	 */
	@SkipValidation
	public String userTree() {
		if (RequestUtil.isJsonRequest(getRequest())) {
			Dept parent = null;
			if (deptId != null) {
				parent = getManager().getDao().get(Dept.class, deptId);
			} else {
				parent = (Dept) getManager().getDao().findObject(
						"from Dept d where d.parentDept is null");
			}
			Map<String, Object> parentMap = null;
			if (parent != null) {
				parentMap = new HashMap<String, Object>();
				parentMap.put("id", parent.getId());
				parentMap.put("text", parent.getName());
				parentMap.put("type", parent.getType());
			}
			Map deptTree = deptAction.buildTreeByParent(parentMap, true,null);
			Map tree = getManager().getUserTree(deptTree, roleName);
			if (!tree.isEmpty()) {
				userTree = new ArrayList<Map>();
				userTree.add(tree);
			}
			return "tree";
		}
		return INDEX;
	}

	/**
	 * 用户登录历史记录查询
	 */
	public String userHistoryList() {
		// 存放查询参数
		List<Object> args = new ArrayList<Object>();
		StringBuffer hql = new StringBuffer(
				"from UserLoginHistory ulh where ulh.id is not null ");
		if (beginDate != null) {
			hql.append("and ulh.loginTime >= ? ");
			args.add(DateUtil.firstSecondOfDate(beginDate));
		}
		if (endDate != null) {
			hql.append("and ulh.loginTime <= ? ");
			args.add(DateUtil.lastSecondOfDate(endDate));
		}
		hql.append("order by ulh.loginTime desc");
		page = PageUtil.getPage(getPageNo(), getPageSize());
		page = getManager().pageQuery(page, hql.toString(), args.toArray());
		restorePageData(page);
		return "userHistoryList";
	}

	/**
	 * AJAX请求，检测登录名是否已存在
	 */
	public String checkName() {
		checkResult = new HashMap<String, Object>();
		checkResult.put("exist",
				getManager().getDao().exists(getModel(), "loginId"));
		return "jsonRst";
	}

	/**
	 * 重置密码
	 * 
	 * @return
	 */
	public String resetPassword() {
		boolean exists = getManager().getDao().exists(getModel(), "loginId",
				"mobile");
		if (exists) {
			// 没有设置手机,或者不正确
			if (!org.apache.commons.lang.xwork.StringUtils.isNumeric(getModel()
					.getMobile())) {
				try {
					getResponse().sendRedirect(
							getRequest().getContextPath()
									+ "/resetPassword.jsp?reset_error=2");
				} catch (IOException e) {
					e.printStackTrace();
				}
				return null;
			}

			String newPassword = RandomStringUtils.randomNumeric(6);
			getModel().setPassword(newPassword);
			/*messageManager.buildSms(getModel().getMobile(),
					"尊敬的用户您好，您使用了重置登录密码的功能，新密码是:" + newPassword + "。");
			// messageManager.buildSms2(getModel().getMobile(),"尊敬的用户"+user.getName()+"您好，您使用了重置登录密码的功能，新密码是:"+newPassword+"。");
*/
			try {
				getResponse().sendRedirect(
						getRequest().getContextPath()
								+ "/login.jsp?login_error=2");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				getResponse().sendRedirect(
						getRequest().getContextPath()
								+ "/resetPassword.jsp?reset_error=1");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 更新在线时间状态
	 * 
	 * @return
	 */
	public String updateStatus() {
		User user = UserUtil.getLoginUser(getRequest());
		String logout = getRequest().getParameter("logout");
		if (StringUtils.equals(logout, "true"))
			getManager().updateOnlineStatus(user, true);
		else {
			long l = new Date().getTime() - user.getLastLoginTime().getTime();
			if (l > 3 * 60 * 60 * 1000) {
				// 登录3小时之后自动登出
				result = new HashMap<String, Object>();
				result.put("logout", true);
				getManager().updateLoginTime(user);
			}
			getManager().updateOnlineStatus(user, false);
		}
		return "updateStatus";
	}

	// 具有审批权限的人。
	public String auditUser() {
		auditUser = new ArrayList<User>();
		auditUser = getManager().getAuthUserByDept(deptId);
		return "auditUser";
	}

	/**
	 * 如果收发岗人员为空，则返回具有审批权限的人。
	 * 
	 * @return
	 */
	public String tranUser() {
		auditUser = new ArrayList<User>();
		auditUser = getManager().getTranUserByDept(deptId);
		if (auditUser.size() == 0) {
			auditUser = getManager().getAuthUserByDept(deptId);
		}
		return "auditUser";
	}

	/**
	 * @return
	 * @Description:为用户设置审批优先级
	 */
	public String levelOfUser() {
		return "levelOfUser";
	}

	public List<Map> getUserTree() {
		return userTree;
	}

	public Integer getDeptId() {
		return deptId;
	}

	public void setDeptId(Integer deptId) {
		this.deptId = deptId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Map<String, Object> getCheckResult() {
		return checkResult;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getEditSelf() {
		return editSelf;
	}

	public void setEditSelf(String editSelf) {
		this.editSelf = editSelf;
	}

	public boolean isNotifiedMessage() {
		return notifiedMessage;
	}

	public void setNotifiedMessage(boolean notifiedMessage) {
		this.notifiedMessage = notifiedMessage;
	}

	public boolean is_notifiedSms() {
		return _notifiedSms;
	}

	public void set_notifiedSms(boolean _notifiedSms) {
		this._notifiedSms = _notifiedSms;
	}

	public Map<String, Object> getResult() {
		return result;
	}

	public void setResult(Map<String, Object> result) {
		this.result = result;
	}

	public List<User> getAuditUser() {
		return auditUser;
	}

	public void setAuditUser(List<User> auditUser) {
		this.auditUser = auditUser;
	}

	public User getUser(User user) {
		return getManager().get(user.getId());
	}

	public String getOnly() {
		return only;
	}

	public void setOnly(String only) {
		this.only = only;
	}

	public long getJinggao() {
		return jinggao;
	}

	public void setJinggao(long jinggao) {
		this.jinggao = jinggao;
	}

	public Integer getDeptIdTwo() {
		return deptIdTwo;
	}

	public void setDeptIdTwo(Integer deptIdTwo) {
		this.deptIdTwo = deptIdTwo;
	}

}
