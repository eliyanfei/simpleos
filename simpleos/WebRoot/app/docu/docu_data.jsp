<%@page import="net.simpleframework.web.WebUtils"%>
<%@page import="net.simpleframework.web.page.PageRequestResponse"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="net.simpleframework.web.page.component.ui.pager.PagerUtils"%>
<%@ page import="java.util.List"%><%@page
	import="net.simpleframework.util.HTMLBuilder"%><%@page
	import="net.simpleos.module.docu.DocuBean"%><%@page
	import="net.simpleos.module.docu.DocuUtils"%><%@page
	import="net.simpleframework.content.ContentUtils"%><%@page
	import="net.simpleframework.util.StringUtils"%><%@page
	import="net.simpleframework.util.DateUtils"%><%@page
	import="net.simpleframework.util.ConvertUtils"%><%@page
	import="net.simpleos.SimpleosUtil"%>

<%
	final PageRequestResponse requestResponse = new PageRequestResponse(request, response);
%>
<div class="list_layout">
	<%
		final List<?> data = PagerUtils.getPagerList(request);
		if (data == null && data.size() == 0) {
			return;
		}
		for (Object o : data) {
			final DocuBean docuBean = (DocuBean) o;
	%>

	<div class="rrow" style="padding-left: 0px;">
		<table width="100%" cellpadding="0" cellspacing="0">
			<tr>
				<td width="20" valign="top"><img
					alt="<%=docuBean.getExtension()%>"
					src="<%=DocuUtils.getFileImage(requestResponse, docuBean)%>">
				</td>
				<td class="wrapWord"><%=DocuUtils.wrapOpenLink(requestResponse, docuBean)%></td>
				<td align="right" class="nnum" nowrap="nowrap" valign="top"><span
					class="nnum"
					title="<%=docuBean.getRemarks()%>#(Docu.remark)/<%=docuBean.getViews()%>#(Docu.read)/<%=docuBean.getDownCounter()%>#(Docu.down)">(<%=docuBean.getRemarks() + "," + docuBean.getViews() + "," + docuBean.getDownCounter()%>)
				</span> By <%=DateUtils.getRelativeDate(docuBean.getCreateDate())%></td>
			</tr>
		</table>
	</div>
	<%
		}
	%>
</div>
