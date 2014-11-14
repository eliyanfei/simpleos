<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="net.simpleframework.organization.account.AccountSession"%>
<%@ page
	import="net.simpleframework.organization.component.login.LoginUtils"%>
<%@ page import="net.simpleframework.organization.account.IAccount"%>
<%@ page import="net.simpleframework.organization.OrgUtils"%>
<%@ page import="net.simpleframework.web.page.PageRequestResponse"%>
<%@ page import="net.simpleframework.my.space.MySpaceUtils"%><%@page
	import="net.simpleframework.util.HTMLBuilder"%><%@page
	import="net.simpleframework.util.ConvertUtils"%><%@page
	import="net.a.ItSiteUtil"%>

<%
	final PageRequestResponse requestResponse = new PageRequestResponse(request, response);
	IAccount account = AccountSession.getLogin(session);
	final boolean rsShow = ConvertUtils.toBoolean(request.getParameter("rsShow"), true);
%>
<%
	if (account != null) {
%>
<img class="photo_icon my_navTooltip_class" alt="#(Itsite.login.1)"
	src="<%=OrgUtils.getPhotoSRC(request, account.getId(), 32, 32)%>"
	style="margin-right: 4px; width: 32px; height: 32px; -webkit-box-shadow: 0px 0px 0px; -moz-box-shadow: 0px 0px 0px; box-shadow: 0px 0px 0px;" />
<a
	onclick="$Actions['editUserWindow']('<%=OrgUtils.um().getUserIdParameterName()%>=<%=account.getId()%>');"><%=account.user()%></a>
<span> ( </span>
<a onclick="$Actions['ajaxLogout']();">#(Itsite.login.0)</a>
<span> )</span>
<%=ItSiteUtil.witchLanguage(requestResponse)%>
<%
	} else {
%>
<div class="headTopText">
	<a href="javascript:;" href="/login.html" hidefocus="true">登入</a>
</div>
<%
	}
%>
