package com.systop.common.modules.fileattch.webapp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.systop.common.modules.fileattch.model.FileAttch;
import com.systop.common.modules.fileattch.service.FileAttchManager;
import com.systop.common.modules.upload.Download;
import com.systop.core.Constants;
import com.systop.core.webapp.struts2.action.JsonCrudAction;
import com.systop.core.webapp.upload.UpLoadUtil;

@SuppressWarnings("serial")
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class FileAttchAction extends
		JsonCrudAction<FileAttch, FileAttchManager> {

	private File attch;

	private String attchFileName;

	private String attchFolder = Constants.FILE_ATTACH_ROOT;

	// id1,id2,id3 组成的查询字符串findFiles()
	private String fileIds;

	// 文件上传成功后异步返回ID
	private Integer handleFileId = null;

	// 异步操作返回结果
	private Map<String, Object> optResult = null;

	public String upLoadFile() {
		handleFileId = 0;
		if (attch != null) {
			FileAttch file = new FileAttch();
			String filePath = UpLoadUtil.doUpload(attch, attchFileName,
					attchFolder, getServletContext());
			file.setName(attchFileName);
			String ext = attchFileName
					.substring(attchFileName.lastIndexOf("."));
			file.setExt(ext);
			file.setPath(filePath);
			// 文件大小负值
			file.setTotalBytes(attch.length());
			file.setCreatetime(new Date());
			getManager().save(file);
			handleFileId = file.getId();
		}
		return "upload_success";
	}

	/**
	 * 下载文件，下载时把文件名称还原成原来的文件名
	 * @return
	 */
	public String downLoad(){
		try {
			Download.downloadFile(getModel().getPath(), getModel().getExt(), getModel().getName(), 65000, getServletContext(),getResponse(),getRequest());
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 异步删除删除附件
	 */
	public String ajaxRemove() {
		optResult = new HashMap<String, Object>();
		if (SUCCESS.equals(remove())) {
			optResult.put("result", "success");
			optResult.put("id", getModel().getId());
		} else {
			optResult.put("result", "error");
			optResult.put("id", getModel().getId());
		}
		return "ajax_delete";
	}

	/**
	 * 删除附件
	 */
	public String remove() {
		if (getModel() != null && getModel().getId() != null) {
			getManager().remove(getModel().getId(), getServletContext());
		}
		return SUCCESS;
	}

	/**
	 * 根据fileIds异步获得文件列表
	 * 
	 * @return
	 */
	public String findFiles() {
		try {
			optResult = new HashMap<String, Object>();
			optResult.put("result", "success");
			optResult.put("files", getManager().findFiles(fileIds));
		} catch (Exception e) {
			optResult.put("result", "error");
			optResult.put("error", e.getClass().toString());
		}
		return "find_files";
	}
	
	public List<FileAttch> findFilesByIds(String ids) {
		List<FileAttch> list=new ArrayList<FileAttch>();
		try {
			list=getManager().findFiles(ids);
		} catch (Exception e) {
			
		}
		return list;
	}

	public File getAttch() {
		return attch;
	}

	public void setAttch(File attch) {
		this.attch = attch;
	}

	public String getAttchFileName() {
		return attchFileName;
	}

	public void setAttchFileName(String attchFileName) {
		this.attchFileName = attchFileName;
	}

	public Integer getHandleFileId() {
		return handleFileId;
	}

	public Map<String, Object> getOptResult() {
		return optResult;
	}

	public void setFileIds(String fileIds) {
		this.fileIds = fileIds;
	}

}
