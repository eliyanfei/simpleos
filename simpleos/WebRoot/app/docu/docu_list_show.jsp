<%@page import="net.simpleframework.content.news.NewsUtils"%>
<%@page
	import="net.simpleframework.web.page.component.ComponentParameter"%>
<%@page import="net.simpleframework.web.page.PageRequestResponse"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@page
	import="net.simpleframework.core.ado.IDataObjectQuery"%><%@page
	import="net.itsite.docu.DocuUtils"%><%@page
	import="net.itsite.docu.DocuBean"%><%@page
	import="net.simpleframework.util.DateUtils"%>

<%
	final PageRequestResponse requestResponse = new PageRequestResponse(request, response);
	final String type = request.getParameter("type");
	final IDataObjectQuery<?> qs = DocuUtils.queryDocu(requestResponse, null, "all", type);
	qs.setCount(8);
%>
<div class="list_layout">
	<%
		DocuBean docuBean;
		while ((docuBean = (DocuBean) qs.next()) != null) {
	%>
	<div class="rrow" style="padding-left: 0px;">
		<table width="100%" cellpadding="0" cellspacing="0">
			<tr>
				<td width="20" valign="top">
					<img alt="<%=docuBean.getExtension()%>"
						src="<%=DocuUtils.getFileImage(requestResponse, docuBean)%>">
				</td>
				<td class="wrapWord"><%=DocuUtils.wrapOpenLink(requestResponse, docuBean)%></td>
				<td align="right" class="nnum" nowrap="nowrap" valign="top">
					<%
						if ("new".equals(type)) {
								out.println(DateUtils.getRelativeDate(docuBean.getCreateDate()));
							} else if ("view".equals(type)) {
								out.println("<span title='阅读' class='nnum'>" + docuBean.getViews() + "</span>");
							}
					%>
				</td>
			</tr>
		</table>
	</div>
	<%
		}
	%>
</div>