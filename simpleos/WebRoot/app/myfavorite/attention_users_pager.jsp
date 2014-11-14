<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="net.simpleframework.web.page.component.ui.pager.PagerUtils"%>
<%@ page import="net.simpleframework.organization.OrgUtils"%>
<%@ page import="net.simpleframework.applets.attention.AttentionBean"%><%@page
	import="net.prj.mvc.myfavorite.MyFavoriteBean"%>

<%
	final String jsp = OrgUtils.deployPath + "jsp/account_layout_item.jsp";
	for (Object o : PagerUtils.getPagerList(request)) {
		request.setAttribute("__account_", OrgUtils.am().queryForObjectById(((MyFavoriteBean) o).getUserId()));
%>
<jsp:include page="<%=jsp%>"></jsp:include>
<%
	}
%>
