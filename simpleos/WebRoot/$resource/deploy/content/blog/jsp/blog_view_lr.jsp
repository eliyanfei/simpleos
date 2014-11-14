<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="net.simpleframework.web.page.component.ComponentParameter"%>
<%@ page
	import="net.simpleframework.content.component.newspager.NewsPagerUtils"%>
<%@ page import="net.simpleframework.content.blog.BlogUtils"%>
<%@ page import="net.simpleframework.util.DateUtils.TimeDistance"%>
<%
	final ComponentParameter nComponentParameter = new ComponentParameter(
			request, response, null);
	nComponentParameter.componentBean = BlogUtils.applicationModule
			.getComponentBean(nComponentParameter);
	final String blog_layout = BlogUtils.deployPath
			+ "jsp/blog_portal.jsp";
	request.setAttribute("__qs",
			NewsPagerUtils.queryRelatedNews(nComponentParameter));
%>
<div class="block_layout1">
	<div class="t f4">#(my_blog.5)</div>
	<div class="wrap_text"><jsp:include page="<%=blog_layout%>"
			flush="true">
			<jsp:param value="false" name="showMore" />
			<jsp:param value="dot1" name="dot" />
		</jsp:include></div>
</div>

<div class="block_layout1">
	<div class="t f4">#(my_blog.2)</div>
	<div class="wrap_text">
		<%
			request.setAttribute("__qs", BlogUtils.queryBlogs(
					nComponentParameter, null, TimeDistance.week, false, 1));
		%>
		<jsp:include page="<%=blog_layout%>" flush="true">
			<jsp:param value="false" name="showMore" />
			<jsp:param value="dot2" name="dot" />
		</jsp:include></div>
</div>