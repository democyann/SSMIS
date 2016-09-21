package com.systop.common.modules.security.user.webapp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.commons.collections.map.LinkedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.hibernate.criterion.MatchMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;
import com.systop.common.modules.security.user.UserConstants;
import com.systop.common.modules.security.user.UserUtil;
import com.systop.common.modules.security.user.dao.UserDao;
import com.systop.common.modules.security.user.model.Permission;
import com.systop.common.modules.security.user.model.Role;
import com.systop.common.modules.security.user.model.User;
import com.systop.common.modules.security.user.service.PermissionManager;
import com.systop.common.modules.security.user.service.RoleManager;
import com.systop.core.ApplicationException;
import com.systop.core.dao.support.Page;
import com.systop.core.util.ReflectUtil;
import com.systop.core.webapp.struts2.action.ExtJsCrudAction;

/**
 * 权限Action
 * 
 * @author Sam Lee
 */
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@SuppressWarnings({ "unchecked", "rawtypes", "serial" })
public class PermissionAction extends
		ExtJsCrudAction<Permission, PermissionManager> {
	/**
	 * 为用户添加的权限暂存在Session中
	 */
	private static final String TEMPLATE_ADDED_PERMISSIONS = "permissions_added_session";

	/**
	 * 为角色删除的权限暂存在Session中
	 */
	private static final String TEMPLATE_REMOVED_PERMISSIONS = "permissions_removed_session";
	/**
	 * The role service.
	 */
	private RoleManager roleManager;
	/**
	 * The role that will be assigned permissions for.
	 */
	private Role role = new Role();
	private Map<String, String> jsonMsg = new HashMap<String, String>();
	private List<Permission> parents = new LinkedList<Permission>();
	private List<Permission> children = new LinkedList<Permission>();
	/**
	 * tree
	 */
	private List<Map<String, Object>> root_permission = null;

	/**
	 * 按权限名称和角色查询
	 * 
	 * @see BaseModelAction#query()
	 */
	@Override
	public String index() {
		if (StringUtils.isBlank(getModel().getName())
				&& StringUtils.isBlank(getModel().getDescn())) {
			items = getManager().query("from Permission");
		} else {
			items = getManager().query(
					"from Permission p where p.name like ? and p.descn like ?",
					new Object[] {
							MatchMode.ANYWHERE.toMatchString(getModel()
									.getName()),
							MatchMode.ANYWHERE.toMatchString(getModel()
									.getDescn()) });
		}
		return INDEX;
	}

	/**
	 * 权限模块-编辑权限 上级权限
	 * 
	 * @return
	 */
	public Map<String, String> getPMap() {
		Map<String, String> pMap = new LinkedMap();
		pMap.put(null, "顶级权限");
		StringBuffer hql = new StringBuffer("from Permission p where 1=1");
		hql.append(" and p.parentPermission is null");
		parents = getManager().getDao().query(hql.toString());
		for (Permission p : parents) {
			pMap.put(p.getId().toString(), p.getDescn());
		}
		return pMap;
	}

	/**
	 * 返回权限操作（权限类型）列表
	 */
	public Map<String, String> getOperations() {
		return UserConstants.PERMISSION_OPERATIONS;
	}

	@Validations(requiredStrings = {
			/*@RequiredStringValidator(fieldName = "model.name", message = "权限名称是必须的."),*/
			@RequiredStringValidator(fieldName = "model.descn", message = "权限描述是必须的.") })
	@Override
	public String save() {
		/*if (!getModel().getName().startsWith("AUTH_")) {
			addActionError("权限名称必须以AUTH_开头.");
			return INPUT;
		}*/
		if (StringUtils.isBlank((getModel().getId()))) {
			getModel().setId(null);
		}
		if (StringUtils.isBlank(getModel().getParentPermission().getId())) {
			getModel().setParentPermission(null);
		}
		getManager().getDao().clear();
		return super.save();
	}

	// The following methods are used for assigning permissions for role.
	/**
	 * 返回所有权限，同时查询指定角色所具有的权限， 并在“所有”权限中标注该角色所拥有的权限的选择状态
	 * 
	 * @param roleId
	 *            指定角色id
	 * 
	 * @return
	 */
	public String permissionsOfRole() {
		initRole();
		// 得到该角色的权限
		Set<Permission> permissionsOfRole = role.getPermissions();
		logger.debug("Role {} has {} permissions.", role.getId(),
				permissionsOfRole.size());
		page = new Page(Page.start(getPageNo(), getPageSize()), getPageSize());
		if (StringUtils.isBlank(getModel().getName())) {
			page = getManager().pageQuery(page,
					getOrderHQL("from Permission p"));
		} else {
			page = getManager().pageQuery(page,
					getOrderHQL("from Permission p where p.name like ?"),
					"%" + getModel().getName() + "%");
		}
		// 得到所有权限
		List allPermissions = page.getData();
		// 从session中取得暂存的待分配的权限
		Set templatePermissionsAdded = getTemporaryPermissions(role,
				TEMPLATE_ADDED_PERMISSIONS);
		// 从session中取得暂存的待反分配的权限
		Set templatePermissionsRemoved = getTemporaryPermissions(role,
				TEMPLATE_REMOVED_PERMISSIONS);
		List mapPerms = new ArrayList(allPermissions.size());

		for (Iterator itr = allPermissions.iterator(); itr.hasNext();) {
			Permission permission = (Permission) itr.next();
			permission.setChanged(false);
			if (permissionsOfRole.contains(permission)) { // 如果权限已经分配给角色则选中
				permission.setChanged(true);
			}
			// 如果权限暂时分配了则选中
			if (templatePermissionsAdded != null
					&& templatePermissionsAdded.contains(permission.getId())) {
				permission.setChanged(true);
			}
			// 如果权限暂时删除了则不选
			if (templatePermissionsRemoved != null
					&& templatePermissionsRemoved.contains(permission.getId())) {
				permission.setChanged(false);
			}
			// 转换为Map，防止延迟加载
			Map mapPerm = ReflectUtil.toMap(permission, new String[] { "id",
					"name", "descn" }, true);
			mapPerm.put("changed", permission.getChanged());
			mapPerms.add(mapPerm);
		}
		page.setData(mapPerms);

		return JSON;
	}

	/**
	 * 取消保存角色的权限信息
	 */
	public String cancelSaveRolePermissions() {
		initRole();
		clearSession(role);
		return JSON;
	}

	/**
	 * 角色管理模块-分配权限操作 保存操作 GWW
	 */
	public String savePermissionList() {
		try {
			String roleId = getRequest().getParameter("roleId");
			Role r = roleManager.get(Integer.valueOf(roleId));
			Set<Permission> ps = new HashSet<Permission>();
			String permissionIds = getRequest().getParameter("permissionIds");
			if (StringUtils.isNotBlank(permissionIds)) {
				for (String id : permissionIds.split(",")) {
					Permission permission = getManager().get(id);
					if (permission != null) {
						ps.add(permission);
					}
				}

			}

		
			r.setPermissions(ps);
			roleManager.update(r);
			jsonMsg.put("msg", "分配成功！");
		} catch (Exception e) {
			// logger
			jsonMsg.put("msg", "分配失败！");
		}
		return "savePermission";
	}

	/**
	 * 角色分配模块--得到所有权限列表 GWW
	 */
	public String permissionList() {
		Role r = roleManager.get(getRole().getId()); // 得到角色对象
		Set<Permission> plist = r.getPermissions(); // 得到角色对应的权限
		StringBuffer hql = new StringBuffer("from Permission p where 1=1");
		hql.append(" and p.parentPermission is null");
		parents = getManager().getDao().query(hql.toString()); // 得到顶级目录
		for (Permission perParent : parents) {
			for (Permission perChild : perParent.getChildrenPermission()) { // 根据顶级目录得到子目录
				for (Permission per : plist) {
					if (per.equals(perChild)) {
						perChild.setChanged(true); // 选中
						break;
					} else {
						perChild.setChanged(false); // 非选中
					}
				}
			}
		}
		for (Permission p : parents) { // 顶级目录的选中状态
			for (Permission cp : p.getChildrenPermission()) {
				if (cp.getChanged()) {
					p.setChanged(true);
					break;
				}
				p.setChanged(false);
			}
		}
		return "permissionList";
	}

	/**
	 * 角色管理模块--权限分配 判断是否可选中状态
	 */
	public static boolean isHas(String id) {
		User u = UserUtil.getLoginUser(ServletActionContext.getRequest()); // 登陆人
		List<String> roles = new ArrayList<String>(); // 登陆人的角色
		for (Role r1 : loadRoles(u)) {
			roles.add(r1.getName());
		}
		if (!roles.contains("ROLE_ADMIN")) { // 如果角色不是系统管理员
			List<String> ids = new LinkedList<String>();// 权限
			for (Role r : loadRoles(u)) {
				for (Permission p : r.getPermissions()) {
					ids.add(p.getId());
				}
			}
			if (ids.contains(id)) {
				return true;
			} else {
				return false;
			}
		} else {
			return true;
		}
	}

	/**
	 * 加载用户的角色，处理延迟加载问题
	 */
	private static Collection<Role> loadRoles(User user) {
		Set<Role> roles = new HashSet();
		ApplicationContext ctx = WebApplicationContextUtils
				.getWebApplicationContext(ServletActionContext
						.getServletContext());
		UserDao userDao = (UserDao) ctx.getBean("userDao");
		List<Role> roleList = userDao.query(
				"select r from Role r join r.users u " + "where u.id=?",
				user.getId());
		for (Role role : roleList) {
			roles.add(role);
		}
		return roles;
	}

	/**
	 * 客户端通过checkbox选择一个角色，通知到服务器端，将这个role存入session.
	 */
	public String selectPermission() {
		if (getModel() == null || getModel().getId() == null || role == null
				|| role.getId() == null) {
			throw new ApplicationException(
					"Please select a permission at least.");
		}

		selectPermission(getModel().getId(), role.getId(), true);
		return JSON;
	}

	/**
	 * 客户端通过checkbox反选择一个角色，通知到服务器端，将这个role从session中去除.
	 */
	public String deselectPermission() {
		if (getModel() == null || getModel().getId() == null || role == null
				|| role.getId() == null) {
			throw new ApplicationException("Please select a role at least.");
		}

		selectPermission(getModel().getId(), role.getId(), false);
		return JSON;
	}

	/**
	 * 为指定的角色分配权限
	 * 
	 * @param roleId
	 *            分配角色的id
	 * @return 成功：successed 失败： false.
	 */
	public String saveRolePermissions() {
		initRole();
		Set<Integer> permissions = getTemporaryPermissions(role,
				TEMPLATE_REMOVED_PERMISSIONS);
		for (Integer permissionId : permissions) {
			Permission permission = getManager().get(permissionId);
			if (permission != null) {
				permission.getRoles().remove(role);
				role.getPermissions().remove(permission);
			}
		}

		permissions = getTemporaryPermissions(role, TEMPLATE_ADDED_PERMISSIONS);
		for (Integer permissionId : permissions) {
			Permission permission = getManager().get(permissionId);
			if (permission != null) {
				permission.getRoles().add(role);
				role.getPermissions().add(permission);
			}
		}

		roleManager.save(role);
		clearSession(role);

		return JSON;
	}

	/**
	 * Build a hql with order clause
	 */
	private String getOrderHQL(String initHQL) {
		if (StringUtils.isBlank(getSortProperty())) {
			return initHQL;
		}
		return new StringBuffer(100).append(initHQL).append(" order by p.")
				.append(getSortProperty()).append(" ").append(getSortDir())
				.toString();
	}

	/**
	 * 向Sesson中存入一个Permission ID, 用于支持页面上分页的时候记录选择的Permission
	 */
	private void selectPermission(String permissionId, Integer roleId,
			boolean selected) {
		if (StringUtils.isBlank(permissionId)) {
			return;
		}
		initRole();
		setModel(getManager().get(getModel().getId()));
		if (role == null || getModel() == null) {
			return;
		}
		if (selected) {
			temporaryAddPermission(role, getModel());
		} else {
			temporaryRemovePermission(role, getModel());
		}
	}

	/**
	 * 初始<code>role</code>对象
	 */
	private void initRole() {
		if (role == null || role.getId() == null) {
			logger.warn("No role found.");
			throw new ApplicationException(
					"No role found, check your 'currentRoleId' var in js.");
		}

		role = getManager().getDao().get(Role.class, role.getId());
		if (role == null) {
			logger.warn("No role found {}", role.getId());
			throw new ApplicationException(
					"No role found, check your 'currentRoleId' var in js.");
		}
	}

	/**
	 * 从Session中取得某个角色的临时权限，包括待分配和待反分配的
	 * 
	 * @param role
	 *            指定的角色
	 * @param sessionName
	 *            Session名字
	 * 
	 * @return
	 */
	private Set getTemporaryPermissions(Role role, String sessionName) {
		Map rolePermissions = (Map) getRequest().getSession().getAttribute(
				sessionName);
		if (rolePermissions == null) {
			rolePermissions = Collections.synchronizedMap(new HashMap());
			getRequest().getSession()
					.setAttribute(sessionName, rolePermissions);
		}
		Set permissionIds = (Set) rolePermissions.get(role.getId());
		if (permissionIds == null) {
			permissionIds = Collections.synchronizedSet(new HashSet());
			rolePermissions.put(role.getId(), permissionIds);
		}

		return permissionIds;
	}

	/**
	 * 根据角色的授权情况，在Session中暂存一个待分配的Permission ID
	 * 
	 * @param role
	 *            给定角色
	 * @param permission
	 *            即将分配给角色的Permission
	 */
	private void temporaryAddPermission(Role role, Permission permission) {
		// 从Session中取得该角色临时分配的权限Id
		Set permissionIdsAdded = getTemporaryPermissions(role,
				TEMPLATE_ADDED_PERMISSIONS);

		// 如果新分配的Permission还没有保存在数据库中，则暂存这个Permission ID
		if (!role.getPermissions().contains(permission)) {
			permissionIdsAdded.add(permission.getId());
			logger.debug("Temporary add permission {} for role {}",
					permission.getId(), role.getId());
		}
		// 同步即将删除的权限
		Set permissionIdsRemoved = getTemporaryPermissions(role,
				TEMPLATE_REMOVED_PERMISSIONS);
		permissionIdsRemoved.remove(permission.getId());
	}

	/**
	 * 根据角色的授权情况，在Session中暂存一个反分配的Permission ID
	 * 
	 * @param role
	 *            给定角色
	 * @param permission
	 *            即将从角色的权限中删除Permission
	 */
	private void temporaryRemovePermission(Role role, Permission permission) {
		// 从session中取得临时反分配的Permission ID
		Set permissionIdsRemoved = getTemporaryPermissions(role,
				TEMPLATE_REMOVED_PERMISSIONS);
		// 如果该权限已经分配给角色了，则暂存
		if (role.getPermissions().contains(permission)) {
			permissionIdsRemoved.add(permission.getId());
			logger.debug("Temporary remove permission {} for role {}",
					permission.getId(), role.getId());
		}
		// 同步即将添加的权限
		Set permissionIdsAdded = getTemporaryPermissions(role,
				TEMPLATE_ADDED_PERMISSIONS);
		permissionIdsAdded.remove(permission.getId());
	}

	/**
	 * 清空session中有关角色的数据
	 * 
	 */
	private void clearSession(Role role) {
		HttpSession session = getRequest().getSession();
		Map map = (Map) session.getAttribute(TEMPLATE_ADDED_PERMISSIONS);
		if (map.containsKey(role.getId())) {
			map.remove(role.getId());
		}
		map = (Map) session.getAttribute(TEMPLATE_REMOVED_PERMISSIONS);
		if (map.containsKey(role.getId())) {
			map.remove(role.getId());
		}
		logger.debug("Session of role {} cleared.", role.getName());
	}

	/**
	 * 权限选择树
	 * 
	 * @return
	 */
	public String selectPermissionIndex() {
		String roleId = getRequest().getParameter("roleId");
		List<String> ids = new ArrayList<String>();
		List<Permission> permissions = getManager().getPermissionsByRoleId(
				roleId);
		for (Permission p : permissions) {
			ids.add(p.getId());
		}
		root_permission = new ArrayList<Map<String, Object>>();
		List<Permission> perms = getManager().getTopPermissions();
		for (Permission permission : perms) {
			Map<String, Object> top = toMap(permission);
			top = buildTreeByParent(top, true, ids);
			root_permission.add(top);
		}
		return "rootPermission";
	}
	
	/**
	 * 构建上级权限选择树
	 * 
	 * @return
	 */
	public String windowIndex(){
		root_permission = new ArrayList<Map<String, Object>>();
		List<Permission> perms =getManager().getTopPermissions();
		Map<String, Object> root = new LinkedHashMap<String, Object>();
		List<Object> children = new ArrayList<Object>();
		for (Permission permission : perms) {
			Map<String, Object> top = toMap(permission);
			top = buildTreeByParent(top, true,null);
			children.add(top);
		}
		root.put("id", null);
		root.put("text", "顶级权限");
		root.put("children", children);
		root_permission.add(root);
		return "rootPermission";
	}

	// 封装到Map中
	private Map<String, Object> toMap(Permission perm) {
		Map<String, Object> map = null;
		if (perm != null) {
			map = new HashMap<String, Object>();
			map.put("id", perm.getId());
			map.put("text", perm.getDescn());
		}
		return map;
	}

	/**
	 * 返回权限树形列表，每一个权限用一个Map表示
	 * 
	 * @param parent
	 * @param nested
	 *            是否递归
	 * @return
	 */
	@SuppressWarnings({ })
	public Map<String, Object> buildTreeByParent(Map<String, Object> parent,
			boolean nested, List<String> ids) {
		if (parent != null) {
			List<Permission> subs = getManager().getPermissionsById(
					(String) parent.get("id"));
			List<Map<String, Object>> childs = new ArrayList();
			for (Permission perm : subs) {
				Map<String, Object> child = toMap(perm);
				if (nested) { // 递归查询子部门
					child = buildTreeByParent(child, nested, ids);
				}
				childs.add(child);
			}
			if (!childs.isEmpty()) {
				parent.put("children", childs);
				parent.put("state", "closed");
				for (Map<String, Object> c : childs) {
					if (ids != null && ids.contains(c.get("id"))) {
						c.put("checked", true);
					}
					Map<String, Object> attr = new LinkedHashMap<String, Object>();
					attr.put("permission", 1);
					c.put("attributes", attr);
				}
			} else {
			}
		}
		return parent;
	}

	/**
	 * 权限删除
	 */
	@Transactional
	@Override
	public String remove(){
		String ids = getRequest().getParameter("ids");
		String[] id = ids.split(",");
		for (String id1 : id) {
			Permission r = getManager().get(id1);
			if (r.getRoles().size() >0) {
				addActionError("所删除权限已经被其他角色使用，不能删除！");
				return this.index();
			} else {
				getManager().remove(r);
			}
		}
		return SUCCESS;
	}
	/**
	 * @param roleManager
	 *            the roleManager to set
	 */
	@Autowired
	public void setRoleManager(RoleManager roleManager) {
		this.roleManager = roleManager;
	}

	/**
	 * @return the role
	 */
	public Role getRole() {
		return role;
	}

	/**
	 * @param role
	 *            the role to set
	 */
	public void setRole(Role role) {
		this.role = role;
	}

	public List<Permission> getParents() {
		return parents;
	}

	public void setParents(List<Permission> parents) {
		this.parents = parents;
	}

	public List<Permission> getChildren() {
		return children;
	}

	public void setChildren(List<Permission> children) {
		this.children = children;
	}

	public Map<String, String> getJsonMsg() {
		return jsonMsg;
	}

	public void setJsonMsg(Map<String, String> jsonMsg) {
		this.jsonMsg = jsonMsg;
	}

	public List<Map<String, Object>> getRoot_permission() {
		return root_permission;
	}

	public void setRoot_permission(List<Map<String, Object>> root_permission) {
		this.root_permission = root_permission;
	}

}
