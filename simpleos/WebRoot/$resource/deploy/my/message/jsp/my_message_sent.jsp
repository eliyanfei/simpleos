<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.web.page.PageRequestResponse"%>
<%@ page import="net.simpleframework.my.friends.FriendsUtils"%>
<%@ page import="net.simpleframework.organization.OrgUtils"%>
<%@ page import="net.simpleframework.organization.IUser"%>
<%@ page
	import="net.simpleframework.organization.account.AccountSession"%>
<%@ page import="net.simpleframework.organization.account.IAccount"%>
<%
	final PageRequestResponse requestResponse = new PageRequestResponse(request, response);
	final String userIdParameterName = OrgUtils.um().getUserIdParameterName();
	final IUser user = OrgUtils.um().queryForObjectById(request.getParameter(userIdParameterName));
	if (user != null) {
		final IAccount login = AccountSession.getLogin(session);
		if (login != null && login.getId().equals2(user.getId())) {
%><div class="f2" style="text-align: center;">
	不能给自己发消息
</div>
<%
	return;
		}
	}
%>
<div id="my_message_sent_form">
	<div class="simple_toolbar2">
		<table style="width: 100%;" cellpadding="2" cellspacing="2">
			<tr>
				<td width="60" align="right">
					#(my_message_sent.0)
				</td>
				<td id="textButtonMessageUsers"></td>
			</tr>
			<tr>
				<td align="right">
					#(my_message_sent.5)
				</td>
				<td>
					<textarea style="width: 98%; padding: 4px; font-size: 13px;"
						id="textareaMessageEditor" name="textareaMessageEditor" rows="10"></textarea>
					<div style="margin-top: 2px;" class="important-tip">
						#(my_message_sent.6)
					</div>
				</td>
			</tr>
		</table>
	</div>
	<div style="text-align: right; padding-top: 4px;">
		<input type="button" class="button2" id="buttonMessageSent"
			onclick="$Actions['ajaxMessageSentOK']();"
			value="#(my_message_sent.1)" />
		<input type="button" value="#(Button.Cancel)"
			onclick="$Actions['myMessageSentWindow'].close();" />
	</div>
</div>
<script type="text/javascript">
(function() {
		var tb = $Comp.textButton("textMessageUsers", function(ev) {
			$Actions["messageUsersSelect"]();
		});
		tb.textObject.setAttribute("readonly", "readonly");
<%if (user != null) {%>
		tb.textObject.value = "<%=user.getName()%>";
<%}%>
	$("textButtonMessageUsers").insert(tb);
	})();
</script>
