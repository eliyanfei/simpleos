<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.organization.component.userpager.UserPagerUtils"%>
<%@ page import="net.simpleframework.util.StringUtils"%>

<div style="float: left"><input type="button" value="#(Button.Save2)" id="userSaveAndNew"
	onclick="$Actions['ajaxUserSave']('next=true');" /></div>
<div style="float: right"><input type="button" class="button2" value="#(Button.Save)" id="userSave"
	onclick="$Actions['ajaxUserSave']();" /> <input type="button" value="#(Button.Cancel)"
	onclick="$Actions['addUserWindow'].close();" /></div>
<form id="userFormEditor" style="clear: both; padding-top: 6px;"></form>
<script type="text/javascript">
	function __deptselct_dict() {
		$Actions['arDeptDict']('<%=UserPagerUtils.BEAN_ID%>=<%=StringUtils.blank(
				request.getParameter(UserPagerUtils.BEAN_ID))%>');
	}
</script>
