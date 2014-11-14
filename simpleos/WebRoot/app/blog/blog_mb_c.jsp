<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.content.blog.BlogUtils"%><%@page
	import="net.a.ItSiteUtil"%><%@page
	import="net.simpleframework.web.page.component.ComponentParameter"%>
<%
	final String my_blog_c = BlogUtils.deployPath + "jsp/my_blog_c.jsp";
%>
<table width="100%">
	<tr>
		<td valign="top"  class="List_PageletsPage">
			<jsp:include page="<%=my_blog_c%>" flush="true"></jsp:include>
		</td>
		<td width="264" valign="top">
			<jsp:include page="my_blog_lr.jsp"></jsp:include>
		</td>
	</tr>
</table>
