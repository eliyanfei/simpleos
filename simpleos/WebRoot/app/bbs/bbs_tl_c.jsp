<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@page
	import="net.simpleframework.content.bbs.BbsUtils"%><%@page
	import="net.simpleframework.web.page.PageRequestResponse"%><%@page
	import="net.itsite.ItSiteUtil"%>
<%
	final String bbs_topic_view = BbsUtils.deployPath + "jsp/bbs_topic_view.jsp";
	final String forumId = request.getParameter("forumId");
	ItSiteUtil.addMenuNav(request.getSession(), "/bbs.html", "#(Itsite.menu.bbs)", false);
	final PageRequestResponse requestResponse = new PageRequestResponse(request, response);
	final String bbs_layout = BbsUtils.deployPath + "jsp/bbs_portal.jsp";
%>
<table width="100%">
	<tr>
		<td valign="top">
			<jsp:include page="<%=bbs_topic_view%>" flush="true"></jsp:include>
		</td>
		<td width="264" valign="top">
			<div class="block_layout1">
				<div class="t1 f4">
					<span class="ts">#(Itsite.bbs.2)</span>
				</div>
				<div class="c">
					<%
						request.setAttribute("__qs", BbsUtils.queryTopics(requestResponse, forumId, null, true, null, null, 0));
					%>
					<jsp:include page="<%=bbs_layout%>" flush="true">
						<jsp:param value="false" name="_show_tabs" />
					</jsp:include></div>
			</div>
			<div class="block_layout1">
				<div class="t1 f4">
					<span class="ts">#(App.Bbs.0)</span>
					<%=ItSiteUtil.getTabList("hotPageletLoad", "_tab_param=2&forumId")%>
				</div>
				<div class="c" id="hot_tabs"></div>
			</div>
			<div class="block_layout1">
				<div class="t1 f4">
					<span class="ts">#(App.Bbs.1)</span>
					<%=ItSiteUtil.getTabList("commentsPageletLoad", "_tab_param=5")%>
				</div>
				<div class="c" id="comments_tabs"></div>
			</div>
			<jsp:include page="/simpleos/commons/ad/ad_bar_right.jsp"></jsp:include>
		</td>
	</tr>
</table>