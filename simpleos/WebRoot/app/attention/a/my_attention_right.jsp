<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@page
	import="net.simpleframework.my.space.MySpaceUtils"%><%@page
	import="net.simpleframework.web.page.PageRequestResponse"%>

<%
	final PageRequestResponse requestResponse = new PageRequestResponse(request, response);
%>
<div>
	<%=MySpaceUtils.applicationModule.attentionTabs(requestResponse)%>
</div>
<div class="simple_tabs_content space_tabs_content">
	<div id="my_attention_id"></div>
</div>
