<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.util.DateUtils.TimeDistance"%>
<%@ page import="net.simpleframework.content.EContentType"%>
<%@ page import="net.simpleframework.applets.tag.TagUtils"%>
<%@ page import="net.simpleframework.web.page.PageRequestResponse"%>
<%@ page import="net.simpleframework.content.news.NewsUtils"%>
<%@ page import="net.simpleframework.web.EFunctionModule"%>
<%@page import="net.simpleframework.content.IContentPagerHandle"%><%@page
	import="net.a.ItSiteUtil"%>
<%
	final String news = NewsUtils.deployPath + "jsp/news.jsp";
	final String news_layout = NewsUtils.deployPath + "jsp/news_layout.jsp";
	final String news_remark_layout = NewsUtils.deployPath + "jsp/news_remark_layout.jsp";
	final String tags_params = IContentPagerHandle._VTYPE + "=" + EFunctionModule.news.ordinal();
	final String tags_layout = TagUtils.deployPath + "jsp/tags_layout.jsp?" + tags_params;
	final PageRequestResponse requestResponse = new PageRequestResponse(request, response);
	ItSiteUtil.addMenuNav(request.getSession(), null, "#(Itsite.menu.news)", false);
%>
<table width="100%">
	<tr>
		<td valign="top" class="List_PageletsPage">
			<jsp:include flush="true" page="<%=news%>"><jsp:param
					value="false" name="showHead" /></jsp:include>
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
						request.setAttribute("__qs", NewsUtils.queryRemarks(requestResponse));
					%>
					<jsp:include page="<%=news_remark_layout%>" flush="true">
						<jsp:param value="false" name="_show_tabs" />
					</jsp:include></div>
			</div>
			<jsp:include page="/app/common/ad_bar_right.jsp"></jsp:include>
		</td>
	</tr>
</table>

