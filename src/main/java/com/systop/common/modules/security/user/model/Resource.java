package com.systop.common.modules.security.user.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.acegisecurity.GrantedAuthority;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.systop.common.modules.security.acegi.resourcedetails.ResourceDetails;
import com.systop.core.model.BaseModel;

/**
 * The persistent class for the resources database table.
 * 
 * @author BEA Workshop
 */
@SuppressWarnings("serial")
@Entity
@Inheritance(strategy=javax.persistence.InheritanceType.SINGLE_TABLE)
@Table(name = "resources", uniqueConstraints = { })
public class Resource extends BaseModel implements ResourceDetails,
    Serializable {
  /**
   * id
   */
  private Integer id;

  /**
   * 资源描述
   */
  private String descn;

  /**
   * 资源名称
   */
  private String name;

  /**
   * 资源字符串
   */
  private String resString;

  /**
   * 资源类型
   */
  private String resType;

  /**
   * 版本
   */
  private Integer version;

  /**
   * 可访问此资源的权限
   */
  private Set<Permission> permissions = new HashSet<Permission>(0);

  /**
   * 缺省构造
   */
  public Resource() {
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
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

  @Column(name = "res_string")
  public String getResString() {
    return this.resString;
  }

  public void setResString(String resString) {
    this.resString = resString;
  }

  @Column(name = "res_type")
  public String getResType() {
    return this.resType;
  }

  public void setResType(String resType) {
    this.resType = resType;
  }

  @Version
  @Column(name = "version")
  public Integer getVersion() {
    return this.version;
  }

  public void setVersion(Integer version) {
    this.version = version;
  }

  @ManyToMany(cascade = { }, fetch = FetchType.LAZY, mappedBy = "resources")
  public Set<Permission> getPermissions() {
    return this.permissions;
  }

  public void setPermissions(Set<Permission> permissions) {
    this.permissions = permissions;
  }

  /**
   * @see java.lang.Object#equals(java.lang.Object)
   */
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (!(other instanceof Resource)) {
      return false;
    }
    Resource castOther = (Resource) other;
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
  
  //Method from ResourceDetails
  /**
   * 可以访问该资源的<code>GrantedAuthority</code>
   */
  private transient GrantedAuthority[] authorities;
  /**
   * @param authorities the authorities to set
   */
  public void setAuthorities(GrantedAuthority[] authorities) {
    this.authorities = authorities;
  }

  /** 
   * @see ResourceDetails#getAuthorities()
   */
  @Transient
  public GrantedAuthority[] getAuthorities() {
    if (authorities != null) {
      return authorities;
    }
    // 将Permission转化为GrantedAuthority
    Set<Permission> perms = getPermissions();
    if (perms != null) {
      List<GrantedAuthority> auths = new ArrayList<GrantedAuthority>();

      for (Permission perm : perms) {
        auths.add(perm.toGrantedAuthority());
      }
      return (GrantedAuthority[]) auths.toArray(new GrantedAuthority[0]);
    }

    return new GrantedAuthority[] {};
  }
}