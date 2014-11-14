<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.organization.OrgUtils"%>
<%@ page import="net.simpleframework.organization.account.Account"%>
<%@ page import="net.simpleframework.util.StringUtils"%>
<%
	final String idParameterName = OrgUtils.um()
			.getUserIdParameterName();
	final String userId = StringUtils.blank(request
			.getParameter(idParameterName));
	final Account account = (Account) OrgUtils.am().queryForObjectById(
			userId);
%>
<div class="ua_binding">
  <table style="width: 100%;" cellpadding="2">
    <tr>
      <td>#(user_account_binding.4)</td>
    </tr>
    <tr>
      <td id="__mail_binding_btn"></td>
    </tr>
    <tr>
      <td><input type="button" value="#(user_account_binding.3)" /></td>
    </tr>
  </table>
</div>
<script type="text/javascript">
  function __user_account_mail_callback(binding) {
    var btn = new Element("INPUT", {
      type: "button"  
    });
    if (binding) {
      btn.setAttribute("value", "#(user_account_binding.2)");
      btn.setAttribute("onclick", 
          "$Actions['ajaxUnBindingEmail']('<%=idParameterName%>=<%=userId%>');");
    } else {
      btn.setAttribute("value", "#(user_account_binding.0)");
      btn.setAttribute("onclick", 
          "$Actions['windowBindingEmail']('<%=idParameterName%>=<%=userId%>');");
    }
    $("__mail_binding_btn").update(btn);
  }
  __user_account_mail_callback(<%=account.isMailbinding()%>);
</script>
