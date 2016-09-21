package com.systop.core.webapp.struts2.validation;

import org.springframework.beans.factory.annotation.Autowired;

import com.systop.core.dao.hibernate.BaseHibernateDao;

/**
 * 数据库验证的基类
 */
public abstract class DaoValidateSupport<T> extends AbstractValidator<T> {
  private BaseHibernateDao dao;

  public BaseHibernateDao getDao() {
    return dao;
  }

  @Autowired
  public void setBaseHibernateDao(BaseHibernateDao dao) {
    this.dao = dao;
  }
}
