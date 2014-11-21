<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@page
	import="net.simpleframework.web.page.PageRequestResponse"%><%@page
	import="net.simpleframework.organization.account.IAccount"%><%@page
	import="net.simpleframework.organization.account.AccountSession"%><%@page
	import="net.simpleframework.organization.OrgUtils"%><%@page
	import="net.itsite.ItSiteUtil"%>

<%
	final PageRequestResponse requestResponse = new PageRequestResponse(request, response);
	IAccount account = AccountSession.getLogin(session);
%>

<div class="topbar" align="center">
	<div class="fixc">
		<div class="le">
			<div class="logo" onclick="$Actions.loc('/');"></div>
		</div>
		<div class="theader">
			<div class="rc">
				<div id="_menu"></div>
			</div>
		</div>
		<div class="re">
			<div class="headTopText">
				<%
					if (account != null) {
						if(ItSiteUtil.isManage(requestResponse)){
				%>
				<a href="/manager/home.html" hidefocus="hidefocus">后台</a>
				<%} %>
				<a href="/mymessage.html" style="position: relative;"
					hidefocus="hidefocus">#(Template.0)<%=ItSiteUtil.getMessages(requestResponse, -1)%></a><a
					href="/myfavorite.html" hidefocus="hidefocus">#(Template.1)</a><a
					href="/space.html" style="font-style: italic;" hidefocus="hidefocus"><img
						style="vertical-align: middle" class="icon16 photo_icon"
						alt="#(Itsite.login.1)"
						src="<%=OrgUtils.getPhotoSRC(request, account.getId(), 32, 32)%>" /><%=account.user().getText()%>
				</a><a href="javascript:void(0);" onclick="$Actions['ajaxLogout']();"
					hidefocus="hidefocus">#(Template.2)</a>
				<%
					} else {
				%>

				<a href="javascript:;" href="/login.html" hidefocus="true">#(Template.3)</a>
				<%
					if ("true".equals(ItSiteUtil.attrMap.get("sys.sys_register"))) {
				%>
				<a href="javascript:;" href="/register.html" hidefocus="true">注册</a>
				<%
					}
				%>
				<%
					}
				%>
				<%=ItSiteUtil.witchLanguage(requestResponse)%>
			</div>
		</div>
		<div class="clearfix"></div>
	</div>
</div>
