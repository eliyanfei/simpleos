<%@page import="net.simpleframework.my.friends.ERequestStatus"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="net.simpleframework.web.page.component.ui.pager.PagerUtils"%>
<%@ page import="java.util.List"%>
<%@ page import="net.simpleframework.my.friends.FriendsRequest"%>
<%@ page import="net.simpleframework.organization.OrgUtils"%>
<%@ page import="net.simpleframework.organization.IUser"%>
<%@ page import="net.simpleframework.util.StringUtils"%>
<%@ page import="net.simpleframework.my.friends.FriendsUtils"%>
<%@ page import="net.simpleframework.util.DateUtils"%>
<%
	final List<?> data = PagerUtils.getPagerList(request);
	if (data == null && data.size() == 0) {
		return;
	}
	final boolean myRequest = FriendsUtils.isMyFriendsRequest(request);
	if (!myRequest) {
%>
<div class="f2"
	style="border-bottom: 3px double #aaa; padding-bottom: 4px; margin-bottom: 10px;">#(my_friends_request_pager.0)</div>
<%
	}
	for (int i = 0; i < data.size(); i++) {
		final FriendsRequest fr = (FriendsRequest) data.get(i);
		final String messageText = fr.getMessageText();
%>
<table style="display: inline-block; vertical-align: top; width: 350px;"
	cellpadding="4">
	<tr>
		<%
			if (!myRequest) {
					final IUser send = OrgUtils.um().queryForObjectById(
							fr.getSentId());
					if (send == null) {
						continue;
					}
		%>
		<td width="70"><img class="photo_icon"
			style="width: 64px; height: 64px;"
			src="<%=OrgUtils.getPhotoSRC(request, send, 64, 64)%>"></td>
		<td>
		<td>
			<div class="f3"><%=send.getText()%>,
				<%=DateUtils.getRelativeDate(fr.getSentDate())%>
			</div>
			<div style="padding: 5px 0px;"><%=StringUtils.blank(messageText)%></div>
			<div style="padding: 2px 0px;">
				<input type="button" value="#(my_friends_request_pager.1)"
					onclick="$Actions['ajaxFriendsRequestAction']('rid=<%=fr.getId()%>&status=yes');" />
				<input type="button" value="#(my_friends_request_pager.2)"
					onclick="$Actions['ajaxFriendsRequestAction']('rid=<%=fr.getId()%>&status=no');" />
			</div>
		</td>
		<%
			} else {
					IUser to = OrgUtils.um().queryForObjectById(fr.getToId());
					if (to == null) {
						continue;
					}
		%>
		<td width="70"><img class="photo_icon"
			style="width: 64px; height: 64px;"
			src="<%=OrgUtils.getPhotoSRC(request, to, 64, 64)%>"></td>
		<td>
			<div class="f3">
				<span
					class="<%=(fr.getRequestStatus() == ERequestStatus.no || fr
							.getRequestStatus() == ERequestStatus.request) ? "rred"
							: ""%>"><%=fr.getRequestStatus()%></span>
				(<%=to.getText()%>,
				<%=DateUtils.getRelativeDate(fr.getSentDate())%>)
			</div>
			<div style="padding: 5px 0px;"><%=StringUtils.blank(messageText)%></div>
			<div style="padding: 2px 0px;">
				<input type="button" value="#(Delete)"
					onclick="$Actions['ajaxDeleteFriendsRequest']('rid=<%=fr.getId()%>');" />
			</div>
		</td>
		<%
			}
		%>
	</tr>
</table>
<%
	}
%>
