package com.systop.common.modules.mail.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * SMTP配置对象
 * @author Sam Lee
 *
 */
@Entity
@Table(name = "smtp_config")
public class SmtpConfig {
  /**
   * Default SMTP port
   */
  public static final int DEFAULT_PORT = 25;
  
  private String id;
  
  /**
   * SMTP Host server
   */
  private String host;
  
  /**
   * User account
   */
  private String username;
  
  /**
   * Password of the user.
   */
  private String password;
  
  /**
   * Port.
   */
  private Integer port = DEFAULT_PORT;
  
  /**
   * Need for authentication?
   */
  private String auth = "false";

  /**
   * @return the host
   */
  public String getHost() {
    return host;
  }

  /**
   * @param host the host to set
   */
  public void setHost(String host) {
    this.host = host;
  }

  /**
   * @return the username
   */
  public String getUsername() {
    return username;
  }

  /**
   * @param username the username to set
   */
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * @return the password
   */
  public String getPassword() {
    return password;
  }

  /**
   * @param password the password to set
   */
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * @return the port
   */
  public Integer getPort() {
    return port;
  }

  /**
   * @param port the port to set
   */
  public void setPort(Integer port) {
    this.port = port;
  }

  /**
   * @return the auth
   */
  public String getAuth() {
    return auth;
  }

  /**
   * @param auth the auth to set
   */
  public void setAuth(String auth) {
    this.auth = auth;
  }

  /**
   * @return the id
   */
  @Id
  @GeneratedValue(generator = "assign")
  @GenericGenerator(name = "assign", strategy = "assigned")
  public String getId() {
    return id;
  }

  /**
   * @param id the id to set
   */
  public void setId(String id) {
    this.id = id;
  }
}
