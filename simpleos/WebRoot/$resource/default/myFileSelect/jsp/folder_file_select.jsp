<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="net.simpleframework.my.file.component.fileselect.FileSelectUtils"%>
<%@ page
	import="net.simpleframework.web.page.component.ui.dictionary.DictionaryUtils"%>
<%@ page
	import="net.simpleframework.web.page.component.ComponentParameter"%><%@page import="net.simpleframework.util.ConvertUtils"%>

<%
	ComponentParameter nComponentParameter = FileSelectUtils
			.getComponentParameter(request, response);
	final boolean ud = ConvertUtils.toBoolean(request.getParameter("ud"),false);
	final String refId = request.getParameter("refId");
	final String te = request.getParameter("te");
	final String beanId = nComponentParameter.componentBean.hashId();
%>
<div class="folder_file_select">
	<div class="simple_toolbar3 clear_float">
		<div style="float: right;"></div>
		<div style="float: left;">
			<%if(!ud){ %>
			<input type="button" value="#(My.folder_file_select.1)"
				onclick="$Actions['__my_folder_select__dict']();" /> <%} %><input
				type="button" value="#(My.folder_file_select.2)"
				onclick="$Actions['__my_folderfile_select'].add('refId=<%=refId %>&te=<%=te %>');" />
		</div>
	</div>
	<div id="__my_folderfile_select_<%=beanId%>" class="list"></div>
	<%if(!ud){ %>
	<div class="tb2">
		<span class="tip">#(My.folder_file_select.3)</span> <input
			type="button" onclick="__do_my_folderfile_selected();"
			value="#(My.folder_file_select.4)" />
	<%} %>
	</div>
</div>
<script type="text/javascript">
	function __do_my_folderfile_selected(d) {
		var ref = $Actions["__my_folderfile_select"];
		var selects = new Array();
		var tp = $("__my_folderfile_select_<%=beanId%>");
		var arr = ref.checkCacheArr(tp);
		if (arr.length > 0) {
			arr.each(function(d2) {
				selects.push({
					id   : ref.rowId(d2),
					text : d2.getAttribute("download")
				});
			});
		} else {
			if (d) {
				selects.push({
					id   : ref.rowId(d),
					text : d.getAttribute("download")
				});
			} else {
				return;
			}
		}
		<%=DictionaryUtils.genSelectCallback(nComponentParameter,
					"selects", "<br>", true)%>
	}

	function __myfile_select_pager_loaded() {
		$$("#__my_folderfile_select_<%=beanId%> .titem").each(function(d) {
			d.observe("dblclick", function() {
				__do_my_folderfile_selected(d);
			});
		});
		$Actions["__my_folderfile_select"].checkCache(
				$("__my_folderfile_select_<%=beanId%>"));
	}
	
	$ready(function() {
		var tp = $("__my_folderfile_select_<%=beanId%>");
		var tp_p = tp.previous();
		var w = $Actions['<%=nComponentParameter.componentBean.getName()%>'].window;
		w.content.setStyle("overflow:hidden;");
		var s = function() {
			var h = w.getSize(true).height - (tp_p ? tp_p.getHeight() : 0) - 
						tp.next().getHeight() - 16;
			tp.setStyle('overflow-y:auto;height: ' + h + 'px;');
 		};
 		s();
    w.observe("resize:ended", s);
	});
</script>