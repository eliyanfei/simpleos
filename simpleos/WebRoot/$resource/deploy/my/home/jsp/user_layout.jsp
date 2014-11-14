<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.util.StringUtils"%>
<%@ page import="net.simpleframework.util.ConvertUtils"%>
<%@ page import="net.simpleframework.organization.account.Exp"%>
<%@ page import="net.simpleframework.organization.component.userpager.UserPagerUtils"%>
<%@ page import="net.simpleframework.organization.account.AccountContext"%>
<%@ page import="net.simpleframework.util.DateUtils"%>
<%@ page import="net.simpleframework.organization.IUser"%>
<%@ page import="net.simpleframework.web.page.PageRequestResponse"%>
<%@ page import="net.simpleframework.my.home.MyHomeUtils"%>
<%@ page import="net.simpleframework.organization.account.IAccount"%>
<%@ page import="net.simpleframework.organization.OrgUtils"%>
<%@ page import="net.simpleframework.my.home.HomeTabsBean"%>
<%@ page import="net.simpleframework.my.space.MySpaceUtils"%>
<%
	final PageRequestResponse requestResponse = new PageRequestResponse(
	request, response);
	final IAccount account = MySpaceUtils.getAccountAware()
	.getAccount(requestResponse);
	if (account == null) {
		return;
	}
	final IUser user = account.user();
	if (user == null) {
		return;
	}
	final boolean me = MySpaceUtils.getAccountAware().isMyAccount(requestResponse);
	final int width = ConvertUtils.toInt(request.getParameter("width"),
	128);
	final int height = ConvertUtils.toInt(
	request.getParameter("height"), 128);
	final HomeTabsBean homeTabs = MyHomeUtils
	.getFirstHomeTab(requestResponse);
%>
<div style="display: inline-block; text-align: left;">
	<img class="photo_icon"
		style="float: left; margin: 0px 12px 8px 0px; width:<%=width%>px; height:<%=height%>px;"
		src="<%=OrgUtils.getPhotoSRC(request, account.getId(), width,
					height)%>">
	<table cellpadding="1" cellspacing="0" style="float: left;">
		<tr>
			<td class="f2"><%=user.getText()%></td>
		</tr>
		<tr>
			<td><%=user.getName()%> , <%=user.getSex()%></td>
		</tr>
		<tr>
			<td>#(user_layout.0) : <%=DateUtils.getRelativeDate(account.getLastLoginDate())%></td>
		</tr>
		<tr>
			<td>#(user_layout.2) : <%=homeTabs != null ? homeTabs.getViews() : 0%></td>
		</tr>
		<tr>
			<td>#(user_account_stat.11) : <%
				if (me) {
					out.write("<a class=\"a2\" onclick=\"$Actions['userAccountLogWindow']();\">");
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
		</tr>
		<tr>
			<td>#(user_account_stat.8) : <%
				if (me) {
					out.write("<a class=\"a2\" onclick=\"$Actions['userAccountLogWindow']();\">");
				}
				out.write(String.valueOf(account.getPoints()));
				if (me) {
					out.write("</a>");
				}
			%>
			</td>
		</tr>
	</table>
	<table cellpadding="1" cellspacing="0" style="clear: both;">
		<tr>
			<td align="right" style="width: 75px; padding-right: 8px;">#(user_layout.1) :</td>
			<td><%=ConvertUtils.toDateString(account.getCreateDate())%></td>
		</tr>
		<tr>
			<td align="right" style="padding-right: 8px;">#(user_edit_base.8) :</td>
			<td><%=StringUtils.blank(user.getHometown())%></td>
		</tr>
		<tr>
			<td align="right" style="padding-right: 8px;">#(user_edit_base.19) :</td>
			<td><%=StringUtils.blank(user.getDescription())%></td>
		</tr>
	</table>
	<%
		if (me) {
	%>
	<div style="text-align: right; margin-top: 6px;">
		<a
			onclick="$Actions['editUserWindow']('<%=OrgUtils.um().getUserIdParameterName()%>=<%=user.getId()%>');">#(user_layout.3)</a>
	</div>
	<%
		}
	%>
</div>
