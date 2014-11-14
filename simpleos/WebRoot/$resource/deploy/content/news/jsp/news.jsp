<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.web.page.PageRequestResponse"%>
<%@ page import="net.simpleframework.content.news.NewsUtils"%>
<%@ page import="net.simpleframework.util.StringUtils"%>
<%@ page import="net.simpleframework.web.WebUtils"%>
<%@ page import="net.simpleframework.web.IWebApplicationModule"%>
<%
	final PageRequestResponse requestResponse = new PageRequestResponse(
			request, response);
	final String appUrl = NewsUtils.applicationModule
			.getApplicationUrl(requestResponse);
%>
<div class="news_app">
	<div class="simple_toolbar2">
		<div style="height: 24px;">
			<div style="float: right;" id="__news_sech"></div>
			<div style="float: left;">
				<input type="button" class="button2"
					onclick="$Actions['__news_app_pager'].add();" value="#(App.news.0)" />
			</div>
		</div>
		<div style="display: none;">search panel</div>
		<div class="tab"><%=NewsUtils.applicationModule.tabs(requestResponse)%></div>
		<div class="tab2"><%=NewsUtils.applicationModule.tabs2(requestResponse)%></div>
	</div>
	<div id="__news_app_pager"></div>
</div>
<script type="text/javascript">
	(function() {
		var sb = $Comp.searchButton(function(sp) {
			$Actions.loc("<%=appUrl%>".addParameter("c=" + $F(sp.down(".txt"))));
		}, function(sp) {
		}, null, 210);
		$("__news_sech").update(sb);
		<%final String c = WebUtils.toLocaleString(request.getParameter("c"));
		if (StringUtils.hasText(c)) {%>
			sb.down(".txt").setValue("<%=c%>");
		<%}%>
	})();
</script>
