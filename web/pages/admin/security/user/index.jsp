<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<%@include file="/common/meta.jsp" %>
<%@include file="/common/ec.jsp" %>
<%@include file="/common/extjs.jsp" %>
    <style type="text/css">
        #ec_toolbar
        {
            position: static !important;
        }
        #ec_bodyZone
        {
            overflow-x:hidden !important;
        }
        .detail-button
        {
            height: 14px;
        }
        
        .online{
        	color: green;
        	font-weight: bold;
        }
        .offline{
        	color:silver;
        }
        .header{
        	width: auto;
        	padding: 5px 3px 4px 5px;
			border: 0px solid #99bbe8;
			overflow: hidden;
			zoom: 1;
			color: #15428b;
			font: bold 12px tahoma, arial, verdana, sans-serif;
			padding: 5px 3px 4px 5px;
			line-height: 15px;
        }
        .search{
        	border-color:#a9bfd3;border-style:solid;border-width:0 0 1px 0;padding:2px;background:#d0def0 url(../images/default/toolbar/bg.gif);;
        }
    </style>
</head>
<body>
	<script type="text/javascript">
		window.onload=function(){
			if($("#jinggao").val()==1){
				Ext.my().msg('', '您已经成功修改选择用户。');
			}else if($("#jinggao").val()==0){
				Ext.my().msg('', '修改选择用户发生错误，请确认要修改用户信息。');
			}
		}
		/**
		 * 提交表单
		 */
		function submitForm() {
			$("#queryForm").submit();
		}

		function changeUser(formid, msg) {
			var checked = false;
			$('input').each(function(i, item) {
				if (item.checked && item.id == 'selectedItems') {
					checked = true;
				}
			});
			if (!checked) {
				alert('请至少选择一个用户');
				return;
			}

			if (confirm(msg)) {
				$('#ec').attr("action", $('#' + formid).attr("action"));
				//ec.from里面已经包含model.type项,不相信查看源代码
				$('#ec').submit();
			}
		}
		function test(id){
			if (confirm("确定初始化该用户的密码！")) {
				window.location.href="rePass.do?model.id="+id+"&only='1'";
			}
		}
	</script>
