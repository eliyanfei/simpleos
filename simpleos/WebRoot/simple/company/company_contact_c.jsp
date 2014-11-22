<%@page import="net.simpleos.backend.BackendUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@page import="net.simpleos.impl.PrjColumns"%><%@page
	import="net.simpleos.impl.PrjColumn"%>

<%
	PrjColumns columns = BackendUtils.applicationModule.getPrjColumns("contact");
%>

<div class="simple_custom" id="company_contact_form">
	<table>
		<%
			for (PrjColumn column : columns.getColumnMap().values()) {
		%>
		<tr>
			<th class="w-100px"><%=column.getText()%></th>
			<td><input type="text" name="<%=column.getName()%>"
				id="<%=column.getName()%>" value="" class="text-3"></td>
		</tr>
		<%
			}
		%>
		<tr>
			<th></th>
			<td><input type="button" class="button2"
				id="company_contact_btn"
				onclick="$IT.A('company_contactAct','type=contact');"
				value="#(Itsite.c.ok)"></td>
		</tr>
	</table>
</div>