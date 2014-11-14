<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@page
	import="net.simpleframework.web.page.PageRequestResponse"%><%@page
	import="net.itsite.document.docu.DocuUtils"%>

<%
	final PageRequestResponse requestResponse = new PageRequestResponse(request, response);
%>
<jsp:include page="/app/docu/docu_bar.jsp"></jsp:include>
<table width="100%">
	<tr>
		<td valign="top">
			<div class="simple_tabs_content space_tabs_content">
				<div id="documentNav"><%=DocuUtils.documentNav(requestResponse)%></div>
				<div class="space_content_item">
					<div id="mydocuId"></div>
				</div>
			</div>
		</td>
		<td width="260" valign="top">
			<jsp:include page="/app/docu/layout_right.jsp"></jsp:include>
		</td>
	</tr>
</table>

