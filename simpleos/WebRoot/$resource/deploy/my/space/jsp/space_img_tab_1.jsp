<%@page import="net.simpleframework.util.StringUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.my.space.MySpaceUtils"%>

<div style="padding: 4px 12px;">
	<input type="text" id="space_img_url_add_text" style="width: 280px;" />
	<input type="button" id="space_img_url_add_btn"
		value="#(space_log.editor.7)" style="margin: 4px 0;"
		onclick="$Actions['space_img_url_add']('url=' + $F('space_img_url_add_text'));" />
	<div id="space_img_url_list">
		<%
			for (String url : MySpaceUtils.getSessionUrls(session)) {
		%>
		<div class="space_img_list">
			<table style="width: 100%;">
				<tr>
					<td class="wrap_text"><%=url%></td>
					<td align="right"><span class="delete_image"
						onclick="$Actions['space_img_url_delete']('url=<%=StringUtils.encodeHex(url.getBytes())%>');"></span>
					</td>
				</tr>
			</table>
		</div>
		<%
			}
		%>
	</div>
</div>