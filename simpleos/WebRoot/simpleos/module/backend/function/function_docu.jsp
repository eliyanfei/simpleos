<%@page import="net.simpleframework.util.StringUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	final String status = request.getParameter("status");
	final String type = request.getParameter("type");
	final String t = StringUtils.text(request.getParameter("dt"),type);
%>
<div id="function_docu_id"></div>
<script type="text/javascript">
$Actions['myDocuListAct_<%=t%>']('status=<%=status%>');
</script>