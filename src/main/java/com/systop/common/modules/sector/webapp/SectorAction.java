package com.systop.common.modules.sector.webapp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.hibernate.criterion.MatchMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;
import com.systop.common.modules.sector.SectorConstants;
import com.systop.common.modules.sector.model.Sector;
import com.systop.common.modules.sector.service.SectorManager;
import com.systop.common.modules.sector.service.SectorSerialNoManager;
import com.systop.common.modules.dict.model.Dicts;
import com.systop.common.modules.dict.service.DictsManager;
import com.systop.common.modules.security.user.LoginUserService;
import com.systop.common.modules.security.user.model.User;
import com.systop.common.modules.security.user.service.UserManager;
import com.systop.core.dao.support.Page;
import com.systop.core.util.ReflectUtil;
import com.systop.core.util.RequestUtil;
import com.systop.core.webapp.struts2.action.ExtJsCrudAction;

/**
 * 部门管理Action
 * 
 * @author Sam Lee
 * 
 */
@SuppressWarnings( { "serial", "unchecked","rawtypes"})
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class SectorAction extends ExtJsCrudAction<Sector, SectorManager> {
	/**
	 * 当前上级部门ID
	 */
	private String parentId;
	// 除去部门管理以外，页面显示部门标识
	private String noLowerSector;
	//部门类型
	private DictsManager dictsManager;

	/**
	 * 部门序列号管理器
	 */
	private SectorSerialNoManager serialNoManager;
	
	/**
	 *获得当前登陆部门
	 */
	@Autowired
	private LoginUserService loginUserService;
	
	@Autowired
	private UserManager userManager;
	
	@Autowired
	private SectorManager sectorManager;
	
	private Integer sectorDictName;

	/**
	 * 用于查询的部门名称
	 */
	private String sectorName = StringUtils.EMPTY;

	private List sectors;

	/**
	 * 部门查询。根据指定的上级部门id(通过{@link #parentId}属性)，查询下级部门。 如果{@link #parentId}
	 * 为null,则查询顶级部门（没有上级部门的）
	 */
	@Override
	@SkipValidation
	public String index() {
			return INDEX;
	}

	/**
	 * Build a tree as json format.
	 */
	public String sectorTree() {
		if (RequestUtil.isJsonRequest(getRequest())) {
			List<Sector> parent = new ArrayList<Sector>();
			
			//当前登陆用户 注意admin部门为空
			/*Sector sector = loginUserService.getLoginUserDept(getRequest());
			//部门编辑，用户添加使用
			if (StringUtils.isNotBlank(parentId)) {
				Sector d = getManager().get(parentId);
				parent.add(d);
			} else {// 部门列表树显示使用
				if (sector != null && sector.getParentSector() != null) {
					// 显示当前登陆用户部门
					parent.add(sector);
				} else {
					// 显示所有部门
					parent = getManager().getDao().query("from Sector d where d.parentSector is null ");
					
				}
			}*/
			parent = getManager().getDao().query("from Sector d where d.parentSector is null ");
			Map parentMap = null;
			sectors = new ArrayList();
			if (!parent.isEmpty()) {
				for(Sector sectorModel : parent){
				parentMap = new HashMap();
				parentMap.put("id", sectorModel.getId());
				parentMap.put("text", sectorModel.getName());
				parentMap.put("code", sectorModel.getCode());
				parentMap.put("descn", sectorModel.getDescn());
				Map sectorTree = getSectorTree(parentMap, true);
				sectors.add(sectorTree);
				}
			}
			return JSON;
		}
		return INDEX;
	}

	/**
	 * 根据指定的父部门id查询子部门
	 */
	private List<Sector> getByParentId(String parentSectorId) {
		List list = Collections.EMPTY_LIST;
		if (StringUtils.isBlank(parentSectorId)
				|| parentSectorId.equals(SectorConstants.TOP_DEPT_ID)) {
			list = getManager().query("from Sector d where d.parentSector is null");
		} else {
			list = getManager().query("from Sector d where d.parentSector.id = ?",
					parentSectorId);
		}

		return list;
	}

	/**
	 * 返回部门树形列表，每一个部门用一个<code>java.util.Map</code>表示，子部门
	 * 用Map的“childNodes”key挂接一个<code>java.util.List</code>.<br>
	 * 本方法供DWR调用，Map中key符合jsam dojo Tree的要求。
	 * 
	 * @param parent
	 *            父部门，如果为null，则表示顶级部门
	 * @param nested
	 *            是否递归查询子部门，true表示递归查询子部门
	 * @return
	 */
	public Map getSectorTree(Map parent, boolean nested) {
		List<Sector> sectors;
		if (parent == null || parent.isEmpty() || parent.get("id") == null) {
			return null;
		}
		// 得到子部门，区县用户直接返回，市级或admin用户查询24个区县
		if (noLowerSector == null) {
			sectors = this.getByParentId((String) parent.get("id"));
		} else {
			Sector sector = getManager().get((String) parent.get("id"));
			if (sector.getParentSector() == null) {
				sectors = this.getByParentId((String) parent.get("id"));
			} else {
				parent.put("leaf", true);
				return parent;
			}
		}

		logger.debug("Sector {} has {} children.", parent.get("text"), sectors
				.size());
		// 转换所有子部门为Map对象，一来防止dwr造成延迟加载，
		// 二来可以符合Ext的数据要求.
		List children = new ArrayList();
		for (Iterator<Sector> itr = sectors.iterator(); itr.hasNext();) {
			Sector sector = itr.next();
			Map child = new HashMap();
			child.put("id", sector.getId());
			child.put("text", sector.getName());
			child.put("descn", sector.getDescn());
			child.put("type", sector.getType());
			child.put("code", sector.getCode());
			// noSonSector不为空，无须递归查询
			if (noLowerSector == null) {
				if (nested) { // 递归查询子部门
					child = this.getSectorTree(child, nested);
				}
			}
			// 标识当前节点为叶子节点，其实是区县部门
			if (noLowerSector != null) {
				child.put("leaf", true);
			}
			children.add(child);

		}
		// noSonSector不为空，代表事件或部门不需要查看下级部门
		if (!children.isEmpty() || noLowerSector != null) {
			parent.put("children", children);
			parent.put("childNodes", children);
			parent.put("leaf", false);
		} else {
			parent.put("leaf", true);
		}

		return parent;
	}

	/**
	 * 删除数据，异步调用
	 */
	@Override
	public String remove() {
		if (ArrayUtils.isEmpty(selectedItems)) {
			if (getModel() != null) {
				Serializable id = extractId(getModel());
				if (id != null) {
					selectedItems = new Serializable[] { id };
				}
			}
		}
		if (selectedItems != null) {
			for (Serializable id : selectedItems) {
				if (id != null) {
					Sector sector = getManager().get(convertId(id));
					if (sector != null) {
						List<User> list = userManager.getUsersBySectorId(sector.getId());
						// 判断此类型下是否有商品存在如果有不可删除		
						if (list != null && list.size() > 0) {
							this.addActionError("该部门下已经绑定员工,不可删除!");
							return INDEX;}
						if(!sector.getChildSectors().isEmpty())
						{
							this.addActionError("该部门含有子部门，不可删除");
							return INDEX;
						}
						 else {
							getManager().remove(sector);
						}
					} else {
						logger.debug("试图删除null数据.");
					}
				}
			}

			logger.debug("{} items removed.", selectedItems.length);
		}
		return SUCCESS;
	}
	
	/**
	 * @return 当前上级部门ID
	 */
	public String getParentId() {
		return parentId;
	}

	/**
	 * @param parentId
	 *            the parentId to set
	 */
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	/**
	 * 得到当前部门（通过{@link #parentId}指定）的上级部门.
	 */
	public Sector getParent() {
		if (parentId == null || parentId.equals(SectorConstants.TOP_DEPT_ID)) {
			Sector sector = new Sector();
			sector.setName(SectorConstants.TOP_DEPT_NAME);
			return sector;
		}

		return getManager().get(parentId);
	}

	/**
	 * @return the sectorName
	 */
	public String getSectorName() {
		return sectorName;
	}

	/**
	 * @param sectorName
	 *            the sectorName to set
	 */
	public void setSectorName(String sectorName) {
		this.sectorName = sectorName;
	}

	/**
	 * 覆盖父类，处理父部门ID为{@link SectorConstants#TOP_DEPT_ID}的情况。
	 */
	@Override
	@Validations(requiredStrings = { @RequiredStringValidator(type = ValidatorType.SIMPLE, fieldName = "model.name", message = "部门名称是必须的.") })
	public String save() {
		//验证部门编码
		if (StringUtils.isBlank((getModel().getCode()))) {
		  addActionError("请编辑部门代码！");
		  return INPUT;
	    }
		//验证部门编码是否重复
		if(getManager().getDao().exists(getModel(), "code")){
			addActionError("部门编码已存在");
			return INPUT;
		}
		//如何部门类型默认是"--请选择--"
		if (getModel().getSectorType() != null&&getModel().getSectorType().getId()==0){
			getModel().setSectorType(null);
		}
		// 如果页面选择了顶级部门作为父部门，则设置父部门为null
		if (getModel().getParentSector() != null
				&&getModel().getSectorType().getId()==0
				&& getModel().getParentSector().getId().equals(SectorConstants.TOP_DEPT_ID)) {
			getModel().setParentSector(null);
		}
		if (getModel().getParentSector() == null
				|| getModel().getSectorType().getId()==0) {
			//限制添加两个根部门 2009-12-28
			getModel().setParentSector(null);
			logger.debug("保存第一 级部门.");
		}
		
		
		  if(getModel().getId().equals(getModel().getParentSector().getId())){
			this.addActionError("上级部门不能是自身！");
			return INPUT;
		
		}
    getManager().getDao().clear();
		return super.save();
	}

	/**
	 * 处理parentSector为null的情况
	 */
	@Override
	@SkipValidation
	public String edit() {
		List<Dicts> dicts = dictsManager.query("from Dicts d where d.name = ?", "学校");
		if(dicts.size() != 0){
			sectorDictName = dicts.get(0).getId();
		}
		
			setModel(getManager().get(getModel().getId()));
			if (getModel().getParentSector() == null) {
				Sector sector = new Sector(); // 构建一个父部门
				sector.setId(SectorConstants.TOP_DEPT_ID);
				sector.setName(SectorConstants.TOP_DEPT_NAME);
				getModel().setParentSector(sector);
				getManager().getDao().evict(getModel()); // 将sector脱离hibernate
				logger.debug("编辑第一级部门");
			}
		
		return INPUT;
	}
	
	@Override
	public String editNew(){
		List<Dicts> dicts = dictsManager.query("from Dicts d where d.name = ?", "学校");
		if(dicts.size() != 0){
			sectorDictName = dicts.get(0).getId();
		}
		return INPUT;
	}
	
	
	public String getAllSchool(){
		StringBuffer hql = new StringBuffer("from Sector d where d.sectorType.code = ?");
		List args = new ArrayList();
		args.add("school");
		
		//按照学校名称取出学校
		if(getModel() != null && !StringUtils.isBlank(getModel().getName())){
			hql.append(" and d.name like ?");
			args.add(MatchMode.ANYWHERE.toMatchString(getModel().getName()));
		}
		//获得所有的学校
		List<Sector> sectors = sectorManager.query(hql.toString(),args.toArray());
		page = new Page(Page.start(getPageNo(), getPageSize()), getPageSize());
	  List mapSectors = new ArrayList(sectors.size());
		for (Iterator itr = sectors.iterator(); itr.hasNext();) {
			Sector sector = (Sector) itr.next();
			// 转换为Map，防止延迟加载
			Map mapSector = ReflectUtil.toMap(sector, new String[] { "id", "name",
					"descn" }, true);
			mapSector.put("changed", sector.getChanged());
			mapSectors.add(mapSector);
		}
		page.setData(mapSectors);
		return "allSchool";
	}
	

	/**
	 * 重置所有部门编号
	 */
	@SkipValidation
	public String updateSerialNo() {
		serialNoManager.updateAllSerialNo();
		return SUCCESS;
	}

	public SectorSerialNoManager getSerialNoManager() {
		return serialNoManager;
	}

	@Autowired(required = true)
	public void setSerialNoManager(SectorSerialNoManager serialNoManager) {
		this.serialNoManager = serialNoManager;
	}
	
	/*部门类型*/
	public Map<String,String> getSectorTypeMap(){
		List<Dicts> sectorList = dictsManager.getDictsByCategory("sector");
		Map sectorTypeMap = new LinkedHashMap();
		for(Dicts dict : sectorList){
			sectorTypeMap.put(dict.getId(), dict.getName());
		}
		return sectorTypeMap;
	}
	
	/*学校类型
	 * */
	public Map<String,String> getSchoolMap(){
		return SectorConstants.SCHOOLTYPE_MAP;
	}

	public List getSectors() {
		return sectors;
	}

	public void setSectors(List sectors) {
		this.sectors = sectors;
	}

	public String getNoLowerSector() {
		return noLowerSector;
	}

	public void setNoLowerSector(String noLowerSector) {
		this.noLowerSector = noLowerSector;
	}

	public UserManager getUserManager() {
		return userManager;
	}
	
	public DictsManager getDictsManager() {
		return dictsManager;
	}

	public void setDictsManager(DictsManager dictsManager) {
		this.dictsManager = dictsManager;
	}

	public Integer getSectorDictName() {
		return sectorDictName;
	}

	public void setSectorDictName(Integer sectorDictName) {
		this.sectorDictName = sectorDictName;
	}

}
