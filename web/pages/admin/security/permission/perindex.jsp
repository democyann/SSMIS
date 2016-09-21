<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
<%@include file="/common/taglibs.jsp" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>权限列表</title>
<script src="${ctx}/scripts/jquery/jquery-1.3.2.js"></script>
<script type="text/javascript">
	function viewC(obj){
		var id = obj.id;
		if($('#'+id).attr('src')=='${ctx}/images/proj/close.gif'){
			$('#'+id).attr('src','${ctx}/images/proj/open.gif');
		}else{
			$('#'+id).attr('src','${ctx}/images/proj/close.gif');
		}
		$('#cdiv'+id).slideToggle('normal');
	}
	
	function allC(obj){
		var id = obj.id;
		if(obj.checked){
			$('#cdiv'+id+' input[disabled!="true"]').attr('checked',true);
		}else{
			$('#cdiv'+id+' input[disabled!="true"]').attr('checked',false);
		}
	}
	
	function parentC(obj){
		var id = obj.id;
		var ids = id.split(',');
		if($('input[id='+ids[1]+']').attr('checked',true)){
			$('input[id='+ids[1]+']').attr('checked',true);
		}else{
			$('input[id='+ids[1]+']').attr('checked',false);
		}
		var inputs = $('input[id='+id+']');
		var flag = false;
		for(n in inputs){
			if(inputs[n].checked){
				flag = true;
			};
		}
		if(flag){
			$('input[id='+ids[1]+']').attr('checked',true);
		}else{
			$('input[id='+ids[1]+']').attr('checked',false);
		};
	}
