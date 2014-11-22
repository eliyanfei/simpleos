<%@page import="net.simpleos.SimpleosUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@page import="java.util.Map"%><%@page
	import="java.util.HashMap"%><%@page
	import="net.simpleos.backend.BackendUtils"%><%@page
	import="net.simpleos.impl.PrjColumns"%><%@page
	import="net.simpleos.backend.company.CompanyUtils"%><%@page
	import="net.simpleos.impl.PrjColumn"%>
<%
	PrjColumns columns = CompanyUtils.appModule.getPrjColumns("contact");
%>
<div class="simple_toolbar2 f3" style="min-height: 400px;">
	<table>
		<%
			for (PrjColumn column : columns.getColumnMap().values()) {
		%>
		<tr>
			<th class="w-100px" nowrap="nowrap" align="right">
				<%=column.getText()%>：
			</th>
			<td width="100%" style="font-weight: normal;">
				<%=SimpleosUtil.attrMap.get("contact."+column.getName())%>
			</td>
		</tr>
		<%
			}
		%>
	</table>
</div>