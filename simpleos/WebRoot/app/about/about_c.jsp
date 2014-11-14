<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@page import="java.util.Map"%><%@page
	import="java.util.HashMap"%><%@page
	import="net.prj.manager.PrjMgrUtils"%>
<%
	Map<String, String> map = new HashMap<String, String>();
	map.putAll(PrjMgrUtils.loadCustom("company"));
%>
<div class="simple_toolbar2">
	<p class="f2">
		<%=map.get("company_name")%>
	</p>
	<div style="font-size: 9.5pt">
		<%=map.get("company_desc")%>
	</div>
	<p class="f2">
		#(App.About.0)
	</p>
	<p style="font-size: 9.5pt">
		<%=map.get("company_content")%>
	</p>
</div>