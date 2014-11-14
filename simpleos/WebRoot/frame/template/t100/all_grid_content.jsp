<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@page
	import="net.simpleframework.desktop.DesktopUIUtils"%><%@page
	import="net.simpleframework.web.page.PageRequestResponse"%>
<%
	PageRequestResponse requestResponse = new PageRequestResponse(request, response);
%>
<link type="text/css" rel="stylesheet"
	href="<%=request.getContextPath()%>/frame/template/t3/themes/default/css/grid.css">
<div id="grid_tab_content"
	style="position: fixed; top: 0px; left: 0px; width: 100%; height: 100%; background-color: #836FFF; z-index: 1000000000; overflow: hidden;">
	<div style="height: 60px; border-bottom: 1px solid black;">
		<div></div>
		<div style="float: right; vertical-align: bottom;">
			<input type="button" class="button1" value="返回"
				onclick="$('all_grid_content').toggle();" id="all_grid_content_id">
		</div>
	</div>
	<div class="aMg_line_x"></div>
	<div class="grid_tab_container" style="height: 100%;" align="center">
		<%=DesktopUIUtils.buildGridDesktop(requestResponse)%>
	</div>
</div>