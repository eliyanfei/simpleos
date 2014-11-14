<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.web.page.PageRequestResponse"%>
<%@ page import="net.simpleframework.content.ContentUtils"%>
<%@ page import="net.simpleframework.organization.account.IAccount"%>
<%
	final PageRequestResponse requestResponse = new PageRequestResponse(
			request, response);
	final IAccount account = ContentUtils.getAccountAware().getAccount(
			requestResponse);
	if (account == null) {
		return;
	}
	request.setAttribute("__user", account.user());
	final boolean myAccount = ContentUtils.getAccountAware()
			.isMyAccount(requestResponse);
%>
<div class="simple_toolbar2 clear_float">
	<%
		if (myAccount) {
	%>
	<div style="float: right;">
		<input type="button" class="button2" value="#(my_blog.3)"
			onclick="$Actions['__my_blog_pager'].add();" />
	</div>
	<%
		}
	%>
	<div style="float: left;">
		<jsp:include page="my_blog_bar.jsp"></jsp:include>
	</div>
</div>
<div id="__my_blog_pager"></div>
<%
	if ("add".equals(request.getParameter("op_act"))) {
%>
<script type="text/javascript">
	$ready(function() {
		(function() {
			$Actions['__my_blog_pager'].add();
		}).delay(0.5);
	});
</script>
<%
	}
%>

