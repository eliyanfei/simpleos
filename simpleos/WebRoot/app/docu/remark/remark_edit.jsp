<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="net.simpleframework.content.component.remark.RemarkUtils"%>
<%@ page
	import="net.simpleframework.web.page.component.ComponentParameter"%>
<div id="formRemarkHtmlEditor">
	<div>
		<input type="hidden" id="parentId" name="parentId" />
		<input type="hidden" id="itemId" name="itemId" />
		<table style="width: 100%;" cellpadding="0" cellspacing="0">
			<tr>
				<td id="textareaRemarkHtmlEditorInfo" class="important-tip">
					#(CKEditor.0)
				</td>
				<td align="right">
					&nbsp;
				</td>
			</tr>
		</table>
		<textarea id="textareaRemarkHtmlEditor"
			name="textareaRemarkHtmlEditor" style="display: none;"></textarea>
	</div>
	<table cellpadding="0" cellspacing="0"
		style="margin-top: 8px; width: 100%;">
		<tr>
			<td align="right">
				<input type="button" class="button2" id="submitRemarkHtmlEditor"
					value="#(remark.1)" onclick="$Actions['ajaxSaveRemark']();"
					key="ctrlReturn" />
				<input type="button" value="#(Button.Cancel)"
					onclick="$Actions['remarkEditWindow'].close();" />
			</td>
		</tr>
	</table>
</div>
