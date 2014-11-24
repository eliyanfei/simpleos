<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.content.bbs.BbsUtils"%>
<%@ page import="net.simpleframework.util.DateUtils.TimeDistance"%>
<%@ page import="net.simpleframework.web.page.PageRequestResponse"%>
<%
	final String bbs_layout = BbsUtils.deployPath + "jsp/bbs_portal.jsp";
	String time = request.getParameter("time");
	if (time == null) {
		time = TimeDistance.day.name();
	}
	final PageRequestResponse requestResponse = new PageRequestResponse(request, response);
%>
<%
	request.setAttribute("__qs", BbsUtils.queryTopics(requestResponse, null, null, false, TimeDistance.valueOf(time), null, 1));
%>
<jsp:include page="<%=bbs_layout%>" flush="true">
	<jsp:param value="false" name="_show_tabs" />
</jsp:include>
