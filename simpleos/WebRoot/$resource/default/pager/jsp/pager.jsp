<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.util.StringUtils"%>
<%@ page
	import="net.simpleframework.web.page.component.ui.pager.EPagerBarLayout"%>
<%@ page
	import="net.simpleframework.web.page.component.ui.pager.PagerUtils"%>
<%@ page
	import="net.simpleframework.web.page.component.ui.pager.IPagerHandle"%>
<%@ page import="net.simpleframework.util.HTTPUtils"%>
<%@ page import="net.simpleframework.util.JavascriptUtils"%>
<%@ page
	import="net.simpleframework.web.page.component.ComponentRenderUtils"%>
<%@ page
	import="net.simpleframework.web.page.component.AbstractComponentBean"%>
<%@ page
	import="net.simpleframework.web.page.component.ComponentParameter"%>
<%@ page import="net.simpleframework.web.page.PageUtils"%>
<%
	final ComponentParameter nComponentParameter = PagerUtils
			.getComponentParameter(request, response);
	final IPagerHandle pagerHandle = (IPagerHandle) nComponentParameter
			.getComponentHandle();
	if (pagerHandle == null) {
		return;
	}
	final int count = pagerHandle.getCount(nComponentParameter);
	final int pageItems = PagerUtils.getPageItems(nComponentParameter);
	final int pageNumber = PagerUtils
			.getPageNumber(nComponentParameter);
	int start;
	if (pageNumber > 1) {
		start = (pageNumber - 1) * pageItems;
		if (start >= count) {
			start = 0;
			PagerUtils.resetPageNumber(nComponentParameter);
		}
	} else {
		start = 0;
	}
	HTTPUtils.putParameter(request, "pager.offset", start);
	pagerHandle.process(nComponentParameter, start);
%>
<div class="pager">
	<%
		out.write(ComponentRenderUtils.genParameters(nComponentParameter));
		final String title = (String) nComponentParameter
				.getBeanProperty("title");
		final EPagerBarLayout layout = (EPagerBarLayout) nComponentParameter
				.getBeanProperty("pagerBarLayout");
		boolean showHeader = count > 0
				&& (layout == EPagerBarLayout.top || layout == EPagerBarLayout.both);
	%>
	<table style="width: 100%" cellpadding="0" cellspacing="0">
		<%
			if (StringUtils.hasText(title) || showHeader) {
		%>
		<tr>
			<td><div class="pager_top_block clear_float">
					<%
						if (showHeader) {
					%><div style="float: right;">
						<jsp:include page="pager_head.jsp" flush="true">
							<jsp:param value="<%=start%>" name="pager.offset" />
							<jsp:param value="<%=count%>" name="_count" />
							<jsp:param value="<%=pageItems%>" name="_pageItems" />
						</jsp:include></div>
					<%
						}
							if (StringUtils.hasText(title)) {
					%>
					<div class="pager_title"><%=title%></div>
					<%
						}
					%>
				</div></td>
		</tr>
		<%
			}
			String dataPath = (String) nComponentParameter
					.getBeanProperty("dataPath");
			if (StringUtils.hasText(dataPath)) {
				dataPath = PageUtils.doPageUrl(nComponentParameter, dataPath);
		%>
		<tr>
			<td><jsp:include page="<%=dataPath%>" flush="true"></jsp:include></td>
		</tr>
		<%
			}
			if (count > 0
					&& (layout == EPagerBarLayout.bottom || layout == EPagerBarLayout.both)) {
		%>
		<tr>
			<td><div class="pager_bottom_block clear_float"><jsp:include
						page="pager_head.jsp" flush="true">
						<jsp:param value="<%=start%>" name="pager.offset" />
						<jsp:param value="<%=count%>" name="_count" />
						<jsp:param value="<%=pageItems%>" name="_pageItems" />
					</jsp:include></div></td>
		</tr>
		<%
			}
		%>
	</table>
</div>
<%
	if (count == 0) {
%>
<div class="pager_no_result"><%=StringUtils.blank(nComponentParameter
						.getBeanProperty("noResultDesc"))%>
</div>
<%
	}
	String beanId = nComponentParameter.componentBean.hashId();
%>
<script type="text/javascript">
	var __pager_action = function(o) {
		o = $target(o);	
		return (o.hasClassName("pager") ? o : o.up(".pager")).action;
	};
	
	$ready(function() {
		var action = $Actions["<%=nComponentParameter.getBeanProperty("name")%>"];
		action.selector = 
			"<%=nComponentParameter.getBeanProperty("selector")%>";
		var ele = $("<%=AbstractComponentBean.FORM_PREFIX + beanId%>").up();
		action.pager = ele;

		ele.action = action;
		var initFunc = window.pager_init_<%=beanId%>;
		if (Object.isFunction(initFunc)) {
			initFunc(action);
		}
		addMethods(action);

		function addMethods(pa) {
			pa.refresh = function(params) {
				action(params);		
			};
		}
		
		<%final String callback = (String) nComponentParameter.getBeanProperty("jsLoadedCallback");
			if (StringUtils.hasText(callback)) {
				out.write(JavascriptUtils.wrapWhenReady(callback));
			}%>
	});
</script>