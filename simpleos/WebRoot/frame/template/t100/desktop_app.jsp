<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="net.simpleframework.web.page.PageRequestResponse"%><%@page
	import="net.simpleframework.desktop.DesktopUIUtils"%>
<%
	PageRequestResponse requestResponse = new PageRequestResponse(request, response);
%>
<%=DesktopUIUtils.buildDesktop(requestResponse)%>
<script type="text/javascript">
$ready(function() {
	$desktop.positionTab();
	$Actions['desktop']();
});
</script>