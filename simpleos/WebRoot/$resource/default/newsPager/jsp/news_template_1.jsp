<%@page import="net.simpleframework.web.EFunctionModule"%>
<%@page import="net.simpleframework.util.HTMLBuilder"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="net.simpleframework.content.component.newspager.INewsPagerHandle"%>
<%@ page
	import="net.simpleframework.content.component.newspager.NewsPagerUtils"%>
<%@ page
	import="net.simpleframework.web.page.component.ComponentParameter"%>
<%@ page
	import="net.simpleframework.content.component.newspager.NewsBean"%>
<%@ page import="net.simpleframework.util.ConvertUtils"%>
<%@ page import="net.simpleframework.util.StringUtils"%>
<%@ page
	import="net.simpleframework.web.page.component.ComponentRenderUtils"%><%@page
	import="net.itsite.ItSiteUtil"%>

<%
	final ComponentParameter nComponentParameter = NewsPagerUtils.getComponentParameter(request, response);
	final INewsPagerHandle nHandle = (INewsPagerHandle) nComponentParameter.getComponentHandle();
	final NewsBean news = nHandle.getEntityBeanByRequest(nComponentParameter);
	if (news == null) {
		return;
	}
	ItSiteUtil.addMenuNav(request.getSession(), null, news.getTopic(), false);
%>
<div class="newspager_template">
	<%=ComponentRenderUtils.genParameters(nComponentParameter)%>
	<div class="nav clear_float">
		<div style="float: right;" id="act_<%=news.getId()%>"><%=StringUtils.blank(nHandle.getActionsHTML(nComponentParameter, news))%>
		<%=HTMLBuilder.SEP%>
			<%=ItSiteUtil.buildComplaint(nComponentParameter, EFunctionModule.blog, news.getId())%>
		</div>
		<div style="float: left;"><%=StringUtils.blank(nHandle.getNavigateHTML(nComponentParameter))%></div>
	</div>
	<div class="f3" style="text-align: center;"><%=news.getTopic()%></div>
	<div class="stat clear_float">
		<div style="float: right;"><%=news.getRemarks()%>#(NewsPagerUtils.2)/<%=news.getViews()%>#(NewsPagerUtils.3)
		</div>
		<div style="float: left;">
			<span>#(NewsPagerUtils.1): <%=ConvertUtils.toDateString(news.getCreateDate())%></span>
			<%
				final String author = news.getAuthor();
				if (StringUtils.hasText(author)) {
			%><span style="margin-left: 10px;">#(np_edit.3): <%=author%></span>
			<%
				}
			%>
		</div>
	</div>
	<div style="padding: 4px 0px;" class="inherit_c wrap_text"><%=NewsPagerUtils.getNewsContent(nComponentParameter, news)%></div>
</div>
<%
	if (news.isAllowComments()) {
%>
<div id="idNewsRemark"></div>
<%
	}
%>
<style>
.newspager_template,#idNewsRemark {
	border: 1px solid #bbb;
	-moz-border-radius: 2px;
	-webkit-border-radius: 2px;
	border-radius: 2px;
	padding: 8px;
	background-color: #f9f9f9;
}

.newspager_template .nav {
	padding: 2px 0px 10px 0px;
}

.newspager_template .stat {
	margin: 10px 0px 2px 0px;
	padding-bottom: 4px;
	border-bottom: 1px dashed #ccc;
}

#idNewsRemark {
	margin-top: 8px;
}
</style>
