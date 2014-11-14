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
	<div align="left" class="_link">
		友情链接
	</div>
	<table class="t_footer2" style="width: 100%;">
		<tr>
			<td width="75%" valign="top">
				<div class="ll">
					<%=PrjLinksUtils.queryLinks(requestResponse, map)%>
				</div>
			</td>
		</tr>
	</table>
	<div id="t_footer" style="margin: 0 auto;" align="center">
		<div>
			<a href="/contact.html">联系我们</a><span style="margin: 0px 4px;">|</span><a
				href="/about.html">关于</a>
		</div>
		<div style="padding-top: 4px; color: gray;">
			Powered by
			<%=StringsUtils.trimNull(map.get("site_name"), "")%>&nbsp;&nbsp;&nbsp;版权所有
			©<%=StringsUtils.trimNull(map.get("site_copyright"), "")%>&nbsp;&nbsp;<%=StringsUtils.trimNull(map.get("site_icp"), "")%>
		</div>
		<div style="padding-top: 4px; color: gray;">
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
