<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="net.simpleframework.web.page.component.ui.pager.PagerUtils"%>
<%@ page import="net.simpleframework.organization.OrgUtils"%>
<%@ page import="net.simpleframework.my.space.UserAttentionBean"%>
<%@ page import="net.simpleframework.util.StringUtils"%>
<%
	final String jsp = OrgUtils.deployPath
			+ "jsp/account_layout_item.jsp";
	final boolean attention = StringUtils.hasText(request
			.getParameter("userId"));
	for (Object o : PagerUtils.getPagerList(request)) {
		final Object id = attention ? ((UserAttentionBean) o)
				.getAttentionId() : ((UserAttentionBean) o).getUserId();
		request.setAttribute("__account_", OrgUtils.am()
				.queryForObjectById(id));
%>
<jsp:include page="<%=jsp%>"></jsp:include>
<%
	}
%>
