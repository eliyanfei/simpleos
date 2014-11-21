<%@page import="net.itsite.ItSiteUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@page import="net.itsite.impl.PrjColumns"%><%@page
	import="java.util.Map"%><%@page
	import="net.simpleos.backend.site.SiteUtils"%><%@page
	import="net.simpleos.backend.BackendUtils"%><%@page
	import="net.itsite.utils.StringsUtils"%>
<%
	PrjColumns columns = SiteUtils.appModule.getPrjColumns("open");
	Map<String, String> map = ItSiteUtil.attrMap;
%>
<div class="simple_custom" id="site_open_form" style="width: 95%;">
	<table style="width: 100%;">
		<%
			String ids = columns.getIds();
			for (String id : ids.split(",")) {
				String[] vs =id.split("=");
		%>
		<tr>
			<td colspan="2"
				style="background-color: #EFEFEF; border: 1px solid #D4D4D4;">
				<%=vs[1]%>
			</td>
		</tr>
		<tr>
			<th nowrap="nowrap">
				App Key
			</th>
			<td width="100%">
				<input type="text" name="open_appKey_<%=vs[0]%>"
					id="open_appKey_<%=vs[0]%>"
					value="<%=StringsUtils.trimNull(map.get("open.open_appKey_" + vs[0]), "")%>"
					class="text-3">
			</td>
		</tr>
		<tr>
			<th nowrap="nowrap">
				App Secret
			</th>
			<td>
				<input type="text" name="open_appSecret_<%=vs[0]%>"
					id="open_appSecret_<%=vs[0]%>"
					value="<%=StringsUtils.trimNull(map.get("open.open_appSecret_" + vs[0]), "")%>"
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