<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@page import="java.util.Map"%><%@page
	import="net.prj.manager.PrjMgrUtils"%><%@page
	import="net.itniwo.commons.StringsUtils"%>

<%
	Map<String, String> map = PrjMgrUtils.loadCustom("docu");
%>
<div style="padding: 0px 12px;">
	<p>
		<input type="button" onclick="$Actions['docuMgrCatalogWindowAct']();"
			value="#(Docu.mgr.2)" />
	</p>
	<p>
		<input type="button" value="#(Docu.mgr.3)" />
		<span id="span_docuReBuildStatData" class="important-tip"
			style="margin-left: 6px;"></span>
	</p>
	<p>
		<input type="button" value="#(Docu.mgr.4)" />
		<span id="span_docuIndexRebuild" class="important-tip"
			style="margin-left: 6px;"></span>
	</p>
	<p>
		<input type="button" value="#(Docu.mgr.5)" class="button2"
			onclick="$IT.A('docuPathAct','docuPath='+$F('docuPath'));">
		<input type="text"
			value="<%=StringsUtils.trimNull(map.get("docuPath"), "c:\\")%>"
			name="docuPath" id="docuPath">
	</p>
</div>
<script type="text/javascript">
(function() {
	function init(act) {
		var info = $("span_" + act);
		info.previous().observe("click", function() {
			info.innerHTML = "#(manager_tools.7)";
			$Actions[act]();
		});
	}
	init("docuReBuildStatData");
	init("docuIndexRebuild");
})();
</script>