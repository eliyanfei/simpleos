<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@page
	import="net.simpleframework.web.page.PageRequestResponse"%><%@page
	import="net.simpleframework.organization.OrgUtils"%><%@page
	import="net.simpleframework.organization.IUser"%><%@page
	import="net.simpleframework.my.space.MySpaceUtils"%><%@page
	import="net.simpleframework.my.dialog.DialogUtils"%><%@page import="net.simpleframework.my.dialog.SimpleDialog"%><%@page import="net.a.ItSiteUtil"%>


<style>
.dialog_content #dialog_editor {
	width: 98%;
	border: 0;
	background: none;
	font-size: 14px;
	background: none;
}

.dialog_content .ins {
	width: 9%;
	background: #EBF7FA;
	height: 18px;
	padding: 3px 0;
}

.dialog_content #dialog_msg_data {
	height: 325px;
}

.dialog_content .__dialog_editor {
	border-bottom: 1px solid #ccc;
}
</style>
<%
	final PageRequestResponse requestResponse = new PageRequestResponse(request, response);
	Object userId = ItSiteUtil.getLoginUser(requestResponse).getId();
	SimpleDialog dialog ;
	if (MySpaceUtils.getAccountAware().isMyAccount(requestResponse)) {
%><div class="f2" style="text-align: center;">
	不能和自己对话
</div>
<%
	return;
	} else {
		dialog = DialogUtils.getSimpleDialog(requestResponse);
		if (dialog == null) {
%><div class="f2" style="text-align: center;">
	不能建立对话
</div>
<%
	return;
		}
	}
%>
<div class="dialog_content">
	<input type="hidden" id="dialogId" name="dialogId"
		value="<%=dialog.getId()%>">

	<table style="width: 100%;" class="space_log_editor" cellpadding="0"
		cellspacing="0">
		<tr>
			<td colspan="2" class="dialog_msg" id="dialog_msg_data">
			</td>
		</tr>
		<tr class="ins">
			<td>
				<a class="emotion" onclick="$Actions['smileyDialogDict']();">#(space_log.3)</a><a onclick="$Actions['myDialogLogWindow']('dialogId=<%=dialog.getId() %>');">对话记录</a>
			</td>
			<td align="right">
				您还可以输入
				<span id="dialog_txt" class="f2">500</span>个字符
			</td>
		</tr>
		<tr>
			<td colspan="2" class="__dialog_editor">
				<textarea rows="5" id="dialog_editor" name="dialog_editor"></textarea>
			</td>
		</tr>
		<tr>
			<td style="font-weight: bold;"> 你正在和(<%=dialog.getSentId().equals2(userId)?dialog.getToUserText():dialog.getSentUserText() %>)对话</td>
			<td align="right" style="padding-top: 5px;">
				<input type="button" value="发送" class="btn" id="__btn"
					onclick="$IT.A('dialogSaveAct');">
				<input type="button" value="关闭" onclick="$IT.C('myDialogWindow');">
			</td>
		</tr>
	</table>
</div>
<script type="text/javascript">
var exec1 = null;
$ready(function() {
	$$(".__dialog_editor textarea").invoke("observe", "keyup", function(evn) {
		var editor = $(this);
		var txt = $('dialog_txt');
		if (txt) {
			var v = $F(editor);
			if (v.length > 500) {
				editor.value = v.substring(0, 500);
			} else {
				txt.innerHTML = 500 - v.length;
			}
		}
	});
	$Comp.addReturnEvent($('dialog_editor'), function(ev) {
		if ($('dialog_editor').value == '') {
			alert('请先输入内容');
		} else {
			$('__btn').click();
		}
	});
	$IT.A('dialogMsgDataAct');
	$IT.startupExec(function(){
		$IT.A('dialogMsgDataAct',
					'userid=<%=request.getParameter("userid")%>');
	});
});

function scrollDiv() {
	var div = $('dialog_msg_data_id');
	if (div.scrollHeight > div.offsetHeight)
		div.scrollTop = div.scrollHeight - div.offsetHeight;
}
</script>
