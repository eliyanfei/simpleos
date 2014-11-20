<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@page
	import="net.prj.manager.template.PrjTemplateUtils"%>
<%
	final String templateUrl = PrjTemplateUtils.getTemplateUrl();
%>
<jsp:include page="<%=templateUrl %>" flush="true">
	<jsp:param value="/app/myfavorite/myfavorite_c.jsp" name="center" />
</jsp:include>