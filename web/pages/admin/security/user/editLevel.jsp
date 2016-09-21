<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<link rel="stylesheet" type="text/css" href="${ctx}/styles/ec/ecside_style.css" />
<%@include file="/common/meta.jsp"%>
<%@include file="/common/extjs.jsp"%>

<%@include file="/common/validator.jsp"%>
<%@include file="/common/uploadify.jsp"%>

<script type="text/javascript" src="${ctx}/pages/admin/dept/edit.js"></script>
<link href="${ctx}/styles/treeSelect.css" type='text/css' rel='stylesheet'>

<script type="text/javascript" src="${ctx}/pages/admin/fileattch/fileattch.js"></script>
<LINK href="${ctx}/pages/admin/fileattch/fileattch.css" type='text/css' rel='stylesheet'>

<script type="text/javascript" src="${ctx}/scripts/my97/WdatePicker.js"></script>
    <script type="text/javascript" src="${ctx}/scripts/jquery/ui/jquery-ui-1.8.21.custom.min.js"></script>
    <link href="${ctx}/scripts/jquery/ui/css/smoothness/jquery-ui-1.8.21.custom.css" rel="stylesheet"/>
<style type="text/css">
.warn {
	color: red;
}

.title {
	border-bottom: 1px solid #99BBE8;
	font-size: 14px;
	font-weight: bold;
	color: #15428b;
}
</style>
<title>编辑用户</title>
</head>
<body>
<div class="x-panel">
<div style="width: 99%; margin: 0 auto;"><%@ include file="/common/messages.jsp"%></div>
<br>
<s:form action="user/save" id="save" validate="true" method="post" >
<s:hidden name="model.id" id="uid"/>
	<table width="800" align="center" border="0" style="border-bottom: 1px solid #99BBE8;">
		<tr>
			<td colspan="2" class="title">
				<span>审批优先级</span>&nbsp;
			</td>
		</tr>
		<tr>
			<td width="50%" valign="top">
			<table width="400" style="line-height: 25px;">
				<tr>
					<td width="100" align="right">用户名：</td>
					<td width="300">
						${model.name }
					</td>
				</tr>
				<tr>
					<td width="100" align="right">审批优先级：</td>
					<td width="300">
					<s:textfield name="model.orederId" cssClass="number"/>	
					</td>
				</tr>
	</table>
	<div align="center" style="margin-top: 5px;">
		<input type="submit" value="保存" class="button">&nbsp;&nbsp;&nbsp;&nbsp; 
		<input type="reset" value="重置" class="button">
	</div>
</s:form></div>
<script type="text/javascript">
	//添加jquery验证
	$(document).ready(function() {
		$("#save").validate();
	});
</script>
</body>
</html>