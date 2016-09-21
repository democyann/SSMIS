var tabsDemo;
	function addTab(title,url){
		   tabsDemo.add({
	          title:title,
	          id:"newtab"+title,
	         frame:false, 
	         header:false,//是否显示panel的标题，为false时不显示标题，为true显示，默认是显示
	         html:'<iframe id="zmIframe'+title+'"  src="'+url+'"   style="width:100%; height:100%; border:0px;overflow:hidden;"  frameborder="0" scrolling="auto"></iframe>' ,
	          closable:true
	     });
	     tabsDemo.setActiveTab("newtab"+title);
}
	
/*
*  登录后首页异步加载的数据模块：
 * .通知公告：/notice/indexNotices.do
 * .待办事项：/flow/node/indexNodes.do
 * .新闻栏目: /article/type/indexTypes.do
 * .新闻列表：/article/indexArticles.do
 * .内部邮件：/email/indexEmails.do
 * .最新项目：/project/indexProjects.do
 * .内部讨论：/forum/topic/indexTopics.do
 * .短消息：/msg/msg.do
 */

	/**
	 * 加载通知公告
	 * @param divId
	 */
	function loadNotices(divId){
		var divContent = $('#'+ divId);
		var width = divContent.width() - 20;
		var htmlStr = "";
		$.ajax({
			url: URL_PREFIX + '/notice/indexNotices.do',
			type: 'post',
			dataType: 'json',
			data:{'viewCount':5},
			success: function(data, textStatus){
				if (data == null || data.length == 0){
					htmlStr = "<div style='margin:5px;'>暂无通知公告...</div>";	    		
				}else{
					/*var nowDay = (new Date()).getDate();*/
					for(var i = 0; i < data.length; i++){
						var newStyle = data[i].noticeIsNew ? "liNew" : "liNormal";
						htmlStr += "<div style='width :80%;white-space:nowrap;word-break:keep-all;overflow:hidden;text-overflow:ellipsis;' class='item'>";
						htmlStr += "<a href='javascript:addTab(\"通知公告\",\""+URL_PREFIX+"/notice/view1.do?model.id="+data[i].id+"\");'  title='" + data[i].title + "'  target='main'> " ;
						htmlStr += "<span class='" + newStyle + "'><span style='color: blue;'>【" + data[i].createDate + "】</span>"+ data[i].title +"</a>";
						/*if(nowDay == data[i].createDate.substring(3)){
							htmlStr +="&nbsp;<img src='"+ URL_PREFIX  +"/images/new.gif' style='widht:20px;height:15px;'/>";
						}*/
						htmlStr += "</div>";
					}
				}
				divContent.html(htmlStr);
			},
			error: function(){
				//nothing...
			}
		});
	}

/**
 * 加载待办事项
 * @param divId
 */
function loadMyNodes(divId){
	var divContent = $('#'+ divId);
	var width = divContent.width() - 20;
	var htmlStr = "";
	$.ajax({
		url: URL_PREFIX + '/jbpm/instance/indexPendingNodes.do',
		type: 'post',
		dataType: 'json',
		data:{'viewCount':5},
		success: function(t){
			
			if (t.urls == null || t.urls.length == 0){
				htmlStr = "<div style='margin:5px;'>暂无待办事项...</div>";	    		
			}else{
				for(var i = 0; i < t.urls.length; i++){
					htmlStr += "<div style='width :80%;white-space:nowrap;word-break:keep-all;overflow:hidden;text-overflow:ellipsis;'  class='item'>";
					if(t.flag[i] == 0){  /*工作流中的待办*/
					htmlStr += "<a href='" + URL_PREFIX + t.urls[i] + "&taskId=" + t.ids[i]+ "' title = ' "+ t.names[i]+t.pertor[i]+" ' ><span style='color: blue;'>【" + t.dateTime[i] + "】</span>"+ 
					t.names[i]+t.pertor[i]
					+"</a>";  
					}else if(t.flag[i] == 1){  /*部门动态待办*/
						htmlStr += "<a href= '" + URL_PREFIX + t.urls[i] + "?model.id=" + t.ids[i]+ "' ><span style='color: blue;'>【" + t.dateTime[i] + "】</span>"+ 
						t.names[i]
						+"</a>";
					}else if(t.flag[i] == 2){ 
						htmlStr += "<a href= '" + URL_PREFIX + t.urls[i]+"' ><span style='color: blue;'>【" + t.dateTime[i] + "】</span>"+ 
						t.names[i]
						+"</a>";
					}
					htmlStr += "</div>";
				}
			}
			divContent.html(htmlStr);
		},
		error: function(){
			//nothing...
		}
	});
}

