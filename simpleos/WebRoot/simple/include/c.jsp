<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.web.WebUtils"%>
<%@ page import="net.simpleframework.util.StringUtils"%>
<html>
	<head>
		<style type="text/css">
.category  #left_menu {
	height: 30px;
	line-height: 29px;
	border-bottom: 1px solid rgb(170, 170, 170);
	vertical-align: middle;
	display: block;
}
</style>
	</head>
	<jsp:include page="inc_head.jsp" flush="true"></jsp:include>
	<body style="margin: 0px; padding: 0px; overflow: hidden;">
		<div align="center" id="site_body">
			<div><jsp:include page="header.jsp" flush="true"></jsp:include></div>
			<div id="site_content" class="bodyc">
				<div class="Category_ListTemplatePage" style="overflow: auto;">
					<%
						final String center = WebUtils.putIncludeParameters(request, request.getParameter("center"));
						if (StringUtils.hasText(center)) {
					%><jsp:include page="<%=center%>" flush="true"></jsp:include>
					<%
						}
					%>
				</div>
			</div>
			<jsp:include page="/simple/include/footer.jsp" flush="true"></jsp:include>
		</div>
	</body>
</html>