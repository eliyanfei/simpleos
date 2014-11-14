<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.applets.tag.TagBean"%>
<%@ page import="net.simpleframework.applets.tag.TagUtils"%>
<%@ page import="net.simpleframework.web.page.PageRequestResponse"%>
<%@ page import="net.simpleframework.core.ado.IDataObjectQuery"%>
<%@ page
  import="net.simpleframework.content.IContentPagerHandle"%>
<%@ page import="net.simpleframework.applets.tag.ITagApplicationModule"%>
<%@ page
  import="net.simpleframework.web.page.component.ado.IDbComponentHandle"%>
<%
	final PageRequestResponse requestResponse = new PageRequestResponse(
			request, response);
	final IDataObjectQuery<TagBean> qs = TagUtils
			.layoutTags(requestResponse);
	final StringBuilder params = new StringBuilder();
	params.append(ITagApplicationModule._CATALOG_ID)
			.append("=")
			.append(request
					.getParameter(ITagApplicationModule._CATALOG_ID))
			.append("&").append(IContentPagerHandle._VTYPE).append("=")
			.append(request.getParameter(IContentPagerHandle._VTYPE));
%>
<div class="tag_layout">
  <%
  	int i = -1;
  	TagBean tag;
  	while ((tag = qs.next()) != null) {
  		if (++i == 7) {
  			i = 0;
  		}
  %><a
    title="#(tags_layout.3): <%=tag.getFrequency()%>, #(tags_layout.4): <%=tag.getViews()%>"
    style="font-size: <%=9 + i%>px; font-family: <%=TagUtils.fontWeight[i]%>"
    href="<%=TagUtils.applicationModule.getTagUrl(requestResponse,
						tag)%>"><%=tag.getTagText()%></a>
  <%
  	}
  %><a class="a2"
    onclick="$Actions['tagsMoreWindow']('<%=params.toString()%>&rows=0');">#(tags_layout.1)</a>
  <%
  	if (IDbComponentHandle.Utils.isManager(requestResponse,
  			TagUtils.applicationModule.getManager(requestResponse))) {
  %><a class="a2"
    onclick="$Actions['tagsManagerWindow']('<%=params.toString()%>');">#(Edit)</a>
  <%
  	}
  %>
</div>
<style type="text/css">
.tag_layout a {
	margin: 2px 4px;
	display: inline-block;
}
</style>