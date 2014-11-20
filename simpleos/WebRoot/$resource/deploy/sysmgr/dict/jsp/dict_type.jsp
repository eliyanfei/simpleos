<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@page import="net.itsite.ItSiteUtil"%>

<%
	ItSiteUtil.addMenuNav(request.getSession(), null, "字典管理", false);
%>
<div class="sys_dict_type">
	<div id="__sys_dict_type"></div>
</div>
<script type="text/javascript">
function __sys_dict_items(params) {
	var act = $Actions['__sys_dict_item'];
	var treeAct = act.getTree();
	if (treeAct) {
		$Actions.addHidden($("__sys_dict_item").down("form"), params);
		treeAct.refresh(params);
	} else {
		act(params);
	}
	$("__sys_dict_item_c").update("");
}
</script>
