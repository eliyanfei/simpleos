<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="net.simpleframework.web.page.component.ui.portal.module.RssUtils"%>
<%@ page import="net.simpleframework.web.page.PageRequestResponse"%>
<%@ page import="net.simpleframework.util.StringUtils"%>
<%
	final PageRequestResponse requestResponse = new PageRequestResponse(
			request, response);
	final String id = "rss_" + StringUtils.hash(requestResponse);
%>
<div id="<%=id%>" class="rss_">
	<%
		out.write(RssUtils.rssRender(requestResponse));
	%>
</div>
<script type="text/javascript">
	$ready(function() {
		$Actions["ajaxRssTooltip"].createTip("#<%=id%> .rss a");
	});
</script>