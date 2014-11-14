<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.organization.OrgUtils"%>
<%@ page import="net.simpleframework.organization.account.IAccount"%>
<%@ page
	import="net.simpleframework.organization.account.EAccountStatus"%>
<%@ page import="net.simpleframework.util.StringUtils"%>
<%@ page import="net.simpleframework.util.ConvertUtils"%>
<%@ page
	import="net.simpleframework.organization.account.AccountContext"%>

<%
	final String accountId = request.getParameter("accountId");
	final IAccount account = OrgUtils.am().queryForObjectById(accountId);
	if (account != null) {
		final String code = request.getParameter("code");
		if (StringUtils.hash(
				ConvertUtils.toDateString(account.getCreateDate()))
				.equals(code)) {
			if (account.getStatus() == EAccountStatus.register) {
				account.setStatus(EAccountStatus.normal);
				OrgUtils.am().getEntityManager().update(account);
				AccountContext.update(account, "org_regist", null);
			}
		}
	}
%>
<div align="center" style="padding-top: 100px;">
	<div class="simple_toolbar3 f2" style="width: 400px; padding: 40px;">
		<%
			if (account == null) {
		%><div>#(regist_activation.0)</div>
		<%
			} else if (account.getStatus() == EAccountStatus.normal) {
		%><div>#(regist_activation.1)</div>
		<%
			}
		%>
		<div style="margin-top: 10px;">
			<input type="button" value="#(regist_activation.2)"
				onclick="$Actions.loc('/');" />
		</div>
	</div>
</div>


