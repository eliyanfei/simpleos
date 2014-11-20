<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@page import="net.itsite.ItSiteUtil"%><%@page
	import="net.simpleframework.web.page.PageRequestResponse"%>
<style>
.home_item {
	text-align: center;
	height: 30px;
	line-height: 30px;
	width: 24%;
	float: left;
	margin: 6px;
	cursor: pointer;
	padding: 30px 0;
}

.home_item span {
	font-size: xx-large;
	font-weight: bold;
}

.home_item1 {
	background: #FFDEAD;
}

.home_item1:HOVER {
	background: #FFF68F;
}

.home_item2 {
	background: #A52A2A;
}

.home_item2:HOVER {
	background: #FF4500;
}

.home_item3 {
	background: #9932CC;
}

.home_item3:HOVER {
	background: #8B008B;
}

.home_item4 {
	background: #00CD00;
}

.home_item4:HOVER {
	background: #008B00;
}

.home_ {
	
}
</style>
<%
	final PageRequestResponse requestResponse = new PageRequestResponse(request, response);
%>
<div class="home_" align="center"
	style="width: 100%; clear: both; display: table;">
	<div class="home_item home_item1"
		onclick="$Actions.loc('/index.html');">
		<span>去前台</span>
	</div>
	<div class="home_item home_item4"
		onclick="$Actions.loc('/manager/user.html');">
		<span>用户管理</span>
	</div>
	<div class="home_item home_item3" onclick="$Actions.loc('/manager/site.html');">
		<span>站点设置</span>
	</div>
	<div class="home_item home_item2" onclick="$Actions.loc('/manager/desktop.html');">
		<span>模板设置</span>
	</div>
	<div class="home_item home_item3" onclick="$Actions.loc('/manager/ad.html');">
		<span>广告设置</span>
	</div>
	<div class="home_item home_item1"
		onclick="$Actions.loc('/manager/company.html');">
		<span>企业信息</span>
	</div>
	<div class="home_item home_item2"
		onclick="$Actions.loc('/manager/function.html?type=news&op=add');">
		<span>发资讯</span>
	</div>
	<div class="home_item home_item3"
		onclick="$Actions.loc('/manager/function.html?type=bbs&op=add');">
		<span>发帖子</span>
	</div>
	<div class="home_item home_item4"
		onclick="$Actions.loc('/manager/message.html');">
		<span>消息<%=ItSiteUtil.getMessages(requestResponse, -1)%></span>
	</div>
	<div class="home_item home_item2"
		onclick="$Actions.loc('/manager/remark.html');">
		<span>评论</span>
	</div>

</div>