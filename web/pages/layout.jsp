<%@ page language="java" contentType="text/html; charset=UTF-8" 	pageEncoding="UTF-8"%>
<%@ page import="com.systop.common.modules.security.user.UserConstants"%>
<%@ page import="java.net.URLDecoder" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/common/taglibs.jsp"%>
<%@include file="/common/meta.jsp"%>
<%@include file="/common/extjs.jsp"%>
<%@include file="/common/jqueryUI.jsp"%>
<%-- <script language="javascript" type="text/javascript" src="${ctx}/pages/main1.jsp"></script> --%>
<script type="text/javascript" src="${ctx}/scripts/jquery/jquery-1.3.2.js" ></script>
<script type="text/javascript" src="${ctx}/scripts/jquery/ui/jquery-ui-1.7.1.js"></script>
<script type="text/javascript" src="${ctx}/scripts/swfobject_modified.js"></script>
<script type="text/javascript" src="${ctx}/scripts/jquery/jgrowl/jquery.jgrowl_minimized.js"></script>
 <%-- <script type="text/javascript" src="${ctx}/scripts/sound/soundmanager2-jsmin.js"></script> --%>
<link rel="stylesheet" href="${ctx}/scripts/jquery/jgrowl/jquery.jgrowl.css" type="text/css"/>
<link href="${ctx}/tool/css/main.css" rel="stylesheet" type="text/css" />
<script src="${ctx}/scripts/utils.js"></script>
<LINK href="${ctx}/styles/style.css" type='text/css' rel='stylesheet'>
<link href="${ctx}/pages/layout.css" type='text/css' rel='stylesheet'/>
<title>新龙教质信息管理系统</title>
<style type="text/css">
    #west
    {
        height: 100%;
    }
    #south{
        display: none;
    }
    
    p {
	}

	span {
	
	}
    
    
	#ext-gen15 {
		font: bold 12px sans-serif;
		padding: 0px 0px 0px 0px;
		display: block;
		height: 20px;
		vertical-align: middle;
	}
    #div{
    	background-image: url('${ctx}/images/scos/top.jpg'); 
    	background-repeat:no-repeat; 
    	background-size:20px;
    }
    
    .title{
	font-weight: bold;
}
.menulink{ font-size: 14px; font-weight: bold; color: #FFF; text-decoration: none; }
#nav { 
	line-height: 36px; 
	height: 36px; 
	
	margin-right: auto; 
	margin-left: auto; 
	color: #FFF; 
	font-size: 14px; 
	font-weight: bold; 
}
body {
	font-family: Verdana, Geneva, sans-serif;
	font-size: 12px;
	margin: 0px;
	padding: 0px;
}
.m_top {
	height: 100px;
	width: 100%;
	background-image: url('${ctx}/images/top_bg.gif');
}
.left {
	margin-top:20px;
	float: left;
	height: 50px;
	width: 500px;
	background-image: url('${ctx}/images/top.png');
	background-repeat: no-repeat;
}
.top_right {
	float: right;
	height: 87px;
	width: 400px;
	background-image: url('${ctx}/images/top_right_bg.gif');
	background-repeat: no-repeat;
}
</style>
</head>


<script type="text/javascript">
	
	
function MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}
function MM_preloadImages() { //v3.0
  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
    if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
}

function MM_findObj(n, d) { //v4.01
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x && d.getElementById) x=d.getElementById(n); return x;
}

function MM_swapImage() { //v3.0
  var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
   if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
}
</script>


<script type="text/javascript">
	function switchTab(n) {
		for ( var i = 1; i <= 4; i++) {
			document.getElementById("tab_" + i).className = "unselect";
			document.getElementById("tab_con_" + i).style.display = "none";
		} 
		 
		document.getElementById("tab_" + n).className = "onselect";
		document.getElementById("tab_con_" + n).style.display = "block";
	}
	function changePassword(){
		var url = "${ctx}/password/modifyPassword.do";
		var config = "dialogWidth=500px;dialogHeight=250px;status=no;help=no;scrollbars=no";
		window.showModalDialog(url,"",config);
	}
