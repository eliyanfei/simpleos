<%@page import="net.itsite.ItSiteUtil"%>
<%@page
	import="net.simpleframework.organization.component.userpager.UserPagerUtils"%>
<%@page import="net.simpleframework.web.page.PageRequestResponse"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	final PageRequestResponse requestResponse = new PageRequestResponse(request, response);
	final String center = UserPagerUtils.getHomePath() + "/jsp/ue/user_edit.jsp";
	final Object userId = ItSiteUtil.getLoginUser(requestResponse).getId();
%>
<jsp:include page="/simpleos/template/template.jsp" flush="true">
	<jsp:param value="<%=center%>" name="center" />
	<jsp:param value="<%=userId%>" name="userid" />
</jsp:include>
