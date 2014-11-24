<%@page import="net.simpleos.utils.StringsUtils"%>
<%@page import="net.simpleos.SimpleosUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@page import="java.util.Map"%><%@page
	import="java.util.HashMap"%><%@page
	import="net.simpleframework.web.page.PageRequestResponse"%><%@page
	import="net.simpleos.backend.links.LinksUtils"%>
<%
	final PageRequestResponse requestResponse = new PageRequestResponse(
			request, response);
	final String menuName = SimpleosUtil.witchMenu(requestResponse);
%>
<div class="footer" align="center">
	<div class="webLink_index" align="left">
		<div align="center" class="_link middle">友情链接</div>
		<div id="link_index" class="middle"><%=LinksUtils.queryLinks(requestResponse,
					SimpleosUtil.attrMap)%>
		</div>
	</div>
	<div class="lineWhite"></div>
	<div class="webFoot">
		<div class="foot middle">
			<div class="footLeft">
				<span><a target="_blank" style="color: #98c3e4;"
					href="/about.html">关于</a> </span> <span><a target="_blank"
					style="color: #98c3e4;" href="/contact.html">联系我们</a> </span>
			</div>
			<div class="footRight">
				<jsp:include page="../footer_base.jsp" flush="true"></jsp:include>
			</div>
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
	</script><%=SimpleosUtil.getAboutHtml(2)%>
</div>