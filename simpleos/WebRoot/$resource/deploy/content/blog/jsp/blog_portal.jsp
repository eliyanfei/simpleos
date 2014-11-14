<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.core.ado.IDataObjectQuery"%>
<%@ page import="net.simpleframework.util.ConvertUtils"%>
<%@ page import="net.simpleframework.content.blog.BlogUtils"%>
<%@ page
	import="net.simpleframework.web.page.component.ComponentParameter"%>
<%@ page
	import="net.simpleframework.content.component.newspager.INewsPagerHandle"%>
<%@ page import="net.simpleframework.content.blog.Blog"%>
<%@ page import="net.simpleframework.organization.account.IAccount"%>
<%@ page import="net.simpleframework.content.ContentLayoutUtils"%>
<%@ page import="net.simpleframework.content.ContentUtils"%>
<%
	final ComponentParameter nComponentParameter = new ComponentParameter(request, response, null);
	final IDataObjectQuery<?> qs = ContentLayoutUtils.getQueryByRequest(nComponentParameter);
	if (qs == null) {
		return;
	}
	nComponentParameter.componentBean = BlogUtils.applicationModule.getComponentBean(nComponentParameter);
	if (nComponentParameter.componentBean == null) {
%>
<div style="text-align: center; padding: 8px;">
	#(blog_layout.0)
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
		final boolean myblog = ConvertUtils.toInt(request.getParameter("btype"), 0) == 2;
		Blog blog;
		int i = 1;
		while ((blog = (Blog) qs.next()) != null) {
			if (ContentLayoutUtils.isRequestNews(nComponentParameter, qs, blog)) {
				continue;
			}
			final boolean isNews = nHandle.isRemarkNew(nComponentParameter, blog);
	%>
	<div class="rrow">
		<div class="ti numDot">
			<span class="sdesc"
				title="<%=blog.getRemarks()%>#(bbs_layout.2), <%=blog.getViews()%>#(bbs_layout.3)"><%=isNews ? "<span class=\"nnum\" style=\"color: red;\">" + blog.getRemarks() + "</span>" : blog.getRemarks()%>/<%=blog.getViews()%></span>
			<span class="n <%=(i > 3 ? "n2" : "")%>"><%=i++%></span>
			<%=nHandle.wrapOpenLink(nComponentParameter, blog)%>
		</div>
	</div>
	<%
		}
	%>
</div>