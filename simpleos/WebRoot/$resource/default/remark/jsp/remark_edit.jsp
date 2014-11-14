<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@page
	import="net.simpleframework.web.page.component.ComponentParameter"%><%@page
	import="net.simpleframework.content.component.remark.RemarkUtils"%>

<div class="simple_toolbar2" id="formRemarkHtmlEditor">
	<table width="100%" cellpadding="2" cellspacing="0">
		<tr>
			<td class="l" valign="top">
				内容
			</td>
			<td>
				<input type="hidden" id="parentId" name="parentId" />
				<input type="hidden" id="itemId" name="itemId" />
				<textarea id="textareaRemarkHtmlEditor" rows="8" style="width: 100%"
					name="textareaRemarkHtmlEditor"></textarea>
			</td>
		</tr>
		<tr>

		</tr>
		<tr>
			<td class="l" valign="top">
			</td>
			<td>
				<%
					final ComponentParameter nComponentParameter = RemarkUtils.getComponentParameter(request, response);
					if ((Boolean) nComponentParameter.getBeanProperty("showValidateCode")) {
				%>
				<div style="padding: 0px 8px;" id="remarkHtmlEditorValidateCode"></div>
				<%
					}
				%>
				<div style="text-align: right; margin-top: 6px;">
					<input type="button" class="button2" id="submitRemarkHtmlEditor"
						value="#(remark.1)" onclick="$Actions['ajaxSaveRemark']();"
						key="ctrlReturn" />
					<input type="button" value="#(Button.Cancel)"
						onclick="$Actions['remarkEditWindow'].close();" />
				</div>
			</td>
		</tr>
	</table>
</div>

<style type="text/css">
#formRemarkHtmlEditor .l {
	width: 70px;
	text-align: right;
}

#formRemarkHtmlEditor {
	background: #f7f7ff;
	border: 1px solid #ddd;
	padding: 6px 8px;
}
</style>
