<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="/common/taglibs.jsp"%>
<html>
<head>

<link rel="stylesheet" type="text/css" href="${ctx}/styles/ec/ecside_style.css" />

<%@include file="/common/meta.jsp"%>
<%@include file="/common/extjs.jsp"%>

<%-- <%@include file="/pages/main1.jsp"%> --%>
<link href="${ctx}/styles/welcome.css" type='text/css' rel='stylesheet'>

<script type="text/javascript"
	src="${ctx}/scripts/jquery/ui/jquery-ui-1.8.21.custom.min.js"></script>
<link href="${ctx}/scripts/jquery/ui/css/smoothness/jquery-ui-1.8.21.custom.css" rel="stylesheet" />
<style type="text/css">
.noBorder {
	margin: -1px;
}

.ui-tabs-nav {
	background: url("${ctx}/images/default/tabs/tab-strip-bg.gif") repeat-x
		scroll center bottom #CEDFF5 !important;
	/*border-bottom: 1px solid #8DB2E3;*/
	/*padding-top: 1px;*/
}

.ui-tabs .ui-tabs-nav li a {
	padding: 0.2em 1em !important;
}

.ui-tabs .ui-tabs-panel {
	padding: 0.9em 0.9em !important;
}

.left-div {
	width: 59.5%;
	float: left;
	margin-left: 5px;
	border:1px solid #72B3E9;
	margin-top: 5px;
}

.right-div {
	width:38%;	
	overflow: hidden;
	float: right;
	border:1px solid #72B3E9;
	margin-top: 5px;
	margin-right: 0.3%;
}
img{
	position: relative;
	top: 4px;
}
.note{
	background-image: url("${ctx}/images/bianqian.png");
	background-repeat: repeat;
	line-height: 20px;
	margin:5px;
	width:100%;
	heigth:100px;
}
.block-title{
	width:auto;
}

