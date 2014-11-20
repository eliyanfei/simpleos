<%@page import="net.itsite.ad.EAd"%>
<%@page import="net.itsite.ad.AdUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.content.blog.BlogUtils"%>
<%@ page import="net.simpleframework.web.page.PageRequestResponse"%>
<%@ page import="net.simpleframework.content.EContentType"%>
<%@ page import="net.simpleframework.util.DateUtils.TimeDistance"%>
<%@page import="net.simpleframework.web.EFunctionModule"%>
<%@page import="net.simpleframework.applets.tag.TagUtils"%>
<%@page import="net.simpleframework.content.IContentPagerHandle"%><%@page import="net.itsite.ItSiteUtil"%>

<%
	final String blog_home = BlogUtils.deployPath + "jsp/blog_home.jsp";
	final String blog_layout = BlogUtils.deployPath + "jsp/blog_portal.jsp";
	final String blog_remark_layout = BlogUtils.deployPath + "jsp/blog_remark_layout.jsp";
	final PageRequestResponse requestResponse = new PageRequestResponse(request, response);
	final String tags_params = IContentPagerHandle._VTYPE + "=" + EFunctionModule.blog.ordinal();
	final String tags_layout = TagUtils.deployPath + "jsp/tags_layout.jsp?" + tags_params;
%>
<table width="100%">
	<tr>
		<td valign="top" class="List_PageletsPage">
			<jsp:include page="<%=blog_home%>"></jsp:include>
		</td>
		<td width="264" valign="top">
			<div class="block_layout1">
				<div class="t1 f4">
					<span class="ts">#(App.Blog.0)</span>
					<%=ItSiteUtil.getTabList("hotPageletLoad", "_tab_param=2")%>
				</div>
				<div class="c" id="hot_tabs"></div>
			</div>
			<div class="block_layout1">
				<div class="t1 f4">
					<span class="ts">#(App.Blog.1)</span>
					<%=ItSiteUtil.getTabList("commentsPageletLoad", "_tab_param=5")%>
				</div>
				<div class="c" id="comments_tabs"></div>
			</div>
			<div class="block_layout1">
				<div class="t1 f4">
					<span class="ts">#(App.Blog.2)</span>
				</div>
				<div class="c">
					<%
						request.setAttribute("__qs", BlogUtils.queryRemarks(requestResponse));
					%>
					<jsp:include page="<%=blog_remark_layout%>" flush="true">
						<jsp:param value="false" name="_show_tabs" />
					</jsp:include></div>
			</div>
		</td>
	</tr>
</table>
