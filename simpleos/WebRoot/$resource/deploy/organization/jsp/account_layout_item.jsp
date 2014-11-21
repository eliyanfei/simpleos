<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.web.page.PageRequestResponse"%>
<%@ page import="net.simpleframework.util.DateUtils"%>
<%@ page import="net.simpleframework.content.ContentUtils"%>
<%@ page import="net.simpleframework.organization.account.IAccount"%>
<%@ page import="net.simpleframework.util.ConvertUtils"%><%@page
	import="net.simpleframework.organization.account.Account"%><%@page
	import="java.util.Date"%>
<%
	final PageRequestResponse requestResponse = new PageRequestResponse(request, response);
	IAccount account = (IAccount) requestResponse.getRequestAttribute("__account_");
	if (account == null) {
		account = new Account();
		account.setCreateDate(new Date());
		account.setLastLoginDate(new Date());
		//return;
	}
	final int size = ConvertUtils.toInt(request.getParameter("size"), 64);
	final int width = size + 6;
	final String __showDate = request.getParameter("__showDate");
	String dateValue = __showDate;
	if (__showDate == null) {
		final boolean showCreateDate = ConvertUtils.toBoolean(request.getParameter("showCreateDate"), false);
		dateValue = DateUtils.getRelativeDate(showCreateDate ? account.getCreateDate() : account.getLastLoginDate());
	}
%>
<div class="li"><%=ContentUtils.getAccountAware().wrapImgAccountHref(requestResponse, account, size, size)%>
	<div
		style="line-height: 20px; height: 20px; width: <%=width%>px; overflow: hidden;"><%=ContentUtils.getAccountAware().wrapAccountHref(requestResponse, account)%></div>
	<div style="line-height: 20px; color: #aaa;">
		<%=dateValue%>
	</div>
</div>