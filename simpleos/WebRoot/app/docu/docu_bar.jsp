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
	final boolean showOd = ConvertUtils.toBoolean(
			request.getParameter("showOd"), false);
%>
<div id="__barHeader"></div>
<div class="simple_toolbar2">
	<table width="100%" cellpadding="1" cellspacing="0">
		<tr>
			<td width="100%" colspan="2">
				<table width="100%" cellpadding="0" cellspacing="0">
					<tr>
						<td align="left"><input type="button" class="button2"
							value="上传文档" onclick="$Actions.loc('/mydocu.html?id=myUpload');">
						</td>
						<td id="__blog_sech" align="right"></td>
					</tr>
				</table>

			</td>
		</tr>
		<tr>
			<td width="100%" colspan="2">
				<table width="100%" cellpadding="0" cellspacing="0">
					<tr>
						<td align="left"><%=DocuUtils.applicationModule.tabs(requestResponse)%>
						</td>
						<td id="docu_bar_tab" align="right">
							<%
								if (showOd) {
							%> <%=DocuUtils.applicationModule.tabs13(requestResponse)%> <%
 	}
 %>
						</td>
					</tr>

				</table>

			</td>
		</tr>

	</table>
</div>
<script type="text/javascript">
$ready(function(){
		var sb = $Comp.searchButton(function(sp) {
			$Actions.loc("/docu.html".addParameter("_docu_topic=" + $F(sp.down(".txt"))));
		}, function(sp) {
		}, '当前已有<%=DocuUtils.docuCounter%>份文档', 210);
		$("__blog_sech").update(sb);
		<%final String c = WebUtils.toLocaleString(request.getParameter("c"));
			if (StringUtils.hasText(c)) {%>
			sb.down(".txt").setValue("<%=c%>
	");
<%}%>
	});
</script>