<s:form action="user/remove" id="removeForm"></s:form>
<s:form action="user/unsealUser" id="unsealForm"></s:form>
<s:form action="user/delectUser" id="delectForm"></s:form>
<s:form action="user/rePass" id="startForm"></s:form>
<div class="x-panel">
<input type="hidden" id="jinggao" value="${jinggao }">
  <div class="search" style="width: 100%;">
    <table style="line-height: 15px; width: 1110px;">
      <tr>
        <td> 
          <form id="queryForm" action="index.do" method="post">
          &nbsp;用户名：<s:textfield id="userLoginId" name="model.loginId"  cssStyle="height:20px;width:80px;"/>
          &nbsp;姓名：<s:textfield id="userName" name="model.name"  cssStyle="height:20px;width:80px;"/>
          &nbsp;状态：<s:select id="userStatus" name="model.status" list='#{"1":"可用","0":"禁用"}' onchange="submitForm()" cssStyle="height:20px;width:80px;"/>&nbsp;
          &nbsp;<input type="submit" value="查询" class="button" />
          </form>
        </td>
        <stc:role ifAnyGranted="ROLE_ADMIN">
        <td width="15%" align="right">
          <a href="${ctx}/security/user/editNew.do">
          <img src="${ctx}/images/icons/add.gif"/> 新建</a></td>
        <td><span class="ytb-sep"></span></td>
        <td>
         <a href="javascript:changeUser('unsealForm','确定要启用所选用户吗?');">
          <img src="${ctx}/images/exticons/recommend.gif"/> 启用</a></td>
        <td><span class="ytb-sep"></span></td>
        <td>
          <a href="javascript:changeUser('removeForm','确定要禁用所选用户吗?');">
          <img src="${ctx}/images/grid/clear.gif"/> 禁用</a></td>
        <td>
        <span class="ytb-sep"></span></td>
        <td>
          <a href="javascript:changeUser('delectForm','确定要删除所选用户吗?');">
          <img src="${ctx}/images/icons/delete.gif"/> 删除</a></td>
        <td> <span class="ytb-sep"></span></td>
         <td>
          <a href="javascript:changeUser('startForm','确定要初始化所选用户的密码吗?');">
          <img src="${ctx}/images/icons/repass.png"/> 初始化密码</a></td>
        <td> <span class="ytb-sep"></span></td>
        </stc:role>
        <td>
          <a href="${ctx}/security/user/index.do">
          <img src="${ctx}/images/exticons/refresh.gif"/> 刷新</a></td>
      </tr>
      
    </table>
    </div>   
    <div >
    <table cellpadding="0" cellspacing="0" style="border-bottom: 1px solid #99bbe8;width: 1110px;">
    	<tr>
    	<td width="250px" valign="top">
    			<iframe name="users" width="248px" height="550px" src="${ctx}/pages/admin/security/user/deptTreeClose.jsp" frameborder="0" scrolling="no"></iframe>
    		</td>
    		<td valign="top">
    			<ec:table items="items" var="item" retrieveRowsCallback="limit" sortRowsCallback="limit"
   					action="index.do"
   					useAjax="false"
				  	doPreload="false" 
				  	maxRowsExported="10000000" 
				  	pageSizeList="20,50"
				  	editable="false" 
				  	rowsDisplayed="20"
				  	generateScript="true"
				  	resizeColWidth="true"
				  	classic="false"
				  	width="99.9%"
				  	height="518px" 
				  	minHeight="518"
				  	toolbarContent="navigation|pagejump|pagesize|refresh|extend|status">
					<ec:row>
						<ec:column width="37" property="_s" title="选择" sortable="false" style="text-align:center">
							<input type="checkbox" name="selectedItems" id="selectedItems" value="${item.id}" class="checkbox" />
						</ec:column>
						<ec:column width="80" property="loginId" title="用户名" sortable="true"/>
						<ec:column width="60" property="name" title="姓名" sortable="true"/>
						<ec:column width="55" property="sex" title="性别"
							style="text-align:center" sortable="true">
							<s:if test='#attr.item.sex == "M"'>男</s:if>
							<s:else>女</s:else>
						</ec:column>
						<ec:column width="80" property="dept.name" title="所属机构" sortable="true" style="white-space:nowrap;word-break:keep-all;overflow:hidden;text-overflow:ellipsis;"/>
						<%-- <ec:column width="80" property="position.name" title="职务" sortable="true" style="white-space:nowrap;word-break:keep-all;overflow:hidden;text-overflow:ellipsis;"/> --%>
						<ec:column width="80" property="partTimeJobDept.name" title="兼职机构" sortable="true" style="white-space:nowrap;word-break:keep-all;overflow:hidden;text-overflow:ellipsis;"/>
						<%-- <ec:column width="80" property="partTimeJobPosition.name" title="兼职职位" sortable="true" style="white-space:nowrap;word-break:keep-all;overflow:hidden;text-overflow:ellipsis;"/> --%>
						<%-- <ec:column width="90" property="phone" title="办公电话"/> --%>
						<ec:column width="90" property="mobile" title="手机"/>
						<ec:column width="80" property="ol" title="在线状态" sortable="true">
							<c:if test="${item.ol == '1' }">
								<span class="online">在线</span>
							</c:if>
							<c:if test="${item.ol == null || item.ol =='0'}">
								<span class="offline">离线</span>
							</c:if>
						</ec:column> 
						<ec:column width="46" property="_status" title="状态" style="text-align:center;" >
							<s:if test="#attr.item.status == 1">
								<img src="${ctx}/images/icons/accept.gif" title="可用">
							</s:if>
							<s:elseif test="#attr.item.status == 0">
								<img src="${ctx}/images/grid/clear.gif" title="禁用">
							</s:elseif>
						</ec:column>
						<ec:column width="100" property="_opt" title="操作" style="text-align:center">
							<a href="view.do?model.id=${item.id}" title="查看"> 
									<img src="${ctx}/images/icons/zoom.gif" class="detail-button"></a>&nbsp;
							<stc:role ifAnyGranted="ROLE_ADMIN">
								<a href="javascript:assignRoles('${item.id}');" title="角色">
									<img src="${ctx}/images/icons/flowpic.gif"></a>&nbsp;
								<a href="edit.do?model.id=${item.id}" title="编辑"> 
									<img src="${ctx}/images/icons/modify.gif"></a>
								<a href="javascript:void(0)" onclick="test(${item.id})" title="初始化密码"> 
									<img src="${ctx}/images/icons/repass.png"></a>
							</stc:role>
						</ec:column>
					</ec:row>
				</ec:table>
			</td>
    	</tr>	
    </table>
	
	</div>
</div>


<div id="win" class="x-hidden">
    <div class="x-window-header">角色列表</div>
    <div id="role_grid"></div>
</div>
<script type="text/javascript" src="${ctx}/pages/admin/security/user/user.js">
</script>
<div id='load-mask'></div>
</body>
</html>