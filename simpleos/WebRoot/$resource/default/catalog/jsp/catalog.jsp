<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="net.simpleframework.content.component.catalog.CatalogUtils"%>
<%@ page
	import="net.simpleframework.content.component.catalog.ICatalogHandle"%>
<%@ page
	import="net.simpleframework.web.page.component.ComponentRenderUtils"%>
<%@ page
	import="net.simpleframework.web.page.component.ComponentParameter"%>
<%
	final ComponentParameter nComponentParameter = CatalogUtils.getComponentParameter(request, response);
	final String beanId = nComponentParameter.componentBean.hashId();
	final ICatalogHandle cHandle = (ICatalogHandle) nComponentParameter.getComponentHandle();
	final String idParameterName = cHandle.getIdParameterName(nComponentParameter);
%>
<div class="catalog">
	<%=ComponentRenderUtils.genParameters(nComponentParameter)%>
	<div id="catalog_<%=beanId%>"></div>
</div>

<script type="text/javascript">
var __catalog_element = function(o) {
	if (o) {
		o = $target(o);
		return o.hasClassName("catalog") ? o : o.up(".catalog");
	} else {
		return $("catalog_<%=beanId%>").up();
	}
};

var __catalog_action = function(o) {
	return __catalog_element(o).action;
};

$ready(function() {
		var action = $Actions["<%=nComponentParameter.componentBean.getName()%>"];
		action.selector = 
			"<%=nComponentParameter.getBeanProperty("selector")%>";
		__catalog_addMethods(action, "<%=idParameterName%>", "<%=beanId%>");
		__catalog_element().action = action;
	});
</script>