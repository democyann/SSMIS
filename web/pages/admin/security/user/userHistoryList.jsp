<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<%@include file="/common/meta.jsp" %>
<%@include file="/common/ec.jsp" %>
<script type="text/javascript" src="${ctx}/scripts/my97/WdatePicker.js"></script>
<title>用户登录记录</title>
</head>
<body>
<div class="x-panel">
     <div class="x-toolbar">
        <s:form action="userHistoryList.do" method="post" cssStyle="margin:3px;">
          	&nbsp;时间&nbsp; 
          	&nbsp;从:<input type="text" name="beginDate" readonly="readonly" style="width: 120px" value='${param.beginDate}' onfocus="WdatePicker({skin:'blueFresh',dateFmt:'yyyy-MM-dd'})" class="Wdate 22"/>&nbsp;      
          	&nbsp;至:<input type="text" name="endDate" readonly="readonly" style="width: 120px" value='${param.endDate}' onfocus="WdatePicker({skin:'blueFresh',dateFmt:'yyyy-MM-dd'})" class="Wdate 22"/>
	        &nbsp;&nbsp;<s:submit value="查询" cssClass="button"></s:submit>
         </s:form>
    </div>     
    <div class="x-panel-body"> 
		<ec:table items="items"  var="item"  retrieveRowsCallback="limit" sortRowsCallback="limit" 
			action="userHistoryList.do" 
			useAjax="false"
			doPreload="false" 
			pageSizeList="20,50" 
			editable="false"
			sortable="false" 
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
				<ec:column width="100" property="user.loginId" title="用户名" />
				<ec:column width="100" property="user.name" title="姓名" />
				<ec:column width="120" property="dept.name" title="所属处室" />
				<ec:column width="140" property="loginTime" title="登录时间" cell="date" format="yyyy-MM-dd HH:mm" />
				<ec:column width="120" property="loginIp" title="登录地址" />
				
			</ec:row>
		</ec:table>
	</div>
</div>
</body>
</html>