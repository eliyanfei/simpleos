<%@page import="net.simpleos.SimpleosUtil"%>
<%@page import="net.simpleframework.sysmgr.dict.SysDict"%>
<%@page import="net.simpleframework.sysmgr.dict.DictUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@page import="net.simpleos.impl.PrjColumns"%><%@page
	import="java.util.Map"%><%@page import="net.simpleos.backend.BackendUtils"%><%@page
	import="net.simpleos.utils.StringsUtils"%>

<%
	Map<String, String> map = SimpleosUtil.attrMap;
	String name = StringsUtils.trimNull(map.get("links.links_linksType"), "sys");
	SysDict sysDict= DictUtils.getSysDictByName("links");
	if(sysDict==null){
		sysDict = new SysDict();
		sysDict.setText("10");
	}
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
		<span class="rred">底部只显示表格的前</span><input name="dict_links" type="text" style="width: 40px;" value="<%=sysDict.getText()%>">个
		<input type="button" class="button2" value="#(Itsite.c.ok)设置"
			onclick="$IT.A('site_dickLinksAct');">
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
	$IT.A('site_linksAct', 'type=links&rs=false&links_linksType=' + t.value);
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