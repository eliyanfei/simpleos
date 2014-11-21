<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	final String status = request.getParameter("status");
	final String type = request.getParameter("type");
%>
<div id="function_docu_id"></div>
<script type="text/javascript">
$Actions['myDocuListAct_<%=type%>']('status=<%=status%>');
</script>