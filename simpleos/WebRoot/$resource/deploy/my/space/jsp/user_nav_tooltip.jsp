<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.web.page.PageRequestResponse"%>
<%@ page import="net.simpleframework.organization.IUser"%>
<%@ page import="net.simpleframework.organization.OrgUtils"%>
<%@ page import="net.simpleframework.util.DateUtils"%>
<%@ page import="net.simpleframework.organization.account.IAccount"%>
<%@ page import="net.simpleframework.util.StringUtils"%>
<%@ page import="net.simpleframework.my.space.MySpaceUtils"%>
<%@ page import="net.simpleframework.content.blog.BlogUtils"%>
<%@ page
	import="net.simpleframework.content.component.topicpager.ETopicQuery"%>
<%@ page import="net.simpleframework.content.bbs.BbsUtils"%><%@page
	import="net.simpleframework.organization.account.IGetAccountAware"%>

<%
	final PageRequestResponse rr = new PageRequestResponse(request, response);
	final IUser user = OrgUtils.um().queryForObjectById(request.getParameter("userId"));
	if (user == null) {
		return;
	}
	final IAccount account = user.account();
	final String uParam = OrgUtils.um().getUserIdParameterName() + "=" + user.getId();
	final String text = MySpaceUtils.getSexText(rr, user.getSex());
%>
<table style="width: 100%;" cellpadding="3" cellspacing="0"
	class="space_navigation">
	<tr>
		<td valign="top" width="70">
			<img class="photo_icon" style="width: 64px; height: 64px;"
				src="<%=OrgUtils.getPhotoSRC(request, user.getId(), 64, 64)%>">
			<div style="margin-top: 2px; text-align: center;"
				id="div_user_attention">
				<%=MySpaceUtils.buildUserAttention(rr, user.getId())%>
			</div>
		</td>
		<td valign="top">
			<div style="padding: 2px 0px;"><%=DateUtils.getRelativeDate(account.getLastLoginDate())%></div>
			<div style="padding: 2px 0px;"><%=StringUtils.blank(user.getDescription())%></div>
			<table cellpadding="0" cellspacing="0">
				<tr>
					<td valign="top" width="48%"><%=MySpaceUtils.buildSpaceLink(rr, "portal.png", MySpaceUtils.applicationModule.getSpaceUrl(rr, user), text + "的空间")%><%=MySpaceUtils.buildSpaceLink(rr, "friends.png", "javascript:$Actions['addMyFriendWindow']('" + uParam + "');", "加为好友")%></td>
					<td width="2%"></td>
					<td valign="top" width="48%"><%=MySpaceUtils.buildSpaceLink(rr, "message.png", "javascript:$Actions['myMessageSentWindow']('" + uParam + "');", "发送消息")%><%=MySpaceUtils.buildSpaceLink(rr, "dialog.png", "javascript:$Actions['myDialogWindow']('" + uParam + "');", "发起对话")%></td>
				</tr>
			</table>
		</td>
	</tr>
</table>
