<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.web.page.PageRequestResponse"%>
<%@ page import="net.simpleframework.content.news.NewsUtils"%>
<%@ page import="net.simpleframework.util.StringUtils"%>
<%@ page import="net.simpleframework.web.WebUtils"%>
<%@ page import="net.simpleframework.web.IWebApplicationModule"%>
<%
	final PageRequestResponse requestResponse = new PageRequestResponse(request, response);
	final String appUrl = NewsUtils.applicationModule.getApplicationUrl(requestResponse);
	final String op = request.getParameter("op");
%>
<div class="news_app">
	<div class="simple_toolbar2">
		<div style="height: 24px;">
			<div style="float: left;">
				<input type="button" class="button2" id="_news_add"
					onclick="$Actions['__news_app_pager'].add();" value="#(App.news.0)" />
				<%
					if (IWebApplicationModule.Utils.isManager(requestResponse, NewsUtils.applicationModule)) {
				%>
				<a class="a2" style="margin-left: 10px;"
					onclick="$Actions['newsManagerToolsWindow']();">#(manager_tools.0)</a>
				<%
					}
				%>
			</div>
		</div>
	</div>
	<div id="__news_mgr_app_pager"></div>
</div>
<script type="text/javascript">
$ready(function(){
	<%if ("add".equals(op)) {%>
			$('_news_add').click();
		<%}%>
});
</script>
