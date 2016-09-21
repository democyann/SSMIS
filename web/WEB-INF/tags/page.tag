<%@ tag pageEncoding="UTF-8"%>
<%@ attribute name="page" type="com.systop.core.dao.support.Page" required="true"%>
<%@ attribute name="pageList" type="java.lang.String" required="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	int current = page.getPageNo();
	int pageSize = page.getPageSize();
	
	int startCount = current * pageSize - pageSize + 1; 
	int endCount = startCount + page.getData().size() - 1;
	request.setAttribute("current", current);
	request.setAttribute("startCount", startCount);
	request.setAttribute("endCount", endCount);
	
	String[] pageSizeArray = pageList.split(",");
%>


<div class="pagebar">
	<table style="width: 100%;">
		<tr>
			<td align="right" style="padding-right: 20px;">
				<%
					if (page.getPageNo() > 1) {
				%> <a href="javascript:void(0);" onclick="goToPage(1)" class="easyui-linkbutton"
				data-options="iconCls:'pagination-first', plain:true"></a> <a href="javascript:void(0);"
				onclick="goToPage('${current - 1}')" class="easyui-linkbutton" data-options="iconCls:'pagination-prev', plain:true"></a>
				<%
					}else{
				%> <a href="#" class="easyui-linkbutton"
				data-options="iconCls:'pagination-first', disabled:true, plain:true"></a> <a href="#" class="easyui-linkbutton"
				data-options="iconCls:'pagination-prev', disabled:true, plain:true"></a> <%
 	}
 %> <%
 	if (page.getPageNo() < page.getPages()){
 %>
				<a href="javascript:void(0);" onclick="goToPage('${current + 1}')" class="easyui-linkbutton"
				data-options="iconCls:'pagination-next', plain:true"></a> <a href="javascript:void(0);"
				onclick="goToPage('${page.pages}')" class="easyui-linkbutton" data-options="iconCls:'pagination-last', plain:true"></a>
				<%
					}else{
				%> <a href="#" class="easyui-linkbutton" data-options="iconCls:'pagination-next', disabled:true, plain:true"></a>
				<a href="#" class="easyui-linkbutton" data-options="iconCls:'pagination-last', disabled:true, plain:true"></a> <%
 	}
 %>

				<span>每页</span> <select id="selectedPageSize" onchange="goToPage(1)">
					<%
						for (String size : pageSizeArray) {
					%>
					<%
						String selected = "";
									if (pageSize == Integer.parseInt(size)) {
										selected = "selected=\"selected\"";
									}
					%>
					<option value="<%=Integer.parseInt(size.trim())%>" <%=selected%>><%=size%></option>
					<%
						}
					%>
			</select> <span>条</span> <span style="margin-left: 5px;">前往</span> <input type="text" id="barPageNo"
				style="width: 30px; height: 15px; line-height: 15px;" value="${page.pageNo}"> <span>&nbsp;/&nbsp;${page.pages}页</span>&nbsp;
				<a href="javascript:void(0);" onclick="getPageNoGoto()" class="easyui-linkbutton"
				data-options="iconCls:'icon-search', plain:true">Go</a> &nbsp; <a href="javascript:void(0);"
				onclick="getPageNoGoto()" class="easyui-linkbutton" data-options="iconCls:'pagination-load', plain:true"
				title="刷新数据"></a> &nbsp; <span style="color: green; font-weight: bold;">共${page.rows}条记录&nbsp;&nbsp;显示${startCount}-${endCount}</span>
			</td>
		</tr>
	</table>
</div>
<script type="text/javascript">
	function goToPage(page) {
		$("#pageNo").val(page);
		$("#pageSize").val($("#selectedPageSize option:selected").val());
		$("#pageQueryForm").submit();
	}

	//获得输入框页码，跳转
	function getPageNoGoto() {
		var barPageNo = $("#barPageNo").val();
		var totalNum = '${page.pages}';
		if (isNaN(barPageNo)) {
			$("#barPageNo").val(1);
			barPageNo = 1;
		}

		if (barPageNo < 1) {
			barPageNo = 1;
		}
		if (barPageNo > totalNum) {
			barPageNo = totalNum;
		}
		goToPage(barPageNo);
	}

	function submitSearch() {
		$("#pageQueryForm").submit();
	}

	function resetSearch() {
		document.getElementById("pageQueryForm").reset();
	}
</script>


