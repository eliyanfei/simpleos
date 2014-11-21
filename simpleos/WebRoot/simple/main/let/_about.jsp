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
				<img
					src="<%=request.getContextPath()%>/simple/main/let/logo.png?v=3"
					alt="">
			</dd>
			<dd>
				<%=$VType.getNowVType().toString()%>(
				<span style="font-weight: bold;"><%=$VType.getNowVType().ver.toString()%></span>)
			</dd>
		</dl>
		<dl class="foot-lst fl fl1">
			<dt>
				关于产品
			</dt>
			<dd>
				<a href="http://www.simpleos.net" id="official">官方网站</a>
			</dd>
			<dd>
				<a href="http://www.simpleos.net/news.html" id="changelog">最新资讯</a>
			</dd>
		</dl>
		<dl class="foot-lst fl fl1">
			<dt>
				技术支持
			</dt>
			<dd>
				<a href="http://www.simpleos.net/bbs/tl/409479.html" id="vip">文档资料</a>
			</dd>
			<dd>
				<a href="http://www.simpleos.net/bbs/tl/409480.html" id="faq">常见问题</a>
			</dd>
		</dl>
	</div>
</div>
<div style="clear: both;"></div>
<div
	style="border-top: 1px dotted #ccc; text-align: center; padding-top: 10px;">
	<a href="http://www.simpleos.net" style="color: blue;" id="donate">捐助我们</a>
	<a href="http://www.simpleos.net/bbs/tl/409481.html" style="color: blue;" id="reportbug">汇报Bug</a>
	<a href="http://www.simpleos.net/bbs/tl/409596.html" style="color: blue;" id="feedback">反馈需求</a>
</div>
<div style="bottom: 13px; right: 3px; position: absolute;">
	Copyright ©2011 版权所有
</div>