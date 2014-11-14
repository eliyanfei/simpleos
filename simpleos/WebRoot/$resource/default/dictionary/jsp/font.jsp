<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="net.simpleframework.web.page.component.ui.dictionary.DictionaryUtils"%>
<%@ page
	import="net.simpleframework.web.page.component.ComponentParameter"%>
<%
	final ComponentParameter nComponentParameter = DictionaryUtils
			.getComponentParameter(request, response);
	final String beanId = nComponentParameter.componentBean.hashId();
%>
<div id="fontEditor<%=beanId%>" style="margin-bottom: 6px;"></div>
<jsp:include page="okcancel_inc.jsp" flush="true"></jsp:include>

<script type="text/javascript">
	$Actions.callSafely('_dict_fontEditor', null, function(action) {
		action.container = $("fontEditor<%=beanId%>");
	});
	<%=DictionaryUtils.fontSelected(nComponentParameter)%>
</script>
