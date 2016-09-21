package com.systop.common.modules.security.user.service;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.acegisecurity.providers.encoding.PasswordEncoder;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.systop.common.modules.dept.model.Dept;
import com.systop.common.modules.dept.service.DeptManager;
import com.systop.common.modules.fileattch.FileConstants;
import com.systop.common.modules.fileattch.service.FileAttchManager;

import com.systop.common.modules.sector.model.Sector;
import com.systop.common.modules.security.user.UserConstants;
import com.systop.common.modules.security.user.dao.UserDao;
import com.systop.common.modules.security.user.exception.InvalidUserEmailException;
import com.systop.common.modules.security.user.exception.NoSuchUserException;
import com.systop.common.modules.security.user.model.Permission;
import com.systop.common.modules.security.user.model.User;
import com.systop.common.modules.security.user.model.UserLoginHistory;
import com.systop.common.modules.security.user.service.listener.UserChangeListener;
import com.systop.core.ApplicationException;
import com.systop.core.Constants;
import com.systop.core.service.BaseGenericsManager;
import com.systop.core.util.ReflectUtil;
import com.systop.core.util.ValidationUtil;

/**
 * 用户管理类
 * 
 * @author Sam Lee
 * 
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
@Service
public class UserManager extends BaseGenericsManager<User> {

	// 管理员账户，不能删除。
	public static final String ADMIN_USER = "admin";

	// 用于加密密码
	@Autowired
	private PasswordEncoder passwordEncoder;

	// JdbcTemplate，用户获取密码
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private DeptManager deptManager;

	// 特制用户DAO
	@Autowired
	private UserDao userDao;

	// 附件操作类
	@Autowired
	private FileAttchManager fileAttchManager;

	/**
	 * User对象持久化操作监听器，可以直接注入UserListener的实例 或完整的类名。
	 * 
	 * @see {@link com.systop.common.security.user.service.listener.UserChangeListener}
	 */
	@Autowired
	private List<UserChangeListener> userChangeListeners;

	/**
	 * 获得具有某一Permission的用户
	 * 
	 * @param perm
	 *            给定Permission
	 * @return List of User or empty list.
	 */
	public List<User> findUsersByPermission(Permission perm) {
		return userDao.findUsersByPermission(perm);
	}

	/**
	 * 返回给定用户User所具有的权限(Permission)
	 * 
	 * @param user
	 *            给定用户
	 * @return List of User or empty list.
	 */
	public List<Permission> findPermissionsByUser(User user) {
		return userDao.findPermissionsByUser(user);
	}

	/**
	 * 根据Id获得用户，同时加在用户照片信息
	 * 
	 * @param id
	 * @return
	 */
	public User get(Serializable id) {
		User u = super.get(id);
		// 获得个人照片
		if (u != null && StringUtils.isNotBlank(u.getPhotoId())) {
			u.setPhoto(fileAttchManager.findFirst(u.getPhotoId()));
		}
		return u;
	}

	/**
	 * 删除用户照片
	 * 
	 * @param user
	 * @param context
	 */
	@Transactional
	public void removeUserPhoto(User user, ServletContext context) {
		if (user != null) {
			fileAttchManager.removeByIds(user.getPhotoId(), context);
			user.setPhotoId(null);
			getDao().update(user);
		}
	}
	/**
	 * 初始化密码
	 * @param model
	 * @param oldPassword
	 */
	@Transactional
	public void initPassword(User model){
		String initPassword = passwordEncoder.encodePassword("111111", null);
		User user = getDao().get(User.class,model.getId());
		user.setPassword(initPassword);
		getDao().getHibernateTemplate().update(user);
		synchronousCache(user, user);
	}
	/**
	 * 根据登录名和密码得到User
	 * 
	 * @param loginId
	 * @param password
	 * @return Instance of User or null.
	 */
	public User getUser(String loginId, String password) {
		List<User> users = getDao().query("from User user where user.loginId=? and user.password=?", new Object[] { loginId, password });
		if (users.isEmpty()) {
			return null;
		}
		return users.get(0);
	}

	/**
	 * 根据用户登录名获得User对象
	 * 
	 * @param loginId
	 * @return Instance of User or null.
	 */
	public User getUser(String loginId) {
		List<User> users = getDao().query("from User user where user.loginId=?", loginId);
		if (users.isEmpty()) {
			return null;
		}
		return users.get(0);
	}

	/**
	 * 更新用户的登录时间(当前时间)和IP地址
	 * 
	 * @param user
	 *            给定用户
	 * @param loginIp
	 *            给定用户登录所在IP
	 */
	@Transactional
	public void updateLoginInformation(User user, String loginIp) {
		if (user == null || user.getId() == null) {
			logger.error("No user login, return only.");
			return;
		}

		user.setLastLoginIp(loginIp);
		user.setLastLoginTime(new Date());
		if (user.getLoginTimes() == null) {
			user.setLoginTimes(1);
		} else {
			user.setLoginTimes(user.getLoginTimes().intValue() + 1);
		}
		// issue 771,set online status
		// user.setLastOnlineTime(new Date());
		update(user);
	}

	/**
	 * 添加用户登录历史记录
	 * 
	 * @param user
	 *            给定用户
	 * @param loginIp
	 *            给定用户登录所在IP
	 */
	@Transactional
	public void addUserLoginHistory(User user, String loginIp) {
		if (user == null || user.getId() == null) {
			logger.error("No user login, return only.");
			return;
		}
		if (loginIp.equals("0:0:0:0:0:0:0:1")) {
			loginIp = "127.0.0.1";
		}
		UserLoginHistory uh = new UserLoginHistory(user, new Date(), loginIp, user.getDept());
		getDao().save(uh);
	}

	/**
	 * 保存用户并关联指定的部门
	 * 
	 * @param user
	 * @param deptId
	 * @param deptIdTwo 
	 */
	@Transactional
	public void save(User user, Integer deptId, Integer deptIdTwo) {
		if (deptId != null) {
			user.setDept(getDao().get(Dept.class, deptId));  
		}
		if(deptIdTwo != null){
			user.setPartTimeJobDept(getDao().get(Dept.class,deptIdTwo));
		}else{
			user.setPartTimeJobDept(null);
			
		}
		save(user);
	}

	/**
	 * 保存用户信息，并根据下面的情况决定是否加密密码:<br>
	 * 
	 * <ul>
	 * <li>如果是新建用户，密码加密</li>
	 * <li>如果是修改用户，使用原来的密码</li>
	 * </ul>
	 * 
	 * @param user
	 *            User to save.
	 */
	@Transactional
	public void save(User user) {

		if (user.getId() == null && user.getStatus() == null) {
			// 新建用户的状态为'正常'
			user.setStatus(UserConstants.USER_STATUS_USABLE);
			// 新建用户时设置密码,并加密,修改用户 不修改密码
			user.setPassword(passwordEncoder.encodePassword(user.getPassword(), null));
		}

		if (getDao().exists(user, "loginId")) {// 验证重复的登录名
			throw new ApplicationException("登录帐号已经存在:" + user.getLoginId());
		}
		if (StringUtils.isNotBlank(user.getIdCard())) {
			if (getDao().exists(user, "idCard")) {// 验证重复的登录名
				throw new ApplicationException("身份证号已经存在:" + user.getIdCard());
			}
		}
		if (StringUtils.isNotBlank(user.getEmail())) {
			if (getDao().exists(user, "email")) {
				throw new ApplicationException("您的E-mail地址已经存在,请更换一个.");
			}

		}
		if (StringUtils.isNotBlank(user.getSkype())) {
			if (getDao().exists(user, "skype")) {
				throw new ApplicationException("您的skype帐号已经有人使用,请更换一个.");
			}

		}
		// 当存在附件id时反向修改附件的类别信息
		fileAttchManager.updateFileType(user.getPhotoId(), FileConstants.USER);
	    getDao().getHibernateTemplate().clear();
		getDao().saveOrUpdate(user);
		synchronousCache(user, user);
	}

	/**
	 * @see BaseManager#remove(java.lang.Object)
	 */
	@Override
	@Transactional
	public void remove(User user) {
		if (Constants.STATUS_UNAVAILABLE.equals(user.getStatus())) {
			return;
		}
		// 确保LoginID存在
		if (StringUtils.isBlank(user.getLoginId())) {
			user = get(user.getId());
		}
		// 确保不能删除(禁用)Admin账户.
		if (ADMIN_USER.equals(user.getLoginId())) {
			logger.warn("不能删除或禁用admin账户，这是一个危险的动作哦。");
			return;
		}
		user.setStatus(Constants.STATUS_UNAVAILABLE);
		if (userChangeListeners != null) {
			for (Iterator itr = userChangeListeners.iterator(); itr.hasNext();) {
				UserChangeListener listener = getListener(itr.next());
				listener.onRemove(user);
			}
		}
	}

	/**
	 * 启用用户
	 * 
	 * @param user
	 */
	@Transactional
	public void unsealUser(User user) {
		if (Constants.STATUS_AVAILABLE.equals(user.getStatus())) {
			return;
		}
		user.setStatus(Constants.STATUS_AVAILABLE);
		if (userChangeListeners != null) {
			for (Iterator itr = userChangeListeners.iterator(); itr.hasNext();) {
				UserChangeListener listener = getListener(itr.next());
				listener.onSave(user, user);
			}
		}
	}
	
	@Transactional
	public void delete(User user){
		getDao().delete(user);
	}

	/**
	 * 修改密码
	 * 
	 * @param model
	 *            封装密码的对象
	 * @param oldPassword
	 * @throws Exception
	 */
	@Transactional
	public void changePassword(User model, String oldPassword) {
		// 获得加密后的密码
		String encodePasswrod = passwordEncoder.encodePassword(oldPassword, null);
		// 判断输入的原密码和数据库中的原密码是否一致
		if (encodePasswrod.equals(getOldPassword(model.getId()))) {
			User user = getDao().get(User.class, model.getId());
			// 设置新密码
			user.setPassword(passwordEncoder.encodePassword(model.getPassword(), null));
			getDao().getHibernateTemplate().update(user);
			synchronousCache(user, user);
		} else {
			throw new ApplicationException("旧密码不正确!");
		}
	}

	@Transactional
	public User changePassword2(User model) {
		User user = getUser(model.getLoginId());
		user.setPassword(passwordEncoder.encodePassword(model.getPassword(), null));
		getDao().getHibernateTemplate().update(user);
		synchronousCache(user, user);
		return user;
		// synchronousCache(user, user);
	}

	/**
	 * 返回指定UserId的密码
	 */
	private String getOldPassword(Integer userId) {
		return (String) jdbcTemplate.query("select password from users_tab where id=?", new Object[] { userId }, new ResultSetExtractor() {

			public Object extractData(ResultSet rs) throws SQLException {
				rs.next();
				return rs.getString(1);
			}

		});
	}

	/**
	 * 得到loginId为{@link #ADMIN_USER}账户.
	 * 
	 * @return Instance of {@code User}
	 * @throws NoSuchUserException
	 *             如果{@link #ADMIN_USER}账户不存在
	 * @throws InvalidUserEmailException
	 *             如果{@link #ADMIN_USER}账户的email不合法
	 */
	public User getAdmin() throws NoSuchUserException, InvalidUserEmailException {
		User admin = getUser(ADMIN_USER);
		if (admin == null) {
			throw new NoSuchUserException("admin 账户不存在.");
		}
		if (!ValidationUtil.isValidEmail(admin.getEmail())) {
			throw new InvalidUserEmailException("admin 账户电子邮件不合法-" + admin.getEmail());
		}

		return admin;
	}

	/**
	 * 返回部门以及子部门下的员工信息
	 * 
	 * @param dept
	 *            部门树形列表
	 */
	public Map getUserTree(Map dept, String roleName) {
		if (dept.isEmpty()) {
			return dept;
		}
		// 设置部门类型
		dept.put("type", dept.get("type"));
		// 得到部门ID
		Integer deptId = (Integer) dept.get("id");
		List<User> list;
		if (StringUtils.isBlank(roleName)) {
			list = query("from User u where u.dept.id=?", deptId);
		} else {
			list = query("select u from User u inner join u.roles r where u.dept.id=? and r.name=?", deptId, roleName);
		}
		// 添加本部门员工
		List children = new ArrayList();
		for (User user : list) {
			Map map = new HashMap();
			map.put("id", user.getId());
			map.put("text", user.getName());
			map.put("type", "user");
			map.put("sex", user.getSex());
			map.put("leaf", true);
			children.add(map);
		}

		// 得到此部门下的所有子部门
		List<Map> childNodes = (List) dept.get("childNodes");
		if (childNodes != null) {
			for (Map map : childNodes) {
				map = getUserTree(map, roleName);
				children.add(map);
			}
		}
		if (!children.isEmpty()) {
			dept.put("children", children);
			dept.put("childNodes", children);
			dept.put("leaf", false);
		} else {
			dept.put("leaf", true);
		}
		return dept;
	}

	/**
	 * 同步缓存
	 * 
	 * @param user
	 * @param oldUser
	 */
	private void synchronousCache(User user, User oldUser) {
		if (userChangeListeners != null) {
			// 执行UserChangeListener，实现缓存同步等功能
			for (Iterator itr = userChangeListeners.iterator(); itr.hasNext();) {
				UserChangeListener listener = getListener(itr.next());
				listener.onSave(user, oldUser);
			}
		}
	}

	/**
	 * 根据登陆名称取得用户
	 * 
	 * @param uId
	 * @param uName
	 */
	public List<User> getUserByName(String uId, String uName) {
		List<User> list = Collections.EMPTY_LIST;
		StringBuffer hql = new StringBuffer("from User u where u.loginId = ?");
		if (StringUtils.isNotBlank(uId)) {
			hql.append(" and u.id != ?");
			list = query(hql.toString(), new Object[] { uName, Integer.valueOf(uId) });
		} else {
			list = query(hql.toString(), uName);
		}
		return list;
	}

	/**
	 * 根据email取得用户
	 * 
	 * @param uId
	 * @param email
	 */
	public List<User> getUserByEmail(String uId, String email) {
		List<User> list = Collections.EMPTY_LIST;
		StringBuffer hql = new StringBuffer("from User u where u.email = ?");
		if (StringUtils.isNotBlank(uId)) {
			hql.append(" and u.id != ?");
			list = query(hql.toString(), new Object[] { email, Integer.valueOf(uId) });
		} else {
			list = query(hql.toString(), email);
		}
		return list;
	}
	/**
	 * 根据email取得用户
	 * 
	 * @param uId
	 * @param email
	 */
	public List<User> getUserByName(String name) {
		List<User> list = Collections.EMPTY_LIST;
		StringBuffer hql = new StringBuffer("from User u where u.name = ?");
		list = query(hql.toString(),name);
		return list;
	}

	/**
	 * 执行监听器
	 * 
	 * @see UserListener
	 */
	private UserChangeListener getListener(Object obj) {
		if (obj == null) {
			return null;
		}
		if (obj instanceof String) {
			return (UserChangeListener) ReflectUtil.newInstance((String) obj);
		} else {
			return (UserChangeListener) obj;
		}
	}

	/**
	 * 判断某个用户是否在线
	 * 
	 * @param user
	 *            用户实体
	 * @return 如果在线(online=1),返回true
	 */
	@Deprecated
	public boolean isUserOnline(User user) {
		if (user == null || user.getId() == null) {
			logger.warn("用户对象为null或者id为null，无法判断用户是否在线。");
			return false;
		}
		user = get(user.getId());
		// Constants.YES.equals(user.getOnline());
		return true;
	}

	/**
	 * update user last online date
	 */
	@Transactional
	public void updateOnlineStatus(User user, boolean reset) {
		User _user = get(user.getId());
		if (reset) {
			_user.setLastOnlineTime(null);
		} else {
			_user.setLastOnlineTime(new Date());
		}
		update(_user);
	}

	public List<User> getNotifiedUsers() {
		List<User> list = Collections.EMPTY_LIST;
		String hql = "from User u where u.notified = ? and u.status = 1";
		list = query(hql, "true");
		return list;
	}

	public List<User> getNotifiedSmsUser() {
		List<User> list = Collections.EMPTY_LIST;
		String hql = "from User u where u.notifiedSms = ? and u.status = 1";
		list = query(hql, "true");
		return list;
	}

	public List<User> getUsersBydeptId(Integer id) {
		List<User> list = query("from User u where u.dept.id = ?", id);
		return list;
	}

	/**
	 * 更新登录时间,按说不应该这样
	 * 
	 * @param user
	 */
	@Transactional
	public void updateLoginTime(User user) {
		User _user = get(user.getId());
		user.setLastLoginTime(new Date());
		update(_user);
	}

	/**
	 * 得到资产审批的财务科室的主管或经理或主任
	 * 
	 */
	public User getCsUser(User user) {

		List<User> users = getDao()
				.query("from User user where user.dept.subCompany.id = "
						+ user.getDept().getSubCompany().getId()
						+ " and user.dept.name like '%财务%' and (user.position.name like '%主管%' or user.position.name like '%经理%'  or user.position.name like '%主任%' ) and user.status='1'");
		if (users.isEmpty()) {
			return null;
		}
		return users.get(0);
	}

	/**
	 * 得到资产审批的boss老总
	 * 
	 */
	public User getBossUser(User user) {
		List<User> users = getDao().query(
				"from User user where user.dept.subCompany.id = " + user.getDept().getSubCompany().getId()
						+ " and  user.position.name like '%总经理%' and user.status='1'");
		if (users.isEmpty()) {
			return null;
		}
		return users.get(0);
	}

	/**
	 * 根据部门获取有审批权限的用户。【公文处使用】
	 * 
	 * @param dept
	 * @return
	 */
	public List<User> getAuthUserByDept(Integer id) {
		String hql = "from User u where u.dept.id = ? and u.position.authority = '0' and u.status = '1'";
		Dept dept = deptManager.get(id);
		List<User> users = query(hql, dept.getId());
		return users;
	}

	/**
	 * 根据部门获取有收发岗权限的用户。【公文处使用】
	 * 
	 * @param dept
	 * @return
	 */
	public List<User> getTranUserByDept(Integer id) {
		String hql = "from User u where u.dept.id = ? and u.position.transceiver = '0' and u.status = '1'";
		Dept dept = deptManager.get(id);
		List<User> users = query(hql, dept.getId());
		return users;
	}

	/**
	 * 通过职位id拿到一组对象
	 * 
	 * @param id
	 * @return
	 */
	public List<User> queryByPositionId(Integer id) {

		StringBuffer hql = new StringBuffer("from User p where 1=1 ");
		List<Object> args = new ArrayList<Object>();
		if (id != 0) {
			hql.append("and p.position.id = ? ");
			args.add(id);
		}
		return query(hql.toString(), args.toArray());
	}

	/**
	 * 获取在线人数
	 * 
	 * @return
	 */
	public Long getOlCount() {
		String hql = "select count(*) from User u where u.ol = ?";
		Long c = null;
		try {
			c = (Long) getDao().getSessionFactory().getCurrentSession().createQuery(hql).setString(0, "1").uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return c;
	}
	/**
	 * 
	 * @return 有排序的用户集合，升序排序。
	 * @Description:查询所有有审批序号的用户，返回此升序集合。
	 */
	public List<User> getOrderUsers() {
		List<User> users = query("from User u where u.orderId is not null order by u.orderId");
		return users;
	}
	/**
	 * 
	 * @param 数组ids
	 * @return 升序排序的用户集合。
	 * @Description:通过传入的用户id集合，查找用户并按照审批循序升序排列，然后返回此用户集合。
	 */
	public List<User> getOrderUsers(Serializable[] selectedItems){
		StringBuffer hql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		hql.append("from User u where ");
		for(int i = 0;i < selectedItems.length;i++){
			if(i==selectedItems.length-1){
			hql.append(" u.id = ?  ");
			args.add(Integer.parseInt(selectedItems[i].toString()));
			}else{
				hql.append(" u.id = ? or ");
				args.add(Integer.parseInt(selectedItems[i].toString()));
			}
			
		
		}
		hql.append("order by u.orderId");
		List<User> users = query(hql.toString(), args.toArray());
		return users;
	}

	/**
	 * 根据用户部门ID,该部门下以及子部门下的所有员工
	 * 
	 * @param id
	 *            部门ID
	 */
	public List<User> getUsersBySectorId(Integer id) {
		Sector sector = getDao().get(Sector.class, id);
		List<User> list = null;
		if (sector != null) {
			String hql = "from User u where u.sector.id like ? ";
			list = getDao().query(hql, MatchMode.END.toMatchString(sector.getId().toString()));
		}
		return list;
	}


	@Transactional
	public void save2(boolean flag, User user, boolean encodePassword) {
		if (user.getId() == null && user.getStatus() == null) { // 新建用户的状态为正常
			user.setCreateTime(new Date());
			user.setStatus(UserConstants.USER_STATUS_USABLE);
		}
		User oldUser = new User(); // 用于同步缓存
		oldUser.setLoginId(user.getLoginId());
		oldUser.setId(user.getId());
		// 验证重复的登录名
		if (getDao().exists(oldUser, new String[] { "loginId" })) {
			throw new ApplicationException("您输入的登录名'" + oldUser.getLoginId() + "'已经存在.");
		}
		// 如果是修改，并且输入的密码为空或UserConstants.DEFAULT_PASSWORD，则表示使用原来的密码
		if (user.getId() != null
				&& (user.getPassword().equals(UserConstants.DEFAULT_PASSWORD) || StringUtils
						.isBlank(user.getPassword()))) {
			String password = getOldPassword(user.getId());
			logger.debug("Use old password '{}'", password);
			user.setPassword(password);
			// 解决a different object with the same identifier value
			// was already associated with the session的问题
			getDao().evict(get(user.getId()));
			getDao().clear();
			super.save(flag, user);
		} else {
			if (passwordEncoder != null && encodePassword) { // 密码加密
				user.setPassword(passwordEncoder.encodePassword(user.getPassword(), null));
			}
			getDao().clear();
			super.save(flag, user);
		}

		if (userChangeListeners != null) { // 执行UserChangeListener，实现缓存同步等功能
			for (Iterator itr = userChangeListeners.iterator(); itr.hasNext();) {
				UserChangeListener listener = getListener(itr.next());
				listener.onSave(user, oldUser);
			}
		}
	}

	/**
	 * @param u
	 * @Description:
	 */
	@Transactional
	public void saveByImport(User user) throws SQLException {
		String sql = "select id from users where id = ?";
		Session session = SessionFactoryUtils.getSession(getDao().getHibernateTemplate().getSessionFactory(), true);
		Query query = session.createSQLQuery(sql).setParameter(0, user.getId());
		List list = query.list();
		if (list.isEmpty()) {
			save(user);
		}
	}
}
