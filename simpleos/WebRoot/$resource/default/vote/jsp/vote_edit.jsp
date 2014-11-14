<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.content.component.vote.VoteUtils"%>
<%@ page import="net.simpleframework.content.component.vote.Vote"%>
<%@ page import="net.simpleframework.content.component.vote.IVoteHandle"%>
<%@ page
	import="net.simpleframework.content.component.vote.VoteItemGroup"%>
<%@ page import="net.simpleframework.core.ado.IDataObjectQuery"%>
<%@ page import="net.simpleframework.util.StringUtils"%>
<%@ page
	import="net.simpleframework.web.page.component.ComponentParameter"%>
<%
	final ComponentParameter nComponentParameter = VoteUtils.getComponentParameter(request, response);
	final IVoteHandle handle = (IVoteHandle) nComponentParameter
			.getComponentHandle();
	final Vote vote = handle
			.getEntityBeanByRequest(nComponentParameter);
%>
<script type="text/javascript">
	function __vote_form() {
		return $Actions["<%=nComponentParameter.getBeanProperty("name")%>"].selector;
	}
	
	function __vote_edit(n, params) {
		var va = $Actions["ajaxVoteEdit" + n + "Page"];
		va.selector = __vote_form();
		$Actions["voteEdit" + n + "Window"](params);
	}

	function __vote_edit_submit(n) {
		var va = $Actions["ajaxVe" + n + "Save"];
		va.selector = "#ve" + n + 
			"FormEditor, <%=nComponentParameter.getBeanProperty("selector")%>";
		va();
	}
	
	function __vote_delete(params) {
		var da = $Actions["ajaxDeleteItemGroup"];
		da.selector = __vote_form();
		da(params);
	}

	function __vote_group_item(o, params) {
		var ga = $Actions["ajaxGroupItem"];
		ga.selector = __vote_form();
		var w = $Actions["groupItemWindow"];
		w.options.title = o.innerHTML.truncate(20);
		w(params);
	}
	
	function __vote_edit_refresh() {
		var va = $Actions["ajaxVoteEditPage"];
	  va.selector = __vote_form();
		va();
	}
</script>
<div class="vote_edit">
	<div class="ve">
		<table style="width: 100%;">
			<tr>
				<td><%=StringUtils.blank(vote.getText())%></td>
				<td width="50px;" align="right"><input type="button"
					value="#(vote_edit.0)" onclick="__vote_edit(1);" />
				</td>
			</tr>
		</table>
	</div>
	<div class="ve">
		<div style="text-align: right;">
			<input type="button" value="#(vote_edit.2)" onclick="__vote_edit(2);" />
		</div>
		<%
			IDataObjectQuery<VoteItemGroup> dataQuery = handle.getItemGroups(
					nComponentParameter, vote);
			VoteItemGroup itemGroup;
			while ((itemGroup = dataQuery.next()) != null) {
				Object id = itemGroup.getId();
		%><div class="ig">
			<table style="width: 100%;">
				<tr>
					<td><%=itemGroup.getText()%>
						<div style="margin-top: 2px;">
							<input type="button" value="#(vote_edit.4)"
								onclick="__vote_group_item(this, '<%=VoteUtils.VOTE_GROUP_ID%>=<%=id%>');" />
						</div>
					</td>
					<td align="right" width="20">
						<div class="edit_image"
							onclick="__vote_edit(2, '<%=VoteUtils.VOTE_GROUP_ID%>=<%=id%>');"></div>
					</td>
					<td align="right" width="20">
						<div class="delete_image"
							onclick="__vote_delete('<%=VoteUtils.VOTE_GROUP_ID%>=<%=id%>');"></div>
					</td>
				</tr>
			</table>
		</div>
		<%
			}
		%>
	</div>
	<div class="vb">
		<table style="width: 100%;">
			<tr>
				<td></td>
				<td align="right"><input type="button" value="#(Button.Cancel)"
					onclick="$Actions['voteEditWindow'].close()" /></td>
			</tr>
		</table>
	</div>
</div>