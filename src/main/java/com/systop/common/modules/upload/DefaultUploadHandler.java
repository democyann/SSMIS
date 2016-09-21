package com.systop.common.modules.upload;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.xwork.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.FileCopyUtils;

import com.systop.common.modules.security.user.model.User;
import com.systop.common.modules.security.user.service.UserManager;
import com.systop.core.ApplicationException;

/**
 * 把文件上传到本地文件系统。
 * 
 * @author catstiger@gmail.com
 * 
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class DefaultUploadHandler implements UploadHandler {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	/**
	 * 放置头像的路径
	 */
	private static final String HEAD_FOLDER = "cnhrhead";

	/**
	 * 用于存放头像的子目录
	 */
	private String headFolder = HEAD_FOLDER;

	/**
	 * 某些特殊用途的目录，位于用户目录下的一个子目录
	 */
	private String specialFolder;

	@Autowired
	private UserManager userManager;

	/**
	 * @see UploadHandler#save(java.io.File, String, Integer)
	 */
	@Override
	public File save(File uploaded, String filename, Integer userId) {
		Assert.notNull(userId);
		if (uploaded == null) {
			throw new ApplicationException("没有传上来文件呀，老大~~~");
		}

		String path = buildPath(userId);
		logger.debug("文件上传至{}", path);

		File file = new File(path + filename);
		try {
			FileCopyUtils.copy(uploaded, file); // 复制文件
			return file;
		} catch (IOException e) {
			logger.debug("保存上传文件时发生IOException {}", e.getMessage());
			e.printStackTrace();
			throw new ApplicationException("保存上传文件时发生I/O异常");
		}
	}

	@Override
	public void save(InputStream uploaded, String filename, Integer userId) {
		Assert.notNull(userId);
		if (uploaded == null) {
			throw new ApplicationException("没有传上来文件呀，老大~~~");
		}

		String path = buildPath(userId);
		try {
			OutputStream out = new BufferedOutputStream(new FileOutputStream(
					new File(path + filename)));
			FileCopyUtils.copy(uploaded, out); // 复制文件
		} catch (IOException e) {
			logger.debug("保存上传文件时发生IOException {}", e.getMessage());
			e.printStackTrace();
			throw new ApplicationException("保存上传文件时发生I/O异常");
		}
	}

	/**
	 * @see UploadHandler#get(String, Integer)
	 */
	@Override
	public InputStream get(String filename, Integer userId) {
		Assert.notNull(userId);
		String path = buildPath(userId);
		File file = new File(path + filename);
		if (file.exists() && file.isFile()) {
			try {
				return new BufferedInputStream(new FileInputStream(file));
			} catch (FileNotFoundException e) {
				logger.debug("没有文件{}", file.getAbsolutePath());
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * @see UploadHandler#isTooMuch(Integer)
	 */
	@Override
	public boolean isTooMuch(Integer userId) {
		User user = userManager.get(userId);
		if (user == null) {
			throw new ApplicationException("用户" + userId + "不存在，更别提文件了！");
		}

		Integer tmp = 10;
		int maxFiles = (tmp == null) ? 0 : tmp.intValue();
		return count(userId) >= maxFiles;
	}

	/**
	 * @see UploadHandler#list(Integer)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<File> list(Integer userId) {
		Assert.notNull(userId);
		List<File> files = null;
		String path = buildPath(userId);
		File file = new File(path);
		if (file.exists() && file.isDirectory()) {
			files = Arrays.asList(file.listFiles(new FileFilter() {
				@Override
				public boolean accept(File pathname) { // 只列出文件，不列出目录
					return pathname.isFile();
				}
			}));
		}
		return (files == null) ? Collections.EMPTY_LIST : files;
	}

	/**
	 * @see UploadHandler#count(Integer)
	 */
	@Override
	public int count(Integer userId) {
		Assert.notNull(userId);

		return list(userId).size();
	}

	/**
	 * 如果path后面没有{@link File#separator},则追加
	 * 
	 * @param rootPath
	 *            the rootPath to set
	 */
	private static String fixRootPath(String rootPath) {
		Assert.isTrue(StringUtils.isNotBlank(rootPath));
		if (!rootPath.endsWith(File.separator)) {
			rootPath += File.separator;
		}
		return rootPath;
	}

	/**
	 * 创建目录,该目录位于当前操作系统用户目录下的cnhr目录下。如果给出了{@link #specialFolder}, 则在此目录下创建
	 * {@link #specialFolder}
	 * 
	 * @param userId
	 * @return 创建目录的完整路径，末尾带有{@link File#separator}
	 */
	protected String buildPath(Integer userId) {
		File rootPath = new File(getRootPath());
		if (!rootPath.exists()) {
			rootPath.mkdirs();
		}
		StringBuilder sPath = new StringBuilder(fixRootPath(rootPath
				.getAbsolutePath())).append(userId.toString());
		if (StringUtils.isNotBlank(specialFolder)) {
			sPath.append(File.separator).append(specialFolder).append(
					File.separator);
		}
		File path = new File(sPath.toString());

		if (path.exists() && path.isFile()) { // 如果存在，并且是一个文件，则删除
			path.delete();
		}

		if (!path.exists()) { // 如果目录不存在，则创建
			path.mkdirs();
		}
		logger.debug("用户{}的文件目录为{}", userId, path.getAbsolutePath());

		return fixRootPath(path.getAbsolutePath());
	}

	/**
	 * 得到保存文件的根目录。 这个目录的末尾是有{@link File#separator}的。
	 */
	public static String getRootPath() {
		String path = new StringBuilder(System.getProperty("user.home"))
				.append(
						ResourceBundle.getBundle("red5").getString(
								"webapp.contextPath")).toString();
		return fixRootPath(path);
	}

	/**
	 * @see UploadHandler#remove(String, Integer)
	 */
	@Override
	public void remove(String filename, Integer userId) {
		String path = buildPath(userId);
		boolean success = false;
		File file = new File(path + filename);
		if (file.exists()) {
			success = file.delete();
		}
		if (!success) {
			logger.warn("用户{}的文件{}不存在.", userId, filename);
		}
	}

	/**
	 * @see UploadHandler#removeAll(Integer)
	 */
	@Override
	public void removeAll(Integer userId) {
		String path = buildPath(userId);
		File file = new File(path);
		if (!file.exists()) {
			logger.warn("用户目录{}不存在。", file.getAbsolutePath());
		}

		try {
			FileUtils.cleanDirectory(file);
		} catch (IOException e) {
			logger.warn("清空用户目录{}的时候发生IO错误{}", file.getName(), e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public String getUserPath(Integer userId) {
		return buildPath(userId);
	}

	public static String userPath(Integer userId) {
		return new DefaultUploadHandler().getUserPath(userId);
	}

	/**
	 * 头像目录
	 */
	public String headPath(Integer userId) {
		StringBuilder path = new StringBuilder(DefaultUploadHandler
				.getRootPath()).append(userId).append(File.separator).append(
				headFolder).append(File.separator);
		File file = new File(path.toString());
		if (!file.exists()) {
			file.mkdirs();
		}
		return path.toString();
	}

	/**
	 * 取得照片路径
	 * 
	 * @param userId
	 *            用户id
	 * @param photoName
	 *            照片名称
	 * @param isThumbnail
	 *            是否缩略图
	 * @return
	 */
	public String getPhotoPath(Integer userId, String photoName,
			boolean isThumbnail) {
		return null;
		/*
		 * StringBuilder path = new
		 * StringBuilder(1000).append(buildPath(userId))
		 * .append(File.separator); if(isThumbnail) {
		 * path.append(Thumbnail.THUMBNAIL_FOLDER).append(File.separator); }
		 * path.append(photoName);
		 * 
		 * return path.toString();
		 */
	}

	/**
	 * @param headFolder
	 *            the headFolder to set
	 */
	public void setHeadFolder(String headFolder) {
		this.headFolder = headFolder;
	}

	/**
	 * @param specialFolder
	 *            the specialFolder to set
	 */
	public void setSpecialFolder(String specialFolder) {
		this.specialFolder = specialFolder;
	}

}
