<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="com.systop.common.modules.dept.DeptConstants" %>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<%@include file="/common/extjs.jsp" %>
<%@include file="/common/meta.jsp" %>
<script type="text/javascript" src="${ctx}/scripts/extjs/diy/ColumnNodeUI.js"></script>
<script type="text/javascript" src="${ctx}/scripts/extjs/diy/ColumnTreeLoader.js"></script>
<script type="text/javascript">
var tree;
var rootName = '<%=DeptConstants.TOP_DEPT_NAME%>';
Ext.onReady(function() {
	Ext.isBorderBox = true;
	tree = new Ext.tree.ColumnTree({
		el : 'grouptree',
		width : 205,
		height:600,
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
		document.getElementById('users').src="${ctx}/userselector/showSkypeUser.do?deptId=" + node.id ;
	});
	tree.render();
	tree.expandAll();
});
</script>
<title>信息交流</title>
</head>
<body>
<table width="900" align="center" cellpadding="0" cellspacing="0" style="border: 1px solid #99bbe8; margin: 5px auto;">
  <tr>
  	<td colspan="2" style="line-height: 25px; height: 25px; background-color: #d0def0; border-bottom: 1px solid #99bbe8;">
  		&nbsp;<img src="${ctx}/images/icons/skype.gif"><b>信息交流</b>&nbsp;&nbsp;
  	</td>
  </tr>
  <tr>
    <td width="205" valign="top" style="border-right: 1px solid #99bbe8;">
		 <div id="grouptree"></div>
	</td>
    <td width="695" valign="top" align="center" style="height: 600px;">
    	<iframe id="users" width="670" onLoad="iFrameHeight()" src="${ctx}/userselector/showSkypeUser.do" frameborder="0" scrolling="no" style="margin: 5px;height: 100%;"></iframe>
    </td>
  </tr>
</table>
<script type="text/javascript" language="javascript">
		function iFrameHeight() {
			var ifm = document.getElementById("users");
			var subWeb = document.frames ? document.frames["users"].document
					: ifm.contentDocument;
			if (ifm != null && subWeb != null) {
				ifm.height = subWeb.body.scrollHeight;
			}
		}
	</script>
</body>
</html>