<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.content.blog.BlogUtils"%>
<%@ page import="net.simpleframework.ado.db.IQueryEntitySet"%>
<%@ page import="net.simpleframework.content.blog.Blog"%>
<%@ page import="net.simpleframework.content.component.newspager.INewsPagerHandle"%>
<%@ page import="net.simpleframework.web.page.component.ComponentParameter"%>
<%@ page import="net.simpleframework.content.ContentUtils"%>
<%@ page import="net.simpleframework.util.ConvertUtils"%>
<%@ page import="net.simpleframework.my.space.MySpaceUtils"%>
<%@ page import="net.simpleframework.organization.account.IAccount"%>
<%@ page import="net.simpleframework.content.component.newspager.NewsPagerUtils"%>
<%
	final ComponentParameter nComponentParameter = new ComponentParameter(
			request, response, null);
	nComponentParameter.componentBean = BlogUtils.applicationModule
			.getMyBlogPagerBean(nComponentParameter);
	final INewsPagerHandle nHandle = (INewsPagerHandle) nComponentParameter
			.getComponentHandle();
	final IQueryEntitySet<?> qs = BlogUtils.queryBlogs(
			nComponentParameter, null, null, true, 0);
	qs.setCount(10);
%> 
<div class="simple_tabs_content space_tabs_content">
	<%
		Blog blog;
		while ((blog = (Blog) qs.next()) != null) {
	%>
	<div class="space_content_item">
		<div class="f3" style="padding: 8px 0px 4px;"><%=nHandle.wrapOpenLink(nComponentParameter, blog)%></div>
		<table cellpadding="0" cellspacing="0">
			<tr>
				<%
					String img = ContentUtils.getContentImage(nComponentParameter,
								blog);
						if (img != null) {
							out.write("<td width=\"110\" valign=\"top\">");
							out.write(img);
							out.write("</td>");
						}
				%>
				<td><div class="gray-color"><%=ContentUtils.getShortContent(blog, 500, true)%></div>
				</td>
			</tr>
		</table>
		<div style="padding-top: 4px;">
			<%=ConvertUtils.toDateString(blog.getCreateDate())%>
			<%=NewsPagerUtils.getNewsContentUrl(nComponentParameter,
						blog)%>
		</div>
	</div>
	<%
		}
		final IAccount account = MySpaceUtils.getAccountAware().getAccount(
				nComponentParameter);
		if (account != null) {
	%>
	<p style="text-align: right;">
		<a class="f2 a2"
			href="<%=BlogUtils.applicationModule.getBlogUrl(
						nComponentParameter, account.user())%>">#(space_blog.0)</a>
	</p>
	<%
		}
	%>
</div>
