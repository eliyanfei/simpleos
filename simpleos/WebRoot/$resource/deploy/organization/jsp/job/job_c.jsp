<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@page import="net.simpleos.SimpleosUtil"%>

<%
	SimpleosUtil.addMenuNav(request.getSession(), null, "角色管理", false);
%>
<div style="margin: 8px;">
	<table style="width: 100%;" cellpadding="0" cellspacing="0">
		<tr>
			<td width="25%" valign="top">
				<div id="__job_tree_c"></div>
			</td>
			<td width="8px;"></td>
			<td valign="top">
				<div id="__job_detail"></div>
			</td>
		</tr>
	</table>
</div>