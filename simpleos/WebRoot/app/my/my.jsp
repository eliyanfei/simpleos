<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@page
	import="net.prj.manager.template.PrjTemplateBean"%><%@page
	import="net.simpleframework.organization.component.userpager.UserPagerUtils"%><%@page
	import="net.prj.manager.template.PrjTemplateUtils"%><%@page
	import="net.a.ItSiteUtil"%><%@page
	import="net.simpleframework.util.ConvertUtils"%><%@page
	import="net.itsite.utils.StringsUtils"%><%@page
	import="net.simpleframework.web.page.PageRequestResponse"%>
<%
	final PageRequestResponse requestResponse = new PageRequestResponse(request, response);
	final PrjTemplateBean templateBean = PrjTemplateUtils.getTemplateBean();
	final String center = UserPagerUtils.getHomePath() + "/jsp/ue/user_edit.jsp";
	final Object userId = ItSiteUtil.getLoginUser(requestResponse).getId();
	final boolean win = ConvertUtils.toBoolean(request.getParameter("win"), false);
	final String templateUrl = "/frame/template/" + (win ? "/t100/c" : StringsUtils.u(templateBean.templateId, "/", templateBean.templateId))
			+ ".jsp";
%>
<jsp:include page="<%=templateUrl %>" flush="true">
	<jsp:param value="<%=center %>" name="center" />
	<jsp:param value="<%=userId %>" name="userid" />
</jsp:include>