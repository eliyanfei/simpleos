<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="net.simpleframework.content.component.topicpager.TopicPagerUtils"%>
<%@ page import="net.simpleframework.util.StringUtils"%>
<%@ page
	import="net.simpleframework.web.page.component.ComponentParameter"%>
<%@ page
	import="net.simpleframework.content.component.topicpager.ITopicPagerHandle"%>
<%@ page
	import="net.simpleframework.content.component.topicpager.PostsBean"%>
<%@ page
	import="net.simpleframework.organization.account.AccountContext"%>
<%
	final ComponentParameter nComponentParameter = TopicPagerUtils
			.getComponentParameter(request, response);
	final ITopicPagerHandle tHandle = (ITopicPagerHandle) nComponentParameter
			.getComponentHandle();
	final PostsBean postsBean = tHandle.getEntityBeanById(
			nComponentParameter, request.getParameter("postId"),
			PostsBean.class);
	final boolean showValidateCode = (Boolean) nComponentParameter
			.getBeanProperty("showValidateCode");
%>
<div id="topicPropEditor">
	<input type="hidden" name="postId"
		value="<%=StringUtils.blank(request.getParameter("postId"))%>" /> <input
		type="hidden" name="quoteId"
		value="<%=StringUtils.blank(request.getParameter("quoteId"))%>" />
	<table class="tbl tbl_first" cellspacing="0">
		<tr>
			<td class="lbl">#(topic_edit.0)</td>
			<td><input type="text" id="tp_topic" name="tp_topic" />
			</td>
		</tr>
	</table>
	<%
		if (postsBean != null && tHandle.isShowTags(nComponentParameter)) {
	%>
	<table class="tbl" cellspacing="0">
		<tr>
			<td class="lbl">#(topic_edit.10)</td>
			<td><input type="text" id="tp_keywords" name="tp_keywords" />
			</td>
		</tr>
	</table>
	<%
		}
	%>
	<table class="tbl" cellspacing="0">
		<tr>
			<td class="lbl">#(topic_edit.1)</td>
			<td style="padding: 3px;">
				<div class="clear_float" style="padding-bottom: 2px;">
					<div style="float: right;"><%=tHandle.getHtmlEditorToolbar(nComponentParameter)%></div>
					<div style="float: left" id="tp_content_info" class="important-tip">#(CKEditor.0)</div>
				</div>
				<div>
					<textarea id="tp_content" name="tp_content" style="display: none;"></textarea>
				</div></td>
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
						<div>
							<%
								if (postsBean != null && postsBean.isFirstPost()) {
									if (TopicPagerUtils.getVote(nComponentParameter,
											postsBean.getTopicId()) == null) {
							%>
							<input type="checkbox" id="tp_vote" name="tp_vote" value="true" /><label
								for="tp_vote">#(topic_edit.2)</label>
							<%
								}
								}
							%>
						</div>
						<div class="btn">
							<span class="desc">#(topic_edit.7)</span><input type="button"
								class="button2" key="ctrlReturn" id="submitTopicHtmlEditor"
								value="#(Button.Submit)"
								onclick="$Actions['ajaxReplyTopicSave']();" /> <input
								type="button" value="#(Button.Cancel)"
								onclick="$Actions['topicPagerReplyWindow'].close();" />
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