<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.util.StringUtils"%>
<%@ page import="net.simpleframework.organization.OrgUtils"%>

<form id="_userbase_form"><input type="hidden" id="<%=OrgUtils.um().getUserIdParameterName()%>"
	name="<%=OrgUtils.um().getUserIdParameterName()%>"
	value="<%=StringUtils.blank(request
							.getParameter(OrgUtils.um().getUserIdParameterName()))%>" />
<table style="width: 100%;" cellpadding="2" cellspacing="0">
	<tr>
		<td>#(user_edit_base.0)</td>
		<td align="right"><input type="button" class="button2" id="_userbase_save"
			onclick="$Actions['ajaxEditUserBase']();" value="#(user_edit_base.1)" /> <input type="button"
			value="#(Button.Cancel)" onclick="$Actions['editUserWindow'].close();" /></td>
	</tr>
	<tr>
		<td valign="top" width="50%">
		<div id="_userbase_0"></div>
		</td>
		<td valign="top" width="50%">
		<div id="_userbase_1"></div>
		</td>
	</tr>
</table>
</form>