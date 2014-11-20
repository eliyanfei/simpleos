<%@page import="net.itsite.ItSiteUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@page import="java.util.Map"%><%@page
	import="java.util.HashMap"%><%@page
	import="net.prj.manager.PrjMgrUtils"%><%@page
	import="net.itsite.impl.PrjColumns"%><%@page
	import="net.prj.manager.company.PrjCompanyUtils"%><%@page
	import="net.itsite.impl.PrjColumn"%>
<%
	PrjColumns columns = PrjCompanyUtils.appModule.getPrjColumns("contact");
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
				<%=ItSiteUtil.attrMap.get("contact."+column.getName())%>
			</td>
		</tr>
		<%
			}
		%>
	</table>
</div>