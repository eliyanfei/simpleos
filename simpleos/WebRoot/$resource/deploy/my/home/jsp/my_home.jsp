<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.web.page.PageRequestResponse"%>
<%@ page import="net.simpleframework.my.home.MyHomeUtils"%>
<div class="clear_float myhome_class" style="height: 28px;">
	<div style="float: right;" class="bar">
		<a onclick="$Actions['layoutModulesWindow']();">#(my_home.2)</a>
	</div>
	<div style="float: left;"><%=MyHomeUtils.tabs(new PageRequestResponse(request,
					response))%>
		<a class="addtab" onclick="$Actions['addMyhomeTabWin']();">#(my_home.0)</a>
	</div>
</div>
<div id="__myhome_layout"></div>