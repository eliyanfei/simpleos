<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.organization.OrgUtils"%><%@page
	import="net.simpleframework.web.page.PageRequestResponse"%><%@page import="net.a.ItSiteUtil"%>
<%
	PageRequestResponse requestResponse = new PageRequestResponse(request, response);
%>
<%=OrgUtils.tabs(requestResponse)%>