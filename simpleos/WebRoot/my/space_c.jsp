<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.my.space.MySpaceUtils"%>
<%
	final String my_space_left = MySpaceUtils.deployPath + "jsp/my_space_left.jsp";
	final String my_space_right = MySpaceUtils.deployPath + "jsp/my_space_right.jsp";
%>
<table style="border: 1px solid rgb(204, 204, 204);">
	<tr>
		<td valign="top" width="270" nowrap="nowrap"><jsp:include
				page="<%=my_space_left%>"></jsp:include></td>
		<td valign="top" width="100%">
			<jsp:include page="<%=my_space_right%>"></jsp:include>
		</td>
	</tr>
</table>