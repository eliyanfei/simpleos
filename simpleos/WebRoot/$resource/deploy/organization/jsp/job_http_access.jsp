<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.util.AlgorithmUtils"%>
<%@ page import="net.simpleframework.util.LocaleI18n"%>
<%@ page import="net.simpleframework.util.StringUtils"%>
<%@ page import="net.simpleframework.web.page.PageRequestResponse"%><%@page
	import="net.a.ItSiteUtil"%>

<%
	String jn = StringUtils.blank(request.getParameter("job"));
	String jname;
	if (jn.startsWith("#")) {
		jname = LocaleI18n.getMessage("job_http_access.4");
		jn = jn.substring(1);
	} else {
		jname = LocaleI18n.getMessage("job_http_access.5");
	}
	PageRequestResponse requestResponse = new PageRequestResponse(request, response);
	final boolean http = requestResponse.isHttpRequest();
%>
<html>
	<%
		if (!ItSiteUtil.isLogin(requestResponse)) {
	%>
	<script type="text/javascript">
$Actions.loc("/login.html");
</script>
	<%
		}
	%>
	<body>
		<div align="center">
			<div
				class="<%=http ? "job_http_access" : "job_http_access job_http_page_access"%>">
				<div class="simple_toolbar">
					<div class="error_image"></div>
					<div class="detail">
						<div class="t1">
							#(job_http_access.0)
						</div>
						<div class="t2 wrap_text"><%=new String(AlgorithmUtils.base64Decode(request.getParameter("v")))%></div>
						<div class="t3"><%=LocaleI18n.getMessage("job_http_access.1", jname, jn)%></div>
					</div>
				</div>
				<div class="bbar">
					<%
						if (http) {
					%>
					<input type="button" value="#(job_http_access.2)"
						onclick="history.back();" />
					<input type="button" value="#(job_http_access.3)"
						onclick="$Actions.loc('/');" />
					<%
						} else {
					%>
					<input type="button"
						value="<%=LocaleI18n.getMessage("Button.Close")%>"
						onclick="$win(this).close();" />
					<%
						}
					%>
				</div>
			</div>
		</div>
	</body>
</html>
