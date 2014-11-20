<%@page import="net.simpleframework.util.HTMLBuilder"%>
<%@page import="net.simpleframework.web.EFunctionModule"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="net.simpleframework.content.component.newspager.NewsBean"%>
<%@ page
	import="net.simpleframework.content.component.newspager.INewsPagerHandle"%>
<%@ page
	import="net.simpleframework.web.page.component.ComponentParameter"%>
<%@ page import="net.simpleframework.util.ConvertUtils"%>
<%@ page import="net.simpleframework.util.StringUtils"%>
<%@ page import="net.simpleframework.content.EContentStatus"%>
<%@ page
	import="net.simpleframework.content.component.newspager.NewsPagerUtils"%>
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
<div class="newspager_template"><%=ComponentRenderUtils.genParameters(nComponentParameter)%>
	<div class="nav2 clear_float">
		<div id="act_<%=news.getId()%>" style="float: right;">
			<%=StringUtils.blank(nHandle.getActionsHTML(nComponentParameter, news))%>
			<%=HTMLBuilder.SEP%>
			<%=ItSiteUtil.buildComplaint(nComponentParameter, EFunctionModule.news, news.getId())%>
			</div>
		<div style="float: left;"><%=nHandle.getNavigateHTML(nComponentParameter)%></div>
	</div>

	<div class="sj">
		<div class="f2" style="padding-bottom: 8px;"><%=news.getTopic()%></div>
		<div class="clear_float">
			<div style="float: right;"><%=news.getRemarks()%>#(NewsPagerUtils.2)/<%=news.getViews()%>#(NewsPagerUtils.3)
			</div>
			<div style="float: left;">
				<span><%=ConvertUtils.toDateString(news.getCreateDate(), "yyyy-MM-dd")%></span><span style="margin-left: 15px;"><%=StringUtils.blank(news.getAuthor())%></span><span
					style="margin-left: 15px;"><%=StringUtils.blank(news.getSource())%></span>
			</div>
		</div>
	</div>

	<div style="padding: 4px 0px;" class="inherit_c wrap_text"><%=NewsPagerUtils.getNewsContent(nComponentParameter, news)%></div>
</div>
<%
	if (news.isAllowComments() && news.getStatus() == EContentStatus.publish) {
%>
<div id="idNewsRemark"></div>
<%
	}
%>
<style type="text/css">
.newspager_template,#idNewsRemark {
	border: 1px solid #bbb;
	padding: 8px;
	text-align: left;
	-moz-border-radius: 2px;
	-webkit-border-radius: 2px;
	border-radius: 2px;
	background-color: #f9f9f9;
}

.newspager_template .nav2 {
	border-bottom: 1px dashed #ccc;
	padding: 4px 0px;
	margin-bottom: 4px;
}

.newspager_template .sj {
	border-bottom: 1px dashed #ccc;
	text-align: center;
	padding: 10px 0px;
}

#idNewsRemark {
	margin-top: 8px;
}
</style>