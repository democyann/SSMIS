package com.systop.common.modules.security.user.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.systop.common.modules.security.user.model.Resource;

/**
 * 菜单持久化对象
 * @author ZhouLihui
 */
@SuppressWarnings("serial")
@Entity
public class Menu extends Resource {

   /**
    * 链接地址
    */
	private String url;
	/**
	 * 父菜单
	 */
	private Menu parentMenu;

	/**
	 * 子菜单
	 */
	private List<Menu> childMenus = new ArrayList<Menu>(0);

	/**
	 * 缺省构造
	 */
	public Menu() {
		super();
	}

	@Column(name = "url")
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "res_pid")
	public Menu getParentMenu() {
		return this.parentMenu;
	}

	public void setParentMenu(Menu parentMenu) {
		this.parentMenu = parentMenu;
	}

	@OneToMany(cascade = { CascadeType.REMOVE }, fetch = FetchType.LAZY, mappedBy = "parentMenu")
	public List<Menu> getChildMenus() {
		return this.childMenus;
	}

	public void setChildMenus(List<Menu> childMenus) {
		this.childMenus = childMenus;
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof Menu)) {
			return false;
		}
		Menu castOther = (Menu) other;
		if (castOther.getId() == null) {
			return false;
		}
		return new EqualsBuilder().append(this.getId(), castOther.getId())
				.isEquals();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return new HashCodeBuilder().append(getId()).toHashCode();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this).append("id", getId()).toString();
	}

}
