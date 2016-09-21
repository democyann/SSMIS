package com.systop.common.modules.security.user.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.systop.common.modules.security.user.exception.InvalidUserEmailException;
import com.systop.common.modules.security.user.exception.NoSuchUserException;
import com.systop.common.modules.security.user.model.Permission;
import com.systop.common.modules.security.user.model.Resource;
import com.systop.common.modules.security.user.model.Role;
import com.systop.common.modules.security.user.model.User;
import com.systop.common.modules.security.user.service.init.SysRolesProvider;
import com.systop.core.Constants;

/**
 * Admin账户初始化工具类，Spring在启动后会自动执行init方法，检查{@link UserManager#ADMIN_USER} 用户是否存在，如果不存在，则创建这个账户。
 * 
 * @author Sam
 * 
 */
@Deprecated
@Service
public class SecurityInitializer {
  protected Logger logger = LoggerFactory.getLogger(getClass());
  /**
   * An arbitrary valid email address is used as default email
   */
  public static final String DEFAULT_EMAIL_SUBFIX = "@gmail.com";
  /**
   * 缺省的密码
   */
  public static final String DEFAULT_PWD = "manager";
  
  /**
   * 管理员角色
   */
  public static final String ADMIN_ROLE = "ROLE_ADMIN";
  
  /**
   * 管理员角色的描述
   */
  public static final String ADMIN_ROLE_DESC = "Administrator";
  
  /**
   * 系统管理员许可
   */
  public static final String ADMIN_AUTH = "AUTH_ADMIN";
  
  /**
   * 系统管理员许可的描述
   */
  public static final String ADMIN_AUTH_DESC = "Admin Authorization";

  @Autowired(required = true)
  private UserManager userManager;

  @Autowired(required = true)
  private ResourceManager resourceManager;

  @Autowired(required = true)
  private PermissionManager permissionManager;

  @Autowired(required = true)
  private RoleManager roleManager;

  @Autowired(required = true)
  private StrutsUrlImportService urlService;
  
  /**
   * SysRolesProvider的实现类，每一个实现类都提供一个或多个系统角色的信息
   */
  @Autowired
  private List<SysRolesProvider> sysRolesProviders;

  @Autowired(required = true)
  private ApplicationContext ctx;
  /**
   * 缺省的Admin用户Email
   */
  private String adminEmail = getRandomEmail();
  /**
   * 缺省的Admin用户密码
   */
  private String adminPwd = DEFAULT_PWD;
  /**
   * Admin账户说明
   */
  private String adminDesc;
  /**
   * Admin账户名称
   */
  private String adminName;
  
  @PostConstruct
  @Transactional
  public void init() {
    Boolean needsInitAll; // 如果为false,则只初始化email，否则全部初始化
    try {
      userManager.getAdmin();
      return; // 有admin账户，并且email合法
    } catch (InvalidUserEmailException e) {
      // email不合法
      needsInitAll = false;
    } catch (NoSuchUserException e) {
      // 用户不存在
      needsInitAll = true;
    }

    if (!needsInitAll) {
      logger.info("初始化管理员Email...");
      initEmail();
    } else {
      logger.info("创建系统管理员:用户名{},密码{}", UserManager.ADMIN_USER, DEFAULT_PWD);
      createAdmin();
    }
    
    initSysRoles(); //初始化系统角色。
  }

  /**
   * 使用随机的电子邮件地址，初始化电子邮件
   */
  private void initEmail() {
    User user = userManager.getUser(UserManager.ADMIN_USER);
    //user.setEmail(getRandomEmail());
    userManager.getDao().merge(user);
  }

  /**
   * 得到一个随机的电子邮件用户，用于初始化用户电子邮件，邮件后缀用{@link #DEFAULT_EMAIL_SUBFIX}
   */
  private static String getRandomEmail() {
    return new StringBuffer(20).append(RandomStringUtils.randomAscii(10)).append(
        DEFAULT_EMAIL_SUBFIX).toString();
  }

  /**
   * 初始化{@link Resoruce}：如果Resources表中没有数据，则导入struts2配置文件。
   */
  private List<Resource> initResources() {
    List<Resource> resources = resourceManager.get();
    if (resources.size() == 0) { // 如果resource没有初始化，则从struts2中导入
      urlService.save();
      resources = resourceManager.get();
    }
    return resources;
  }

