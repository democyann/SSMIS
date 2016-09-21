<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/pages/common/taglibs.jsp"%>
<form id="pageForm" style="margin-right: 10px;">
	  <a href="javascript:goToPage(1);"  title="第一页">
	  <img src="${ctx}/images/grid/firstPage.gif"></a>&nbsp;&nbsp; 
	  
	  <s:if test="page.hasPreviousPage">
	    <a href="javascript:goToPage('${page.pageNo-1}');" title="上一页">
	    <img src="${ctx}/images/grid/prevPage.gif"></a>
	  </s:if>
	  <s:else>
	    <img src="${ctx}/images/grid/prevPageDisabled.gif" title="已是第一页">
	  </s:else>&nbsp;&nbsp; 
	  
	  <s:if test="page.hasNextPage">
	    <a href="javascript:goToPage('${page.pageNo+1}');" title="下一页">
	    <img src="${ctx}/images/grid/nextPage.gif"></a>
	  </s:if>
	  <s:else>
	    <img src="${ctx}/images/grid/nextPageDisabled.gif" title="没有下一页">
	  </s:else>&nbsp;&nbsp; 
	  
	  <a href="javascript:goToPage('${page.pages}');"  title="最后一页">
	  <img src="${ctx}/images/grid/lastPage.gif"></a>&nbsp;&nbsp; 
	   &nbsp;第<font color="green">${page.pageNo}</font>页&nbsp;共<font color="green">${page.pages}</font>页&nbsp;
	   &nbsp;共<font color="red">${page.rows}</font>条&nbsp;
	  <span>前往</span>
	  <input type="text" id="pageNo" style="width:35px; margin: 0px 2px;" class="required digits">页
	  <input type="button" class="button" onclick="getPageNoGoto()" value="前往">
	  <input type="hidden" id="totalNum" value="${page.pages}">
  </form>
<script type="text/javascript">
	//定义验证
	jQuery.extend(jQuery.validator.messages, {
		required : "",
		digits : ""
	});
	
	var validator = $("#pageForm").validate();
	
	function goToPage(index) {
		$("#ec_p").val(index);
		$("#pageQueryForm").submit();
	}

	//获得输入框页码，跳转
	function getPageNoGoto() {

		if (validator.form()) {//调用验证		
			var pageNo = $("#pageNo").val();
			var totalNum = $("#totalNum").val();
		    if(pageNo < 1){
		    	pageNo = 1;
		    }
		    if (pageNo > totalNum){
		    	pageNo = totalNum;
		    }
			goToPage(pageNo);
		}
	}
</script>