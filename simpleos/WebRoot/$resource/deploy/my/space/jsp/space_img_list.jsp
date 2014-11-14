<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.my.space.MySpaceUtils"%>
<%@ page import="java.io.File"%>
<%
	final File[] files = MySpaceUtils.getUploadDir(session).listFiles();
	if (files == null) {
		return;
	}
	for (File file : files) {
%>
<div class="space_img_list">
	<table cellpadding="0" cellspacing="0" style="width: 100%;">
		<tr>
			<td class="wrap_text"><%=file.getName()%></td>
			<td align="right"><span class="delete_image"
				onclick="$Actions['ajaxSpaceImgDelete']('code=<%=file.hashCode()%>');"></span></td>
		</tr>
	</table>
</div>
<%
	}
%>
