<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.content.EContentType"%>
<%@ page import="net.simpleframework.applets.tag.ITagApplicationModule"%>
<%@ page import="net.simpleframework.content.IContentPagerHandle"%>
<%
	final StringBuilder params = new StringBuilder();
	params.append(ITagApplicationModule._TAG_ID)
			.append("=")
			.append(request.getParameter(ITagApplicationModule._TAG_ID))
			.append("&").append(IContentPagerHandle._VTYPE)
			.append("=")
			.append(request.getParameter(IContentPagerHandle._VTYPE));
%>
<div class="tags_option">
	<table style="width: 100%;" cellpadding="2" cellspacing="0">
		<tr>
			<td class="l">#(tags_option.0)</td>
			<td><select name="to_type" id="to_type">
					<option value="<%=EContentType.normal.name()%>"><%=EContentType.normal%></option>
					<option value="<%=EContentType.recommended.name()%>"><%=EContentType.recommended%></option>
			</select></td>
		</tr>
	</table>
</div>
<div style="text-align: right; margin-top: 6px;">
	<input type="button" value="#(Button.Ok)" class="button2" onclick="$Actions['ajaxTagOptionsSave']('<%=params%>');" />
	<input type="button" value="#(Button.Cancel)" onclick="$Actions['_tagOptionsWindow'].close();" />
</div>
<style type="text/css">
.tags_option {
	background: #f7f7ff;
	border: 1px solid #ddd;
	padding: 6px 8px;
}

.tags_option .l {
	width: 70px;
	text-align: right;
}
</style>