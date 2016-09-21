var selMap = {};
$(function(){
    if ($("#userChooser").length == 0)
        $("<div id='userChooser'/>").appendTo("body");
});
/**
 * 
 * @param func
 * @param clear
 * @param one
 * @param authoritys
			职位权限：多个用,分开
 * @see com.systop.scos.position.model.Position
 * @returns
 */
function openSel(func,clear,one,authoritys)
{
	var url = URL_PREFIX+"/pages/admin/security/user/selector/selectorMail.jsp";
	if($.trim(authoritys) != ''){
		url += "?authoritys=" + authoritys;
	}
    onlyOne = one;
    if (navigator.userAgent.search(/android|iphone|ipad/i) == -1) {
        var conf = "dialogWidth=610px;dialogHeight=440px;status=no;help=no;scrollbars=no";
        var users = window.showModalDialog(url, window, conf);
        //直接关闭的话就不操作什么了
        if(users)
            func(users);
        if(clear){
        selMap = {};
        }
    } else
    {
        //create is faster than open,cache insteadof reload
        $("#userChooser").dialog({
            height:480,
            width:640,
            modal:true,
            create:function(){
                $(this).load(url);
            },
            open:function()
            {
                //每次都读新的
                window._selMap = {};
                $.extend(window._selMap, selMap);
                //因为第一页不再加载
                try{
                    $("#users")[0].contentWindow.afterFillForm();
                }catch(e){}
            }
        });
        this.returnAction = function(users){
            func(users);
            selList = [];
            $("#userChooser").dialog("close");
        }
    }
}