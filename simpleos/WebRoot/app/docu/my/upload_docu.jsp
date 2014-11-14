<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@page
	import="net.itsite.document.docu.EDocuFunction"%><%@page
	import="net.itsite.document.docu.DocuUtils"%><%@page
	import="net.simpleframework.util.StringUtils"%><%@page
	import="net.itsite.document.docu.DocuBean"%><%@page
	import="net.simpleframework.web.page.PageRequestResponse"%><%@page
	import="net.simpleframework.web.WebUtils"%><%@page import="net.a.ItSiteUtil"%>

<%
	final String docuId = request.getParameter("docuId");
	final boolean hasDocu = StringUtils.hasText(docuId);
	final DocuBean docuBean = DocuUtils.applicationModule.getBean(DocuBean.class, docuId);
	final PageRequestResponse requestResponse = new PageRequestResponse(request, response);
	final String upload = WebUtils.addParameters(DocuUtils.applicationModule.getViewUrl1(requestResponse, 4, 0, 0), "id=myUpload");
%>
<div>
	<input type="hidden" id="docuId" name="docuId">
	<div id="docu_content_" style="display: <%=hasDocu ? "" : "none"%>">
		<table width="100%'">
			<tbody>
				<tr>
					<td class="l">
						#(Docu.add.0)：
					</td>
					<td>
						<input type="text" id="docu_title" name="docu_title"
							style="width: 90%">
					</td>
				</tr>
				<tr>
					<td class="l" valign="top">
						#(Docu.add.1)：
					</td>
					<td>
						<textarea id="docu_content" name="docu_content" rows="5"
							style="width: 90%"></textarea>
					</td>
				</tr>
				<tr>
					<td class="l">
						#(Docu.add.2)：
					</td>
					<td>
						<input type="text" id="docu_keyworks" name="docu_keyworks"
							style="width: 90%">
						<div id="docu_tag"></div>
					</td>
				</tr>
				<tr>
					<td class="l">
						#(Docu.add.3)：
					</td>
					<td>
						<div id="td_docu_catalog" style="width: 90%"></div>
						<input type="hidden" name="docu_catalog" id="docu_catalog">
					</td>
				</tr>
			</tbody>
		</table>
	</div>
</div>
<div align="center">
	<%
		if (hasDocu) {
	%>
	<input type="button" id="uploadDocuBtn" class="button2"
		onclick="$IT.A('docuSaveAct');" value="#(Docu.add.4)">
	<%
		} else {
	%>
	<div style="color: red;">
		#(Docu.add.7)
	</div>
	<input type="button" id="uploadDocuBtn" class="button2"
		onclick="$Actions.loc('<%=upload%>');" value="#(Docu.add.8)">
	<%
		}
	%>
</div>
<style type="text/css">
#docu_content_ .l {
	width: 100px;
	text-align: right;
}

#docu_content_ {
	background: #f7f7ff;
	padding: 6px 8px;
}
</style>
<script type="text/javascript">
$ready(function() {
	addTextButton("docu_catalog_text", "docuCatalogAct", "td_docu_catalog",
			false);
});
</script>