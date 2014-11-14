<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="net.simpleframework.content.component.remark.RemarkUtils"%>
<%@ page
	import="net.simpleframework.web.page.component.ComponentParameter"%>
<%@ page
	import="net.simpleframework.web.page.component.ComponentRenderUtils"%>
<%
	final ComponentParameter nComponentParameter = RemarkUtils.getComponentParameter(request, response);
	final String hashId = nComponentParameter.componentBean.hashId();
%>
<div class="remark">
	<%=ComponentRenderUtils.genParameters(nComponentParameter)%>
	<div id="remark_list_<%=hashId%>"></div>
	<div id="remark_tb_<%=hashId%>" class="tb">
		<div class="simple_toolbar1">
			<textarea id="textareaRemarkEditor" name="textareaRemarkEditor"
				rows="5"></textarea>
		</div>
		<table style="width: 100%;" cellpadding="0" cellspacing="0">
			<tr>
				<td valign="top">
					<div class="ins">
						<a class="emotion" onclick="$Actions['smileyRemarkDict']();">#(remark.12)</a>
					</div>
				</td>
				<td width="80" align="right">
					<span class="btn" id="submitRemarkEditor"
						onclick="$Actions['ajaxSave2Remark']();"><%=nComponentParameter.getBeanProperty("actionText")%></span>
				</td>
			</tr>
		</table>
		<div style="padding: 4px 8px;">
			<table cellpadding="0" cellspacing="0">
				<tr>
					<%
						if ((Boolean) nComponentParameter.getBeanProperty("showValidateCode")) {
					%>
					<td id="remarkEditorValidateCode"></td>
					<%
						}
					%>
					<%
						if ((Boolean) nComponentParameter.getBeanProperty("showHigh")) {
					%>
					<td align="right" valign="middle">
						<a onclick="__remark_window(this);">#(remark.11)</a>
					</td>
					<%
						}
					%>
				</tr>
			</table>
		</div>
	</div>
</div>