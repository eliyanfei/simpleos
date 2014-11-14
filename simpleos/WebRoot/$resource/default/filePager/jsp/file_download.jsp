<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="net.simpleframework.content.component.filepager.IFilePagerHandle"%>
<%@ page
	import="net.simpleframework.content.component.filepager.FileBean"%>
<%@ page
	import="net.simpleframework.content.component.filepager.FilePagerUtils"%>
<%@ page import="net.simpleframework.util.IoUtils"%>
<%@ page import="net.simpleframework.util.StringUtils"%>
<%@ page import="net.simpleframework.util.NumberUtils"%>
<%@ page import="net.simpleframework.util.ConvertUtils"%>
<%@ page import="net.simpleframework.util.HTMLUtils"%>
<%@ page
	import="net.simpleframework.web.page.component.ComponentParameter"%>
<%
	final ComponentParameter nComponentParameter = FilePagerUtils
			.getComponentParameter(request, response);
	final IFilePagerHandle fHandle = (IFilePagerHandle) nComponentParameter
			.getComponentHandle();
	final String fileId = fHandle
			.getIdParameterName(nComponentParameter);
	final FileBean file = fHandle
			.getEntityBeanByRequest(nComponentParameter);
%>
<div class="filedownload">
	<div class="simple_toolbar3">
		<table style="width: 100%;">
			<tr>
				<td class="ft"><%=FilePagerUtils
					.getFileImage(nComponentParameter, file, 32)%></td>
				<td align="right">
					<div class="btn">
						<a
							onclick="$Actions['ajaxDownloadFile']('<%=fileId%>=<%=file.getId()%>');">#(file_download.0)</a>
					</div>
				</td>
			</tr>
		</table>
	</div>
	<div class="simple_toolbar1">
		<table style="width: 100%; border-spacing: 4px;" class="fixed_table">
			<tr>
				<td class="lbl">#(file_download.1)</td>
				<td>
					<div class="wrap"><%=file.getFilename()%></div>
				</td>
			</tr>
			<tr>
				<td class="lbl">#(file_download.2)</td>
				<td><%=HTMLUtils.convertHtmlLines(StringUtils.text(
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
			<tr>
				<td class="lbl">#(file_download.8)</td>
				<td><%=StringUtils.blank(file.getIp())%></td>
			</tr>
			<tr>
				<td class="lbl">#(file_download.9)</td>
				<td>
					<div class="wrap_text"><%=FilePagerUtils.getMd5(nComponentParameter, file)%></div>
				</td>
			</tr>
			<tr>
				<td class="lbl">#(file_download.10)</td>
				<td>
					<div class="wrap_text"><%=FilePagerUtils.getSha1(nComponentParameter, file)%></div>
				</td>
			</tr>
		</table>
	</div>
	<div class="close">
		<input type="button" value="#(Button.Cancel)"
			onclick="$Actions['filepagerDownloadWindow'].close();" />
	</div>
</div>
