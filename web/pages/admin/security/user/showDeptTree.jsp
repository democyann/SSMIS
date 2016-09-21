<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="com.systop.common.modules.dept.DeptConstants" %>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<%@include file="/common/extjs.jsp" %>
<script type="text/javascript" src="${ctx}/scripts/extjs/diy/ColumnNodeUI.js"></script>
<script type="text/javascript" src="${ctx}/scripts/extjs/diy/ColumnTreeLoader.js"></script>
<script type="text/javascript">
var tree;
var rootName = "石家庄瑞海检疫技术服务有限公司";
Ext.onReady(function() {
	Ext.isBorderBox = true;
	tree = new Ext.tree.ColumnTree({
		el : 'grouptree',
		width : 205,
		height:510,
		animate : false,
		rootVisible : false,
		autoScroll : true,
		lines : true,
		title : '',
		useArrows : false,

		columns : [{
			header : '',
			width : 200,
			dataIndex : 'text'
		}],

		loader : new Ext.tree.ColumnTreeLoader({
			dataUrl : '/systop/dept/deptTree2.do',
			uiProviders : {
				'col' : Ext.tree.ColumnNodeUI
			},
			listeners : {
				"beforeCreateNode" : function(l, attr) {
					attr.uiProvider = "col";
					attr.iconCls = (attr.leaf) ? "task" : "task-folder";					
				}
			}
		}),

		root : new Ext.tree.AsyncTreeNode({
			 text: rootName,
		     id:'0'
		})
	});
	
	tree.on('click', function(node) {
		window.parent.location.href = "${ctx}/security/user/show.do?deptId=" + node.id ;
	});
	tree.render();
	tree.expandAll();
});
</script>
<title>用户管理</title>
</head>
<body>
<div id="grouptree" style=" border: 0px;"></div>
</body>
</html>