/**
 * 加载新闻类别
 * @param divId
 */
function loadArticleTypes(typeDivId){
	var divContent = $('#'+ typeDivId);
	var width = divContent.width() - 20;
	var htmlStr = "";
	$.ajax({
		url: URL_PREFIX + '/article/type/indexTypes.do',
		type: 'post',
		dataType: 'json',
		success: function(data, textStatus){
			if (data != null || data.length > 0){
				htmlStr = "<div class='ellipsis' style='width:" + width + "; margin:0px 5px;'>";
				for(var i = 0; i < data.length; i++){
					htmlStr += "<a href='#' onmouseover='javascript:loadArticles(" + data[i].id + ", \"articles\")'>" + data[i].name + "</a>";
					if(i +1<data.length ){
						htmlStr+="&nbsp;&nbsp;|&nbsp;&nbsp;";
					}
				}
				htmlStr += "</div>";
			}
			divContent.html(htmlStr);
		},
		error: function(){
			//nothing...
		}
	});
}

/**
 * 部门动态
 * @param typeId
 * @param articleDivId
 */
function loadArticles( articleDivId){
	var divContent = $('#'+ articleDivId);
	var width = divContent.width() - 20;
	var htmlStr = "";
	$.ajax({
		url: URL_PREFIX + '/article/indexArticles.do',
		type: 'post',
		dataType: 'json',
		data:{'viewCount':5},
		success: function(data){
			if (data == null || data.length == 0){
				htmlStr = "<div style='margin:5px;'>暂无部门动态...</div>";	    		
			}else{
				/*var nowDay = (new Date()).getDate();*/
				for(var i = 0; i < data.length; i++){
					htmlStr += "<div style='width :80%;white-space:nowrap;word-break:keep-all;overflow:hidden;text-overflow:ellipsis;' class='item'>";
					htmlStr += "<a href='javascript:addTab(\"部门动态\",\""+URL_PREFIX+"/article/checkView1.do?model.id="+data[i].id+"\");'   target='main'  title='" + data[i].title + "'><span style='color: blue;'>【" + data[i].createDate + "】</span>" + data[i].title +"</a>";
					/*if(nowDay == data[i].createDate.substring(3)){
						htmlStr +="&nbsp;<img src='"+ URL_PREFIX  +"/images/new.gif' style='widht:20px;height:15px;'/>";
					}*/
					htmlStr += "</div>";
				}
			}
			divContent.html(htmlStr);
		},
		error: function(){
			//alert("e");
			//nothing...
		}
	});
}

/**
 * 加载内部讨论区
 * @param divId
 */
function loadTopics(divId){
	var divContent = $('#'+ divId);
	var width = divContent.width() - 20;
	var htmlStr = "";
	$.ajax({
		url: URL_PREFIX + '/forum/topic/indexTopics.do',
		type: 'post',
		dataType: 'json',
		data:{'viewCount':5},
		success: function(data, textStatus){
			if (data == null || data.length == 0){
				htmlStr = "<div style='margin:5px;'>暂无最新讨论1...</div>";	    		
			}else{
				for(var i = 0; i < data.length; i++){
					htmlStr += "<div style='width:" + width + "' class='item'>";
					htmlStr += "<a href='" + URL_PREFIX + "/forum/topic/view.do?model.id="+data[i].id+"' title='" + data[i].title + "' target='_blank'>" + data[i].createDate + "&nbsp;&nbsp;" + data[i].title +"</a>";
					htmlStr += "</div>";
				}
			}
			divContent.html(htmlStr);
		},
		error: function(){
			//nothing...
		}
	});
}

/**
 * 待阅事宜
 * @param divId
 */
