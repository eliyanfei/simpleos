<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.util.StringUtils"%>
<%@ page import="net.simpleframework.organization.OrgUtils"%>

<form id="_userpwd_form"><input type="hidden" id="<%=OrgUtils.um().getUserIdParameterName()%>"
	name="<%=OrgUtils.um().getUserIdParameterName()%>"
	value="<%=StringUtils.blank(request
					.getParameter(OrgUtils.um().getUserIdParameterName()))%>" />
<table style="width: 100%;" cellpadding="2" cellspacing="0">
	<tr>
		<td>#(user_edit_pwd.0)</td>
		<td align="right"><input type="button" class="button2" id="_userpwd_save"
			onclick="$Actions['ajaxEditPassword']();" value="#(user_edit_pwd.2)" /> <input type="button"
			value="#(Button.Cancel)" onclick="$Actions['editUserWindow'].close();" /></td>
	</tr>
	<tr>
		<td colspan="2">
		<div id="_userpwd"></div>
		</td>
	</tr>
	<tr>
		<td colspan="2">
		<input style="vertical-align: middle;" type="checkbox" id="user_SendMail" 
			name="user_SendMail" value="true" checked />
		<label style="vertical-align: middle;" for="user_SendMail">#(user_edit_pwd.1)</label>
		</td>
	</tr>
</table>
</form>


