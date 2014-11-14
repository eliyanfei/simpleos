<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.content.bbs.BbsUtils"%>
<%@ page import="net.simpleframework.web.page.component.ComponentParameter"%>
<%@ page import="net.simpleframework.ado.db.IQueryEntitySet"%>
<%@ page import="net.simpleframework.content.bbs.BbsTopic"%>
<%@ page import="net.simpleframework.content.component.topicpager.ITopicPagerHandle"%>
<%@ page import="net.simpleframework.util.ConvertUtils"%>
<%@ page import="net.simpleframework.content.ContentUtils"%>
<%@ page import="net.simpleframework.content.component.topicpager.PostsTextBean"%>
<%@ page import="net.simpleframework.my.space.MySpaceUtils"%>
<%@ page import="net.simpleframework.organization.account.IAccount"%>
<%@ page import="net.simpleframework.content.component.topicpager.ETopicQuery"%>
<%
	final ComponentParameter nComponentParameter = new ComponentParameter(
			request, response, null);
	nComponentParameter.componentBean = BbsUtils.applicationModule
			.getComponentBean(nComponentParameter);
	final ITopicPagerHandle tHandle = (ITopicPagerHandle) nComponentParameter
			.getComponentHandle();
	final ETopicQuery topicQuery = MySpaceUtils
			.bbsTopicQuery(nComponentParameter);
	final IQueryEntitySet<?> qs = BbsUtils
			.queryTopics(nComponentParameter, null, null, false, null,
					topicQuery, 0);
	qs.setCount(10);
%>
<div class="simple_tabs_content space_tabs_content">
	<div><%=MySpaceUtils.bbsNav(nComponentParameter)%></div>
	<%
		BbsTopic topic;
		while ((topic = (BbsTopic) qs.next()) != null) {
			final PostsTextBean postsText = tHandle.getPostsText(
					nComponentParameter, topic);
	%>
	<div class="space_content_item">
		<div class="f3" style="padding: 8px 0px 4px;"><%=tHandle.wrapOpenLink(nComponentParameter, topic)%></div>
		<table cellpadding="0" cellspacing="0">
			<tr>
				<%
					String img = ContentUtils.getContentImage(nComponentParameter,
								postsText);
						if (img != null) {
							out.write("<td width=\"110\" valign=\"top\">");
							out.write(img);
							out.write("</td>");
						}
				%>
				<td><div class="gray-color"><%=ContentUtils.getShortContent(postsText, 500, true)%></div>
				</td>
			</tr>
		</table>
		<div style="padding-top: 4px;">
			<%=ConvertUtils.toDateString(topic.getCreateDate())%>
		</div>
	</div>
	<%
		}
		final IAccount account = MySpaceUtils.getAccountAware().getAccount(
				nComponentParameter);
		if (account != null) {
	%>
	<p style="text-align: right;">
		<a class="f2 a2"
			href="<%=BbsUtils.applicationModule.getTopicUrl2(
						nComponentParameter, account.user(), topicQuery)%>">#(space_bbs.2)</a>
	</p>
	<%
		}
	%>
</div>