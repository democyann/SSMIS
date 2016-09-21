<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@page import="com.systop.core.ApplicationException" %>
<%@include file="/common/taglibs.jsp" %>
<%
    Exception ex = null;
    if (pageContext.getException() instanceof ApplicationException) {
        ex = pageContext.getException();
        pageContext.setAttribute("ex",ex);
    } else if (pageContext.getException().getCause() instanceof ApplicationException) {
        ex = (Exception) pageContext.getException().getCause();
    }
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <script type="text/javascript" src="${ctx}/scripts/jquery/jquery-1.7.2.min.js"></script>
    <script type="text/javascript" src="${ctx}/scripts/cookie.js"></script>
    <LINK href="${ctx}/styles/style.css" type='text/css' rel='stylesheet'>
    <title>500 Error</title>
</head>
<body>
<div align="center">
    <div class="error_msg">
        <img src="${ctx}/images/error/500.jpg" width="450" style="width: 450px;">
        <%
            if (ex != null) {
                out.println(ex.getMessage());
            } else {
                out.println("<span/>对不起，服务器无法响应您的请求!</span/>");
            }
        %>
    </div>
</div>
<div align="center">
    <p>&nbsp;</p>

    <p>
        <%
            //这里写的不好
            String __context = request.getContextPath();
            String __path;
            String __title;
            if (ex == null) {
                __path = __context + "/main.do";
                __title = "返回首页";
            } else {
                __path = __context;
                if(__context.equals(""))
                    __path = "/";
                __title = "重新登录";
            }
        %>

        <a href="<%=__path%>" target="_top" id="redirectLink"><%=__title%></a>
    </p>
</div>

<% if(ex!=null){%>
<script type="text/javascript">
    //因为没有登录导致的问题,可以在登录之后返回之前请求的页面,非首页,没有框架的前提下
    $(function(){
        if(location.href.indexOf("main.do")==-1 && window.top===window){
            _setCookie("urlFrom",encodeURI(location.pathname+location.search),undefined,URL_PREFIX);
        }
    })
</script>
<%}%>
</body>
</html>
