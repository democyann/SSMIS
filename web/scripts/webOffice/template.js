/****************************************************
*
* 					初始化控件
*
****************************************************/
// 关闭文档
function unloadFile() {
	document.all.WebOffice1.SetToolBarButton2("Menu Bar",1,4);	// 恢复被屏蔽的菜单栏文件菜单(word)
	document.all.WebOffice1.SetToolBarButton2("Standard",1,3);	// 恢复被屏蔽的新建项
	document.all.WebOffice1.SetToolBarButton2("Standard",2,3);	// 恢复被屏蔽的打开项
	document.all.WebOffice1.SetToolBarButton2("Standard",3,3);	// 恢复被屏蔽的保存项
	document.all.WebOffice1.SetKeyCtrl(595,0,0);				// 恢复保存快捷键(Ctrl+S) 
    document.all.WebOffice1.SetKeyCtrl(592,0,0);				// 恢复打印快捷键(Ctrl+P)
	document.all.WebOffice1.Close();
}
/****************************************************
*
* 					初始化控件
*
****************************************************/
// 重新装载文档
function LoadDocument(){
	unloadFile();					// 关闭
	WebOffice1_NotifyCtrlReady();	// 再打开
}
/****************************************************
*
* 			保存模板书签关联，和模板文件
*
****************************************************/
// 
function SaveDoc(templateId,recordId,fileType) {
	// 此处添加 SavaBookMarks()
	if(webform.FileName.value==""){
		alert("模板名称不能为空！");
		return false;
	}
	if(webform.Descript.value==""){
		alert("模板说明不能为空！");
		return false;
	}
	var returnValue;
	document.all.WebOffice1.HttpInit();			//初始化Http引擎
	// 添加相应的Post元素 
	document.all.WebOffice1.HttpAddPostString("TemplateId",templateId);
	document.all.WebOffice1.HttpAddPostString("RecordID", recordId);
	document.all.WebOffice1.HttpAddPostString("FileName",webform.FileName.value);
	document.all.WebOffice1.HttpAddPostString("Descript",webform.Descript.value);	
	document.all.WebOffice1.HttpAddPostString("FileType",fileType);
	document.all.WebOffice1.HttpAddPostCurrFile("FileBody","");		// 上传文件

	// 上传文件，并返回是否成功
	returnValue = document.all.WebOffice1.HttpPost("../template/SaveTemplate.jsp");	
	if("succeed" == returnValue){
			alert("文件上传失败");
	} else  {
			alert("文件上传成功");	
	}
}
/****************************************************
*
* 					保存文档至本地
*
****************************************************/
function SaveToLocal() {
	document.all.WebOffice1.ShowDialog(10000)
}
/****************************************************
*
* 					打印文档
*
****************************************************/
function PrintDoc() {
	document.all.WebOffice1.ShowDialog(88);	
}
/****************************************************
*
* 					打开本地文档
*
****************************************************/
function OpenLocalFile(fileType) {
	document.all.WebOffice1.LoadOriginalFile("open", fileType);	
}
/****************************************************
*
* 					定义当前文档书签
*
****************************************************/
function DefineMarks() {
	document.all.WebOffice1.BookMarkOpt("/ListBookMarks.jsp",1);
}
/****************************************************
*
* 					填充模板
*
****************************************************/
function FillBookMarks(){
	document.all.WebOffice1.BookMarkOpt("/FillBookMarks.jsp",2);
}