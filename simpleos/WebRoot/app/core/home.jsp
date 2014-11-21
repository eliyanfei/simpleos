<%@page import="net.simpleframework.my.home.MyHomeUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	final String center = MyHomeUtils.deployPath + "jsp/my_home.jsp";
%>
<jsp:include page="/simpleos/template/template.jsp" flush="true">
	<jsp:param value="<%=center%>" name="center" />
</jsp:include>