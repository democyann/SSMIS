<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="com.systop.common.modules.dept.DeptConstants" %>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<link rel="stylesheet" type="text/css" href="${ctx}/styles/ec/ecside_style.css" />
<%@include file="/common/extjs.jsp" %>
<%@include file="/common/meta.jsp" %>
<link href="${ctx}/scripts/extjs/resources/css/column-tree.css" type='text/css' rel='stylesheet'>
<script type="text/javascript" src="${ctx}/scripts/extjs/diy/ColumnNodeUI.js"></script>
<script type="text/javascript" src="${ctx}/scripts/extjs/diy/ColumnTreeLoader.js"></script>
<title></title>
<style type="text/css">
.task {
    background-image:url(${ctx}/images/icons/file.png) !important;
}
.task-folder {
    background-image:url(${ctx}/images/icons/foldericon.png) !important;
}
.x-tree-node-cb {
    border:0;
    height:14px;
}
.x-tree-col-text {
   padding:0px;
   margin:0px;
   font-size:12px;
   font-family: sans-serif;
}
.x-tree-hd-text {
   font-size:12px;
   font-family: sans-serif;
}
#tree-dept {
	border-right:1px #99bbe8 solid;
	height:500px;
}
</style>
<script type="text/javascript">
var rootName = '石家庄瑞海检疫技术服务有限公司';

var modifyType = "${param.modify}";

var tree;
Ext.onReady(function() {
	Ext.isBorderBox = true;
	tree = new Ext.tree.ColumnTree({
		el : 'tree-dept',
		width : 503,
		animate : true,
		autoHeight : false,
		rootVisible : false,//是否显示根节点
		autoScroll : true,
		lines : true,
		title : '',
		useArrows : false,

		columns : [{
			header : '机构名称',
			width : 250,
			dataIndex : 'text'
		}, {
			header : '编辑',
			width : 100,
			dataIndex : 'edit'
		}],

		loader : new Ext.tree.ColumnTreeLoader({
			dataUrl : '/systop/dept/deptTree.do',
			uiProviders : {
				'col' : Ext.tree.ColumnNodeUI
			},
			listeners : {
				"beforeCreateNode" : function(l, attr) {
					attr.uiProvider = "col";
					//attr.checked = false;
					attr.iconCls = (attr.leaf) ? "task" : "task-folder";
					attr.cn = "selectedItems";
					if (!attr.descn){
						attr.descn = '&nbsp;';
					}
					if (modifyType == "finance"){
						attr.edit = [
								'<div>&nbsp;',
								Ext.ctx('<img src="{ctx}/images/icons/cog.gif" border="0" onclick="edit('), attr.id, ', true)"></div>'].join('');
					}else{
						/* if (attr.companyId){ */
					
						attr.edit = [
										'<div>&nbsp;',
										Ext.ctx('<img src="{ctx}/images/icons/modify.gif" title="修改" border="0" onclick="edit('), attr.id, ', null)">&nbsp;&nbsp;',
										'<img src="${ctx}/images/exticons/cross.gif" title="删除" border="0" onclick="remove(', attr.id, ')"></div>'].join('');
					}
					/* } */
				}
			}
		})
	});
	var root = new Ext.tree.AsyncTreeNode({
        text: rootName,
    	
        id:'0'
    });
    tree.setRootNode(root);
	tree.render();
	//tree.expandAll();
});

function edit(id, isFinance) {
	Ext.get("targetId").dom.value = id;
	Ext.get("isFinance").dom.value = isFinance;
	Ext.get("operator").dom.submit();
}

function remove(id) {
	Ext.MessageBox.confirm('提示', '确定删除此机构吗?', function(btn) {
		if(btn == 'yes') {
			window.location.href = "remove.do?model.id=" + id;
		}
	});
}
</script>
</head>
<body>
	<div align="left">
			<%@include file="/pages/common/messages.jsp"%>
	</div>	
<div class="x-panel">
  <div class="x-toolbar" style="padding: 5px;" align="right">
	    <img src="${ctx}/images/icons/folder_go.gif"/><a href="${ctx}/systop/dept/edit.do"> 新建机构</a>
	    &nbsp;&nbsp;&nbsp;&nbsp;
	    <%-- <img src="${ctx}/images/grid/clear.png"/><a href="${ctx}/systop/dept/updateDeptSerialNo.do">重置机构编号</a>
	    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; --%>
  </div>
  <div class="x-panel-body">
	  <table width="100%" cellpadding="0" cellspacing="0" style="border-bottom:1px #99bbe8 solid;">
	    <tr>
	      <td width="501" valign="top">
	          <div id="tree-dept"></div>
	      </td>
	      <td width="100%" valign="top">
	      </td>
	    </tr>
	  </table>
  </div>
</div>
<form action="${ctx}/systop/dept/edit.do" id="operator" method="get">
   <input type="hidden" name="model.id" id="targetId">
   <input type="hidden" name="isFinance" id="isFinance">
</form>


</body>
</html>