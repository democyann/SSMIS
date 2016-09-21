package com.systop.common.modules.upload;

import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * 处理通过视频服务传上来的文件。
 * 
 * @author catstiger@gmail.com
 * 
 */
public interface UploadHandler {
	/**
	 * 将用户通过视频服务传上来的文件保存到以用户的id命名的目录下。
	 * 
	 * @param uploaded
	 *            上传的文件
	 * @param filename
	 *            保存后的文件名
	 * @param userId
	 *            用户ID
	 * @return 返回保存后的file对象
	 * @see {@link com.googlecode.jtiger.modules.security.user.model.User#geId()}
	 */
	public File save(File uploaded, String filename, Integer userId);

	/**
	 * 将用户通过视频服务传上来的文件保存到以用户的id命名的目录下。
	 * 
	 * @param uploaded
	 *            上传的文件
	 * @param filename
	 *            保存后的文件名
	 * @param userId
	 *            用户ID
	 * @see {@link com.googlecode.jtiger.modules.security.user.model.User#geId()}
	 */
	public void save(InputStream uploaded, String filename, Integer userId);

	/**
	 * 从用户的目录（以用户id命名）下取得某个文件
	 * 
	 * @param filename
	 *            文件名
	 * @param userId
	 *            用户id
	 * @return 返回{@link InputStream},如果没有，则返回<code>null</code>
	 */
	public InputStream get(String filename, Integer userId);

	/**
	 * 判断某个用户的用户目录下的文件是否已经达到上限
	 * 
	 * @param userId
	 *            用户id
	 * @return
	 */
	public boolean isTooMuch(Integer userId);

	/**
	 * 列出某个目录（以用户id命名）下所有的文件
	 * 
	 * @param userId
	 *            给出用户id
	 * @return List of files in given folder, if none, return
	 *         {@link java.util.Collections#EMPTY_LIST}
	 */
	public List<File> list(Integer userId);

	/**
	 * 得到某个目录（以用户id命名）下，文件的数量
	 * 
	 * @param userId
	 *            给出用户id
	 * @return Amount of files in given folder, if none, return <code>0</code>
	 */
	public int count(Integer userId);

	/**
	 * 删除某个用户的单个文件
	 * 
	 * @param filename
	 *            给出文件名
	 * @param userId
	 *            给出用户名
	 */
	public void remove(String filename, Integer userId);

	/**
	 * 删除某个用户名下所有文件
	 * 
	 * @param userId
	 *            给出用户名
	 */
	public void removeAll(Integer userId);

	/**
	 * 返回用户存放文件的目录
	 */
	public String getUserPath(Integer userId);

	/**
	 * 返回头像目录，位于用户目录下的一个子目录
	 */
	public String headPath(Integer userId);

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
			boolean isThumbnail);

	/**
	 * 指定一个子目录，用于专门的用途。
	 * 
	 * @param specialFolder
	 */
	public void setSpecialFolder(String specialFolder);

}
