<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.applets.openid.OpenIDUtils"%><%@page
	import="net.prj.core.impl.frame.ITemplateBean"%><%@page
	import="net.prj.manager.template.PrjTemplateUtils"%><%@page
	import="java.util.List"%><%@page import="java.util.ArrayList"%><%@page
	import="java.util.Collections"%><%@page
	import="net.simpleframework.web.page.PageRequestResponse"%><%@page
	import="net.prj.manager.template.PrjTemplateBean"%>
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
		List<String> list = new ArrayList<String>(PrjTemplateUtils.templateMap.keySet());
		Collections.sort(list);
		PrjTemplateBean template = PrjTemplateUtils.getTemplateBean();
		for (String temp : list) {
			ITemplateBean templateBean = PrjTemplateUtils.templateMap.get(temp);
			if (templateBean.isDesign()) {
				continue;
			}
			String src = request.getContextPath() + "/frame/template/" + templateBean.getName() + "/" + templateBean.getName() + ".png";
			boolean same = template.templateId.equals(templateBean.getName());
	%>
	<div class="template_item <%=same ? "template_item_select" : ""%>"
		align="center">
		<img src="<%=src%>">
		<div class="template_item_desc"><%=templateBean.getTitle()%>
			<%-- <input
				type="checkbox" id="fixedHeader_<%=templateBean.getName()%>"
				<%=same&&"true".equals(template.attrMap.get("fixedHeader")) ? "checked=\"checked\"" : ""%>>
			<label for="fixedHeader_<%=templateBean.getName()%>">
				#(Desktop.Template.0)
			</label>
			<input type="checkbox" id="fixedFooter_<%=templateBean.getName()%>"
				<%=same&&"true".equals(template.attrMap.get("fixedFooter")) ? "checked=\"checked\"" : ""%>>
			<label for="fixedFooter_<%=templateBean.getName()%>">
				#(Desktop.Template.1)
			</label> --%>
			<input type="checkbox" id="fullScreen_<%=templateBean.getName()%>"
				<%=same&&"true".equals(template.attrMap.get("fullScreen")) ? "checked=\"checked\"" : ""%>>
			<label for="fullScreen_<%=templateBean.getName()%>">
				#(Desktop.Template.2)
			</label>
			
			<input type="button" value="#(Select)"
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
	/* params += '&fixedHeader=' + $('fixedHeader_' + t).checked;
	params += '&fixedFooter=' + $('fixedFooter_' + t).checked; */
    params += '&fullScreen=' + $('fullScreen_' + t).checked;
	$IT.A('saveTemplate', params);
}
</script>