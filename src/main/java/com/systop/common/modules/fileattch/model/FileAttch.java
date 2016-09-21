package com.systop.common.modules.fileattch.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.systop.core.Constants;
import com.systop.core.model.BaseModel;

@Entity
@Table(name = "file_attchs")
@SuppressWarnings("serial")
public class FileAttch extends BaseModel {

	// 主键
	private Integer id;

	// 文件名
	private String name;

	// 文件路径
	private String path;

	// 创建时间
	private Date createtime;

	// 扩展名
	private String ext;

	// 类型
	private String fileType;

	// 是否删除
	private String isDelete = Constants.NO;

	// 创建人
	private String creator;

	// 文件大小Bytes
	private Long totalBytes;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	@Column(name = "ext", length = 10)
	public String getExt() {
		return ext;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}

	@Column(name = "file_type", length = 30)
	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	@Column(name = "is_delete", length = 1)
	public String getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(String isDelete) {
		this.isDelete = isDelete;
	}

	@Column(name = "creator", length = 64)
	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Long getTotalBytes() {
		return totalBytes;
	}

	public void setTotalBytes(Long totalBytes) {
		this.totalBytes = totalBytes;
	}

}
