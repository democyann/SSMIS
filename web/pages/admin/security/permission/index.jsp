<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<title></title>
<%@include file="/common/ec.jsp"%>
<%@include file="/common/meta.jsp"%>
<script type="text/javascript" src="${ctx}/scripts/utils.js" ></script>

	<script type="text/javascript" >
	function onRemove(formid, msg) {
		var checked = false;
		$('input').each(function(i, item) {
			if (item.checked && item.id == 'selectedItems') {
				checked = true;
			}
		});
		if (!checked) {
			alert('请至少选择一条要删除信息');
			return;
		}

		if (confirm(msg)) {
			var s = null;
			$('input').each(function(i, item) {//循环每一个表单里的input元素
				if (item.checked && item.id == 'selectedItems') {//确定选中的是复选框
					if (s != null) {
						s = s + item.value + ",";
						//	alert(item.value);
					} else {//第一次
						s = item.value + ",";
					}
				}
			});

			// alert(s);
			window.location.href = "remove.do?ids=" + s;
		}
	}
	
	   function load(mes){
			if(mes!=""){
			alert(mes); 
			}
	  }
	</script>
</head>
<body onload="load('${message}');">
<s:form action="permission/remove" method="POST" id="removeForm" validate="false">
</s:form>
<div class="x-panel">
<table width="100%" border="0" cellpadding="0"
			cellspacing="1">
			<tr>
				<td><%@ include file="/common/messages.jsp"%></td>
			</tr>
		</table>
<div class="x-toolbar">
<table width="99%">
	<tr>
		<td><s:form action="permission/index" theme="simple">
	       <%--  &nbsp; 权限名称：<s:textfield theme="simple" name="model.name" ></s:textfield> --%>
	        &nbsp;权限描述：<s:textfield theme="simple" name="model.descn" ></s:textfield>
	        &nbsp;&nbsp;<s:submit value="查询" cssClass="button"></s:submit>
		</s:form></td>
		<td align="right">
		<table>
			<tr>
				<td><a href="${ctx}/security/permission/editNew.do"><img
					src="${ctx}/images/icons/add.gif" /> 新建权限</a></td>
				<td><span class="ytb-sep"></span></td>
				<td><a href="#" onclick="javascript:onRemove('removeForm','确认要删除信息吗?');"><img
					src="${ctx}/images/icons/delete.gif" /> 删除权限</a>
			</tr>
		</table>
		</td>
	</tr>
</table>
</div>

<div class="x-panel-body">
<div style="margin-left: -3px;" align="center">
	<ec:table items="items" var="item" retrieveRowsCallback="process" sortRowsCallback="process"
	 action="index.do" 
	 useAjax="true" 
	 doPreload="false"
	 maxRowsExported="1000"
	 pageSizeList="20,50" 
	 editable="false" 
	 sortable="true"
	 rowsDisplayed="20" 
	 generateScript="true" 
	 resizeColWidth="true"
	 classic="false" 
	 width="100%"
	 height="460px" 
	 minHeight="460"
	 toolbarContent="navigation|pagejump|pagesize|refresh|extend|status">
	<ec:row>
		<ec:column width="50" property="_s" title="选择" style="text-align:center" viewsAllowed="html" sortable="false">
			<s:if test="#attr.item.isSys==1"> 
				<input type="checkbox" class="checkbox" disabled="disabled" /> 
			</s:if>
			<s:else> 
				<input type="checkbox" name="selectedItems" id="selectedItems" value="${item.id}" class="checkbox" />
			</s:else>
		</ec:column>
		<ec:column width="40" property="_0" title="序号" value="${GLOBALROWCOUNT}" sortable="false" />
		<%-- <ec:column width="150" property="name" title="权限名称" /> --%>		
		<ec:column width="150" property="descn" title="权限描述" />
		<ec:column width="60" property="_2" title="系统权限" style="text-align:center;">
		   <s:if test="#attr.item.isSys==1">
		      <span style="color:red">是</span>
		   </s:if>
		   <s:else>
		      <span style="">否</span>
		   </s:else>
		</ec:column>
	<%-- 	<ec:column property="_0" title="资源" style="text-align:center" viewsAllowed="html" sortable="false">
			<a href="#" onclick="javascript:assignResources(${item.id})"> 
				<img src="${ctx}/images/icons/news.gif" border="0" title="分配资源" /> 
			</a>
		</ec:column> --%>
		<%-- <ec:column width="40" property="_1" title="菜单" style="text-align:center" viewsAllowed="html" sortable="false">
			<a href="#" onclick="javascript:assignMenus(${item.id})">
				<img src="${ctx}/images/icons/news.gif" border="0" title="分配菜单" /> 
			</a>
		</ec:column> --%>
		<ec:column width="50" property="_0" title="操作"
			style="text-align:center" viewsAllowed="html" sortable="false">
			<s:if test="#attr.item.isSys==1">
			 <img src="${ctx}/images/icons/modify.gif" style="border: 0px" title="系统权限不能编辑" />
			</s:if>
			<s:else>
			<a href="edit.do?model.id=${item.id}"> <img
				src="${ctx}/images/icons/modify.gif" style="border: 0px" title="编辑" />
			</a>
			</s:else>
		</ec:column>
	</ec:row>
</ec:table></div>
<div id="win" class="x-hidden">
<div class="x-window-header">资源列表</div>
<div id="res_grid"></div>
</div>
<!-- <div id="win_menu" class="x-hidden">
<div class="x-window-header">菜单列表</div>
<div id="tree-div"></div>
</div> -->
</div>
</div>
</body>

</html>