<%@page import="net.simpleframework.ado.db.IQueryEntitySet"%>
<%@page import="net.simpleos.backend.links.LinksUtils"%>
<%@page import="net.simpleos.backend.links.LinksBean"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<style>
<!--
 .h3p {
	font-size: 16px;
	color: #666666;
	font-weight:bold;
	border-bottom: dashed 1px #CCCCCC;
	line-height: 2.5;
	padding-bottom: 10px;
}
-->
</style>
<div style="background: white;min-height: 450px;">
	<div class="h3p">友情链接</div>
	<%
		IQueryEntitySet<LinksBean> qs = LinksUtils.appModule
		.queryBean(LinksBean.class);
		LinksBean linksBean = null;
		while ((linksBean = qs.next()) != null) {
	%>
	<a style='color:<%=linksBean.getColor()%>;margin-right: 20px;'
		href='<%=linksBean.getUrl()%>' target='blank'><%=linksBean.getTitle()%></a>
	<%
		}
	%>
</div>