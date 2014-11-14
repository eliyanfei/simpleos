<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.organization.OrgUtils"%>
<%@ page import="net.simpleframework.web.page.PageRequestResponse"%>
<%@ page import="net.simpleframework.content.blog.BlogUtils"%>
<%@ page import="net.simpleframework.organization.IUser"%>
<%@ page import="net.simpleframework.my.space.MySpaceUtils"%>
<%
	final IUser user = (IUser) request.getAttribute("__user");
	if (user == null) {
		return;
	}
	final PageRequestResponse requestResponse = new PageRequestResponse(
			request, response);
%>
<div style="float: left; width: 90px; margin: 4px;">
	<a
		href="<%=MySpaceUtils.applicationModule.getSpaceUrl(
					requestResponse, user)%>"><img
		class="photo_icon" style="width: 64px; height: 64px;"
		src="<%=OrgUtils.getPhotoSRC(request, user, 64, 64)%>" /> </a>
</div>
<div style="float: left">
	<a
		href="<%=BlogUtils.applicationModule.getBlogUrl(requestResponse,
					user)%>"
		class="f2"><%=user.getText()%>#(my_blog_bar.0)</a>
	<div style="margin-top: 8px;">
		<%=BlogUtils.applicationModule.tabs(requestResponse)%></div>
</div>