<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@page
	import="net.simpleframework.web.page.PageRequestResponse"%><%@page
	import="net.itsite.document.docu.DocuUtils"%><%@page
	import="net.simpleframework.util.ConvertUtils"%><%@page
	import="net.a.ItSiteUtil"%><%@page
	import="net.itsite.document.docu.EDocuFunction"%><%@page
	import="net.simpleframework.web.WebUtils"%><%@page
	import="net.simpleframework.util.StringUtils"%>
<%
	final PageRequestResponse requestResponse = new PageRequestResponse(
			request, response);
	final boolean manager = ItSiteUtil.isManage(requestResponse,
			DocuUtils.applicationModule);
%>
<div class="simple_toolbar2">
	<table width="100%" cellpadding="1" cellspacing="0">
		<tr>
			<td width="100%" colspan="2">
				<table width="100%" cellpadding="0" cellspacing="0">
					<tr>
						<td align="left"><input type="button" class="button2"
							value="#(Docu.all.0)"
							onclick="$Actions.loc('/mydocu.html?id=myUpload');"> <%
 	if (manager) {
 		out.write("<a class=\"a2\" style=\"vertical-align: middle;\" onclick=\"$Actions['docuMgrToolsWindowAct']();\">管理员工具</a>");
 	}
 %></td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
</div>
<div id="alldocuId"></div>