<%@page
	import="net.simpleframework.web.page.component.ComponentParameter"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="net.simpleframework.web.page.PageRequestResponse"%>
<%@page import="net.simpleframework.util.ConvertUtils"%>
<%@page import="net.simpleframework.organization.account.IAccount"%>
<%@page import="net.simpleframework.organization.OrgUtils"%>
<%@page import="net.simpleframework.content.ContentUtils"%><%@page
	import="net.itsite.document.docu.DocuBean"%><%@page
	import="net.itsite.document.docu.DocuUtils"%><%@page
	import="net.itsite.ad.AdUtils"%><%@page import="net.itsite.ad.EAd"%><%@page
	import="java.util.Random"%>

<%
	final PageRequestResponse requestResponse = new PageRequestResponse(request, response);
	final DocuBean docuBean = DocuUtils.applicationModule.getViewDocuBean(requestResponse);
	if (docuBean == null) {
		return;
	}
	final String title = docuBean == null ? "" : docuBean.getTitle();
%>
<div class="block_layout1">
	<div class="t f4">
		#(Docu.view.0)
	</div>
	<div class="wrap_text">
		<jsp:include page="docu_user_info.jsp" flush="true"></jsp:include>
	</div>
</div>
<div class="block_layout1" id="recommendId">
	<div class="t1 f4">
		<span class="ts">#(Docu.view.1)</span>
	</div>
	<div class="wrap_text">
		<%
			request.setAttribute("__qs", DocuUtils.queryRelatedDocu(requestResponse, docuBean == null ? "" : docuBean.getTitle()));
		%>
		<jsp:include page="view_layout_show.jsp" flush="true">
			<jsp:param value="dot1" name="dot" />
			<jsp:param value="7" name="rows" />
		</jsp:include></div>
</div>
<jsp:include page="/app/common/ad_bar_right.jsp"></jsp:include>