</script>
<script type="text/javascript">	
	Ext.onReady(function() {
		Ext.state.Manager.setProvider(new Ext.state.CookieProvider());

		var viewport = new Ext.Viewport( {
			layout : 'border',
			items : [ new Ext.BoxComponent( { // raw
						region : 'north',
						el : 'north',
						height : 51
					}),
                {
				region : 'west',
				id : 'west-panel',
				title : '&nbsp;',
				split : true,
				width : 150,
				minSize : 150,
				maxSize : 400,
				collapsible : true,
				margins : '0 0 0 5',
				layout : 'accordion',
                layoutConfig : {
					animate : true
				},
			items : [
							{
								title : '<span class="title">个人事务</span>',
								html : document.getElementById('grbg').innerHTML,
								border : false,
								iconCls : 'qbdy'
							}
						<stc:menu hasMenu="系统管理">,
						{
							title : '<span class="title">系统管理</span>',
							html : document.getElementById('xtgl').innerHTML,
							border : false,
							iconCls : 'permit'
						}
						</stc:menu>
						
						]
					},
			{
				region : 'center',
				contentEl : 'center',
				split : true,
				border : true,
				margins : '0 5 0 0'
			} ]
		});
		//alert($('#west').height());
        $(".menu-iframe").show();
        var date = new Date();
        var hh = date.getHours();
        var sj ;
        if(hh > 12&&hh<24){
			sj="下午";
		}else{
			sj="上午";
		}
        //<img style='margin: 0px 0px 0px 0px;' src='${ctx}/images/head.gif' />
		document.getElementById("ext-gen15").innerHTML ="<center style='padding:0px 0px 0px 0px'><label style='position: relative;top: 0px;'>"+"${loginUser.name},"+"您好！</label></center>" ;
        

	});
</script>
<%@include file="/pages/menu.jsp"%>
<div class="m_top" id="north" align="center">
<table width="100%" height="100%" border="0" id="tab1">
	<tr  height="100" valign="top" style="width: 100%">
		<td align="left" valign="middle">
			<div class="left">
			<object id="FlashID" classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" width="94" height="88">
		      <param name="movie" value="images/20110307104309143.swf" />
		      <param name="quality" value="high" />
		      <param name="wmode" value="transparent" />
		      <param name="swfversion" value="6.0.65.0" />
		      <!-- 此 param 标签提示使用 Flash Player 6.0 r65 和更高版本的用户下载最新版本的 Flash Player。如果您不想让用户看到该提示，请将其删除。 -->
		      <param name="expressinstall" value="scripts/expressInstall.swf" />
		      <!-- 下一个对象标签用于非 IE 浏览器。所以使用 IECC 将其从 IE 隐藏。 -->
		      <!--[if !IE]>-->
		      <object type="application/x-shockwave-flash" data="images/20110307104309143.swf" width="94" height="88">
		        <!--<![endif]-->
		        <param name="quality" value="high" />
		        <param name="wmode" value="transparent" />
		        <param name="swfversion" value="6.0.65.0" />
		        <param name="expressinstall" value="scripts/expressInstall.swf" />
		        <!-- 浏览器将以下替代内容显示给使用 Flash Player 6.0 和更低版本的用户。 -->
		        <div>
		          <h4>此页面上的内容需要较新版本的 Adobe Flash Player。</h4>
		          <p><a href="http://www.adobe.com/go/getflashplayer"><img src="http://www.adobe.com/images/shared/download_buttons/get_flash_player.gif" alt="获取 Adobe Flash Player" width="112" height="33" /></a></p>
		        </div>
		        <!--[if !IE]>-->
		      </object>
		      <!--<![endif]-->
		    </object>
		    </div>
		</td>
		<td align="right">
		<div class="top_right">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
	      <tr>
	        <td height="60" colspan="7">&nbsp;</td>
	      </tr>
	      <tr>
	        <td width="13%" height="25">&nbsp;</td>
	        <td width="7%" valign="bottom"><img src="images/iconpng.png" width="20" height="20" /></td>
	        <td width="15%" valign="middle" style="color:#000;"><a href="#" onclick="changePassword()">修改密码</a></td>
	        <td width="7%" valign="bottom"><img src="images/201101251529233131.png" width="20" height="20" /></td>
	        <td width="15%" valign="middle" style="color:#000;"><a href="${ctx}/pages/mHelp.jsp"  >系统帮助</a></td>
	        <td width="7%" valign="bottom"><img src="images/computer_on_min.png" width="20" height="20" /></td>
	        <td width="15%" valign="middle" style="color:#000;"> <a href="javascript:window.location.reload();" >办公桌面</a></td>
	        <td width="7%" valign="bottom"><img src="images/112935M32-0-lp.png" width="17" height="20" /></td>
	        <td width="24%" valign="middle" style="color:#000;">
	        	 <stc:ifLogin>
					<a href="javascript:logout()" target="_self">退出系统</a>
				 </stc:ifLogin>	
			</td>
	      </tr>
	    </table>
	    </div>
		</td>
	</tr>
