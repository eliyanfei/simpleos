<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.util.StringUtils"%>
<%@ page import="net.simpleframework.organization.OrgUtils"%>
<%@ page import="net.simpleframework.util.AlgorithmUtils"%>
<html>
<body>
	<form id="uploadPhoto" style="width: 280px;">
		<input type="hidden" id="<%=OrgUtils.um().getUserIdParameterName()%>"
			name="<%=OrgUtils.um().getUserIdParameterName()%>"
			value="<%=StringUtils.blank(request.getParameter(OrgUtils.um()
					.getUserIdParameterName()))%>" />
	</form>
	<input style="margin-top: 4px;" type="button" id="btnUploadPhoto" value="#(Upload)"
		onclick="$Actions['uploadPhoto']();" />
	<p>#(user_edit_photo_upload.0)</p>	
	<script type="text/javascript">
		(function() {
			var ele = $Comp.textFileButton();
			ele.file.writeAttribute("id", "user_photo");
			ele.file.writeAttribute("name", "user_photo");
			$("uploadPhoto").insert(ele);
		})();
	</script>
	<%
		final String isrc = request.getParameter("isrc");
		if (StringUtils.hasText(isrc)) {
	%>
	<script type="text/javascript">
		(function() {
			parent.$("user_edit_photo_image").src = 
				"<%=new String(AlgorithmUtils.base64Decode(isrc))%>";
		})();
	</script>
	<%
		}
	%>
</body>
</html>