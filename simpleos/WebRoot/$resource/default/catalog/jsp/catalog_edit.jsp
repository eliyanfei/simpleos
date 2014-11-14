<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="net.simpleframework.content.component.catalog.CatalogUtils"%>
<%@ page import="net.simpleframework.util.StringUtils"%>
<%@ page
	import="net.simpleframework.content.component.catalog.ICatalogHandle"%>
<%@ page
	import="net.simpleframework.web.page.component.AbstractComponentBean"%>
<%@ page
	import="net.simpleframework.web.page.component.ComponentParameter"%>
<%
	final ComponentParameter nComponentParameter = CatalogUtils.getComponentParameter(request, response);
	final String catalogId = nComponentParameter.componentBean.hashId();
	final ICatalogHandle cHandle = (ICatalogHandle) nComponentParameter.getComponentHandle();
	final String idParameterName = cHandle.getIdParameterName(nComponentParameter);
%>
<script type="text/javascript">
function __catalog_item_save(params) {
	var ca = $Actions["ajaxCatalogSave"];
	ca.selector = "#<%=AbstractComponentBean.FORM_PREFIX + catalogId%>, #__catalog_itemForm";
	ca(params);
}

function __catalog_parent_select() {
	var ca = $Actions["dictCatalogTree"];
	ca.selector = "#<%=AbstractComponentBean.FORM_PREFIX + catalogId%>";
	ca();
}
</script>
<div style="margin-bottom: 4px;">
	<table cellpadding="0" cellspacing="0" style="width: 100%;">
		<tr>
			<td align="left">
				<input type="button" value="#(Button.Save2)" id="__catalog_itemBtn2"
					onclick="__catalog_item_save('next=true');" />
			</td>
			<td align="right">
				<input type="button" class="button2" value="#(Button.Save)"
					id="__catalog_itemBtn" onclick="__catalog_item_save();" />
				<input type="button" value="#(Button.Cancel)"
					onclick="$Actions['catalogWindow'].close();" />
			</td>
		</tr>
	</table>
</div>
<form id="__catalog_itemForm">
	<input type="hidden" name="<%=idParameterName%>"
		id="<%=idParameterName%>"
		value="<%=StringUtils.blank(request.getParameter(idParameterName))%>" />
</form>
<%
	if (cHandle.showHtml()) {
%>
<script type="text/javascript">
$ready(function() {
	$IT.A('catalog_description');
});
</script>
<%
	}
%>