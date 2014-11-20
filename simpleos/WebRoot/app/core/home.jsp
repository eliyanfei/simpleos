<%@page import="net.simpleframework.my.home.MyHomeUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@page
	import="net.prj.manager.template.PrjTemplateUtils"%>
<%
	final String templateUrl = PrjTemplateUtils.getTemplateUrl();
	final String center = MyHomeUtils.deployPath + "jsp/my_home.jsp";
%>
<jsp:include page="<%=templateUrl%>" flush="true">
	<jsp:param value="<%=center%>" name="center" />
</jsp:include>