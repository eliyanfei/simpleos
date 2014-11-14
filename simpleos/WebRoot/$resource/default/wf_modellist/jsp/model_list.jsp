<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="net.simpleframework.web.page.component.ui.pager.PagerUtils"%>
<%@ page
	import="net.simpleframework.web.page.component.ComponentParameter"%>
<%@ page
	import="net.simpleframework.web.page.component.ui.pager.TablePagerUtils"%>
<%
	final ComponentParameter nComponentParameter = PagerUtils
			.getComponentParameter(request, response);
	final String hashId = nComponentParameter.componentBean.hashId();
%>
<%=TablePagerUtils.renderTable(nComponentParameter)%>
<script type="text/javascript">
	var pager_init_<%=hashId%> = function(action) {
		__table_pager_addMethods(action);	
		
		action.bindMenu("ml_<%=hashId%>_Menu");
		
		action.upload_model = function() {	
			var act = $Actions['ml_upload_window'];
			act.pager = action;
			act();
		};
		
		action.del = function(item) {
			var act = $Actions['ml_delete_model'];
			act.selector = action.selector;
			act('modelId=' + action.rowId(item));
		};
		
		action.opt = function(item) {
			var act = $Actions['ml_opt_window'];
			act('modelId=' + action.rowId(item));
		};
	};
</script>
