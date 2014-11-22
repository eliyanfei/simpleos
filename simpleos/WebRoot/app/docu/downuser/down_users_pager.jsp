<%@page import="net.simpleframework.util.DateUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="net.simpleframework.web.page.component.ui.pager.PagerUtils"%>
<%@ page import="net.simpleframework.organization.OrgUtils"%>
<%@page import="net.simpleos.module.docu.DocuLogBean"%><%@page
	import="net.simpleos.SimpleosUtil"%>

<%
	final String jsp = OrgUtils.deployPath + "jsp/account_layout_item.jsp";
	for (Object o : PagerUtils.getPagerList(request)) {
		DocuLogBean docuLogBean = ((DocuLogBean) o);
		request.setAttribute("__account_", SimpleosUtil.getAccountById(docuLogBean.getUserId()));
%>
<jsp:include page="<%=jsp%>"><jsp:param
		value="<%=DateUtils.getRelativeDate(docuLogBean.getDownDate())%>"
		name="__showDate" /></jsp:include>
<%
	}
%>
