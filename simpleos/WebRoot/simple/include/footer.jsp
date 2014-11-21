<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.web.page.PageFilter"%><%@page
	import="net.simpleframework.util.HTMLBuilder"%><%@page
	import="net.simpleframework.web.page.PageRequestResponse"%><%@page
	import="net.itsite.ItSiteUtil"%><%@page
	import="net.simpleframework.web.page.SkinUtils"%><%@page
	import="net.simpleframework.my.dialog.DialogUtils"%><%@page
	import="net.simpleframework.web.page.SessionCache"%><%@page
	import="java.util.List"%><%@page import="net.simpleos.$VType"%>
<%
	final PageRequestResponse requestResponse = new PageRequestResponse(request, response);
	final String menuName = ItSiteUtil.witchMenu(requestResponse);
%>
<div id="site_footer"
	style="margin: 0 auto; position: fixed; bottom: 0px; right: 0px; left: 0px;"
	align="center">
	<%=ItSiteUtil.getAboutHtml(1)%>
	<div style="float: left;">
		<%
			final List<String> navList = (List<String>) request.getSession().getAttribute("navList");
			if (navList != null) {
				int i = 0;
				for (String nav : navList) {
		%>
		<span class="nav_item"><%=nav%></span>
		<%
			if (i++ != navList.size() - 1) {
		%>
		<span class="nav_img"></span>
		<%
			}
				}
			}
		%>
	</div>
</div>
<script type="text/javascript">
$ready(function() {
	$$('.mm').each(function(c) {
		if (c.innerHTML == '<%=menuName%>') {
			c.addClassName('mm1');
			return;
		}
	});
});
</script>
