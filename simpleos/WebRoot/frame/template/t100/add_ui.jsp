<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="net.simpleframework.desktop.DesktopMgr"%><%@page
	import="java.util.Map"%><%@page import="java.util.List"%><%@page
	import="net.simpleframework.desktop.DesktopMgr.DesktopBean"%><%@page
	import="net.simpleframework.desktop.DesktopUIUtils"%><%@page
	import="net.simpleframework.web.page.PageRequestResponse"%><%@page
	import="net.simpleframework.organization.account.IAccount"%><%@page
	import="net.simpleframework.organization.account.AccountSession"%><%@page
	import="net.simpleframework.util.StringUtils"%>

<%
	PageRequestResponse requestResponse = new PageRequestResponse(request, response);
	IAccount account = AccountSession.getLogin(request.getSession());
	Map<String, String> cacheMap = DesktopUIUtils.listUIName(requestResponse);
	Map<String, DesktopBean> desktopMap = DesktopMgr.getDesktopMgr().getPermissionDesktopMap(request);
%>

<style>
#uiDiv .desktop_icon {
	padding: 0px;
	background-color: #eee;
	-moz-border-radius: 5px;
	-webkit-border-radius: 5px;
	border: 1px solid white;
	border-radius: 5px;
	list-style: none;
}

#uiDiv .desktop_icon:HOVER {
	padding: 0px;
	background-color: #ddd;
	-moz-border-radius: 5px;
	-webkit-border-radius: 5px;
	border: 1px solid white;
	border-radius: 5px;
}

.ui_catalog {
	border: 1px solid #ddd;
	-moz-border-radius: 5px;
	border-radius: 5px;
	border-radius: 5px;
}

#uiDiv .cache_ui {
	position: absolute;
	top: 0px;
	right: 0px;
	background-color: red;
	font-weight: bold;
	-moz-border-radius: 3px;
	border-radius: 3px;
	border-radius: 3px;
}
</style>
<div id="uiDiv">
	<div style="float: left; width: 100%;">
		<fieldset class="ui_catalog">
			<legend style="margin-left: 5px;">
				所有
			</legend>
			<ul>
				<%
					for (DesktopBean bean : desktopMap.values()) {
				%>
				<li class="desktop_icon ui-draggable ui-droppable"
					title="<%=bean.text%>"
					style="margin: 0px; float: left; position: relative;"
					<%if (!cacheMap.containsKey(bean.name)) {%>
					ondblclick="$Actions['addUIAct']('tabId='+$('desktopmenu').getAttribute('tabId')+'&uiName=<%=bean.name%>');"
					<%}%>>
					<span class="icon"><img src="<%=bean.icon%>"> </span>
					<div
						class="<%=cacheMap.containsKey(bean.name) ? "textdesc_has" : "textdesc"%>">
						<%=StringUtils.substring(bean.text, 5)%>
						<s></s>
					</div>
					<%
						if (cacheMap.containsKey(bean.name)) {
					%>
					<div class="cache_ui"><%=cacheMap.get(bean.name)%></div>
					<%
						}
					%>
				</li>
				<%
					}
				%>
			</ul>
		</fieldset>
	</div>
</div>