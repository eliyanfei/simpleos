<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="net.simpleframework.web.page.component.base.ajaxrequest.AjaxRequestUtils"%>
<%
	AjaxRequestUtils.doAjaxRequest(request, response);
%>