<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.organization.OrgUtils"%>
<%@ page import="net.simpleframework.organization.account.IAccount"%>
<%
	final String accountId = request.getParameter("accountId");
	final IAccount account = OrgUtils.am().queryForObjectById(accountId);
%>
<form id="editmailForm">
<div id="editmailEditor"></div>
<input type="hidden" id="accountId" name="accountId" value="<%=accountId%>" /></form>
<div style="text-align: right; padding-top: 4px;"><input id="editmailSave" type="button"
	value="#(Button.Ok)" class="button2" onclick="$Actions['ajaxEditmail']();" /> <input type="button"
	value="#(Button.Cancel)" onclick="$Actions['editmailWindow'].close();" /></div>

<script type="text/javascript">
	function _load_account() {
		$('em_accountName').value = "<%=account.user().getName()%>";
		$('em_mail').value = "<%=account.user().getEmail()%>";
	}
</script>