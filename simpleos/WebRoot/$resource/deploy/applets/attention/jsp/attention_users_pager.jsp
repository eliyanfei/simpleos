<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.web.page.component.ui.pager.PagerUtils"%>
<%@ page import="net.simpleframework.organization.OrgUtils"%>
<%@ page import="net.simpleframework.applets.attention.AttentionBean"%>
<%
	final String jsp = OrgUtils.deployPath
			+ "jsp/account_layout_item.jsp";
	for (Object o : PagerUtils.getPagerList(request)) {
		request.setAttribute("__account_", OrgUtils.am()
				.queryForObjectById(((AttentionBean) o).getAccountId()));
%>
<jsp:include page="<%=jsp%>"></jsp:include>
<%
	}
%>
