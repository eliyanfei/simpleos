<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.web.page.PageRequestResponse"%>
<%@ page import="net.simpleframework.applets.openid.OpenIDUtils"%>
<%@ page import="net.simpleframework.applets.openid.OpenIDBean"%>
<%@ page import="net.simpleframework.util.StringUtils"%>
<%
	final PageRequestResponse requestResponse = new PageRequestResponse(
			request, response);
	final OpenIDBean openIDBean = OpenIDUtils.applicationModule
			.getOpenIDBean(requestResponse);
	if (openIDBean == null) {
%>
<div class="f2">#(openid_return.0)</div>
<%
	} else {
		final String url = OpenIDUtils.applicationModule.login(
				requestResponse, openIDBean);
		if (StringUtils.hasText(url)) {
%>
<script type="text/javascript">$Actions.loc("<%=url%>");</script>
<%
			return;
		}
%>
<div class="openid_return">
<div class="f2">#(openid_return.1)</div>
<div class="attri"><input type="hidden" id="oi_email" name="oi_email"
	value="<%=openIDBean.getEmail()%>" />
<table cellpadding="4">
	<tr>
		<td class="l">#(openid_return.2)</td>
		<td><input class="txt" id="oi_text" name="oi_text" type="text"
			value="<%=openIDBean.getText()%>" /></td>
	</tr>
	<tr>
		<td class="l">#(openid_return.3)</td>
		<td><input class="txt" id="oi_name" name="oi_name" type="text"
			value="<%=openIDBean.getEmail()%>" /></td>
	</tr>
	<tr>
		<td class="l">#(openid_return.4)</td>
		<td><input class="txt" id="oi_password" name="oi_password" type="password" /></td>
	</tr>
	<tr>
		<td class="l"></td>
		<td><input type="button" id="oi_submit" class="button2" value="#(Button.Ok)"
			onclick="$Actions['ajaxOpenidAttriSave']();" /></td>
	</tr>
</table>
</div>
</div>
<style type="text/css">
.openid_return .attri {
	margin-top: 10px;
	border: 1px solid #ccc;
	padding: 10px;
	background-color: #f9ffff;
	-moz-border-radius: 6px;
	-webkit-border-radius: 6px;
	border-radius: 6px;
}

.openid_return .txt {
	width: 200px;
}

.openid_return .l {
	text-align: right;
}
</style>
<%
	}
%>
