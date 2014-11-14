<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.util.ConvertUtils"%>
<%@ page import="net.simpleframework.core.ado.IDataObjectQuery"%>
<%@ page import="net.simpleframework.organization.account.IAccount"%>
<%
	final IDataObjectQuery<?> qs = (IDataObjectQuery<?>) request.getAttribute("__qs");
	if (qs == null) {
		return;
	}
	final int rows = ConvertUtils.toInt(request.getParameter("rows"), 6);
	if (rows > 0) {
		qs.setCount(rows);
	}
	final boolean showCreateDate = ConvertUtils.toBoolean(request.getParameter("showCreateDate"), false);
%>
<div class="account_layout" style="text-align: center;">
	<%
		IAccount account;
		while ((account = (IAccount) qs.next()) != null) {
			request.setAttribute("__account_", account);
	%>
	<jsp:include page="account_layout_item.jsp"><jsp:param
			value="<%=showCreateDate %>" name="showCreateDate" /></jsp:include>
	<%
		}
	%>
</div>
