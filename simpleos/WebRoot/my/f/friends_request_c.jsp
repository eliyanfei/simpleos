<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.web.page.PageRequestResponse"%>
<%@ page import="net.simpleframework.my.friends.FriendsUtils"%>
<%
	final PageRequestResponse requestResponse = new PageRequestResponse(
			request, response);
	final String my_friends_request = FriendsUtils.deployPath
			+ "jsp/my_friends_request.jsp";
%>
<%=FriendsUtils.tabs(requestResponse)%>
<div class="simple_tabs_content" style="width: 75%; padding: 8px;">
	<jsp:include page="<%= my_friends_request%>"></jsp:include></div>