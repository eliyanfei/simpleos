<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.content.bbs.BbsUtils"%><%@page
	import="net.a.ItSiteUtil"%>

<%
	final String bbs_user = BbsUtils.deployPath + "jsp/bbs_user.jsp";
	ItSiteUtil.addMenuNav(request.getSession(), null, "#(Itsite.menu.bbs)", false);
%>
<table width="100%">
	<tr>
		<td valign="top">
			<jsp:include page="<%=bbs_user%>"></jsp:include>
		</td>
	</tr>
</table>
