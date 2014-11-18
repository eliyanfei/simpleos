<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.web.WebUtils"%>
<%@ page import="net.simpleframework.util.StringUtils"%>
<html>
<body>
	<div align="center"><jsp:include page="header.jsp" flush="true"></jsp:include>
		<div class="bodyc" align="center">
			<div class="fixc" style="min-height: 450px;">
				<%
					final String center = WebUtils.putIncludeParameters(request,
							request.getParameter("center"));
					if (StringUtils.hasText(center)) {
				%><jsp:include page="<%=center%>" flush="true"></jsp:include>
				<%
					}
				%>
			</div>
			<jsp:include page="footer.jsp" flush="true"></jsp:include>
		</div>
	</div>
</body>
</html>
