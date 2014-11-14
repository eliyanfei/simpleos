<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.web.page.component.ui.pager.PagerUtils"%>
<%@ page import="java.util.List"%>
<%@ page import="net.simpleframework.my.message.SimpleMessage"%>
<%@ page import="net.simpleframework.organization.OrgUtils"%>
<%@ page import="net.simpleframework.organization.IUser"%>
<%@ page import="net.simpleframework.util.ConvertUtils"%>
<%@ page import="net.simpleframework.my.message.MessageUtils"%>
<%@ page import="net.simpleframework.web.page.PageRequestResponse"%>
<%@ page import="net.simpleframework.my.space.MySpaceUtils"%>
<%@ page import="net.simpleframework.util.HTMLBuilder"%>
<%
	final List<?> list = PagerUtils.getPagerList(request);
	if (list == null || list.size() == 0) {
		return;
	}
	final PageRequestResponse requestResponse = new PageRequestResponse(
			request, response);
	boolean s = "s".equals(request.getParameter("box"));
	boolean r = "r".equals(request.getParameter("box"));
%>
<div class="my_message_list">
	<%
		for (Object o : list) {
			final SimpleMessage sMessage = (SimpleMessage) o;
			final IUser sent = OrgUtils.um().queryForObjectById(
					s ? sMessage.getToId() : sMessage.getSentId());
			if (sent == null) {
				continue;
			}
	%>
	<table cellpadding="0" cellspacing="0" class="item">
		<tr>
			<%
				if (!s) {
			%>
			<td width="80px" align="right" valign="top"><img class="photo_icon"
				style="width: 64px; height: 64px;" src="<%=OrgUtils.getPhotoSRC(request, sent, 64, 64)%>" />
			</td>
			<td class="pbg"></td>
			<%
				}
			%>
			<td valign="top">
				<div class="txt">
					<table cellpadding="0" cellspacing="0">
						<tr>
							<td><%=s ? "#(my_message_c.0)" : "#(my_message_c.1)"%> <%=MySpaceUtils.getAccountAware().wrapAccountHref(
						requestResponse, sent)%> <%=ConvertUtils.toDateString(sMessage.getSentDate())%></td>
							<td align="right"><a class="a2"
								onclick="$Actions['myMessageSentWindow']('<%=OrgUtils.um().getUserIdParameterName()%>=<%=sent.getId()%>');">#(my_message_c.2)</a>
								<%
									if (r) {
								%><%=HTMLBuilder.SEP%><a class="a2"
								onclick="$Actions['__my_message_delete']('messageId=<%=sMessage.getId()%>');">#(Delete)</a>
								<%
									}
								%>
							</td>
						</tr>
					</table>
					<p><%=MessageUtils.getTextBody(sMessage.getTextBody())%></p>
				</div>
			</td> 
			<%
				if (s) {
			%>
			<td class="pbg2"></td>
			<td width="80px" align="left" valign="top"><img class="photo_icon"
				style="width: 64px; height: 64px;" src="<%=OrgUtils.getPhotoSRC(request, sent, 64, 64)%>" />
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