  /**
   * 如果没有{@link #ADMIN_AUTH},则初始化,返回其实例
   */
  private Permission initAdminPermission(Set<Resource> resources) {
    Permission perm = permissionManager.findObject("from Permission p where p.name=?", ADMIN_AUTH);
    if (perm == null) {
      perm = new Permission();
      perm.setDescn(ADMIN_AUTH_DESC);
      perm.setName(ADMIN_AUTH);
      perm.setIsSys(Constants.STATUS_AVAILABLE); //标记为系统许可
      perm.setResources(resources); // 为许可分配资源
      permissionManager.save(perm);
    }
    return perm;
  }

  /**
   * 如果没有{@link #ADMIN_ROLE},则初始化,返回其实例
   */
  private Role initAdminRole(Set<Permission> perms) {
    Role role = roleManager.findObject("from Role r where r.name=?", ADMIN_ROLE);
    if (role == null) {
      role = new Role();
      role.setDescn(ADMIN_ROLE_DESC);
      role.setName(ADMIN_ROLE);
      role.setIsSys(Constants.STATUS_AVAILABLE);//标记为系统角色
      role.setPermissions(perms);
      roleManager.save(role);
    }
    return role;
  }

  /**
   * 创建一个LoginID为{@link UserManager#ADMIN_USER}的账户，使用缺省的秘密和随机的email 并分配权限
   * 
   * @see {@link #DEFAULT_PWD}
   * @see {@link #DEFAULT_EMAIL_SUBFIX}
   * @see {@link UserManager#ADMIN_USER}
   */
  @Transactional
  public User createAdmin() {
    SessionFactory sessionFactory = (SessionFactory) ctx.getBean("sessionFactory",
        SessionFactory.class);
    Session session = SessionFactoryUtils.getSession(sessionFactory, true);
    TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(session));
    logger.debug("Open single Hibernate Session in AdminInitializer");
    try {
      // 设置资源
      List<Resource> resList = initResources();
      Set<Resource> resSet = new HashSet<Resource>(resList);

      // 设置许可
      Permission adminPerm = initAdminPermission(resSet);
      Set<Permission> perms = new HashSet<Permission>(1);
      perms.add(adminPerm);

      // 设置角色
      Role adminRole = initAdminRole(perms);
      Set<Role> roles = new HashSet<Role>(1);
      roles.add(adminRole);

      // 设置管理员账户
      User admin = new User();
      admin.setLoginId(UserManager.ADMIN_USER);
      admin.setPassword(adminPwd);
      admin.setEmail(adminEmail);
      admin.setDescn(adminDesc);
      admin.setName(adminName);
      admin.setStatus(Constants.STATUS_AVAILABLE);
      admin.setIsSys(Constants. YES);
      admin.setRoles(roles);

      userManager.save(admin);
      return admin;
    } finally {
      TransactionSynchronizationManager.unbindResource(sessionFactory);
      logger.debug("Closing single Hibernate Session in AdminInitializer");
      SessionFactoryUtils.closeSession(session);
    }

  }
  
  /**
   * 初始化系统角色，系统角色的初始值在{@code sysRolesProviders}中
   */
  private void initSysRoles() {
    if(sysRolesProviders == null || sysRolesProviders.size() == 0) {
      logger.warn("There is no system roles to be initialized");
      return;
    }
    
    for(SysRolesProvider srp : sysRolesProviders) {
      List<Role> roles = srp.getSysRoles();
      if(roles != null) {
        for(Role role : roles) {
           initSysRole(role);          
        }
      }
    }
    Role ROLE_LONGLAT = new Role();
    ROLE_LONGLAT.setName("ROLE_LONGLAT");
    ROLE_LONGLAT.setDescn("经纬度角色");
    ROLE_LONGLAT.setIsSys(Constants.STATUS_AVAILABLE);
    roleManager.save(ROLE_LONGLAT);
  }
  /**
   * 创建单个的角色.
   * @param role
   */
  @Transactional
  private void initSysRole(Role role) {
    if(role == null || StringUtils.isBlank(role.getName())) {
      return;
    }
    
    if(roleManager.getDao().exists(role, "name")) {
      logger.info("System role [{}] is already exists.", role.getName());
      return;
    }
    logger.info("Creating system role [{}]...", role.getName());
    roleManager.save(role);
  }

  /**
   * @param adminEmail the adminEmail to set
   */
  public void setAdminEmail(String adminEmail) {
    this.adminEmail = adminEmail;
  }

  /**
   * @param adminPwd the adminPwd to set
   */
  public void setAdminPwd(String adminPwd) {
    this.adminPwd = adminPwd;
  }

  /**
   * @param adminDesc the adminDesc to set
   */
  public void setAdminDesc(String adminDesc) {
    this.adminDesc = adminDesc;
  }

  /**
   * @param adminName the adminName to set
   */
  public void setAdminName(String adminName) {
    this.adminName = adminName;
  }
}
