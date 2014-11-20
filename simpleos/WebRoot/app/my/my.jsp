<%@page import="net.itsite.ItSiteUtil"%>
<%@page
	import="net.simpleframework.organization.component.userpager.UserPagerUtils"%>
<%@page import="net.simpleframework.web.page.PageRequestResponse"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@page
	import="net.prj.manager.template.PrjTemplateUtils"%>
<%
	final String templateUrl = PrjTemplateUtils.getTemplateUrl();
	final PageRequestResponse requestResponse = new PageRequestResponse(request, response);
	final String center = UserPagerUtils.getHomePath() + "/jsp/ue/user_edit.jsp";
	final Object userId = ItSiteUtil.getLoginUser(requestResponse).getId();
%>
<jsp:include page="<%=templateUrl%>" flush="true">
	<jsp:param value="<%=center%>" name="center" />
	<jsp:param value="<%=userId%>" name="userid" />
</jsp:include>