<%@page import="net.simpleos.module.ad.AdUtils"%>
<%@page import="net.simpleos.module.ad.EAd"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<style>
<!--
.ad_bar {
	position: fixed;
	top: 40px;
	z-index: 1;
	margin-left: 0px;
}
-->
</style>
<div id="ad_bar" align="center">
	<%=AdUtils.getAd(EAd.right)%>
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