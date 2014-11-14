<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.organization.OrgUtils"%>
<%@ page import="net.simpleframework.util.StringUtils"%>
<%
	final String userIdParameter = OrgUtils.um()
			.getUserIdParameterName();
%>
<div id="__sent_mail_form"><input type="hidden" name="<%=userIdParameter%>"
	value="<%=StringUtils.blank(request.getParameter(userIdParameter))%>" />
<table style="width: 100%;" cellpadding="2" cellspacing="0" class="title">
	<tr>
		<td class="lbl">#(User.sent_mail.0)</td>
		<td><input id="sentMailTopic" name="sentMailTopic" type="text" /></td>
	</tr>
</table>
<textarea id="textareaSentMailHtmlEditor" name="textareaSentMailHtmlEditor" style="display: none;"></textarea>
<div class="bb"><input type="button" id="buttonSentMail"
	onclick="$Actions['ajaxSentMailOK']();" value="#(User.sent_mail.1)" /> <input type="button"
	value="#(Button.Cancel)" onclick="$Actions['userSentMailWindow'].close();" /></div>
</div>