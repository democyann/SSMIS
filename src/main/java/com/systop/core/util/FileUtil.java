package com.systop.core.util;

import java.io.File;
import java.text.DecimalFormat;

public final class FileUtil {
  public static String formatSize(File file) {
    if(file == null || !file.exists() || file.isDirectory()) {
      return "0KB";
    }
    double size = new Double(file.length());
    
    if(size < 1024) {
      return size + "B";
    } else if(size < 1024 * 1024) {
      double s = size / 1024;
      return new DecimalFormat("####.0").format(s) + "KB";
    } else if (size < 1024 * 1024 * 1024) {
      double s = size / (1024 * 1024);
      return new DecimalFormat("####.0").format(s) + "MB";
    }
    return "0KB";
  }
  
  private FileUtil(){    
  }
}
