<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@page
	import="net.prj.manager.site.PrjSiteUtils"%><%@page
	import="net.prj.manager.PrjMgrUtils"%><%@page import="java.util.Map"%><%@page
	import="java.util.HashMap"%><%@page
	import="net.itniwo.commons.StringsUtils"%>

<%
	Map<String, String> map = new HashMap<String, String>();
	map.putAll(PrjMgrUtils.loadCustom("site"));
	map.putAll(PrjMgrUtils.loadCustom("links"));
%>
<div class="footer" align="center">
	<div>
		<%=StringsUtils.trimNull(map.get("links_homeLinks"), "")%>
	</div>
	<div class="lsep"></div>
	<div class="cr">
		<div>
			Powered by
			<%=StringsUtils.trimNull(map.get("site_name"), "")%>&nbsp;&nbsp;&nbsp;版权所有
			©<%=StringsUtils.trimNull(map.get("site_copyright"), "")%>&nbsp;&nbsp;<%=StringsUtils.trimNull(map.get("site_icp"), "")%>
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
	$('SiteTemplate_loadTime').innerHTML = document.getCookie('pageload_time');
});
</script>
</div>