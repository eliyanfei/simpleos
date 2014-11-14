<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.organization.account.IAccount"%>
<%@ page import="net.simpleframework.util.ConvertUtils"%>
<%@ page import="net.simpleframework.util.IPAndCity"%>
<%@ page import="net.simpleframework.organization.OrgUtils"%>
<%@ page import="net.simpleframework.util.DateUtils"%>
<%@ page import="net.simpleframework.organization.IUser"%>
<%@ page import="net.simpleframework.organization.account.Exp"%>
<%@ page
	import="net.simpleframework.organization.component.userpager.UserPagerUtils"%>
<%@ page
	import="net.simpleframework.organization.account.AccountContext"%>
<%@ page
	import="net.simpleframework.organization.account.AccountSession"%>
<%
	final String idParameterName = OrgUtils.um()
			.getUserIdParameterName();
	final IAccount account = OrgUtils.am().queryForObjectById(
			request.getParameter(idParameterName));
	if (account == null) {
		return;
	}
	final IUser user = account.user();
	if (user == null) {
		return;
	}
	final IAccount login = AccountSession.getLogin(session);
	final boolean me = login != null
			&& login.getId().equals(account.getId());
%>
<div class="ua_stat">
	<table style="width: 100%;" class="tbl" cellspacing="1">
		<tr>
			<td class="l">#(user_account_stat.0)</td>
			<td class="v"><%=ConvertUtils.toDateString(account.getCreateDate())%></td>
		</tr>
		<tr>
			<td class="l">#(user_account_stat.1)</td>
			<td class="v"><%=account.getStatus()%></td>
		</tr>
		<tr>
			<td class="l">#(user_account_stat.2)</td>
			<td class="v"><%=ConvertUtils.toDateString(account.getLastLoginDate())%></td>
		</tr>
		<tr>
			<td class="l">#(user_account_stat.3)</td>
			<td class="v">
				<div style="margin-bottom: 2px;"><%=account.getLastLoginIP()%></div>
				<div style="color: #777;"><%=IPAndCity.getCity(account.getLastLoginIP(), true)%></div>
			</td>
		</tr>
		<tr>
			<td class="l">#(user_account_stat.4)</td>
			<td class="v"><%=account.getLoginTimes()%></td>
		</tr>
		<tr>
			<td class="l">#(user_account_stat.5)</td>
			<td class="v"><%=DateUtils.getDifferenceDate(account.getOnlineMillis())%></td>
		</tr>
		<tr>
			<td class="l">#(user_account_stat.6)</td>
			<td class="v">
				<table style="width: 100%;">
					<tr>
						<td>
							<%
								if (account.isMailbinding()) {
									out.write(user.getEmail());
								}
							%>
						</td>
						<td align="right">
							<%
								if (me) {
									out.write("<input type=\"button\" value=\"#(user_account_stat.12)\" onclick=\"$Actions['userAccountBindingWindow']('"
											+ idParameterName + "=" + account.getId() + "');\" />");
								}
							%>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td class="l">#(user_account_stat.7)</td>
			<td class="v"></td>
		</tr>
		<%
			if (AccountContext.isRuleEnable()) {
		%>
		<tr>
			<td class="l">#(user_account_stat.11)</td>
			<td class="v">
				<table style="width: 100%;">
					<tr>
						<td>
							<%
								if (me) {
										out.write("<a onclick=\"$Actions['userAccountLogWindow']();\">");
									}
									out.write(String.valueOf(account.getExp()));
									if (me) {
										out.write("</a>");
									}
									final Exp exp = AccountContext.getExp(account.getExp());
									if (exp != null) {
										out.write(UserPagerUtils.getExpIcon(exp));
									}
							%>
						</td>
						<td align="right"><input type="button"
							value="#(user_account_stat.13)"
							onclick="$Actions['userAccountRuleWindow']();" /></td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td class="l">#(user_account_stat.8)</td>
			<td class="v">
				<%
					if (me) {
							out.write("<a onclick=\"$Actions['userAccountLogWindow']();\">");
						}
						out.write(String.valueOf(account.getPoints()));
						if (me) {
							out.write("</a>");
						}
				%>
			</td>
		</tr>
		<tr>
			<td class="l">#(user_account_stat.9)</td>
			<td class="v"><%=account.getReputation()%></td>
		</tr>
		<tr>
			<td class="l">#(user_account_stat.10)</td>
			<td class="v"><%=account.getMoney()%></td>
		</tr>
		<%
			}
		%>
	</table>
</div>
<style type="text/css">
.ua_stat .tbl {
	background: #ddd;
}

.ua_stat .tbl .l {
	background: #f7f7ff;
	width: 125px;
	text-align: right;
	padding: 4px;
}

.ua_stat .tbl .v {
	background: #fff;
	padding: 4px;
}
</style>