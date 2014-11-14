<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="net.simpleframework.web.page.component.ui.portal.PortalUtils"%>
<%@ page
	import="net.simpleframework.web.page.component.ui.portal.module.IPortalModuleHandle"%>
<%@ page
	import="net.simpleframework.web.page.component.ui.portal.module.PortalModuleRegistryFactory"%>
<%@ page import="net.simpleframework.web.page.IForward"%>
<%@ page
	import="net.simpleframework.web.page.component.ComponentParameter"%>
<div id="optionEditorForm">
	<input type="hidden" name="<%=PortalUtils.BEAN_ID%>"
		value="<%=request.getParameter(PortalUtils.BEAN_ID)%>" /><input
		type="hidden" name="<%=PortalUtils.PAGELET_ID%>"
		value="<%=request.getParameter(PortalUtils.PAGELET_ID)%>" />
	<%
		final ComponentParameter nComponentParameter = PortalUtils
			.getComponentParameter(request, response);
			final IPortalModuleHandle mh = PortalModuleRegistryFactory
			.getInstance().getModuleHandle(
			PortalUtils.getPageletBean(nComponentParameter));
			if (mh != null) {
		final IForward forward = mh
		.getPageletOptionContent(nComponentParameter);
		if (forward != null) {
			out.write(forward.getResponseText(nComponentParameter));
		}
			}
	%>
</div>