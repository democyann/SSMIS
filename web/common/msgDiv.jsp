<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div id="bgDiv" style="position: absolute; top:0; left:0;z-index:2000;border:0px cursor:wait;FILTER:Alpha(Opacity=30);">
</div>

<div id="makingDiv"
	style="position:absolute;
	       z-index: 99999;
	       left: 50%;
	       top: 50%; 
	       height:50px;
	       width: 300px; 
	       margin-top:-25px;
	       margin-left:-80px;
	       background:#BAD0EE;
	       visibility: hidden; 
	       border: 0;vertical-align: middle;">

<div id="msgDiv"
	style="position: absolute; 
	       top: 1px;
	       left: 1px; 
	       background: #E8F2FE;
	       color: #003366;
	       vertical-align: middle; 
	       margin:1px; 
	       height:46px; 
	       line-height: 46px;
	       width:296px; 
	       color: red; 
	       overflow: hidden;
	       padding-top: 15px;
	       padding-bottom: 10px;
	       padding-left: 10px;" 
	      >
</div>
</div>
<script type="text/javascript">
function loadDiv(arg){
  	var makingDiv = document.getElementById('makingDiv'); 
    var msgDiv = document.getElementById('msgDiv');
    var bgDiv = document.getElementById('bgDiv');
    if(makingDiv && msgDiv){  
    	var msgImg = document.getElementById('msgImg');
    	//如果还没有msgImg,则创建,以避免重复创建
    	if(!msgImg){
    	  //创建img元素
          msgImg = document.createElement("img");
          msgImg.setAttribute("id","msgImg");
          msgImg.setAttribute("src","<c:url value="/images/grid/loading.gif"/>");
          msgImg.setAttribute("style","width:16px;height:16px;padding:5px;border: 0;");
          msgImg.setAttribute("align","absmiddle");
          //创建文本元素,值为调用本js函数时的参数
          var msgTxt = document.createTextNode(arg);     
          //将img元素放入msgDiv
          msgDiv.appendChild(msgImg);
          //将文本节点放入msgDiv
          msgDiv.appendChild(msgTxt);
          //将msgDiv放入makingDiv
          makingDiv.appendChild(msgDiv);
    	}
        //显示makingDiv(弹出提示层)
        makingDiv.style.visibility = 'visible';
        if(bgDiv){
          //用于遮挡选择框的iframe
          var iframeCover = document.getElementById('iframe_cover');
          if(!iframeCover){
          	iframeCover = document.createElement("iframe");
          	iframeCover.setAttribute("id","iframe_cover");
          	iframeCover.setAttribute("src","");;
          	iframeCover.setAttribute("scrolling","no");
          	iframeCover.setAttribute("frameborder","0");
          	iframeCover.setAttribute("vspale","0");
          	iframeCover.setAttribute("style","position:absolute; top:0px;left:0px;z-index:-1;");
          	//将iframe放入bgDiv
          	bgDiv.appendChild(iframeCover);          	 
          }
          //设置iframe和bgDiv的宽度和高度,以遮盖主整个右侧页面
          iframeCover.style.width = document.body.clientWidth;
          iframeCover.style.height = document.body.clientHeight; 
          bgDiv.style.width = document.body.clientWidth;
          bgDiv.style.height = document.body.clientHeight; 
          //设置bgDiv为可见
       	  bgDiv.style.visibility = 'visible';       	  
        }
    }   
 }
</script>