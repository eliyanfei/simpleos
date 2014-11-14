<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.web.page.PageRequestResponse"%>
<%@ page import="net.simpleframework.content.ContentUtils"%>
<%@ page import="net.simpleframework.content.blog.BlogUtils"%>
<%@ page import="net.simpleframework.organization.OrgUtils"%>
<%@ page import="net.simpleframework.web.EFunctionModule"%>
<%@ page
	import="net.simpleframework.organization.account.AccountViewLogUtils"%>
<%
	final PageRequestResponse requestResponse = new PageRequestResponse(
			request, response);
	final boolean myAccount = ContentUtils.getAccountAware()
			.isMyAccount(requestResponse);
	final String blog_remark_layout = BlogUtils.deployPath
			+ "jsp/blog_remark_layout.jsp";
%>
<div class="block_layout2">
	<div class="t f4">
		#(my_blog.0)
		<%
			if (myAccount) {
		%>
		<span><a onclick="$Actions['__my_blog_catalog'].add();"
			style="font-weight: normal;">#(Add)</a> </span>
		<%
			}
		%>
	</div>
	<div class="c">
		<div id="__my_blog_catalog"></div>
	</div>
</div>

<div class="block_layout1">
	<div class="t f4">#(my_blog.6)</div>
	<div class="wrap_text">
		<%
			request.setAttribute("__qs",
					BlogUtils.queryRemarks(requestResponse));
		%>
		<jsp:include page="<%=blog_remark_layout%>" flush="true">
			<jsp:param value="false" name="showMore" />
			<jsp:param value="dot2" name="dot" />
			<jsp:param value="true" name="homeAccount" />
		</jsp:include></div>
</div>

<div class="block_layout1">
	<div class="t f4">#(my_blog.8)</div>
	<div class="wrap_text">
		<%
			AccountViewLogUtils
					.updateLog(requestResponse, EFunctionModule.blog);
			request.setAttribute("__qs", AccountViewLogUtils.queryLogs(
					requestResponse, EFunctionModule.blog));
			String view_log_layout = OrgUtils.deployPath
					+ "jsp/account_view_log_layout.jsp";
		%>
		<jsp:include page="<%=view_log_layout%>" flush="true"></jsp:include></div>
</div>