</table>
</div>
  <div id="img">
   </div>
<div id="center" style="height: 100%">
	<iframe id="main" name="main" src="${ctx}/pages/main1.jsp" style="width:100%; height:100%; border:0px;overflow:hidden;"  frameborder="0" scrolling="auto">
	</iframe>
	
</div>
<div id="aSound"></div>
<!-- 悬浮框 -->
 <SCRIPT LANGUAGE="JavaScript">
    <!-- Begin
    var xPos = document.getElementById("tab1").clientWidth-20;
    var yPos = document.getElementById("tab1").clientHeight/2;
    var step = 1;
    var delay = 40;
    var height = 0;
    var Hoffset = 0;
    var Woffset = 0;
    var yon = 0;
    var xon = 0;
    var pause = true;
    var interval;
    //img.style.top = yPos;
    function changePos() {
    width = document.getElementById("tab1").clientWidth;
    height = document.getElementById("tab1").clientHeight;
    Hoffset = img.offsetHeight;
    Woffset = img.offsetWidth;
    img.style.left = xPos + document.getElementById("tab1").scrollLeft;
    img.style.top = yPos + document.getElementById("tab1").scrollTop; 
    /* if (yon) {
    yPos = yPos + step;
    }else {
    yPos = yPos - step;
    } */
   /*  if (yPos < 0) {
    yon = 1;
    yPos = 0;
    } */
    if (yPos >= (height - Hoffset)) {
    yon = 0;
    yPos = (height - Hoffset);
    }
    if (xon) {
    xPos = xPos + step;
    }else {
    xPos = xPos - step;
    }
    if (xPos < 0) {
    xon = 1;
    xPos = 0;
    }
    if (xPos >= (width - Woffset)) {
    xon = 0;
    xPos = (width - Woffset);
    }
    }
    function start() {
   // img.visibility = "visible";
    interval = setInterval('changePos()', delay);
    }
    start();
    img.onmouseover = function() {
    clearInterval(interval);
    interval = null;
    }
    img.onmouseout = function() {
    interval = setInterval('changePos()', delay);
    }
    //  End -->
    </script>
