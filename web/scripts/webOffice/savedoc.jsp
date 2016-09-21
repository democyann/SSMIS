<%@page import="java.io.File"%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/common/taglibs.jsp"%>
<%@ page language="java" import="com.jspsmart.upload.*"%>
<% StringBuffer URLTemp = request.getRequestURL();  
String URL = URLTemp.substring(0,URLTemp.lastIndexOf(":")).toString();  
String localPort = String.valueOf(request.getLocalPort());  
request.setAttribute("contextAllPath", URL+":"+localPort+request.getContextPath()); %>
<%
try{
	//上传下载前的初始化方法 
	SmartUpload su = new SmartUpload();
	su.initialize(pageContext);
	//设置不可以上传的文件后缀列表,没有后缀名的文件也不能上传，用,,来表示
	String denieddList="exe,bat,,";
	su.setDeniedFilesList(denieddList);
	//设置可以上传的文件
	String allowedList="doc,jpeg,png,jpg,docx";
	su.setAllowedFilesList(allowedList);
	//设定单个文件的最大值不超过10兆
	su.setMaxFileSize(1024*1024*10);
	su.upload();
	//Eclipse启动项目需要绝对路径，su参数为物理路径
	//部署启动项目可写相对路径，su参数为虚拟路径
	//String w = request.getAttribute("ctx") + "/uploads/fileattchs/";
	String w = com.systop.core.Constants.FILE_ATTACH_ROOT;
	Files files=su.getFiles();
	String destFileName =  su.getRequest().getParameter("fileName");
	File f = new File(w + destFileName);
	if(f.exists()){
		f.delete();
	}
	for(int i=0;i<files.getCount();i++){
		com.jspsmart.upload.File file=files.getFile(i);
		//判断有没有上传文件  
		if(file.isMissing()){
			continue;
		}
		//System.out.println(w+destFileName);
		file.saveAs(w + destFileName, SmartUpload.SAVE_PHYSICAL);
	}
		out.clear();
		out.write("succeed");
		out.flush();
	}catch(Exception e){
		e.printStackTrace();
		out.clear();
		out.write("failed");//返回控件HttpPost()方法值。
		out.flush();
	}%>