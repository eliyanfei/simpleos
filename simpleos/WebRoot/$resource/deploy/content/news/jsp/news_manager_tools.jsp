<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<div style="padding: 0px 12px;">
	<p>
		<input type="button" onclick="$Actions['newsCatalogWindowAct']();"
			value="目录管理" />
	</p>
	<p>
		<input type="button" value="#(manager_tools.2)" />
		<span id="span_ajaxNewsStatRebuild" class="important-tip"
			style="margin-left: 6px;"></span>
	</p>
	<p>
		<input type="button" value="#(manager_tools.4)" />
		<span id="span_ajaxNewsIndexRebuild" class="important-tip"
			style="margin-left: 6px;"></span>
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
	init("ajaxNewsStatRebuild");
	init("ajaxNewsIndexRebuild");
})();
</script>