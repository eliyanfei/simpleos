<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="net.simpleframework.web.page.component.ui.pager.PagerUtils"%>
<%@ page
	import="net.simpleframework.web.page.component.ui.pager.EPagerPosition"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="net.simpleframework.util.ConvertUtils"%>
<%@ page
	import="net.simpleframework.web.page.component.ui.pager.PagerBean"%>
<%@ page
	import="net.simpleframework.web.page.component.ComponentParameter"%>
<%
	final ComponentParameter nComponentParameter = PagerUtils
			.getComponentParameter(request, response);
	final int count = ConvertUtils.toInt(
			request.getParameter("_count"), 0);
	int pageItems = ConvertUtils.toInt(
			request.getParameter("_pageItems"), 15);
	if (pageItems <= 0) {
		pageItems = 1;  
	}
	final int indexPages = (Integer) nComponentParameter.getBeanProperty("indexPages");
	final HashMap<String, Integer> pageVar = new HashMap<String, Integer>();
%>
<div class="pager_head">
	<pg:pager items="<%=count%>" export="currentPageNumber=pageNumber" 
		maxPageItems="<%=pageItems%>" maxIndexPages="<%=indexPages%>">
		<pg:index export="itemCount,pageCount">
			<pg:page export="firstItem,lastItem"><%
				pageVar.put("currentPageNumber", currentPageNumber);
							pageVar.put("pageCount", pageCount);
							pageVar.put("itemCount", itemCount);
							pageVar.put("firstItem", firstItem);
							pageVar.put("lastItem", lastItem);
			%><%=PagerUtils.nbar(nComponentParameter,
								EPagerPosition.left2, pageItems, pageVar)%><%=PagerUtils.nbar(nComponentParameter,
								EPagerPosition.left, pageItems, pageVar)%><pg:pages><%
				pageVar.put("pageNumber", pageNumber);
			%><%=PagerUtils.nbar(nComponentParameter,
									EPagerPosition.number, pageItems, pageVar)%></pg:pages><%=PagerUtils.nbar(nComponentParameter,
								EPagerPosition.right, pageItems, pageVar)%><%=PagerUtils.nbar(nComponentParameter,
								EPagerPosition.right2, pageItems, pageVar)%></pg:page>
		</pg:index>
	</pg:pager><%=PagerUtils.getPagerActions(nComponentParameter, count,
					pageItems, pageVar)%>
</div>

