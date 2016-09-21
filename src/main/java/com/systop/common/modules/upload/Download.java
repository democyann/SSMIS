package com.systop.common.modules.upload;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
/**
 * 下载文件时还原为上传时的文件名称。
 * 
 * @author LinJie
 * 
 */
public final class Download {

	private static boolean m_denyPhysicalPath;

	private static String m_contentDisposition;

	public Download() {
		m_denyPhysicalPath = false;
		m_contentDisposition = new String();
	}	
	
	/**
	 * 文件下载
	 * @param sourceFilePathName  要下载的文件名（带目录的文件全名）
	 * @param contentType  下载文件类型（MIME格式的文件类型信息，可被浏览器识别）
	 * @param destFileName  下载后默认的文件名
	 * @param i  数组长度
	 * @param servletConfig  上下文环境
	 * @param response   响应
	 */
	public static void downloadFile(String sourceFilePathName,
			String contentType, String destFileName, int i,
			ServletContext servletConfig, HttpServletResponse response,HttpServletRequest request)
			throws ServletException, IOException {
		if (sourceFilePathName == null) {//验证下载文件名称是否正确
			throw new IllegalArgumentException((new StringBuilder("File '")).append(sourceFilePathName).append("' not found (1040).").toString());
		}
		if (sourceFilePathName.equals("")) {//验证下载文件名称是否正确
			throw new IllegalArgumentException((new StringBuilder("File '")).append(sourceFilePathName).append("' not found (1040).").toString());
		}
		if (!isVirtual(sourceFilePathName, servletConfig) && m_denyPhysicalPath) {//验证下载文件是否存在
			throw new SecurityException("Physical path is denied (1035).");
		}
		if (isVirtual(sourceFilePathName, servletConfig)) {
			sourceFilePathName = servletConfig.getRealPath(sourceFilePathName);
		}
		File file = new File(sourceFilePathName);
		FileInputStream fileinputstream = new FileInputStream(file);
		long l = file.length();
		int k = 0;
		byte abyte0[] = new byte[i];
		if (contentType == null) {
			response.setContentType("application/x-msdownload");
		} else if (contentType.length() == 0) {
			response.setContentType("application/x-msdownload");
		} else {
			response.setContentType(contentType);
		}
		response.setContentLength((int) l);
		m_contentDisposition = m_contentDisposition == null ? "attachment;"
				: m_contentDisposition;
		if (destFileName == null) {
			response.setHeader("Content-Disposition",
					(new StringBuilder(String.valueOf(m_contentDisposition)))	.append(" filename=").append(toUtf8String(getFileName(sourceFilePathName))).toString());
		} else if (destFileName.length() == 0) {
			response.setHeader("Content-Disposition", m_contentDisposition);
		} else {
            String agent = request.getHeader("USER-AGENT");
            if(agent.contains("MSIE"))
            {
            response.setHeader("Content-Disposition",
					(new StringBuilder(String.valueOf(m_contentDisposition))).append(" filename=").append(toUtf8String(destFileName)).toString());
            }else
            {
                response.setHeader("Content-Disposition",
                        (new StringBuilder(String.valueOf(m_contentDisposition))).append(" filename=").append("\"").append(new String(destFileName.getBytes("UTF-8"), "ISO8859-1")).append("\"").toString());
            }
		}
		while ((long) k < l) {
			int j = fileinputstream.read(abyte0, 0, i);
			k += j;
			OutputStream out = response.getOutputStream();
			out.write(abyte0, 0, j);
		}
		fileinputstream.close();
	}

	/**
	 * 转码
	 * @param s
	 * @return
	 */
	public static String toUtf8String(String s) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c >= 0 && c <= '\377') {
				sb.append(c);
			} else {
				byte b[];
				try {
					b = Character.toString(c).getBytes("utf-8");
				} catch (Exception ex) {
					System.out.println(ex);
					b = new byte[0];
				}
				for (int j = 0; j < b.length; j++) {
					int k = b[j];
					if (k < 0)
						k += 256;
					sb.append((new StringBuilder("%")).append(
							Integer.toHexString(k).toUpperCase()).toString());
				}
			}
		}

		return sb.toString();
	}

	/**
	 * 获取文件名
	 * @param s
	 * @return
	 */
	private static String getFileName(String s) {
		int i = 0;
		i = s.lastIndexOf('/');
		if (i != -1)
			return s.substring(i + 1, s.length());
		i = s.lastIndexOf('\\');
		if (i != -1)
			return s.substring(i + 1, s.length());
		else
			return s;
	}

	/**
	 * 验证文件是否存在
	 * @param s
	 * @param servletConfig
	 * @return
	 */
	private static boolean isVirtual(String s, ServletContext servletConfig) {
		if (servletConfig.getRealPath(s) != null) {
			File file = new File(servletConfig.getRealPath(s));
			return file.exists();
		} else {
			return false;
		}
	}

	public static void downloadFile(String string, String string2,
			String exportedName, int i, ServletContext servletContext,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		
	}
}
