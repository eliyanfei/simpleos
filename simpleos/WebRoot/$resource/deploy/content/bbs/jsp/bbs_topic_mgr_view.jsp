<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.content.IContentPagerHandle"%>
<%@ page import="net.simpleframework.applets.tag.TagUtils"%>
<%@ page import="net.simpleframework.web.page.PageRequestResponse"%>
<%@ page import="net.simpleframework.content.bbs.BbsUtils"%>
<%@ page import="net.simpleframework.content.bbs.BbsForum"%>
<%@ page import="net.simpleframework.applets.tag.ITagApplicationModule"%>
<%@ page import="net.simpleframework.web.EFunctionModule"%><%@page
	import="net.a.ItSiteUtil"%><%@page import="net.simpleframework.web.IWebApplicationModule"%>

<%
	final PageRequestResponse requestResponse = new PageRequestResponse(request, response);
	final BbsForum forum = BbsUtils.getForum(requestResponse);
%>
<div class="bbs_toolbar_pane simple_toolbar2">
	<div class="tbar" style="height: 24px;">
		<div style="float: right;" id="__bbs_topic_sech"></div>
		<div style="float: left;">
			<%
				if (forum != null) {
			%>
			<input type="button" class="button2"
				onclick="$Actions['bbsTopicPager'].add();" value="#(topicpager.0)" />
			<%
				} else {
			%><input type="button" class="button2" id="bbs_forum_add"
				onclick="$Actions['forumAddSelectDict']();" value="#(topicpager.0)" />
			<%
				}
				if (IWebApplicationModule.Utils.isManager(requestResponse, BbsUtils.applicationModule)) {
			%><a class="a2" style="margin-left: 10px;"
				onclick="$Actions['bbsManagerToolsWindow']();">#(BbsUtils.3)</a>
			<%
				}
			%>
		</div>
	</div>
</div>
<%
	if (forum != null) {
		final StringBuilder tags_layout = new StringBuilder();
		tags_layout.append(TagUtils.deployPath).append("jsp/tags_layout.jsp?").append(IContentPagerHandle._VTYPE).append("=")
				.append(EFunctionModule.bbs.ordinal()).append("&").append(ITagApplicationModule._CATALOG_ID).append("=")
				.append(forum.getId());
		if (forum.isShowTags()) {
%><div class="simple_toolbar">
	<jsp:include page="<%=tags_layout.toString()%>">
		<jsp:param value="40" name="rows" />
	</jsp:include>
</div>
<%
	}
	}
%>
<div id="bbsTopicMgrPager"></div>
<%
	if ("add".equals(request.getParameter("op_act"))) {
%>
<script type="text/javascript">
$ready(function() {
	(function() {
		$('bbs_forum_add').click();
	}).delay(0.5);
});
</script>
<%
	}
%>