function loadMails(divId){
	var divContent = $('#'+ divId);
	var width = divContent.width() - 20;
	var htmlStr = "";
	$.ajax({
		url: URL_PREFIX + '/pages/business/bumf/distribute/indexMails.do',
		type: 'post',
		dataType: 'json',
		data:{'viewCount':5},
		success: function(data, textStatus){
			if (data == null || data.length == 0){
				htmlStr = "<div style='margin:5px;'>暂无待阅...</div>";	    		
			}else{
				for(var i = 0; i < data.length; i++){
					htmlStr += "<div style='width :80%;white-space:nowrap;word-break:keep-all;overflow:hidden;text-overflow:ellipsis;' class='item'>";
					if(data[i].flag == true){
					htmlStr += "<a href='" + URL_PREFIX + "/pages/business/bumf/distribute/disviewMain.do?model.id="+data[i].id+"'model.incomingMessage.id='" + data[i].tid + "' > <span style='color: blue;'>【" + data[i].createDate + "】</span>" + data[i].title +"</a>";
					}else{
					htmlStr += "<a href='" + URL_PREFIX + "/pages/business/bumf/distribute/disviewMain.do?model.id="+data[i].id+"'model.outgoingMessage.id='" + data[i].tid + "'  ><span style='color: blue;'>【" + data[i].createDate + "】</span>" + data[i].title +"</a>";	
					}
					htmlStr += "</div>";
					}
			}
			if(htmlStr == ""){
				htmlStr = "<div style='margin:5px;'>暂无待阅...</div>";	
			}
			divContent.html(htmlStr);
		},
		error: function(){
			//nothing...
		}
	});
}

/**
 * 加载规章制度
 * @param divId
 */
function loadUsefulLink(divId){
	var divContent = $('#'+ divId);
	var width = divContent.width() - 20;
	var htmlStr = "";
	$.ajax({
		url: URL_PREFIX + '/ruleType/indexRuleTypes.do',
		type: 'post',
		dataType: 'json',
		data:{'viewCount':10},
		success: function(data, textStatus){
			if (data == null || data.length == 0){
				htmlStr = "<div style='margin:5px;'>暂无发布规章制度...</div>";	    		
			}else{
				for(var i = 0; i < data.length; i++){
					htmlStr += "<div style='width:" + width + "' class='item'>";
					htmlStr += "<a href='" + URL_PREFIX + "/rule/queryxx3.do?id="+data[i].id+"'>"+data[i].zcMc+"</a>";
					htmlStr += "</div>";
				}
			}
			divContent.html(htmlStr);
		},
		error: function(){
			
		}
	});
}


/**
 * 加载图片新闻信息
 * @param divId
 */
function loadArticlePhotos(divId){
	var divContent = $('#'+ divId);
	var width = divContent.width() - 20;
	var htmlStr = "";
	$.ajax({
		url: URL_PREFIX + '/article/indexArticlePhotos.do',
		type: 'post',
		dataType: 'json',
		data:{'viewCount':4},
		success: function(data, textStatus){
			if (data == null || data.length == 0){
				htmlStr = "<div style='margin:5px;'>暂无图片新闻...</div>";	    		
			}else{
				//htmlStr += "<marquee direction='left' height='110' scrollamount='2' onmouseover='this.stop()' onmouseout='this.start()'>";
				for(var i = 0; i < data.length; i++){
					htmlStr += "<a href='" + URL_PREFIX + "/article/view.do?model.id="+data[i].id+"' title='" + data[i].title + "' target='_blank'>";
					htmlStr += "<img style='border:#999999 1px solid;margin:2px;height:125px;max-width: 190px;' alt='" + data[i].title + "' src='" + URL_PREFIX +"/article/downloadArticlePhoto.do?model.id="+ data[i].id + "' />";
					htmlStr += "</a>";
				}
				//htmlStr += "</marquee>";
			}
			divContent.html(htmlStr);
		},
		error: function(){
			//nothing...
		}
	});
}




/**
 * 加载最新邮件
 * @param divId
 */
function loadMsg(divId){
	var divContent = $('#'+ divId);
	var width = divContent.width() - 100;
	var htmlStr = "";
	$.ajax({
		url: URL_PREFIX + '/mail/indexMails.do',
		type: 'post',
		dataType: 'json',
		data:{'viewCount':5},
		success: function(data, textStatus){
			if (data == null || data.length == 0){
				htmlStr = "<div style='margin:5px;'>暂无...</div>";	    		
			}else{
				for(var i = 0; i < data.length; i++){
					var readStr = (data[i].isRead == 1) ? "<span style='color:green'>【已读】</span>" : "<span style='color:red'>【未读】</span>";
					htmlStr += "<div style='width :80%;white-space:nowrap;word-break:keep-all;overflow:hidden;text-overflow:ellipsis;' class='item'>";
					htmlStr += "<a href='javascript:addTab(\"最新邮件\",\""+URL_PREFIX+"/mail/view1.do?mailId="+data[i].id+"\");'  title='" + data[i].subject + "'  target='main'>" + readStr + "<span style='color: blue;'>【" + data[i].sendTime + "】</span>" +data[i].subject +"</a>";
					htmlStr += "</div>";
				}
			}
			divContent.html(htmlStr);
		},
		error: function(){
			//nothing...
		}
	});
}

