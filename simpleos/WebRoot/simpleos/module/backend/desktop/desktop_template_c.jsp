<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.applets.openid.OpenIDUtils"%><%@page
	import="net.simpleos.template.ITemplateBean"%><%@page
	import="net.simpleos.backend.template.TemplateUtils"%><%@page
	import="java.util.List"%><%@page import="java.util.ArrayList"%><%@page
	import="java.util.Collections"%><%@page
	import="net.simpleframework.web.page.PageRequestResponse"%><%@page
	import="net.simpleos.backend.template.TemplateBean"%>
<%
	PageRequestResponse requestResponse = new PageRequestResponse(request, response);
%>
<style>
.template_item_desc {
	padding-bottom: 6px;
}

.template_item {
	float: left;
	background-color: #fafafa;
	border: 1px solid #ccc;
	padding: 6px;
	margin-right: 28px;
}

.template_item_select {
	background-color: red;
}

.template_item img {
	width: 320px;
	height: 300px;
}
</style>
<div id="template_check">
	<%
		List<String> list = new ArrayList<String>(TemplateUtils.templateMap.keySet());
			Collections.sort(list);
			TemplateBean template = TemplateUtils.getTemplateBean();
			for (String temp : list) {
		ITemplateBean templateBean = TemplateUtils.templateMap.get(temp);
		if (templateBean.isDesign()) {
			continue;
		}
		String src = request.getContextPath() + "/simpleos/template/" + templateBean.getName() + "/" + templateBean.getName() + ".png";
		boolean same = template.templateId.equals(templateBean.getName());
	%>
	<div class="template_item <%=same ? "template_item_select" : ""%>"
		align="center">
		<img src="<%=src%>">
		<div class="template_item_desc"><%=templateBean.getTitle()%>
			<input type="checkbox" id="fullScreen_<%=templateBean.getName()%>"
				<%=(same&&"true".equals(template.attrMap.get("fullScreen")))||templateBean.isFullScreen() ? "checked=\"checked\"" : ""%>>
			<label for="fullScreen_<%=templateBean.getName()%>">
				#(Desktop.Template.2) </label> <input type="button" value="#(Select)"
				<%=same ? "" : "class=\"button2\""%>
				onclick="saveModel('<%=templateBean.getName()%>');">
		</div>
	</div>
	<%
		}
	%>
</div>
<script type="text/javascript">
	function saveModel(t) {
		var params = 'templateId=' + t;
		params += '&fullScreen=' + $('fullScreen_' + t).checked;
		$IT.A('saveTemplate', params);
	}
</script>