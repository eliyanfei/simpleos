<%@page import="net.simpleframework.util.StringUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@page
	import="net.prj.manager.template.PrjTemplateUtils"%>
<%
	final String templateUrl = PrjTemplateUtils.getTemplateUrl();
%>
<%
	final String id = StringUtils.text(request.getParameter("id"), "myAll");
%>
<jsp:include page="<%=templateUrl %>" flush="true">
	<jsp:param value="/app/docu/my/mydocu_c.jsp" name="center" /><jsp:param
		value="false" name="showOd" />
</jsp:include>
<script type="text/javascript">
$ready(function() {
	refreshDocu('<%=id%>');
	if ('myUpload' == '<%=id%>') {
		$Actions['myUploadAct']();
	} else {
		$Actions['myDocuTableAct']('docu_type=myAll');
	}
});
</script>
