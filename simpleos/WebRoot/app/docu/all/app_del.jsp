<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div id="__docuAttrForm2">
	<table width="100%" cellpadding="2" cellspacing="0">
		<tr>
			<td class="l" valign="top">
				#(Docu.all.1)
			</td>
			<td>
				<textarea rows="10" style="width: 98%" id="delReason"
					name="delReason"></textarea>
			</td>
		</tr>
	</table>
</div>
<div>
	<div align="right">
		<input type="button" class="button2"
			onclick="$Actions['docuDeleteAct']('docuIds=<%=request.getParameter("docuIds")%>&delReason='+$F('delReason'));"
			value="#(Itsite.c.ok)">
		<input type="button" onclick="$IT.C('appDelDocuWindowAct');"
			value="#(Itsite.c.cancel)">
	</div>
</div>
<style type="text/css">
#__docuAttrForm2 .l {
	width: 50px;
	text-align: right;
}

#__docuAttrForm2 {
	background: #f7f7ff;
	border: 1px solid #ddd;
	padding: 6px 8px;
}
</style>
