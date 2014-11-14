<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.util.StringUtils"%>
<%@page
	import="net.simpleframework.web.page.component.ui.pager.PagerUtils"%>
<%@page import="java.util.List"%>
<%@page import="net.itsite.document.docu.DocuRemark"%>
<%@page import="net.simpleframework.web.page.PageRequestResponse"%><%@page
	import="net.simpleframework.content.component.remark.RemarkDocuUtil"%>

<%
	final PageRequestResponse requestResponse = new PageRequestResponse(request, response);
	final List<?> data = PagerUtils.getPagerList(request);
	if (data == null && data.size() == 0) {
		return;
	}
%>
<div class="list">
	<div class="title">
		评论 (共
		<%=data.size()%>
		条评论)
	</div>
	<%
		out.write(RemarkDocuUtil.itemsHtml(requestResponse, data));
	%>
</div>
<script type="text/javascript">
function __remark_window(o, params) {
	$Actions["ajaxRemarkEditPage"].selector = "";
	$Actions["remarkEditWindow"](params);
}
</script>