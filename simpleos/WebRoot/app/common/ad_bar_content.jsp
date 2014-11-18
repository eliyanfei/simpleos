<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@page import="net.itsite.ad.AdUtils"%><%@page
	import="net.itsite.ad.EAd"%>
<style>
</style>
<div id="ad_bar">
	<%=AdUtils.getAd(EAd.content)%>
</div>
<script type="text/javascript">
<!--
	var oldPY = $('ad_bar').viewportOffset()[1];
	window.onscroll = function() {
		var adb = $('ad_bar');
		var pY = adb.viewportOffset()[1];
		if (pY <= 0) {
			adb.addClassName('ad_bar');
		}
		var top = document.body.scrollTop || document.documentElement.scrollTop
				|| window.pageYOffset;

		if (top <= oldPY) {
			adb.removeClassName('ad_bar');
		}
		;
	};
//-->
</script>