<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="net.simpleframework.content.component.filepager.IFilePagerHandle"%>
<%@ page
	import="net.simpleframework.content.component.filepager.FilePagerUtils"%>
<%@ page
	import="net.simpleframework.web.page.component.ComponentParameter"%>
<%
	final ComponentParameter nComponentParameter = FilePagerUtils
			.getComponentParameter(request, response);
	final IFilePagerHandle fHandle = (IFilePagerHandle) nComponentParameter
			.getComponentHandle();
	final String idParameterName = fHandle
			.getIdParameterName(nComponentParameter);
%>
<form id="__fp_fileForm">
	<input type="hidden" id="<%=idParameterName%>"
		name="<%=idParameterName%>"
		value="<%=request.getParameter(idParameterName)%>" />
</form>
<div style="text-align: right; margin: 6px 4px 0px 0px;">
	<input type="button" value="#(Button.Save)" class="button2"
		onclick="$Actions['ajaxFilePropsSave']();" /> <input type="button"
		value="#(Button.Cancel)"
		onclick="$Actions['editFilepagerWindow'].close();" />
</div>
