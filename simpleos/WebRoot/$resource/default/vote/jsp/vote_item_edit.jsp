<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.content.component.vote.VoteUtils"%>
<%@ page import="net.simpleframework.content.component.vote.IVoteHandle"%>
<%@ page import="net.simpleframework.content.component.vote.VoteItemGroup"%>
<%@ page import="net.simpleframework.content.component.vote.VoteItem"%>
<%@ page import="net.simpleframework.core.ado.IDataObjectQuery"%>
<%@ page import="net.simpleframework.web.page.component.ComponentParameter"%>
<%
	final	ComponentParameter nComponentParameter = VoteUtils.getComponentParameter(request, response);
	final IVoteHandle handle = (IVoteHandle) nComponentParameter
			.getComponentHandle();
	final VoteItemGroup itemGroup = handle.getEntityBeanById(nComponentParameter,
			request.getParameter(VoteUtils.VOTE_GROUP_ID), VoteItemGroup.class);
%>
<script type="text/javascript">
	function __vote_item_refresh() {
		var ga = $Actions["ajaxGroupItem"];
		ga.selector = __vote_form();
		ga("<%=VoteUtils.VOTE_GROUP_ID%>=<%=itemGroup.getId()%>");
	}

	function __vote_item_delete(params) {
		var da = $Actions["ajaxDeleteItem"];
		da.selector = __vote_form();
		da(params);
	}
</script>
<div class="vote_edit">
<form></form>
<div class="ve">
<div style="text-align: right;"><input type="button" value="#(Add)"
	onclick="__vote_edit(3, '<%=VoteUtils.VOTE_GROUP_ID%>=<%=itemGroup.getId()%>');" /> <input type="button"
	value="#(Button.Cancel)" onclick="$Actions['groupItemWindow'].close();" /></div>
<%
	final IDataObjectQuery<VoteItem> items = handle.getVoteItems(nComponentParameter, itemGroup);
	VoteItem voteItem;
	while ((voteItem = items.next()) != null) {
%>
<div class="ig">
<table style="width: 100%;" cellpadding="0" cellspacing="0">
	<tr>
		<td><%=voteItem.getText()%></td>
		<td align="right" width="20">
		<div class="edit_image"
			onclick="__vote_edit(3, '<%=VoteUtils.VOTE_GROUP_ID%>=<%=itemGroup.getId()%>&<%=VoteUtils.VOTE_ITEM_ID%>=<%=voteItem.getId()%>');"></div>
		</td>
		<td align="right" width="20">
		<div class="delete_image"
			onclick="__vote_item_delete('<%=VoteUtils.VOTE_ITEM_ID%>=<%=voteItem.getId()%>');"></div>
		</td>
	</tr>
</table>
</div>
<%
	}
%>
</div>
</div>