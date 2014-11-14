<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="net.simpleframework.web.page.component.ui.pager.PagerUtils"%>
<%@ page
	import="net.simpleframework.web.page.component.ComponentParameter"%>
<%@ page
	import="net.simpleframework.web.page.component.ui.pager.TablePagerUtils"%>
<%@ page
	import="net.simpleframework.web.page.component.ui.pager.TablePagerColumn"%>
<%@ page import="java.util.Map"%>
<%@ page
	import="net.simpleframework.web.page.component.ui.pager.EExportFileType"%>
<%
	final ComponentParameter nComponentParameter = PagerUtils
			.getComponentParameter(request, response);
	final Map<String, TablePagerColumn> columns = TablePagerUtils
			.getTablePagerData(nComponentParameter)
			.getTablePagerExportColumns();
%>
<div class="tablepager_export">
	<div class="simple_toolbar">
		<div>
			<span style="padding: 0 3px">#(tablepager_export.1)</span> <select
				id="tablepager_export_filetype"><option
					value="<%=EExportFileType.csv.name()%>"><%=EExportFileType.csv%></option>
			</select>
		</div>
		<div id="tablepager_export_pbar"></div>
		<div style="text-align: right;">
			<input type="button" value="#(tablepager_export.2)"
				onclick="do_tablepager_export(this);" />
		</div>
		<div class="f3 lbl">#(tablepager_export.3)</div>
		<div class="all">
			<input type="checkbox" id="col_check_all" /><label
				for="col_check_all">#(tablepager_export.4)</label>
		</div>
		<table style="width: 100%;">
			<%
				int i = 0;
				final StringBuilder sb = new StringBuilder();
				for (TablePagerColumn oCol : columns.values()) {
					String name = oCol.getColumnName();
					i++;
					if (i % 4 == 1) {
						sb.append("<tr>");
					}
					sb.append("<td><input type='checkbox' value='").append(name)
							.append("' id='col_").append(name)
							.append("' checked />");
					sb.append("<label for='col_").append(name).append("'>")
							.append(oCol.getColumnText()).append("</label></td>");
					if (i % 4 == 0) {
						sb.append("</tr>");
					}
				}
				out.write(sb.toString());
			%>
		</table>
	</div>
</div>
<script type="text/javascript">
	(function() {
		$("col_check_all").observe(
				"click",
				function(evt) {
					var all = this;
					all.up(".tablepager_export").select("table input[type=checkbox]")
							.each(function(box) {
								box.checked = all.checked;
							});
				});
	})();

	function do_tablepager_export(o) {
		var act = $Actions["ajaxTablePagerExport"];
		var checked = [];
		o.up(".tablepager_export").select("table input[type=checkbox]").each(
				function(box) {
					if (box.checked) {
						checked.push($F(box));
					}
				});
		act("filetype=" + $F("tablepager_export_filetype") + "&v="
				+ checked.join(";"));
		$Actions["tablepager_export_pbar"].start();
	}
</script>