<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.my.file.MyFileUtils"%>
<%@ page import="net.simpleframework.web.page.PageRequestResponse"%><%@page
	import="net.itsite.ItSiteUtil"%>

<%
	final PageRequestResponse requestResponse = new PageRequestResponse(request, response);
	ItSiteUtil.addMenuNav(request.getSession(), null, "文档管理", false);
%>
<div class="myfolder">
	<div id="__my_folder"></div>
</div>
<script type="text/javascript">
function __my_folder_refresh() {
	$Actions["__my_folder"].getTree().refresh();
	$Actions["__my_folder_refresh"]();
}
</script>