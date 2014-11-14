<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.Map"%>
<%@ page import="net.simpleframework.web.page.PageRequestResponse"%>
<%@ page
	import="net.simpleframework.content.component.newspager.NewsBean"%>
<%@ page
	import="net.simpleframework.content.component.newspager.INewsPagerHandle"%>
<%@ page
	import="net.simpleframework.web.page.component.ComponentParameter"%>
<%@ page import="net.simpleframework.content.blog.BlogUtils"%>
<%@ page import="net.simpleframework.content.news.NewsUtils"%>
<%@ page
	import="net.simpleframework.web.page.component.AbstractComponentBean"%>
<%@ page import="net.simpleframework.util.ConvertUtils"%>
<%@ page import="net.simpleframework.content.ContentLayoutUtils"%>
<%@ page import="net.simpleframework.web.EFunctionModule"%>
<%
	final Map<?, ?> row = (Map<?, ?>) request
			.getAttribute("attention_row");
	if (row == null) {
		return;
	}
	final PageRequestResponse requestResponse = new PageRequestResponse(
			request, response);
	EFunctionModule vtype = ConvertUtils.toEnum(EFunctionModule.class,
			row.get("VTYPE"));
	final AbstractComponentBean componentBean = vtype == EFunctionModule.blog ? BlogUtils.applicationModule
			.getComponentBean(requestResponse)
			: NewsUtils.applicationModule
					.getComponentBean(requestResponse);
	if (componentBean != null) {
		final ComponentParameter nComponentParameter = ComponentParameter
				.get(requestResponse, componentBean);
		final INewsPagerHandle nHandle = (INewsPagerHandle) nComponentParameter
				.getComponentHandle();
		final NewsBean news = nHandle.getEntityBeanById(
				nComponentParameter, row.get("ATTENTIONID"));
		if (news != null) {
%>
<div class="rrow"
	style="padding-bottom: 4px; background: url('<%=ContentLayoutUtils
							.dotImagePath(
									nComponentParameter,
									vtype == EFunctionModule.blog ? BlogUtils.deployPath
											: NewsUtils.deployPath)%>') 5px 5px no-repeat;">
	<table style="width: 100%;" cellpadding="0" cellspacing="0">
		<tr>
			<td><%=nHandle.wrapOpenLink(nComponentParameter, news)%></td>
			<td align="right">&nbsp;<a class="a2 nnum"
				onclick="$Actions['attentionUsersWindow']('attentionId=<%=news.getId()%>&vtype=<%=vtype%>');"><%=row.get("C")%></a>&nbsp;</td>
		</tr>
	</table>
</div>
<%
	}
	}
%>