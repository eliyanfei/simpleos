<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.content.blog.BlogUtils"%><%@page
	import="net.itsite.ItSiteUtil"%><%@page
	import="net.simpleframework.web.page.component.ComponentParameter"%><%@page
	import="net.simpleframework.content.component.newspager.NewsPagerUtils"%>

<%
	final String blog_view_c = BlogUtils.deployPath + "jsp/blog_view_c.jsp";
	final String blog_layout = BlogUtils.deployPath + "jsp/blog_portal.jsp";
	final ComponentParameter nComponentParameter = new ComponentParameter(request, response, null);
	nComponentParameter.componentBean = BlogUtils.applicationModule.getComponentBean(nComponentParameter);
	request.setAttribute("__qs", NewsPagerUtils.queryRelatedNews(nComponentParameter));
%>
<table width="100%">
	<tr>
		<td valign="top">
			<jsp:include page="<%=blog_view_c%>" flush="true"></jsp:include>
			<jsp:include page="/simpleos/commons/ad/ad_bar_content.jsp"></jsp:include>
		</td>
		<td width="264" valign="top">
			<div class="block_layout1">
				<div class="t1 f4">
					<span class="ts">#(my_blog.5)</span>
				</div>
				<div class="c"><jsp:include page="<%=blog_layout%>"
						flush="true">
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
			<jsp:include page="/simpleos/commons/ad/ad_bar_right.jsp"></jsp:include>
		</td>
	</tr>
</table>
<script type="text/javascript">
(function() {
	$Comp.fixMaxWidth(".newspager_template .inherit_c", 680);
})();
</script>
