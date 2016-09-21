package com.systop.common.modules.dict.webapp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.hibernate.criterion.MatchMode;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import com.systop.common.modules.dict.DictConstants;
import com.systop.common.modules.dict.model.Dicts;
import com.systop.common.modules.dict.service.DictsManager;
import com.systop.core.util.PageUtil;
import com.systop.core.webapp.struts2.action.DefaultCrudAction;

/**
 * @ClassName: DictsAction
 * @Description:字典模块信息Action,负责处理与之相关的各种请求
 * @author 乔轩
 * @date 2013-5-14 下午05:41:16
 * 
 */
@SuppressWarnings({ "serial" })
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class DictsAction extends DefaultCrudAction<Dicts, DictsManager> {
	private String exceptionFlag;
	private List<String> alist = new ArrayList<String>();
	private String dicttype;
	public String save() {
		// 判断编码是否已存在
		if (getManager().getDao().exists(getModel(), "code")) {
			addActionError("编码'" + getModel().getCode() + "'已存在!");
			return INPUT;
		}
		if (getManager().getDao().exists(getModel(), "name")) {
			addActionError("名称'" + getModel().getName() + "'已存在!");
			return INPUT;
		}

		getModel().setUser(getLoginUser());
		getModel().setCreateTime(new Date());
		getManager().save(getModel());
		return SUCCESS;
	}

	/**
	 * @Title: adminIndex
	 * @Description: 字典表查询
	 * @author 乔轩
	 * @date 2013-5-16 下午05:58:41
	 * @return
	 */
	public String adminIndex() {
		getModel();
		StringBuffer hql = new StringBuffer("from Dicts d where 1=1");
		List<Object> args = new ArrayList<Object>();
		if (StringUtils.isNotBlank(getModel().getName())) {
			hql.append(" and d.name like ?");
			args.add(MatchMode.ANYWHERE.toMatchString(getModel().getName()));
		}
		if (StringUtils.isNotBlank(getModel().getCode())) {
			hql.append(" and d.code like ?");
			args.add(MatchMode.ANYWHERE.toMatchString(getModel().getCode()));
		}
		if (StringUtils.isNotBlank(getModel().getCategory())) {
			hql.append(" and d.category like ?");
			args.add(MatchMode.ANYWHERE.toMatchString(getModel().getCategory()));
		}
		hql.append(" order by d.category ");
		page = PageUtil.getPage(getPageNo(), getPageSize());
		page = getManager().pageQuery(page, hql.toString(), args.toArray());
		restorePageData(page);
		if (exceptionFlag != null) {
			addActionError("数据已经使用，不能删除！");
		}

		return "adminIndex";
	}

	@Transactional
	@Override
	@SkipValidation
	public String remove() {
		List<Dicts> dicts = new ArrayList<Dicts>();
		if (selectedItems != null) {
			for (Serializable id : selectedItems) {
				Dicts d = getManager().get(Integer.parseInt(id.toString()));
				dicts.add(d);
			}
		} else {
			dicts.add(getManager().get(getModel().getId()));
		}
		for (Dicts d : dicts) {
			if (d.getKsId()!=null) {
				addActionError("所删除字典信息关联其他字典，删除失败！");
				return INDEX;
			} else {
				getManager().remove(d);
			}
		}
		String result = super.remove();
		return result;
	}

	/**
	 * 返回字典表类型Map
	 */
	public Map<String, String> getDictMap() {
		return DictConstants.DICT_MAP;
	}
	/**
	 * 动态补全查询所有对应的字典表
	 * @return
	 */
	public String getDictsByCode(){
		if(StringUtils.isNotBlank(dicttype)){
			List<Dicts> dictlist = getManager().getDictByCode(dicttype);
			for(Dicts dict : dictlist){
				alist.add(dict.getName());
			}
		}
		return "json";
	}
	public String getExceptionFlag() {
		return exceptionFlag;
	}

	public void setExceptionFlag(String exceptionFlag) {
		this.exceptionFlag = exceptionFlag;
	}

	public List<String> getAlist() {
    	return alist;
    }

	public void setAlist(List<String> alist) {
    	this.alist = alist;
    }

	public String getDicttype() {
    	return dicttype;
    }

	public void setDicttype(String dicttype) {
    	this.dicttype = dicttype;
    }
	
	
}
