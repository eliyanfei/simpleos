<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="net.simpleframework.web.page.component.ui.htmleditor.HtmlEditorUtils"%>
<%@ page
	import="net.simpleframework.web.page.component.ui.dictionary.DictionaryUtils"%>
<%@ page
	import="net.simpleframework.web.page.component.ComponentParameter"%>
<%@ page import="net.simpleframework.util.StringUtils"%>
<%
	final ComponentParameter nComponentParameter = DictionaryUtils
			.getComponentParameter(request, response);
	final String beanId = nComponentParameter.componentBean.hashId();
%>
<div class="dictionary_smiley">
	<%
		for (int i = 0; i < 90; i++) {
	%><img onclick="smiley_<%=beanId%>(this);"
		src="<%=HtmlEditorUtils.getHomePath()%>/smiley/<%=i%>.gif" /><%
		}
	%>
</div>

<script type="text/javascript">
	smiley_<%=beanId%> = function(img) {
		<%StringBuilder sb = new StringBuilder();
			final String callback = (String) nComponentParameter.getBeanProperty("jsSelectCallback");
			if (StringUtils.hasText(callback)) {
				sb.append(callback);
			} else {
				final String binding = StringUtils.text(
						(String) nComponentParameter.getBeanProperty("bindingId"),
						(String) nComponentParameter.getBeanProperty("bindingText"));
				if (StringUtils.hasText(binding)) {
					sb.append("var b = $('").append(binding).append("');");
					sb.append("if (b) { var s = img.src; ");
					sb.append("var j = s.substring(s.lastIndexOf('/') + 1, s.lastIndexOf('.'));");
					sb.append("$Actions.setValue(b, '[:em' + j + ']', true); $Actions['");
					sb.append(nComponentParameter.getBeanProperty("name"));
					sb.append("'].close(); b.focus(); }");
				}
			}
			out.write(sb.toString());%>
	};
</script>

