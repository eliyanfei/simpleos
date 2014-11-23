<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@page
	import="net.simpleframework.content.news.NewsUtils"%><%@page
	import="net.simpleframework.content.blog.BlogUtils"%>

<%
	final String blog_home = BlogUtils.deployPath + "jsp/blog_mgr_home.jsp";
%>
<div>
	<jsp:include flush="true" page="<%=blog_home%>"><jsp:param
			value="false" name="showHead" /></jsp:include>
</div>