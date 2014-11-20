<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@page
	import="net.simpleframework.web.page.PageRequestResponse"%><%@page
	import="net.itsite.document.docu.DocuBean"%><%@page
	import="net.itsite.document.docu.DocuUtils"%><%@page
	import="net.simpleframework.util.IoUtils"%><%@page
	import="net.simpleframework.organization.OrgUtils"%><%@page
	import="net.itsite.ItSiteUtil"%><%@page
	import="net.simpleframework.organization.account.IAccount"%><%@page
	import="java.util.Random"%><%@page
	import="net.simpleframework.organization.account.Account"%>

<%
	final PageRequestResponse requestResponse = new PageRequestResponse(
			request, response);
	IAccount account = ItSiteUtil.getLoginAccount(requestResponse);
	final DocuBean docuBean = DocuUtils.applicationModule
			.getViewDocuBean(requestResponse);
	if (account == null) {
		account = new Account();
	}
	if (docuBean == null || account == null) {
		return;
	}
%>
<style>

.padd1 {
	line-height: 25px;
	font-size: 14px;
	color: black;
}
</style>
<div id="docu_down" style="background: #EBF7FA;height: 100%;overflow: auto;">
	<div class="bor">
		<div class="padd">
			<div class="padd1">
				<table>
					<tr>
						<td nowrap="nowrap" valign="top">#(Docu.down.0)：</td>
						<td><%=docuBean.getTitle()%><img
							alt="<%=docuBean.getExtension()%>" style="margin-left: 5px;"
							src="<%=DocuUtils.getFileImage(requestResponse, docuBean)%>">
						</td>
					</tr>
					<tr>
						<td nowrap="nowrap">#(Docu.down.1)：</td>
						<td class="nnum"><%=IoUtils.toFileSize(docuBean.getFileSize())%>
						</td>
					</tr>
				</table>
			</div>
			<div class="padd2" align="center">
				<input type="button" value="#(Docu.down)" class="button2"
					style="font-weight: bold; color: #A41C05;"
					onclick="$Actions['docuDownloadAct']('docuId=<%=docuBean.getId()%>&s=<%=docuBean.getFileSize()%>');">
			</div>
		</div>
	</div>
</div>