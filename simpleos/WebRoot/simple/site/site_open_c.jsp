<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@page import="net.itsite.impl.PrjColumns"%><%@page
	import="java.util.Map"%><%@page
	import="net.prj.manager.site.PrjSiteUtils"%><%@page
	import="net.prj.manager.PrjMgrUtils"%><%@page
	import="net.itniwo.commons.StringsUtils"%>
<%
	PrjColumns columns = PrjSiteUtils.appModule.getPrjColumns("open");
	Map<String, String> map = PrjMgrUtils.loadCustom("open");
%>
<div class="simple_custom" id="site_open_form" style="width: 95%;">
	<table style="width: 100%;">
		<%
			String ids = columns.getIds();
			for (String id : ids.split(",")) {
		%>
		<tr>
			<td colspan="2"
				style="background-color: #EFEFEF; border: 1px solid #D4D4D4;">
				<%=id%>
			</td>
		</tr>
		<tr>
			<th nowrap="nowrap">
				App Key
			</th>
			<td width="100%">
				<input type="text" name="open_appKey_<%=id%>"
					id="open_appKey_<%=id%>"
					value="<%=StringsUtils.trimNull(map.get("open_appKey_" + id), "")%>"
					class="text-3">
			</td>
		</tr>
		<tr>
			<th nowrap="nowrap">
				App Secret
			</th>
			<td>
				<input type="text" name="open_appSecret_<%=id%>"
					id="open_appSecret_<%=id%>"
					value="<%=StringsUtils.trimNull(map.get("open_appSecret_" + id), "")%>"
					class="text-3">
			</td>
		</tr>
		<%
			}
		%><tr>
			<th></th>
			<td>
				<input type="button" class="button2" id="site_open_btn"
					onclick="$IT.A('site_openAct');" value="#(Itsite.c.ok)">
			</td>
		</tr>
	</table>
</div>