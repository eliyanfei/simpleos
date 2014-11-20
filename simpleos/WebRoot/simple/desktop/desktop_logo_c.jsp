<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@page import="net.itsite.utils.UID"%>

<%
	String id = UID.asString();
%>
<div>
	<table width="100%">
		<tr>
			<td nowrap="nowrap">
				<img alt="logo" id="template_logoId"
					src="/default/images/logo.png?v=<%=id%>">
			</td>
			<td id="template_logoUploadId" width="100%">
			</td>
		</tr>
	</table>
</div>
