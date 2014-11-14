<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.web.page.PageRequestResponse"%>
<%@ page import="net.simpleframework.my.space.MySpaceUtils"%><%@page
	import="net.simpleframework.util.ConvertUtils"%><%@page
	import="net.simpleframework.util.StringUtils"%>
<%
	final PageRequestResponse requestResponse = new PageRequestResponse(request, response);
	final String logId = request.getParameter("logId");
	final boolean hasLog = StringUtils.hasText(logId);
%>
<div class="simple_tabs_content space_tabs_content">
	<%
		if (MySpaceUtils.getAccountAware().isMyAccount(requestResponse)) {
	%>
	<jsp:include page="space_log_editor.jsp"></jsp:include>
	<%
		if(!hasLog){
	%>
	<div style="text-align: right; margin: 8px 4px 0px 0px;">
		<%=MySpaceUtils.applicationModule.logTabs(requestResponse)%></div>
	<%
		}
		}
	%>
	<%
		if(!hasLog){
	%>
	<div id="__my_space_log_pager" style="padding: 4px;"></div>
	<%}else{ %>
	<div id="__my_space_log_id"></div>
	<%} %>
</div>
<script type="text/javascript">
function space_log_image_click(img, params) {
	if (!img.callback) {
		img.observeImageLoad(function() {
			img.show();
			img.up().$style("width: 600px;");
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
			img.up().$style("width: 600px;");
		} else {
			img.src = img.osrc;
			img.up().$style("width: 210px;");
		}
	}
}
</script>