<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.organization.OrgUtils"%>
<%@ page import="net.simpleframework.ado.db.ExpressionValue"%>
<%@ page
	import="net.simpleframework.organization.account.EAccountStatus"%>
<%
	request.setAttribute(
			"__qs",
			OrgUtils.am().query(
					new ExpressionValue(
							"status=? order by createdate desc",
							new Object[] { EAccountStatus.normal })));
	final String layout = OrgUtils.deployPath
			+ "jsp/account_layout.jsp";
%>
<jsp:include page="<%=layout%>" flush="true">
	<jsp:param value="true" name="showCreateDate" />
</jsp:include>