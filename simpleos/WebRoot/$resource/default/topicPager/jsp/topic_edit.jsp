<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="net.simpleframework.content.component.topicpager.TopicPagerUtils"%>
<%@ page
	import="net.simpleframework.content.component.topicpager.ITopicPagerHandle"%>
<%@ page
	import="net.simpleframework.web.page.component.ComponentParameter"%>
<%@ page
	import="net.simpleframework.content.component.topicpager.TopicBean"%>
<%@ page
	import="net.simpleframework.organization.account.AccountContext"%><%@page import="net.simpleos.SimpleosUtil"%>

<%
	final ComponentParameter nComponentParameter = TopicPagerUtils
	.getComponentParameter(request, response);
	final ITopicPagerHandle tHandle = (ITopicPagerHandle) nComponentParameter
	.getComponentHandle();
	final TopicBean topicBean = tHandle
	.getEntityBeanByRequest(nComponentParameter);
	final String idParameterName = tHandle
	.getIdParameterName(nComponentParameter);
	final boolean showValidateCode = (Boolean) nComponentParameter
	.getBeanProperty("showValidateCode");
%>
<div id="topicPropEditor">
	<input type="hidden" id="<%=idParameterName%>"
		name="<%=idParameterName%>"
		value="<%=topicBean != null ? topicBean.getId() : ""%>" />
	<table class="tbl tbl_first" cellspacing="0">
		<tr>
			<td class="lbl">#(topic_edit.0)</td>
			<td><input type="text" id="tp_topic" name="tp_topic" /></td>
		</tr>
	</table>
	<%
		if (tHandle.isShowTags(nComponentParameter)) {
	%><table class="tbl" cellspacing="0">
		<tr>
			<td class="lbl">#(topic_edit.10)</td>
			<td><input type="text" id="tp_keywords" name="tp_keywords" /></td>
		</tr>
	</table>
	<%
		}
	%>
	<table class="tbl" cellspacing="0">
		<tr>
			<td class="lbl">#(topic_edit.1)</td>
			<td style="padding: 3px;">
				<div class="clear_float" style="padding-bottom: 3px;">
					<div style="float: right;"><%=SimpleosUtil.getHtmlEditorToolbar(nComponentParameter, "topic", "tp_content")%></div>
					<div style="float: left" id="tp_content_info" class="important-tip">#(CKEditor.0)</div>
				</div>
				<div>
					<textarea id="tp_content" name="tp_content" style="display: none;"></textarea>
				</div>
			</td>
		</tr>
	</table>
	<table class="tbl" cellspacing="0">
		<tr>
			<td class="lbl">
				<%
					if (showValidateCode) {
						out.write("#(topic_edit.3)");
					}
				%>
			</td>
			<td>
				<div class="clear_float" style="padding: 4px;">
					<div style="float: right;">
						<div>
							<%
								if (topicBean == null) {
							%>
							<input type="checkbox" id="tp_attention" name="tp_attention"
								value="true" /><label for="tp_attention">#(topic_edit.4)</label>
							<%
								}
								if (topicBean == null
										|| TopicPagerUtils.getVote(nComponentParameter,
												topicBean.getId()) == null) {
							%>
							<input type="checkbox" id="tp_vote" name="tp_vote" value="true" /><label
								for="tp_vote">#(topic_edit.2)</label>
							<%
								}
							%>
						</div>
						<div>
							<input type="checkbox" id="tp_att1" name="tp_att1" value="true" /><label
								for="tp_att1">#(topic_edit.5)</label>
							<%
								if (AccountContext.isRuleEnable()) {
							%>
							<label for="tp_att1">#(topic_edit.9)</label> <input
								style="width: 20px; vertical-align: middle; text-align: center;"
								type="text" id="tp_att3" name="tp_att3" value="0" />
							<%
								}
							%><input type="checkbox" id="tp_att2" name="tp_att2" value="true"
								style="margin-left: 4px;" /><label for="tp_att2">#(topic_edit.6)</label>
						</div>
						<div class="btn">
							<span class="desc">#(topic_edit.7)</span><input type="button"
								class="button2" key="ctrlReturn" id="submitTopicHtmlEditor"
								value="#(Button.Submit)" onclick="$Actions['ajaxSaveTopic']();" />
							<input type="button" value="#(Button.Cancel)"
								onclick="$Actions['topicPagerWindow'].close();" />
						</div>
					</div>
					<%
						if (showValidateCode) {
					%>
					<div style="float: left;" id="topicEditorValidateCode"></div>
					<%
						}
					%>
				</div>
			</td>
		</tr>
	</table>
</div>
<script type="text/javascript">
	(function() {
		window.onbeforeunload = function() {
			return "#(CKEditor.1)";
		};
	})();
</script>