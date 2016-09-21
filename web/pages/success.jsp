<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@include file="/common/meta.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>无标题文档</title>
<style type="text/css">
.greenfont { font-size: 24px; font-weight: bold; color: #40B100; }
</style>
</head>

<body>
<table style="height: 200px; width: 500px;border: 5px solid #C6DEEC; background-color: #F6FBFF;margin-top: 100px;" align="center">
  <tr><td>
	  <table width="250" border="0" align="center" cellpadding="0" cellspacing="0" style="font-family: Verdana, Geneva, sans-serif; font-size: 12px;">
	    <tr>
	      <td width="76" height="80"><img src="${ctx}/images/icons/duigou.gif" width="63" height="56" /></td>
	      <td width="224" class="greenfont">操作成功！</td>
	    </tr>
	    <tr>
	      <td width="76" height="69">&nbsp;</td>
	      <td width="224" style="line-height:25px;">
		      <c:choose>
		      	<c:when test="${param.accessStatus  != null && param.accessStatus != '' && param.url  != null && param.url != ''}">
		     			<strong>相关操作：</strong><br />
		     		<a href="${ctx}${param.url}&accessStatus=${param.accessStatus}">返回</a><br />
		     	</c:when>
		      	<c:when test="${param.url  != null && param.url != ''}">
		      		<strong>相关操作：</strong><br />
		     		<a href="${ctx}${param.url}">返回</a><br />      		      	
		      	</c:when>
		      </c:choose>
<!-- 	       		<a href="#" onclick="history.go(-1)">返回</a><br />	      	 -->
	       </td>
	    </tr>
	  </table>
  </td></tr>
</table>
</body>
</html>