<script type="text/javascript">
	/* soundManager.url = '${ctx}/scripts/sound/swf'; // directory where SM2 .SWFs live
	soundManager.debugMode = false;
	
	function palySound(){
		soundManager.onready(function() {
			var mySound = soundManager.createSound({
				id : 'aSound',
				url : '${ctx}/images/newlist.mp3'
			});
			mySound.play();

		});
	} */

	/* $.jGrowl.defaults.position = "bottom-right";
	$.jGrowl.defaults.life = 13000;
	$.jGrowl.defaults.pool = 5;
	$.jGrowl.defaults.closerTemplate='<div>[全部关闭] &nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:;" onclick="readAllMsgs()" style="color: red;">【全部已读】</a></div>';
	 */
	// 设置消息全部已读
	function readAllMsgs(){
		//if(confirm("确定要将所有未读消息设置为已读？")){
			$.ajax({
				url : '${ctx}/msg/ajaxReadAllMsgs.do',
				type : 'post',
				dataType : 'json',
				error : function() {
					//alert('check new msg error');
				},
				success : function(result) {
					if(!result.success){
						alert('已读设置失败！');
					}
				}
			});
		//}
	}

	/* function checkNewMsg() {
		$.ajax({
					url : '${ctx}/msg/checkNewMsgs.do',
					type : 'post',
					dataType : 'json',
					error : function() {
						//alert('check new msg error');
					},
					success : function(data, textStatus) {
						if (data == null || data.length == 0) {
							return;
						}
						palySound();
						var htmlStr = null;
						var num = 0;
						for ( var i = 0; i < data.length; i++) {
							num = i + 1;
							htmlStr = "";
							htmlStr += "<div style='width:200px; height:20px; padding:0px;'>";
							htmlStr += "<a href='${ctx}/msg/view.do?model.id="+ data[i][0]
									+ "&updateState=Y' target='main'><b>" + num + "</b>. " 
									+ data[i][1] + "</a>";
							htmlStr += "</div>";
							$.jGrowl(htmlStr);
						}
					}
				});
	}
	/* window.setInterval("checkNewMsg()", 90000); */
	/* checkNewMsg(); */

	function showLocale(objD) {
		var str, spanHead, spanFoot;
		var yy = objD.getYear();
		if (yy < 1900)
			yy = yy + 1900;
		var MM = objD.getMonth() + 1;
		if (MM < 10)
			MM = '0' + MM;
		var dd = objD.getDate();
		if (dd < 10)
			dd = '0' + dd;
		var hh = objD.getHours();
		if (hh < 10)
			hh = '0' + hh;
		var mm = objD.getMinutes();
		if (mm < 10)
			mm = '0' + mm;
		var ss = objD.getSeconds();
		if (ss < 10)
			ss = '0' + ss;
		var ww = objD.getDay();
		if (ww == 0)
			ww = "星期日";
		if (ww == 1)
			ww = "星期一";
		if (ww == 2)
			ww = "星期二";
		if (ww == 3)
			ww = "星期三";
		if (ww == 4)
			ww = "星期四";
		if (ww == 5)
			ww = "星期五";
		if (ww == 6)
			ww = "星期六";
		spanHead = "<span style='font-family: sans-serif;'>";
		spanFoot = "</span>";
		str = spanHead + yy + "-" + MM + "-" + dd + "&nbsp;" + ww + "&nbsp;"
				+ hh + ":" + mm + ":" + ss + spanFoot;
		var sj;
       if(hh > 12&&hh<24){
			sj="下午";
		}else{
			sj="上午";
		}
	//	document.getElementById("ext-gen15").innerHTML =sj ;
		return (str);
		
	}
	function tick() {
		var today;
		today = new Date();
	//	document.getElementById("localtime").innerHTML = showLocale(today);
		window.setTimeout("tick()", 1000);
	}
	tick();
  $(function(){
//     var loc = _getCookie("userLocation");
//     $("#userLocation").html(loc);
  
  });
  
  /**$(function(){
		if("${passwordFlag}" == "1"){
			Ext.MessageBox.confirm('提示','${loginUser.name}' + '，您好！<br>这是您首次登陆，请修改初始密码！', function(btn){
				if (btn == 'yes') {
					var url = "${ctx}/password/modifyPassword.do";
					var config = "dialogWidth=500px;dialogHeight=250px;status=no;help=no;scrollbars=no";
					window.showModalDialog(url,"",config);
				}
			});
		}
	})*/
</script>
<script type="text/javascript" src="${ctx}/scripts/cookie.js"></script>
    <!--[if !IE]>-->
    <%--<script type="text/javascript" src="${ctx}/scripts/geo.js"></script>--%>
    <!--<![endif]-->
<script>
    var logoutUrl = "${ctx}/security/user/updateStatus.do";
    var statusTimer;
    $(function(){
        //update online statuss per 5 min
        $.get(logoutUrl);
        statusTimer = setInterval(function(){
            //issue 846 todo
            $.get(logoutUrl,{"time":<%=new java.util.Date().getTime()%>},function(data){
/*                if(data.logout=="true"){
                    $("<div></div>").dialog({
                        buttons:{
                            "取消":function(){
                                clearTimeout(outTimer);
                                $(this).dialog("close");
                            }
                        }
                        ,title:"会话超时，请重新登录！"
                        ,modal:true
                    });
                    outTimer = setTimeout(function(){
                        logout();
                    },15*1000)
                }*/
            });
        },5*60*1000);
    });

    function logout(){
        clearTimeout(statusTimer);
        var redirect = function(){
            location.href = "${ctx}/j_acegi_logout";
        }
        //issue 847
        $.ajax({
            url:logoutUrl,
            data:{"logout":"true"},
            success:redirect,
            error:redirect
        })
    }
    
</script>
</body>
</html>