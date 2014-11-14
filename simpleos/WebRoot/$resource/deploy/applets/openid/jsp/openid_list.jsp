<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.applets.openid.OpenIDUtils"%>

<div class="openid_list"><a style="margin-right: 8px;"
	onclick="$Actions['ajaxOpenidAction']('op=Google');"><img
	src="<%=OpenIDUtils.deployPath%>images/google.gif" /></a> <a
	onclick="$Actions['ajaxOpenidAction']('op=Yahoo');"><img
	src="<%=OpenIDUtils.deployPath%>images/yahoo.gif" /></a></div>