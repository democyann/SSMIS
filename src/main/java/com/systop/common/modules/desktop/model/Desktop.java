package com.systop.common.modules.desktop.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.systop.common.modules.security.user.model.User;
import com.systop.core.model.BaseModel;

@Entity
@Table(name = "desktops")
@SuppressWarnings("serial")
public class Desktop extends BaseModel{

	// 主键
	private Integer id;
	
	//皮肤
	private String skin = "simple";
	
	//主题
	private String theme;
	
	//用户
	private User user;
	
	//桌面菜单
	private Set<DesktopMenu> desktopMenus = new HashSet<DesktopMenu>(0);
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}	
	
	public String getSkin() {
		return skin;
	}

	public void setSkin(String skin) {
		this.skin = skin;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "desktop")
	@OrderBy("serialNo")
	public Set<DesktopMenu> getDesktopMenus() {
		return desktopMenus;
	}

	public void setDesktopMenus(Set<DesktopMenu> desktopMenus) {
		this.desktopMenus = desktopMenus;
	}	
}
