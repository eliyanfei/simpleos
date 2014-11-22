<%@page import="net.simpleos.SimpleosUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@page import="java.util.Map"%>
<%
	Map<String, String> map = SimpleosUtil.attrMap;
%>
<div class="simple_toolbar2">
	<p class="f2">
		<%=map.get("company.company_name")%>
	</p>
	<div style="font-size: 9.5pt">
		<%=map.get("company.company_desc")%>
	</div>
	<p class="f2">#(App.About.0)</p>
	<p style="font-size: 9.5pt">
		<%=map.get("company.company_content")%>
	</p>
</div>