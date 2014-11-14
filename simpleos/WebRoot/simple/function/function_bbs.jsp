<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@page
	import="net.simpleframework.content.bbs.BbsUtils"%>


<%
	final String bbs_topic_view = BbsUtils.deployPath + "jsp/bbs_topic_mgr_view.jsp";
%>
<div>
	<jsp:include flush="true" page="<%=bbs_topic_view%>"><jsp:param
			value="false" name="showHead" /></jsp:include>
</div>