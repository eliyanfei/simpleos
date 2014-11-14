<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.web.page.PageRequestResponse"%>
<%@ page import="net.simpleframework.my.space.MySpaceUtils"%>
<%@ page
	import="net.simpleframework.organization.account.AccountViewLogUtils"%>
<%@ page import="net.simpleframework.organization.OrgUtils"%>
<%@ page import="net.simpleframework.web.EFunctionModule"%><%@page
	import="net.simpleframework.ado.db.IQueryEntitySet"%><%@page
	import="net.simpleframework.organization.account.AccountViewLog"%>


<%
	final PageRequestResponse requestResponse = new PageRequestResponse(request, response);
	MySpaceUtils.updateViews(requestResponse);
%>
<jsp:include page="space_user_layout.jsp"></jsp:include>
<%
	AccountViewLogUtils.updateLog(requestResponse, EFunctionModule.space_log);
	IQueryEntitySet<AccountViewLog> qs = AccountViewLogUtils.queryLogs(requestResponse, EFunctionModule.space_log);
	request.setAttribute("__qs", qs);
	String view_log_layout = OrgUtils.deployPath + "jsp/account_view_log_layout.jsp";
%>
<%
	if (qs != null && qs.getCount() != 0) {
%>
<div class="space_account_log_layout">
	#(my_space.0)
</div>
<%
	}
%>
<jsp:include page="<%=view_log_layout%>" flush="true">
	<jsp:param value="52" name="width" />
	<jsp:param value="52" name="height" />
</jsp:include>
