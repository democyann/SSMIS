/**
 * JqueryEasyUI, DataGride获取一条记录
 * @param dataGride
 * @returns
 */
function getSelected(dataGride) {
	var row = dataGride.datagrid('getSelected');
	if (row == null) {
		msgAlert('请选择一条您要操作的数据！');
		return null;
	}
	return row;
}

var systemMsg = "系统提示";

function msgAlert(msg){
	$.messager.alert(systemMsg, msg);
}


function submitFrom(id) {
	document.getElementById(id).submit();
}

function submitForm(id){
	$('#'+id).submit();
}

function resetForm(id) {
	document.getElementById(id).reset();
}

