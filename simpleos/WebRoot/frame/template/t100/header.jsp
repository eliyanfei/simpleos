<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@page
	import="net.simpleframework.web.page.PageRequestResponse"%><%@page
	import="net.simpleframework.organization.account.IAccount"%><%@page
	import="net.simpleframework.organization.account.AccountSession"%><%@page
	import="net.simpleframework.organization.OrgUtils"%><%@page
	import="net.a.ItSiteUtil"%>

<%
	final PageRequestResponse requestResponse = new PageRequestResponse(request, response);
	IAccount account = AccountSession.getLogin(session);
%>

<div class="topbar" align="center">
	<div class="fixc">
		<div class="le">
			<div class="logo" onclick="$Actions.loc('/');"></div>
		</div>
		<div class="re">
			<%
				if (account != null) {
			%>
			<a href="/msg/notice/my" style="position: relative;"
				hidefocus="hidefocus">消息</a><a href="/favorites/my"
				hidefocus="hidefocus">收藏</a><a href="/my.html"
				style="font-style: italic;" hidefocus="hidefocus"><img
					style="vertical-align: middle" class="icon16 photo_icon"
					alt="#(Itsite.login.1)"
					src="<%=OrgUtils.getPhotoSRC(request, account.getId(), 32, 32)%>" /><%=account.user().getName()%>
			</a><a href="javascript:void(0);" onclick="$Actions['ajaxLogout']();"
				hidefocus="hidefocus">注销</a>
			<%=ItSiteUtil.witchSkin(requestResponse)%>
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

		</div>
		<div class="clearfix"></div>
	</div>
</div>
<div class="header" align="center">
	<div class="fixc">

		<nav>
		<div class="nav"></div>
		<div class="rc">
			<a href="/framework" class="fitem" hidefocus="hidefocus">框架</a><a
				href="/product" class="fitem" hidefocus="hidefocus">产品</a><a
				href="/news/list" class="fitem" hidefocus="hidefocus">资讯</a><a
				href="/bbs/category" class="fitem" hidefocus="hidefocus">交流</a>
		</div>
		</nav>
		<div class="clearfix"></div>
	</div>
</div>