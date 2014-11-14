<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.web.WebUtils"%>
<%@ page import="net.simpleframework.util.StringUtils"%>
<div align="center" id="site_body">
	<%
		final String center = WebUtils.putIncludeParameters(request, request.getParameter("center"));
		if (StringUtils.hasText(center)) {
	%><jsp:include page="<%=center%>" flush="true"></jsp:include>
	<%
		}
	%>
</div>
