package com.systop.common.modules.fileattch.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.systop.common.modules.fileattch.FileConstants;
import com.systop.common.modules.fileattch.model.FileAttch;
import com.systop.core.Constants;
import com.systop.core.service.BaseGenericsManager;

/**
 * 附件管理类
 * 
 * @author Nice
 */
@Service
public class FileAttchManager extends BaseGenericsManager<FileAttch> {

	/**
	 * 根据字符串'id'更新相关的附件的'type'。
	 * 
	 * @param ids
	 *            主键字符串,由一个或多个主键组成。例如：'1,2,3,'。该参数可以为null
	 * @param fileType
	 *            文件类别
	 */
	@Transactional
	public void updateFileType(String ids, String fileType) {
		if (StringUtils.isNotBlank(ids)) {
			String[] strIds = ids.split(FileConstants.SPLIT_REGEX);
			for (String strId : strIds) {
				if (StringUtils.isNumeric(strId)) {// 判断字符串是否为数字
					FileAttch file = get(Integer.valueOf(strId));// 获取附件信息
					if(file == null)
						return;
					file.setFileType(fileType);// 设置类别
					save(file);// 保存类别信息
				}
			}
		}
	}

	/**
	 * 根据字符串'ids'删除相关附件。
	 * 
	 * @param ids
	 *            主键字符串,由一个或多个主键组成。例如：'1,2,3,'。该参数可以为null
	 */
	public void removeByIds(String ids, ServletContext context) {
		if (StringUtils.isNotBlank(ids) && context != null) {
			String[] strIds = ids.split(FileConstants.SPLIT_REGEX);
			for (String strId : strIds) {
				if (StringUtils.isNumeric(strId)) {// 判断字符串是否为数字
					remove(Integer.valueOf(strId), context);
				}
			}
		}
	}

	/**
	 * 删除一个附件，同时删除物理文件，如果物理文件删除失败将附件信息标识为
	 */
	@Transactional
	public void remove(Integer id, ServletContext context) {
		FileAttch fileAttch = get(id);
		if (fileAttch != null) {
			String path = fileAttch.getPath();
			File file = new File(path);
			if (!file.exists()) {
				path = context.getRealPath(path);
				file = new File(path);
			}
			if (file.exists()) {// 判断文件是否存在
				if (!file.delete()) {
					// 如果文件删除失败需要记录吗？应该需要吧，得先记录下，以后再删
					fileAttch.setIsDelete(Constants.YES);
					save(fileAttch);
				}
			}
			super.remove(fileAttch);
		}
	}
	
	/**
	 * 根据字符串'strIds'查询获得相关附件。
	 * 
	 * @param ids
	 *            主键字符串,由一个或多个主键组成。例如：'1,2,3,'。该参数可以为null
	 */
	public List<FileAttch> findFiles(String strIds){
		List<FileAttch> files = null;
		if (StringUtils.isNotBlank(strIds)){
			String[] ids = strIds.split(FileConstants.SPLIT_REGEX);
			files = new ArrayList<FileAttch>();
			for (String id : ids) {
				if (StringUtils.isNumeric(id)) {
					FileAttch file = get(Integer.valueOf(id));
					if (file != null) {
						files.add(file);
					}
				}
			}
			
		}
		return files;
	}
	
	/**
	 * 根据字符串'id'查询获得第一个附件。
	 * 
	 * @param ids
	 *            主键字符串,由一个或多个主键组成。例如：'1,2,3,'。该参数可以为null
	 */
	public FileAttch findFirst(String strIds){
		FileAttch file = null;
		if (StringUtils.isNotBlank(strIds)){
			String id = strIds.split(FileConstants.SPLIT_REGEX)[0];
			if (StringUtils.isNumeric(id)){
				file = get(Integer.valueOf(id));
			}
		}
		return file;
	}

	/**
	 * 根据上传的附件Id更新附件表的Type
	 */
  public FileAttch getFileAttch(String attachId) {
    String hql = "from FileAttch fa where fa.id = ? ";
    attachId = attachId.replace("\"", "");
    return (FileAttch) getDao().findObject(hql, new Object[] { Integer.parseInt(attachId) });
  }

  /**
	 * 删除type为空的附件
	 */
	@Transactional
	public void removeByType(){
		ServletContext context = ServletActionContext.getServletContext();
		List<FileAttch> faList = query("from FileAttch fa where fa.fileType is null or fa.fileType = ?","");
		if(!faList.isEmpty()){
			for(FileAttch fa : faList){
				if(StringUtils.isBlank(fa.getPath())){
					remove(fa);
					continue;
				}
				File f = new File(context.getRealPath(fa.getPath()));
				if(f!=null&&f.exists()){
					f.delete();
				}
				remove(fa);
			}
		}
	}
}
