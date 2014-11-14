<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@page
	import="net.simpleframework.content.news.NewsUtils"%><%@page
	import="net.simpleframework.util.DateUtils.TimeDistance"%><%@page
	import="net.simpleframework.content.EContentType"%><%@page
	import="net.simpleframework.web.page.PageRequestResponse"%><%@page
	import="net.simpleframework.util.ConvertUtils"%><%@page
	import="net.simpleframework.content.blog.BlogUtils"%>

<%
	final PageRequestResponse requestResponse = new PageRequestResponse(request, response);
	final String blog_layout = BlogUtils.deployPath + "jsp/blog_portal.jsp";
	final TimeDistance timeDistance = TimeDistance.whichOne(request.getParameter("time"));
	final int _tab_param = ConvertUtils.toInt(request.getParameter("_tab_param"), -1);
	request.setAttribute("__qs", BlogUtils.queryBlogs(requestResponse, null, timeDistance, false, 0, _tab_param));
%>
<jsp:include page="<%=blog_layout%>" flush="true">
	<jsp:param value="false" name="_show_tabs" />
</jsp:include>
