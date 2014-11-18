<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.content.news.NewsUtils"%>
<%@ page
	import="net.simpleframework.web.page.component.ComponentParameter"%>
<%@ page import="net.simpleframework.util.DateUtils.TimeDistance"%>
<%@ page
	import="net.simpleframework.content.component.newspager.NewsPagerUtils"%><%@page
	import="net.simpleframework.content.component.newspager.INewsPagerHandle"%><%@page
	import="net.simpleframework.content.component.newspager.NewsBean"%><%@page
	import="net.a.ItSiteUtil"%>
<%
	ItSiteUtil.addMenuNav(request.getSession(), "/news.html", "#(Itsite.menu.news)", false);
	final ComponentParameter nComponentParameter = NewsPagerUtils.getComponentParameter(request, response);
	final String templatePage = NewsUtils.getTemplatePage(nComponentParameter);
	final String news_layout = NewsUtils.deployPath + "jsp/news_layout.jsp";
%>
<table width="100%">
	<tr>
		<td valign="top">
			<jsp:include page="<%=templatePage%>" flush="true"></jsp:include>
		<jsp:include page="/app/common/ad_bar_content.jsp"></jsp:include>
		</td>
		<td width="264" valign="top">
			<div class="block_layout1">
				<div class="t1 f4">
					<span class="ts">#(Itsite.news.3)</span>
				</div>
				<div class="c">
					<%
						request.setAttribute("__qs", NewsPagerUtils.queryRelatedNews(nComponentParameter));
					%>
					<jsp:include page="<%=news_layout%>" flush="true">
						<jsp:param value="false" name="_show_tabs" />
					</jsp:include></div>
			</div>
			<div class="block_layout1">
				<div class="t1 f4">
					<span class="ts">#(App.Blog.0)</span>
					<%=ItSiteUtil.getTabList("hotPageletLoadV", "_tab_param=2")%>
				</div>
				<div class="c" id="hot_tabs"></div>
			</div>
			<div class="block_layout1">
				<div class="t1 f4">
					<span class="ts">#(App.Blog.1)</span>
					<%=ItSiteUtil.getTabList("commentsPageletLoadV", "_tab_param=5")%>
				</div>
				<div class="c" id="comments_tabs"></div>
			</div>
			<jsp:include page="/app/common/ad_bar_right.jsp"></jsp:include>
		</td>
	</tr>
</table>
<script type="text/javascript">
(function() {
	$Comp.fixMaxWidth(".newspager_template .inherit_c", 680);
})();
</script>
