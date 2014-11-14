<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@page
	import="net.simpleframework.ado.db.ITableEntityManager"%><%@page
	import="net.simpleframework.my.message.MessageUtils"%><%@page
	import="net.simpleframework.my.message.SimpleMessage"%><%@page
	import="net.simpleframework.util.ConvertUtils"%>
<%
	
%>
<style>
#mm_subject,#mm_textBody,#td_mm_toid .text,#td_mm_toid .text input {
	border-width: 0;
	width: 99%;
	background-image: none;
}
</style>
<div id="my_message_sent_form">
	<div id="__np__newsAddForm">
		<table cellspacing="0" class="tbl tbl_first">
			<tr>
				<td class="lbl">
					#(Message.1)
				</td>
				<td>
					<div id="td_mm_toid"></div>
					<input type="hidden" id="mm_toid" name="mm_toid" />
				</td>

			</tr>
		</table>
		<table cellspacing="0" class="tbl">
			<tr>
				<td class="lbl">
					#(Message.2)
				</td>
				<td>
					<input type="text" id="mm_subject" name="mm_subject">
				</td>
			</tr>
		</table>
		<table cellspacing="0" class="tbl">
			<tr>
				<td class="lbl" valign="top">
					#(Message.3)
				</td>
				<td>
					<textarea rows="8" id="mm_textBody" name="mm_textBody"></textarea>
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
											<input type="button" id="buttonMessageSent" value="#(Message.4)"
												class="button2" onclick="$IT.A('ajaxMessageSentOK');" />
											<input type="button" value="#(Itsite.c.close)"
												onclick="$IT.C('myMessageSendWin');" />
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
<script type="text/javascript">
(function() {
	addTextButton("mm_toid_text", "messageUsersSelect", "td_mm_toid", false);
})();
</script>
