<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="net.simpleframework.organization.component.jobselect.JobSelectUtils"%>
<%@ page
	import="net.simpleframework.web.page.component.ui.dictionary.DictionaryUtils"%>
<%@ page
	import="net.simpleframework.organization.component.jobselect.IJobSelectHandle"%>
<%@ page
	import="net.simpleframework.web.page.component.ComponentParameter"%>
<%
	ComponentParameter nComponentParameter = JobSelectUtils
			.getComponentParameter(request, response);
	final String beanId = nComponentParameter.componentBean.hashId();
	final IJobSelectHandle jsHandle = (IJobSelectHandle) nComponentParameter
			.getComponentHandle();
%>
<div class="job_select">
	<div class="js1">
		<table style="width: 100%;" cellpadding="0" cellspacing="0">
			<tr>
				<td class="icon">
					<div></div></td>
				<td><a class="js4"
					onclick="var act=$Actions['__jobChartSelectDict']; act.a = this; act();"><%=jsHandle.getDefaultJobChart(nComponentParameter)%></a>
				</td>
			</tr>
		</table>
	</div>
	<div class="js2">
		<div id="container_<%=beanId%>"></div>
	</div>
	<div class="js3">
		<table style="width: 100%;" cellpadding="0" cellspacing="0">
			<tr>
				<td><%=DictionaryUtils.getActions(nComponentParameter)%></td>
				<td align="right"><input type="button" value="#(Button.Cancel)"
					onclick="$Actions['<%=nComponentParameter.getBeanProperty("name")%>'].close();" />
				</td>
			</tr>
		</table>
	</div>
</div>
<script type="text/javascript">
	var selected<%=beanId%> = function(branch, ev) {
		var selects = __tree_getSelects($Actions['__jobSelectTree'].tree, branch, ev);
		if (selects && selects.length > 0) {
			<%=DictionaryUtils.genSelectCallback(nComponentParameter,
					"selects")%>
		}
	};
	
	$ready(function() {
		var tp = $("container_<%=beanId%>").up();
		var w = $Actions['<%=nComponentParameter.getBeanProperty("name")%>'].window;
		w.content.setStyle("overflow:hidden;");
		var s = function() {
			var h = w.getSize(true).height - tp.previous().getHeight() - 
					tp.next().getHeight() - 27;
			tp.setStyle('height: ' + h + 'px;');
 		};
 		s();
    w.observe("resize:ended", s);
	});
</script>

