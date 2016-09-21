<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="com.systop.common.modules.dept.DeptConstants" %>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8" />
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<link rel="stylesheet" type="text/css" href="${ctx}/scripts/extjs/resources/css/loading.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/scripts/extjs/resources/css/ext-all.css" />
<script type="text/javascript" src="${ctx}/scripts/extjs/adapter/ext/ext-base.js"></script>
<script type="text/javascript" src="${ctx}/scripts/extjs/ext-all.js"></script>
<script type="text/javascript" src="${ctx}/scripts/extjs/locale/ext-lang-zh_CN.js"></script>
<script type="text/javascript" src="${ctx}/scripts/extjs/ext-util.js"></script>

<%@include file="/common/meta.jsp" %>
<script type="text/javascript" src="${ctx}/scripts/extjs/diy/ColumnNodeUI.js"></script>
<script type="text/javascript" src="${ctx}/scripts/extjs/diy/ColumnTreeLoader.js"></script>
<script type="text/javascript">
var tree;
var rootName = '石家庄瑞海检疫技术服务有限公司';
$(function() {
	 _selMap = {};
	Ext.isBorderBox = true;
	tree = new Ext.tree.ColumnTree({
		el : 'grouptree',
		width : 205,
		height:400,
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
			dataUrl : '/systop/dept/deptTreeSeleectRadio.do',
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
		document.getElementById('users').src="${ctx}/userselector/onlyRadiocheck.do?deptId=" + node.id + "&authoritys=${param.authoritys}";
	});
	tree.render();
	//tree.expandAll();
});
</script>

<style type="text/css">

.green{
	color: green;
	font-weight: bold;
}

.red{
	color: red;
	font-weight: bold;
}
	
</style>
<title>选择用户</title>
</head>
<body>
<%
	session.setAttribute("selected_users", null);
%>
<div class="x-toolbar" style="width: 606px;">
	<table width="100%">
	  <tr>
	  	<td width="50%" style="color: #15428b; font-weight: bold;">
	  		&nbsp;选择用户</td>
	  	<td width="50%" align="right">
			<input type="button" class="button red" value="确认选择" onclick="showSelectedUsers()">
			&nbsp;
	  	</td>
	  </tr>
	</table>
	
</div>
<table width="606" cellpadding="0" cellspacing="0" style="border: 1px solid #D9D9D9; border-right: 0px; ">
  <tr>
    <td width="205" valign="top">
		 <div id="grouptree"></div>
	</td>
    <td width="401" valign="top">
    <iframe name="users" width="100%" height="408" src="${ctx}/userselector/onlyRadiocheck.do?authoritys=${param.authoritys}" frameborder="0" scrolling="no" id="users"></iframe>
    </td>
  </tr>
</table>
<script type="text/javascript">
    var win = window.dialogArguments || window.opener || window;
    if (win != window){
        _selMap = {};
        $.extend(_selMap, win.selMap);
        onlyOne = win.onlyOne;
    }
    selList = [];

    function showSelectedUsers() {
        $.each(_selMap, function (i, n) {
            if(n) selList.push(n);
        });
        win.selMap = {};

        $.extend(win.selMap,_selMap);

        if (navigator.userAgent.search(/android|iphone|ipad/i) == -1) {
            //modal
            window.returnValue = selList;
        } else {
            return returnAction(selList);
        }
        window.close();
    }

    function clearSelectedUsers()
    {
        win.selMap = {};
        if(win==window){
            $("#users")[0].contentWindow.$(":checked").attr("checked","");
            //$("#users")[0].contentWindow._selMap = _selMap;
            returnAction([]);
        }
        else{
            window.returnValue = [];
            window.close();
        }
    }

    //
    function selectAll(){
        window.frames[0].selectAll();
    }

</script>
</body>
</html>