/**
 * 加载便签
 * @param divId
 */
var text;
var width;
function loadNote(divId){
	var divContent = $('#'+ divId);
	width = divContent.width() - 20;
	var htmlStr = "";
	$.ajax({
		url: URL_PREFIX + '/note/lastNote.do',
		type: 'post',
		dataType: 'json',
		success: function(data){
			if (data.id == null){
				htmlStr = "<div><textarea id='newContent' onclick='xinjian();' rows='7' class='note' readonly='true'>暂无...</textarea>" +"</div>";
			}else{
				htmlStr = "<div><textarea id='newContent' onclick='bianji();' rows='7' class='note' readonly='true'>" + data.content + "</textarea><input id='newId' type='hidden' value=" + data.id + ">" +"</div>";
				text = data.content;
			}
			divContent.html(htmlStr);
		},
		error: function(){
			//nothing...
		}
	});
}

function xinjian(){
	//alert("xinjian");
	$('#noteTop').removeAttr('onmouseover');
	$('#noteTop').removeAttr('onmouseout');
	$('#daohangtiao').css('display','block');
	$('#daohangtiao').empty().append("<a href='#' onclick='baocun();'><font color='red'>保存</font></a>");
	$('#note').empty().append("<textarea onblur='baocun()' id='newContent' rows='7' class='note'>1、\r\n2、\r\n3、\r\n4、\r\n5、</textarea><input id='newId' type='hidden'/>");
	var esrc = document.getElementById("newContent"); 
	var rtextRange =esrc.createTextRange(); 
	rtextRange.moveStart('character',2); 
	rtextRange.collapse(true); 
	rtextRange.select(); 
}
function bianji(){
	
	$('#noteTop').removeAttr('onmouseover');
	$('#noteTop').removeAttr('onmouseout');
	$('#daohangtiao').css('display','block');
	$('#daohangtiao').empty().append("<a href='#' onclick='baocun();'><font color='red'>保存</font></a>");
	var newId = $('#newId').val();
	//var newContent = $('#newContent').text();
	$('#note').empty().append("<textarea onblur='baocun()' id='newContent' rows='7' class='note'></textarea><input id='newId' type='hidden'/>");
	$('#newId').val(newId);
	$('#newContent').val(text);
	var esrc = document.getElementById("newContent"); 
	var rtextRange =esrc.createTextRange(); 
	rtextRange.moveStart('character',esrc.value.length); 
	rtextRange.collapse(true); 
	rtextRange.select(); 
	
	
}

function shanchu(){
	var id = $('#newId').val();
	if(id == undefined){
		alert("暂无内容，无法删除！");
	}else{		
		$.ajax({
			type : "POST",
			url : URL_PREFIX + "/note/jsondelete.do",
			dataType : 'json',
			data : {'newId' : id},
			success : function() {
				loadNote("note");
				
				$('#daohangtiao').css('float','right');
				$('#daohangtiao').css('margin-right','25px');
				$('#daohangtiao').css('display','none');
				
				$('#daohangtiao').empty().append("<a href='#' onclick='xinjian();'><img src='" + URL_PREFIX + "/images/exticons/add.gif'/></a>&nbsp;");
				$('#daohangtiao').append("<a href='#' onclick='bianji();'><img src='" + URL_PREFIX + "/images/exticons/edit.gif'/></a>&nbsp;");
				$('#daohangtiao').append("<a href='#' onclick='shanchu();'><img src='" + URL_PREFIX + "/images/exticons/delete.gif'/></a>");
				
				$('#noteTop').mouseover(function(){$('#daohangtiao').css('display','block');});
				$('#noteTop').mouseout(function(){$('#daohangtiao').css('display','none');});
				alert("删除成功！");
			}
		});
	}
}

