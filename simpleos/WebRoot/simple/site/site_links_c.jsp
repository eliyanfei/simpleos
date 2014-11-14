<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@page import="net.itsite.impl.PrjColumns"%><%@page
	import="net.prj.manager.site.PrjSiteUtils"%><%@page
	import="java.util.Map"%><%@page import="net.prj.manager.PrjMgrUtils"%><%@page
	import="net.itniwo.commons.StringsUtils"%>

<%
	Map<String, String> map = PrjMgrUtils.loadCustom("links");
	String name = StringsUtils.trimNull(map.get("links_linksType"), "sys");
%>
<div style="padding: 0 25px;" id="site_links_form">
	<div>
		<input type="radio" name="links_linksType" id="links_sys_mgr"
			value="sys" <%="sys".equals(name) ? "checked=\"checked\"" : ""%>
			onclick="linksToggle('links_sys_mgr_id','links_custom_id',this);">
		<label for="links_sys_mgr">
			#(links.3)
		</label>
		<input type="radio" name="links_linksType" id="links_custom"
			value="custom"
			<%="custom".equals(name) ? "checked=\"checked\"" : ""%>
			onclick="linksToggle('links_custom_id','links_sys_mgr_id',this);">
		<label for="links_custom">
			#(links.4)
		</label>
	</div>
	<div id="links_sys_mgr_id">
		<input type="button" class="button2" value="#(links.5)"
			onclick="$IT.A('linksAddWin');">
		<div id="linksDataId"></div>
	</div>
	<div id="links_custom_id" style="display: none;">
		<input type="button" class="button2" value="#(Itsite.c.ok)"
			onclick="$IT.A('site_linksAct');">
		<textarea name="links_homeLinks" id="links_homeLinks" rows="10"
			style="display: none;"></textarea>
	</div>
</div>
<script type="text/javascript">
function linksToggle(t1, t2, t) {
	$(t1).style.display = '';
	$(t2).style.display = 'none';
	$IT.A('site_linksAct', 'rs=false&links_linksType=' + t.value);
}
$ready(function() {
	if ($('links_custom').checked) {
		$('links_custom_id').style.display = '';
		$('links_sys_mgr_id').style.display = 'none';
	}
	if ($('links_sys_mgr').checked) {
		$('links_custom_id').style.display = 'none';
		$('links_sys_mgr_id').style.display = '';
	}
});
</script>