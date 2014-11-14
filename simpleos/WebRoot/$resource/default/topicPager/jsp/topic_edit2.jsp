<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="net.simpleframework.content.component.topicpager.TopicPagerUtils"%>
<%@ page
	import="net.simpleframework.content.component.topicpager.ITopicPagerHandle"%>
<%@ page import="net.simpleframework.util.StringUtils"%>
<%@ page
	import="net.simpleframework.web.page.component.ComponentParameter"%>
<%@ page import="net.simpleframework.content.EContentType"%>
<%
	final ComponentParameter nComponentParameter = TopicPagerUtils
			.getComponentParameter(request, response);
	final ITopicPagerHandle tHandle = (ITopicPagerHandle) nComponentParameter
			.getComponentHandle();
	final String idParameterName = tHandle
			.getIdParameterName(nComponentParameter);
%>
<div class="tp_edit2 simple_toolbar">
	<input type="hidden" name="<%=idParameterName%>"
		value="<%=StringUtils.blank(request.getParameter(idParameterName))%>" />
	<table style="width: 100%;" cellpadding="2" cellspacing="0">
		<tr>
			<td class="l">#(topic_edit2.3)</td>
			<td><select name="tp_type" id="tp_type">
					<option value="<%=EContentType.normal.name()%>"><%=EContentType.normal%></option>
					<option value="<%=EContentType.recommended.name()%>"><%=EContentType.recommended%></option>
			</select></td>
		</tr>
		<tr>
			<td class="l">#(topic_edit2.4)</td>
			<td><select name="tp_star" id="tp_star">
					<option value="0">#(topic_edit2.1)</option>
					<option value="1">1</option>
					<option value="2">2</option>
					<option value="3">3</option>
					<option value="4">4</option>
					<option value="5">5</option>
			</select> <span>#(topic_edit2.0)</span></td>
		</tr>
	</table>
</div>
<div style="text-align: right; margin-top: 6px;">
	<input type="button" class="button2" value="#(Button.Ok)"
		onclick="$Actions['ajaxSaveTopic2']();" /> <input type="button"
		value="#(Button.Cancel)"
		onclick="$Actions['topicEdit2Window'].close();" />
</div>
<style type="text/css">
.tp_edit2 .l {
	width: 70px;
	text-align: right;
}
</style>