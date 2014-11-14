<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="net.simpleframework.content.component.catalog.CatalogUtils"%>
<%@ page
	import="net.simpleframework.content.component.catalog.ICatalogHandle"%>
<%@ page import="net.simpleframework.core.ado.IDataObjectQuery"%>
<%@ page
	import="net.simpleframework.content.component.catalog.CatalogOwner"%>
<%@ page import="net.simpleframework.organization.EMemberType"%>
<%@ page import="net.simpleframework.organization.impl.JobMember"%>
<%@ page import="net.simpleframework.core.bean.ITextBeanAware"%>
<%@ page import="net.simpleframework.core.id.ID"%>
<%@ page
	import="net.simpleframework.web.page.component.ComponentParameter"%>
<%
	final ComponentParameter nComponentParameter = CatalogUtils
			.getComponentParameter(request, response);
	final ICatalogHandle cHandle = (ICatalogHandle) nComponentParameter
			.getComponentHandle();
	final String idParameterName = cHandle
			.getIdParameterName(nComponentParameter);
	final String catalogId = request.getParameter(idParameterName);
	final IDataObjectQuery<CatalogOwner> doq = cHandle.catalogOwners(
			nComponentParameter, catalogId);
	if (doq == null) {
		return;
	}
%>
<script type="text/javascript">
	function __catalog_owner_user_select(params) {
		var us = $Actions['catalogUserSelect'];
		us.idQuery = 
			"<%=idParameterName + "=" + catalogId%>";
		us(params);
	}
	
	function __catalog_owner_delete(params) {
		var od = $Actions['ajaxCatalogOwnerDelete'];
		var oa = $Actions["ajaxCatalogOwnerPage"];
		od.selector = oa.selector;
		od("<%=idParameterName + "=" + catalogId%>".addParameter(params));
	}

	function __catalog_owner_refresh() {
		$Actions["ajaxCatalogOwnerPage"](
				"<%=idParameterName + "=" + catalogId%>");
	}
</script>
<div class="catalog_owner">
	<table style="width: 100%;" cellpadding="0" cellspacing="0">
		<tr>
			<td><input type="button" value="#(catalog_owner.0)" /> <input
				type="button" value="#(catalog_owner.1)"
				onclick="__catalog_owner_user_select();" />
			</td>
			<td align="right"><input type="button" value="#(Button.Cancel)"
				onclick="$Actions['catalogOwnerWindow'].close();" />
			</td>
		</tr>
	</table>
	<table style="width: 100%;" cellpadding="5" class="oitem">
		<tr>
			<th width="100px;">#(catalog_owner.2)</th>
			<th width="100px;">#(catalog_owner.3)</th>
			<th>&nbsp;</th>
		</tr>
		<%
			CatalogOwner catalogOwner;
			while ((catalogOwner = doq.next()) != null) {
				final EMemberType ownerType = catalogOwner.getOwnerType();
				final ID ownerId = catalogOwner.getOwnerId();
				final ITextBeanAware textBean = (ITextBeanAware) JobMember
						.memberBean(ownerType, ownerId);
		%>
		<tr>
			<td align="center"><%=ownerType%></td>
			<td align="center"><%=textBean != null ? textBean.getText() : ""%></td>
			<td align="left">
				<div class="delete_image"
					onclick="__catalog_owner_delete('ownerType=<%=ownerType%>&ownerId=<%=ownerId%>');"></div>
			</td>
		</tr>
		<%
			}
		%>
	</table>
</div>
