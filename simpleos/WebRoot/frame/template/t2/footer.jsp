<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@page
	import="net.prj.manager.site.PrjSiteUtils"%><%@page
	import="net.prj.manager.PrjMgrUtils"%><%@page import="java.util.Map"%><%@page
	import="java.util.HashMap"%><%@page
	import="net.itsite.utils.StringsUtils"%><%@page
	import="net.itsite.ItSiteUtil"%><%@page
	import="net.simpleframework.web.page.PageRequestResponse"%><%@page
	import="net.prj.manager.links.PrjLinksUtils"%>

<%
	Map<String, String> map = new HashMap<String, String>();
	map.putAll(PrjMgrUtils.loadCustom("site"));
	map.putAll(PrjMgrUtils.loadCustom("links"));
	final PageRequestResponse requestResponse = new PageRequestResponse(request, response);
	final String menuName = ItSiteUtil.witchMenu(requestResponse);
%>
<div class="footer" align="center">
	<div class="webLink_index" align="left">
		<div align="center" class="_link middle">
			友情链接
		</div>
		<div id="link_index" class="middle"><%=PrjLinksUtils.queryLinks(requestResponse, map)%>
		</div>
	</div>
	<div class="lineWhite"></div>
	<div class="webFoot">
		<div class="foot middle">
			<div class="footLeft">
				<span><a target="_blank" style="color: #98c3e4;" href="/about.html">关于</a> </span>
				<span><a target="_blank" style="color: #98c3e4;"  href="/contact.html">联系我们</a> </span>
			</div>
			<div class="footRight">
				Powered by
				<%=StringsUtils.trimNull(map.get("site_name"), "")%>&nbsp;&nbsp;&nbsp;版权所有
				©<%=StringsUtils.trimNull(map.get("site_copyright"), "")%>&nbsp;&nbsp;<%=StringsUtils.trimNull(map.get("site_icp"), "")%>

				(建议使用
				<a target="_blank" href="http://firefox.com.cn/">Firefox</a>,
				<a target="_blank" href="http://www.google.com/chrome/">Chrome</a>来浏览本站！)

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
</script><%=ItSiteUtil.getAboutHtml(2)%>
</div>