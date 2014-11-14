<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="net.simpleframework.web.page.component.ui.dictionary.DictionaryUtils"%>
<%@ page
	import="net.simpleframework.web.page.component.ComponentParameter"%>
<%@ page import="net.simpleframework.util.ConvertUtils"%>
<%
	final ComponentParameter nComponentParameter = DictionaryUtils
			.getComponentParameter(request, response);
	final String beanId = nComponentParameter.componentBean.hashId();
%>
<div class="okcancel clear_float">
	<div style="float: right">
		<span style="margin-right: 4px;"><%=DictionaryUtils.getActions(nComponentParameter)%></span>
		<%
			if (DictionaryUtils.isMultiple(nComponentParameter)) {
		%>
		<input type="button" class="button2" value="#(Button.Ok)"
			onclick="selected<%=beanId%>();" />
		<%
			}
		%>
		<input type="button" value="#(Button.Cancel)"
			onclick="$Actions['<%=nComponentParameter.getBeanProperty("name")%>'].close();" />
	</div>
	<%
		if (ConvertUtils.toBoolean(
				nComponentParameter.getBeanProperty("showHelpTooltip"),
				true)) {
	%><div style="float: left">
		<img id="help<%=beanId%>"
			src="<%=nComponentParameter.componentBean
						.getPageResourceProvider().getCssResourceHomePath(
								nComponentParameter)%>/images/help.png" />
	</div>
	<%
		}
	%>
</div>
