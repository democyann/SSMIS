package com.systop.common.modules.security.user.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.userdetails.UserDetails;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.systop.common.modules.dept.model.Dept;
import com.systop.common.modules.dict.model.Dicts;
import com.systop.common.modules.fileattch.model.FileAttch;
import com.systop.common.modules.sector.model.Sector;
import com.systop.core.Constants;
import com.systop.core.model.BaseModel;
import com.systop.core.util.DateUtil;


/**
 * 用户表 The persistent class for the users database table.
 */
@Entity
@Table(name = "users_tab", uniqueConstraints = {})
@SuppressWarnings("serial")
public class User extends BaseModel implements UserDetails, Serializable {

	private static Log log = LogFactory.getLog(User.class);

	// 主键
	private Integer id;

	// 登录帐号
	private String loginId;

	// 密码
	private String password;

	// 确认密码
	private String confirmPwd;
	/** 注册时间 */
	private Date createTime;

	// 状态
	private String status;

	// 姓名
	private String name;

	// 性别
	private String sex;

	// 手机
	private String mobile;

	// 办公电话
	private String phone;

	// 传真
	private String fox;

	// 电子邮件地址
	private String email;

	//QQ
	private String oicq;

	//MSN
	private String msn;
	
	//skypeId
	private String skype;

	// 地址
	private String address;
	
	// 邮编
	private String zip;

	// 身份证号
	private String idCard;

	// 出生日期
	private Date birthDay;

	// 毕业院校
	private String college;
	
	 //毕业时间
	private Date graduationTime;

	// 学历
	private String degree;

	// 专业
	private String major;

	// 入职时间
	private Date joinTime;
	
	//转正时间
	private Date formalDate;

	

	// 用户描述
	private String descn;

	// 最后登录IP
	private String lastLoginIp;

	// 最后登录时间
	private Date lastLoginTime;

	// 登录次数
	private Integer loginTimes;

	// 是否系统用户
	private String isSys = Constants.NO;

	// 用户排序ID
	private Integer orderId;

	// 用户照片
	private String photoId;
	
	//用户照片附件对象
	private FileAttch photo;
	
	// 所属班级、学院、院校
	private Dept dept;
	
	// 所具有的角色
	private Set<Role> roles = new HashSet<Role>(0);

	// 紧急联系方式
 	private String emergencyPhone;
 	
 	// 档案存放
 	private String fileStorage;
 	
 	// 籍贯
 	private String hometown;
 	//是否在线
 	private String ol;
 	
	// 兼职部门
	private Dept partTimeJobDept;
	


 	//用户姓名首字母
 	private String nameFirst;
 	
 	private Double dayCost;
 	
 	/** 密级 */
	private String dense;
 	
    private String notified;
    private String notifiedSms;

    private Date lastOnlineTime;

	
	/** 用户类别 */
	private String type;
	/** 所属部门 */
	private Sector sector;

	/** 所属专业 */
	private Dicts dict;
	/** attchIds */
	private String attchIds;
	/** 实训成绩 */
	private float score;
	/** 实训成绩ID*/
	private String scoreId;
	/** 小组ID */
	private String groupId;
	/**教师评语*/
	private String comment;
	/**是否教师打分，教师打分值为1 */
	private String teacherFlag;
    // 缺省构造器
	public User() {
	}

	public User(Integer id) {
		this.id = id;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "login_id", length = 50)
	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	@Column(name = "password", length = 255)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	// 不做数据库映射
	@Transient
	public String getConfirmPwd() {
		return confirmPwd;
	}

	public void setConfirmPwd(String confirmPwd) {
		this.confirmPwd = confirmPwd;
	}