function baocun(){
	var content = $('#newContent').val();
	var id = $('#newId').val();
	$.ajax({
		type : "POST",
		url : URL_PREFIX + "/note/jsonbaocun.do",
		dataType : 'json',
		data : {'newContent' : content,
				'newId' : id},
		success : function() {
			loadNote("note");
			
			$('#daohangtiao').css('float','right');
			$('#daohangtiao').css('margin-right','25px');
			$('#daohangtiao').css('display','none');
			
			$('#daohangtiao').empty().append("<a href='#' onclick='xinjian();'><img src='" + URL_PREFIX + "/images/exticons/add.gif'/></a>&nbsp;");
			$('#daohangtiao').append("<a href='#' onclick='bianji();'><img src='" + URL_PREFIX + "/images/exticons/edit.gif'/></a>&nbsp;");
			$('#daohangtiao').append("<a href='#' onclick='shanchu();'><img src='" + URL_PREFIX + "/images/exticons/delete.gif'/></a>");
			
			$('#noteTop').mouseover(function(){$('#daohangtiao').css('display','block');});
			$('#noteTop').mouseout(function(){$('#daohangtiao').css('display','none');});
			//alert("保存成功！");
		}
	});
}
function loadWel(wel){
	var nwel = $("#"+wel);
	var width = nwel.width() - 20;
	var nmncount=0;
	nwel.css("background-color","#DFE8F7");
	var nhtml = "";
	var dateTime=new Date();
    var yy=dateTime.getFullYear();
	var MM=dateTime.getMonth()+1;  //因为1月这个方法返回为0，所以加1
	var dd=dateTime.getDate();
	var days=[ "日 ", "一 ", "二 ", "三 ", "四 ", "五 ", "六 ",]; 
	var week=dateTime.getDay();
	nhtml+="<table><tr><td  colspan='3'>"+ yy+"年"+MM+"月"+dd+"日&nbsp;&nbsp;"+"星期"+days[week]+"</td><td></td><td></td></tr>";
	nhtml+="<tr style='height:10px;'><td></td><td></td><td></td></tr>";
	nhtml+="<tr><td style='width:25%;'>您现有：</td><td style='color:red;width:60%;'><a href='javascript:addTab(\"收件箱\",\""+URL_PREFIX+"/pages/scos/mail/mailLayout.jsp\");'   target='main'>收件箱&nbsp;(未读&nbsp;&nbsp;";
	$.ajax({                                                                                                                                                               
		url: URL_PREFIX + '/mail/indexMails.do',
		type: 'post',
		dataType: 'json',
		data:{'viewCount':0},
		success: function(data){
			if (data != null ){
				nhtml +=data+"&nbsp;&nbsp;封)&nbsp;</a></td>";
			}
			
					$.ajax({
						url: URL_PREFIX + '/jbpm/instance/notCount.do',
						type:'post',
						dataType:'json',
						success:function(data){
						for(var i = 0; i < data.length; i++){
						/*	if(data!=null){
								nmncount=data;
							}*/
							nhtml+="<tr><td style='width:15%;'></td><td style='color:red;width:60%;'><a href='javascript:addTab(\"待办事宜\",\""+URL_PREFIX+"/jbpm/instance/needMyHandle.do?className=all\");'   target='main'>待办事宜&nbsp;&nbsp;" +data[i].notCount +"&nbsp;&nbsp;个</a></td></tr>";
							nhtml+="<tr><td style='width:15%;'></td><td style='color:red;width:60%;'><a href='javascript:addTab(\"待阅事宜\",\""+URL_PREFIX+"/pages/business/bumf/distribute/toberead.do\");'   target='main'>待阅事宜&nbsp;&nbsp;" +data[i].dyCount +"&nbsp;&nbsp;个</a></td></tr>";
							nhtml+="<tr style='height:10px;'><td></td><td></td><td></td></tr>"
							nhtml+="<tr><td colspan='3' style='color:red;'><a href='http://www.163.com/' target='view_window'>网易</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href='http://www.ftchinese.com/' target='view_window'>FT中文网</a>&nbsp;&nbsp;&nbsp;&nbsp;</td></tr>";
							nhtml+="<tr><td colspan='3' style='color:red;'>系统30分钟自动注销，请及时保存文件！！！</td></tr>";
							nwel.html(nhtml);
						}
						}
					});
					
				}
			});
		}