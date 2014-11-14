<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@page
	import="net.simpleframework.util.StringUtils"%><%@page
	import="net.simpleframework.content.EContentStatus"%><%@page
	import="net.itsite.document.docu.EDocuFunction"%><%@page
	import="net.itsite.document.docu.DocuUtils"%><%@page
	import="net.simpleframework.web.page.PageRequestResponse"%>
<%@page import="net.simpleframework.web.EFunctionModule"%>
<%
	final PageRequestResponse requestResponse = new PageRequestResponse(request, response);
%>
<jsp:include page="/app/docu/all/alldocu_c.jsp" flush="true"></jsp:include>
