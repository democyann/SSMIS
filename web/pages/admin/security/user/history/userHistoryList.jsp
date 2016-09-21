<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<%@include file="/common/ec.jsp" %>
<%@include file="/common/extjs.jsp" %>
<%@include file="/common/meta.jsp" %>
<script type="text/javascript" src="${ctx}/amcharts/swfobject.js"></script>
<link href="${ctx}/styles/treeSelect.css" type='text/css'
	rel='stylesheet'>
<style type="text/css">
	input[type="text"]{
	width:180px;
}
</style>
<title></title>
</head>
<body>
<div class="x-panel">
     <div class="x-toolbar">
      <table width="99%">
        <tr>
          <td> 
        <s:form action="/userHistory/userHistoryList.do" theme="simple">
      <table border="0"><tr>  	
	      <td>用户名:<s:textfield name="queryUsername" /></td>
	      <td>处室:</td>
	         <td width="20"><div id="comboxWithTree" class="required"></div>
				<s:hidden name="deptId" id="deptId"></s:hidden></td>
	         <td>&nbsp; 时间:
      <input type="text" id="beginDate" name="beginDate" value="${param.beginDate}"
          onfocus="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd HH:mm'})" class="Wdate required"  readonly/>      
      <!-- 选择查询结束时间 --> 
     至
        <input type="text" id="endDate" name="endDate" value="${param.endDate}"
          onfocus="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd HH:mm'})" class="Wdate required"  readonly/> </td>     
	       <td>&nbsp;&nbsp;<s:submit value="查询" cssClass="button"></s:submit></td> 
         </tr></table>
         </s:form>
        </td>
        
        </tr>
      </table>
    </div>     
    <div class="x-panel-body"> 
	<ec:table items="items" 
	var="item" 
	retrieveRowsCallback="process"
	sortRowsCallback="process" 
	action="userHistoryList.do" 
	useAjax="false"
	doPreload="false" 
	pageSizeList="20,50" 
	editable="false"
	sortable="true" 
	rowsDisplayed="20" 
	generateScript="true"
	resizeColWidth="false" 
	classic="false" 
	width="100%" 
	height="460px"
	minHeight="460"
	toolbarContent="navigation|pagejump|pagesize|refresh|extend|status">
	<ec:row>
	    
		<ec:column width="40" property="_0" title="序号" value="${GLOBALROWCOUNT}" sortable="false" style="text-align:center"/>
		<ec:column width="80" property="user.name" title="用户名称" />
		<ec:column width="80" property="dept.name" title="处室" />
		<ec:column width="120" property="loginTime" title="登录时间" cell="date" format="yyyy-MM-dd HH:mm" />
		<ec:column width="120" property="loginIp" title="登录地址" />
		
	</ec:row>
	</ec:table>
	</div>
</div>
<script type="text/javascript" src="${ctx}/pages/admin/dept/edit.js"></script>
<script type="text/javascript">
Ext.onReady(function() {
	var dtree = new DeptTree({
		url : '/systop/dept/deptTree.do',
		parent : '<stc:loginUserDept showPath="false" propertyName="id" showTopDept="true"></stc:loginUserDept>',
		initValue : '${deptName}',
		el : 'comboxWithTree',
		innerTree :'inner-tree',
		onclick : function(nodeId) {
		  Ext.get('deptId').dom.value = nodeId;
		}
	});
	dtree.init();	
	
});
</script>
</body>
</html>