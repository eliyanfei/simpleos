<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="net.simpleframework.content.bbs.BbsUtils"%>
<%@page import="net.simpleframework.web.page.PageRequestResponse"%><%@page
	import="net.simpleos.SimpleosUtil"%><%@page
	import="net.simpleframework.content.component.topicpager.TopicPagerUtils"%><%@page
	import="net.simpleframework.web.page.component.ComponentParameter"%>
<%
	final ComponentParameter nComponentParameter = TopicPagerUtils.getComponentParameter(request, response);
	BbsUtils.initTopicPager(nComponentParameter);
	final String bbs_post_view = BbsUtils.deployPath + "jsp/bbs_posts_view.jsp";
	SimpleosUtil.addMenuNav(request.getSession(), "/bbs.html", "#(Itsite.menu.bbs)", false);
%>
<jsp:include page="<%=bbs_post_view%>" flush="true"></jsp:include>