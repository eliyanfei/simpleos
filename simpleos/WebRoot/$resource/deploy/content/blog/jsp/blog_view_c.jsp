<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<%@ page
  import="net.simpleframework.web.page.component.ComponentParameter"%>
<%@ page import="net.simpleframework.content.blog.BlogUtils"%>
<%@ page
  import="net.simpleframework.content.component.newspager.INewsPagerHandle"%>
<%@ page import="net.simpleframework.content.blog.Blog"%>
<%@ page import="net.simpleframework.organization.OrgUtils"%>
<%
	final ComponentParameter nComponentParameter = new ComponentParameter(
			request, response, null);
	final String templatePage = BlogUtils
			.getTemplatePage(nComponentParameter);
	final INewsPagerHandle nHandle = (INewsPagerHandle) nComponentParameter
			.getComponentHandle();
	final Blog blog = nHandle
			.getEntityBeanByRequest(nComponentParameter);
	if (blog == null) {
		return;
	}
	request.setAttribute("__user",
			OrgUtils.um().queryForObjectById(blog.getUserId()));
%>
<div class="simple_toolbar2 clear_float">
  <div style="float: left;">
    <jsp:include page="my_blog_bar.jsp"></jsp:include>
  </div>
</div>
<jsp:include page="<%=templatePage%>" flush="true"></jsp:include>
