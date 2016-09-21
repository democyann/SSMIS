<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<%@include file="/common/meta.jsp"%>
<%@include file="/common/ec.jsp"%>
<%@include file="/common/extjs.jsp" %>
<%@include file="/common/meta.jsp"%>
<script type="text/javascript" src="${ctx}/scripts/calendar/WdatePicker.js"></script>
<title>字典管理</title>
<script type="text/javascript">
function remove(id) {
	if (confirm("您确认要删除该信息吗!")) {
		window.location.href = "remove.do?model.id=" + id;
	}
}
function onRemove() {
    var sels = document.getElementsByName('selectedItems');
    var checked = false;
    for(i = 0; i < sels.length; i++) {
        if(sels[i].id == 'selectedItems' && sels[i].checked) {
           checked = true;
           break;
        }
    } 
    if(!checked) {
        alert('请至少选择一条信息。');
        return;
    }
    
    if(confirm("您确定删除所选信息吗？")) {
       var form = document.getElementById("ec");
       form.action = "remove.do";
       form.submit();
    } else {
       return false;
    }
}

$(function(){

	$('#uploadVal').bind('change',function(){
		var form = $('#importForm');
		var uploadVal = $(this).val();
		if(uploadVal.lastIndexOf('\.tpf') != -1){
			overlay();
			form.submit();
		}else{
			alert("请选择正确的导入文件类型！\n例：*.tpf");
		}
	});
});
</script>
</head>
<style type="text/css">
.button {
	width:60px;
	height: 20px;
	padding: 2px 0px 2px 0px;
	background-color: #D2E0F1;
	border: 1px solid #99BBE8;
	background-image: url("../../../images/grid/footerBg.gif");
}
#uploadVal{
	position:absolute;
	filter:alpha(opacity=0);
    -moz-opacity: 0.55;
    opacity: 0.55;        
    z-index:10;
    float: left;
    left:605px;
    top: 6px;
}
#button{
	position:absolute;
	float: left;
	z-index: 0;
	left: 644px;
    top: 6px;
}
#export{
	position:absolute;
	float: left;
	z-index: 0;
	left: 580px;
    top: 6px;
}
</style>
<body>
 <%@include file="/common/messages.jsp" %>
<div class="x-panel">
  <div class="x-toolbar" style="">
     <table width="100%" style="position: relative;top: 5px;">
		     <tr>
			   <td>
	            <form action="adminIndex.do" method="post">
				   <table width="100%">
				     <tr>
				      <td width="2%">名称：</td>
				      <td width="5%"><s:textfield name="model.name"></s:textfield></td>
				      <td width="2%">编码：</td>
				      <td width="5%"><s:textfield name="model.code"></s:textfield></td>
				      <td width="2%">类型：</td>
				      <td width="5%"> <s:select list="dictMap" name="model.category" headerKey="" headerValue="请选择" cssStyle="width:120px;"/></td>
				      <td width="80%"><input type="submit" value="查询" class="button"></td>
				</form>
				      <td style=" padding-left:5px; padding-top:5px;" align="right">   
				        <a href="${ctx}/admin/dict/edit.do"><img src="${ctx}/images/icons/add.gif"/>新建</a>&nbsp;
				      </td>
				      </tr>
				    </table>
         </td>
             <td style=" padding-left:5px; padding-top:5px;" align="right">     
             	<a href="#" onclick="onRemove()">
             		<img src="${ctx}/images/icons/delete.gif" />删除</a>
             	</td>
       </tr>
     </table>
   </div>
   <div class="x-panel-body">
     <div style="margin-left:-3px;" align="center">
     <ec:table items="items" var="item" retrieveRowsCallback="limit" sortRowsCallback="limit"
	   action="adminIndex.do" 
	   useAjax="true"
	   doPreload="false"
	   editable="false" 
	   sortable="true"
	   rowsDisplayed="17" 
	   generateScript="true" 
	   resizeColWidth="true"
	   classic="false" 
	   width="100%" 
	   height="392" 
	   minHeight="392"
	   toolbarContent="navigation|pagejump|refresh|extend|status">
	   <ec:row>
	     <ec:column width="40" property="_s" title="选择" style="text-align:center" sortable="false">
			<input type="checkbox" name="selectedItems" id="selectedItems" value="${item.id}" class="checkbox"/>
		</ec:column>
		<ec:column width="160" property="name" title="名称" />
		<ec:column width="110" property="code" title="编码" />
		<ec:column width="90" property="category" title="类型"  cell="com.systop.common.modules.dict.webapp.cell.DictMapCell" />
		<ec:column width="180" property="descn" title="备注" />
		<ec:column width="80" property="createTime" title="创建日期" cell="date" />
		<ec:column width="90" property="user.name" title="创建人" />
		<ec:column width="60" property="_0" title="操作" style="text-align:center">
			<a href="edit.do?model.id=${item.id}">编辑</a> <%-- |
			<a href="#" onclick="remove('${item.id}')" >删除</a> --%>
		</ec:column>
	  </ec:row>
    </ec:table>
    </div>
  </div>
</div>
<div id="win" class="x-panel">
    <div id="school-grid"></div>
</div>
<script type="text/javascript" src="${ctx}/pages/tpf/material/school.js"></script>
</body>
</html>