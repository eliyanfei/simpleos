<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="net.simpleframework.content.component.remark.RemarkUtils"%>
<%@ page
	import="net.simpleframework.content.component.remark.IRemarkHandle"%>
<%@ page import="net.simpleframework.util.StringUtils"%>
<%@ page
	import="net.simpleframework.web.page.component.ComponentParameter"%>
<%
	final ComponentParameter nComponentParameter = RemarkUtils
			.getComponentParameter(request, response);
	final IRemarkHandle remarkHandle = (IRemarkHandle) nComponentParameter
			.getComponentHandle();
	long count = remarkHandle.getCount(nComponentParameter);
	if (count > 0) {
%>
<div class="list">
	<div class="title"><%=StringUtils.replace(
						(String) nComponentParameter.getBeanProperty("title"),
						"${count}", String.valueOf(count))%></div>
	<%
		}
		out.write(RemarkUtils.itemsHtml(nComponentParameter));
	%>
</div>
<script type="text/javascript">
	function __remark_window(o, params) {
		$Actions["ajaxRemarkEditPage"].selector = 
				"<%=nComponentParameter.getBeanProperty("selector")%>";
		$Actions["remarkEditWindow"](params);
	}
</script>