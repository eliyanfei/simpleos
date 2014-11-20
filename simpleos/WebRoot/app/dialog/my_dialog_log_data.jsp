<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@page
	import="net.simpleframework.web.page.PageRequestResponse"%><%@page
	import="net.simpleframework.web.page.component.ui.pager.PagerUtils"%><%@page
	import="java.util.List"%><%@page
	import="net.simpleframework.my.dialog.SimpleDialog"%><%@page
	import="net.simpleframework.my.dialog.SimpleDialogItem"%><%@page
	import="net.simpleframework.my.space.MySpaceUtils"%><%@page import="net.simpleframework.util.ConvertUtils"%><%@page import="net.simpleframework.web.page.component.ui.dictionary.SmileyUtils"%>

<div id="dialog_msg_data_id" style="height: auto;">
	<%
		final PageRequestResponse requestResponse = new PageRequestResponse(request, response);
		final List<?> data = PagerUtils.getPagerList(request);
		if (data == null && data.size() == 0) {
			return;
		}
		boolean isMe = true;
		SimpleDialog dialog = null;
		for (Object o : data) {
			SimpleDialogItem dialogItem = (SimpleDialogItem) o;
			if (dialog == null) {
				dialog = MySpaceUtils.getTableEntityManager(SimpleDialog.class).queryForObjectById(dialogItem.getDialogId(), SimpleDialog.class);
			}
			String sentUser = null;
			isMe = dialogItem.isMe();
			if (isMe) {
				sentUser = dialog.getSentUserText();
			} else {
				sentUser = dialog.getToUserText();
			}
	%>
	<div>
		<div class="tit <%=isMe ? "tome" : "toyou"%>"><%=sentUser%>
			<%=ConvertUtils.toDateString(dialogItem.getSentDate())%></div>
		<div class="cnt"><%=SmileyUtils.replaceSmiley(dialogItem.getContent())%></div>
	</div>
	<%
		}
	%>
</div>