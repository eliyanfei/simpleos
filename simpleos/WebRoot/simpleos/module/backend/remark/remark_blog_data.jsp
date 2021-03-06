<%@page import="net.simpleframework.web.WebUtils"%>
<%@page import="net.simpleos.SimpleosUtil"%>
<%@page import="net.simpleframework.content.EContentStatus"%>
<%@page import="net.simpleframework.util.ConvertUtils"%>
<%@page import="net.simpleos.utils.StringsUtils"%>
<%@page import="net.simpleframework.web.page.PageRequestResponse"%>
<%@page import="net.simpleframework.content.ContentUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="net.simpleframework.web.page.component.ui.pager.PagerUtils"%>
<%@ page import="java.util.List"%><%@page
	import="net.simpleframework.util.HTMLBuilder"%><%@page
	import="net.simpleframework.web.page.component.ComponentParameter"%><%@page
	import="net.simpleframework.organization.OrgUtils"%><%@page
	import="net.simpleframework.util.DateUtils"%><%@page
	import="net.simpleframework.content.blog.BlogRemark"%><%@page
	import="net.simpleframework.content.blog.Blog"%><%@page
	import="net.simpleframework.content.blog.BlogUtils"%><%@page import="net.simpleos.mvc.remark.RemarkUtils"%>

<%
	final ComponentParameter componentParameter = new ComponentParameter(request, response, null);
%>
<style>
.pager .pager_top_block .pager_title {
	margin-left: 3px;
}

.pager .pager_head {
	margin-right: 6px;
}

.listpager {
	
}

.listpager .titem {
	display: inline-block;
	vertical-align: top;
	border: 1px solid #EEE;
	margin-top: -1px;
	width: 99%;
}

.iconpager .titem {
	display: inline-block;
	vertical-align: top;
	border: 1px solid #EEE;
	margin-top: -1px;
	width: 49%;
}

.listpager .titem:HOVER,.iconpager .titem:HOVER {
	background-color: #F8F8F8;
}

.listpager .title,.iconpager .title {
	font-weight: bold;
}
</style>
<div class="listpager" id="_tablepaper">
	<%
		final List<?> data = PagerUtils.getPagerList(request);
		if (data == null && data.size() == 0) {
			return;
		}
		int i = 1;
		for (Object o : data) {
			final BlogRemark remark = (BlogRemark) o;
			final Blog blog = BlogUtils.getTableEntityManager(Blog.class).queryForObjectById(remark.getDocumentId(), Blog.class);
			if (blog == null) {
				continue;
			}
	%>
	<div class="titem <%=i++ % 2 == 0 ? "even" : ""%>"
		rowid="<%=remark.getId()%>">
		<table cellpadding="4" style="width: 100%;">
			<tr>
				<td valign="top" width="100%">
					<table style="width: 100%">
						<tr>
							<td class="title">
								<a
									href="<%=BlogUtils.applicationModule.getBlogViewUrl(componentParameter, blog)%>"
									target="blank"><%=blog.getTopic()%></a>
							</td>
						</tr>
						<tr>
							<td class="wrapWord">
								<%=RemarkUtils.buildRelateRemark(remark, componentParameter)%>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</div>
	<%
		}
	%>
</div>
