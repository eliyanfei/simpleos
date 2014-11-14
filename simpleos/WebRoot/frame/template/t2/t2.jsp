<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.web.WebUtils"%>
<%@ page import="net.simpleframework.util.StringUtils"%>
<html>
	<head>
	</head>
	<body style="margin: 0px; padding: 0px; overflow: auto;">
		<jsp:include page="header.jsp" flush="true"></jsp:include>
		<div class="bodyc" align="center">
			<div class="fixc">
				<%
					final String center = WebUtils.putIncludeParameters(request, request.getParameter("center"));
					if (StringUtils.hasText(center)) {
				%><jsp:include page="<%=center%>" flush="true"></jsp:include>
				<%
					}
				%>
			</div>
		</div>
		<jsp:include page="footer.jsp" flush="true"></jsp:include>
		<jsp:include page="/frame/template/initer.jsp" flush="true"></jsp:include></body>
</html>



