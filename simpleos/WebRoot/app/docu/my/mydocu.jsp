<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@page
	import="net.prj.manager.template.PrjTemplateBean"%><%@page
	import="net.prj.manager.template.PrjTemplateUtils"%><%@page
	import="net.itniwo.commons.StringsUtils"%><%@page
	import="net.simpleframework.util.ConvertUtils"%><%@page
	import="net.simpleframework.content.EContentStatus"%><%@page
	import="net.simpleframework.util.StringUtils"%>

<%
	final String id = StringUtils.text(request.getParameter("id"), "myAll");
%>

<%
	final PrjTemplateBean templateBean = PrjTemplateUtils.getTemplateBean();
	final boolean win = ConvertUtils.toBoolean(request.getParameter("win"), false);
	final String templateUrl = "/frame/template/" + (win ? "/t3/c" : StringsUtils.u(templateBean.templateId, "/", templateBean.templateId))
			+ ".jsp";
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
