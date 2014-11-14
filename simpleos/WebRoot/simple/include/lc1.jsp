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
				<div style="background: #fff;">
					<div class="Category_BlankTemplatePage">
						<table>
							<tbody>
								<tr>
									<td width="250" valign="top" class="category">
										<div style="overflow: auto; height: 100%;">
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
									<td class="splitbar" width="1" style="cursor: e-resize;"></td>
									<td valign="top">
										<div class="col2">
											<div class="Category_ListTemplatePage"
												style="overflow: auto;">
												<%
													if (StringUtils.hasText(center)) {
												%><jsp:include page="<%=center%>" flush="true"></jsp:include>
												<%
													}
												%>
											</div>
										</div>
									</td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>
			</div>
			<%
				final String footer = request.getParameter("footer");
				if (StringUtils.hasText(footer)) {
			%><jsp:include page="<%=footer%>" flush="true"></jsp:include>
			<%
				}
			%>
			<jsp:include page="/simple/include/footer.jsp" flush="true"></jsp:include>
		</div>
	</body>
	<script type="text/javascript">
(function() {
	var sb = $$("#site_content .splitbar")[0];
	$Comp.createSplitbar(sb, sb.previous());
})();
</script>
</html>



