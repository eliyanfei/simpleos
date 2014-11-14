<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<%@ page
  import="net.simpleframework.content.IContentPagerHandle"%>
<%@ page import="net.simpleframework.applets.tag.ITagApplicationModule"%>
<%
	final StringBuilder params = new StringBuilder();
	params.append(ITagApplicationModule._CATALOG_ID)
			.append("=")
			.append(request
					.getParameter(ITagApplicationModule._CATALOG_ID))
			.append("&").append(IContentPagerHandle._VTYPE).append("=")
			.append(request.getParameter(IContentPagerHandle._VTYPE));
%>
<div class="simple_toolbar">
  <table style="width: 100%;" cellpadding="0" cellspacing="0">
    <tr>
      <td><input type="text" id="_tagsSearch" name="_tagsSearch"
        style="width: 150px;" /><span style="margin-left: 6px;"
        class="important-tip">#(tags_manager.4)</span></td>
      <td align="right" style="padding-right: 11px;"><a
        onclick="$Actions['ajaxRebuildTags']('<%=params.toString()%>');">#(tags_manager.7)</a></td>
    </tr>
  </table>
</div>
<div id="_tagsTablePager"></div>
<script type="text/javascript">
	(function() {
		var txt = $("_tagsSearch");
		$Comp.addBackgroundTitle(txt, "#(tags_manager.5)");
		$Comp.addReturnEvent(txt, function(ev) {
			$Actions["_tagsTablePager"]("_tagsSearch=" + $F(txt));
		});
	})();
</script>