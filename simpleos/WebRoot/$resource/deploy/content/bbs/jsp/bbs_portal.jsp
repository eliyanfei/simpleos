<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.util.ConvertUtils"%>
<%@ page
	import="net.simpleframework.web.page.component.ComponentParameter"%>
<%@ page import="net.simpleframework.content.bbs.BbsUtils"%>
<%@ page import="net.simpleframework.content.bbs.BbsTopic"%>
<%@ page import="net.simpleframework.core.ado.IDataObjectQuery"%>
<%@ page
	import="net.simpleframework.content.component.topicpager.ITopicPagerHandle"%>
<%@ page import="net.simpleframework.content.bbs.BbsForum"%>
<%@ page import="net.simpleframework.content.ContentLayoutUtils"%>
<%
	final ComponentParameter nComponentParameter = new ComponentParameter(request, response, null);
	final IDataObjectQuery<?> qs = ContentLayoutUtils.getQueryByRequest(nComponentParameter);
	if (qs == null) {
		return;
	}
	nComponentParameter.componentBean = BbsUtils.applicationModule.getComponentBean(nComponentParameter);
	if (nComponentParameter.componentBean == null) {
%>
<div style="text-align: center; padding: 8px;">
	#(bbs_layout.0)
</div>
<%
	return;
	}
	final ITopicPagerHandle tHadle = (ITopicPagerHandle) nComponentParameter.getComponentHandle();
	final boolean mytopic = ConvertUtils.toInt(request.getParameter("btype"), 0) == 2;
	final String[] tabs = { "最新", "精华", "热点", "活跃" };
	final boolean _show_tabs = ConvertUtils.toBoolean(request.getParameter("_show_tabs"), true);
	if (_show_tabs) {
%>
<div class="tabs" style="position: absolute; right: 0px;">
	<%
		int i = 0;
			int ta = ConvertUtils.toInt(request.getParameter("_tab_param"), 0);
			for (String tab : tabs) {
	%>
	<span class="tab <%=i == ta ? "active" : ""%>"
		onclick="$IT.togglePagelet(this,'_tab_param=<%=i++%>');"><%=tab%></span>
	<%
		}
	%>
</div>
<%
	}
%>
<div class="list_layout">
	<%
		BbsTopic topic;
		int i = 1;
		while ((topic = (BbsTopic) qs.next()) != null) {
	%>
	<div class="rrow">
		<div class="ti numDot">
			<span class="sdesc"
				title="<%=topic.getReplies()%>#(bbs_layout.2), <%=topic.getViews()%>#(bbs_layout.3)"><%=tHadle.isReplyNew(nComponentParameter, topic) ? "<span class=\"nnum\" style=\"color: red;\">" + topic.getReplies()
						+ "</span>" : topic.getReplies()%>/<%=topic.getViews()%></span>
			<span class="n <%=(i > 3 ? "n2" : "")%>"><%=i++%></span>
			<%=tHadle.wrapOpenLink(nComponentParameter, topic)%>
		</div>
	</div>
	<%
		}
	%>
</div>