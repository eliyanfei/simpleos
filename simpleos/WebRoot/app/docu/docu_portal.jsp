<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.core.ado.IDataObjectQuery"%>
<%@ page import="net.simpleframework.util.ConvertUtils"%>
<%@ page import="net.simpleframework.organization.account.IAccount"%>
<%@ page import="net.simpleframework.content.ContentLayoutUtils"%>
<%@ page import="net.simpleframework.content.ContentUtils"%><%@page
	import="net.simpleos.module.docu.DocuBean"%><%@page
	import="net.simpleos.module.docu.DocuUtils"%><%@page
	import="net.simpleframework.util.DateUtils"%><%@page
	import="net.simpleframework.web.page.component.ComponentParameter"%><%@page
	import="net.simpleframework.util.LocaleI18n"%>

<%
	final ComponentParameter nComponentParameter = new ComponentParameter(request, response, null);
	final IDataObjectQuery<?> qs = ContentLayoutUtils.getQueryByRequest(nComponentParameter);
	if (qs == null) {
		return;
	}
%>
<%
	final String[] tabs = { LocaleI18n.getMessage("Docu.news"), LocaleI18n.getMessage("Docu.hot"), LocaleI18n.getMessage("Docu.down") };
	final boolean _show_tabs = ConvertUtils.toBoolean(request.getParameter("_show_tabs"), true);
	if (_show_tabs) {
%>
<div class="tabs" style="position: absolute; right: 0px;">
	<%
		int i = 0;
			int ta = ConvertUtils.toInt(request.getParameter("_tab_param"), 0);
			for (String tab : tabs) {
	%>
	<span class="tab <%=i == ta ? "active" : ""%>"
		onclick="$IT.togglePagelet(this,'_tab_param=<%=i++%>');"><%=tab%></span>
	<%
		}
	%>
</div>
<%
	}
%>
<div class="list_layout">
	<%
		DocuBean docuBean;
		int i = 1;
		request.setAttribute("home", true);
		while ((docuBean = (DocuBean) qs.next()) != null) {
	%>
	<div class="rrow" style="padding-left: 0px;">
		<table width="100%" cellpadding="0" cellspacing="0">
			<tr>
				<td width="20" valign="top">
					<img alt="<%=docuBean.getExtension()%>"
						src="<%=DocuUtils.getFileImage(nComponentParameter, docuBean)%>">
				</td>
				<td class="wrapWord"><%=DocuUtils.wrapOpenLink(nComponentParameter, docuBean)%></td>
				<td align="right" class="nnum" nowrap="nowrap" valign="top">
					<span class="nnum"
						title="<%=docuBean.getDownCounter()%>#(Docu.down)/<%=docuBean.getViews()%>#(Docu.read)"><%=docuBean.getDownCounter() + "/" + docuBean.getViews()%></span>
				</td>
			</tr>
		</table>
	</div>
	<%
		}
	%>
</div>