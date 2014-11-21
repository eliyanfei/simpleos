<%@page import="net.itsite.ItSiteUtil"%>
<%@page import="net.itsite.utils.StringsUtils"%>
<%@page import="java.util.Map"%>
<%@page import="net.simpleos.backend.BackendUtils"%>
<%@page import="net.simpleos.backend.site.SiteUtils"%>
<%@page import="net.itsite.impl.PrjColumns"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="net.simpleframework.organization.component.login.LoginUtils"%>
<%@ page
	import="net.simpleframework.web.page.component.ComponentParameter"%>
<%
	final ComponentParameter nComponentParameter = LoginUtils.getComponentParameter(request, response);
	final String beanId = nComponentParameter.componentBean.hashId();
%>
<form id="_loginForm">
	<input type="hidden" id="<%=LoginUtils.BEAN_ID%>"
		name="<%=LoginUtils.BEAN_ID%>" value="<%=beanId%>" />
	<input type="hidden" id="_accountType" name="_accountType" />
	<div class="lm">
		<a id="_accountMenu" hidefocus="hidefocus" class="login_icon_normal">注册帐号</a><span
			class="right_down_menu"></span>
	</div>
	<div>
		<input id="_accountName" name="_accountName" type="text" />
	</div>
	<div>
		#(login.0)
	</div>
	<div>
		<input id="_passwordName" name="_passwordName" type="password" />
	</div>
	<div><%=LoginUtils.getLoginToolbar(nComponentParameter)%></div>
	<div></div>
	<div></div>
	<div
		style="text-align: <%=nComponentParameter.getBeanProperty("actionAlign")%>">
		<%
			if("true".equals(ItSiteUtil.attrMap.get("sys.sys_testing"))){
		%>
		<span style="color: red;">admin/admin</span>
		<%} %>
		<input id="_loginBtn" class="button2" type="submit" value="#(login.3)"
			onclick="$Actions['arLogin']();" />
		<%
			if ((Boolean) nComponentParameter.getBeanProperty("showResetAction")) {
		%><input type="reset" onclick="this.up('form').reset();" />
		<%
			}
		%>
	</div>
</form>
<script type="text/javascript">
var _AccountTypeMSG = {
	"normal" : "#(login.4)",
	"email" : "#(login.5)",
	"mobile" : "#(login.6)"
};

function _changeAccountType(type) {
	if (!type) {
		type = "normal";
	}
	document.setCookie("_account_type", type, 24 * 365);
	$("_accountType").value = type;
	var m = $("_accountMenu");
	m.update(_AccountTypeMSG[type]);
	m
			.up()
			.setStyle(
					"background: url('<%=nComponentParameter.componentBean.getResourceHomePath(nComponentParameter)%>/images/"
							+ type + ".png') no-repeat 0 center;");
}

function _loginCallback(json) {
	var save_cookie = function() {
		document.setCookie("_account_name", $F("_accountName").stringToHex(),
				24 * 365);
		var _autoLogin = $("_autoLogin");
		if (_autoLogin && $F(_autoLogin) == "true") {
			document.setCookie("_account_pwd", $F("_passwordName"), 24 * 365);
		}
	};
	if (json["callback"]
			&& eval("(function(json) {" + json["callback"] + "})(json);")) {
		if (json["ok"])
			save_cookie();
		return;
	}
	if (json["password"]) {
		Validation.insertAfter("_passwordName", json["password"]);
	} else if (json["account"]) {
		Validation.insertAfter("_accountName", json["account"]);
	} else if (json["status"]) {
		var ele = $("_accountName");
		Validation.insertAfter(ele, json["status"]);
		var a = ele.insertAfter.down("a");
		$Actions["activationMenu"].bindEvent(a);
	} else {
		if (json["ok"])
			save_cookie();
		if (json["url"])
			window.location = json["url"];
	}
}

$ready(function() {
	var name = document.getCookie("_account_name");
	if (name) {
		$("_accountName").value = name.hexToString();
	}
	_changeAccountType(document.getCookie("_account_type"));
});
</script>
