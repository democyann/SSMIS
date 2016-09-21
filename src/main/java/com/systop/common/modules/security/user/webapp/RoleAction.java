package com.systop.common.modules.security.user.webapp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.json.annotations.JSON;
import org.hibernate.criterion.MatchMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;
import com.systop.common.modules.security.user.model.Role;
import com.systop.common.modules.security.user.model.User;
import com.systop.common.modules.security.user.service.RoleManager;
import com.systop.common.modules.security.user.service.UserManager;
import com.systop.core.ApplicationException;
import com.systop.core.dao.support.Page;
import com.systop.core.util.ReflectUtil;
import com.systop.core.webapp.struts2.action.ExtJsCrudAction;

/**
 * 角色Action
 * 
 * @author Sam Lee
 * 
 */
@SuppressWarnings({ "serial", "rawtypes", "unchecked" })
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RoleAction extends ExtJsCrudAction<Role, RoleManager> {
	/**
	 * 为用户添加的角色暂存在Session中名字
	 */
	private static final String TEMPLATE_ADDED_ROLES = "roles_added_session";

	/**
	 * 为用户删除的角色暂存在Session中名字
	 */
	private static final String TEMPLATE_REMOVED_ROLES = "roles_removed_session";

	private User user;

	private UserManager userManager;
	/**
	 * 用于对应页面上CheckBox，List等组件的选择值.
	 */
	protected Serializable[] selectedItems;

	// Action methods

	/**
	 * 按名称和描述执行的角色查询
	 */
	@Override
	public String index() {
		StringBuffer sql = new StringBuffer("from Role r where 1=1 ");
		List<Object> args = new ArrayList<Object>();
		
		if (StringUtils.isNotBlank(getModel().getName())  && StringUtils.isNotBlank(getModel().getDescn())) {
			sql.append("and r.name like ? and r.descn like ?");
			args.add(MatchMode.ANYWHERE.toMatchString(getModel().getName()));
			args.add(MatchMode.ANYWHERE.toMatchString(getModel().getDescn()) );
		}
		
		items = getManager().query(sql.toString(), args.toArray());
		return INDEX;
	}

	/**
	 * Add validators for role saving.
	 */
	@Validations(requiredStrings = {
			@RequiredStringValidator(fieldName = "model.name", message = "角色名称是必须的."),
			@RequiredStringValidator(fieldName = "model.descn", message = "角色描述是必须的.") })
	@Override
	public String save() {
		if (StringUtils.isNotBlank(getModel().getName())) {
			getModel().setName("ROLE_"+getModel().getName().toUpperCase());
		}
		return super.save();
	}

	/**
	 * 删除
	 */
	public String remove(){
		
		String ids = getRequest().getParameter("ids");
		String[] id = ids.split(",");
		for (String id1 : id) {
			Role r = getManager().get(id1);
			if (r.getUsers().size() >0) {
				addActionError(r.getDescn()+"已经被其他用户使用，不能删除！");
				return this.index();
			} else {
				getManager().remove(r);
			}
		}
		return "success";
	}
	// Following methods are used for assigning roles to user.
	/**
	 * 列出所有角色,同时，根据当前{@link #user}的角色，将其中已经分配给{@link #user}
	 * 的角色的changed属性设置为true.
	 * 
	 */
	public String rolesOfUser() {
		//StringBuffer sql = new StringBuffer("from Role r where 1=1 ");
		// 分公司 update for 2013-01-06; by: wf
		
		
		initUser();
		// 得到该用户的角色
		Set<Role> rolesOfUser = user.getRoles();
		logger.debug("User {} has {} roles.", user.getId(), rolesOfUser.size());
		page = new Page(Page.start(getPageNo(), getPageSize()), getPageSize());
		if (getModel() == null || StringUtils.isBlank(getModel().getName())) {
		//page = getManager().pageQuery(page, getOrderHQL(sql.toString()));
			page = getManager().pageQuery(page, getOrderHQL("from Role r where r.name not in ('ROLE_ADMIN')" ));	
		} else {
			page = getManager().pageQuery(page, "from Role r where r.name not in ('ROLE_ADMIN') and r.name like ?" ,MatchMode.ANYWHERE.toMatchString(getModel().getName()));
		/*	sql.append(" and r.name like ? ");
			page = getManager().pageQuery(page,
					getOrderHQL(sql.toString()),"%" + getModel().getName() + "%");*/
		}
		// 得到所有角色
		List allRoles = page.getData();
		// 从session中取得暂存的待分配的Roles
		Set templateRolesAdded = getTemporaryRoles(user, TEMPLATE_ADDED_ROLES);

		// 从session中取得暂存的待反分配的Roles
		Set templateRolesRemoved = getTemporaryRoles(user,
				TEMPLATE_REMOVED_ROLES);
		List mapRoles = new ArrayList(allRoles.size());
		for (Iterator itr = allRoles.iterator(); itr.hasNext();) {
			Role role = (Role) itr.next();
			role.setChanged(false);
			if (rolesOfUser.contains(role)) { // 如果角色已经分配给用户，则选中
				role.setChanged(true);
			}
			// 如果角色暂时分配了，则选中
			if (templateRolesAdded != null
					&& templateRolesAdded.contains(role.getId())) {
				role.setChanged(true);
			}
			// 如果角色暂时删除了，则不选中
			if (templateRolesRemoved != null
					&& templateRolesRemoved.contains(role.getId())) {
				role.setChanged(false);
			}
			// 转换为Map，防止延迟加载
			Map mapRole = ReflectUtil.toMap(role, new String[] { "id", "name",
					"descn" ,"version"}, true);
			mapRole.put("changed", role.getChanged());
			mapRoles.add(mapRole);
		}
		page.setData(mapRoles);
		return JSON;
	}

	/**
	 * 取消保存用户的角色信息
	 * 
	 * @param userId
	 *            用户Id
	 */
	public String cancelSaveUserRoles() {
		initUser();
		clearSession(user);
		return JSON;
	}

	/**
	 * 客户端通过checkbox选择一个角色，通知到服务器端，将这个role存入session.
	 */
	public String selectRole() {
		if (getModel() == null || getModel().getId() == null || user == null
				|| user.getId() == null) {
			throw new ApplicationException("Please select a role at least.");
		}

		selectRole(getModel().getId(), user.getId(), true);
		return JSON;
	}

	/**
	 * 客户端通过checkbox反选择一个角色，通知到服务器端，将这个role从session中去除.
	 */
	public String deselectRole() {
		if (getModel() == null || getModel().getId() == null || user == null
				|| user.getId() == null) {
			throw new ApplicationException("Please select a role at least.");
		}

		selectRole(getModel().getId(), user.getId(), false);
		return JSON;
	}

	/**
	 * 为指定的用户分配角色
	 * 
	 * @param userId
	 *            Id of the user to be assigned.
	 * @return true if successed ,otherwise, false.
	 */
	public String saveUserRoles() {
		initUser();
		// 临时删除（反分配）的角色
		try{
		Set<String> roles = getTemporaryRoles(user, TEMPLATE_REMOVED_ROLES);
		/*for (String roleId : roles) {
			Role role = getManager().get(roleId);
			if (role != null) {
				role.getUsers().remove(user);
				user.getRoles().remove(role);
			}
		}*/
		Iterator it=roles.iterator();
		while(it.hasNext()){
			Role role = getManager().get(Integer.valueOf(it.next().toString()));
			if (role != null) {
				role.getUsers().remove(user);
				user.getRoles().remove(role);
			}
		}
		
		// 临时添加（分配）的角色
		roles = getTemporaryRoles(user, TEMPLATE_ADDED_ROLES);
	/*	for (String roleId : roles) {
			Role role = getManager().get(roleId);
			if (role != null) {
				role.getUsers().add(user);
				user.getRoles().add(role);
			}
		}*/
		Iterator it1=roles.iterator();
		while(it1.hasNext()){
			Role role = getManager().get(Integer.valueOf(it1.next().toString()));
			if (role != null) {
				role.getUsers().add(user);
				user.getRoles().add(role);
			}
		}

		userManager.save(user);
		clearSession(user);
		}catch(Exception e){
			e.printStackTrace();
		}
		return JSON;
	}

	// private methods

	/**
	 * Build a hql with order clause
	 */
	private String getOrderHQL(String initHQL) {
		if (StringUtils.isBlank(getSortProperty())) {
			return initHQL;
		}
		return new StringBuffer(100).append(initHQL).append(" order by r.")
				.append(getSortProperty()).append(" ").append(getSortDir())
				.toString();
	}

	/**
	 * 页面通过AJAX方式把为用户分配的角色Id和用户Id传入服务器端。<tt>addRole</tt>
	 * 方法将角色Id保存在session中的一个Map对象中，该对象以User实例和RoleId为key-value.
	 * 
	 * @param roleId
	 *            角色id
	 * @param userId
	 *            用户id
	 * @param selected
	 *            是删除（false）还是添加（true）
	 */
	private void selectRole(Integer roleId, Integer userId, boolean selected) {
		if (roleId==null|| null ==  userId ) {
			return;
		}
		// 根据ID获得Role和User，其中User将作为“key”保存在Map对象中
		initUser();
		setModel(getManager().get(getModel().getId()));
		if (user == null || getModel() == null) {
			return;
		}
		if (selected) {
			temporaryAddRole(user, getModel());
		} else {
			temporaryRemoveRole(user, getModel());
		}
	}

	/**
	 * 清空session中有关用户的数据
	 * 
	 */
	private void clearSession(User user) {
		HttpSession session = getRequest().getSession();
		Map map = (Map) session.getAttribute(TEMPLATE_ADDED_ROLES);
		if (map.containsKey(user.getId())) {
			map.remove(user.getId());
		}
		map = (Map) session.getAttribute(TEMPLATE_REMOVED_ROLES);
		if (map.containsKey(user.getId())) {
			map.remove(user.getId());
		}
		logger.debug("Session of user {} cleared.", user.getLoginId());
	}

	/**
	 * Initialize the <code>user</code> property.
	 */
	private void initUser() {
		if (user == null || user.getId() == null) {
			throw new ApplicationException("Please select user to assign to");
		}

		user = userManager.get(user.getId());
		if (user == null) {
			throw new ApplicationException("Please select user to assign to");
		}
	}

	/**
	 * 根据用户的授权情况，在Session中暂存一个待分配的Role ID
	 * 
	 * @param user
	 *            给定用户
	 * @param role
	 *            即将分配给用户的Role
	 */
	private void temporaryAddRole(User user, Role role) {
		// 从Session中取得该用户临时分配的角色Id
		Set roleIdsAdded = getTemporaryRoles(user, TEMPLATE_ADDED_ROLES);

		// 如果新分配的Role还没有保存在数据库中，则暂存这个Role ID
		if (!user.getRoles().contains(role)) {
			roleIdsAdded.add(role.getId());
			logger.debug("Temporary add role {} for user {}", role.getId(),
					user.getId());
		}
		// 同步即将删除的角色
		Set roleIdsRemoved = getTemporaryRoles(user, TEMPLATE_REMOVED_ROLES);
		roleIdsRemoved.remove(role.getId());
	}

	/**
	 * 根据用户的授权情况，在Session中暂存一个反分配的Role ID
	 * 
	 * @param user
	 *            给定用户
	 * @param role
	 *            即将从用户的角色中删除Role
	 */
	private void temporaryRemoveRole(User user, Role role) {
		// 从session中取得临时反分配的Role ID
		Set roleIdsRemoved = getTemporaryRoles(user, TEMPLATE_REMOVED_ROLES);

		// 如果该角色已经分配给用户了，则暂存
		if (user.getRoles().contains(role)) {
			roleIdsRemoved.add(role.getId());
			logger.debug("Temporary remove role {} for user {}", role.getId(),
					user.getId());
		}
		// 同步即将添加的角色
		Set roleIdsAdded = getTemporaryRoles(user, TEMPLATE_ADDED_ROLES);
		roleIdsAdded.remove(role.getId());
	}

	/**
	 * 从Session中取得某个用户的临时Role，包括待分配和待反分配的。
	 * 
	 * @param user
	 *            指定的用户
	 * @param sessionName
	 *            Session名字
	 * 
	 * @return
	 */
	private Set<String> getTemporaryRoles(User user, String sessionName) {
		Map userRoles = (Map) getRequest().getSession().getAttribute(
				sessionName);
		if (userRoles == null) {
			userRoles = Collections.synchronizedMap(new HashMap());
			getRequest().getSession().setAttribute(sessionName, userRoles);
		}

		Set<String> roleIds = (Set) userRoles.get(user.getId());
		if (roleIds == null) {
			roleIds = Collections.synchronizedSet(new HashSet());
			userRoles.put(user.getId(), roleIds);
		}

		return roleIds;
	}

	// Getter & Setters
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Autowired
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	@JSON(deserialize = true)
	public Serializable[] getSelectedItems() {
		return selectedItems;
	}

	public void setSelectedItems(Serializable[] selectedItems) {
		this.selectedItems = selectedItems;
	}
}
