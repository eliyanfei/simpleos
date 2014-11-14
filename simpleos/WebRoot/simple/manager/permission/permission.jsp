<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	final String permission_tree = "/app/manager/permission/permission_tree.jsp";
	final String permission_list = "/app/manager/permission/permission_list.jsp";
%>
<jsp:include page="/simple/include/lc.jsp" flush="true">
	<jsp:param value="<%=permission_tree%>" name="left" />
	<jsp:param value="<%=permission_list%>" name="center" />
</jsp:include>
