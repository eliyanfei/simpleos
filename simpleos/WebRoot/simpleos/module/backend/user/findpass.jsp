<%@page import="net.simpleos.utils.StringsUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.util.StringUtils"%>
<%@ page import="net.simpleframework.organization.OrgUtils"%>
<%
	String sid = request.getParameter("sid");
	if (!StringsUtils.isNotBlank1(sid)) {
		response.sendRedirect("/index.html");
	}
%>
<div align="center" style="margin-top: 50px;">
	<form id="_userpwd_form">
		<input type="hidden" id="sid" name="sid" value="<%=sid%>" />
		<div id="_userpwd"></div>
	</form>
</div>


