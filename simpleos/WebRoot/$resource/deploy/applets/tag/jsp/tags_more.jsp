<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.applets.tag.TagUtils"%>
<%@ page import="net.simpleframework.applets.tag.TagBean"%>
<%@ page import="net.simpleframework.core.ado.IDataObjectQuery"%>
<%@ page import="net.simpleframework.web.page.PageRequestResponse"%>
<%
	final PageRequestResponse requestResponse = new PageRequestResponse(
			request, response);
	final IDataObjectQuery<TagBean> qs = TagUtils
			.layoutTags(requestResponse);
%>
<div class="tag_layout simple_toolbar">
	<%
		TagBean tag;
		while ((tag = qs.next()) != null) {
	%><a title="#(tags_layout.3): <%=tag.getFrequency()%>, #(tags_layout.4): <%=tag.getViews()%>"
		href="<%=TagUtils.applicationModule.getTagUrl(requestResponse,
						tag)%>"><%=tag.getTagText()%></a>
	<%
		}
	%>
</div>