<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.organization.OrgUtils"%>
<%@ page import="net.simpleframework.util.StringUtils"%>
<%
	String userId = StringUtils.blank(request.getParameter(OrgUtils
			.um().getUserIdParameterName()));
%>
<form id="bindingMail_form">
  <input type="hidden" id="<%=OrgUtils.um().getUserIdParameterName()%>"
    name="<%=OrgUtils.um().getUserIdParameterName()%>"
    value="<%=userId%>" />
  <div class="ua_binding_mail_0">
    <table style="width: 100%;" cellpadding="2">
      <tr>
        <td>#(user_account_binding_email.0)</td>
      </tr>
      <tr>
        <td><input type="text" style="width: 98%;"
          id="textSendBindingMail" name="textSendBindingMail"
          value="<%=StringUtils.blank(OrgUtils.um()
					.queryForObjectById(userId).getEmail())%>" /></td>
      </tr>
      <tr>
        <td>#(user_account_binding_email.1)</td>
      </tr>
      <tr>
        <td>
          <div id="bindingMailValidateCode"></div>
        </td>
      </tr>
      <tr>
        <td><input type="button" id="btnSendBindingMail"
          value="#(user_account_binding_email.2)"
          onclick="$Actions['ajaxSendBindingMail']();" /></td>
      </tr>
    </table>
  </div>
  <div class="ua_binding_mail_1">
    <table style="width: 100%;" cellpadding="2">
      <tr>
        <td>#(user_account_binding_email.3)</td>
      </tr>
      <tr>
        <td><input type="text" id="textBindingMailCode"
          name="textBindingMailCode" style="width: 98%" /></td>
      </tr>
      <tr>
        <td><input type="button"
          value="#(user_account_binding_email.4)" id="btnBindingMail"
          onclick="$Actions['ajaxBindingMail']();" /></td>
      </tr>
    </table>
  </div>
</form>
