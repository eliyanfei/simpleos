<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="net.simpleframework.web.page.component.ui.pager.PagerUtils"%>
<%@ page import="net.simpleframework.organization.OrgUtils"%>
<%@ page import="net.simpleframework.my.friends.Friends"%>
<%@ page import="net.simpleframework.organization.IUser"%>
<%@ page import="net.simpleframework.util.DateUtils"%>
<%@ page import="net.simpleframework.organization.account.IAccount"%>
<%@ page import="net.simpleframework.util.StringUtils"%>
<%@ page import="net.simpleframework.util.IPAndCity"%>
<%@ page import="net.simpleframework.web.page.PageRequestResponse"%>
<%@ page import="net.simpleframework.my.space.MySpaceUtils"%>
<%
	List<?> list = PagerUtils.getPagerList(request);
	if (list == null || list.size() == 0) {
		return;
	}
	final PageRequestResponse requestResponse = new PageRequestResponse(
			request, response);
	for (Object o : list) {
		final Friends f = (Friends) o;
		final IAccount account = OrgUtils.am().queryForObjectById(
				f.getFriendId());
		if (account == null) {
			continue;
		}
		final IUser user = account.user();
		if (user == null) {
			continue;
		}
%>
<table style="display: inline-block; vertical-align: top; width: 320px;" cellpadding="4">
	<tr>
		<td valign="top" style="padding-top: 6px;"><span title="#(my_friends_pager.0)"
			class="drag_image" style="width: 7px;"></span></td>
		<td width="70" valign="top"><img class="photo_icon" style="width: 64px; height: 64px;"
			src="<%=OrgUtils.getPhotoSRC(request, user, 64, 64)%>">
			<div style="text-align: center; margin-top: 4px;">
				<input type="checkbox" value="<%=f.getId() %>" />
			</div>
		</td>
		<td valign="top">
			<div class="f3">
				<%=MySpaceUtils.getAccountAware().wrapAccountHref(requestResponse, user) %>
				<span>, <%=DateUtils.getRelativeDate(account.getLastLoginDate())%></span>
			</div> <%
 	final String desc = user.getDescription();
 		if (StringUtils.hasText(desc)) {
 %><div style="padding-top: 5px;"><%=desc%></div> <%
 	}
 %>
			<div style="padding-top: 5px;"><%=IPAndCity.getCity(account.getLastLoginIP(), true)%></div>
			<div style="padding-top: 5px;">
				<a onclick="$Actions['ajaxDeleteMyFriend']('fid=<%=f.getId()%>');" class="a2">#(Delete)</a>
			</div>
		</td>
	</tr>
</table>
<%
	}
%>
<script type="text/javascript">
	function __my_friends_checkArr(drag) {
		var o = drag.up("table").down("input[type=checkbox]");
		var arr = [ o.value ];
		$("idMyFriendsPager").select("input[type=checkbox]").each(function(c) {
			if (c != o && c.checked && c.value != "on") {
				arr.push(c.value);
			}
		});
		return arr;
	}

	function __my_friends_checkAll(o) {
		$("idMyFriendsPager").select("input[type=checkbox]").each(function(c) {
			if (o == c) {
				return;
			}
			c.checked = o.checked;
		});
	}
</script>
