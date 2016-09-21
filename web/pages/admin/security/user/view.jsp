<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<%@include file="/common/meta.jsp"%>
<%@include file="/common/extjs.jsp"%>

<%@include file="/common/validator.jsp"%>
<%@include file="/common/uploadify.jsp"%>

<script type="text/javascript" src="${ctx}/pages/admin/dept/edit.js"></script>
<link href="${ctx}/styles/treeSelect.css" type='text/css' rel='stylesheet'>

<script type="text/javascript" src="${ctx}/pages/admin/fileattch/fileattch.js"></script>
<LINK href="${ctx}/pages/admin/fileattch/fileattch.css" type='text/css' rel='stylesheet'>

<script type="text/javascript" src="${ctx}/scripts/my97/WdatePicker.js"></script>

<style type="text/css">
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
<div class="x-toolbar">
	<div style="float: left; margin:5px; font-weight: bold;">用户信息</div>
	<div style="float: right;">
		<input type="button" value="返回" class="button" onclick="jacasvript:window.location.href='${ctx}/security/user/index.do';">
		&nbsp;&nbsp;&nbsp;&nbsp;
	</div>
</div>
<div style="width: 99%; margin: 0 auto;"><%@ include file="/common/messages.jsp"%></div>
<br>
	<table width="710" align="center" cellpadding="0" cellspacing="0">
		<tr>
			<th colspan="2" class="title">
				<span>用户基本信息</span>&nbsp;
			</th>
		</tr>
		<tr>
		<td width="50%" valign="top" class="title">
			<table width="350" style="line-height: 25px;" border="0">
				<tr>
					<td width="100" align="right">员工姓名：</td>
					<td width="250"> ${model.name }</td>
				</tr>
				<tr>
					<td align="right">员工性别：</td>
					<td>

                        ${model.sex eq "M" ? "男":"女"}
                    </td>
				</tr>
				<tr>
					<td align="right">所属机构：</td>
					<td>${dept.name }</td>
				</tr>
				<%-- <tr>
					<td align="right">公司职位：</td>
					<td>${position.name}</td>
				</tr> --%>

                <stc:role ifAnyGranted="ROLE_HR">
				<tr>
					<td align="right">身份证号：</td>
					<td>${model.idCard}</td>
				</tr>
				<tr>
					<td align="right">出生日期：</td>
					<td>
						<s:date name="model.birthDay" format="yyyy-MM-dd"/>
					</td>
				</tr>
                </stc:role>
				<tr>
					<td align="right">手机：</td>
					<td>${model.mobile}</td>
				</tr>
				<tr>
					<td align="right">办公电话：</td>
					<td>${model.phone}</td>
				</tr>
				<tr>
					<td align="right">传真号码：</td>
					<td>${model.fox}</td>
				</tr>
				<tr>
					<td align="right">电子邮件：</td>
					<td>${model.email}</td>
				</tr>
				<tr>
					<td align="right">QQ：</td>
					<td>${model.oicq}</td>
				</tr>
				
                <stc:role ifAnyGranted="ROLE_HR">
				<tr>
					<td align="right">最高学历：</td>
					<td>${model.degree}</td>
				</tr>
				<tr>
					<td align="right">毕业院校：</td>
					<td>${model.college}</td>
				</tr>
				<tr>
					<td align="right">毕业时间：</td>
					<td><s:date name="model.graduationTime" format="yyyy-MM-dd"/></td>
				</tr>
                </stc:role>

			</table>
			</td>
			<td width="50%" valign="top" class="title">
			<table width="350" style="line-height: 25px;" border="0">
				<tr>
					
					<td colspan="2" valign="top">
						<div style="width: 200px; height: 255px; border: 1px solid #CCC; margin: 5 5 5 38px;text-align: center;padding:2px;overflow-x: hidden">
						  <img id="photo" src="${ctx}${model.photo.path}" height="255"/></div>
					</td>
				</tr>
                <stc:role ifAnyGranted="ROLE_HR">
				<tr>
					<td align="right" width="100">所学专业：</td>
					<td width="250">${model.major}</td>
				</tr>
				<tr>
					<td align="right">入职时间：</td>
					<td><s:date name="model.joinTime" format="yyyy-MM-dd"/></td>
				</tr>
				<tr>
					<td align="right">转正时间：</td>
					<td><s:date name="model.formalDate" format="yyyy-MM-dd"/></td>
				</tr>
				<tr>
					<td align="right">家庭住址：</td>
					<td>${model.address}</td>
				</tr>
				<tr>
					<td align="right">邮政编码：</td>
					<td>${model.zip}</td>
				</tr>
                </stc:role>
			</table>
			</td>
		</tr>
	</table>
</div>
<script type="text/javascript">
	
	var sex = '${model.sex}';
	var photoId = '${model.photoId}';
	function defaultUserPhoto(sex){
		if (photoId == null || photoId.length == 0){
			if (sex == 'M'){
				$("#photo").attr("src", URL_PREFIX+"/images/default_male.png");
			}else{
				$("#photo").attr("src", URL_PREFIX+"/images/default_female.png");
			}
		}
	}
	defaultUserPhoto(sex);
</script>
</body>
</html>