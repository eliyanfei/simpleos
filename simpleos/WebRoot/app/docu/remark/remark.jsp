<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="net.simpleframework.content.component.remark.RemarkUtils"%>
<%@page import="net.simpleframework.web.page.PageRequestResponse"%>

<%
	final PageRequestResponse requestResponse = new PageRequestResponse(request, response);
	String hashId = requestResponse.getRequestParameter(RemarkUtils.BEAN_ID);
%>
<div class="remark">
	<div id="remark_docu_list"></div>
</div>