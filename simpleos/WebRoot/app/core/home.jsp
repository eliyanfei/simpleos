<%@page import="net.simpleframework.my.home.MyHomeUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@page
	import="net.prj.manager.template.PrjTemplateBean"%><%@page
	import="net.prj.manager.template.PrjTemplateUtils"%><%@page
	import="net.itniwo.commons.StringsUtils"%><%@page
	import="net.simpleframework.util.ConvertUtils"%>

<%
	final PrjTemplateBean templateBean = PrjTemplateUtils
			.getTemplateBean();
	final boolean win = ConvertUtils.toBoolean(
			request.getParameter("win"), false);
	final String templateUrl = "/frame/template/"
			+ (win ? "/t100/c" : StringsUtils.u(
					templateBean.templateId, "/",
					templateBean.templateId)) + ".jsp";
	final String center = MyHomeUtils.deployPath + "jsp/my_home.jsp";
%>
<jsp:include page="<%=templateUrl%>" flush="true">
	<jsp:param value="<%=center%>" name="center" />
</jsp:include>