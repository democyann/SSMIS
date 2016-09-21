<%
	String theme = (String) session.getAttribute("currentTheme");

	if (theme == null || "".equals(theme.trim())) {
		theme = "cupertino";
		session.setAttribute("currentTheme", theme);
	}
%>
<link href="${ctx}/scripts/jquery/ui/css/${currentTheme}/jquery-ui.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${ctx}/scripts/jquery/jquery-1.9.1.js"></script>
<script type="text/javascript" src="${ctx}/scripts/jquery/ui/jquery-ui-1.10.2.custom.js"></script>
<script type="text/javascript">
	$(function() {
		$(document).tooltip();
	});
</script>
