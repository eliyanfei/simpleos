<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.util.StringUtils"%>
<%@ page import="net.simpleframework.organization.OrgUtils"%>
<%@ page
	import="net.simpleframework.organization.component.userpager.UserPagerUtils"%>
<%@ page
	import="net.simpleframework.web.page.component.ComponentParameter"%>
<form id="__user_edit_form">
	<input type="hidden" id="<%=OrgUtils.um().getUserIdParameterName()%>"
		name="<%=OrgUtils.um().getUserIdParameterName()%>"
		value="<%=StringUtils.blank(request.getParameter(OrgUtils.um().getUserIdParameterName()))%>" />
</form>
<table width="100%"
	style="border: 1px solid #aaa; background-color: white; min-height: 400px;"
	cellpadding="0" cellspacing="0">
	<tr>
		<td class="category" id="userInfoListId" valign="top" nowrap="nowrap"
			style="border-right: 1px solid #aaa; background: #eef; width: 200px;"></td>
		<td id="__user_edit_data_id" style="width: 100%;" valign="top"></td>
	</tr>
</table>

<script type="text/javascript">
function __user_pager_refresh() {
<%final ComponentParameter nComponentParameter = UserPagerUtils.getComponentParameter(request, response);
			if (nComponentParameter.componentBean != null) {%>
	$Actions["<%=nComponentParameter.getBeanProperty("name")%>"].refresh();
	<%}%>
	}
</script>