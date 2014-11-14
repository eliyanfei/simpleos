<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page
	import="net.simpleframework.content.component.topicpager.ITopicPagerHandle"%>
<%@ page
	import="net.simpleframework.content.component.topicpager.TopicBean"%>
<%@ page
	import="net.simpleframework.content.component.topicpager.PostsBean"%>
<%@ page
	import="net.simpleframework.content.component.topicpager.PostsTextBean"%>
<%@ page
	import="net.simpleframework.content.component.topicpager.TopicPagerUtils"%>
<%@ page import="net.simpleframework.util.StringUtils"%>
<%@ page import="net.simpleframework.util.ConvertUtils"%>
<%@ page
	import="net.simpleframework.web.page.component.ui.pager.PagerUtils"%>
<%@ page import="net.simpleframework.organization.OrgUtils"%>
<%@ page import="net.simpleframework.core.bean.IIdBeanAware"%>
<%@ page import="net.simpleframework.util.HTMLBuilder"%>
<%@ page
	import="net.simpleframework.web.page.component.ComponentParameter"%>
<%@ page import="net.simpleframework.web.WebUtils"%>
<%
	final ComponentParameter nComponentParameter = TopicPagerUtils.getComponentParameter(request, response);
	final ITopicPagerHandle tHandle = (ITopicPagerHandle) nComponentParameter.getComponentHandle();
	final TopicBean topic = tHandle.getEntityBeanByRequest(nComponentParameter);
	if (topic == null) {
		return;
	}
%>
<div class="cl">
	<%
		final Object fo = request.getAttribute("firstPost");
		final Object lo = request.getAttribute("lastPost");
		if (fo instanceof IIdBeanAware && lo instanceof IIdBeanAware) {
			out.write(HTMLBuilder.inputHidden("firstPost", ((IIdBeanAware) fo).getId()));
			out.write(HTMLBuilder.inputHidden("lastPost", ((IIdBeanAware) lo).getId()));
		}
	%>
	<table cellpadding="2" cellspacing="2" style="width: 100%;"
		class="fixed_table">
		<tr>
			<th width="20%" class="head">
				<div class="views">
					<span>#(topic_view_pager.7)</span>
					<label><%=topic.getViews()%></label><%=HTMLBuilder.SEP%><span>#(topic_view_pager.8)</span>
					<label><%=topic.getReplies()%></label>
				</div>
			</th>
			<th width="80%" class="head">
				<label>
					#(topic_edit.1)
				</label>
				<div class="bb" id="act_<%=topic.getId()%>"><%=StringUtils.blank(tHandle.getActionsHTML(nComponentParameter, topic))%></div>
			</th>
		</tr>
		<%
			final List<?> posts = PagerUtils.getPagerList(request);
			WebUtils.updateViews(nComponentParameter.getSession(), tHandle.getTableEntityManager(nComponentParameter), topic);
			for (int i = 0; i < posts.size(); i++) {
				final PostsBean postsBean = (PostsBean) posts.get(i);
				final PostsTextBean postsText = tHandle.getPostsText(nComponentParameter, postsBean);
		%>
		<tr class="<%=(i > 0) ? "postbody" : ""%>">
			<td class="t1" align="center"><%=ConvertUtils.toDateString(postsBean.getCreateDate())%></td>
			<td class="t1">
				<table style="width: 100%;" cellpadding="0" cellspacing="0"
					id="post<%=postsBean.getId()%>">
					<tr>
						<td class="<%=(i == 0) ? "subject2" : "subject"%>"><%=StringUtils.text(postsText != null ? postsText.getSubject() : null, topic.getTopic())%></td>
						<td align="right" width="180px;"><%=tHandle.getPostActions(nComponentParameter, postsBean)%></td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td class="t2">
				<div class="uv"><%=TopicPagerUtils.getPostUserHTML(nComponentParameter, postsBean)%></div>
			</td>
			<td class="t3"><%=TopicPagerUtils.getPostTextHTML(nComponentParameter, topic, postsBean, postsText)%></td>
		</tr>
		<%
			}
		%>
	</table>
</div>
<script type="text/javascript">
Object.extend(POST_UTILS, {
		userIdParameterName : "<%=OrgUtils.um().getUserIdParameterName()%>",
		viewUrl : "<%=StringUtils.blank(tHandle.getPostViewUrl(nComponentParameter, topic))%>",
		topicUrl : "<%=tHandle.getTopicUrl(nComponentParameter)%>"
	});
</script>