</style>
<title>翰子昂移动互联实验室</title>
</head>
<body >
	         <div id="main" style="height: 100%;background-color: white;" >
	         
	         <%--
				<div class="left-div" >
				<div class="block-title">
				   	<img  src="${ctx}/images/proj/edit_online.png">
					<a href="${ctx}/jbpm/instance/needMyHandle.do?className=all">待办事宜</a>
					<div align="right" style="margin-top: -18px;">
						<img src="${ctx}/images/exticons/refresh.gif" onclick="readnew()" title="刷新"/>
						<a href="${ctx}/jbpm/instance/needMyHandle.do?className=all">
							<img src="${ctx}/images/grid/zoom.png" title="更多"/>
						</a>
					</div>
				</div>
				<div class="block-body list" id="myNodes"></div>
			   </div>
			   
			  <div class=" right-div">
				<div class="block-title">
			     	<img src="${ctx}/images/icons/class.gif">
					<a href="javascript:void(0)">欢迎</a>
					<div align="right" style="margin-top: -18px;">
						<img src="${ctx}/images/exticons/refresh.gif" onclick="readdb()" title="刷新"/>
						
					</div>
				</div>
				<div class="block-body list" id="welcome"></div>
			</div>
			
			<div class=" left-div">
				<div class="block-title">
			    	<img src="${ctx}/images/icons/msg.gif">
					<a href="${ctx}/pages/business/bumf/distribute/toberead.do">待阅事宜</a>
					<div align="right" style="margin-top: -18px;">
						<img src="${ctx}/images/exticons/refresh.gif" onclick="unread()" title="刷新"/>
						<a href="${ctx}/pages/business/bumf/distribute/toberead.do">
							<img src="${ctx}/images/grid/zoom.png" title="更多"/>
						</a>
					</div>
				</div>
				<div class="block-body list" id="unread"></div>
			</div>

			
            <div class=" right-div">
				<div class="block-title">
				    <img src="${ctx}/images/icons/visit_p.gif">
					<a href="${ctx}/article/perusalIndex.do">部门动态</a>
					<div align="right" style="margin-top: -18px;">
						<img src="${ctx}/images/exticons/refresh.gif" onclick="action()" title="刷新"/>
						<a href="${ctx}/article/perusalIndex.do">
							<img src="${ctx}/images/grid/zoom.png" title="更多"/>
						</a>
					</div>
				</div>
				<!--  <div class="block-body list" id="gzzd"></div>-->
				<!-- <div class="block-subTitle" id="articleTypes"></div> -->
				<div class="block-body list" id="action"></div>
			</div> 
              <div class="left-div">
				<div class="block-title">
				<img src="${ctx}/images/icons/message.png">
				<a href="${ctx}/pages/scos/mail/mailLayout.jsp">最新邮件</a>
				<div align="right" style="margin-top: -18px;">
						<img src="${ctx}/images/exticons/refresh.gif" onclick="newmail()" title="刷新"/>
						<a href="${ctx}/pages/scos/mail/mailLayout.jsp">
							<img src="${ctx}/images/grid/zoom.png" title="更多"/>
						</a>
				</div>
					
				</div>
				<div class="block-body list" id="msg"></div>
			</div>   
		     <div class="right-div">
				<div class="block-title">
				   <img src="${ctx}/images/icons/newspaper.png">
					<a href="${ctx}/notice/indexCy.do">通知公告</a>
					<div align="right" style="margin-top: -18px;">
						<img src="${ctx}/images/exticons/refresh.gif" onclick="notice()" title="刷新"/>
						<a href="${ctx}/notice/indexCy.do">
							<img src="${ctx}/images/grid/zoom.png" title="更多"/>
						</a>
					</div>
				</div>
				<div class="block-body list" id="notices"></div>
			  </div>
              <div id="base" style="display:inline">
			<div class="blockm left-div">
				<div id="noteTop" class="block-title" onmouseover="javascript:$('#daohangtiao').css('display','block');" onmouseout="javascript:$('#daohangtiao').css('display','none');"/>
					<div style="float: left;">
					<img src="${ctx}/images/icons/diary.png">
						<a href="${ctx}/note/index.do">我的便签</a>
					</div>
					<div id="daohangtiao" style="float: right;margin-right: 25px;display: none;">
						<a href="#" onclick="xinjian();"><img src="${ctx}/images/exticons/add.gif"/></a>&nbsp;
						<a href="#" onclick="bianji();"><img src="${ctx}/images/exticons/edit.gif"/></a>&nbsp;
						<a href="#" onclick="shanchu();"><img src="${ctx}/images/exticons/delete.gif"/></a>
					</div>
				</div>
			<div  class="block-body list" id="note"></div>
			</div> --%>
		<%--  <div class="blockm right-div" style="clear: both;">
				<div class="block-title">
						<img src="${ctx}/images/icons/down.gif">
					<a href="${ctx}/forum/board/index.do" >讨论区</a>
				</div>
				<div class="block-body list" id="topics"></div>
			</div>  --%>
		</div>
	<!-- </div> -->
	<script type="text/javascript" src="${ctx}/pages/main.js"></script>
	<script type="text/javascript" language="javascript">
		var URL_PREFIX = '${ctx}';
		/* //加载通知公告
		loadNotices("notices");
		//加载待办事项
		loadMyNodes("myNodes");
		//加载待阅事宜
		loadMails("unread");
		//加载新闻类别
		//loadArticleTypes("articleTypes", "articles");
		//加载部门动态
		loadArticles("action");
		 //加载最新 邮件
		loadMsg("msg");
		//加载欢迎块
		loadWel("welcome"); */
		//加载规章制度
		//loadUsefulLink("gzzd");
		/* //加载内部论坛
		loadTopics("topics");
		 */
		//加载图片新闻
	    //	loadArticlePhotos("articlePhotos");
		/* */
		/* //加载便签
		loadNote("note"); */
		function readnew(){
			loadMyNodes("myNodes");
			Ext.my().msg('', '刷新成功！');
		}
		function unread(){
			loadMails("unread");
			Ext.my().msg('', '刷新成功！');
		}
		function action(){
			loadArticles("action");
			Ext.my().msg('', '刷新成功！');
		}
		function newmail(){
			loadMsg("msg");
			Ext.my().msg('', '刷新成功！');
		}
		function notice(){
			loadNotices("notices");
			Ext.my().msg('', '刷新成功！');
		}
		function readdb(){
			loadWel("welcome");
			Ext.my().msg('', '刷新成功！');
		}
	</script>
</body>
</html>