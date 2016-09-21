<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<form id="pageForm">
	  <a href="#" onclick="goToPage(1)"  title="第一页">
	  <img src="${ctx}/images/grid/firstPage.gif"></a>&nbsp;&nbsp; 
	  
	  <s:if test="page.hasPreviousPage">
	    <a href="#" onclick="goToPage('${page.pageNo-1}')" title="上一页">
	    <img src="${ctx}/images/grid/prevPage.gif"></a>
	  </s:if>
	  <s:else>
	    <img src="${ctx}/images/grid/prevPageDisabled.gif" title="已是第一页">
	  </s:else>&nbsp;&nbsp; 
	  
	  <s:if test="page.hasNextPage">
	    <a href="#" onclick="goToPage('${page.pageNo+1}')" title="下一页">
	    <img src="${ctx}/images/grid/nextPage.gif"></a>
	  </s:if>
	  <s:else>
	    <img src="${ctx}/images/grid/nextPageDisabled.gif" title="没有下一页">
	  </s:else>&nbsp;&nbsp; 
	  
	  <a href="#" onclick="goToPage('${page.pages}')"  title="最后一页">
	  <img src="${ctx}/images/grid/lastPage.gif"></a>&nbsp;&nbsp; 
	  
	  <font color="red">${page.pageNo}</font>/共<font color="red">${page.pages}</font>页&nbsp;&nbsp;
	  <span>前往</span>
	  <input type="text" id="pageNo" value="${page.pageNo}" style="width:25px; margin: 0px 2px;" class="required digits">页
	  <a href="#" onclick="getPageNoGoto()">GO</a>
	  <input type="hidden" id="totalNum" value="${page.pages}">
  </form>
<script type="text/javascript">
//定义验证
jQuery.extend(jQuery.validator.messages, { required : "", digits : ""});
var validator = $("#pageForm").validate();;
function goToPage(index) {
	$("#ec_p").val(index);
	$("#pageQueryForm").submit();
}

//获得输入框页码，跳转
function getPageNoGoto(){
	if(validator.form()){//调用验证
		var pageNo = $("#pageNo").val();
	    if(pageNo < 1){
	    	pageNo = 1;
	    }
	    if (pageNo > $("#totalNum").val()){
	    	pageNo = $("#totalNum").val();
	    }
		goToPage(pageNo);
	}
}
</script>