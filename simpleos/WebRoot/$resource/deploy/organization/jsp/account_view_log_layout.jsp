<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.util.ConvertUtils"%>
<%@ page import="net.simpleframework.core.ado.IDataObjectQuery"%> 
<%@ page import="net.simpleframework.organization.account.IGetAccountAware"%>
<%@ page import="net.simpleframework.util.DateUtils"%>
<%@ page import="net.simpleframework.web.page.PageRequestResponse"%>
<%@ page import="net.simpleframework.content.ContentUtils"%>
<%@ page import="net.simpleframework.organization.account.IAccount"%>
<%@ page import="net.simpleframework.organization.OrgUtils"%>
<%@ page import="net.simpleframework.organization.account.AccountViewLog"%>
<%
	final IDataObjectQuery<?> qs = (IDataObjectQuery<?>) request
			.getAttribute("__qs");
	if (qs == null) {
		return;
	}
	final int rows = ConvertUtils
			.toInt(request.getParameter("rows"), 8);
	if (rows > 0) {
		qs.setCount(rows);
	}
%>
<div class="account_layout" style="text-align: center;">
	<%
		final int width = ConvertUtils.toInt(request.getParameter("width"),
				45);
		final int height = ConvertUtils.toInt(
				request.getParameter("height"), 45);
		final PageRequestResponse requestResponse = new PageRequestResponse(
				request, response);
		IGetAccountAware getAccount = ContentUtils.getAccountAware();
		AccountViewLog log;
		while ((log = (AccountViewLog) qs.next()) != null) {
			IAccount account = OrgUtils.am()
					.queryForObjectById(log.getViewId());
			if (account == null) {
				continue;
			}
	%>
	<div class="li"><%=getAccount.wrapImgAccountHref(requestResponse,
						account, width, height)%>
		<div style="line-height: 20px; height: 20px; width: <%=width + 5%>px; overflow: hidden;"><%=getAccount.wrapAccountHref(requestResponse, account)%></div>
		<div style="line-height: 20px; color: #aaa;">
			<%=DateUtils.getRelativeDate(log.getLastUpdate())%>
		</div>
	</div>
	<%
		}
	%>
</div>