<%@page import="net.simpleos.utils.StringsUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@page
	import="java.util.Map"%><%@page import="java.util.HashMap"%><%@page
	import="net.simpleos.SimpleosUtil"%><%@page
	import="net.simpleframework.web.page.PageRequestResponse"%><%@page
	import="net.simpleos.backend.links.LinksUtils"%><%@page
	import="net.simpleos.$VType"%>
<%
	final PageRequestResponse requestResponse = new PageRequestResponse(
	request, response);
	final String menuName = SimpleosUtil.witchMenu(requestResponse);
%>
<div class="footer" align="center">
	<div class="links">
		<%=LinksUtils.queryLinks(requestResponse,
					SimpleosUtil.attrMap)%>
	</div>
	<div class="lsep"></div>
	<div class="cr">
		<div>
			Powered by
			<%=StringsUtils.trimNull(
					SimpleosUtil.attrMap.get("site.site_name"), "")%>&nbsp;&nbsp;&nbsp;版权所有
			©<%=StringsUtils.trimNull(
					SimpleosUtil.attrMap.get("site.site_copyright"), "")%>&nbsp;&nbsp;<a
				href="http://www.miitbeian.gov.cn/" target="_blank"><%=StringsUtils.trimNull(
					SimpleosUtil.attrMap.get("site.site_icp"), "")%></a>
			<a target="_blank" href="/about.html" hidefocus="hidefocus">关于</a> <a
				target="_blank" href="/contact.html" hidefocus="hidefocus">联系我们</a>
		</div>
		<div>
			建议使用 <a target="_blank" href="http://firefox.com.cn/"
				hidefocus="hidefocus">Firefox</a>, <a target="_blank"
				href="http://www.google.com/chrome/" hidefocus="hidefocus">Chrome</a>来浏览本站！
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
	<%=SimpleosUtil.getAboutHtml(2)%>
</div>
