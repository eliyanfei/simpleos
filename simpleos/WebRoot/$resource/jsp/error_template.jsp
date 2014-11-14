<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.util.HTTPUtils"%>
<script type="text/javascript">
  (window.parent || window).location = 
    "<%=HTTPUtils.wrapContextPath(request, request.getParameter("systemErrorPage"))%>";
</script>