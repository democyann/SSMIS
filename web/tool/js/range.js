loadingPane = null;
var rangeDeltaX = -5;
var rangeDeltaY = -5;
function showTitlePane(state,txt,iLeft,iTop) 
{
		loadingPane = $("mp_loading");
		if (loadingPane == null) {
			var el = document.createElement('DIV');
			el.setAttribute("id","mp_loading");
			el.setAttribute("align","center");
			el.style.cssText="display:none;font-family:宋体;font-size:14px;font-weight:bold;border:0px solid #000000;background-color:#f2f2f2;padding:1px;position:absolute; right:1px; top:1px; width:30%; height:12%; z-index:10000;filter:alpha(opacity=90);";
			document.body.appendChild(el);
			loadingPane = el;
		}
		if(typeof txt == "string")
		{
			//alert(iLeft);
			//alert(iTop);
			loadingPane.innerHTML=txt;
			if(typeof iLeft == "number")
				loadingPane.style.left = iLeft;
			if(typeof iTop == "number")
				loadingPane.style.top = iTop;
		}
		else
			loadingPane.innerHTML="";
		if (state) {
			
			loadingPane.style.display="block";
			//document.body.style.cursor="wait";
			
			//loadingPane.style.top = document.body.scrollTop+1;
		} else {
			loadingPane.style.display="none";
			//document.body.style.cursor="auto";
		}
}

/* 坐标模式 */
function drawLine(obj)
{
	//alert("hi");
	//alert(obj);
 var areaObj = obj;
 //alert(areaObj.href);

 
 var str = areaObj.coords;
 //alert(str);
 var arr = str.split(",");
 var xArr = new Array(arr.length/2);
 var yArr = new Array(arr.length/2);
 var x=0,y=0;
 var max =1,min =2;
 for (var t =0 ;t<arr.length ;t=t+2 )
 {
 	//alert("x == "+arr[t]);
 xArr[x++] = parseInt(arr[t]);
 }
 for (var t =1 ;t<arr.length ;t=t+2 )
 {
 	//alert("y == "+arr[t]);
 yArr[y++] = parseInt(arr[t]);
 }
var x1 = find(xArr,min);
//alert("x min == "+x1);
var x2 = find(xArr,max);
//alert("x max == "+x2);
var y1 = find(yArr,min);
//alert("y min == "+y1);
var y2 = find(yArr,max);
//alert("y max == "+y2);

 var mapObj = obj.parentElement;
 if(typeof mapObj == "object")
 {
 	//alert(mapObj.name);
	
	var imgs = document.all.tags("img");
	//alert(imgs.length);
	if(imgs != null)
	{
		for(var i=0;i<imgs.length;i++)
		{
			var imgobj = imgs.item(i);
			//alert(imgobj.src);
			var mapname = imgobj.useMap;
			//alert(mapname);
			if(typeof mapname == "string" && mapname.toLowerCase() == ("#" + mapObj.name).toLowerCase())
			{
				//alert(mapname);
				var imgleft = 0;
				var imgtop = 0;
				
				
				var imgparent = imgobj.parentElement;
				while(typeof imgparent == "object" && imgparent.tagName.toUpperCase() != "BODY")
				{
					/**
					alert(imgparent.tagName);
					alert(imgparent.offsetLeft);
					alert(imgparent.style.borderLeftWidth);
					alert(imgparent.offsetTop);
					alert(imgparent.style.borderTopWidth);
					*/
					imgleft += getWidthNumber(imgparent.offsetLeft) - getWidthNumber(imgparent.style.borderLeftWidth);
					imgtop += getWidthNumber(imgparent.offsetTop) - getWidthNumber(imgparent.style.borderTopWidth);
					imgparent = imgparent.parentElement;
				}
				
				imgleft = imgleft + rangeDeltaX;
				imgtop = imgtop + rangeDeltaY;
 var articleRe = /content_(\d+)\.htm/i;
 if(typeof areaObj.href == "string")
 {
 	var r = areaObj.href.match(articleRe);
	if(r)
	{
		var titleObj = $("mp" + r[1]);
		//titleObj="<table border=0 width=100% right=100%><tr><td align=center valign=middle>"+titleObj+"</td></tr></table>";
		//alert(titleObj);
		var xposition = event.clientX + document.body.scrollLeft - document.body.clientLeft + 3;
		var yposition = event.clientY + document.body.scrollTop  - document.body.clientTop;
		if(typeof titleObj == "object" && typeof titleObj.innerHTML == "string")
			showTitlePane(true, titleObj.innerHTML, xposition, yposition);
		else
			showTitlePane(false);
		
	}
}
				/**
				alert("left1 == "+imgleft);
				alert("top1 == "+imgtop);
				alert("x1 == "+x1);
				alert("x2 == "+x2);
				alert("y1 == "+y1);
				alert("y2 == "+y2);
				*/
				MouseOverMap(x1,y1,x2,y2,imgleft,imgtop);
			}
		}
	}
	
	
 }
}

function getWidthNumber(sWidth)
{
	if(typeof sWidth == "number")
		return sWidth;

	var re = /\d+/i;
	//alert("width == "+sWidth);
	var r = sWidth.match(re);
	if(r)
	{
		//alert(r);
		return parseInt(r);
	}
	else
		return 0;
}

function find(arr,type)
{
	var tmp=arr[0]; 
	if (type==1)
	{
	 for (var loop=0;loop<arr.length ;loop++ )
	 if (arr[loop]>tmp)
		 tmp = arr[loop];
	 return tmp;
	}
	else if (type == 2)
		{

	 for (var loop=0;loop<arr.length ;loop++ )
	 if (arr[loop]<tmp)
	 		 tmp = arr[loop];
	 return tmp;
	}

}

function MouseOverMap(x1,y1,x2,y2,imgleft,imgtop) {
var divElm = document.getElementById("leveldiv");
//alert("div == "+divElm);
var Left = x1;
var Top = y1;
var Right = parseInt(x2 - x1);
var bottom = parseInt(y2 - y1);
divElm.style.border = "solid 2px #FF0000";
divElm.style.left = Left+imgleft;
divElm.style.top = Top+imgtop;
divElm.style.width = Right+5;
divElm.style.height = bottom+5;
divElm.style.cursor = "pointer";
}

function MouseOutMap() {
	var divElm = document.getElementById("leveldiv");
	divElm.style.border = "";
	showTitlePane(false);
}
 
function clickmap(obj)
{
	//window.open(obj.href,"newwin","toolbar=no,resizable=no,scrollbars=yes,dependent=no,width=700,height=500");	
	//obj.target="_blank";
}