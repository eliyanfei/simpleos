<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@page import="net.simpleos.utils.UID"%>

<%
	String id = UID.asString();
%>
<div>
	<table width="100%">
		<tr>
			<td nowrap="nowrap" class="rred">温馨提示:高度不要超过70</td>
		</tr>
		<tr>
			<td nowrap="nowrap"><img alt="logo" id="template_logoId"
				src="/default/images/logo.png?v=<%=id%>"></td>
			<td id="template_logoUploadId" width="100%"></td>
		</tr>
	</table>
</div>
