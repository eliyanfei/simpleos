<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.util.HTMLBuilder"%>
<%@ page import="net.simpleframework.organization.account.IAccountRule"%>
<%@ page import="net.simpleframework.organization.account.AccountContext"%>
<div class="user_account_log">
<div class="tb"><select style="margin-left: 6px;"
	onchange="$Actions['user_account_log_table']('eventId=' + $F(this));">
	<option value="">#(user_account_log.2)</option>
	<%
		for (IAccountRule aRule : AccountContext.getAccountContext()
				.getAccountRuleMap().values()) {
	%>
	<option value="<%=aRule.getEventId()%>"><%=aRule.getText()%></option>
	<%
		}
	%>
</select><a style="margin-left: 20px;" onclick="$Actions['user_account_log_table']('time=day');">#(user_account_log.3)</a><%=HTMLBuilder.SEP%><a
	onclick="$Actions['user_account_log_table']('time=week');">#(user_account_log.4)</a></div>
<div id="user_account_log_table"></div>
</div>