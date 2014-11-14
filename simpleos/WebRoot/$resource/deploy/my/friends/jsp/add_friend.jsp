<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.organization.OrgUtils"%>
<%@ page import="net.simpleframework.organization.IUser"%>
<%@ page import="net.simpleframework.organization.account.IAccount"%>
<%@ page import="net.simpleframework.organization.account.AccountSession"%>
<%@ page import="net.simpleframework.util.LocaleI18n"%>
<%
	final String userIdParameterName = OrgUtils.um()
			.getUserIdParameterName();
	final IUser user = OrgUtils.um().queryForObjectById(
			request.getParameter(userIdParameterName));
	final IAccount login = AccountSession.getLogin(session);
	if (user.getId().equals2(login.getId())) {
%>
<div class="f2" style="text-align: center;">#(add_friend.1)</div>
<%
	return;
	}
%>
<div style="padding: 2px;" class="add_friend_request">
	<input type="hidden" id="af_groupid" name="af_groupid" /> <input type="hidden" id="af_toid"
		name="af_toid" value="<%=user.getId()%>" />
	<div style="padding: 2px 0px 6px 0px;">
		<%=LocaleI18n.getMessage("add_friend.0", user.getText())%>
	</div>
	<textarea id="af_messageText" name="af_messageText" rows="5" style="width: 99%; padding: 2px;"></textarea>
	<div style="padding: 4px 0px;">
		<input type="button" value="#(add_friend.2)" onclick="$Actions['friendsGroupDict']();" /> <label
			id="af_groupid_label" style="padding-left: 8px;"></label>
	</div>
	<div style="text-align: right; margin-top: 6px;">
		<input type="button" class="button2" value="#(Button.Ok)" onclick="$Actions['ajaxDoAddFriend']();" /> <input
			type="button" onclick="$Actions['addMyFriendWindow'].close();" value="#(Button.Cancel)" />
	</div>
</div>