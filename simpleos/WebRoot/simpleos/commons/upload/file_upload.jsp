<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div id="fileForm__">
	<input type="hidden" id="refId" name="refId"
		value="<%=request.getParameter("refId")%>">
	<div id="addFilesSwfUpload"></div>
</div>
<script type="text/javascript">
$ready(function() {
	var win = $Actions["addFilepagerWindow"].window;
	if (win) {
		win.observe("resize:ended", function() {
			$Actions["addFilesSwfUpload"].updateUI();
		});
	}
});
</script>