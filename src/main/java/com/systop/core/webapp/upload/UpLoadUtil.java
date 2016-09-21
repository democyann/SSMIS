package com.systop.core.webapp.upload;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletContext;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.FileCopyUtils;

import com.systop.core.util.DateUtil;

/**
 * 文件上传util
 * 
 * @author Lunch
 */
public final class UpLoadUtil {

  private UpLoadUtil() {
  };

  /**
   * 处理上传文件，将上传的文件另存到某一目录下。
   * 
   * @param file 上传文件对象，struts2将上传的文件保存到一个临时目录。
   * @param fileName 另存的文件名。
   * @param folderName 另存的目录名。
   * @param isReName 是否重命名
   * @return 返回完整的文件名，如果失败，返回<code>null</code>
   */
  public static String doUpload(File file, String fileName, String folderName, ServletContext sctx,
      boolean isReName) {
    if (file == null || StringUtils.isBlank(fileName)) {
      return null;
    }
    String folderPath = folderName.replace('/', File.separatorChar);
    if(folderPath.endsWith(File.separator)){
		folderPath = folderPath.substring(0, folderPath.length() - 1);
    }
    File folder = new File(folderPath);
    if(!folder.getAbsolutePath().equals(folderPath)){
    	folderPath = sctx.getRealPath(folderName);
    	folder = new File(folderPath);
    }
//    String folderPath = sctx.getRealPath(folderName);
//    File folder = new File(folderPath);
    if (!folder.exists()) {
      folder.mkdirs();
    }

    String destFileName;

    // 是否重命名
    if (isReName) {
      // 扩展名
      String ext = fileName.substring(fileName.lastIndexOf("."));
      // 重新命名
      final int randomLength = 3;
      destFileName = DateUtil.getDateTime("yyyyMMddhhmmss", new Date())
          + RandomStringUtils.randomNumeric(randomLength) + ext;
    } else {
      destFileName = fileName;
    }

    File dest = new File(folderPath + File.separatorChar + destFileName);
    try {
      FileCopyUtils.copy(file, dest);
      return (folderName + destFileName).replaceAll("\\\\", "/");
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * 处理上传文件，将上传的文件另存到某一目录下。
   * 
   * @param file 上传文件对象，struts2将上传的文件保存到一个临时目录。
   * @param fileName 另存的文件名。
   * @param folderName 另存的目录名。
   * @return 返回完整的文件名，如果失败，返回<code>null</code>
   */
  public static String doUpload(File file, String fileName, String folderName, ServletContext sctx) {
    return doUpload(file, fileName, folderName, sctx, true);
  }
}
