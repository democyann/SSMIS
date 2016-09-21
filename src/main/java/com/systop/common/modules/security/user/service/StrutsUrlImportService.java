package com.systop.common.modules.security.user.service;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.StrutsConstants;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import com.systop.common.modules.security.acegi.resourcedetails.ResourceDetails;
import com.systop.core.ApplicationException;
import com.systop.core.dao.hibernate.BaseHibernateDao;
import com.systop.core.util.ResourceBundleUtil;

@SuppressWarnings({"unchecked", "rawtypes"})
@Service
public class StrutsUrlImportService {
  private static Logger logger = LoggerFactory.getLogger(StrutsUrlImportService.class);
  /**
   * XPath for query "struts.action.extension"
   */
  private static final String XPATH_STRUTS_EXT = "/struts/constant[@name='"
      + StrutsConstants.STRUTS_ACTION_EXTENSION + "']";
  
  @Autowired(required = true)
  @Qualifier("baseHibernateDao")
  private BaseHibernateDao baseHibernateDao;
  /**
   * Default extension
   */
  private static final String DEFAULT_POSTFIX = "do";
  /**
   * Struts2 Root XML configuration file
   */
  private String root = "struts.xml";
  
  /**
   * Struts2 扩展名
   */
  private String actionPostfix;
  
  @Transactional
  public void save() {
    //  读取struts2配置文件，将action的完整路径存入List
    List resStrings = readStrutsXml(getRootConfigFile(root)); 
    if (!resStrings.isEmpty()) {
      for (Iterator listItr = resStrings.iterator(); listItr.hasNext();) {
        String resString = (String) listItr.next();
        com.systop.common.modules.security.user.model.Resource resource = 
          new com.systop.common.modules.security.user.model.Resource();
        //将action中的/用.替换，作为resource name.
        resource.setName(resString.replaceAll("/", "."));
        resource.setResString(resString);
        resource.setResType(ResourceDetails.RES_TYPE_URL);
        //如果resString没有重复存在，则保存
        if (!baseHibernateDao.exists(resource, "resString")) {
          baseHibernateDao.save(resource);
        }
      }
    }
  }
  
  /**
   * 读取配置文件，找到其中的action的完整的路径，包括后缀.
   * 同时，找到配置文件中的include节点，将包含的配置文件也通过递归的方式读取。
   * @return List of full path of actions.
   */
  
  private List readStrutsXml(Resource resource) {
    String postfix = getActionPostfix();
    List list = new LinkedList(); // this List will be modifed frequently, so we use linked list

    try {
      Document doc = this.readDoc(resource);
      List packageElements = doc.selectNodes("/struts/package"); //Packages in a file.
      if (packageElements != null) {
        for (Iterator itr = packageElements.iterator(); itr.hasNext();) {
          Element packageEle = (Element) itr.next();
          logger.debug("find package '{}'", packageEle.attributeValue("name"));

          String namespace = packageEle.attributeValue("namespace");
          //All actions under the current pacakage.
          List actionElements = packageEle.selectNodes("action");
          if (actionElements != null) {
            for (Iterator actionItr = actionElements.iterator(); actionItr.hasNext();) {
              Element actionEle = (Element) actionItr.next();
              String actionName = actionEle.attributeValue("name");
              String resString = buildActionPath(namespace, actionName, postfix);
              logger.debug("Find resource string [{}]", resString);
              list.add(resString);
            }
          }
        }
      }
      //找到包含的配置文件并递归查找所有action
      List includes = doc.selectNodes("/struts/include");
      if (includes != null) {
        for (Iterator itr = includes.iterator(); itr.hasNext();) {
          Element incEle = (Element) itr.next();
          if (incEle != null) {
            String filename = incEle.attributeValue("file");
            logger.debug("Find a included struts file [{}]", filename);
            Resource[] resources = getStrutsConfigFile(filename);
            for(Resource res : resources) {
              list.addAll(readStrutsXml(res));
            }
          }
        }
      }
    } catch (ApplicationException e) {
      return Collections.EMPTY_LIST;
    }

    return list;
  }
  
