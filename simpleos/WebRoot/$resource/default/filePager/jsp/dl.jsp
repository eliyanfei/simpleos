<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="net.simpleframework.content.component.filepager.FilePagerUtils"%>
<%@ page
	import="net.simpleframework.web.page.component.ComponentParameter"%>
<%@ page
	import="net.simpleframework.my.file.component.fileselect.FileSelectUtils"%>
<%
	try {
		final ComponentParameter nComponentParameter = FilePagerUtils
				.getComponentParameter(request, response);
		if (nComponentParameter.componentBean == null) {
			// 此处可以不要，主要是兼容老系统  
			nComponentParameter.componentBean = FileSelectUtils
					.getFilePager(nComponentParameter);
		}
		FilePagerUtils.doDownload(nComponentParameter);
	} finally {
		try {
			out.clear();
		} catch (Throwable th) {
		}
	}
%>