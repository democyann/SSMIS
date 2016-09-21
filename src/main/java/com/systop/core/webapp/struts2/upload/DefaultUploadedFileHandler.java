package com.systop.core.webapp.struts2.upload;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;
import org.springframework.util.FileCopyUtils;

import com.systop.core.util.DateUtil;

public class DefaultUploadedFileHandler implements UploadedFileHandler {
  /**
   * Logger
   */
  private static final Log logger = LogFactory.getLog(DefaultUploadedFileHandler.class);
  /**
   * 目标目录
   */
  private String targetPath;

  /**
   * 重命名，不包括扩展名
   */
  private String rename;

  public File doUpload(File source, String originalName) {
    Assert.hasText(targetPath, "Please setup target path.");
    Assert.notNull(source, "Uploaded file must not be null.");
    Assert.isTrue(source.exists(), "Uploaded file must exist.");
    String ext = originalName;
    if (originalName.indexOf(".") > 0) {
      ext = originalName.substring(originalName.lastIndexOf("."));
    }
    if (StringUtils.isBlank(rename)) {
      rename = DateUtil.getDateTime("yyyyMMddhhmmss", new Date())
          + RandomStringUtils.randomNumeric(3);
    }
    File dest = new File(new StringBuffer(targetPath)
        .append(File.separatorChar).append(rename).append(ext).toString());
    try {
      FileCopyUtils.copy(source, dest);
      return dest;
    } catch (IOException e) {
      logger.error(e.getMessage());
      return null;
    }    
  }

 
  public void setTargetPath(String targetPath) {
    this.targetPath = targetPath;
    File folder = new File(targetPath);
    if (!folder.exists()) {
      folder.mkdirs();
    }
  }

  public void setRename(String rename) {
    this.rename = rename;
  }
}
