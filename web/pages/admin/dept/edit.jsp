<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<title>编辑机构</title>
<%@include file="/common/extjs.jsp"%>
<%@include file="/common/meta.jsp"%>
<%@include file="/common/validator.jsp"%>
<%@include file="/common/uploadify.jsp"%>
<%@include file="/common/kindeditor.jsp"%>
<link href="${ctx}/styles/treeSelect.css" type='text/css' rel='stylesheet'>
<link href="${ctx}/scripts/ztree3.5/css.css" rel="stylesheet" type="text/css"></link>
<script type="text/javascript" src="${ctx}/scripts/ztree3.5/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript" src="${ctx}/scripts/ztree3.5/jquery.ztree.excheck-3.5.js"></script>
<script type="text/javascript">
var rootName = '石家庄瑞海检疫技术服务有限公司';
var initValue = '${model.parentDept.name}';
if (initValue.length == 0) {
	//initValue = rootName;
}
</script> 
<script type="text/javascript">
function schoolType(){
	var deptDictName = '${deptDictName}';
	var deptSchoolType = $("#deptType").val();
	if(deptSchoolType != deptDictName){
		$("#schoolTypeId").css('display','none');
		/* document.getElementById("schoolTypeId").style.display="none"; */
	}else{
		$("#schoolTypeId").attr("style","display:block;");
	}
}
</script>
<style type="text/css">
body {
	margin: 0px;
	padding: 0px;
	border: 0px none #ffffff;
	font-size: 12px;
	overflow: auto;
}

.select {
	width: 30px;
	font-weight: bold;
	}
