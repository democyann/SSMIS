package com.systop.common.modules.desktop.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.systop.common.modules.security.user.model.Menu;
import com.systop.core.model.BaseModel;

@Entity
@Table(name = "desktop_menus")
@SuppressWarnings("serial")
public class DesktopMenu extends BaseModel{
	
	//主键
	private Integer id;

	//桌面
	private Desktop desktop;
	
	//菜单
	private Menu menu;
	
	//颜色
	private String color;
	
	//序号
	private Integer serialNo;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "desktop_id")
	public Desktop getDesktop() {
		return desktop;
	}

	public void setDesktop(Desktop desktop) {
		this.desktop = desktop;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "menu_id")
	public Menu getMenu() {
		return menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public Integer getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(Integer serialNo) {
		this.serialNo = serialNo;
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof DesktopMenu)) {
			return false;
		}
		DesktopMenu castOther = (DesktopMenu) other;
		if (this.getDesktop() == null || this.getMenu() == null || castOther.getDesktop() == null || castOther.getMenu() == null) {
			return false;
		}
		return new EqualsBuilder()
				.append(this.getDesktop().getId(), castOther.getDesktop().getId())
				.append(this.getMenu().getId(), castOther.getMenu().getId())
				.isEquals();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		HashCodeBuilder hcb = new HashCodeBuilder();
		if (this.getDesktop() != null && this.getMenu() != null){
			hcb.append(getDesktop().getId()).append(getMenu().getId());
		}else{
			hcb.append(getId());
		}
		return hcb.toHashCode();
	}
}
