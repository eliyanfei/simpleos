<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="net.simpleframework.organization.component.register.UserRegisterUtils"%>
<%@ page import="net.simpleframework.util.StringUtils"%>
<%@ page
	import="net.simpleframework.web.page.component.ComponentParameter"%>
<%
	final ComponentParameter nComponentParameter = UserRegisterUtils
			.getComponentParameter(request, response);
	final String beanId = nComponentParameter.componentBean.hashId();
	final String termsUrl = (String) nComponentParameter
			.getBeanProperty("termsUrl");
	final String closeAction = (String) nComponentParameter
			.getBeanProperty("closeAction");
%>
<form id="_registForm">
	<input type="hidden" id="<%=UserRegisterUtils.BEAN_ID%>"
		name="<%=UserRegisterUtils.BEAN_ID%>" value="<%=beanId%>" />
	<div id="_registForm_0" style="margin-bottom: 8px;"></div>
	<div id="_registForm_1" style="margin-bottom: 8px;"></div>
	<div id="_registForm_2" style="margin-bottom: 8px;"></div>
	<div style="height: 24px;">
		<div style="float: right;">
			<input id="_buttonRegistAccount" type="button" class="button2"
				value="#(regist.0)" onclick="$Actions['ajaxRegistAccount']();" />
			<%
				if (StringUtils.hasText(closeAction)) {
			%><input type="button" value="#(Button.Cancel)"
				onclick="<%=closeAction%>" />
			<%
				}
			%>
		</div>
		<%
			if (StringUtils.hasText(termsUrl)) {
		%>
		<div style="float: left;">
			<a onclick="$Actions['termsWindow']();">#(regist.12)</a>
		</div>
		<%
			}
		%>
	</div>
</form>

<script type="text/javascript">
	function _registCallback(json) {
		Validation.clearInsert("textRegistValidateCode");
	  if (json["validateCode"]) {
	    Validation.insertAfter("textRegistValidateCode", json["validateCode"]);
	  } else if (json["account"]) {
	    Validation.insertAfter("user_account", json["account"]);
	  } else {
	  	$('textRegistValidateCode').clear();
	  	if (json["jsCallback"]) 
	  		$eval(json["jsCallback"]);
	  }
	}
</script>
