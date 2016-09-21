<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>  
<!DOCTYPE html>
<html>
<head>
<%@include file="/common/meta.jsp"%>
<%@include file="/common/pm/jquery.jsp"%>
<%@include file="/common/pm/easyui.jsp"%>
<title>管理登录 -- 翰子昂移动互联实验室</title>
<link href="${ctx}/styles/login.css" rel="stylesheet" type="text/css" />
<stc:ifLogin>
<meta http-equiv="refresh" content="0; url=${ctx}/main.do"/> 
</stc:ifLogin>
<script type="text/javascript">
//屏蔽鼠标右键
$(document).ready(function(){  
    $(document).bind("contextmenu",function(e){  
         return false;  
    });  
 });  



 //清除用户名和密码
function srpwd(){
	$("#j_username").val("");
	$("#j_password").val("");
	document.getElementById("msg").innerHTML="";
}

/*//清除密码
function cleanPassword(){
	$("#j_password").val("");
	document.getElementById("msg").innerHTML="";
} 

  //清除验证码
function cleancaptcha(){
	$("#j_captcha_response").val("");
	document.getElementById("msg").innerHTML="";
}  */

//登录页面若在框架内，则跳出框架
if (self != top) {
	top.location = top.location;
};

function a(arg) {
	var r_obj = null;
	if(typeof(arg) == 'object') {
		return arg;
	}
	r_obj = document.getElementById(arg);
	if (r_obj == null) {
		r_obj = document.getElementsByName(arg)[0];
	}
	return r_obj;
}

window.setTimeout("document.getElementById('j_username').focus();", 50);
document.onkeydown = function(event){
	var e=event||window.event;
	var keyCode = e.keyCode ? e.keyCode : e.which ? e.which : e.charCode;
	var obj = e.srcElement ? e.srcElement : e.target;
	if(keyCode == 13){
		checkData();
	}
};
function checkData(){
	if(document.getElementById("j_username").value.length < 1) {
		document.getElementById("msg").innerHTML="<div style=''><img border='0' src='<c:url value='/images/icons/warning.gif'/>'/>&nbsp;请输入您的用户名！</div>";
     document.getElementById("j_username").focus();
		return false;
	}
	if( document.getElementById("j_password").value.length < 1) {
		document.getElementById("msg").innerHTML="<div style=''><img border='0' src='<c:url value='/images/icons/warning.gif'/>'/>&nbsp;请输入您的密码！</div>";
     document.getElementById("j_password").focus() ;
		return false;
	}
	if($("#suggest").val()=='1'){
		document.getElementById("msg").innerHTML="<div style=''><img border='0' src='<c:url value='/images/icons/warning.gif'/>'/>&nbsp;用户名不能有汉字！</div>";
		document.getElementById("j_username").focus();
		return false;
	}
	document.getElementById('loginForm').submit();
}
function clean(){
	document.getElementById("msg").innerHTML="";
}
/* function imageChange(){
	$('#captchaFrame').attr('src','<c:url value="/captcha.jpg" />'+'?'+Math.random());
	                               
} */
//重置
function reset(){
	$("#j_username").val("");
	$("#j_password").val("");
	//$("#j_captcha_response").val("");
}
//验证不为汉字
function testchina(val){
	if(val==''){
		$("#msg").html("<img border='0' src='<c:url value='/images/icons/warning.gif'/>'/>&nbsp;用户名不能为空！");
		return ;
	}
	if(/[\u4e00-\u9fa5]/g.test(val)){
		$("#msg").html("<img border='0' src='<c:url value='/images/icons/warning.gif'/>'/>&nbsp;用户名不能为中文！");
		$("#suggest").val("1");
	}else{
		$("#msg").html("");
		$("#suggest").val("");
	}
}

</script>
</head>
<body  style="">
<input type="hidden" id="suggest">
		<%           
            Cookie[] cookies = request.getCookies(); 
            String name = "";    
		    String pwd="";  
		    boolean flagOne = false;
		    boolean flagTwo = false;
		    boolean flag = false;
			if(cookies!=null)    
				{  			   
				    for (int i = 0; i < cookies.length; i++)     
				    {    
				       Cookie c = cookies[i];         
				       if(c.getName().equalsIgnoreCase("username"))    
				       {    
				          name = java.net.URLDecoder.decode(c.getValue(),"UTF-8"); 
				          flagOne = true;
				        }    
				       if(c.getName().equalsIgnoreCase("userpwd"))    
				       {    
				          pwd = c.getValue(); 
				          flagTwo = true;
				        }   
				       if(flagOne == true && flagTwo == true){
				    	   flag = true;
				       }
				    }  	
				  } 
			%>

