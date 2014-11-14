<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.web.page.component.ui.pager.PagerUtils"%>
<%@ page import="java.util.List"%>
<%@ page import="net.simpleframework.organization.IUser"%>
<%@ page import="net.simpleframework.organization.OrgUtils"%>
<%@ page import="net.simpleframework.organization.account.IAccount"%>
<%@ page import="net.simpleframework.util.DateUtils"%>
<%
	final List<?> data = PagerUtils.getPagerList(request);
	if (data == null && data.size() == 0) {
		return;
	}
	for (Object o : data) {
		final IUser user = (IUser) o;
		IAccount account = user.account();
		if (account == null) {
			continue;
		}
%>
<table style="display: inline-block; vertical-align: top; width: 320px;" cellpadding="4">
	<tr>
		<td width="70" valign="top"><img class="photo_icon" style="width: 64px; height: 64px;"
			src="<%=OrgUtils.getPhotoSRC(request, user, 64, 64)%>"></td>
		<td valign="top">
			<div class="f3">
				<span><%=user.getText()%>, <%=DateUtils.getRelativeDate(account.getLastLoginDate())%></span>
			</div>
			<div style="padding-top: 8px;">
				<a class="a2"
					onclick="$Actions['addMyFriendWindow']('<%=OrgUtils.um().getUserIdParameterName()%>=<%=user.getId()%>');">#(friends_utils.0)</a>
			</div>
		</td>
	</tr>
</table>
<%
	}
%>