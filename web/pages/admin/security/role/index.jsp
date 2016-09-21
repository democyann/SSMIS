<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<title>角色管理</title>
<%@include file="/common/easyui.jsp" %>
<%@include file="/common/ec.jsp" %>
<%@include file="/common/extjs.jsp" %>
<%@include file="/common/meta.jsp" %>
<script type="text/javascript" src="${ctx}/scripts/utils.js" ></script>
<script type="text/javascript">
	var roleId;
	var url_root = '${ctx}';
	function permission(id,roleName){
	
		var jsmc = roleName;
		$('#permissionTree').tree({
			checkbox : true,
			animate:true,
			//data: {"bs" : bs},
			url :  '${ctx}/security/permission/selectPermissionIndex.do?roleId='+id,
			onClick : function(node) {	
				$(this).tree('collapse', node.target);
			},
			onLoadSuccess:function(){
			
			}
		});
	
		$('#permissionDig').dialog('open');
		$('.panel-title').text("权限列表，当前角色："+jsmc);
		roleId = id;
	}
	function selectAll(){
		var nodes = $('#permissionTree').tree("getRoots");
		for(var i=0; i<nodes.length; i++){
			$('#permissionTree').tree('check',nodes[i].target); 
        }
	}
	
	function removeAll(){
		var nodes = $('#permissionTree').tree("getChecked");
		for(var i=0; i<nodes.length; i++){
			$('#permissionTree').tree('uncheck',nodes[i].target); 
        }
	}
	
	function selectPerm(){
		var nodes = $('#permissionTree').tree('getChecked');
        var permissionIds='';
        for(var i=0; i<nodes.length; i++){
           // if(nodes[i].attributes != undefined){
            //	if(nodes[i].attributes.permission = '1'){
	            	permissionIds += nodes[i].id + ',';
            //	}
           // }
        }
  
        permissionIds = permissionIds.substring(0, permissionIds.length-1);
        if(permissionIds==''){
        	$.messager.confirm('提示', '确定要清空此角色的权限吗？', function(r){
				if(r){
					$.ajax({
		        		url:'${ctx}/security/permission/savePermissionList.do',
		        		data:{
		        			'roleId':roleId,
		        			'permissionIds' : permissionIds
		        		},
		        		type: 'post',
		        		dataType: 'json',
		        		success: function(r){
		        			$.messager.alert('提示',r.msg,'',function(){
		        				$('#permissionDig').dialog('close');
		        			});
		        			
		        		},
		        		error: function(){
		        			//nothing...
		        		}
		        	});
				}
			});
        }else{
        	$.ajax({
        		url:  '${ctx}/security/permission/savePermissionList.do',
        		data:{
        			'roleId':roleId,
        			'permissionIds' : permissionIds
        		},
        		type: 'post',
        		dataType: 'json',
        		success: function(r){
        			$.messager.alert('提示',r.msg,'',function(){
        				$('#permissionDig').dialog('close');
        			});
        			
        		},
        		error: function(){
        			//nothing...
        		}
        	});
        }
	}
	
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

</script>
</head>
<body>
<s:form id="removeForm" action="role/remove" method="POST"></s:form>
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
          <td> 
            <s:form action="role/index" theme="simple">
	         &nbsp;角色名称：<s:textfield theme="simple" name="model.name" ></s:textfield>
	         &nbsp;角色描述：<s:textfield theme="simple" name="model.descn" ></s:textfield>
	         &nbsp;&nbsp;<s:submit value="查询" cssClass="button"></s:submit>
         </s:form>
         </td>
         <td align="right">
         <table> 
           <tr>
             <td><a href="${ctx}/security/role/index.do"><img src="${ctx}/images/icons/house.gif"/> 首页</a></td>
             <td><span class="ytb-sep"></span></td>
             <td><a href="${ctx}/security/role/editNew.do"><img src="${ctx}/images/icons/add.gif"/> 新建</a></td>
             <td><span class="ytb-sep"></span></td>
             <td>
             	<a href="javascript:onRemove('removeForm','确认要删除信息吗?');">
             	<img src="${ctx}/images/icons/delete.gif"/> 删除</a></td>
           </tr>
         </table>
         </td>
       </tr>
     </table>
   </div>   

   <div class="x-panel-body">
     <div style="margin-left:-3px;" align="center">
	<ec:table items="items" var="item" retrieveRowsCallback="process" sortRowsCallback="process" 
		action="index.do"
		useAjax="true" doPreload="false"
		maxRowsExported="1000" 
		pageSizeList="20,50" 
		editable="false" 
		sortable="false"	
		rowsDisplayed="20"	
		generateScript="true"	
		resizeColWidth="true"	
		classic="false"	
		width="100%" 	
		height="460px"	
		minHeight="460" 
		toolbarContent="navigation|pagejump|pagesize|refresh|extend|status" >
		<ec:row>
		    <ec:column width="50" property="_s" title="选择" style="text-align:center" viewsAllowed="html" sortable="false">
		       <s:if test="#attr.item.isSys==1">
		           <input type="checkbox" class="checkbox" disabled="disabled"/>
		       </s:if>
		       <s:else>
		       	<input type="checkbox" name="selectedItems" id="selectedItems" value="${item.id}" class="checkbox"/>
		       	
		       </s:else>
		    </ec:column>
			<ec:column width="40" property="_0" title="序号" value="${GLOBALROWCOUNT}"  sortable="false"/>
			<ec:column width="180" property="name"     title="角色名称" />
			<ec:column width="150" property="descn" title="角色描述" />
			<ec:column width="60" property="_2" title="系统角色" style="text-align:center;">
			   <s:if test="#attr.item.isSys==1">
			      <span style="color:red">是</span>
			   </s:if>
			   <s:else>
			      <span style="">否</span>
			   </s:else>
			</ec:column>
			<ec:column width="40" property="_0" title="权限" style="text-align:center">
				<a href="#" onclick="javascript:permission('${item.id}','${item.descn }')">
			       <img src="${ctx}/images/icons/authority.gif" border="0" title="分配权限"/></a>
			</ec:column>		
			<ec:column width="40" property="_0" title="操作" style="text-align:center">
			   <a href="edit.do?model.id=${item.id}"> 
			      <img src="${ctx}/images/icons/modify.gif" style="border:0px" title="编辑"/></a>
			</ec:column>
		</ec:row>
	</ec:table>
  </div>
  </div>
  </div>
<div id="win" class="x-hidden">
    <div class="x-window-header">许可列表</div>
    <div id="perm_grid"></div>
</div>
<script type="text/javascript" src="${ctx}/pages/admin/security/role/role.js">
</script>
	<div id="permissionDig" class="easyui-dialog"   title="权限列表" data-options="iconCls:'icon-add',closed:true,modal:true,
buttons:[
		{
			text:'清除',
			iconCls:'icon-remove',
			handler:function(){
				removeAll();
			}
		},
		{
			text:'全选',
			iconCls:'icon-ok',
			handler:function(){
				selectAll();
			}
		},{
			text:'确定',
			iconCls:'icon-ok',
			handler:function(){
				selectPerm();
			}
		},{
			text:'取消',
			iconCls:'icon-cancel',
			handler:function(){
				$('#permissionDig').dialog('close');
			}
		}]	
" style="width:500px;height:460px;padding:0px">
	<!-- 用于 Tree 渲染 -->
	<div>
		<table width="460px"  cellspacing="0" align="center" class="mytable">
			<tr>
				<td>
					<ul id="permissionTree"></ul>
				</td>
			</tr>
		</table>
	</div>
</div>
</body>
</html>