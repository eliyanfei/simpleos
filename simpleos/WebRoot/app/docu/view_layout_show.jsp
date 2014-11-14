<%@page import="net.simpleframework.content.news.NewsUtils"%>
<%@page
	import="net.simpleframework.web.page.component.ComponentParameter"%>
<%@page import="net.simpleframework.web.page.PageRequestResponse"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.util.ConvertUtils"%>
<%@ page import="net.simpleframework.content.ContentLayoutUtils"%><%@page
	import="net.simpleframework.core.ado.IDataObjectQuery"%><%@page
	import="net.itsite.document.docu.DocuBean"%><%@page
	import="net.itsite.document.docu.DocuUtils"%>

<%
	final PageRequestResponse requestResponse = new PageRequestResponse(request, response);
	final IDataObjectQuery<?> qs = ContentLayoutUtils.getQueryByRequest(requestResponse);
	if (qs == null)
		return;
	final String docuId = request.getParameter("docuId");
%>
<div class="list_layout">
	<%
		DocuBean docuBean;
		while ((docuBean = (DocuBean) qs.next()) != null) {
			if (docuBean.getId().equals2(docuId)) {
				continue;
			}
	%>
	<div class="rrow" style="padding-left: 0px;">
		<table width="100%" cellpadding="0" cellspacing="0">
			<tr>
				<td width="20" valign="top">
					<img alt="<%=docuBean.getExtension()%>"
						src="<%=DocuUtils.getFileImage(requestResponse, docuBean)%>">
				</td>
				<td class="wrapWord"><%=DocuUtils.wrapOpenLink(requestResponse, docuBean)%></td>
				<td align="right" valign="top">
					<span class="nnum"
						title="<%=docuBean.getDownCounter()%>#(Docu.down)/<%=docuBean.getViews()%>#(Docu.read)"><%=docuBean.getDownCounter()%>/<%=docuBean.getViews()%></span>
				</td>
			</tr>
		</table>
	</div>
	<%
		}
	%>
</div>
