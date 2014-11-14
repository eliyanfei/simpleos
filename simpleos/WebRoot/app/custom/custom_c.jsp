<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@page import="java.util.Map"%><%@page
	import="java.util.HashMap"%><%@page
	import="net.prj.manager.menu.PrjMenuBean"%><%@page
	import="net.prj.manager.menu.PrjMenuUtils"%>

<%
	String name = "/" + request.getParameter("menu") + ".html";
	PrjMenuBean menuBean = PrjMenuUtils.appModule.getBeanByExp(PrjMenuBean.class, "url=?", new Object[] { name });
	if (menuBean == null)
		return;
%>
<div class="simple_toolbar2" style="min-height: 400px;">
	<%=menuBean.getDescription()%>
</div>