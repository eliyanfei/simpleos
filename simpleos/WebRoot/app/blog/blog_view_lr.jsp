<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="net.simpleframework.web.page.component.ComponentParameter"%>
<%@ page
	import="net.simpleframework.content.component.newspager.NewsPagerUtils"%>
<%@ page import="net.simpleframework.content.blog.BlogUtils"%>
<%@ page import="net.simpleframework.util.DateUtils.TimeDistance"%><%@page
	import="net.itsite.ad.AdUtils"%><%@page import="net.itsite.ad.EAd"%><%@page
	import="net.simpleframework.content.component.newspager.INewsPagerHandle"%><%@page
	import="net.simpleframework.content.blog.Blog"%><%@page
	import="net.a.ItSiteUtil"%>

<%
	final ComponentParameter nComponentParameter = new ComponentParameter(request, response, null);
	nComponentParameter.componentBean = BlogUtils.applicationModule.getComponentBean(nComponentParameter);
	final String blog_layout = BlogUtils.deployPath + "jsp/blog_portal.jsp";
	request.setAttribute("__qs", NewsPagerUtils.queryRelatedNews(nComponentParameter));
%>
<div class="block_layout1">
	<div class="t1 f4">
		<span class="ts">#(my_blog.5)</span>
	</div>
	<div class="c"><jsp:include page="<%=blog_layout%>" flush="true">
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
