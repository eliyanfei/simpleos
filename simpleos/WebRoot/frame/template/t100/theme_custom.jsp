<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="net.simpleframework.web.page.PageRequestResponse"%><%@page
	import="net.simpleframework.desktop.DesktopUIUtils"%>

<%
	PageRequestResponse requestResponse = new PageRequestResponse(request, response);
%>
<style>
#themeCustomId div {
	float: left;
}

#themeCustomId div img {
	cursor: pointer;
}
</style>
<div style="height: 500px;">
	<div id="themeuploadId"></div>
	<div id="themeCustomId">
		<%=DesktopUIUtils.buildTheme(requestResponse, true)%>
	</div>
</div>