</script>
</head>
<body style="font-size: x-small;">
<form id="permissionForm">
	<input type="hidden" name="role.id" value="${role.id }">
	<table>
		<tr style="border: 1px solid #87CEFA;">
	<s:iterator value="parents" var="ps" status="p">
	<s:if test="#p.index <= 3">
		<s:if test="@com.systop.common.modules.security.user.webapp.PermissionAction@isHas(#ps.id)">
		<td width="25%" valign="top" style="border: 1px solid #87CEFA; padding: 2px;2px;2px;2px;">
				<div id="pdiv${id }" style="background-color: #87CEFA; "> 
				<s:if test="changed">
					<img id="${id }" onclick="viewC(this);" src="${ctx}/images/proj/open.gif" width="17.5px" height="17px"/>
					<input id="${id }" type="checkbox" onclick="allC(this);" name="selectedItems" value="${id }" checked="checked">${descn }
				</s:if>
				<s:else>
					<img id="${id }" onclick="viewC(this);" src="${ctx}/images/proj/open.gif" width="17.5px" height="17px"/>
					<input id="${id }" type="checkbox" onclick="allC(this);" name="selectedItems" value="${id }">${descn }
				</s:else>
				</div>
				<div id="cdiv${id }">
				<s:iterator value="#ps.childrenPermission" var="cs">
					<s:if test="@com.systop.common.modules.security.user.webapp.PermissionAction@isHas(#cs.id)">
					<div>
						<s:if test="changed">
							|--
							<input id="c,${parentPermission.id }" onclick="parentC(this);" type="checkbox" name="selectedItems" checked="checked" value="${id }">${descn }
						</s:if>
						<s:else>
							|--
							<input id="c,${parentPermission.id }" onclick="parentC(this);" type="checkbox" name="selectedItems" value="${id }">${descn }
						</s:else>
					</div>
					</s:if>
					<s:else>
					<div>
						<s:if test="changed">
							|--
							<input disabled="true" type="checkbox" name="selectedItems" checked="checked" value="${id }">${descn }
						</s:if>
						<s:else>
							|--
							<input disabled="true" type="checkbox" name="selectedItems" value="${id }">${descn }
						</s:else>
					</div>
					</s:else>
				</s:iterator>
				</div>
		</td>
		</s:if>
		<s:else>
			<td  width="25%" valign="top" style="border: 1px solid #87CEFA;padding: 2px;2px;2px;2px;">
					<div style="background-color: #87CEFA; ">
						<s:if test="changed">
							<img id="${id }" onclick="viewC(this);" src="${ctx}/images/proj/open.gif" width="17.5px" height="17px"/>
							<input disabled="true" type="checkbox" name="selectedItems" value="${id }" checked="checked">${descn }
						</s:if>
						<s:else>
							<img id="${id }" onclick="viewC(this);" src="${ctx}/images/proj/open.gif" width="17.5px" height="17px"/>
							<input disabled="true" type="checkbox"  name="selectedItems" value="${id }">${descn }
						</s:else>
					</div>
					<div id="cdiv${id }">
					<s:iterator value="#ps.childrenPermission" var="cs">
						<div>
						<s:if test="changed">
							|--
							<input disabled="true" type="checkbox" name="selectedItems" checked="checked" value="${id }">${descn }
						</s:if>
						<s:else>
							|--
							<input disabled="true" type="checkbox" name="selectedItems" value="${id }">${descn }
						</s:else>
					</div>
					</s:iterator>
					</div>
			</td>
		</s:else>
	</s:if>
	</s:iterator>
		</tr>
		<tr>
	<s:iterator value="parents" var="ps" status="s">
		<s:if test="#s.index > 3">
		<s:if test="@com.systop.common.modules.security.user.webapp.PermissionAction@isHas(#ps.id)">
		<td  width="25%" valign="top" style="border: 1px solid #87CEFA;padding: 2px;2px;2px;2px;">
				<div id="pdiv${id }" style="background-color: #87CEFA; ">
				<s:if test="changed">
					<img id="${id }" onclick="viewC(this);" src="${ctx}/images/proj/open.gif" width="17.5px" height="17px"/>
					<input id="${id }" type="checkbox" onclick="allC(this);" name="selectedItems" value="${id }" checked="checked">......1${descn }
				</s:if>
				<s:else>
					<img id="${id }" onclick="viewC(this);" src="${ctx}/images/proj/open.gif" width="17.5px" height="17px"/>
					<input id="${id }" type="checkbox" onclick="allC(this);" name="selectedItems" value="${id }">.....${descn }
				</s:else>
				</div>
				<div id="cdiv${id }">
				<s:iterator value="#ps.childrenPermission" var="cs">
					<s:if test="@com.systop.common.modules.security.user.webapp.PermissionAction@isHas(#cs.id)">
					<div>
						<s:if test="changed">
							|--
							<input id="c,${parentPermission.id }" onclick="parentC(this);" type="checkbox" name="selectedItems" checked="checked" value="${id }">${descn }
						</s:if>
						<s:else>
							|--
							<input id="c,${parentPermission.id }" onclick="parentC(this);" type="checkbox" name="selectedItems" value="${id }">${descn }
						</s:else>
					</div>
					</s:if>
					<s:else>
					<div>
						<s:if test="changed">
							|--
							<input disabled="true" type="checkbox" name="selectedItems" checked="checked" value="${id }">${descn }
						</s:if>
						<s:else>
							|--
							<input disabled="true" type="checkbox" name="selectedItems" value="${id }">${descn }
						</s:else>
					</div>
					</s:else>
				</s:iterator>
				</div>
		</td>
		</s:if>
		<s:else>
			<td   width="25%" valign="top" style="border: 1px solid #87CEFA;padding: 2px;2px;2px;2px;">
					<div style="background-color: #87CEFA; ">
					<s:if test="changed">
						<img id="${id }" onclick="viewC(this);" src="${ctx}/images/proj/open.gif" width="17.5px" height="17px"/>
						<input disabled="true" type="checkbox" name="selectedItems" value="${id }" checked="checked">${descn }
					</s:if>
					<s:else>
						<img id="${id }" onclick="viewC(this);" src="${ctx}/images/proj/open.gif" width="17.5px" height="17px"/>
						<input disabled="true" type="checkbox" name="selectedItems" value="${id }">${descn }
					</s:else>
					</div>
					<div id="cdiv${id }">
					<s:iterator value="#ps.childrenPermission" var="cs">
						<div>
						<s:if test="changed">
							|--
							<input disabled="true" type="checkbox" name="selectedItems" checked="checked" value="${id }">${descn }
						</s:if>
						<s:else>
							|--
							<input disabled="true" type="checkbox" name="selectedItems" value="${id }">${descn }
						</s:else>
					</div>
					</s:iterator>
					</div>
			</td>
		</s:else>
	</s:if>
	</s:iterator>
		</tr>
	</table>
</form>
</body>
</html>