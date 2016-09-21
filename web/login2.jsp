<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<%
	try {
		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals("mobile-mode")) {
				String value = cookie.getValue();
				if (value.equals("true")) {
					response.sendRedirect("m/index.jsp");
				}
			}
		}
	} catch (Exception e) {
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>系统登录</title>
<stc:ifLogin>
	<meta http-equiv="refresh" content="0; url=${ctx}/main.do">
</stc:ifLogin>
<link rel="icon" href="${ctx}/images/logo.png" type="image/png">
<link rel="shortcut icon" href="${ctx}/images/logo_0.ico">
<LINK href="${ctx}/styles/style.css" type='text/css' rel='stylesheet'>

<style type="text/css">
.STYLE3 {
	font-family: sans-serif;
	color: #11588E;
	font-weight: bold;
}

.username {
	height: 28px;
	width: 200px;
	padding-left: 21px;
	background-image: url(${ctx}/images/icons/user.gif);
	background-position: 1px 4px;
	background-repeat: no-repeat;
	border: 1px solid #88ABDC;
	color: red;
	font-weight: bold;
	font-family: sans-serif;
}

.password {
	height: 28px;
	width: 200px;
	padding-left: 21px;
	background-image: url(${ctx}/images/icons/lock_go.gif);
	background-position: 2px 4px;
	background-repeat: no-repeat;
	border: 1px solid #88ABDC;
	color: red;
	font-weight: bold;
	font-family: cursive;
}

.reset-button {
	margin-left: 20px;
	margin-left: 10px\9;
	width: 75px;
}
</style>
</head>
<body bgcolor="#FFFFFF">
	<div style="background-color: #8AB2ED; height: 25px;"></div>
	<div style="height: 30px;"></div>
	<table width="99%" align="center" height="480" border="0"
		cellpadding="0" cellspacing="0">
		<tr>
			<td valign="middle" align="center">
				<form id="loginForm" action='${ctx}/j_security_check' method="post">
					<table width="431" align="center" cellpadding="0" cellspacing="0"
						style="line-height: 35px;">
						<tr>
							<td align="center" valign="middle" colspan="2"><img
								src="${ctx}/images/scos/logo.jpg"></td>
						</tr>
						<tr>
							<td colspan="2" align="center"><c:if
									test="${param.login_error == 'user_psw_error' || param.login_error == '1'}">
									<div
										style="border-bottom: 1px dashed #88ABDC; width: 270px; padding: 5 0 2 8;">
										<img border="0"
											src="<c:url value='/images/icons/warning.gif'/>" />&nbsp;用户名或密码错误,请重试！
									</div>
								</c:if> <c:if test="${param.login_error == 'too_many_user_error'}">
									<div
										style="border-bottom: 1px dashed #88ABDC; width: 270px; padding: 5 0 2 8;">
										<img border="0"
											src="<c:url value='/images/icons/warning.gif'/>" />&nbsp;该用户已经在其他地方登录了,请稍候。
									</div>
								</c:if> <c:if test="${param.login_error == '2'}">
									<div
										style="border-bottom: 1px dashed #88ABDC; width: 270px; padding: 5 0 2 8;">
										<img border="0"
											src="<c:url value='/images/icons/warning.gif'/>" />&nbsp;用户密码已重置，请重新登录。
									</div>
								</c:if></td>
						</tr>
						<tr>
							<td width="180px" align="right" class="STYLE3">用户名：</td>
							<td><input type="text" align="left" name="j_username" id="j_username"
								class="username" /></td>
						</tr>
						<tr>
							<td width="180px" align="right" class="STYLE3">密&nbsp;&nbsp;&nbsp;&nbsp; 码：</td>
							<td><input name="j_password" align="left" id="j_password" type="password"
								class="password" /></td>
						</tr>
						<tr></tr>
						<tr>
							<td colspan="2" align="center"><input type="submit"
								class="button"  value="登录" /> <input type="reset" class="button"
								value="重置" style="margin-left: 20px;" /></td>
						</tr>
					</table>
		</form>
		</td>
		</tr>
	</table>
	<div style="height: 20px;"></div>
	<div style="border-top: 4px solid #8AB2ED;">
		<div style="padding-top: 10px; text-align: center;">翰子昂移动互联实验室</div>
		<div style="padding-top: 5px; text-align: center; font-family: arial;">
			&copy;HByy all right is reserved</div>
	</div>


</body>
</html>
