<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div id="swfForm">
	<div id="addFilesSwfUpload"></div>
	<input type="hidden" id="refId" name="refId" value="<%=request.getParameter("refId") %>">
	<input type="hidden" id="te" name="te" value="<%=request.getParameter("te") %>">
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