<%@page import="net.simpleframework.web.page.PageRequestResponse"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@page import="net.itsite.complaint.EComplaint"%>

<div class="simple_toolbar2" id="dealcomplaintForm">
	<input type="hidden" id="complaintId" name="complaintId"
		value="<%=request.getParameter("complaintId")%>">
	<table width="100%" cellpadding="2" cellspacing="0">
		<tr>
			<td class="l" valign="top">
				处理结果
			</td>
			<td>
				<textarea name="comp_dealContent" id="comp_dealContent"
					style="width: 100%" rows="10">已经处理</textarea>
			</td>
		</tr>
	</table>
</div>
<div style="text-align: right; margin-top: 6px;">
	<input type="button" class="button2" value="确定举报"
		onclick="$IT.A('complaintDealAct');" />
	<input type="button" value="取消"
		onclick="$IT.C('dealComplaintWindowAct');" />
</div>
<style type="text/css">
#dealcomplaintForm .l {
	width: 70px;
	text-align: right;
}

#dealcomplaintForm {
	background: #f7f7ff;
	border: 1px solid #ddd;
	padding: 6px 8px;
}
</style>
