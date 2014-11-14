<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.content.component.vote.VoteUtils"%>
<%@ page import="net.simpleframework.content.component.vote.IVoteHandle"%>
<%@ page import="net.simpleframework.content.component.vote.Vote"%>
<%@ page
	import="net.simpleframework.content.component.vote.VoteItemGroup"%>
<%@ page import="net.simpleframework.content.component.vote.VoteItem"%>
<%@ page import="net.simpleframework.core.ado.IDataObjectQuery"%>
<%@ page import="net.simpleframework.util.StringUtils"%>
<%@ page import="net.simpleframework.util.NumberUtils"%>
<%@ page
	import="net.simpleframework.web.page.component.ComponentParameter"%>
<%
	final	ComponentParameter nComponentParameter = VoteUtils.getComponentParameter(request, response);
	final IVoteHandle vh = (IVoteHandle) nComponentParameter.getComponentHandle();
	final Vote vote = vh.getEntityBeanByRequest(nComponentParameter);
%>
<div class="vote_c simple_toolbar3">
	<%
	final IDataObjectQuery<VoteItemGroup> dataQuery = vh.getItemGroups(nComponentParameter, vote);
	VoteItemGroup g;
	while ((g = dataQuery.next()) != null) {
		final String gid = StringUtils.hash(g);
%>
	<div class="vg" id="vg_<%=gid%>">
		<div class="vgt"><%=g.getText()%></div>
		<div class="vi">
			<table style="width: 100%;">
				<%
	final IDataObjectQuery<VoteItem> dataQuery2 = vh.getVoteItems(nComponentParameter, g);
		VoteItem vi;
		int count = 0, i, j = 1;
		while ((vi = dataQuery2.next()) != null) {			
			count += (i = vh.getResults(nComponentParameter, vi.getId()).getCount());
%>
				<tr>
					<td class="vno"><%=j++%>.</td>
					<td><%=vi.getText()%></td>
					<td class="vbar"><div></div> <input type="hidden"
						value="<%=i%>" /></td>
					<td class="vnum"><a
						onclick="$Actions['windowVoteUsersView']('itemId=<%=vi.getId()%>&<%=VoteUtils.BEAN_ID%>=<%=nComponentParameter.componentBean.hashId()%>');"><%=
			NumberUtils.format(",###", i)%></a></td>
				</tr>
				<%
	}
%>
			</table>
		</div>
<script type="text/javascript">
	(function() {
		var c = <%=count%>;
		$("vg_<%=gid%>").select(".vbar").each(function(bar) {
			bar = bar.down();
			var c2 = parseInt($F(bar.next()));
			var v = c2 == 0 ? 0 : (c2 / c) * 1000;
			bar.pb = new $Comp.ProgressBar(bar, {
				maxProgressValue : 1000,
				height: 15,
				startAfterCreate: false,
				showAbortAction: false,
				showDetailAction: false
			});
			bar.pb.setProgress(v);
		});
	}).delay(0.1);
</script>
	</div>
	<%
	}
%>
	<div class="vb" style="text-align: right;">
		<input type="button" value="#(Button.Cancel)"
			onclick="$Actions['voteViewWindow'].close();">
	</div>
</div>
<script type="text/javascript">
	(function() {
		var win = $Actions["voteViewWindow"].window;
		win.observe("resize:ended", function() {
			win.element.select(".vbar").each(function(bar) {
				bar.pb.updateUI();
			});
		});
	})();
</script>