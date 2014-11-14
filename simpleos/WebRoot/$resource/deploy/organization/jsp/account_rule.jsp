<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="net.simpleframework.organization.account.AccountContext"%>
<%@ page import="net.simpleframework.organization.account.IAccountRule"%>
<%@ page
	import="net.simpleframework.organization.account.AccountSession"%>
<%@ page import="net.simpleframework.organization.account.IAccount"%>
<%@ page import="net.simpleframework.util.StringUtils"%>
<div class="account_rule">
	<table style="width: 100%;" cellspacing="1" class="tbl">
		<thead>
			<tr>
				<th width="75px;">#(account_rule.0)</th>
				<th width="75px;">#(account_rule.1)</th>
				<th width="30px;">#(account_rule.2)</th>
				<th width="30px;">#(account_rule.3)</th>
				<th>#(account_rule.4)</th>
			</tr>
		</thead>
		<tbody>
			<%
				final IAccount account = AccountSession.getLogin(session);
				int i = 0;
				for (IAccountRule aRule : AccountContext.getAccountContext()
						.getAccountRuleMap().values()) {
			%>
			<tr>
				<td style="color: #36706C;"><%=aRule.getText()%></td>
				<td><%=StringUtils.blank(aRule.getModule())%></td>
				<td><%=aRule.getExp(account)%></td>
				<td><%=aRule.getPoints(account)%></td>
				<td style="text-align: left;"><%=StringUtils.text(aRule.getDescription(), "&nbsp;")%></td>
			</tr>
			<%
				}
			%>
		</tbody>
	</table>
</div>
<style type="text/css">
.account_rule {
	padding: 6px;
}

.account_rule .tbl {
	background: #ddd;
}

.account_rule .tbl th {
	background: #f7f7ff;
	padding: 8px 4px;
}

.account_rule .tbl td {
	background: #fff;
	padding: 3px;
	text-align: center;
}
</style>