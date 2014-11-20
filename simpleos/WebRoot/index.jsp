<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@page
	import="net.simpleframework.web.page.PageRequestResponse"%><%@page
	import="net.itsite.ItSiteUtil"%>
<%
	final PageRequestResponse requestResponse = new PageRequestResponse(request, response);
	response.sendRedirect(request.getContextPath()+"/index.html");
%>