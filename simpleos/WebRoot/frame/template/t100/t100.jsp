<%@page
	import="net.simpleframework.web.page.component.ComponentParameter"%>
<%@page import="net.simpleframework.desktop.DesktopMenuHandle"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="net.simpleframework.desktop.DesktopUIUtils"%><%@page
	import="net.simpleframework.web.page.PageRequestResponse"%><%@page
	import="net.simpleframework.organization.account.IAccount"%><%@page
	import="net.simpleframework.organization.account.AccountSession"%><%@page
	import="net.simpleframework.organization.OrgUtils"%><%@page
	import="net.simpleframework.my.home.MyHomeUtils"%><%@page
	import="net.simpleframework.web.WebUtils"%><%@page
	import="net.simpleframework.desktop.DesktopMgr"%><%@page
	import="net.simpleframework.desktop.DesktopMgr.DesktopBean"%><%@page
	import="net.itsite.permission.PlatformUtis"%><%@page
	import="net.itsite.AnnounceHandle"%><%@page
	import="net.simpleframework.organization.account.Account"%><%@page
	import="net.a.ItSiteUtil"%><%@page
	import="net.simpleframework.organization.IUser"%><%@page
	import="net.simpleframework.core.id.LongID"%>

<%
	PageRequestResponse requestResponse = new PageRequestResponse(request, response);
	IUser user = ItSiteUtil.getLoginUser(requestResponse);
	boolean hasRole = PlatformUtis.hasJob(user.getId(), "announce_mgr");
%>
<html>
	<head>
		<meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
		<style type="text/css">
</style>
		<style>
body {
	filter:
		"progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod='scale')";
	-moz-background-size: 100% 100%;
	background-size: 100% 100%;
}

#panel_content textarea {
	-moz-border-radius: 5px;
	-webkit-border-radius: 5px;
	border-radius: 5px;
}
</style>
	</head>
	<body class="bodyc"
		style="background-image: url('/frame/template/t100/themes/theme/10gaotie5.jpg'); background-color: #ccc; margin: 0; padding: 0;">
		<div id="desktopmenu" tabId="ui-tab1">
			<input type="hidden" id="ui_add_flag" value="false">
			<input type="hidden" id="now_ui_name" value="">
			<script type="text/javascript">
if ($desktop.getCookies('desktop_theme'))
	document.body.style.backgroundImage = "url("
			+ $desktop.getCookies('desktop_theme') + ")";
</script>
			<div id="navBar" style="top: 0px;">
				<%=DesktopUIUtils.buildTabs(requestResponse)%>
				<a href="javascript:void;" class="allgrid" title="全局视图" id="allgrid"
					onclick="$('all_grid_content').toggle();">&nbsp;</a>
			</div>
			<div id="desktopPanel" style="margin-top: 25px; margin-bottom: 75px;">
				<div id="desktopInnerPanel" class="ui-draggable"
					style="width: 5464px; height: 100px; left: -0px;">
				</div>
			</div>
			<div id="taskBarWrap">
				<div id="taskBar" style="">
					<a class="float_left" href="javascript:void;" id="show_desktop"
						style="margin-right: 8px;" title="导航菜单"> <img
							src="<%=request.getContextPath()%>/frame/template/t100/themes/default/images/rams_iconX32.png">
					</a>
					<div id="task_lb" appIds="" style="height: 40px; float: left;">
						<%
							for (DesktopBean db : DesktopMgr.getDesktopMgr().toolBarList) {
						%>
						<div class="task_lb_sub defaultTab" style="width: 48px;"
							id="<%=db.name%>__"
							onclick="$('now_ui_name').value='<%=db.name%>';this.toggle();<%=db.action%>;"
							title="<%=db.text%>">
							<div class="task_lb_sub_icon" style="padding-left: 3px;">
								<img src="<%=db.icon%>">
							</div>
						</div>
						<%
							}
						%>
					</div>
					<div id="task_timer"
						style="float: right; font-size: 18px; color: white; font-weight: bold;">
						12:45:25
					</div>
				</div>
			</div>
			<div id="lr_bar" style="top: 63px; left: 0px;">
				<div id="start_block" align="center">
					<a> <img class="photo_icon" id="start_btn"
							alt="#(Itsite.login.1)"
							<%if (!LongID.zero.equals2(user.getId())) {%>
							onclick="$Actions['editUserWindow']('<%=OrgUtils.um().getUserIdParameterName()%>=<%=user.getId()%>');"
							<%}%>
							src="<%=OrgUtils.getPhotoSRC(request, user.getId(), 48, 48)%>" />
					</a>
					<a style="color: white;"
						<%if (!LongID.zero.equals2(user.getId())) {%>
						onclick="$Actions['editUserWindow']('<%=OrgUtils.um().getUserIdParameterName()%>=<%=user.getId()%>');"
						<%}%>><%=user.getText()%></a>

				</div>
				<ul id="default_app" class="ui-droppable">
					<li class="ui-draggable ui-droppable" id="configMgrWin_home"
						onclick="$('now_ui_name').value='configMgrWin';$Actions['configMgrWin']();">
						<span><img src="/frame/template/t100/themes/app/config.png"
								title="配置管理"> </span>
					</li>
				</ul>
				<div class="default_tools" align="center" id="fast_config">
					<%
						if (!LongID.zero.equals2(user.getId())) {
					%>
					<img src="/frame/template/t100/themes/icon/logout_32.png"
						id="ajaxLogout_tools" style="margin-top: 3px;"
						onclick="$Actions['ajaxLogout']();" title="退出系统">
					<%
						} else {
					%><img src="/frame/template/t100/themes/icon/logout_32.png"
						id="ajaxLogout_tools" style="margin-top: 3px;"
						onclick="$Actions['loginWin']();" title="登入系统">
					<%
						}
					%>
					<img src="/frame/template/t100/themes/icon/theme_32.png" title="主题"
						id="themeWin_tools" style="margin-top: 3px;"
						onclick="$Actions['themeWin']();">
					<img src="/frame/template/t100/themes/icon/sound_yes_32.png"
						title="声音" id="desktop_sound_id"
						onclick="$desktop.setSound(this);">
					<img src="/frame/template/t100/themes/icon/setting_32.png" title="系统"
						id="settingWin_tools" onclick="$Actions['settingWin']();">
				</div>
			</div>
			<script type="text/javascript">
