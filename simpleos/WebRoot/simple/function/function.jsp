<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String type = request.getParameter("type");
	String op = request.getParameter("op");
%>
<jsp:include page="/simple/include/lc.jsp"><jsp:param
		value="/simple/function/function_left.jsp" name="left" /><jsp:param
		value="/simple/function/function_c.jsp" name="center" /></jsp:include>
		
<script type="text/javascript">
</script>