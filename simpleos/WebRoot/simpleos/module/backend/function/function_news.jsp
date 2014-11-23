<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@page
	import="net.simpleframework.content.news.NewsUtils"%>

<%
	final String status = request.getParameter("status");
	final String news = NewsUtils.deployPath + "jsp/news_mgr.jsp";
%>
<div id="remarkListId_news">
	<jsp:include flush="true" page="<%=news%>"><jsp:param
			value="false" name="showHead" /><jsp:param value="<%=status %>"
			name="status" /></jsp:include>
</div>