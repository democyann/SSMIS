<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<%@include file="/pages/common/taglibs.jsp"%>
<%@include file="/pages/common/meta.jsp"%>
<%@include file="/common/validator.jsp"%>
		<%@include file="/common/extjs.jsp" %>
 </head>
<body>
<div class="r_main">
  <div class="x-toolbar" style="width: 100%">
    <table style="line-height: 25px;" width="100%">
      <tr>
        <td> 
          <form id="pageQueryForm" action="show.do" method="post">
          &nbsp;用户名：<s:textfield id="userLoginId" name="model.loginId"  cssStyle="height:20px;"/>&nbsp;&nbsp;
          &nbsp;姓　名：<s:textfield id="userName" name="model.name"  cssStyle="height:20px;"/>&nbsp;&nbsp;
          <s:hidden name="ec_p" id="ec_p" />
          &nbsp;<s:submit value="查询" cssClass="button" />
          </form>
        </td>
        <td align="right"><a href="${ ctx}/addressBook/index.do"><img src="${ctx}/images/proj/flow_chart.png">个人通讯录</a></td>
      </tr>
    </table>
    </div>   
    <div>
    <table cellpadding="0" cellspacing="0" style="border-bottom: 1px solid #99bbe8;width: 100%;">
    	<tr>
    		<td width="205" valign="top">
    			<iframe name="users" width="205" height="510" src="${ctx}/pages/admin/security/user/showDeptTree.jsp" frameborder="0" scrolling="no"></iframe>
    		</td>
    		<td valign="top">
    			  <table id="fineTable" width="100%" cellpadding="0" cellspacing="0" align="center" style="margin-bottom:10px;table-layout: fixed;"> 
					<tr class="title">
			          <th width="3%" style="text-align: center;">No.</th>
			          <th width="7%" style="text-align: center;">姓名</th>
			          <!-- <th width="5%" style="text-align: center;">性别</th>
			          <th width="7%" style="text-align: center;">是否在线</th> -->
			          <th width="9%" style="text-align: center;">处室</th>
			          <th width="9%" style="text-align: center;">职务</th>
			          <th width="13%" style="text-align: center;">办公电话</th>
			          <th width="13%" style="text-align: center;">移动电话</th>
			          <th width="13%" style="text-align: center;">Email</th>
		             </tr>
		          	<s:iterator value="items" var="item" status="st">
					 <tr onmousemove="this.className='trOver';" onmouseout="this.className='trOut';">
						<td align="center"> ${(page.pageNo - 1) * 20 + st.index + 1}</td>
						<td align="center">${item.name }</td>
						<%-- <td align="center">
							<s:if test='#attr.item.sex == "M"'>男</s:if>
							<s:else>女</s:else>
						</td>
						<td align="center">
							<s:if test="#attr.item.online">
								<span class="online"><font color="green">在线</font></span>
							</s:if>
							<s:else>
								<span class="offline">离线</span>
							</s:else>
						</td> --%>
						<td align="center">${item.dept.name}</td>
						<td align="center">${item.position.name}</td>
						<td>${item.phone}</td>
						<td>${item.mobile}</td>
						<td>${item.email}</td>
					</tr>
					</s:iterator>
				</table>
			</td>
    	</tr>	
    			<tr>
		<td colspan="13" style="border: 0px; padding-top: 10px;" align="right"><%@include file="/pages/common/page.jsp"%></td>
	</tr>
    </table>
	</div>
</div>
<script type="text/javascript" src="${ctx}/pages/admin/security/user/user.js">
</script>
<div id='load-mask'></div>
</body>
</html>