<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<title>字典编辑</title>
<link rel="stylesheet" type="text/css" href="${ctx}/styles/ec/ecside_style.css" />
<%@include file="/common/meta.jsp"%>
<%@include file="/common/validator.jsp"%>
</head>
<body>
	<div class="x-panel">
		<table width="100%" border="0" cellpadding="5"
			cellspacing="1">
			<tr>
				<td><%@ include file="/common/messages.jsp"%></td>
			</tr>
		</table>
		<div class="x-panel-body" align="center" style="width: 100%">
			<form action="save.do" id="save" method="post" style="margin: auto;">
			  <s:hidden name="model.id"/>
			  <fieldset style="width: 80%; padding:10px 10px 10px 10px;">
			    <legend>信息编辑</legend>
				<table width="700" border="0" align="center" style="line-height: 30px; margin: 20px auto;">
					<tr>
						<td width="100" align="right">名称：</td>
						<td width="420" >
							<s:textfield name="model.name" id="name" size="55" cssStyle="width:300px;" cssClass="required"/>
							<font color="red">*</font>
						</td>

					</tr>
					<tr>
						<td width="100" align="right">编码：</td>
						<td width="420" >
							<s:textfield name="model.code" id="code" size="55" cssStyle="width:300px;" cssClass="required"/>
							<font color="red">*</font>
						</td>
					</tr>
					<tr>
						<td width="100" align="right">类型：</td>
						<td width="420" >
							<s:select id="category" name="model.category" list='dictMap' headerKey="" headerValue="请选择" cssStyle="width:300px;" cssClass="required"/>
							<font color="red">*</font>
						</td>
					</tr>
					<tr>
						<td width="100" align="right" valign="top">备注：</td>
						<td colspan="3">
							<s:textarea name="model.descn" id="descn"  cssStyle=" width: 300px; height: 100px;"/>
						</td>
					</tr>
				</table>
			  </fieldset><br><br>
			  <div align="center">
				<input type="submit" value="保存" class="button">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<input type="reset" value="重置" class="button">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<input type="button" value="返回" class="button" onclick="history.go(-1)">
			  </div>
			</form>
		</div>
	</div>
	<script type="text/javascript">
	$(document).ready(function() {
		$("#save").validate();
	});
	</script>
</body>
</html>