<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="net.simpleframework.web.page.component.ui.pager.TablePagerUtils"%>
<%@ page
	import="net.simpleframework.web.page.component.ComponentParameter"%>
<%@ page
	import="net.simpleframework.web.page.component.ui.pager.PagerUtils"%>
<%
	final ComponentParameter nComponentParameter = PagerUtils
			.getComponentParameter(request, response);
%>
<%=TablePagerUtils.renderTable(nComponentParameter)%>
<script type="text/javascript">
	var pager_init_<%=nComponentParameter.componentBean.hashId()%> = function(action) {
		__table_pager_addMethods(action); 
		<%=TablePagerUtils.renderJavascriptEvent(nComponentParameter)%>
		$Actions["tablePagerHeadTooltip"].createTip(action.pager.select(".thead .lbl"));
	};
</script>