  private Document readDoc(Resource resource) {
    SAXReader reader = new SAXReader();
    try {
      return reader.read(resource.getURL());
    } catch (DocumentException e) {
      e.printStackTrace();
      throw new ApplicationException(e.getMessage());
    } catch (IOException e) {
      e.printStackTrace();
      throw new ApplicationException(e.getMessage());
    }
  }
  
  /**
   * 从Struts的配置文件中，找到被包含（inclided）的struts配置文件，
   * 从struts2.1.2开始，支持批量包含，例如：
   * <pre>
   * &lt;include file="com/systop/** /struts*.xml"&gt;&lt;/include&gt;
   * </pre>
   * 本方法可以解析这种批量包含的情况。
   * @param pattern 
   * @return 如果解析错误，返回空的Resource数组
   */
  private Resource[] getStrutsConfigFile(String pattern) {
    ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
    try {
      Resource[] resources = resourcePatternResolver.getResources(pattern);
      return resources;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return new Resource[]{};
  }
  
  /**
   * 找到Struts配置文件的File对象
   */
  private Resource getRootConfigFile(String filePath) {
    if(filePath.startsWith(ResourceUtils.CLASSPATH_URL_PREFIX)) {
      filePath = filePath.substring(ResourceUtils.CLASSPATH_URL_PREFIX.length());
    }
    Resource resouce = new ClassPathResource(filePath);
    logger.debug("Find a struts2 config file '{}'", resouce);
    return resouce;
  }
  
  /**
   * 从Struts的xml配置文件或struts.properites,或default.properties中找到
   * struts2的扩展后缀名.如果解析不成功，则返回{@link #DEFAULT_POSTFIX} 
   * 
   */
  private String getActionPostfix() {
    if (!StringUtils.isEmpty(actionPostfix)) {
      return actionPostfix;
    }
    //从struts.xml找到扩展名
    try {
      Document doc = readDoc(getRootConfigFile(root));
      Element ele = (Element) doc.selectObject(XPATH_STRUTS_EXT);
      if (ele != null) {
        logger.debug("Find struts extension from {}", root);
        actionPostfix = ele.attributeValue("value");
        return actionPostfix;
      }

    } catch (ApplicationException e) {
      e.printStackTrace();
      return DEFAULT_POSTFIX;
    }

    ResourceBundle rb = null;
    try { //从用户自定义struts2属性文件中找到扩展名
      rb = ResourceBundle.getBundle("struts");
      logger.debug("find struts2 properties file 'struts.properties'");
    } catch (MissingResourceException e) {
      try { //从Struts2缺省属性文件中找到扩展名
        rb = ResourceBundle.getBundle("org.apache.struts2.default");
        logger.debug("find struts2 default properties file");
      } catch (MissingResourceException missAgain) {
        // Do noting
      }
    }
    if (rb != null) {
      actionPostfix = ResourceBundleUtil.getString(rb, XPATH_STRUTS_EXT, DEFAULT_POSTFIX);
      return actionPostfix;
    }
    return DEFAULT_POSTFIX;
  }
  
  /**
   * 构建Action的完整路径
   * @param namespace struts2 名字空间
   * @param actionName 某名字空间下的Action的Name
   * @param actionPostfix Action的扩展后缀名
   * @return
   */
  private String buildActionPath(String namespace, String actionName, String actionPostfix) {
    if (StringUtils.isBlank(actionName)) {
      actionName = "*";
    }

    if (StringUtils.isBlank(namespace) || !namespace.endsWith("/")) {
      namespace += "/";
    }

    if (actionName.startsWith("/")) {
      actionName = actionName.substring(1);
    }

    return new StringBuffer(namespace).append(actionName).append(".").append(actionPostfix).append(
        "*").toString();
  }


  /**
   * @param root the root to set
   */
  public void setRoot(String root) {
    this.root = root;
  }

}
