<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@page
	import="net.simpleframework.ado.db.ITableEntityManager"%><%@page
	import="net.simpleframework.my.message.MessageUtils"%><%@page
	import="net.simpleframework.my.message.SimpleMessage"%><%@page
	import="net.simpleframework.util.ConvertUtils"%>

<%
	final ITableEntityManager manager = MessageUtils.getTableEntityManager(SimpleMessage.class);
	final SimpleMessage message = manager.queryForObjectById(request.getParameter("myMessageId"), SimpleMessage.class);
	if (message == null) {
		return;
	}
	message.setMessageRead(true);
	manager.update(new Object[] { "messageRead" }, message);
%>
<div>
	<div id="__np__newsAddForm" style="overflow: hidden;">
		<table cellspacing="0" class="tbl tbl_first">
			<tr>
				<td class="lbl">
					#(Message.V.0)
				</td>
				<td>
					<%=ConvertUtils.toDateString(message.getSentDate(), "yyyy-MM-dd HH:mm")%>
				</td>
				<td class="lbl">
					#(Message.V.1)
				</td>
				<td>
					<%=message.getUserText(message.getSentId())%>
				</td>

			</tr>
		</table>
		<table cellspacing="0" class="tbl">
			<tr>
				<td class="lbl">
					#(Message.V.2)
				</td>
				<td>
					<%=message.getSubject()%>
				</td>
			</tr>
		</table>
		<table cellspacing="0" class="tbl">
			<tr>
				<td class="lbl" valign="top">
					#(Message.V.3)
				</td>
				<td>
					<div style="overflow: auto;"><%=message.getTextBody()%></div>
				</td>
			</tr>
		</table>
	</div>
</div>