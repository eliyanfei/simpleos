<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.my.home.MyHomeUtils"%>
<%
	final String center = MyHomeUtils.deployPath + "jsp/my_home.jsp";
%>
<jsp:include page="/simple/template/c1.jsp" flush="true">
	<jsp:param value="<%=center%>" name="center" />
</jsp:include>