	@Column(name = "status", length = 1)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "name", length = 50)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "sex", length = 1)
	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	@Column(name = "mobile", length = 40)
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Column(name = "phone", length = 40)
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Column(name = "fox", length = 40)
	public String getFox() {
		return fox;
	}

	public void setFox(String fox) {
		this.fox = fox;
	}

	@Column(name = "email", length = 255)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getGraduationTime() {
		return graduationTime;
	}

	public void setGraduationTime(Date graduationTime) {
		this.graduationTime = graduationTime;
	}

	@Column(name = "oicq", length = 16)
	public String getOicq() {
		return oicq;
	}

	public void setOicq(String oicq) {
		this.oicq = oicq;
	}

	@Column(name = "msn", length = 64)
	public String getMsn() {
		return msn;
	}

	public void setMsn(String msn) {
		this.msn = msn;
	}

	@Column(name = "skype", length = 64)
	public String getSkype() {
		return skype;
	}

	public void setSkype(String skype) {
		this.skype = skype;
	}

	@Column(name = "address", length = 255)
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name = "zip", length = 6)
	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	@Column(name = "id_card", length = 20)
	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	@Column(name = "birthday")
	public Date getBirthDay() {
		return birthDay;
	}

	public void setBirthDay(Date birthDay) {
		this.birthDay = birthDay;
	}

	@Column(name = "college", length = 100)
	public String getCollege() {
		return college;
	}

	public void setCollege(String college) {
		this.college = college;
	}

	@Column(name = "degree", length = 100)
	public String getDegree() {
		return degree;
	}

	public void setDegree(String degree) {
		this.degree = degree;
	}

	@Column(name = "major", length = 100)
	public String getMajor() {
		return major;
	}

	public void setMajor(String major) {
		this.major = major;
	}

	@Column(name = "join_time")
	public Date getJoinTime() {
		return joinTime;
	}

	public void setJoinTime(Date joinTime) {
		this.joinTime = joinTime;
	}

	@Column(name = "descn", length = 500)
	public String getDescn() {
		return descn;
	}



	public void setDescn(String descn) {
		this.descn = descn;
	}

	@Column(name = "last_login_ip", length = 32)
	public String getLastLoginIp() {
		return lastLoginIp;
	}

	public void setLastLoginIp(String lastLoginIp) {
		this.lastLoginIp = lastLoginIp;
	}

	@Column(name = "last_login_time")
	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	@Transient
	public String getLastLoginTimeStr() {
		return DateUtil.convertDateToString(lastLoginTime, "yyyy-MM-dd HH:mm");
	}

	@Column(name = "loginTimes")
	public Integer getLoginTimes() {
		return loginTimes;
	}

	public void setLoginTimes(Integer loginTimes) {
		this.loginTimes = loginTimes;
	}

	@Column(name = "is_sys", columnDefinition = "char(1) default '0'")
	public String getIsSys() {
		return isSys;
	}

	public void setIsSys(String isSys) {
		this.isSys = isSys;
	}

	@Column(name = "order_id")
	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	
	@Column(name = "photo_id")
	public String getPhotoId() {
		return photoId;
	}

	public void setPhotoId(String photoId) {
		this.photoId = photoId;
	}
	
	@Transient
	public FileAttch getPhoto() {
		return photo;
	}

	public void setPhoto(FileAttch photo) {
		this.photo = photo;
	}
	
	public Date getFormalDate() {
		return formalDate;
	}

	public void setFormalDate(Date formalDate) {
		this.formalDate = formalDate;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.EAGER)
	@JoinColumn(name = "dept_id")
	public Dept getDept() {
		return this.dept;
	}

	public void setDept(Dept dept) {
		this.dept = dept;
	}

	
	@ManyToOne(cascade = {}, fetch = FetchType.EAGER)
	@JoinColumn(name = "partTimeJobDept_id")
	public Dept getPartTimeJobDept() {
		return partTimeJobDept;
	}

	public void setPartTimeJobDept(Dept partTimeJobDept) {
		this.partTimeJobDept = partTimeJobDept;
	}

	

	@ManyToMany(targetEntity = Role.class, cascade = {}, fetch = FetchType.LAZY)
	@JoinTable(name = "user_role", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = { @JoinColumn(name = "role_id") })
	public Set<Role> getRoles() {
		return this.roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	/**
	 * 是否有角色
	 */
	@Transient
	public boolean getHasRoles() {
		return roles != null && roles.size() > 0;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof User)) {
			return false;
		}
		User castOther = (User) other;
		return new EqualsBuilder().append(this.getId(), castOther.getId())
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getId()).toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("id", getId())
				.append("name", getName()).toString();
	}

	// Methods from UserDetails
	/**
	 * 用户所具有的权限，可用通过角色-权限对应关系得到
	 */
	private transient GrantedAuthority[] authorities;

	/**
	 * @see {@link UserDetails#getAuthorities()}
	 */
	@Transient
	public GrantedAuthority[] getAuthorities() {
		return this.authorities;
	}

	/**
	 * @param authorities
	 *            the authorities to set
	 */
	public void setAuthorities(GrantedAuthority[] authorities) {
		log.info("Set GrantedAuthorities :" + Arrays.toString(authorities));
		this.authorities = authorities;
	}

	/**
	 * @see {@link UserDetails#getUsername()}
	 */
	@Transient
	public String getUsername() {
		return this.loginId;
	}

	/**
	 * @see {@link UserDetails#isAccountNonExpired()}
	 */
	@Transient
	public boolean isAccountNonExpired() {
		return true;
	}

	/**
	 * @see {@link UserDetails#isAccountNonLocked()}
	 */
	@Transient
	public boolean isAccountNonLocked() {
		return true;
	}

	/**
	 * {@link UserDetails#isCredentialsNonExpired()}
	 */
	@Transient
	public boolean isCredentialsNonExpired() {
		return true;
	}

	/**
	 * {@link UserDetails#isEnabled()}
	 */
	@Transient
	public boolean isEnabled() {
		return StringUtils.equalsIgnoreCase(status, Constants.STATUS_AVAILABLE);
	}

    @Column(name = "emergency_phone")
	public String getEmergencyPhone() {
		return emergencyPhone;
	}

	public void setEmergencyPhone(String emergencyPhone) {
		this.emergencyPhone = emergencyPhone;
	}

	@Column(name = "file_storage")
	public String getFileStorage() {
		return fileStorage;
	}

	public void setFileStorage(String fileStorage) {
		this.fileStorage = fileStorage;
	}

	public String getHometown() {
		return hometown;
	}

	public void setHometown(String hometown) {
		this.hometown = hometown;
	}

    @Column(name="notified")
    public String getNotified() {
        return notified;
    }

    public void setNotified(String notified) {
        this.notified = notified;
    }

    @Column(name="notified_sms")
    public String getNotifiedSms() {
        return notifiedSms;
    }

    public void setNotifiedSms(String notifiedSms) {
        this.notifiedSms = notifiedSms;
    }

    @Column(name="last_online_time")
    public Date getLastOnlineTime() {
        return lastOnlineTime;
    }

    public void setLastOnlineTime(Date lastOnlineTime) {
        this.lastOnlineTime = lastOnlineTime;
    }

    /**
     * @return
     */
    @Transient
    public boolean getOnline(){
        if(lastOnlineTime==null) return false;
        long l = new Date().getTime() - lastOnlineTime.getTime();
        return l <= (5 * 60+5) * 1000;//比5分钟多5秒
    }

	
	public String getDense() {
		return dense;
	}

	public void setDense(String dense) {
		this.dense = dense;
	}
	@Column(name="ol",length=1)
	public String getOl() {
		return ol;
	}

	public void setOl(String ol) {
		this.ol = ol;
	}
	@Column(name="name_first")
	public String getNameFirst() {
		return nameFirst;
	}

	public void setNameFirst(String nameFirst) {
		this.nameFirst = nameFirst;
	}
	@Column(name = "day_cost")
	public Double getDayCost() {
		return dayCost;
	}

	public void setDayCost(Double dayCost) {
		this.dayCost = dayCost;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "sector_id")
	public Sector getSector() {
		return sector;
	}

	public void setSector(Sector sector) {
		this.sector = sector;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "specialty_id")
	public Dicts getDict() {
		return dict;
	}

	public void setDict(Dicts dict) {
		this.dict = dict;
	}

	@Column(name = "type", length = 20)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Transient
	public String getAttchIds() {
		return attchIds;
	}

	public void setAttchIds(String attchIds) {
		this.attchIds = attchIds;
	}
	@Transient
	public float getScore() {
		return score;
	}

	public void setScore(float score) {
		this.score = score;
	}

	@Transient
	public String getScoreId() {
		return scoreId;
	}

	public void setScoreId(String scoreId) {
		this.scoreId = scoreId;
	}

	@Transient
	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	@Transient
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	@Transient
	public String getTeacherFlag() {
		return teacherFlag;
	}

	public void setTeacherFlag(String teacherFlag) {
		this.teacherFlag = teacherFlag;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
    
    
}