<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="net.simpleframework.web.page.component.ui.pager.PagerUtils"%>
<%@ page import="net.simpleframework.my.space.SapceLogBean"%>
<%@ page import="net.simpleframework.organization.OrgUtils"%>
<%@ page import="net.simpleframework.organization.IUser"%>
<%@ page import="net.simpleframework.my.space.MySpaceUtils"%>
<%@ page import="net.simpleframework.web.page.PageRequestResponse"%>
<%@ page import="net.simpleframework.util.DateUtils"%>
<%@ page import="net.simpleframework.util.StringUtils"%>
<%@ page import="net.simpleframework.organization.account.IAccount"%>
<%@ page
	import="net.simpleframework.organization.account.AccountSession"%>
<%
	final PageRequestResponse requestResponse = new PageRequestResponse(
			request, response);
	final IAccount login = AccountSession.getLogin(session);
	for (Object o : PagerUtils.getPagerList(request)) {
		final SapceLogBean log = (SapceLogBean) o;
		final IUser user = OrgUtils.um().queryForObjectById(log.getUserId());
		if (user == null) {
			continue;
		}
		final String content = MySpaceUtils.spaceLogContent(
				requestResponse, log);
		if (!StringUtils.hasText(content)) {
			continue;
		}
		final boolean canDelete = OrgUtils.isManagerMember(session) || 
			(login != null && login.getId().equals2(user.getId()));
%>
<div class="space_content_item">
	<table style="width: 100%;">
		<tr>
			<td valign="top" width="50"><img class="photo_icon"
				style="width: 36px; height: 36px;"
				src="<%=OrgUtils.getPhotoSRC(request, user, 64, 64)%>">
			</td>
			<td valign="top"><%=content%>
				<table style="width: 100%;" cellpadding="0" cellspacing="0">
					<tr>
						<td class="gray-color"><%=DateUtils.getRelativeDate(log.getCreateDate())%></td>
						<td align="right">
							<%if(!log.getReplyFrom().equals2(0)){ %>
							<a class="a2" href="/space.html?logId=<%=log.getReplyFrom()%>">@内容</a>
							<%} %>
							<%
								if (canDelete) {
							%> <a class="a2"
							onclick="$Actions['ajaxSpaceLogDelete']('logid=<%=log.getId()%>');">#(Delete)</a>
							<%
								}
							%> <a class="a2"
							onclick="do_my_space_log_remark(this, '<%=log.getId()%>');">#(space_log_list.0)</a>
							(<%=log.getRemarks()%>)</td>
					</tr>
				</table>
				<div style="display: none;">#(space_log_list.8)</div></td>
		</tr>
	</table>
</div>
<%
	}
%>
<script type="text/javascript">
	function do_my_space_log_remark(a, logid) {
		var ele = a.up("table").next();
		if (!ele.down()) {
			var act = $Actions["ajaxSpaceLogRemarkPage"];
			act.container = ele;
			act.selector = ele;
			act('logid=' + logid);
		}
		ele.$toggle();
	}
</script>
