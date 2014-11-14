<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.content.bbs.BbsUtils"%>
<%@ page import="net.simpleframework.web.page.PageRequestResponse"%>
<%
	final PageRequestResponse requestResponse = new PageRequestResponse(
			request, response);
%>
<div class="bbs_forum">
	<jsp:include page="bbs_toolbar_pane.jsp" flush="true">
		<jsp:param value="true" name="tools" />
	</jsp:include>
	<div class="c">
		<%=BbsUtils.forumView2(requestResponse)%>
	</div>
</div>
<script type="text/javascript">
	(function() {
		$$(".bbs_forum .ctitle img").each(function(img) {
			$Comp.imageToggle(img, img.up().next());
		});
	})();
</script>