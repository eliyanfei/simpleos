<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.web.WebUtils"%>
<%@ page import="net.simpleframework.util.StringUtils"%>
<html>
	<jsp:include page="/simple/include/inc_head.jsp" flush="true"></jsp:include>
	<body style="margin: 0px; padding: 0px;">
		<div align="center">
			<div id="t_header"><jsp:include
					page="/simple/include/header.jsp" flush="true"></jsp:include></div>
			<div id="t_main">
				<div id="t_body" style="margin: 0 auto;" class="clear_float">
					<table width="100%" height="100%" cellpadding="0" cellspacing="0">
						<tr>
							<td width="22%" valign="top" style="border-right: 3px solid rgb(243, 243, 243);">
								<div class="c_left" style="width: 680px;">
									<%
										final String left = WebUtils.putIncludeParameters(request, request.getParameter("left"));
										final String center = WebUtils.putIncludeParameters(request, request.getParameter("center"));
										if (StringUtils.hasText(left)) {
									%><jsp:include page="<%=left%>" flush="true"></jsp:include>
									<%
										}
									%>
								</div>
							</td>
							<td valign="top">
								<div class="c_right" style="width: 304px;">
									<%
										if (StringUtils.hasText(center)) {
									%><jsp:include page="<%=center%>" flush="true"></jsp:include>
									<%
										}
									%>
								</div>
							</td>
						</tr>
					</table>
				</div>
				<jsp:include page="/simple/include/links.jsp" flush="true"></jsp:include>
				<jsp:include page="/simple/include/footer.jsp" flush="true"></jsp:include>
			</div>
		</div>
	</body>
</html>



