package com.systop.core.webapp.struts2.upload;

import java.io.File;
/**
 * 用于处理Struts2文件上传的接口。
 * @author Sam Lee
 *
 */
public interface UploadedFileHandler {

  /**
   * 处理Struts2上传之后的文件。Struts2将上传的文件保存在一个
   * 目录下(在struts.properties中指定)，<code>doUpload()</code>
   * 就是处理这个上传的临时文件.
   * 
   * @param source 临时文件.
   * @param originalName 文件的原始名称，用于获取扩展名。
   * @return 返回处理之后的文件对象.
   */
  File doUpload(File source, String originalName);
  /**
   * 设置保存文件的目标路径
   * @param targetPath
   */
  void setTargetPath(String targetPath) ;
  /**
   * 设置保存文件的重命名
   */
  void setRename(String rename);

}