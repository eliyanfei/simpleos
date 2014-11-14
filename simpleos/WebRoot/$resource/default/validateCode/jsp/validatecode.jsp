<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="net.simpleframework.web.page.component.ui.validatecode.ValidateCodeUtils"%>
<%
	ValidateCodeUtils.doRender(request, response);
%>