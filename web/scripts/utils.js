/**
 * Use for delete rows in ecside
 * 
 * @param options = {
 *            noneSelectedMsg:"If user select nothing to delete",
 *            confirmMsg:"Confirm message.", ecsideFormId:"ecside from id",
 *            deleteFormId:"Remove form id, you must provide a form to post
 *            delete request." }
 */
function onRemove(options) {
	if (!options)
		options = {};
	var opt = {
		noneSelectedMsg : (options.noneSelectedMsg)
				? options.noneSelectedMsg
				: '请至少选择一个.',
		confirmMsg : (options.confirmMsg) ? options.confirmMsg : '确认删除吗?',
		ecsideFormId : (options.ecsideFormId) ? options.ecsideFormId : 'ec',
		deleteFormId : (options.deleteFormId)
				? options.deleteFormId
				: 'removeForm'
	};
	var checked = false;
	$('input').each(function(i, item) {
		if (item.checked && item.id == 'selectedItems') {
			checked = true;
		}
	});
	if (!checked) {
		alert(opt.noneSelectedMsg);
		return;
	}

	if (confirm(opt.confirmMsg)) {
		$('#' + opt.ecsideFormId)[0].action = $('#' + opt.deleteFormId)[0].action;		
		$('#' + opt.ecsideFormId)[0].submit();
	} else {
		return false;
	}
}

var Util = function() {
};

Util.resetDatePicker = function(pickerId) {
    var picker = document.getElementById(pickerId);
    var v = picker.value;
    if(!v) return;
    var idx = v.indexOf('0:00:00.000');
    idx = (idx > 0) ? idx : v.indexOf(' 00:00:00.0');
    if( idx > 0) {  
        v = v.substring(0, idx);
    } 
    picker.value = v;
};

/**
 * 处理运算时候,浮点计算不准确的问题
 * @param f 需要处理的数字
 * @param size 保留的位数
 * @return {Number}
 */
function formatFloat(f,size)
{
    var tf=f*Math.pow(10,size);
    tf=Math.round(tf+0.000000001);
    tf=tf/Math.pow(10,size);
    return tf;
}

/**
 * 格式化为rmb
 * todo 小数点位数不太对
 * @param num
 * @return {String}
 */
function formatCurrency(num){
    try{
    num = new Number(num);
    if (Number.isNaN(num))
        num = 0.00;
    return "￥" + num.toLocaleString();
    }
    catch(e){
        return num;
    }
}

/**
 * 加载脚本,适合加载站外的脚本,不会影响页面的加载
 * @param url
 * @param callback
 */
function loadScript(url, callback){

    var script = document.createElement("script")
    script.type = "text/javascript";

    if (script.readyState){  //IE
        script.onreadystatechange = function(){
            if (script.readyState == "loaded" ||
                script.readyState == "complete"){
                script.onreadystatechange = null;
                if(callback)
                callback();
            }
        };
    } else {  //Others
        script.onload = function(){
            if(callback)
            callback();
        };
    }

    script.src = url;
    document.getElementsByTagName("head")[0].appendChild(script);
}

/**
 * 打开窗口
 * 使用方法
 *
 $(".item-link").bind("click",function(){
 openLiteWindow($(this).attr("href"));
 return false;
 })
 *
 * @param url
 * @param width
 * @param height
 * @param name,不传的话会自动重用,传null的话,会复用
 */
function openLiteWindow(url,width,height,name){
    width=width || 710;
    height=height || 525;
    if(typeof name=="undefined")
        name="";
    var iTop = (window.screen.availHeight-30-height)/2; //获得窗口的垂直位置;
    var iLeft = (window.screen.availWidth-10-width)/2; //获得窗口的水平位置;
    window.open(url,name,"height="+height+",width="+width+",status=no,toolbar=no,menubar=no,location=no,scrollbars=yes,top="+iTop+",left="+iLeft+",resizable=yes");

}

/**
 * 将p替换为br
 * @param content
 * @return {*}
 */
function formatContentEditable(content){
    return content.replace(/<\/p>/gi,'<br>').replace(/<p>|<br>$/gi,'')
}
