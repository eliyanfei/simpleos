<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.organization.OrgUtils"%>
<%@ page import="net.simpleframework.util.StringUtils"%>
<%
	final String userIdParameter = OrgUtils.um().getUserIdParameterName();
	final String t = request.getParameter("t");
%>
<style>
#sentMessageTopic,#textareaSentMessageHtmlEditor {
	border-width: 0;
	width: 99%;
	background-image: none;
}
</style>
<div id="__sent_mail_form">
	<input type="hidden" name="<%=userIdParameter%>"
		value="<%=StringUtils.blank(request.getParameter(userIdParameter))%>" />
	<input type="hidden" name="t" value="<%=StringUtils.blank(t)%>" />
	<div id="__np__newsAddForm">
		<table cellspacing="0" class="tbl tbl_first">
			<tr>
				<td class="lbl">
					主题
				</td>
				<td>
					<input id="sentMessageTopic" name="sentMessageTopic" type="text" />
				</td>
			</tr>
		</table>
		<table cellspacing="0" class="tbl">
			<tr>
				<td class="lbl" valign="top">
					内容
				</td>
				<td>
					<textarea id="textareaSentMessageHtmlEditor" rows="15"
						name="textareaSentMessageHtmlEditor"></textarea>
				</td>
			</tr>
		</table>
		<table class="tbl" cellspacing="0">
			<tr>
				<td class="lbl">
				</td>
				<td colspan="3">
					<div class="clear_float" style="padding: 4px;">
						<div style="float: right; width: 100%;">
							<div class="btn">
								<table width="100%">
									<tr>
										<td align="left">
										</td>
										<td>
											<input type="button" id="buttonSentMessage"
												onclick="$Actions['ajaxSentMessageOK']();" value="发送消息" />
											<input type="button" value="#(Button.Cancel)"
												onclick="$Actions['userSentMessageWindow'].close();" />
										</td>
									</tr>
								</table>
							</div>
						</div>
					</div>
				</td>
			</tr>
		</table>
	</div>
</div>