<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.my.file.component.fileselect.FileSelectUtils"%>
<%@ page import="net.simpleframework.content.component.filepager.FileBean"%>
<%@ page import="net.simpleframework.util.StringUtils"%>
<%@ page import="net.simpleframework.util.HTMLUtils"%>
<%@ page import="net.simpleframework.util.ConvertUtils"%>
<%@ page import="net.simpleframework.util.NumberUtils"%>
<%@ page import="net.simpleframework.util.IoUtils"%>
<%@ page import="net.simpleframework.web.page.PageRequestResponse"%>
<%
	final FileBean file = FileSelectUtils
			.getFileBean(new PageRequestResponse(request, response));
	if (file == null) {
%>
<div class="important-tip">#(FileSelectAction.1)</div>
<%
	return;
	}
%>
<table class="file_tooltip fixed_table" style="width: 100%;" cellpadding="2">
	<tr>
		<td class="lbl">#(file_download.1)</td>
		<td>
		<div class="wrap_text"><%=file.getFilename()%></div>
		</td>
	</tr>
	<tr>
		<td class="lbl">#(file_download.2)</td>
		<td class="wrap_text"><%=HTMLUtils.convertHtmlLines(StringUtils.text(
					file.getDescription(), "#(file_download.3)"))%></td>
	</tr>
	<tr>
		<td class="lbl">#(file_download.4)</td>
		<td><%=IoUtils.toFileSize(file.getFilesize())%> ( <%=NumberUtils.format(",###", file.getFilesize())%>
		<span>#(file_download.5)</span> )</td>
	</tr>
	<tr>
		<td class="lbl">#(file_download.6)</td>
		<td><%=ConvertUtils.toDateString(file.getCreateDate())%></td>
	</tr>
	<tr>
		<td class="lbl">#(file_download.7)</td>
		<td><%=file.getDownloads()%></td>
	</tr>
</table>
<style type="text/css">
.file_tooltip .lbl {
	width: 60px;
	text-align: right;
	color: #2283CF;
}
</style>