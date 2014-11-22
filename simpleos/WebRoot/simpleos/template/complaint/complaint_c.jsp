<%@page import="net.simpleframework.web.page.PageRequestResponse"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@page import="net.simpleos.module.complaint.EComplaint"%>

<div class="simple_toolbar2" id="complaintForm">
	<input type="hidden" id="comp_refId" name="comp_refId"
		value="<%=request.getParameter("refId")%>">
	<input type="hidden" id="comp_refModule" name="comp_refModule"
		value="<%=request.getParameter("refModule")%>">
	<table width="100%" cellpadding="2" cellspacing="0">
		<tr>
			<td class="l">
				举报原因
			</td>
			<td>
				<%
					for (EComplaint complaint : EComplaint.values()) {
				%>
				<input type="radio" value="<%=complaint.name()%>" checked="checked"
					onclick="complaintText('<%=complaint.toTip()%>');"
					name="comp_complaint" id="<%=complaint.name()%>">
				<label for="<%=complaint.name()%>"><%=complaint.toString()%></label>
				<%
					}
				%>
			</td>
		</tr>
		<tr>
			<td class="l" valign="top">
				详细原因
			</td>
			<td>
				<textarea name="comp_content" id="comp_content" style="width: 100%"
					rows="10"></textarea>
			</td>
		</tr>
		<tr>
			<td class="l" valign="top">
			</td>
			<td>
				<div style="text-align: right; margin-top: 6px;">
					<input type="button" class="button2" value="确定举报" id="compBtn"
						onclick="$IT.A('complaintSaveAct');" />
					<input type="button" value="取消"
						onclick="$IT.C('complaintWindowAct');" />
				</div>
			</td>
		</tr>
	</table>
</div>

<script type="text/javascript">
function complaintText(text) {
	$Comp.addBackgroundTitle($('comp_content'), text);
}
complaintText("请详述举报原因");
</script>
<style type="text/css">
#complaintForm .l {
	width: 70px;
	text-align: right;
}

#complaintForm {
	background: #f7f7ff;
	border: 1px solid #ddd;
	padding: 6px 8px;
}
</style>
