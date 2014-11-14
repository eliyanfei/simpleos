<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div style="padding: 3px;">#(space_log.editor.1)</div>
<div id="space_img_swfupload"></div>
<script type="text/javascript">
	$ready(function() {
		var win = $Actions["windowSpaceImgUpload"].window;
		if (win) {
			win.observe("resize:ended", function() {
				$Actions["space_img_swfupload"].updateUI();
			});
		}
	});
</script>