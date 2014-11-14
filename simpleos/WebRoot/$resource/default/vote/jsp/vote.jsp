<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.util.StringUtils"%>
<%@ page import="net.simpleframework.core.ado.IDataObjectQuery"%>
<%@ page import="net.simpleframework.content.component.vote.VoteUtils"%>
<%@ page import="net.simpleframework.content.component.vote.VoteItemGroup"%>
<%@ page import="net.simpleframework.content.component.vote.VoteItem"%>
<%@ page import="net.simpleframework.content.component.vote.IVoteHandle"%>
<%@ page import="net.simpleframework.content.component.vote.Vote"%>
<%@ page import="net.simpleframework.web.page.component.ComponentRenderUtils"%>
<%@ page import="net.simpleframework.web.page.component.ComponentParameter"%>
<%
	final ComponentParameter nComponentParameter = VoteUtils.getComponentParameter(request, response);
	final IVoteHandle vHandle = (IVoteHandle) nComponentParameter
	.getComponentHandle();
	final Vote vote = vHandle.getVoteByDocumentId(nComponentParameter);
	final String beanId = nComponentParameter.componentBean.hashId();
%>
<div class="vote_c simple_toolbar3">
<%=ComponentRenderUtils.genParameters(nComponentParameter)%>
<div id="vote_<%=beanId%>">
<%
	if (vote != null) {
		final String vt = vote.getText();
		if (StringUtils.hasText(vt)) {
%>
<div class="vt f4"><%=vt%></div>
<%
	}
		final IDataObjectQuery<VoteItemGroup> dataQuery = vHandle.getItemGroups(
				nComponentParameter, vote);
		VoteItemGroup g;
		while ((g = dataQuery.next()) != null) {
%>
<div class="vg">
<input type="hidden" value="<%=g.getMultiple()%>"/>
<div class="vgt"><%=g.getText()%></div>
<%
	final IDataObjectQuery<VoteItem> dataQuery2 = vHandle.getVoteItems(nComponentParameter, g);
		VoteItem vi;
		while ((vi = dataQuery2.next()) != null) {
%>
<div class="vi">
<table>
	<tr>
		<td><input id="vi_<%=vi.getId()%>" onclick="__vote_cb_click(this);" 
			name="__vg_<%=g.getId()%>" type="<%=(g.getMultiple() == (short) 1) ? "radio"
								: "checkbox"%>" 
			value="<%=vi.getId()%>"/></td>
		<td><label for="vi_<%=vi.getId()%>" 
			onclick="__vote_cb_click(this.up().previous().down());"><%=vi.getText()%></label></td>
	</tr>
</table>
</div>
<%
	}
%>
</div>
<%
	}
%>
</div>
<div class="vb">
<table style="width: 100%;" cellpadding="0" cellspacing="0">
	<tr>
		<td><%=vHandle.getManagerToolbar(nComponentParameter)%></td>
		<td align="right">
			<input type="button" class="button2" value="#(Button.Submit)" onclick="__vote_action(this)._submit();"> 
			<input type="button" value="#(vote.1)" onclick="__vote_action(this)._view();">
		</td>
	</tr>
</table>
</div>
<%
	} else {
%><div class="vb">
<input type="button" value="#(vote.6)" onclick="__vote_action(this)._create();"/></div>
<%
	}
%>
</div>
<script type="text/javascript">
	var VOTE_CLICK_MSG = "#(vote.4)";
	
	$ready(function() {
		var action = $Actions["<%=nComponentParameter.getBeanProperty("name")%>"];
		action.selector = 
			"<%=nComponentParameter.getBeanProperty("selector")%>";
		__vote_addMethods(action, "<%=beanId%>");
		action.vote = $("vote_<%=beanId%>").up();
		action.vote.action = action;
	});
</script>