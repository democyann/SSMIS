<link rel="stylesheet" type="text/css" href="${ctx }/scripts/easy_ui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="${ctx }/scripts/easy_ui/themes/icon.css">
<%@include file="/common/meta.jsp" %>
<script type="text/javascript" src="${ctx }/scripts/easy_ui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${ctx }/scripts/easy_ui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="${ctx }/scripts/easy_ui/validatebox.js"></script>
<script type="text/javascript">
$(function(){
	
	
	//window.history.forward(1);
	
});
function pleaseWait(mes) {
	$("<div class=\"datagrid-mask\"></div>").css({display:"block",width:"100%",height:$(window).height()}).appendTo("body");
	$("<div class=\"datagrid-mask-msg\"></div>").html(mes).appendTo("body").css({display:"block",left:($(document.body).outerWidth(true) - 190) / 2,top:($(window).height() - 45) / 2});
}
function dispalyLoad() {
    $(".datagrid-mask").remove();
    $(".datagrid-mask-msg").remove();
}
</script>