.ztreeDiv{
	width:250px;
	border:solid 1px #CBCBCB;
}
}
</style>
</head>
<body>
	<div class="x-panel" style="margin: 5px;"><%@ include file="/common/messages.jsp"%>
		<s:form id="saveDeptForm" action="save" theme="simple" validate="true" method="post">
			<fieldset>
				<legend>机构信息编辑</legend>
				<%-- <s:hidden name="model.id" /> --%>
				<input type="hidden" name="model.id" value="${model.id}">
				<input type="hidden" name="judge" id="judge" value="${model.judgement}">
				<input type="hidden" name="sub" id="sub" value="${model.jgbm}">
				<input type="hidden" name="pId" id="pId" value="${model.parentDept.id}">
				<s:hidden name="model.serialNo" />
				<br>
				<table width="360" style="line-height: 25px;" align="center">
					<tr>
						<td width="80" align="right">上级机构：</td>
						<td width="250" align="left">
							<div id="comboxWithTree"></div> 
						 <input type="hidden" id="parentId"  name="parentId" > 
						</td>
					</tr>
					
					  <tr>
					     <td width="80"  align="right" id="somekey"></td>
			             <td width="250" id="somevalue">
			             </td>
				     </tr>
				       <tr>
					     <td width="80"  align="right" id="somekey1"></td>
			             <td width="250" id="somevalue1">
			             </td>
				     </tr>
					<tr>
						<td width="80" align="right">机构名称：</td>
						<td width="250">
							<s:textfield name="model.name" id="deptName" maxlength="25" cssStyle="width:200px;" cssClass="required" />
							<font color="red">*</font>
						</td>
					</tr>
					<tr>
						<td width="80" align="right">机构简称：</td>
						<td width="250">
							<s:textfield name="model.shortName" id="shortName" maxlength="25" cssStyle="width:200px;" cssClass="required" />
							<font color="red">*</font>
						</td>
					</tr>
					
                         <tr>
                             <td width="80" align="right">机构类型：</td>
                            <td width="250" >
                             <s:select id="deptType" name="model.deptType.id" list="deptTypeMap"  headerKey="" headerValue="请选择" cssStyle="width:200px; " cssClass="required" />	
                              <font color="red">*</font></td>
                        </tr>
                         <tr>
					    <td width="80" align="right">机构编码：</td>
                        <td width="250"><s:textfield name="model.code" id="code" size="25" cssStyle="width:200px;"  />
                        
                        <s:if test="#attr.model.id == null">
				        </s:if>
			          	
                        </td>
                        </tr>
                         <tr>
                             <td width="80" align="right">机构描述：</td>
                            <td width="250">
                             <s:textarea name="model.descn" id="descn" theme="simple" rows="7" cols="32"
                             cssStyle="border:1px #cbcbcb solid;z-index:auto; width:200px;"/></td>
                        </tr>
					<%-- <c:if test="${model.jgDept != null}"> --%>
					<tr style="display: none;" id="jg">
					   <td width="80" align="right">监管部门：</td>
					   <td width = "250">
					       <div class="ztreeDiv">
							<ul id="ztree" class="ztree"></ul>
						    </div>
		                 <s:hidden id="deptIds" name="model.jgDept" ></s:hidden>
					   </td>
					</tr>
					<%-- </c:if> --%>
					<%-- <s:hidden name="model.serialNo"/> --%>
					<tr>
						<td colspan="2" align="center" height="40">
							<input type="button" value="保存" onclick="jkjsSave()" class="button">&nbsp;&nbsp;&nbsp;&nbsp;
						    <input type="reset" value="重置" class="button" onclick="dj();">&nbsp;&nbsp;&nbsp;&nbsp;
							<input type="button" value="返回" onclick="history.go(-1)" class="button">
						</td>
					</tr>
				</table>
			</fieldset>
		</s:form>
	</div>
	<script type="text/javascript" src="${ctx}/pages/admin/dept/edit.js"></script>
	<script type="text/javascript">
		var cz ="0";
		if(cz =="0"){
			$('#parentId').val("${model.parentDept.id}");
		}else{
			$('#parentId').val("");
		}
	var dtree ;
	//当点击重置按钮时清空父节点
	function dj(){
		dtree.initValue ="";
		cz="1";
	}
	var rootName = '石家庄瑞海检疫技术服务有限公司';
	var rootNameTwo = '监管部门';
		Ext.onReady(function() {
			 dtree = new DeptTree({
				url : '/systop/dept/deptTree.do?model.id=${model.id}',
				rootVisible : false,//是否显示根节点
				rootName : rootName,
				el : 'comboxWithTree',
				innerTree : 'inner-tree',
				initValue : initValue,
				onclick : function(id,text) {
					getOnclickDept(text,rootName,rootNameTwo); //判断单选按钮是否显示问题
					Ext.get('parentId').dom.value = id;
					getDeptType(id);
					getDeptJum(id);//通过得到的id查询是监管部门还是检疫局
				}
			});
			getOnclickDeptEdit(); //修改时判断单选按钮是否显示问题
			dtree.init();
		});

		//添加jquery验证
		$(document).ready(function() {
			$("#saveDeptForm").validate();
					$("#typeCbo").attr("checked", true);
					$('#type').val("1");
		});
		
		function clickTypeCbo(typeCbo){
			if(typeCbo.checked){
				$('#type').val("1");
			}else{
				$('#type').val("0");
			}
		}
		
		$.validator.addMethod("typeCheck", function(value, element) {
		    var res = "";
		    var strType = $('input[name="type"]:checked').val();
			if(strType == null || strType == '') {
				res = "err";
				document.getElementById('typeDesc').innerHTML = '<font color="red">未选择机构所属类型</font>';
			}else{
				document.getElementById('typeDesc').innerHTML = '';
			}
		    return res != "err";
		},"");
		
		// 通过机构ID得到机构type值
		function getDeptType(parentId){
			$.ajax({
				type : "POST",
				url : "${ctx}/systop/dept/findDept.do",
				data : {
					"parentId" : parentId 
				},
				dataType : "json",
				success : function(result) {
					if(result.type != null){
						var type = result.type;
						if(type != null && type == "1"){
							$("#typeCbo").attr("checked", true);
						}else{
							$("#typeCbo").attr("checked", false);
						}
						$("#type").val(type);
					}
				},
				error : function(XMLHttpRequest, textStatus, errorThrown) {
					Ext.MessageBox.show({
						title : '提示',
						minWidth : 250,
						msg : XMLHttpRequest + "@@" + textStatus + "@@@"
								+ errorThrown,//"发生异常，请与管理员联系！",
						buttons : Ext.MessageBox.OK,
						icon : Ext.MessageBox.INFO
					});
				}
			});
		}
		//根据机构的Id查询机构是监管部门还是检疫局
		function getDeptJum(parentId){
			$.ajax({
				type : "POST",
				url : "${ctx}/systop/dept/findDeptJum.do",
				data : {
					"parentId" : parentId 
				},
				dataType : "json",
				success : function(result) {
					 if(result != null){
						 var type = result.fg;
						 if(type == true){
							 $("#jg").show();
							}else{
								$("#jg").hide();
							}
					} 
				},
				error : function(XMLHttpRequest, textStatus, errorThrown) {
					Ext.MessageBox.show({
						title : '提示',
						minWidth : 250,
						msg : XMLHttpRequest + "@@" + textStatus + "@@@"
								+ errorThrown,//"发生异常，请与管理员联系！",
						buttons : Ext.MessageBox.OK,
						icon : Ext.MessageBox.INFO
					});
				}
			});
		}
		
		//点击机构时判断显示问题
		function getOnclickDept(text,rootName,rootNameTwo){
			//检疫局
			 if(text == rootName){
				 $('#somekey').html('请选择：');
					$('#somevalue').html(
						"<input type='radio' name='r1' value='1' checked='checked' >公司本部"
					   +"<input type='radio' name='r1' value='2' >分支机构"
					);
			 }else{
					$('#somekey').html('');
					$('#somevalue').html('');
			} 
			//监管部门
			 if(text == rootNameTwo){
				 $('#somekey1').html('请选择：');
					$('#somevalue1').html(
						"<input type='radio' name='r1' value='1' checked='checked' >河北局"
					   +"<input type='radio' name='r1' value='2' >河北局分支机构"
					);
			 }else{
				 $('#somekey1').html('');
			     $('#somevalue1').html('');
			 }
		}
		//修改时判断单选按钮是否显示问题
		function getOnclickDeptEdit(){
			//修改分支--检疫局
			if(document.getElementById("judge").value == 1 
					&& "" != document.getElementById("sub").value){ //公司本部
				$('#somekey').html('请选择：');
				$('#somevalue').html(
					"<input type='radio' name='r1' value='1' checked='checked' >公司本部"
				   +"<input type='radio' name='r1' value='2' >分支机构"
				);
			}else if(document.getElementById("judge").value == 2
					&& "" != document.getElementById("sub").value){ //分支机构
				$('#somekey').html('请选择：');
				$('#somevalue').html(
					"<input type='radio' name='r1' value='1' >公司本部"
				   +"<input type='radio' name='r1' value='2' checked='checked'>分支机构"
				);
			}
			
			
			//修改分支---》监管部门
			if(document.getElementById("judge").value == 1 
					&& "" == document.getElementById("sub").value){ //公司本部
				$('#somekey1').html('请选择：');
				$('#somevalue1').html(
						"<input type='radio' name='r1' value='1' checked='checked' >河北局"
						   +"<input type='radio' name='r1' value='2' >河北局分支机构"
				);
			}else if(document.getElementById("judge").value == 2
					&& "" == document.getElementById("sub").value){ //分支机构
				$('#somekey1').html('请选择：');
				$('#somevalue1').html(
						"<input type='radio' name='r1' value='1'  >河北局"
						   +"<input type='radio' name='r1' value='2' checked='checked'>河北局分支机构"
				);
			}
			
			if(document.getElementById("sub").value!= '5' && document.getElementById("judge").value==5){
				$("#jg").hide();
			}
			if(document.getElementById("sub").value == '5' && document.getElementById("judge").value!='1' 
					&& document.getElementById("judge").value!='2' && document.getElementById("pId").value != ""){
			  $("#jg").show();
			}
		}
		
		function jkjsSave() {
			/* if(Ext.get('parentId').dom.value==""){
				alert("请选择所属机构！");
			}else{ */
			$('#saveDeptForm').attr("action", "${ctx}/systop/dept/save.do");
			$('#saveDeptForm').submit();
			/* } */
		}
</script>
<script>
			$(function(){
				 zTree = $.fn.zTree.init($("#ztree"), setting, null);
			}); 
			//监督部门
			var deptIds = "${model.jgDept}";
			var setting = {
				check:{
					enable: true,
					chkStyle:"radio",
					chkboxType: { "Y": "s", "N": "ps"}
				},
				data:{
					simpleData:{
						enable: true,
						idKey: "id",
						pIdKey: "pId",
						rootPId: -1
					}
				},
				async:{
					enable: true,
					url: URL_PREFIX + "/systop/dept/deptZtreeJg.do?deptIds=" + deptIds,
					autoParam: ["deptIds="+deptIds],
					type: 'POST',
					dataType: 'json'
				},
				callback: {
					onCheck: OnCheckNode
				}
			};
			
			function OnCheckNode(event, treeId, treeNode){
				var zTree1 = $.fn.zTree.getZTreeObj("ztree");
				var nodes = zTree1.getCheckedNodes(true);
				var deptIds=',';
				for(var i=0 ;i<nodes.length ; i++){
					deptIds += nodes[i].id +',';
				}
				$('#deptIds').val(deptIds);
}
</script>
</body>
</html>