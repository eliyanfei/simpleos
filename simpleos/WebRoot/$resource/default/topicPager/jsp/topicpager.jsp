<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="net.simpleframework.web.page.component.ui.pager.TablePagerUtils"%>
<%@ page
	import="net.simpleframework.content.component.topicpager.ITopicPagerHandle"%>
<%@ page
	import="net.simpleframework.web.page.component.ComponentParameter"%>
<%@ page
	import="net.simpleframework.content.component.topicpager.TopicPagerUtils"%>
<%
	final ComponentParameter nComponentParameter = TopicPagerUtils
			.getComponentParameter(request, response);
	final ITopicPagerHandle tHandle = (ITopicPagerHandle) nComponentParameter
			.getComponentHandle();
	final String idParameterName = tHandle
			.getIdParameterName(nComponentParameter);
%>
<%=TablePagerUtils.renderTable(nComponentParameter)%>
<script type="text/javascript">	
	var pager_init_<%=nComponentParameter.componentBean.hashId()%> = function(action) {
		__table_pager_addMethods(action);	
		action.bindMenu("topicPager_Menu");
		__topicpager_addMethods(action, "<%=idParameterName%>");
	};
</script>