<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%
	final String status = request.getParameter("status");
%>
<div id="remarkListId_news">
	<jsp:include flush="true" page="/app/docu/all/alldocu.jsp"><jsp:param
			value="false" name="showHead" /><jsp:param value="<%=status %>"
			name="status" /></jsp:include>
</div>