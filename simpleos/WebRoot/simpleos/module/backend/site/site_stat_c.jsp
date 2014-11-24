<%@page import="net.simpleos.SimpleosUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@page import="net.simpleos.impl.PrjColumns"%><%@page
	import="net.simpleos.backend.BackendUtils"%><%@page
	import="java.util.Map"%><%@page
	import="net.simpleos.utils.StringsUtils"%>
<%
	PrjColumns columns = BackendUtils.applicationModule.getPrjColumns("stat");
	Map<String, String> map = SimpleosUtil.attrMap;
%>
<div class="simple_custom" id="site_stat_form">
	<table width="100%">
		<tr>
			<th valign="top" class="w-100px" nowrap="nowrap">统计代码</th>
			<td width="100%"><textarea name="stat_code" id="stat_code"
					class="area-1" rows="10" style="width: 98%;"></textarea></td>
		</tr>
		<tr>
			<th></th>
			<td><input type="button" class="button2" id="site_stat_btn"
				onclick="$IT.A('site_statAct','type=stat');" value="#(Itsite.c.ok)">
			</td>
		</tr>
	</table>
</div>