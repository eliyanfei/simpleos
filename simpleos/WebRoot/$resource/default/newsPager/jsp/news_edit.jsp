<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="net.simpleframework.web.page.component.ComponentParameter"%>
<%@ page import="net.simpleframework.content.EContentType"%>
<%@ page
	import="net.simpleframework.content.component.newspager.INewsPagerHandle"%>
<%@ page
	import="net.simpleframework.content.component.newspager.NewsPagerUtils"%>
<%@ page import="net.simpleframework.content.EContentStatus"%>
<%@ page
	import="net.simpleframework.content.component.newspager.ENewsTemplate"%>
<%@ page import="net.simpleframework.util.StringUtils"%>
<%
	final ComponentParameter nComponentParameter = NewsPagerUtils
			.getComponentParameter(request, response);
	final INewsPagerHandle nHandle = (INewsPagerHandle) nComponentParameter
			.getComponentHandle();
	final String idParameterName = nHandle
			.getIdParameterName(nComponentParameter);
%>
<div id="__np__newsAddForm2" class="simple_toolbar">
	<input type="hidden" name="<%=idParameterName%>"
		value="<%=StringUtils.blank(request.getParameter(idParameterName))%>" />
	<table style="width: 100%;" cellpadding="2" cellspacing="0">
		<tr>
			<td class="l">#(news_edit.0)</td>
			<td><select id="nv_template" name="nv_template">
					<%
						for (Enum<?> e : ENewsTemplate.values()) {
					%><option value="<%=e.name()%>"><%=e%></option>
					<%
						}
					%>
			</select></td>
		</tr>
		<tr>
			<td class="l">#(news_edit.1)</td>
			<td><select id="nv_type" name="nv_type">
					<option value="<%=EContentType.normal.name()%>"><%=EContentType.normal%></option>
					<option value="<%=EContentType.recommended.name()%>"><%=EContentType.recommended%></option>
					<option value="<%=EContentType.announce.name()%>"><%=EContentType.announce%></option>
					<option value="<%=EContentType.image.name()%>"><%=EContentType.image%></option>
			</select></td>
		</tr>
		<tr>
			<td class="l">#(news_edit.2)</td>
			<td><select id="nv_status" name="nv_status">
					<option value="<%=EContentStatus.edit.name()%>"><%=EContentStatus.edit%></option>
					<option value="<%=EContentStatus.audit.name()%>"><%=EContentStatus.audit%></option>
					<option value="<%=EContentStatus.publish.name()%>"><%=EContentStatus.publish%></option>
					<option value="<%=EContentStatus.lock.name()%>"><%=EContentStatus.lock%></option>
			</select></td>
		</tr>
	</table>
</div>
<div style="text-align: right; margin-top: 6px;">
	<input type="button" class="button2" value="#(Button.Ok)"
		onclick="$Actions['ajaxNewsSave']();" /> <input type="button"
		value="#(Button.Cancel)"
		onclick="$Actions['newsEditWindow2'].close();" />
</div>
<style type="text/css">
#__np__newsAddForm2 .l {
	width: 70px;
	text-align: right;
}
</style>

