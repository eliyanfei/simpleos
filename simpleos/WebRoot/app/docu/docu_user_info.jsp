<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@page
	import="net.simpleframework.web.page.PageRequestResponse"%><%@page
	import="net.itsite.document.docu.DocuBean"%><%@page
	import="net.itsite.document.docu.DocuUtils"%><%@page
	import="net.simpleframework.organization.IUser"%><%@page
	import="net.simpleframework.content.ContentUtils"%><%@page
	import="net.simpleframework.util.ConvertUtils"%><%@page
	import="net.simpleframework.util.IoUtils"%><%@page
	import="net.simpleframework.util.StringUtils"%><%@page
	import="net.simpleframework.util.HTMLBuilder"%><%@page
	import="net.itsite.document.docu.EDocuFunction"%><%@page
	import="net.a.ItSiteUtil"%>

<%
	final PageRequestResponse requestResponse = new PageRequestResponse(
			request, response);
	final DocuBean docuBean = DocuUtils.applicationModule
			.getViewDocuBean(requestResponse);
	if (docuBean == null) {
		return;
	}
	final IUser user = ItSiteUtil.getUserById(docuBean.getUserId());
%>
<div>
	<table>
		<tr>
			<td valign="top"><%=ContentUtils.getAccountAware().wrapImgAccountHref(
					requestResponse, user)%></td>
			<td valign="top">
				<table>
					<tr>
						<td align="right">#(Docu.user.0)：</td>
						<td colspan="3"><%=user == null ? "匿名" : user.getText()%></td>
					</tr>
					<tr>
						<td align="right">#(Docu.user.1)：</td>
						<td colspan="3" class="nnum"><%=ConvertUtils.toDateString(docuBean.getCreateDate(),
					"yyyy-MM-dd")%></td>
					</tr>
					<tr>
						<td align="right">#(Docu.down)：</td>
						<td><a class="nnum"
							onclick="$Actions['downUsersWindowAct']('docuId=<%=docuBean.getId()%>');"><%=docuBean.getDownCounter()%></a>
						</td>
						<td align="right">#(Docu.read)：</td>
						<td class="nnum"><%=docuBean.getViews()%></td>
					</tr>
					<tr>
						<td align="right">#(Docu.remark)：</td>
						<td class="nnum"><%=docuBean.getRemarks()%></td>
						<td align="right">#(Docu.user.2)：</td>
						<td class="nnum"><%=IoUtils.toFileSize(docuBean.getFileSize())%></td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td></td>
			<td><input type="button" class="button2" value="我要下载"
				onclick="$Actions['docuDownWindowAct']('docuId=<%=docuBean.getId()%>');">
		</tr>
		<tr>
			<td colspan="2">
				<%
					if (StringUtils.hasText(docuBean.getContent())) {
				%> <span id="dc_s"> <%=ItSiteUtil.getShortString(docuBean.getContent(), 20,
						true)%> <%
 	if (docuBean.getContent().length() > 20) {
 %> <a onclick="$('dc_s').$toggle();$('dc_m').$toggle();">#(Docu.user.4)»</a>
			</span> <span id="dc_m" style="display: none;"> <%=docuBean.getContent()%>
					<a onclick="$('dc_s').$toggle();$('dc_m').$toggle();">#(Docu.user.5)»</a>
			</span> <%
 	}
 	}
 %>
			</td>
		</tr>
	</table>
</div>
