<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@page import="net.simpleos.module.ad.AdUtils"%><%@page
	import="net.simpleos.module.ad.EAd"%><%@page
	import="net.simpleframework.web.WebUtils"%><%@page
	import="net.simpleframework.util.StringUtils"%><%@page
	import="net.simpleos.utils.StringsUtils"%><%@page
	import="net.simpleframework.web.page.PageRequestResponse"%><%@page
	import="net.simpleframework.util.HTMLBuilder"%><%@page
	import="net.simpleos.SimpleosUtil"%>
<%
	SimpleosUtil.addMenuNav(request.getSession(), "/", "#(Itsite.menu.home)", true);
	final PageRequestResponse requestResponse = new PageRequestResponse(request, response);
%>
<style>
.navbar-brand {
	font-size: 24px;
	line-height: 20px;
	font-weight: bolder;
}

a.mm:hover {
	background-color: #00008B;
	height: 50px;
	text-decoration: none;
	color: white;
	cursor: pointer;
}

a.mm.active {
	background-color: #00008B;
	height: 50px;
	text-decoration: none;
	color: white;
	cursor: pointer;
}

a.mm {
	display: inline-block;
	cursor: default;
	height: 50px;
	line-height: 50px;
	padding: 0 15px 0 15px;
	border: 1px solid transparent;
	_border-color: tomato;
	_filter: chroma(color =                 tomato);
	line-height: 50px\0;
	font-size: 16px;
	font-weight: bold;
}
</style>
<div id="site_header2">
	<div class="HeaderPageT2">
		<div class="desc" style="float: left;">
			<table cellpadding="0" cellspacing="0" height="100%">
				<tr>
					<td class="navbar-brand" nowrap="nowrap">
						智汇企业门户系统
					</td>
					<td align="left" class="menubar">
						<div id="_simple_menu" align="left" style="margin-left: 10px;"></div>
					</td>
				</tr>
			</table>
		</div>
		<div style="float: right;line-height: 50px;">
			<jsp:include page="header_tb.jsp" flush="true"></jsp:include>
		</div>
	</div>
</div>
<style>
.block_layout1,.block_layout2,.list_layout .rrow {
	-moz-border-radius: 4px;
	-webkit-border-radius: 4px;
	border-radius: 4px;
}

.simple_tabs a.active {
	-moz-border-radius: 4px 4px 0 0;
	-webkit-border-radius: 4px 4px 0 0;
	border-radius: 4px 4px 0 0;
}

.block_layout1 .t {
	border-bottom: 1.5px solid #CCC;
}
</style>
<script type="text/javascript">
function __login_callback() {
	location.reload();
}

var load = true;

if ($Actions['mainMenu']) {
	$Actions['mainMenu']();
	load = false;
}

$ready(function() {
	if (load) {
		$Actions['mainMenu']();
	}
});

$Actions.showError = function(err, alertType) {
	if (!window.UI || !window.UI.Window || alertType) {
		alert(err.title);
	} else {
		if (!this.errWindows) {
			this.errWindows = {};
		}
		var _WIDTH = 600;
		var _HEIGHT = 150;
		var w;
		if (this.errWindows[err.hash]) {
			w = this.errWindows[err.hash];
		} else {
			w = this.errWindows[err.hash] = new UI.Window( {
				minimize : false,
				maximize : false,
				width : _WIDTH,
				height : _HEIGHT
			});
			w.observe("hidden", function() {
				delete this.errWindows[err.hash];
			}.bind(this));
		}

		var detail = new Element("div");
		var c = new Element("div", {
			className : "sy_error_dialog"
		}).insert(new Element("div", {
			className : "simple_toolbar et wrap_text"
		}).update(err.title.convertHtmlLines())).insert(detail);

		var right = new Element("div", {
			style : "float: right;"
		});

		c.insert(new Element("div", {
			className : "eb"
		}).insert(right));

		w.setHeader($MessageConst["Error.Title"]).setContent(c);
		w.center().observe("shown", function() {
			w.adapt().center().activate();
		}).show(true);
	}
};
</script>