package com.systop.common.modules.dept.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.struts2.json.annotations.JSON;

import com.systop.common.modules.dept.DeptConstants;
import com.systop.common.modules.dict.model.Dicts;
import com.systop.common.modules.security.user.model.User;
import com.systop.common.modules.subcompany.model.SubCompany;
import com.systop.core.model.BaseModel;

/**
 * The persistent class for the depts database table.
 * 
 * @author Sam Lee
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "depts", uniqueConstraints = {})
public class Dept extends BaseModel {

	/** id */
	private Integer id;

	/**
	 * 部门描述
	 */
	private String descn;

	/**
	 * 部门名称
	 */
	private String name;
	/**
	 * 院校编码
	 */
	private String code;

	/**
	 * 部门类别
	 */
	private String type;

	/**
	 * 部门类别描述【不存储在数据库中】
	 */
	private String typeStr;

	/**
	 * 部门编号规则：两位数字，从1自动排；
	 */
	private String serialNo;

	/**
	 * 部门类别
	 */
	private String deptSort = "0";

	// 排序
	private Integer orderId;
	/**
	 * 学校、学院、班级的类别标识
	 */
	private Dicts deptType;
	/**
	 * 本科专科
	 */
	private String schoolType;

	/**
	 * 部门简称
	 */
	private String shortName;
	/**
	 * 部门的角色
	 * 
	 * private Set<Role> roles = new HashSet<Role>(0);
	 */

	/**
	 * 上级部门
	 */
	private Dept parentDept;

	/**
	 * 下级部门
	 */
	private Set<Dept> childDepts = new HashSet<Dept>(0);

	/**
	 * 部门下用户
	 */
	private Set<User> users = new HashSet<User>(0);

	/**
	 *  公司
	 */
	private SubCompany subCompany;
	
	/**
	 * 是否为部门,公司，分公司
	 */
	private String judgement;// 公司类别   0是总公司, 1为公司本部, 2分支机构, 3 部门, 4是分公司
	
	/**
	 * 审核人
	 */
	private User shr;
	
	/**
	 * 监管部门
	 */
	private String jgDept;
	/**
	 * 区分是否为监管部门 5:表示为监管部门  null:表示非监管部门
	 */
	private String jgbm; 
	/**
	 * 缺省构造
	 */
	public Dept() {
	}

	/**
	 * @param name
	 *            部门名称
	 * @param type
	 *            部门类别
	 */
	public Dept(String name, String type) {
		this.name = name;
		this.type = type;
	}

	/**
	 * 为在Red5中应用中属性查询而加的构造
	 * 
	 * @param id
	 * @param parentDept
	 */
	public Dept(Integer id, Dept parentDept) {
		this.id = id;
		this.parentDept = parentDept;
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

	@Column(name = "descn")
	public String getDescn() {
		return this.descn;
	}

	public void setDescn(String descn) {
		this.descn = descn;
	}

	@Column(name = "name")
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "serial_no",length=200)
	public String getSerialNo() {
		return this.serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	@Column(name = "dept_sort")
	public String getDeptSort() {
		return deptSort;
	}

	public void setDeptSort(String deptSort) {
		this.deptSort = deptSort;
	}

	@Column(name = "order_id")
	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	@Column(name = "jg_dept")
	public String getJgDept() {
		return jgDept;
	}

	public void setJgDept(String jgDept) {
		this.jgDept = jgDept;
	}

	/**
	 * 隐藏部门管理，对应的角色关联关系删除
	 * 
	 * @ManyToMany(cascade = {}, fetch = FetchType.LAZY, targetEntity =
	 *                     Role.class)
	 * @JoinTable(name = "dept_role", joinColumns =                 {@JoinColumn(name
	 *  = "dept_id")}, inverseJoinColumns =
	 *                 {@JoinColumn(name = "role_id")}) public
	 *                 Set<Role> getRoles() { return this.roles; }
	 * 
	 *                 public void setRoles(Set<Role> roles) { this.roles =
	 *                 roles; }
	 */

	@ManyToOne(cascade = {}, fetch = FetchType.EAGER)
	@JoinColumn(name = "parent_id")
	public Dept getParentDept() {
		return this.parentDept;
	}

	public void setParentDept(Dept parentDept) {
		this.parentDept = parentDept;
	}

	@OneToMany(cascade = {}, fetch = FetchType.LAZY, mappedBy = "parentDept")
	public Set<Dept> getChildDepts() {
		return this.childDepts;
	}

	public void setChildDepts(Set<Dept> childDepts) {
		this.childDepts = childDepts;
	}

	@OneToMany(cascade = {}, fetch = FetchType.LAZY, mappedBy = "dept")
	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	@Transient
	public String getTypeStr() {
		if (StringUtils.isNotBlank(getType())) {
			if (getType().equals(DeptConstants.TYPE_COMPANY)) {
				typeStr = DeptConstants.TYPE_DESCN_COMPANY;
			} else if (getType().equals(DeptConstants.TYPE_SUB_COMPANY)) {
				typeStr = DeptConstants.TYPE_DESCN_SUB_COMPANY;
			}
		}
		return typeStr;
	}

	public void setTypeStr(String typeStr) {
		this.typeStr = typeStr;
	}

	@Transient
	public boolean getHasChild() {
		return this.getChildDepts().size() > 0;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof Dept)) {
			return false;
		}
		Dept castOther = (Dept) other;
		return new EqualsBuilder().append(this.getId(), castOther.getId())
				.isEquals();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getId()).toHashCode();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(this).append("id", getId()).toString();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@JSON(serialize = false)
	@ManyToOne(cascade = {}, fetch = FetchType.EAGER)
	@JoinColumn(name = "subCompany_id")
	public SubCompany getSubCompany() {
		return subCompany;
	}

	public void setSubCompany(SubCompany subCompany) {
		this.subCompany = subCompany;
	}
	
	@Column(name="judge_ment")
	public String getJudgement() {
		return judgement;
	}

	public void setJudgement(String judgement) {
		this.judgement = judgement;
	}

	@JSON(serialize = false)
	@ManyToOne(cascade = {}, fetch = FetchType.EAGER)
	@JoinColumn(name = "shr_id")
	public User getShr() {
		return shr;
	}

	public void setShr(User shr) {
		this.shr = shr;
	}
	
	@Column(name="short_name")
	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	@Column(name="jgbm")
	public String getJgbm() {
		return jgbm;
	}

	public void setJgbm(String jgbm) {
		this.jgbm = jgbm;
	}

	@JSON(serialize = false)
	@Transient
	public Dept getSchoolDept(Dept dept) {
		if (dept.getDeptType().getCode().equals("school")) {
			return dept;
		} else {
			return getSchoolDept(dept.getParentDept());
		}
	}

	@JSON(serialize = false)
	public String getSchoolType() {
		return schoolType;
	}

	public void setSchoolType(String schoolType) {
		this.schoolType = schoolType;
	}
	
	@JSON(serialize = false)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@JSON(serialize = false)
	@OneToOne
	public Dicts getDeptType() {
		return deptType;
	}

	public void setDeptType(Dicts deptType) {
		this.deptType = deptType;
	}
	
}
