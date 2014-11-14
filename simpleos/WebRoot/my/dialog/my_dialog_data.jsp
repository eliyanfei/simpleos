<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@page
	import="net.simpleframework.my.dialog.DialogUtils"%><%@page
	import="net.simpleframework.web.page.PageRequestResponse"%><%@page
	import="net.simpleframework.core.ado.IDataObjectQuery"%><%@page
	import="net.simpleframework.my.dialog.SimpleDialog"%><%@page
	import="net.simpleframework.util.ConvertUtils"%><%@page
	import="net.simpleframework.web.page.component.ui.dictionary.SmileyUtils"%><%@page
	import="net.a.ItSiteUtil"%><%@page
	import="net.simpleframework.my.dialog.SimpleDialogItem"%>
<%
	final PageRequestResponse requestResponse = new PageRequestResponse(request, response);
	final SimpleDialog dialog = DialogUtils.getSimpleDialog(requestResponse);
	IDataObjectQuery<SimpleDialogItem> qs = DialogUtils.queryDialog(requestResponse,dialog);
%>
<style>
#dialog_msg_data_id {
	height: 325px;
	overflow: auto;
}

#dialog_msg_data_id .cnt {
	text-indent: 1em;
}

#dialog_msg_data_id .tit {
	padding: 3px 0;
}

#dialog_msg_data_id .toyou {
	color: green;
}

#dialog_msg_data_id .tome {
	color: blue;
}
</style>
<div id="dialog_msg_data_id">
	<%
		if (qs != null) {
			String sentUser = null;
			SimpleDialogItem dialogItem;
			Object userId = ItSiteUtil.getLoginUser(requestResponse).getId();
			boolean isMe = true;
			while ((dialogItem = qs.next()) != null) {
				isMe = dialogItem.isMe();
				if(isMe){
					sentUser = dialog.getSentUserText();
				}else{
					sentUser =  dialog.getToUserText();
				}
	%>
	<div>
		<div class="tit <%=isMe ? "tome" : "toyou"%>"><%=sentUser%>
			<%=ConvertUtils.toDateString(dialogItem.getSentDate())%></div>
		<div class="cnt"><%=SmileyUtils.replaceSmiley(dialogItem.getContent())%></div>
	</div>
	<%
		}
		}
	%>
</div>
