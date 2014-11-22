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
<%@ page import="net.simpleframework.util.HTMLBuilder"%>
<%@page import="net.simpleos.SimpleosUtil"%>
<%@page import="net.simpleframework.my.dialog.SimpleDialog"%>

<%
	final List<?> list = PagerUtils.getPagerList(request);
	if (list == null || list.size() == 0) {
		return;
	}
	final String box = request.getParameter("box");
	final boolean me = "me".equals(box);
	final boolean you = "you".equals(box);
	final PageRequestResponse requestResponse = new PageRequestResponse(request, response);
	final Object userId = SimpleosUtil.getLoginUser(requestResponse).getId();
%>
<div class="my_message_list">
	<%
		for (Object o : list) {
			final SimpleDialog dialog = (SimpleDialog) o;
			final IUser sent = OrgUtils.um().queryForObjectById(dialog.getSentId().equals2(userId) ? dialog.getToId() : dialog.getSentId());
			if (sent == null) {
				continue;
			}
	%>
	<table cellpadding="0" cellspacing="0" class="item">
		<tr>
			<%
				if (!you) {
			%>
			<td width="80px" align="right" valign="top">
				<img class="photo_icon" style="width: 64px; height: 64px;"
					src="<%=OrgUtils.getPhotoSRC(request, sent, 64, 64)%>" />
			</td>
			<td class="pbg"></td>
			<%
				}
			%>
			<td valign="top">
				<div class="txt">
					<table cellpadding="0" cellspacing="0">
						<tr>
							<td>
								与(<%=MySpaceUtils.getAccountAware().wrapAccountHref(requestResponse, sent)%>)对话
							</td>
							<td>
							</td>
							<td align="right" nowrap="nowrap">
								<a class="a2"
									onclick="$Actions['myMessageSentWindow']('userid=<%=sent.getId()%>');">发消息</a>
								<%=HTMLBuilder.SEP%>
								<a class="a2"
									onclick="$Actions['myDialogWindow']('userid=<%=sent.getId()%>');">对话</a>
							</td>
						</tr>
					</table>
					<p
						style="border-top: 1px dashed #666; padding-top: 10px; color: #24691F;">
						开始时间：<%=ConvertUtils.toDateString(dialog.getCreateDate())%>
						结束时间：<%=ConvertUtils.toDateString(dialog.getLastDate())%></p>
				</div>
			</td>
			<%
				if (you) {
			%>
			<td class="pbg2"></td>
			<td width="80px" align="right" valign="top">
				<img class="photo_icon" style="width: 64px; height: 64px;"
					src="<%=OrgUtils.getPhotoSRC(request, sent, 64, 64)%>" />
			</td>
			<%
				}
			%>
		</tr>
	</table>
	<%
		}
	%>
</div>
