<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<%@include file="/common/meta.jsp" %>
<style type="text/css">
.warn {
	color: red;
}
	.main{
		width: 670px;
        overflow-y: auto;
        max-height: 560px;
	}
	.skype{
		width: 110px;
		height: 25px;
		line-height: 25px;
		margin: 5px 3px 5px 0;
		border-bottom: 1px dashed #c0c0c0;
        border-right: 1px dashed #c0c0c0;
		float: left;
        position: relative;

	}
    .skype img
    {
        width: 16px;
    }
    .name-span{
        margin-left: 46px;
        position: absolute;
        left: 0;

    }
    .online-name-span{
        color: blue;
    }
</style>
<title>选择用户</title>
</head>
<body>
	<div style="border-bottom: 1px solid #99BBE8;">
		<form action="showSkypeUser.do" method="post" style="margin: auto;">
			<table style="line-height: 25px;" cellpadding="0" cellspacing="0">
			  <tr>
			  	<td>姓名：&nbsp;</td>
			  	<td>&nbsp;<s:textfield name="model.name" />&nbsp;</td>
			  	<td>
                      &nbsp;<input type="submit" value="查询" class="button">
                      &nbsp;<input type="button" value="刷新" class="button" onclick="location.reload()">
                  </td>
			  </tr>
			</table>
		</form>
	</div>
	<div class="main">
	<s:iterator value="items" var="item">
		<c:if test="${!empty item.oicq}">
		<div class="skype">
            <span class="icon-span">
                <a target="_blank" href="http://wpa.qq.com/msgrd?v=3&uin=${item.oicq}&site=qq&menu=yes" title="qq号码:${item.oicq}">
                    <img src="http://wpa.qq.com/pa?p=2:${item.oicq}:45" style="border:none">
                </a>
            </span>

            <span class="name-span ${item.online?'online-name-span':''}">${item.name}&nbsp;</span>
		</div>
		</c:if>
	</s:iterator>
</div>
<div class="main">
 <span class="warn">
       注意：
               如需网上交流，请在个人资料中设置qq号。
 </span>
</div>
<script type="text/javascript">
    //延迟加载图标,last
    $(".skype img").each(function () {
        $(this).attr("src", $(this).attr("_src"));
    })
</script>
</body>
</html>