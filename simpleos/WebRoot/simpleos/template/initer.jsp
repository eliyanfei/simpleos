<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div id="_idTopControl" title="回到顶部" style="display: none;">
	<img class="img" border=0 src="/simpleos/template/_top.jpg">
</div>
<script type="text/javascript">
$ready(function() {
	var tc = $("_idTopControl").observe("click", function() {
		window.scrollTo(0, 0);
	});
	Event.observe(window, "scroll", function() {
		var scrollTop = document.documentElement.scrollTop
				|| document.body.scrollTop || 0;
		tc.style.display = scrollTop > 1 ? "" : "none";
	});
});
</script>