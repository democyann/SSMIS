package com.systop.common.modules.dict.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.systop.common.modules.security.user.model.User;
import com.systop.core.model.BaseModel;

/**
 * @ClassName: Dicts
 * @Description:字典表
 * @author 乔轩
 * @date 2013-5-14 下午05:41:16
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "dict")
public class Dicts extends BaseModel {

	/** id */
	private Integer id;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/** 创建人 */
	private User user;

	/**
	 * 编码
	 */
	private String code;

	/**
	 * 类别
	 */
	private String category;

	/**
	 * 描述、备注
	 */
	private String descn;

	/**
	 * 存有知识树代表专业的Id
	 */
	private Integer ksId;

	/**
	 * 缺省构造
	 */
	public Dicts() {

	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return id;
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

	@Column(name = "create_time")
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Column(name = "code")
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "category")
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Integer getKsId() {
		return ksId;
	}

	public void setKsId(Integer ksId) {
		this.ksId = ksId;
	}

}
