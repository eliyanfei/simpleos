<%@page import="net.simpleframework.web.page.PageRequestResponse"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@page import="net.itsite.document.docu.DocuUtils"%>

<%
	try {
		final PageRequestResponse requestResponse = new PageRequestResponse(request, response);
		DocuUtils.doDownload(requestResponse);
	} finally {
		out.clear();
		out = pageContext.pushBody();
	}
%>