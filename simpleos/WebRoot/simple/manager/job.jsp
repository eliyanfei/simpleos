<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.organization.OrgUtils"%>
<%
	final String left = OrgUtils.deployPath + "jsp/job/jobchart_c.jsp";
	final String center = OrgUtils.deployPath + "jsp/job/job_c.jsp";
%>
<jsp:include page="/simple/include/lc1.jsp" flush="true">
	<jsp:param value="<%=left%>" name="left" />
	<jsp:param value="<%=center%>" name="center" />
</jsp:include>