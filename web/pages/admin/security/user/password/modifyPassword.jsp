<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<%@page import="com.systop.common.modules.security.user.UserUtil"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<base target="_self">  
<title>修改密码</title>
<%@include file="/common/meta.jsp"%>
<%@include file="/common/validator.jsp"%>
<style type="text/css">
.simple {
	font-size: 12px;
	padding: 4px;
	color: #0099FF;
	text-align: right;
}
</style>
<script type="text/javascript"
	src="${ctx}/pages/admin/security/user/password/changePassword.js"></script>
</head>
<body>
<div class="x-toolbar" style="padding: 5px; font-weight: bold;">修改密码</div>
<div><%@ include file="/common/messages.jsp"%></div>
<s:form id="modifyPassword" action="changePassword.do" method="post" cssStyle="margin:10px auto;">
<fieldset style="margin: 10px;">
	<input type="hidden" name="model.id" value='<%=UserUtil.getLoginUser(request).getId()%>'>
	<table width="400" align="center" style="margin: 10px auto;">
		<tr>
			<td class="simple" width="80">旧密码：</td>
			<td width="220">
				<s:password name="oldPassword" id="oldPassword"  maxlength="25" cssClass="required" /></td>
			<td width="100">&nbsp;</td>
		</tr>
		<tr>
			<td class="simple">新密码：</td>
			<td>
				<s:password name="model.password" id="pwd"  maxlength="25" cssClass="required checkpwd"
							onkeyup="pwStrength(this.value);"
							onblur="pwStrength(this.value);" />
			</td>
			<td>
			  <table width="90" border="0" style="line-height: 20px;">
				<tr align="center" bgcolor="#E0E0E0">
					<td width="30" id="strength_L">弱</td>
					<td width="30" id="strength_M">中</td>
					<td width="30" id="strength_H">强</td>
				</tr>
			  </table>
			</td>
		</tr>
		<tr>
			<td class="simple">重复密码：</td>
			<td colspan="2">
				<s:password name="model.confirmPwd" id="repwd"  maxlength="25" cssClass="required checkRepwd" />
				<span id="pwdMsg" style="color: red"></span>
			</td>
		</tr>
	</table>
</fieldset>
<div align="center">
	<input type="submit" class="button" value="保存">&nbsp;&nbsp;
	<input type="button" class="button" value="关闭" onclick="window.close();">
</div>
</s:form>
<script type="text/javascript">
$(function() {
	//验证密码一致性
	$.validator.addMethod("checkpwd", function(value, element) {
		var isOk = false;
		var pwd = value;
		var repwd = $("#pwd").val();
		if (pwd != null && repwd != null) {
			if (pwd == repwd) {
				isOk = true;
			}
		}
		return isOk;
	}, null);
});
	
	$(function() {
		//验证密码一致性
		$.validator.addMethod("checkRepwd", function(value, element) {
			var isOk = false;
			var pwd = $("#pwd").val();
			var repwd = value;
			if (pwd != null && repwd != null) {
				if (pwd == repwd) {
					isOk = true;
					$("#pwdMsg").html(null);
				}else{
					$("#pwdMsg").html("两次密码不一致!");
				}
			}
			return isOk;
		}, null);
	});
	$(document).ready(function() {
		$("#modifyPassword").validate();
	});
</script>
</body>
</html>