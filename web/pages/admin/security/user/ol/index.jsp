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
    </style>
</head>
<body>
	<script type="text/javascript">
		/**
		 * 提交表单
		 */
		function submitForm() {
			$("#queryForm").submit();
		}

		function checkAll(){
			window.location.href=URL_PREFIX+"/security/user/olIndex.do";
		}
	</script>
<div class="x-panel">

  <div class="x-toolbar" style="min-width: 916px;width: 1000px">
    <table style="line-height: 25px;width: 1000px;">
      <tr>
        <td> 
          <form id="queryForm" action="olIndex.do" method="post">
          &nbsp;用户名：<s:textfield id="userLoginId" name="model.loginId"  cssStyle="height:20px;width:80px;"/>
          &nbsp;姓名：<s:textfield id="userName" name="model.name"  cssStyle="height:20px;width:80px;"/>
          &nbsp;<input type="submit" value="查询" class="button" />
          &nbsp;<input type="button" onclick="checkAll();" value="查询全部" class="button" />
          &nbsp;<a href="${ctx}/security/user/olIndex.do">
          <img src="${ctx}/images/exticons/refresh.gif"/> 刷新</a></td>
          </form>
      </tr>
    </table>
    </div>   
    <div >
    <table cellpadding="0" cellspacing="0" style="border-bottom: 1px solid #99bbe8;width: 1000px;">
    	<tr>
    		<td width="250px" valign="top">
    			<iframe name="users" width="248px" height="550px" src="${ctx}/pages/admin/security/user/olDeptTree.jsp" frameborder="0" scrolling="no"></iframe>
    		</td>
    		<td valign="top">
    			<ec:table items="items" var="item" retrieveRowsCallback="limit" sortRowsCallback="limit"
   					action="olIndex.do"
   					useAjax="false"
				  	doPreload="false" 
				  	maxRowsExported="10000000" 
				  	pageSizeList="20,50"
				  	editable="false" 
				  	sortable="false" 
				  	rowsDisplayed="20"
				  	generateScript="true"
				  	resizeColWidth="true"
				  	classic="false"
				  	width="99.9%"
				  	height="518px" 
				  	minHeight="518"
				  	toolbarContent="navigation|pagejump|pagesize|refresh|extend|status">
					<ec:row>
						
						<ec:column width="80" property="loginId" title="用户名" />
						<ec:column width="60" property="name" title="姓名" />
						<ec:column width="55" property="sex" title="性别"
							style="text-align:center">
							<s:if test='#attr.item.sex == "M"'>男</s:if>
							<s:else>女</s:else>
						</ec:column>
						<ec:column width="100" property="dept.name" title="处室"/>
						<ec:column width="100" property="position.name" title="职务"/>
						<%-- <ec:column width="90" property="phone" title="办公电话"/> --%>
						<ec:column width="90" property="mobile" title="移动电话"/>
						<ec:column width="80" property="_sfzx" title="是否在线">
							<c:if test="${item.ol == '1' }">
								<span class="online">在线</span>
							</c:if>
							<c:if test="${item.ol == null || item.ol =='0'}">
								<span class="offline">离线</span>
							</c:if>
						</ec:column> 
						<ec:column width="100" property="_opt" title="操作" style="text-align:center">
							<a href="#" onclick="send('${item.id}')">发送邮件</a>
						</ec:column>
					</ec:row>
				</ec:table>
			</td>
    	</tr>	
    </table>
	
	</div>
</div>
</body>
<script type="text/javascript">
	function send(id){
		var conf = "dialogWidth=730px;dialogHeight=530px;status=no;help=no;scrollbars=no;scroll=no";
		var win = window;
		window.showModalDialog(URL_PREFIX+'/mail/sendMsg.do?onlineId=' + id, win, conf);
		
	}
</script>
</html>