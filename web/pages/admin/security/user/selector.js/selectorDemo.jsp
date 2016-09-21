<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<%@include file="/common/meta.jsp"%>
<%@include file="/common/extjs.jsp"%>
<script type="text/javascript" src="${ctx}/scripts/extjs/diy/TreeCheckNodeUI.js"></script>
<script type="text/javascript" src="${ctx}/pages/admin/security/user/selector.js/selector.js"></script>
<title></title>
<style type="text/css">
.dept-tree-node-icon {
	background-image:url(${ctx}/images/icons/dept.gif);
}
.emp-M-tree-node-icon,.emp-F-tree-node-icon {
	background-image:url(${ctx}/images/icons/users_1.gif);
}
</style>
</head>
<body>
<div id="emp_sel_container" class="x-hidden">
    <div class="x-window-header">员工列表</div>
    <div id="emp_grid"></div>
</div>


<script>
var emp;
   emp = new UserSelector({
       url: '${ctx}/security/user/userTree.do',
       el: 'emp_sel_container',
       idsEl:'ids',
       textEl:'txts',
       width : 250,
	   height : 450,
	   multiSel : true
   });
   
function showWin() {   
   emp.show(true);
}

</script>
<input onclick="showWin()" value="全部" type="button" class="button">
<input id="txts" type="text"/>
<input id="ids" type="hidden"/>


</body>
</html>