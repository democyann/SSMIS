<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>主页面</title>
<%@include file="/common/meta.jsp"%>
<%@include file="/common/extjs.jsp"%>
<!-- <script type="text/javascript" src="main.jsp"></script> -->
<script>
var tabsDemo;
var modelwidth =  window.parent.document.body.offsetWidth ;
var client_height = document.body.offsetHeight;
Ext.onReady(function(){
	  Ext.BLANK_IMAGE_URL = '${ctx}/images/001.png';
	  Ext.QuickTips.init();

    tabsDemo=new Ext.TabPanel({
           renderTo:Ext.getBody(),
           activeTab:0,
           enableTabScroll:true,//挤的时候能够滚动收缩
           height:client_height, 
           width:modelwidth-170,
           resizeTabs:true,//宽度能自动变化,但是影响标题的显示
           frame:true,
           //下面是比上面例子新增的关键右键菜单代码
           listeners:{
                    //传进去的三个参数分别为:这个tabpanel(tabsDemo),当前标签页,事件对象e
                   "contextmenu":function(tdemo,myitem,e){
                	
                	   if(myitem.id!='newtab办公桌面'){//办公桌面不可以关闭
                	 
                	   menu=new Ext.menu.Menu([{
                           text:"关闭当前页",
                           handler:function(){
                              tdemo.remove(myitem);
                        
                           }
                  },{
                           text:"关闭其他所有页",
                           handler:function(){
                              //循环遍历
                              tdemo.items.each(function(item){
                                   if(item.closable&&item!=myitem)
                                   {
                                      //可以关闭的其他所有标签页全部关掉
                                      tdemo.remove(item);
                                   
                                   }
                              });
                           }

                  },{
                      text: '关闭全部标签',
                      handler : function(){
                    	  tdemo.items.each(function(item){
                              if(item.closable){
                            	//	alert(item.id);
                            	  tdemo.remove(item);
                            	  $('#zmIframe').attr('src','${ctx}/pages/main.jsp' );//给Tab里面的iFrame重新加载
                            
                              }
                          });
                      }
                	   
                  }
                  
                  ]);

                  //显示在当前位置
                  menu.showAt(e.getPoint());
                    }
                   }
           },
           items:[{

                     title:"系统桌面",
                     id:"newtab办公桌面",
                    // autoLoad:{url:"${ctx}/pages/main.jsp"}
                     frame:false, 
                     html:'<iframe id="zmIframe" src="${ctx}/pages/main.jsp" style="width:100%; height:100%; border:0px;overflow:hidden;" frameborder="0" scrolling="auto"></iframe>',
                     listeners:{// 添加监听器，点击此页面的tab时候要重新加载（刷新功能）
                    	 activate :function(tab){
                             $('#zmIframe').attr('src','${ctx}/pages/main.jsp' );//给Tab里面的iFrame重新加载
					     	}
					   }
           }]

    });
    
});


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

</script>

</head>
<body bgcolor="#FFFFFF">
	<%-- 	<div style="background-color: #8AB2ED; height: 25px;">
	  <a id="AddNewTab" href="javascript:addTab('个人信息','${ctx}/security/user/editSelf.do');">添加新标签页</a>
	</div> --%>
</body>
</html>
