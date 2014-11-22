<%@page import="net.simpleframework.util.StringUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%
	final String id = StringUtils.text(request.getParameter("id"), "myAll");
%>
	
<jsp:include page="/simpleos/template/template.jsp" flush="true">
	<jsp:param value="/app/docu/mydocu_c.jsp" name="center" /><jsp:param
		value="false" name="showOd" />
</jsp:include>
<script type="text/javascript">
function refreshDocu(tid) {
		$$('#documentNav .nav_arrow').each(function(c) {
			c.removeClassName('nav_arrow');
			c.removeClassName('a2');
		});
		if ($(tid))
			$(tid).addClassName('nav_arrow');
			$(tid).addClassName('a2');
	}
$ready(function() {
	refreshDocu('<%=id%>');
	if ('myUpload' == '<%=id%>') {
		$Actions['myUploadAct']();
	} else {
		$Actions['myDocuListTableAct']('docu_type=myAll');
	}
});
</script>