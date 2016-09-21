package com.systop.common.modules.mail.service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.DefaultPropertiesPersister;
import org.springframework.util.PropertiesPersister;
import org.springframework.util.ResourceUtils;

import com.systop.common.modules.mail.model.SmtpConfig;

/**
 * Use properties file to saving the SMTP settings.
 * @author Sam Lee
 *
 */

public class SmtpConfigPropertiesFileManager implements SmtpConfigManager {
  protected Logger logger = LoggerFactory.getLogger(getClass());
  
  // Constants of the keys
  public static final String KEY_SMTP_HOST = "mail.smtp.host";
  public static final String KEY_SMTP_PORT = "mail.smtp.port";
  public static final String KEY_SMTP_USERNAME = "mail.smtp.username";
  public static final String KEY_SMTP_PASSWORD = "mail.smtp.password";
  public static final String KEY_SMTP_AUTH = "mail.smtp.auth";
  /**
   * Properties file name.
   */
  private String propertiesLocation;
 
  /**
   * @param propertiesLocation the propertiesLocation to set
   */
  public void setPropertiesLocation(String propertiesLocation) {
    this.propertiesLocation = propertiesLocation;
  }
  
  
  public SmtpConfig getSmtpConfig() {
    PropertiesPersister propPersister = new DefaultPropertiesPersister(); 
    Reader reader = null;
    try {
      reader = new FileReader(ResourceUtils.getFile(propertiesLocation));
    } catch (FileNotFoundException e) {
      logger.error("Properties file {}, not found.", propertiesLocation);
      return null;
    }
    Properties props = new Properties();
    try {
      propPersister.load(props, reader);
    } catch (IOException e) {
      logger.error("An IO exception occurs, {}." + e.getMessage());
      return null;
    }
    
    SmtpConfig smtp = new SmtpConfig();
    smtp.setHost(props.getProperty(KEY_SMTP_HOST));
    smtp.setUsername(props.getProperty(KEY_SMTP_USERNAME));
    smtp.setPassword(props.getProperty(KEY_SMTP_PASSWORD));
    smtp.setPort(Integer.valueOf(props.getProperty(KEY_SMTP_PORT)));
    smtp.setAuth(props.getProperty(KEY_SMTP_AUTH));
    
    return smtp;
  }

  
  public void setSmtpConfig(SmtpConfig smtp) {
    Properties props = new Properties();
    props.put(KEY_SMTP_HOST, smtp.getHost());
    props.put(KEY_SMTP_USERNAME, smtp.getUsername());
    props.put(KEY_SMTP_PASSWORD, smtp.getPassword());
    props.put(KEY_SMTP_PORT, String.valueOf(smtp.getPort()));
    props.put(KEY_SMTP_AUTH, String.valueOf(smtp.getAuth()));
    PropertiesPersister propPersister = new DefaultPropertiesPersister(); 
    Writer writer = null;
    try {
      writer = new FileWriter(ResourceUtils.getFile(propertiesLocation));
      propPersister.store(props, writer, "SMTP Settings");
    } catch (FileNotFoundException e) {
      logger.error("Properties file {}, not found.", propertiesLocation);
    } catch (IOException e) {
      logger.error("An IO Exception occurs.{}", e.getMessage());
      return;
    }
  }

}
