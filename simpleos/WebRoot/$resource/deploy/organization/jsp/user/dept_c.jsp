<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@page
	import="net.simpleframework.web.page.PageRequestResponse"%><%@page
	import="net.simpleframework.organization.OrgUtils"%><%@page
	import="net.simpleos.SimpleosUtil"%>


<%
	PageRequestResponse requestResponse = new PageRequestResponse(request, response);
	SimpleosUtil.addMenuNav(request.getSession(), null, "用户和账号管理", false);
%>
<%=OrgUtils.tabs(requestResponse)%>
<div id="__dept_tree" style="padding: 8px;">
</div>
<script type="text/javascript">
</script>