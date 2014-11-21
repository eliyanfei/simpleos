<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@page
	import="net.simpleos.backend.template.TemplateUtils"%>
<%
	final String templateUrl = TemplateUtils.getTemplateUrl();
	final String center = request.getParameter("center");
%>
<jsp:include page="<%=templateUrl%>" flush="true">
	<jsp:param value="<%=center%>" name="center" />
</jsp:include>