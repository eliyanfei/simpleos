<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="net.simpleframework.web.page.component.ui.pager.TablePagerUtils"%>
<%@ page
	import="net.simpleframework.content.component.filepager.IFilePagerHandle"%>
<%@ page
	import="net.simpleframework.web.page.component.ComponentParameter"%>
<%@ page
	import="net.simpleframework.content.component.filepager.FilePagerUtils"%>
<%
	final ComponentParameter nComponentParameter = FilePagerUtils
			.getComponentParameter(request, response);
	final IFilePagerHandle fHandle = (IFilePagerHandle) nComponentParameter
			.getComponentHandle();

	final String idParameterName = fHandle
			.getIdParameterName(nComponentParameter);
%>
<%=TablePagerUtils.renderTable(nComponentParameter)%>
<script type="text/javascript">
	var pager_init_<%=nComponentParameter.componentBean.hashId()%> = function(action) {
		__table_pager_addMethods(action);	
		__filepager_addMethods(action, "<%=idParameterName%>");
		
		action.bindMenu("filePager_Menu");
	};
</script>