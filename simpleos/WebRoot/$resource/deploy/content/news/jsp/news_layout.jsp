<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.core.ado.IDataObjectQuery"%>
<%@ page import="net.simpleframework.content.news.NewsUtils"%>
<%@ page
	import="net.simpleframework.web.page.component.ComponentParameter"%>
<%@ page import="net.simpleframework.content.news.News"%>
<%@ page import="net.simpleframework.util.ConvertUtils"%>
<%@ page
	import="net.simpleframework.content.component.newspager.INewsPagerHandle"%>
<%@ page
	import="net.simpleframework.content.news.INewsApplicationModule"%>
<%@ page import="net.simpleframework.content.ContentLayoutUtils"%>
<%
	final ComponentParameter nComponentParameter = new ComponentParameter(request, response, null);
	final IDataObjectQuery<?> qs = ContentLayoutUtils.getQueryByRequest(nComponentParameter);
	if (qs == null) {
		return;
	}
	nComponentParameter.componentBean = NewsUtils.applicationModule.getComponentBean(nComponentParameter);
	if (nComponentParameter.componentBean == null) {
%>
<div style="text-align: center; padding: 8px;">
	#(news_layout.0)
</div>
<%
	return;
	}
	final INewsPagerHandle nHandle = (INewsPagerHandle) nComponentParameter.getComponentHandle();
	final String[] tabs = { "最新", "推荐", "热点", "活跃" };
	final boolean _show_tabs = ConvertUtils.toBoolean(request.getParameter("_show_tabs"), true);
	if (_show_tabs) {
%>
<div class="tabs" style="position: absolute; right: 0px;">
	<%
		int i = 0;
			int ta = ConvertUtils.toInt(request.getParameter("_tab_param"), 0);
			for (String tab : tabs) {
	%>
	<span class="tab <%=i == ta ? "active" : ""%>"
		onclick="$IT.togglePagelet(this,'_tab_param=<%=i++%>');"><%=tab%></span>
	<%
		}
	%>
</div>
<%
	}
%>
<div class="list_layout">
	<%
		News news;
		int i = 1;
		while ((news = (News) qs.next()) != null) {
			if (ContentLayoutUtils.isRequestNews(nComponentParameter, qs, news)) {
				continue;
			}
			final boolean isNews = nHandle.isRemarkNew(nComponentParameter, news);
	%>
	<div class="rrow">
		<div class="ti numDot">
			<span class="sdesc"
				title="<%=news.getRemarks()%>#(bbs_layout.2), <%=news.getViews()%>#(bbs_layout.3)"><%=isNews ? "<span class=\"nnum\" style=\"color: red;\">" + news.getRemarks() + "</span>" : news.getRemarks()%>/<%=news.getViews()%></span>
			<span class="n <%=(i > 3 ? "n2" : "")%>"><%=i++%></span>
			<%=nHandle.wrapOpenLink(nComponentParameter, news)%>
		</div>
	</div>
	<%
		}
	%>
</div>