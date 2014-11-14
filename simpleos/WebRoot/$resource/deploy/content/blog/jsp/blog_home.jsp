<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.web.page.PageRequestResponse"%>
<%@ page import="net.simpleframework.content.blog.BlogUtils"%>
<%@ page import="net.simpleframework.util.StringUtils"%>
<%@ page import="net.simpleframework.web.WebUtils"%>
<%@ page
	import="net.simpleframework.organization.component.login.LoginUtils"%>
<%@ page import="net.simpleframework.organization.account.IAccount"%>
<%@ page
	import="net.simpleframework.organization.account.AccountSession"%>
<%
	final PageRequestResponse requestResponse = new PageRequestResponse(
			request, response);
	final String appUrl = BlogUtils.applicationModule
			.getApplicationUrl(requestResponse);
	final IAccount login = AccountSession.getLogin(requestResponse
			.getSession());
	final String myblogUrl = login == null ? LoginUtils
			.getLocationPath() : BlogUtils.applicationModule
			.getBlogUrl(requestResponse, login.user());
%>
<div class="simple_toolbar2 clear_float">
	<div style="float: right;" id="__blog_sech"></div>
	<div style="float: left;">
		<div style="margin-bottom: 4px;">
			<input type="button" class="button2" value="#(my_blog.3)"
				onclick="$Actions.loc('<%=WebUtils.addParameters(myblogUrl, "op_act=add") %>');" />
		</div>
		<div><%=BlogUtils.applicationModule.tabs(requestResponse)%></div>
	</div>
</div>
<div id="__g_blog_pager"></div>
<script type="text/javascript">	
	(function() {
		var sb = $Comp.searchButton(function(sp) {
			$Actions.loc("<%=appUrl%>".addParameter("c=" + $F(sp.down(".txt"))));
		}, function(sp) {
		}, null, 210);
		$("__blog_sech").update(sb);
		<%final String c = WebUtils.toLocaleString(request.getParameter("c"));
			if (StringUtils.hasText(c)) {%>
			sb.down(".txt").setValue("<%=c%>");
		<%}%>
	})();
</script>