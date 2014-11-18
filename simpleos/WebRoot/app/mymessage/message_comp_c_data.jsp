<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="net.simpleframework.web.page.component.ui.pager.PagerUtils"%>
<%@ page import="java.util.List"%>
<%@ page import="net.simpleframework.organization.OrgUtils"%>
<%@ page import="net.simpleframework.organization.IUser"%>
<%@ page import="net.simpleframework.util.ConvertUtils"%>
<%@ page import="net.simpleframework.my.message.MessageUtils"%>
<%@ page import="net.simpleframework.web.page.PageRequestResponse"%>
<%@ page import="net.simpleframework.my.space.MySpaceUtils"%>
<%@ page import="net.simpleframework.util.HTMLBuilder"%><%@page
	import="net.itsite.complaint.ComplaintBean"%><%@page
	import="net.a.ItSiteUtil"%><%@page
	import="net.itsite.complaint.ComplaintUtils"%>

<%
	final List<?> list = PagerUtils.getPagerList(request);
	if (list == null || list.size() == 0) {
		return;
	}
	final PageRequestResponse requestResponse = new PageRequestResponse(request, response);
	final boolean manager = ItSiteUtil.isManage(requestResponse, ComplaintUtils.applicationModule);
%>
<div class="my_message_list">
	<%
		for (Object o : list) {
			final ComplaintBean compBean = (ComplaintBean) o;
			final IUser sent = OrgUtils.um().queryForObjectById(compBean.getUserId());
			if (sent == null) {
				continue;
			}
			boolean isDeal = compBean.isDeal();
	%>
	<table cellpadding="0" cellspacing="0" class="item">
		<tr>
			<td width="80px" align="right" valign="top">
				<img class="photo_icon" style="width: 64px; height: 64px;"
					src="<%=OrgUtils.getPhotoSRC(request, sent, 64, 64)%>" />
			</td>
			<td class="pbg"></td>
			<td valign="top">
				<div class="txt"  style="<%=!isDeal?"background: #F9F2A7;":"" %>">
					<table cellpadding="0" cellspacing="0">
						<tr>
							<%
								if (manager) {
							%>
							<td width="60"><%=MySpaceUtils.getAccountAware().wrapAccountHref(requestResponse, sent)%></td>
							<%
								}
							%>
							<td>
								举报<%=compBean.wrapRefTitle(requestResponse)%>
							</td>
							<td align="right" nowrap="nowrap">
								<%
									if (manager && !isDeal) {
								%>
								<a class="a2"
									onclick="$Actions['dealComplaintWindowAct']('complaintId=<%=compBean.getId()%>');">处理</a>
								<%=HTMLBuilder.SEP%>
								<%
									}
								%><a class="a2"
									onclick="$Actions['complaintDeleteAct']('complaintId=<%=compBean.getId()%>');">删除</a>
								<%=HTMLBuilder.SEP%>
								<a class="a2"
									onclick="$Actions['complaintOKAct']('complaintId=<%=compBean.getId()%>');">确认</a>
							</td>
						</tr>
					</table>
					<p id="comp_<%=compBean.getId()%>" style="display: none;"></p>
					<p style="color: #24691F;">
						举报内容：<%=compBean.getRefModule().toString()%></p>
					<p style="color: #24691F;">
						举报原因：<%=compBean.getComplaint().toString()%></p>
					<p style="color: #24691F;">
						详细原因：<%=ConvertUtils.toDateString(compBean.getCreateDate())%></p>
					<p><%=MessageUtils.getTextBody(compBean.getContent())%></p>
					<%
						if (isDeal) {
					%>
					<p
						style="border-top: 1px dashed #666; padding-top: 10px; color: #24691F;">
						处理结果：<%=ConvertUtils.toDateString(compBean.getDealDate())%></p>
					<p>
						<%=MessageUtils.getTextBody(compBean.getDealContent())%></p>
					<%
						}
					%>
				</div>
			</td>
		</tr>
	</table>
	<%
		}
	%>
</div>
<script type="text/javascript">
function showComplaint(refId) {
	$('comp_' + refId).innerHTML = $('div_comp_' + refId).innerHTML;
	$('comp_' + refId).toggle();
}
</script>