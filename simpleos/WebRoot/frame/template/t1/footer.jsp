<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@page
	import="net.prj.manager.site.PrjSiteUtils"%><%@page
	import="net.prj.manager.PrjMgrUtils"%><%@page import="java.util.Map"%><%@page
	import="java.util.HashMap"%><%@page
	import="net.itniwo.commons.StringsUtils"%><%@page
	import="net.a.ItSiteUtil"%><%@page
	import="net.simpleframework.web.page.PageRequestResponse"%><%@page
	import="net.prj.manager.links.PrjLinksUtils"%><%@page
	import="net.prj.core.$VType"%>
<%
	Map<String, String> map = new HashMap<String, String>();
	map.putAll(PrjMgrUtils.loadCustom("site"));
	map.putAll(PrjMgrUtils.loadCustom("links"));
	final PageRequestResponse requestResponse = new PageRequestResponse(request, response);
	final String menuName = ItSiteUtil.witchMenu(requestResponse);
%>
<div class="footer" align="center">
	<div class="links">
		<%=PrjLinksUtils.queryLinks(requestResponse, map)%>
	</div>
	<div class="lsep"></div>
	<div class="cr">
		<div>
			Powered by
			<%=StringsUtils.trimNull(map.get("site_name"), "")%>&nbsp;&nbsp;&nbsp;版权所有
			©<%=StringsUtils.trimNull(map.get("site_copyright"), "")%>&nbsp;&nbsp;<%=StringsUtils.trimNull(map.get("site_icp"), "")%>
			<a target="_blank" href="/about.html" hidefocus="hidefocus">关于</a>
			<a target="_blank" href="/contact.html" hidefocus="hidefocus">联系我们</a>
		</div>
		<div>
			建议使用
			<a target="_blank" href="http://firefox.com.cn/"
				hidefocus="hidefocus">Firefox</a>,
			<a target="_blank" href="http://www.google.com/chrome/"
				hidefocus="hidefocus">Chrome</a>来浏览本站！
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
	<%=ItSiteUtil.getAboutHtml(2)%>
</div>
