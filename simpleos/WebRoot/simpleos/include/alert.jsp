<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@page
	import="net.simpleframework.util.StringUtils"%>

<%
	String info = StringUtils.text(request.getParameter("info"), "");
%>
<div class="sy_alert_dialog">
	<div class="top">
		<div class="img"></div>
		<div class="txt wrap_text">
			<%=info%>
		</div>
	</div>
	<div class="btn">
		<input type="button" value="确定" onclick="$IT.C('alertWin');">
	</div>
</div>

