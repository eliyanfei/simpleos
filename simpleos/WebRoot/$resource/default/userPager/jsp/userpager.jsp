<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="net.simpleframework.web.page.component.ui.pager.TablePagerUtils"%>
<%@ page import="net.simpleframework.organization.OrgUtils"%>
<%@ page
	import="net.simpleframework.web.page.component.ComponentParameter"%>
<%@ page
	import="net.simpleframework.organization.component.userpager.UserPagerUtils"%>
<%
	final String idParameterName = OrgUtils.um()
			.getUserIdParameterName();
	final ComponentParameter nComponentParameter = UserPagerUtils
			.getComponentParameter(request, response);
%>
<%=TablePagerUtils.renderTable(nComponentParameter)%>
<script type="text/javascript">
	var pager_init_<%=nComponentParameter.componentBean.hashId()%> = function(action) {
		__table_pager_addMethods(action);	
		action.bindMenu("userPager_Menu");

		action.add = function(params) {
			var aa = $Actions["ajaxAddUserPage"];
			aa.selector = action.selector;
			$Actions["addUserWindow"](params);
		};

		action.edit = function(o, params) {
			var aa = $Actions["ajaxEditUserPage"];
			aa.selector = action.selector;
			$Actions["editUserWindow"]((
					"<%=idParameterName%>=" + action.rowId(o)).addParameter(params));
		};

		action.active = function(o, params) {
			var da = $Actions["userPagerActive"];
			da.selector = action.selector;
			da(("<%=idParameterName%>=" + action.rowId(o)).addParameter(params));	
		};
		
		action.del = function(o, params) {
			var da = $Actions["userPagerDelete"];
			da.selector = action.selector;
			da(("<%=idParameterName%>=" + action.rowId(o)).addParameter(params));	
		};
		action.lock = function(o, params) {
			var da = $Actions["userPagerLock"];
			da.selector = action.selector;
			da(("<%=idParameterName%>=" + action.rowId(o)).addParameter(params));	
		};
		action.unlock = function(o, params) {
			var da = $Actions["userPagerUnlock"];
			da.selector = action.selector;
			da(("<%=idParameterName%>=" + action.rowId(o)).addParameter(params));	
		};

		action.del2 = function(params) {
			action.__checkall(function(ids) {
				var da = $Actions["userPagerDelete"];
				da.selector = action.selector;
				da(("<%=idParameterName%>=" + ids).addParameter(params));
			});
		};

		action.undel = function(o, params) {
			var da = $Actions["userPagerUnDelete"];
			da.selector = action.selector;
			da(("<%=idParameterName%>=" + action.rowId(o)).addParameter(params));
		};

		action.move2dept = function(o, params) {
			var win = $Actions['move2DeptWindow'];
			win._move2dept_callback = function(selects) {
				var id = selects[0].id;
		    if (id == "root") 
		    	return false;
		    var ma = $Actions["ajaxMove2Dept"];
		    ma.selector = action.selector;
		    ma("<%=idParameterName%>=" + action.rowId(o) + 
		  	    "&mDepartment=" + id);
		    return true;
			};
			win.selector = action.selector;
			win(params);
		};

		action.account_stat = function(o, params) {
			var aa = $Actions["ajaxUserAccountStatPage"];
			aa.selector = action.selector;
			$Actions["userAccountStatWindow"](("<%=idParameterName%>=" + 
					action.rowId(o)).addParameter(params));
		};

		action.account_binding = function(o, params) {
			var aa = $Actions["ajaxUserAccountBindingPage"];
			aa.selector = action.selector;
			$Actions["userAccountBindingWindow"](("<%=idParameterName%>=" + 
					action.rowId(o)).addParameter(params));
		};
		
		action.sentmail = function(params) {
			action.__checkall(function(ids) {
				var da = $Actions["userSentMailWindow"];
				da.selector = action.selector;
				da(("<%=idParameterName%>=" + ids).addParameter(params));
			});
		};
		
		action.sentmessage = function(params) {
			if(!params){
				action.__checkall(function(ids) {
					var da = $Actions["userSentMessageWindow"];
					da.selector = action.selector;
					da(("<%=idParameterName%>=" + ids).addParameter(params));
				});
			} else {
				$Actions["userSentMessageWindow"](params);
			}
		};
	};
</script>