<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="net.simpleframework.content.component.topicpager.TopicPagerUtils"%>
<%@ page
	import="net.simpleframework.content.component.topicpager.ITopicPagerHandle"%>
<%@ page
	import="net.simpleframework.content.component.topicpager.TopicBean"%>
<%@ page
	import="net.simpleframework.web.page.component.ComponentParameter"%><%@page
	import="net.simpleos.SimpleosUtil"%><%@page
	import="net.simpleframework.content.bbs.BbsUtils"%><%@page
	import="net.simpleframework.content.bbs.BbsForum"%><%@page
	import="net.simpleframework.web.page.PageRequestResponse"%>

<%
	final ComponentParameter nComponentParameter = TopicPagerUtils.getComponentParameter(request, response);
	final TopicBean topic = ((ITopicPagerHandle) nComponentParameter.getComponentHandle()).getEntityBeanByRequest(nComponentParameter);
	if (topic == null) {
		return;
	}

	final BbsForum forum = BbsUtils.getForum(new PageRequestResponse(request, response));
	if (forum != null) {
		SimpleosUtil.addMenuNav(request.getSession(), "/bbs/tl/" + forum.getId() + ".html", forum.getText(), false);
	}

	SimpleosUtil.addMenuNav(request.getSession(), null, topic.getTopic(), false);
%>
<div class="tp_view">
	<div id="__pager_postsId"></div>
	<div class="fr">
		<table cellpadding="0" cellspacing="0" style="width: 100%;"
			class="fixed_table">
			<tr>
				<td width="32%" class="left"><%=TopicPagerUtils.getFastReplyLeft(nComponentParameter, topic)%></td>
				<td width="68%" class="right"><%=TopicPagerUtils.getFastReplyRight(nComponentParameter, topic)%></td>
			</tr>
		</table>
	</div>
</div>
