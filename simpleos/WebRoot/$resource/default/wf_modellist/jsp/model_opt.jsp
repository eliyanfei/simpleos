<%@page import="net.simpleframework.workflow.EProcessModelStatus"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>

<div class="mo_edit simple_toolbar">
  <input type="hidden" name="modelId"
    value="<%=request.getParameter("modelId")%>" />
  <table style="width: 100%;" cellpadding="2">
    <tr>
      <td class="l">#(model_opt.0)</td>
      <td><select name="model_status" id="model_status">
          <%
          	for (EProcessModelStatus status : EProcessModelStatus.values()) {
          %>
          <option value="<%=status.ordinal()%>"><%=status%></option>
          <%
          	}
          %>
      </select></td>
    </tr>
  </table>
</div>
<div style="text-align: right; margin-top: 6px;">
  <input type="button" class="button2" value="#(Button.Ok)"
    onclick="$Actions['model_opt_save']();" /> <input type="button"
    value="#(Button.Cancel)"
    onclick="$Actions['ml_opt_window'].close();" />
</div>
<style type="text/css">
.mo_edit .l {
	width: 70px;
	text-align: right;
}
</style>