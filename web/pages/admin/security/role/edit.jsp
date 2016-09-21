<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<%@include file="/common/ec.jsp" %>
<%@include file="/common/meta.jsp" %>
<script type="text/javascript">
$(function(){
	$('#selAll').click(function() {
		var isCheck = this.checked;
		$('input[name="model.methodIds"]').each(function(i, obj) {
			obj.checked = isCheck;
		});
		
	});
	
});

</script>
</head>
<body>
<div class="x-panel">
    <div class="x-toolbar">
	  <table width="100%">	      
		<tr>	         
		  <td align="right">   
			<table>
			  <tr>
      <td><a href="${ctx}/security/role/index.do"> 角色管理首页</a></td> 
      <td><span class="ytb-sep"></span></td>
      <td><a href="#"> 编辑角色信息</a></td>
              </tr>
            </table>
          </td>
        </tr>
      </table>
    </div>
<table width="100%" border="0" cellpadding="5" cellspacing="1">
	<tr>
		<td><%@ include file="/common/messages.jsp"%></td>
	</tr>
</table>
<div class="x-panel-body">
<table width="80%" align="center"><tr><td align="center">
<s:form action="role/save" validate="true" theme="simple">
<s:hidden name="model.id"/>
<s:hidden name="model.version"/>
	<fieldset style="margin:10px;">
	<legend>编辑角色</legend>
	<table width="100%">
	<tr>
		<td style="width:30%;text-align:right;">角色名称：</td>
		<td>
		<s:if test="model.isSys==1">
		   <s:textfield id="model.name" name="model.name" disabled="true" title="系统角色名称不可修改"></s:textfield>
		</s:if>
		<s:else>
		  <s:textfield id="model.name" name="model.name" value="%{model.name == null ? 'ROLE_' : model.name}"></s:textfield>
		</s:else>
		<font color="red">&nbsp;必填,角色名称必须以ROLE_开头</font>
		</td>
	</tr>
	<tr>
		<td style="width:30%;text-align:right;">角色描述：</td>
		<td>
		<s:textfield id="model.descn" name="model.descn" ></s:textfield><font color="red">&nbsp;*</font>
		</td>
	</tr>
	</table>
	</fieldset>
	<table width="100%" style="margin-bottom:10px;">
	<tr>
		<td colspan="2" align="center" class="font_white">
		<s:submit value="保存" cssClass="button"></s:submit>&nbsp;&nbsp;&nbsp;
		<input type="button" value="返回" class="button" onclick="history.go(-1)">
		</td>
	</tr>
</table>
</s:form>
 </td></tr></table>
        </div></div>
</body>
</html>