<div class="box" align="center">
<input type="hidden" id="suggest">
	<div class="login_title">
		<img src="${ctx}/images/login_title.png" width="660" height="80">
	</div>
	<div class="login">
		<div class="loginbox">
			<div class="loginbox_left">
				<img src="${ctx}/images/login_danghui.png" width="280" height="167">
			</div>
			<div class="loginbox_right">
				<div class="login_form">
					<div style="height: 15px;"></div>
					<form id="loginForm" action='j_security_check' method="post" style="margin: 0px;" target="_top">
					<table width="280" border="0" align="center" cellpadding="0" cellspacing="0" style="margin: 0 auto;">
						<tr>
							<td width="63" height="35" align="left">用户名：</td>
							<td colspan="2">
								<input type="hidden" ia="flag" name="flag" value="1">
								<input id="j_username" name="j_username" type="text" class="inputstyle" value="<%=name %>" onclick="srpwd();"  style="width: 150px;" />
	        					<span id="suggest"></span>
							</td>
							
						</tr>
						<tr>
							<td height="35" align="left">密&nbsp;&nbsp; 码：</td>
							<td colspan="2">
								<span><input id="j_password" name="j_password" type="password" class="inputstyle" value="<%=pwd %>" style="width: 150px;" />
							</td>
						 <td width="100" >
		        				<% if(flag == false){%>
	      	     					<input  type='checkbox' name='jzpwd' id="chk1" value='yes' style="border: 0px;" />&nbsp;&nbsp;记住密码
	      	   					<%} else if(flag == true){%>
	      	     				<input  type='checkbox' name='jzpwd' id="chk1" value='yes'  checked  style="border: 0px;"/>&nbsp;&nbsp;记住密码
	      	  					<%} %> </span>
	       					 </td> 
						</tr>
					<%-- 	<tr>
							<td height="35" align="left">验证码：</td>
							<td width="77">
								<input name="kaptchaCode" type="text" class="login_input" id="kaptchaCode" size="6">
							</td>
							<td width="140">
								<img id="VerificationCode" src="${ctx}/VerificationCode.jpg"> &nbsp; &nbsp;
								[<a href="javascript:changeCode('${ctx}');">换</a>]
							</td>
						</tr> --%>
						<tr>
							<td align="center" id="msg" style="color:red;" colspan="3">
					        	<c:if test="${param.login_error == 'user_psw_error' || param.login_error == '1'}">
									<img border="0" src="<c:url value='/images/icons/warning.gif'/>" />&nbsp;用户名或密码错误,请重试！
								</c:if>
								<c:if test="${param.login_error == 'too_many_user_error'}">
									<img border="0" src="<c:url value='/images/icons/warning.gif'/>" />&nbsp;该用户已登录!
								</c:if>
								<c:if test="${param.login_error == '2'}">
									<img border="0" src="<c:url value='/images/icons/warning.gif'/>" />&nbsp;用户密码已重置，请重新登录。
								</c:if>
					        </td>
						</tr>
					</table>
					</form>
					<table id="msg_table" width="280" border="0" align="center" cellpadding="0" cellspacing="0" style="margin: 0 auto; color: #F00;">
						<tr>
							<td height="36" id="msg_td">
								<s:if test="#parameters.error_code[0] == 'not_authority_admin'">
									用户未登录
								</s:if>
								<s:if test="hasActionErrors()">
									<s:iterator value="actionErrors">
			    						<s:property escape="false"/><br>
	    							</s:iterator>
								</s:if>
							</td>
						</tr>
					</table>
				</div>
				<div class="login_btn">
					<img src="${ctx}/images/login_btn.png" width="108" height="31" onClick="checkData()">
				</div>
			</div>
		</div>
	</div>
	<div class="copyright">
		版权所有 &copy; 河北新龙信息技术有限公司<br> 备案序号：冀ICP备2014040188号
	</div>
</body>
</html>
