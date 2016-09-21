<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="Cache-Control" content="no-cache"/>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Expires" content="0"/>

<META NAME="ROBOTS" CONTENT="NOINDEX, NOFOLLOW">
<meta name="viewport" content="width=1024">

<script type="text/javascript">
   if(typeof jQuery == 'undefined') {
      document.write("<script type='text/javascript' src='${ctx}/scripts/jquery/jquery-1.7.2.min.js' ><\/script>");
   }
</script>
<script type="text/javascript" src="${ctx}/scripts/index.js" ></script>
<%--播放器脚本--%>
<script type="text/javascript" src="${ctx}/scripts/AWPlayer/jwplayer.js"></script>
<%--打印脚本--%>
<script type="text/javascript" src="${ctx}/scripts/jquery/print/jquery.PrintArea.js_4.js"></script>

<%--样式--%>
<LINK href="${ctx}/styles/style.css" type='text/css' rel='stylesheet'>
<link href="${ctx}/styles/kindEditor.mod.css" type='text/css' rel='stylesheet'>

<%-- 
<link rel="icon" href="${ctx}/images/001.ico" type="image/png">
<link rel="shortcut icon" href="${ctx}/images/001.ico" > --%>

<%--issue501--%>
<script type="text/javascript">
    setTimeout(function () {
        if(typeof Ext != "undefined")
            jQuery("#ext-comp-1001").removeAttr("name");
    }, 1000);
</script>

<script type="text/javascript">

	var clicktime=0;
	$(function(){
		var jqueryForm = $('#save');
		if(jqueryForm ==undefined){
			return;
		}
		
		jqueryForm.submit = function(){
			if($("#save").validate().form()){
			clicktime++;
			//alert(clicktime);
			if(clicktime!=1){
				return false;
			}
			 
			}
		}
		
	});
</script>