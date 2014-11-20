<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.content.IContentPagerHandle"%>
<%@ page import="net.simpleframework.applets.tag.TagUtils"%>
<%@ page import="net.simpleframework.web.page.PageRequestResponse"%>
<%@ page import="net.simpleframework.content.bbs.BbsUtils"%>
<%@ page import="net.simpleframework.content.bbs.BbsForum"%>
<%@ page import="net.simpleframework.applets.tag.ITagApplicationModule"%>
<%@ page import="net.simpleframework.web.EFunctionModule"%><%@page
	import="net.itsite.ItSiteUtil"%>

<jsp:include page="bbs_toolbar_pane.jsp" flush="true"></jsp:include>
<%
	final BbsForum forum = BbsUtils.getForum(new PageRequestResponse(request, response));
	if (forum != null) {
		ItSiteUtil.addMenuNav(request.getSession(), null, forum.getText(), false);
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
<div id="bbsTopicPager"></div>
<%
	if ("add".equals(request.getParameter("op_act"))) {
%>
<script type="text/javascript">
$ready(function() {
	(function() {
		$Actions['bbsTopicPager'].add();
	}).delay(0.5);
});
</script>
<%
	}
%>