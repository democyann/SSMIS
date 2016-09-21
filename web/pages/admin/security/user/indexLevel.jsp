<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<%@include file="/pages/common/taglibs.jsp"%>
		<%@include file="/pages/common/meta.jsp"%>
		<%@include file="/common/validator.jsp"%>
		<script type="text/javascript" src="${ctx}/scripts/my97/WdatePicker.js"></script>
		<title>审批优先级设定</title>
	</head>
<body>
	<div id="r_main">
		
		<div class="x-toolbar">
	
</div>
<div class="x-panel-body">
<div align="center">
		<table width="800" >
				<tr>
					<td style="border: 0px; padding-top: 22px;"	align="left"><%@include file="/pages/common/messages.jsp"%></td>
				</tr>
		</table>
	</div>	
			<table id="fineTable" width="700">
				
				<tr>
					<th width="100">排列序号</th>
					<th width="150">姓名</th>
					<th width="200">处室</th>
					<th width="250">职位</th>
				  <th width="250">排序</th>
				</tr>
				<s:iterator value="items"  var="item" status="st">
					<tr onmousemove="this.className='trOver';"
						onmouseout="this.className='trOut';">
						<td align="center">${st.index+1}</td>
						<td align="center">${item.name}</td>
						<td align="center">${item.dept.name}</td>
						<td align="center">${item.position.name}</td>
					    <td align="center">
					     <s:if test="#st.last == false " >
					    <a href="${ctx}/security/user/downOrder.do?model.id=${item.id}" title="向下"><img src="${ctx}/images/icons/col-move-top.gif" width="10" height="10"></a>&nbsp;&nbsp;&nbsp;
					    </s:if>
					     <s:if test="#attr.item.orderId != 1 " >
					    <a href="${ctx}/security/user/upOrder.do?model.id=${item.id}" title="向上"><img src="${ctx}/images/icons/col-move-bottom.gif" width="10" height="10"></a>
					    </s:if>
					    </td>
					</tr>
				</s:iterator>
			</table>
		</div>
	</div>
<script type="text/javascript">
		function changeUser(formid, msg) {
			var checked = false;
			$('input').each(function(i, item) {
				if (item.checked && item.id == 'selectedItems') {
					checked = true;
				}
			});
			if (!checked) {
				alert('请至少选择一条要删除信息');
				return;
			}

			if (confirm(msg)) {
				var s = null;
				$('input').each(function(i, item) {//循环每一个表单里的input元素
					if (item.checked && item.id == 'selectedItems') {//确定选中的是复选框
						if(s!=null){
						s=s+item.value+",";
						}else{//第一次
							s=item.value+",";
						}
					}
				});
				window.location.href = "remove.do?ids=" + s;
			}
		}
	</script>
	<script type="text/javascript">
		function jkjsSave() {
			window.location = "${ctx}/security/user/indexLevel.do"
		}
	</script>

</body>
</html>