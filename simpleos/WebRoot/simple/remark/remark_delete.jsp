<%@page import="net.simpleframework.web.page.PageRequestResponse"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@page import="net.itsite.complaint.EComplaint"%>

<div class="simple_toolbar21" id="rForm">
	<input type="hidden" id="raction" name="raction"
		value="remark<%=request.getParameter("rtype")%>Table">
	<input type="hidden" id="rtype" name="rtype"
		value="<%=request.getParameter("rtype")%>">
	<input type="hidden" id="remarkId" name="remarkId"
		value="<%=request.getParameter("remarkId")%>">
	<table width="100%" cellpadding="2" cellspacing="0">
		<tr>
			<td class="l" valign="top">
				#(Remark.0)
			</td>
			<td>
				<textarea name="rd_content" id="rd_content" style="width: 100%"
					rows="11"></textarea>
			</td>
		</tr>
		<tr>
			<td class="l" valign="top">
			</td>
			<td>
				<div style="text-align: right; margin-top: 6px;">
					<input type="button" class="button2" value="#(Remark.1)" id="rdBtn"
						onclick="$IT.A('remarkDeleteAct');" />
					<input type="button" value="#(Itsite.c.cancel)" onclick="$IT.C('remarkWindowAct');" />
				</div>
			</td>
		</tr>
	</table>
</div>

<style type="text/css">
#rForm .l {
	width: 50px;
	text-align: right;
}

#rForm {
	padding: 6px 8px;
}
</style>
