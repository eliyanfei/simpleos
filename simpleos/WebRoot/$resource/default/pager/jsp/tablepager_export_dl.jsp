<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="net.simpleframework.web.page.component.ui.pager.PagerUtils"%>
<%@ page
	import="net.simpleframework.web.page.component.ComponentParameter"%>
<%@ page
	import="net.simpleframework.web.page.component.ui.pager.ITablePagerHandle"%>
<%@ page import="net.simpleframework.util.StringUtils"%>
<%@ page
	import="net.simpleframework.web.page.component.ui.pager.EExportFileType"%>
<%@ page import="net.simpleframework.util.ConvertUtils"%>
<%@ page
	import="net.simpleframework.web.page.component.ui.pager.TablePagerColumn"%>
<%@ page import="java.util.LinkedHashMap"%>
<%@ page import="java.util.Map"%>
<%@ page
	import="net.simpleframework.web.page.component.ui.pager.TablePagerUtils"%>
<%
	try {
		final ComponentParameter nComponentParameter = PagerUtils
				.getComponentParameter(request, response);
		final ITablePagerHandle tHandle = (ITablePagerHandle) nComponentParameter
				.getComponentHandle();
		final String[] arr = StringUtils.split(request.getParameter("v"), ";");
		LinkedHashMap<String, TablePagerColumn> columns = null;
		if (arr != null && arr.length > 0) {
			columns = new LinkedHashMap<String, TablePagerColumn>();
			Map<String, TablePagerColumn> all = TablePagerUtils
					.getTablePagerData(nComponentParameter)
					.getTablePagerColumns();
			for (String v : arr) {
				TablePagerColumn oCol = all.get(v);
				if (oCol != null) {
					columns.put(v, oCol);
				}
			}
		}
		tHandle.export(
				nComponentParameter,
				ConvertUtils.toEnum(EExportFileType.class,
						request.getParameter("filetype")), columns);
	} finally {
		try {
			out.clear();
		} catch (Throwable th) {
		}
	}
%>