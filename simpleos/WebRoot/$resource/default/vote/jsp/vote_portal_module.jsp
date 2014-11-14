<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="net.simpleframework.web.page.component.ui.portal.PortalUtils"%>
<%
	final String pageletId = request
			.getParameter(PortalUtils.PAGELET_ID);
%>
<div id="vote_<%=pageletId%>" class="vote_layout"></div>
<style type="text/css">
.vote_layout .vote_c {
	border: 0px;
	margin: 4px 0px 4px 4px;
	background: none !important;
}

.vote_layout .vote_c .vg {
	margin: 5px 0px 0px 0px;
}

.vote_layout .vote_c .vb {
	margin: 8px 4px 0px 4px;
}
</style>