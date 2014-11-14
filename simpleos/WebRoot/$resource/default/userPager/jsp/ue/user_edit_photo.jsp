<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.organization.component.userpager.UserPagerUtils"%>
<%@ page import="net.simpleframework.util.StringUtils"%>
<%@ page import="net.simpleframework.organization.OrgUtils"%>
<%
	final String idParameterName = OrgUtils.um()
			.getUserIdParameterName();
	final String userId = StringUtils.blank(request
			.getParameter(idParameterName));
%>
<table style="width: 100%;" cellpadding="2" cellspacing="0">
	<tr>
		<td colspan="2">#(user_edit_photo.0)</td>
	</tr>
	<tr>
		<td valign="top"><img id="user_edit_photo_image" class="photo_icon"
			style="width: 168px; height: 168px;" src="<%=OrgUtils.getPhotoSRC(request, userId, 164, 164)%>">
		</td>
		<td valign="top"><iframe width="100%" frameborder="0" marginheight="0" marginwidth="0"
				scrolling="no"
				src="<%=request.getContextPath() + UserPagerUtils.getHomePath()
					+ "/jsp/ue/user_edit_photo_upload.jsp?" + idParameterName
					+ "=" + userId%>"></iframe>
		</td>
	</tr>
</table>


