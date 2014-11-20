<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.my.space.SapceLogBean"%>
<%@ page import="net.simpleframework.my.space.MySpaceUtils"%>
<%@ page import="net.simpleframework.ado.db.ExpressionValue"%>
<%@ page import="net.simpleframework.web.EFunctionModule"%>
<%@ page import="net.simpleframework.ado.db.IQueryEntitySet"%>
<%@ page import="net.simpleframework.organization.IUser"%>
<%@ page import="net.simpleframework.organization.OrgUtils"%>
<%@ page import="net.simpleframework.web.page.PageRequestResponse"%>
<%@ page import="net.simpleframework.util.DateUtils"%><%@page
	import="net.simpleframework.content.ContentUtils"%>
<%@page import="net.itsite.ItSiteUtil"%><%@page import="java.util.List"%><%@page
	import="net.simpleframework.web.page.component.ui.pager.PagerUtils"%>


<div class="space_log_layout space_content_item"
	style="float: left; display: block; width: 100%;">
	<%
		final PageRequestResponse requestResponse = new PageRequestResponse(request, response);
		final List<?> data = PagerUtils.getPagerList(request);
		if (data == null && data.size() == 0) {
			return;
		}
		IUser admin = OrgUtils.um().getUserByName("admin");
		for (Object o : data) {
			SapceLogBean log = (SapceLogBean) o;
			IUser user = OrgUtils.um().queryForObjectById(log.getUserId());
			final String content = MySpaceUtils.spaceLogContent(requestResponse, log);
	%>
	<div class="item" style="width: 49%; float: left;">
		<table style="width: 100%;" cellpadding="0" cellspacing="0"
			class="fixed_table">
			<tr>
				<td valign="top" width="40">
					<img class="photo_icon" style="width: 24px; height: 24px;"
						src="<%=OrgUtils.getPhotoSRC(request, user == null ? admin : user, 64, 64)%>">
				</td>
				<td style="word-break : break-all;">
					<%=content%>
					<table style="width: 100%;" cellpadding="0" cellspacing="0">
						<tr>
							<td class="gray-color"><%=DateUtils.getRelativeDate(log.getCreateDate())%></td>
							<td align="right">
								<%if(!log.getReplyFrom().equals2(0)){ %>
							<a class="a2" href="/space.html?logId=<%=log.getReplyFrom()%>">@#(Itsite.content)</a>
							<%} %>
								<a class="a2"
									onclick="do_my_space_log_remark(this, '<%=log.getId()%>');">#(Itsite.remark)</a>
								(<%=log.getRemarks()%>)
							</td>
						</tr>
					</table>
					<div style="display: none;">
						#(space_log_list.8)
					</div>
			</tr>
		</table>
	</div>
	<%
		}
	%>
</div>
<style type="text/css">
.space_log_layout .item {
	border-bottom: 1px dashed #ccc;
	padding: 4px 0;
}

.space_log_layout .item:hover {
	background-color: #f8f8f8;
	-moz-transition: background-color 0.3s;
	-webkit-transition: background-color 0.3s;
	transition: background-color 0.3s;
}

.space_log_layout .btn {
	text-align: right;
	padding-top: 4px;
}
</style>
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
