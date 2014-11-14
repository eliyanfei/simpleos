<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="net.simpleframework.content.component.remark.RemarkItem"%>
<%@ page import="net.simpleframework.core.ado.IDataObjectQuery"%>
<%@ page import="net.simpleframework.content.blog.BlogUtils"%>
<%@ page
	import="net.simpleframework.web.page.component.ComponentParameter"%>
<%@ page import="net.simpleframework.core.id.ID"%>
<%@ page import="net.simpleframework.content.ContentUtils"%>
<%@ page import="net.simpleframework.content.blog.Blog"%>
<%@ page import="net.simpleframework.content.ContentLayoutUtils"%>
<%@ page
	import="net.simpleframework.content.component.newspager.INewsPagerHandle"%>
<%
	final ComponentParameter nComponentParameter = new ComponentParameter(request, response, null);
	nComponentParameter.componentBean = BlogUtils.applicationModule.getComponentBean(nComponentParameter);
	final IDataObjectQuery<?> qs = BlogUtils.queryRemarks(nComponentParameter);
%>
<div class="list_layout">
	<%
		final INewsPagerHandle nHandle = (INewsPagerHandle) nComponentParameter.getComponentHandle();
		RemarkItem remark;
		while ((remark = (RemarkItem) qs.next()) != null) {
			final Blog blog = nHandle.getEntityBeanById(nComponentParameter, remark.getDocumentId());
			if (blog == null) {
				continue;
			}
	%>
	<div class="rrow">
		<%=nHandle.wrapOpenLink(nComponentParameter, blog)%>
		<div class="gray-color"><%=ContentUtils.getShortContent(remark, 50, false)%></div>
		<%
			out.write(ContentLayoutUtils.layoutTime(nComponentParameter, remark, !ID.Utils.isNone(remark.getUserId())));
		%>
	</div>
	<%
		}
	%>
</div>