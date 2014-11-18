<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.my.friends.FriendsUtils"%>
<%@ page import="net.simpleframework.web.page.PageRequestResponse"%>
<%
	final PageRequestResponse requestResponse = new PageRequestResponse(
			request, response);
	final String my_friends_search = FriendsUtils.deployPath
			+ "jsp/my_friends_search.jsp";
%>
<jsp:include page="<%=my_friends_search%>"></jsp:include>
