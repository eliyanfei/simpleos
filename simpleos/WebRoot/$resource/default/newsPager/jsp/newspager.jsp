<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="net.simpleframework.web.page.component.ui.pager.TablePagerUtils"%>
<%@ page
	import="net.simpleframework.content.component.newspager.INewsPagerHandle"%>
<%@ page
	import="net.simpleframework.web.page.component.ComponentParameter"%>
<%@ page
	import="net.simpleframework.content.component.newspager.NewsPagerUtils"%>
<%
	final ComponentParameter nComponentParameter = NewsPagerUtils
			.getComponentParameter(request, response);
	final INewsPagerHandle nHandle = (INewsPagerHandle) nComponentParameter
			.getComponentHandle();

	final String idParameterName = nHandle
			.getIdParameterName(nComponentParameter);
%>
<%=TablePagerUtils.renderTable(nComponentParameter)%>
<script type="text/javascript">
	var pager_init_<%=nComponentParameter.componentBean.hashId()%> = function(action) {
		__table_pager_addMethods(action);	
		action.bindMenu("newsPager_Menu");
		__newspager_addMethods(action, "<%=idParameterName%>");
	};
</script>