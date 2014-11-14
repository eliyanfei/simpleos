<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="net.simpleframework.web.page.component.ui.swfupload.SwfUploadUtils"%>
<%@ page
	import="net.simpleframework.web.page.component.ComponentRenderUtils"%>
<%@ page
	import="net.simpleframework.web.page.component.ComponentParameter"%>
<%
	final ComponentParameter nComponentParameter = SwfUploadUtils
			.getComponentParameter(request, response);
	final String beanId = nComponentParameter.componentBean.hashId();
%>
<div class="swfupload">
	<%=ComponentRenderUtils.genParameters(nComponentParameter)%>
	<div class="simple_toolbar3">
		<div>
			<span id="placeholder_<%=beanId%>"></span> <input type="button"
				value="#(swfupload.0)" id="upload_<%=beanId%>"
				onclick="__job_upload_action();" />
		</div>
		<div id="message_<%=beanId%>" class="message" style="margin: 4px;"></div>
	</div>
	<div id="fileQueue_<%=beanId%>" class="queue"></div>
</div>
<script type="text/javascript">
<%=SwfUploadUtils.genJavascript(nComponentParameter)%>
	function __job_upload_action() {
		var paramsObject = $Actions["<%=nComponentParameter.getBeanProperty("name")%>"].paramsObject;
		if (!paramsObject) {
			paramsObject = new Object(); 
		}
		paramsObject["<%=SwfUploadUtils.BEAN_ID%>"] = "<%=beanId%>";
		$Actions["jobUploadAction"](Object.toQueryString(paramsObject));
	}
</script>