<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="net.simpleframework.content.component.topicpager.TopicBean"%>
<%@ page
	import="net.simpleframework.web.page.component.ComponentParameter"%>
<%@ page
	import="net.simpleframework.content.component.topicpager.ITopicPagerHandle"%>
<%@ page import="net.simpleframework.web.page.PageRequestResponse"%>
<%@ page import="net.simpleframework.content.bbs.BbsUtils"%>
<%@ page
	import="net.simpleframework.web.page.component.AbstractComponentBean"%>
<%@ page import="java.util.Map"%>
<%@ page import="net.simpleframework.content.ContentLayoutUtils"%>
<%@ page import="net.simpleframework.web.EFunctionModule"%>
<%
	final Map<?, ?> row = (Map<?, ?>) request
			.getAttribute("attention_row");
	if (row == null) {
		return;
	}
	final PageRequestResponse requestResponse = new PageRequestResponse(
			request, response);
	final AbstractComponentBean componentBean = BbsUtils.applicationModule
			.getComponentBean(requestResponse);
	if (componentBean != null) {
		final ComponentParameter nComponentParameter = ComponentParameter
				.get(requestResponse, componentBean);
		final ITopicPagerHandle tHandle = (ITopicPagerHandle) nComponentParameter
				.getComponentHandle();
		final TopicBean topic = tHandle.getEntityBeanById(
				nComponentParameter, row.get("ATTENTIONID"));
		if (topic != null) {
%>
<div class="rrow"
	style="padding-bottom: 4px; background: url('<%=ContentLayoutUtils.dotImagePath(
							nComponentParameter, BbsUtils.deployPath)%>') 5px 5px no-repeat;">
	<table style="width: 100%;" cellpadding="0" cellspacing="0">
		<tr>
			<td><%=tHandle.wrapOpenLink(nComponentParameter, topic)%></td>
			<td align="right">&nbsp;<a class="a2 nnum"
				onclick="$Actions['attentionUsersWindow']('attentionId=<%=topic.getId()%>&vtype=<%=EFunctionModule.bbs.ordinal()%>');"><%=row.get("C")%></a>&nbsp;</td>
		</tr>
	</table>
</div>
<%
	}
	}
%>
