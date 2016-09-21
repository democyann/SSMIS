<%@page import="com.systop.common.modules.security.user.UserUtil"%>
<%@page import="java.net.InetAddress"%>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%request.setAttribute("ctx", request.getContextPath()); %>
<%request.setAttribute("sip", InetAddress.getLocalHost().getHostAddress()); %>
<%@ taglib prefix="stc" uri="/systop/common" %>
<script>
	  var i = 1;    //点击的次数
	  var zk="";   //展开 
	  var j = 1;
	  var  zj="";//四级展开
		function dj1(op){
			 j = 1;
	  	if(zk != op&&zk!=""){ //打开一个新节点时，点击次数初始化
	  		$('img_'+op).src="${ctx}/images/icons/minus.gif";  
	  		$('img1_'+op).src="${ctx}/images/icons/folder_open.gif";
	  		i = 1;
	  	}
			if(i%2==1){     //打开节点
			    document.getElementById('img_'+op).src="${ctx}/images/icons/minus.gif";
			    document.getElementById('img1_'+op).src="${ctx}/images/icons/folder_open.gif";
				if(zk != op&&zk!=""){//打开一个新节点,将旧的节点关闭
					$('#'+zk).html($('#'+zk+'2').html());//旧的节点关闭
				}
				$('#'+op).append($('#'+op+'1').html());
				zk =op;
				i++;
			}else{//关闭节点
				$('#'+op).html($('#'+op+'2').html());
				i++;
			}
		}
	  function djdg1(){
	  	if(zk!=""){ //存在打开的节点
	  		$('#'+zk).html($('#'+zk+'2').html());//旧的节点关闭
	  		 i = 1; 
	  		zk="";
	  	}
	  } 

		function dj(op){ 
			 j = 1;
		//  alert("dj");
	  	if(zk != op&&zk!=""){ //打开一个新节点时，点击次数初始化
	  		$('img_'+op).src="${ctx}/images/icons/minus.gif";  
	  		i = 1;
	  	}
			if(i%2==1){     //打开节点
			    document.getElementById('img_'+op).src="${ctx}/images/icons/minus.gif";
				if(zk != op&&zk!=""){//打开一个新节点
					$('#'+zk).html($('#'+zk+'2').html());//旧的节点关闭
				}
				$('#'+op).append($('#'+op+'1').html());
				zk =op;
				i++;
			}else{//关闭节点
				$('#'+op).html($('#'+op+'2').html());
				i++;
			}
		}
	  
	  function djdg(){
	  	if(zk!=""){ //存在打开的节点
	  		$('#'+zk).html($('#'+zk+'2').html());//旧的节点关闭
	  		 i = 1; 
	  		zk="";
	  	}
	  } 
	
	  /**四级菜单调用的方法*/
	  
	  function dj2(op){
		   // alert("J为："+j+"   ZJ为："+zj);
			if(zj != op&&zj!=""){ //打开一个新节点时，点击次数初始化
		  		$('img_'+op).src="${ctx}/images/icons/minus.gif";  
		  		$('img1_'+op).src="${ctx}/images/icons/folder_open.gif";
		  		j= 1;
		  	}
			    
				if(j%2==1){     //打开节点
				    document.getElementById('img_'+op).src="${ctx}/images/icons/minus.gif";
				    document.getElementById('img1_'+op).src="${ctx}/images/icons/folder_open.gif";
				 	if(zj != op&&zj!=""){//打开一个新节点
					
						$('#'+zj).html($('#'+zj+'2').html());//旧的节点关闭
					} 
					$('#'+op).append($('#'+op+'1').html());
					zj =op;
					j++;
				}else{//关闭节点
					$('#'+op).html($('#'+op+'2').html());
					j++;
				}
			}
	  
</script>
<div  id="menu" style="display:none">
    


     
	<!-- 个人办公 -->
	<div id="grbg">
		<div style="padding-left:5px;">
			<div style="padding-top:2px"><a href="javascript:addTab('个人信息','${ctx}/security/user/editSelf.do');" target="main" ><img src="${ctx}/images/icons/dept.gif" class="icon">个人信息</a></div>
	   </div>
	</div>
	<!-- 系统管理 -->
	<div id="xtgl">
		<div style="padding-left:5px;">
			<stc:menu hasMenu="机构管理"><div style="padding-top:2px"><a href="javascript:addTab('机构管理','${ctx}/systop/dept/index.do');" onclick ="javascript:djdg1();" target="main"><img src="${ctx}/images/icons/dept.gif" class="icon">机构管理</a></div></stc:menu>
			<stc:menu hasMenu="职位管理"><div style="padding-top:2px"><a href="javascript:addTab('职位管理','${ctx}/position/index.do');" onclick ="javascript:djdg1();" target="main"><img src="${ctx}/images/icons/dept.gif" class="icon">职位管理</a></div></stc:menu>
		  <stc:menu hasMenu="用户管理"><div style="padding-top:2px"><img src="${ctx}/images/icons/dept.gif" class="icon"><a href="javascript:addTab('用户管理','${ctx}/security/user/index.do');" onclick ="javascript:djdg1();" target="main">用户管理</a></div></stc:menu>
		 	<stc:menu hasMenu="角色管理"><div style="padding-top:2px"><img src="${ctx}/images/icons/dept.gif" class="icon"><a href="javascript:addTab('角色管理','${ctx}/security/role/index.do');" onclick ="javascript:djdg1();" target="main">角色管理</a></div></stc:menu>
		 	<stc:menu hasMenu="权限管理"><div style="padding-top:2px"><a href="javascript:addTab('权限管理','${ctx}/security/permission/index.do');" onclick ="javascript:djdg1();" target="main"><img src="${ctx}/images/icons/dept.gif" class="icon">权限管理</a></div></stc:menu>
		 	<stc:menu hasMenu="字典管理"><div style="padding-top:2px;"><a href="javascript:addTab('字典管理','${ctx}/admin/dict/adminIndex.do');" onclick ="javascript:djdg1();" target="main"><img src="${ctx}/images/icons/dept.gif" class="icon">字典管理</a></div></stc:menu>
			
		
		</div>
	</div>

</div>