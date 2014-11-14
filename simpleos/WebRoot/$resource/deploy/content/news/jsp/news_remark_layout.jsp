<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.content.news.NewsUtils"%>
<%@ page
	import="net.simpleframework.web.page.component.ComponentParameter"%>
<%@ page import="net.simpleframework.content.ContentLayoutUtils"%>
<%@ page import="net.simpleframework.content.ContentUtils"%>
<%@ page import="net.simpleframework.content.news.News"%>
<%@ page
	import="net.simpleframework.content.component.newspager.INewsPagerHandle"%>
<%@ page import="net.simpleframework.core.id.ID"%>
<%@ page import="net.simpleframework.core.ado.IDataObjectQuery"%>
<%@ page
	import="net.simpleframework.content.component.remark.RemarkItem"%>
<%
	final ComponentParameter nComponentParameter = new ComponentParameter(request, response, null);
	nComponentParameter.componentBean = NewsUtils.applicationModule.getComponentBean(nComponentParameter);
	final IDataObjectQuery<?> qs = NewsUtils.queryRemarks(nComponentParameter);
%>
<div class="list_layout">
	<%
		final INewsPagerHandle nHandle = (INewsPagerHandle) nComponentParameter.getComponentHandle();
		RemarkItem remark;
		while ((remark = (RemarkItem) qs.next()) != null) {
			final News news = nHandle.getEntityBeanById(nComponentParameter, remark.getDocumentId());
			if (news == null) {
				continue;
			}
	%>
	<div class="rrow">
		<%=nHandle.wrapOpenLink(nComponentParameter, news)%>
		<div class="gray-color"><%=ContentUtils.getShortContent(remark, 50, false)%></div>
		<%
			out.write(ContentLayoutUtils.layoutTime(nComponentParameter, remark, !ID.Utils.isNone(remark.getUserId())));
		%>
	</div>
	<%
		}
	%>
</div>