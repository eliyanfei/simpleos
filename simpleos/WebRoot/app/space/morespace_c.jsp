<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@page
	import="net.simpleframework.my.space.MySpaceUtils"%>

<%
	final String space_log_editor = MySpaceUtils.deployPath + "jsp/space_log_editor.jsp";
%>
<div class="simple_toolbar space_tabs_content"><jsp:include
		page="<%=space_log_editor %>"></jsp:include><div id="moreSpaceLogId"
		style="margin-top: 10px;"></div>
</div>
<script type="text/javascript">
	function space_log_image_click(img, params) {
		if (!img.callback) {
			img.observeImageLoad(function() {
				img.show();
				img.up().$style("width: 400px;");
			});
			img.callback = function(src) {
				if (!src)
					return;
				img.hide();
				img.osrc = img.src;
				img.src = img.nsrc = src;
			};
			$Actions["ajaxSpaceLogImageLoaded"](params);
		} else {
			img.clearImageLoad();
			if (img.src == img.osrc) {
				img.src = img.nsrc;
				img.up().$style("width: 400px;");
			} else {
				img.src = img.osrc;
				img.up().$style("width: 210px;");
			}
		}
	}
</script>