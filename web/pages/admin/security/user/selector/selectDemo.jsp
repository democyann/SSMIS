<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	选择用户：
	<input id="names" type="text" onclick="openSelector(this)">
	<input id="ids" type="text">

	<script type="text/javascript">
		function openSelector(obj) {
			var ids = document.getElementById("ids");
			var conf = "dialogWidth=608px;dialogHeight=438px;status=no;help=no;scrollbars=no";
			var users = window.showModalDialog("selector.jsp", null, conf);
			var selectNames = "";
			var selectIds = "";
			if (users != null){
				for(var i = 0; i < users.length; i++){
					selectNames = selectNames + users[i].name + ",";
					selectIds = selectIds + users[i].id + ",";
				}
			}
			obj.value = selectNames;
			ids.value = selectIds;
		}
	</script>
	<br>
</body>
</html>