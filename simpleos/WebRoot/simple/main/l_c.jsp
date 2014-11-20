<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@page
	import="net.simpleframework.web.page.PageRequestResponse"%><%@page
	import="net.itsite.ItSiteUtil"%><%@page
	import="net.simpleframework.util.HTMLBuilder"%>

<%
	final PageRequestResponse requestResponse = new PageRequestResponse(request, response);
%>
<div class="HomePage" style="padding: 0 15px;">
	<table>
		<tbody>
			<tr>
				<td valign="top" width="70%">
					<div class="block">
						<div class="b21">
						</div>
						<div class="b21">
						</div>
					</div>
					<div class="block">
						<div class="b21">
						</div>
						<div class="b21">
						</div>
					</div>
					<div class="block">
						<div class="b22">
						</div>
					</div>
				</td>
				<td width="30%" valign="top">
					<div class="block">
						<div class="b211">
						</div>
					</div>
					<div class="block">
						<div class="b211">
						</div>
					</div>
				</td>
			</tr>
		</tbody>
	</table>
</div>
<style type="text/css">
#t_body {
	padding: 3px;
}

.layout ul.sortable li {
	margin: 3px 3px 6px 3px;
}
</style>
<script type="text/javascript">
$ready(function() {
	if (Browser.IEVersion && Browser.IEVersion <= 7.0) {
		$Actions["ieMessageWindow"].startMessage();
	}
	//		$Actions.callSafely("new_tooltip", null, function(act) {
	//			act.show("simple_new_function");
	//			return true;
	//		});
});
</script>
