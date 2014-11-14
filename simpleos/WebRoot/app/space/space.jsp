<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@page
	import="net.simpleframework.web.page.PageRequestResponse"%><%@page
	import="net.prj.manager.template.PrjTemplateBean"%><%@page
	import="net.prj.manager.template.PrjTemplateUtils"%><%@page
	import="net.a.ItSiteUtil"%><%@page
	import="net.simpleframework.util.ConvertUtils"%><%@page
	import="net.itsite.utils.StringsUtils"%>
<%
	final PageRequestResponse requestResponse = new PageRequestResponse(request, response);
	final PrjTemplateBean templateBean = PrjTemplateUtils.getTemplateBean();
	final Object userId = ItSiteUtil.getLoginUser(requestResponse).getId();
	final boolean win = ConvertUtils.toBoolean(request.getParameter("win"), false);
	final String templateUrl = "/frame/template/" + (win ? "/t100/c" : StringsUtils.u(templateBean.templateId, "/", templateBean.templateId))
			+ ".jsp";
%>
<jsp:include page="<%=templateUrl %>" flush="true">
	<jsp:param value="/app/space/space_c.jsp" name="center" />
</jsp:include>