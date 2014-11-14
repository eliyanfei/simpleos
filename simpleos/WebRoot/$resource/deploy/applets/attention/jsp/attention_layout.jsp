<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.ado.db.IQueryEntitySet"%>
<%@ page import="net.simpleframework.util.ConvertUtils"%>
<%@ page import="java.util.Map"%>
<%@ page import="net.simpleframework.web.EFunctionModule"%>
<%
	final IQueryEntitySet<?> qs = (IQueryEntitySet<?>) request
			.getAttribute("__qs");
	if (qs == null) {
		return;
	}
	qs.setCount(ConvertUtils.toInt(request.getParameter("rows"), 6));
%>
<div class="list_layout">
	<%
		Map<?, ?> row;
		while ((row = (Map<?, ?>) qs.next()) != null) {
			request.setAttribute("attention_row", row);
			EFunctionModule vtype = ConvertUtils.toEnum(
					EFunctionModule.class, row.get("VTYPE"));
			if (vtype == EFunctionModule.bbs) {
	%>
	<jsp:include page="attention_layout_bbs.jsp"></jsp:include>
	<%
		} else if (vtype == EFunctionModule.blog
					|| vtype == EFunctionModule.news) {
	%>
	<jsp:include page="attention_layout_news.jsp"></jsp:include>
	<%
		}
		}
	%>
</div>