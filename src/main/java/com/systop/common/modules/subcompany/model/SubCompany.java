package com.systop.common.modules.subcompany.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.struts2.json.annotations.JSON;

import com.systop.common.modules.dept.model.Dept;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.model.BaseModel;

/**
 * 公司信息
 * 
 * @author WangYaping
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "subcompany")
public class SubCompany extends BaseModel {

	private Integer id;// 主键

	private String name;// 公司名称

	private String phone;// 电话

	private String fax;// 传真

	private String address;// 地址

	private String descn;// 备注

	private List<Dept> deptList;// 部门集合

	private String deptSort;// 公司类别 0为总公司，1为分公司

	private Double x;// 地图x坐标

	private Double y;// 地图y坐标

	private Boolean canceled = false;
	
	private User gwgly; //公文管理员
	
	private User gsld; //公司领导
	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "name", length = 128)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "phone", length = 64)
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Column(name = "fax", length = 64)
	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	@Column(name = "address", length = 128)
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name = "descn", length = 400)
	public String getDescn() {
		return descn;
	}

	public void setDescn(String descn) {
		this.descn = descn;
	}

	@OneToMany(cascade = {}, mappedBy = "subCompany")
	public List<Dept> getDeptList() {
		return deptList;
	}

	public void setDeptList(List<Dept> deptList) {
		this.deptList = deptList;
	}

	public String getDeptSort() {
		return deptSort;
	}

	public void setDeptSort(String deptSort) {
		this.deptSort = deptSort;
	}

	@Column(name = "x")
	public Double getX() {
		return x;
	}

	public void setX(Double x) {
		this.x = x;
	}

	@Column(name = "y")
	public Double getY() {
		return y;
	}

	public void setY(Double y) {
		this.y = y;
	}

	public Boolean getCanceled() {
		return canceled;
	}

	public void setCanceled(Boolean canceled) {
		this.canceled = canceled;
	}
	
	@JSON(serialize = false)
	@ManyToOne(cascade = {}, fetch = FetchType.EAGER)
	@JoinColumn(name = "gwgly_id")
	public User getGwgly() {
		return gwgly;
	}

	public void setGwgly(User gwgly) {
		this.gwgly = gwgly;
	}
	
	@JSON(serialize = false)
	@ManyToOne(cascade = {}, fetch = FetchType.EAGER)
	@JoinColumn(name = "gsld_id")
	public User getGsld() {
		return gsld;
	}

	public void setGsld(User gsld) {
		this.gsld = gsld;
	}
	
	
}
