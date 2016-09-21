<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8" />
<%@include  file="/common/taglibs.jsp"%>
<html>
<head>
    <title>重置密码</title>
    <stc:ifLogin>
        <meta http-equiv="refresh" content="0; url=${ctx}/main.do">
    </stc:ifLogin>
    <link rel="icon" href="${ctx}/images/logo.png" type="image/png">
    <link rel="shortcut icon" href="${ctx}/images/logo_0.ico">
    <LINK href="${ctx}/styles/style.css" type='text/css' rel='stylesheet'>

    <style type="text/css">
        .STYLE3 {
            font-family: sans-serif;
            color: #11588E;
            font-weight: bold;
        }
        .username{
            height:28px;
            width:200px;
            padding-left:21px;
            background-image:url(${ctx}/images/icons/user.gif);
            background-position:1px 4px;
            background-repeat:no-repeat;
            border: 1px solid #88ABDC;
            color:red;
            font-weight: bold;
            font-family: sans-serif;
        }
        .password{
            height:28px;
            width:200px;
            padding-left:21px;
            background-image:url(${ctx}/images/icons/lock_go.gif);
            background-position:2px 4px;
            background-repeat:no-repeat;
            border: 1px solid #88ABDC;
            color:red;
            font-weight: bold;
            font-family: cursive;
        }
    </style>
</head>
<body bgcolor="#FFFFFF">
<div style="background-color:#8AB2ED; height: 25px;"></div>
<div style="height:30px;"></div>
<table width="99%" align="center" height="480" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td width="48%" align="center" valign="middle">
            <img src="${ctx}/images/scos/left.jpg">
        </td>
        <td width="2%" align="center">
            <img src="${ctx}/images/scos/m.jpg">
        </td>
        <td width="48%" valign="middle">
            <form id="loginForm" action='${ctx}/security/user/resetPassword.do' method="post" >
                <table width="431" align="center" cellpadding="0" cellspacing="0" style="line-height: 35px;">
                    <tr>
                        <td colspan="2">
                            <c:if test="${param.reset_error == '1'}">
                                <div style="border-bottom: 1px dashed #88ABDC; width: 270px; padding: 5 0 2 8;">
                                    <img border="0" src="<c:url value='/images/icons/warning.gif'/>"/>&nbsp;用户不存在,或者手机号码不正确,经检查输入!
                                </div>
                            </c:if>
                            <c:if test="${param.reset_error == '2'}">
                                <div style="border-bottom: 1px dashed #88ABDC; width: 270px; padding: 5 0 2 8;">
                                    <img border="0" src="<c:url value='/images/icons/warning.gif'/>"/>&nbsp;手机号码不正确,不能发送,请联系管理员!
                                </div>
                            </c:if>
                        </td>
                    </tr>

                    <tr>
                        <td width="83" align="center" class="STYLE3">用户名：</td>
                        <td width="348">
                            <input type="text" name="loginId"  class="username"/>
                        </td>
                    </tr>
                    <tr>
                        <td width="83" align="center" class="STYLE3">手机号码：</td>
                        <td width="348">
                            <input type="text" name="mobile"  class="username"/>
                        </td>
                    </tr>

                    <tr>
                        <td colspan="2" style="padding:10px 0px 0px 83px;position: relative;">
                            <input type="submit" class="button" value="重置密码" />
                            <input type="button" class="button" value="返回" onclick="location.href='${ctx}'"/>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2" style="padding-top:50px;">
                            <img src="${ctx}/images/scos/kouhao.jpg">
                        </td>
                    </tr>
                </table>
            </form>
        </td>
    </tr>
</table>
<div style="height:20px;"></div>
<div style="border-top: 4px solid #8AB2ED;">
    <div style="padding-top:10px; text-align:center;">
        翰子昂移动互联实验室
    </div>
    <div style="padding-top:5px; text-align:center; font-family: arial;">
        &copy;ZON'J Co,Ltd all right is reserved
    </div>
</div>


</body>
</html>
