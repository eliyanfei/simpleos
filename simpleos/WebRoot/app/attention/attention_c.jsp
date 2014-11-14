<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.my.space.MySpaceUtils"%>
<%
	final String my_space_left = MySpaceUtils.deployPath + "jsp/my_space_left.jsp";
%>
<table
	style="border: 1px solid rgb(204, 204, 204); background-color: white;">
	<tr>
		<td valign="top" width="270" nowrap="nowrap" style="background: #eef;"><jsp:include
				page="<%=my_space_left%>"></jsp:include></td>
		<td valign="top" width="100%">
			<jsp:include page="/app/attention/a/my_attention_right.jsp"></jsp:include>
		</td>
	</tr>
</table>
