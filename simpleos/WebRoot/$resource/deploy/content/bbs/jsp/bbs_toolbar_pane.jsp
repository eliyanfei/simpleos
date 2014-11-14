<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.content.bbs.BbsUtils"%>
<%@ page import="net.simpleframework.web.page.PageRequestResponse"%>
<%@ page import="net.simpleframework.content.bbs.BbsForum"%>
<%@ page import="net.simpleframework.organization.OrgUtils"%>
<%@ page import="net.simpleframework.util.StringUtils"%>
<%@ page import="net.simpleframework.web.WebUtils"%>
<%@ page import="net.simpleframework.web.IWebApplicationModule"%>
<%
	final PageRequestResponse requestResponse = new PageRequestResponse(
			request, response);
	final BbsForum forum = BbsUtils.getForum(requestResponse);
%>
<div class="bbs_toolbar_pane simple_toolbar2">
	<div class="tbar" style="height: 24px;">
		<div style="float: right;" id="__bbs_topic_sech"></div>
		<div style="float: left;">
			<%
				if (forum != null) {
			%>
			<input type="button" class="button2"
				onclick="$Actions['bbsTopicPager'].add();" value="#(topicpager.0)" />
			<%
				} else {
			%><input type="button" class="button2"
				onclick="$Actions['forumAddSelectDict']();" value="#(topicpager.0)" />
			<%
				}
				if (IWebApplicationModule.Utils.isManager(requestResponse,
						BbsUtils.applicationModule)) {
			%>
			<%
				}
			%>
		</div>
	</div>
	<div style="display: none;" class="sech_pane_params">
		<table style="width: 420px;">
			<tr>
				<td class="l">#(bbs_search_pane.0)</td>
				<td class="v"><input type="text" name="_s_topic" id="_s_topic" />
				</td>
			</tr>
			<tr>
				<td class="l">#(bbs_search_pane.2)</td>
				<td class="v b1"></td>
			</tr>
			<tr>
				<td class="l">#(bbs_search_pane.3)</td>
				<td class="v b2"></td>
			</tr>
			<tr>
				<td class="l">#(bbs_search_pane.1)</td>
				<td class="v"><input type="text" name="_s_author"
					id="_s_author" /></td>
			</tr>
			<tr>
				<td class="l">#(bbs_search_pane.4)</td>
				<td class="v"><a onclick="$Actions['forumInsertDict']();">#(bbs_search_pane.6)</a>
					<div id="_s_catalog">
						<%
							if (forum != null) {
						%>
						<div id="<%=forum.getId()%>">
							<span><%=forum.getText()%></span> <a class="delete_image"
								onclick="this.up().remove();"></a>
						</div>
						<%
							}
						%>
					</div>
				</td>
			</tr>
			<tr>
				<td class="l"></td>
				<td class="v"><input type="button" class="button2"
					value="#(Button.Ok)" onclick="BBS_UTILS.search_submit(this);" /> <input
					type="button" value="#(Button.Cancel)"
					onclick="this.up('.sech_pane_params').$hide();" /></td>
			</tr>
		</table>
	</div>
	<div class="bar">
		<%=BbsUtils.applicationModule.tabs(requestResponse)%>
	</div>
	<div class="bar2"><%=BbsUtils.applicationModule.tabs2(requestResponse)%></div>
</div>
<script type="text/javascript">
	Object.extend(BBS_UTILS, {
		userIdParameterName : "<%=OrgUtils.um().getUserIdParameterName()%>",
		topicUrl : "<%=BbsUtils.applicationModule.getTopicUrl(
					requestResponse, null)%>",
		searchbar_msg : "#(bbs_forum_view.8)"
	});
	(function() {
		BBS_UTILS.search_init();
		
		var sb = BBS_UTILS.create_searchbar();
		$("__bbs_topic_sech").update(sb);
		<%final String c = WebUtils.toLocaleString(request
					.getParameter("c"));
			if (StringUtils.hasText(c)) {%>
		sb.down(".txt").setValue("<%=c%>");
		<%}%>
	})();
</script>
