<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<title>密码修改成功</title>
	<%@include file="/common/meta.jsp"%>
</head>
<body>
<div class="x-toolbar" style="padding: 5px; font-weight: bold;">密码修改成功</div>
<fieldset style="margin: 10px;">
	<table width="400" align="center" style="margin: 25px auto;">
	  <tr>
	    <td width="80">
	    	<img src="${ctx}/images/icons/ok.png"></td>
	    <td width="320">
	    	<div style="color: green; font-size: 18px; font-weight: bold; line-height: 25px;">用户密码修改成功!</div>
	    	<div style="color:green; line-height: 25px;">以后登录请使用新密码</div>
	    </td>
	  </tr>
	</table>
</fieldset>
<div align="center">
	<input type="button" class="button" value="关闭窗口" onclick="window.close();">
</div>
</body>
</html>