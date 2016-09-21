package com.systop.common.modules.sector.model;

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

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.struts2.json.annotations.JSON;
import org.hibernate.annotations.GenericGenerator;

import com.systop.common.modules.dict.model.Dicts;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.model.BaseModel;

/**
 * The persistent class for the sector database table.
 * 
 * @author Sam Lee
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "sector", uniqueConstraints = {})
public class Sector extends BaseModel {

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
	 * 标识
	 * 
	 */
	private String type;

	/**
	 * 部门编号规则：两位数字，从1自动排；
	 */
	private String serialNo;

	/**
	 * 部门类别
	 */
	private String sectorSort = "0";
	
	/**
	 * 部门编码
	 */
	private String code;

	/**
	 * 学校、学院、班级的类别标识
	 */
	private Dicts sectorType;
	
	/**
	 * 部门的角色
	 * 
	 * private Set<Role> roles = new HashSet<Role>(0);
	 */

	/**
	 * 上级部门
	 */
	private Sector parentSector;

	/**
	 * 下级部门
	 */
	private Set<Sector> childSectors = new HashSet<Sector>(0);

	/**
	 * 部门下用户
	 */
	private Set<User> users = new HashSet<User>(0);

	/**
	 * 缺省构造
	 */
	public Sector() {
		
	}

	/**
	 * @param name
	 *            部门名称
	 * @param type
	 *            部门类别
	 */
	public Sector(String name, String type) {
		this.name = name;
		this.type = type;
	}
	/**
	 * 为在Red5中应用中属性查询而加的构造
	 * @param id
	 * @param parentSector
	 */
	public Sector(Integer id, Sector parentSector) {
		this.id = id;
		this.parentSector = parentSector;
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

	@JSON(serialize = false)
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

	@JSON(serialize = false)
	@Column(name = "serial_no")
	public String getSerialNo() {
		return this.serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	@JSON(serialize = false)
	@Column(name = "sector_sort")
	public String getSectorSort() {
		return sectorSort;
	}

	public void setSectorSort(String sectorSort) {
		this.sectorSort = sectorSort;
	}

	/**
	 * 隐藏部门管理，对应的角色关联关系删除
	 * 
	 * @ManyToMany(cascade = {}, fetch = FetchType.LAZY, targetEntity =
	 *                     Role.class)
	 * @JoinTable(name = "sector_role", joinColumns =                 {@JoinColumn(name
	 *  = "sector_id")}, inverseJoinColumns =
	 *                 {@JoinColumn(name = "role_id")}) public
	 *                 Set<Role> getRoles() { return this.roles; }
	 * 
	 *                 public void setRoles(Set<Role> roles) { this.roles =
	 *                 roles; }
	 */

	@JSON(serialize = false)
	@ManyToOne(cascade = {}, fetch = FetchType.EAGER)
	@JoinColumn(name = "parent_id")
	public Sector getParentSector() {
		return this.parentSector;
	}

	public void setParentSector(Sector parentSector) {
		this.parentSector = parentSector;
	}

	@JSON(serialize = false)
	@OneToMany(cascade = {}, fetch = FetchType.EAGER, mappedBy = "parentSector")
	public Set<Sector> getChildSectors() {
		return this.childSectors;
	}

	public void setChildSectors(Set<Sector> childSectors) {
		this.childSectors = childSectors;
	}
	@JSON(serialize = false)
	@OneToMany(cascade = {}, fetch = FetchType.EAGER, mappedBy = "sector")
	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}
	@JSON(serialize = false)
	@OneToOne(fetch = FetchType.EAGER)
	public Dicts getSectorType() {
		return sectorType;
	}

	public void setSectorType(Dicts sectorType) {
		this.sectorType = sectorType;
	}
	@JSON(serialize = false)
	@Transient
	public boolean getHasChild() {
		return this.getChildSectors().size() > 0;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	@JSON(serialize = false)
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof Sector)) {
			return false;
		}
		Sector castOther = (Sector) other;
		return new EqualsBuilder().append(this.getId(), castOther.getId())
				.isEquals();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	@JSON(serialize = false)
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

	@JSON(serialize = false)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
