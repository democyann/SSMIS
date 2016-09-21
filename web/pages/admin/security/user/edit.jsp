<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<link rel="stylesheet" type="text/css" href="${ctx}/styles/ec/ecside_style.css" />
<%@include file="/common/meta.jsp"%>
<%@include file="/common/extjs.jsp"%>

<%@include file="/common/validator.jsp"%>
<%@include file="/common/uploadify.jsp"%>

<script type="text/javascript" src="${ctx}/pages/admin/dept/edit.js"></script>
<script type="text/javascript" src="${ctx}/pages/admin/dept/editTwo.js"></script>
<link href="${ctx}/styles/treeSelect.css" type='text/css' rel='stylesheet'>

<script type="text/javascript" src="${ctx}/pages/admin/fileattch/fileattch.js"></script>
<LINK href="${ctx}/pages/admin/fileattch/fileattch.css" type='text/css' rel='stylesheet'>

<script type="text/javascript" src="${ctx}/scripts/my97/WdatePicker.js"></script>

    <script type="text/javascript" src="${ctx}/scripts/jquery/ui/jquery-ui-1.8.21.custom.min.js"></script>
    <link href="${ctx}/scripts/jquery/ui/css/smoothness/jquery-ui-1.8.21.custom.css" rel="stylesheet"/>
    <style>
   	.dept-ol .ui-selecting { background: #FECA40; }
   	.dept-ol .ui-selected { background: #F39814; color: white; }
   	.dept-ol { list-style-type: none; margin: 0; padding: 0; }
    .dept-ol ol { list-style-type: none; margin: 0; padding-left:20px; }
   	.dept-ol li { margin: 3px; padding: 0.4em; height: 18px; }
   	</style>

<script type="text/javascript">
	var rootName = '石家庄瑞海检疫技术服务有限公司';
	var initValue = '${model.dept.name}';
	var dtree;
	if (initValue.length == 0) {
		//initValue = rootName;
	}
	// <s:if test="#attr.editSelf != 1">
	Ext.onReady(function() {
		 dtree = new DeptTree({
			url : '/systop/dept/deptTree.do',
			rootName : rootName,
			initValue : initValue,
			el : 'comboxWithTree',
			innerTree : 'inner-tree',
			onclick : function(nodeId) {
				Ext.get('deptId').dom.value = nodeId;
				/* getPosition(nodeId); */
			}
		});
		dtree.init();
	});
	//</s:if>
	
	//兼职处室
	var rootNameTwo = '石家庄瑞海检疫技术服务有限公司';
	var initValueTwo = '${model.partTimeJobDept.name}';
	if (initValue.length == 0) {
		//initValue = rootName;
	}
	Ext.onReady(function() {
		var dtreeTwo = new DeptTreeTwo({
			url : '/systop/dept/deptTree.do',
			rootName : rootNameTwo,
			initValue : initValueTwo,
			el : 'comboxWithTreeTwo',
			innerTree : 'inner-tree',
			onclick : function(nodeId) {
				Ext.get('deptIdTwo').dom.value = nodeId;
				getPositionTwo(nodeId);
			}
		});
		dtreeTwo.init();
	});
	
	function changePassword(){
		var url = "${ctx}/password/modifyPassword.do";
		var config = "dialogWidth=500px;dialogHeight=250px;status=no;help=no;scrollbars=no";
		window.showModalDialog(url,"",config);
	}
	function changeSkin(){
		var url = "${ctx}/pages/layout/changeSkin.jsp";
		var config = "dialogWidth=500px;dialogHeight=250px;status=no;help=no;scrollbars=no";
		window.showModalDialog(url,window,config);
	}
	
		//alert(${model.dept.id});
		/* getPosition(${model.dept.id});
		getPositionTwo(${model.partTimeJobDept.id}); */
	
	$(function() {
		$.validator.addMethod("isValidIdCard", function(value, element){
			 var ret = false;
	   	     var w = [7 ,9,10,5,8,4,2, 1,6, 3, 7, 9 ,10, 5 ,8 ,4, 2];     	   
			 if (value.length == 18) {
			     //身份证号码长度必须为18，只要校验位正确就算合法
			      var crc = value.substring(17);
			      var a = new Array();
			      var sum = 0;
			      for (var i  = 0; i < 17; i++) {
			        a.push(value.substring(i, i+1));
			        sum += parseInt(a[i], 10)*parseInt(w[i], 10);
			      }
			      sum %= 11;
			      var res = "-1";
			      switch (sum){
			      case 0:{
			        res = "1";
			        break;
			      }
			      case 1:{
			        res = "0";
			        break;
			      }
			      case 2:{
			        res = "X";
			        break;
			      }
			      case 3:{
			        res = "9";
			        break;
			      }
			      case 4:{
			        res = "8";
			        break;
			      }
			      case 5:{
			        res = "7";
			        break;
			      }
			      case 6:{
			        res = "6";
			        break;
			      } 
			      case 7:{
			        res = "5";
			        break;
			     } 
			     case 8:{
			       res = "4";
			       break;
			     } 
			     case 9:{
			       res = "3";
			       break;
			     }
			     case 10:{
			       res = "2";
			       break;
			     }
			 }
			
			 if (crc.toLowerCase() != res.toLowerCase()) {
			    	$("#idCardDescn").html('请输入正确的身份证号！' );
					return false;
				} else {
					$("#idCardDescn").html('');
						if (18 == value.length) { //18位身份证号码
					        birthdayValue = value.charAt(6) + value.charAt(7) + value.charAt(8) + value.charAt(9) + '-' + value.charAt(10) + value.charAt(11)+ '-' + value.charAt(12) + value.charAt(13);
							$('#birthday').val(birthdayValue);
					        if (parseInt(value.charAt(16) / 2) * 2 != value.charAt(16)){
					        	$('input[name="model.sex"]').each(function(i, obj) {
					    			if(obj.value == 'M'){
					    				obj.checked = 'checked';
					    			}
					    		});
					            }
					        else{				        	
					        	$('input[name="model.sex"]').each(function(i, obj) {
					    			if(obj.value == 'F'){
					    				obj.checked = 'checked';
					    			}
					    		})
					         }
		
					    }
				}
				return true;
			}else if (value.length == 15) {
			        //15位的身份证号，只验证是否全为数字
			        var pattern = /\d/;
			        ret = pattern.test(value); 
			   } else if (value.length == 0 ) {
				   $("#idCardDescn").html('');
					return true;
			   } else if (value.length != 15 && value.length != 18) {
					$("#idCardDescn").html('身份证位数错误:' +value.length+'位！' );
					return false;
				}
			  return ret;	   
		   },"");
	});

	$(function() {
		$.validator.addMethod("telephone", function(value, element){
			if (value.length != 0)
			{
				if(value.length != 11){
					$("#telephoneError").html('电话号码数位不正确！');
					return false;
				}else{
					var pattern = /^1\d{10}$/;
			        flag = pattern.test(value); 
			        if(flag){
			        	$("#telephoneError").html('');
				        return true;
				        }else{
				        	$("#telephoneError").html('请输入合法的手机号码！');
				        	return false;
				        }
				}
			 }else{
				$("#telephoneError").html('');
		        return true;
			 }
		},"");
	})

	$(function() {
		$.validator.addMethod("phone", function(value, element){
			if (value.length != 0)
			{	var pattern = /^0\d{2,3}-\d{7,8}$/;
				var pattern2 = /^1\d{10}$/;
		        flag = pattern.test(value);
		        flag2 = pattern2.test(value);
		        if(flag || flag2){
		        	$("#phoneError").html('');
			        return true;
			        }else{
			        	$("#phoneError").html('请输入合法电话号码！如0311-87000000或13900000000');
			        	return false;
			        }
			}else{
				$("#phoneError").html('');
		        return true;
			 }
		},"");
	})
	
	$(function() {
		$.validator.addMethod("fox", function(value, element){
			if (value.length != 0)
			{	var pattern = /^0\d{2,3}-\d{7,8}$/;
		        flag = pattern.test(value);
		        if(flag ){
		        	$("#foxError").html('');
			        return true;
			        }else{
			        	$("#foxError").html('请输入合法电话号码！如0311-87000000');
			        	return false;
			        }
			}else{
				$("#foxError").html('');
		        return true;
			 }
		},"");
	})
	//验证不为汉字
function testchina(val){
	if(val==''){
		$("#warnid").html("用户名不能为空！");
		return ;
	}
	if(/[\u4e00-\u9fa5]/g.test(val)){
		$("#warnid").html("用户名不能为中文！");
		$("#suggest").val("1");
	}else{
		$("#warnid").html("");
		$("#suggest").val("*");
	}
}
</script>

<style type="text/css">
.warn {
	color: red;
}

.title {
	border-bottom: 1px solid #99BBE8;
	font-size: 14px;
	font-weight: bold;
	color: #15428b;
}
</style>
<title>编辑用户</title>
</head>
<body>
<div class="x-panel">
<div style="width: 99%; margin: 0 auto;"><%@ include file="/common/messages.jsp"%></div>
<br>
<input type="hidden" id="suggest">
<s:form action="user/save" id="save" validate="true" method="post" >
	<%-- <s:if test="model.id == null">
		<table id="userLoginInfo" width="810" align="center" border="0">
			<tr>
				<td class="title">
					<span>用户登录信息</span>
				</td>
			</tr>
			<tr>
				<td style="padding: 5px 0px 5px 35px;">
					&nbsp;登录帐号： 
					<s:textfield name="model.loginId" id="loginId" cssStyle="width:140px" maxlength="14" cssClass="required loginId loginIdUnique" />
					<span class="warn">*</span>&nbsp;&nbsp;
					&nbsp;登录密码： 
					<s:password name="model.password" id="pwd" cssStyle="width:140px" maxlength="14" cssClass="required pwd" />
					<span class="warn">*</span>&nbsp;&nbsp;
					&nbsp;确认密码： 
					<s:password name="model.confirmPwd" id="repwd" cssStyle="width:140px" maxlength="14" cssClass="required repwd" />
					<span class="warn">*</span>
					<s:if test="#attr.model.id == null">
					<s:hidden name="model.password" value="111111"/>
					</s:if>
					<s:else>
					<s:hidden name="model.password"/>
					</s:else>
					
				</td>
			</tr>
		</table>
	</s:if> --%>
	<table width="90%" align="center" border="0" style="border: 1px solid #99BBE8;">
		<tr>
			<td colspan="2" class="title">
				<span>用户信息编辑</span>&nbsp;
				<s:if test='editSelf == "1"'>
					<span style="margin-left: 20px; color: green; cursor: pointer;"  title="修改密码" onclick="changePassword()">
						<img src="${ctx}/images/icons/cog.gif">&nbsp;修改个人密码
					</span>
				</s:if>
			</td>
		</tr>
		<tr>
			<td width="50%" valign="top">
			<table width="100%" style="line-height: 25px;">
				<tr>
					<td width="30%" align="right">登录帐号：</td>
					<td width="70%">
						<s:if test="model.id == null">
							<s:textfield name="model.loginId" id="loginId" cssStyle="width:95%;" maxlength="14" cssClass="required loginId loginIdUnique" />
							<span id="warnid" class="warn">*</span>
							<s:hidden name="model.password" value="111111"/>
						</s:if>
						<s:else>
							<s:textfield name="model.loginId" id="loginId" cssStyle="width:95%;" maxlength="14" cssClass="loginId loginIdUnique" readonly="true"/>
							<s:hidden name="model.password"/>
						</s:else>
					</td>
				</tr>
				<tr>
					<td width="30%" align="right">员工姓名：</td>
					<td width="70%">
						<s:textfield name="model.name" cssStyle="width:95%;" maxlength="20" cssClass="required" />
						<span class="warn">*</span></td>
				</tr>
				<tr>
					<td width="30%" align="right">姓名首字母：</td>
					<td>
						<s:textfield name="model.nameFirst" cssStyle="width:95%;" maxlength="20" cssClass="required" />
						<span class="warn">*</span></td>
				</tr>
				<tr>
					<td width="30%" align="right">所属机构：</td>
					<td width="70%">
						<s:hidden name="model.id" id="uid"/>
						<s:hidden name="editSelf" />
						<table cellpadding="0" cellspacing="0">
							<tr>
								<td> 
								   <s:if test="#attr.editSelf == 1">
									  <input type="text" value="${model.dept.name}" id = "dname" class="dept" disabled="disabled"/>
									</s:if>
									<s:else>
									  <div id='comboxWithTree'></div>
									    <div id='comboxWithTreeYc' ></div>
									</s:else>
									<input type="hidden" id="deptId" name="deptId" class="dept" value="${model.dept.id}" />
								</td>
								<td>
									<span id="deptmsg" class="warn"></span>&nbsp; 
									<span class="warn">*</span>
								</td>
							</tr>
						</table>
					</td>
				</tr>
                   <%--  <tr>
                        <td width="30%" align="right">单位/部门：</td>
                        <td width="70%">
                            <s:if test="#attr.editSelf == 1">
                          		 <s:select list="#{'':'请选择'}" name="model.position.id" id="positionId"
                                      cssStyle="width:95%;"  cssClass="checkPositionId" disabled="true" />
                            </s:if>
                            <s:else>
                                <s:select list="#{'':'请选择'}" name="model.position.id" id="positionId"  
                                          cssStyle="width:95%;" cssClass="checkPositionId"/>
                            </s:else>
                             <span class="warn">*</span>
                        </td>
                    </tr> --%>
                    	<tr>
					<td width="30%" align="right">兼职机构：</td>
					<td width="70%">
						<%-- <s:hidden name="model.id" id="uidTwo"/> --%>
						<s:hidden name="editSelf" />
						<table cellpadding="0" cellspacing="0">
							<tr>
								<td> 
								   <s:if test="#attr.editSelf == 1">
									  <input type="text"   value="${model.partTimeJobDept.name}"  id="dname" disabled="disabled"/>
									</s:if>
									<s:else>
									  <div id='comboxWithTreeTwo'></div>
									</s:else>
									<input type="hidden" id="deptIdTwo" name="deptIdTwo"   />
								</td>
								<td>
									<!-- <span id="deptmsg" class="warn"></span>&nbsp;  -->
									<!-- <span class="warn">*</span> -->
								</td>
							</tr>
						</table>
					</td>
				</tr>
                  <%--   <tr>
                        <td width="30%" align="right">兼职职位：</td>
                        <td width="70%">
                            <s:if test="#attr.editSelf == 1">
                          		 <s:select list="#{'':'请选择'}" name="model.partTimeJobPosition.id" id="positionIdTwo"
                                      cssStyle="width:95%;" disabled="true" />
                            </s:if>
                            <s:else>
                                <s:select list="#{'':'请选择'}" name="model.partTimeJobPosition.id" id="positionIdTwo"  
                                          cssStyle="width:95%;" />
                            </s:else>
                             <!-- <span class="warn">*</span> -->
                        </td>
                    </tr> --%>
                    
				<tr>
					<td align="right">身份证号：</td>
					<td>
						<s:textfield id="idCard" name="model.idCard" maxlength="18" cssClass="isValidIdCard" cssStyle="width:95%;"></s:textfield>
				  		<span id="idCardDescn" class="warn"></span>
					</td>
				</tr>
				<tr>
					<td align="right">员工性别：</td>
					<td>
						<s:radio list="%{@com.systop.common.modules.security.user.UserConstants@SEX_MAP}" name="model.sex" cssStyle="border:0px;" onclick="defaultUserPhoto(this.value)"/></td>
				</tr>
				<tr>
					<td align="right">出生日期：</td>
					<td>
						<input type="text" id="birthday" name="model.birthDay" style="width:95%;" readonly="readonly" 
							   value='<s:date name="model.birthDay" format="yyyy-MM-dd"/>'
							   onClick="WdatePicker({skin:'blueFresh'})" class="Wdate" />
					</td>
				</tr>
				<tr>
					<td align="right">手机：</td>
					<td>
						<s:textfield name="model.mobile" id="mobile" cssClass="telephone"  maxlength="18" cssStyle="width:95%;"/>
						<span id="telephoneError" class="error" htmlfor="mobile"></span>
					</td>
				</tr>
				<tr>
					<td align="right">办公电话：</td>
					<td>
						<s:textfield name="model.phone" id="phone" cssStyle="width:95%;" maxLength="20" cssClass="phone"/>
						<span id="phoneError" class="error" htmlfor="phone"></span>
					</td>
				</tr>
				<tr>
					<td align="right">传真号码：</td>
					<td>
						<s:textfield name="model.fox" id="fox" cssStyle="width:95%;" maxLength="40"  cssClass="fox" />
						<span id="foxError" class="error" htmlfor="fox"></span>
					</td>
				</tr>
				<tr>
					<td align="right">紧急联系方式：</td>
					<td>
						<s:textfield name="model.emergencyPhone" id="emergencyPhone" cssStyle="width:95%;" maxlength="100" /></td>
				</tr>
				<tr>
					<td align="right">电子邮件：</td>
					<td>
						<s:textfield name="model.email" id="email" cssStyle="width:95%;" maxLength="255" cssClass="email"/></td>
				</tr>
				<tr>
					<td align="right">QQ：</td>
					<td>
						<s:textfield name="model.oicq" id="oicq" cssStyle="width:95%;" cssClass="number"  maxLength="255" />
					</td>
				</tr>
				<tr>
					<td align="right">最高学历：</td>
					<td>
						<s:textfield name="model.degree" id="degree" cssStyle="width:95%;" maxlength="100" /></td>
				</tr>
				<tr>
					<td align="right">毕业院校：</td>
					<td>
						<s:textfield name="model.college" id="college" cssStyle="width:95%;" maxlength="100" /></td>
				</tr>
				<tr>
					<td align="right">毕业时间：</td>
					<td>
						<input type="text" name="model.graduationTime" style="width:95%;" readonly="readonly" 
							   value='<s:date name="model.graduationTime" format="yyyy-MM-dd"/>'
							   onClick="WdatePicker({skin:'blueFresh'})" class="Wdate" /></td>
				</tr>
				
				<tr>
					<td align="right">邮政编码：</td>
					<td>
						<s:textfield name="model.zip" id="zip" cssStyle="width:95%;" cssClass="number"  maxlength="255" maxLength="6"/>
					</td>
				</tr>
			</table> 
			</td>
			<td width="50%" valign="top">
			<table width="100%" border="0" style="line-height: 25px; ">
				<tr>
					<td width="25%" align="right">
						<span>员工照片：</span>
						<div>小于200KB</div>
					</td>
					<td width="75%" height="300" valign="top">
						<div style="width:width:50%; height: 250px; border: 1px solid #CCC;overflow-x: hidden;text-align: center;padding:2px;">
						  <img id="photo" src="${ctx}${model.photo.path}" height="250"/></div>
						<s:if test="model.photoId == null || model.photoId==''">
							<input type="file" id="systop_upload" style="width: 20%" />
							<div id="custom">
								<div id="systop-file-queue" style="width: 20%"></div>
							</div>
							<div id="systop-uploaded-files">
								<s:hidden id="fileAttchIds" name="model.photoId" onpropertychange="updateUserPhoto();"/>
							</div>
							<div id="systop_file_list" style="width: 20%"></div>
						</s:if>
						<s:else>
							<a href="javascript:removeUserPhoto('${model.id}')"><img src="${ctx}/images/icons/remove.gif">删除用户照片</a>
						</s:else>
					</td>
				</tr>
				<tr>
					<td align="right">所学专业：</td>
					<td>
						<s:textfield name="model.major" id="major" cssStyle="width:95%;" maxlength="100" /></td>
				</tr>
				<tr>
					<td align="right">入职时间：</td>
					<td>
						<input type="text" name="model.joinTime" style="width:95%;" readonly="readonly" 
							   value='<s:date name="model.joinTime" format="yyyy-MM-dd"/>'
							   onClick="WdatePicker({skin:'blueFresh'})" class="Wdate" />
					</td>
				</tr>
				<tr>
					<td align="right">转正时间：</td>
					<td>
						<input type="text" name="model.formalDate" style="width:95%;" readonly="readonly" 
							   value='<s:date name="model.formalDate" format="yyyy-MM-dd"/>'
							   onClick="WdatePicker({skin:'blueFresh'})" class="Wdate" />
					</td>
				</tr>
				<tr>
					<td align="right">家庭住址：</td>
					<td>
						<s:textfield name="model.address" id="address" cssStyle="width:95%" maxlength="255" />
					</td>
				</tr>
				<tr>
					<td align="right">籍&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;贯：</td>
					<td>
						<s:textfield name="model.hometown" id="hometown" cssStyle="width:95%" maxlength="255" />
					</td>
				</tr>
				
				
			</table> 
			</td>
		</tr>
	</table>
	<div align="center" style="margin-top: 5px;">
		<input type="button" value="保存" class="button" onclick="subform()">&nbsp;&nbsp;&nbsp;&nbsp; 
		<input type="button" value="重置" class="button" onclick="dj();">&nbsp;&nbsp;&nbsp;&nbsp; 
		<s:if test='editSelf == "1"'>
		<!-- <input type="button" value="返回" class="button" onclick="history.go(-1)"> -->
	    </s:if>
	    <s:else>
	         <input type="button" value="返回" class="button" onclick="history.go(-1)"> 
	    </s:else>
	</div>
</s:form></div>
<script type="text/javascript">
var cz ="0";
var dtreeTwo;
if(cz =="0"){
	var partTimeJobDeptId= "${model.partTimeJobDept.id}";
	$('#deptIdTwo').val(partTimeJobDeptId);
}else{
	 $('#deptIdTwo').val("");
	 $('#positionIdTwo').empty(); 
}
 function dj(){
	 $('#positionIdTwo').empty(); 
	   cz="1";
	   $('#save')[0].reset();
	   
	   document.getElementById('comboxWithTree').style.display = "none";
	   Ext.onReady(function() {
			 dtree = new DeptTree({
				url : '/systop/dept/deptTree.do',
				rootName : rootName,
				initValue : initValue,
				el : 'comboxWithTreeYc',
				innerTree : 'inner-tree',
				onclick : function(nodeId) {
					Ext.get('deptId').dom.value = nodeId;
					getPosition(nodeId);
				}
			});
			dtree.init();
		});
 } 
 
	function subform(){
		if($("#suggest").val()=='1'){
			alert("登录帐号不能包含汉字！");
		}else{
			$("#save").submit();
		}
	}

	//===============jquery验证扩展.Start===============
	var uid = $("#uId").val();
	/* var uidTwo = $("#uIdTwo").val(); */
	$(function() {
		$.validator.addMethod("dept", function(value, element) {
			if (value == null || value.length == 0 || value == 0) {
				$("#deptmsg").html("选择处室");
				return false;
			} else {
				$("#deptmsg").html("");
				return true;
			}
		}, "");
		$.validator.addMethod("checkPositionId", function(value, element) {
			return value != null && value.length != 0 && value != 0;
		}, "必选");
		//验证用户名
		$.validator.addMethod("loginId", function(value, element) {
			var reg = new RegExp(
					"^([\u4E00-\uFA29]|[\uE7C7-\uE7F3]|[a-zA-Z0-9_\.\-])*$");
			return reg.test(value); //首先判断非法字符
		}, "包含非法字符");

		

		$.validator.addMethod("pwd", function(value, element) {
			if (value.length < 6) {
				return false;
			}
			return true;
		}, "长度少于6位");

		$.validator.addMethod("repwd", function(value, element) {
			if (value != $('#pwd').val()) {
				return false;
			}
			return true;
		}, "密码不一致");

		$.validator.addMethod("idCard", function(value, element) {
			if (value != null && value.length > 0) {
				if (value.length != 15 && value.length != 18) {
					return false;
				}
			}
			return true;
		}, "长度错误");
	});

	//添加jquery验证
	$(document).ready(function() {
		$("#save").validate();
	});
	//===============jquery验证扩展.End===============
		
	//===============上传组件
	renderUploader(false, '*.jpg; *.gif; *.png', '图片(JPG; GIF; PNG)', 200000);
	
	function updateUserPhoto(){
		var fileAttchId = $('#fileAttchIds').val();
		if(fileAttchId  == null || fileAttchId == ""){
			$("#photo").attr("src", URL_PREFIX+"/images/default_male.png");
		}else{
			showCarPhoto(fileAttchId);
		}
	}
	/**
	 * 显示图片
	 * 
	 * @param fileIds
	 */
	function showCarPhoto(fileIds){
		if (fileIds == null || fileIds.length ==0){
			return;
		}
		$.ajax({
		    url: URL_PREFIX + '/fileattch/findFiles.do?fileIds=' + fileIds,
		    type: 'get',
		    dataType: 'json',
		    error: function(){
		    	alert('get file list error');
		    },
		    success: function(data, textStatus){
		    	if (data != null && data.files != null){
		    		for(var i = 0; i < data.files.length; i++){
		    			// 文件信息
		    			$("#photo").attr("src", URL_PREFIX + data.files[i].path);			    			
		    		}
		    	}
		    }
		});
	}
	
	/**
	 *删除用户照片
	 */
	function removeUserPhoto(modeId){
		if(confirm("您确实要删除用户照片吗?")){
			window.location.href = "removePhoto.do?model.id=" + modeId + "&editSelf=${editSelf}";
		}
	}
	
	var sex = '${model.sex}';
	var photoId = '${model.photoId}';
	function defaultUserPhoto(sex){
		if (photoId == null || photoId.length == 0){
			if (sex == 'M'){
				$("#photo").attr("src", URL_PREFIX+"/images/default_male.png");
			}else{
				$("#photo").attr("src", URL_PREFIX+"/images/default_female.png");
			}
		}
	}
	defaultUserPhoto(sex);
</script>
<div id="deptChooser">
    <ol class="dept-ol">
    </ol>
</div>
</body>
</html>