$Actions['desktopAppAjax']();
$ready(function() {
	$desktop.init(".ui-navbar", ".deskIcon");
	if ($desktop.getCookies('lr_') == 'true')
		$desktop.createLeftMove("lr_bar", 73);
});
</script>
		</div>
		<div id="audioId" style="display: none;"></div>
		<div id="panel" style="display: none;">
			<div id="panel_title">
				<span>公告通知</span>
				<%
					if (hasRole) {
				%>
				<div
					style="float: right; width: 24px; height: 24px; cursor: pointer; margin-top: -5px;">
					<img style="width: 24px; height: 24px;" alt="" id="announce_act"
						st="edit" onclick="announceEdit(this);"
						src="/frame/template/t100/themes/default/images/announce_edit.png">
				</div>
				<%
					}
				%>
			</div>
			<div id="panel_content">
				<%=AnnounceHandle.queryAnnounce()%>
			</div>
			<div id="handle">
				<span id="handle_open" style="display: none;"
					onclick="$desktop.announce(this,false);"></span>
				<span id="handle_close" onclick="$desktop.announce(this,true);"></span>
			</div>
		</div>
		<div id="all_grid_content" style="display: none;"></div>
		<div id="map_legend_content" style="display: none;"></div>
		<input type="hidden" id="map_baidu_idx" value="">
		<jsp:include page="help.jsp"></jsp:include>
	</body>
	<script type="text/javascript">
<%=DesktopUIUtils.getAllTheme(requestResponse)%>
$desktop.playAudio("login.wav");
contextPath = "<%=request.getContextPath()%>";
$ready(function() {
	$desktop.initDesktopConfig();
	$desktop.addEvent();
		<%DesktopMenuHandle menu = new DesktopMenuHandle();
			ComponentParameter compParameter = new ComponentParameter(request, response, null);
			String jsCode = menu.jsCode(compParameter);%>
		<%=jsCode%>
		window.startMenu=new $startMenu('#show_desktop',menus);
		startMenu.createMenu();
});

function announceEdit(obj) {
	var st = obj.getAttribute('st');
	if (st == 'edit') {
		obj.setAttribute('st','save');
		obj.src = '/frame/template/t100/themes/default/images/announce_save.png';
		$('panel_content').innerHTML = '<textarea style="width: 100%;height: 100%" id="announceValue"></textarea>';
		$Actions['getAnnounce']();
	} else {
		obj.setAttribute('st','edit');
		obj.src = '/frame/template/t100/themes/default/images/announce_edit.png';
		$Actions['saveAnnounce']('announceValue='+$F('announceValue'));
		$('panel_content').innerHTML = $F('announceValue');
	}
}

(function() {
	var clock = $('task_timer');
	var pad = function(x) {
		return x < 10 ? '0' + x : x;
	};
	var ticktock = function() {
		var d = new Date();
		var h = pad(d.getHours());
		var m = pad(d.getMinutes());
		var s = pad(d.getSeconds());
		var current_time = [ h, m, s ].join(':');
		clock.innerHTML = current_time;
	};
	ticktock();
	setInterval(ticktock, 1000);
}());
</script>
</html>