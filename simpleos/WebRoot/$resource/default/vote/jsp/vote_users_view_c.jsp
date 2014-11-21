<%@page import="net.simpleframework.util.DateUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="net.simpleframework.web.page.component.ui.pager.PagerUtils"%>
<%@ page import="net.simpleframework.organization.OrgUtils"%>
<%@ page import="net.simpleframework.content.component.vote.VoteResult"%>
<%
	final String jsp = OrgUtils.deployPath
			+ "jsp/account_layout_item.jsp";
	final boolean admin = OrgUtils.isManagerMember(session);
	for (Object o : PagerUtils.getPagerList(request)) {
		final VoteResult result = (VoteResult) o;
		request.setAttribute("__account_", OrgUtils.am()
				.queryForObjectById(result.getUserId()));
%><jsp:include page="<%=jsp%>"><jsp:param value="<%=DateUtils.getRelativeDate(result.getCreateDate()) %>" name="__showDate"/></jsp:include><%
	if (admin) {
%><a style="vertical-align: top;" class="delete2_image"
	onclick="$Actions['ajaxVoteUserDelete']('resultId=<%=result.getId()%>')"></a>
<%
	}
	}
%>