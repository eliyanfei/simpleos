<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@page import="net.simpleos.$VType"%>


<style>
<!--
.foot-lst {
	width: 145px;
	height: 200px;
	padding: 0 16px 0 15px;
}

.foot-lst-first,.foot-lst-last {
	width: 180px;
}

.foot-lst-first {
	padding-left: 0;
}

.footer-wrap {
	width: 100%;
}

.foot-lst dt {
	font-size: 14px;
}

.fl {
	float: left;
}

.fl1 {
	border-left: 1px dotted #666;
}

dl,dt,dd {
	margin: 0;
	padding: 0;
}

.footer-wrap dt {
	margin-bottom: 15px;
}

.footer-wrap dl dd {
	margin-bottom: 4px;
}
-->
</style>
<div class="footer-wrap">
	<div>
		<dl class="foot-lst foot-lst-first fl"
			style="text-align: center; padding-top: 20px;">
			<dd>
				<a href="http://www.simpleos.net"> <img
					src="<%=request.getContextPath()%>/simpleos/about/logo.png?v=3"
					alt="">
				</a>
			</dd>
			<dd>
				<%=$VType.getNowVType().toString()%>( <span
					style="font-weight: bold;"><%=$VType.getNowVType().ver.toString()%></span>)
			</dd>
		</dl>
		<dl class="foot-lst fl fl1">
			<dt>关于平台</dt>
			<dd>
				<a href="http://www.simpleos.net/developer.html" id="changelog">开发者</a>
			</dd>
			<dd>
				<a href="http://www.simpleos.net/use.html" id="changelog">使用者</a>
			</dd>
			<dd>
				<a href="http://www.simpleos.net" id="official">官方网站</a>
			</dd>
			<dd>
				<a href="http://www.simpleos.net" id="donate">捐助我们</a>
			</dd>
		</dl>
		<dl class="foot-lst fl fl1">
			<dt>技术支持</dt>
			<dd>
				<a href="http://www.simpleos.net/docu.html" id="vip">文档资料</a>
			</dd>
			<dd>
				<a href="http://www.simpleos.net/bbs/tl/3370.html" id="faq">最新版本</a>
			</dd>
			<dd>
				<a href="http://www.simpleos.net/bbs/tl/3411.html" id="faq">问题咨询</a>
			</dd>
		</dl>
	</div>
</div>
<div
	style="clear: both;border-top: 1px dotted #ccc; text-align: center; padding-top: 10px;"></div>
<div style="bottom: 13px; right: 3px; position: absolute;">
	Copyright ©2014 版权所有</div>