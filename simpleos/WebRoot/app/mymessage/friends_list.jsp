<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.web.page.PageRequestResponse"%>
<%@ page import="net.simpleframework.my.friends.FriendsUtils"%>
<%
	final PageRequestResponse requestResponse = new PageRequestResponse(
			request, response);
	final String my_friends = FriendsUtils.deployPath
			+ "jsp/my_friends.jsp";
	final String my_friends_group = FriendsUtils.deployPath
			+ "jsp/my_friends_group.jsp";
%>
<table style="padding: 5px; width: 100%;">
	<tr>
		<td valign="top" style="width: 200px;"><jsp:include
				page="<%=my_friends_group%>"></jsp:include></td>
		<td valign="top"><div class="splitbar"></div></td>
		<td valign="top" style="padding-left: 10px;"><jsp:include
				page="<%=my_friends%>"></jsp:include></td>
	</tr>
</table>
<script type="text/javascript">
	(function() {
		var sb = $$(".simple_tabs_content .splitbar")[0];
		$Comp.createSplitbar(sb, sb.up().previous());
	})();
</script>
