<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<head>
<style type="text/css">
.overlay{
		display:none;
		position:absolute;
        right:0px;
        bottom:0px;
        width:100%;
        height:100%;
        background-color:#D6E6FF;
        filter:alpha(opacity=55);
        -moz-opacity: 0.55;
        opacity: 0.55;        
        z-index:10;
        text-align: center;
}
#AjaxLoading{
	display:none;
	position: absolute;
	top: 40%;
	left: 40%;
	border: 3px solid #3980F4;
	width:200px;
	height: 60px;
	background-color: white;
	float: left;
	filter:alpha(opacity=100);
    text-align: center;
    z-index:11;
}

#exportImg{
	position: relative;
	top: 10px;
}

</style>
<script type="text/javascript">
	function overlay(){
		$("#AjaxLoading").css('display','block');
		$(".overlay").css('display','block');
	}
</script>
</head>
<body>
<div class="overlay">
</div>
	<div id="AjaxLoading" class="showbox" >
		<div class="loadingWord"><img id="exportImg" src="${ctx}/images/waiting.gif">正在导入，请稍候...</div>
	</div>
</body>
