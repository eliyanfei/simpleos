<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.my.space.SapceLogBean"%>
<%@ page import="net.simpleframework.my.space.MySpaceUtils"%>
<%@ page import="net.simpleframework.ado.db.ExpressionValue"%>
<%@ page import="net.simpleframework.web.EFunctionModule"%>
<%@ page import="net.simpleframework.ado.db.IQueryEntitySet"%>
<%@ page import="net.simpleframework.organization.IUser"%>
<%@ page import="net.simpleframework.organization.OrgUtils"%>
<%@ page import="net.simpleframework.web.page.PageRequestResponse"%>
<%@ page import="net.simpleframework.util.DateUtils"%><%@page
	import="net.simpleframework.content.ContentUtils"%>
<%@page import="net.simpleos.SimpleosUtil"%><%@page
	import="net.simpleframework.web.page.component.ui.dictionary.SmileyUtils"%>
<%
	final PageRequestResponse requestResponse = new PageRequestResponse(request, response);
%>
<style>
.sayEditor {
	border: 2px solid #5490C0;
	float: left;
	width: 78%;
	height: 36px;
	padding: 2px;
	resize: none;
	overflow: auto;
	font-size: 9pt;
	resize: none;
	-moz-border-radius: 4px 0 0 4px;
	-webkit-border-radius: 4px 0 0 4px;
	border-radius: 4px 0 0 4px;
}

.saySubmit {
	text-align: center;
	float: left;
	width: 18%;
	height: 44px;
	margin-right: 3px;
	font-size: 10pt;
	line-height: 42px;
	color: white;
	background: #5490C0;
	-moz-border-radius: 0 4px 4px 0;
	-webkit-border-radius: 0 4px 4px 0;
	border-radius: 0 4px 4px 0;
	cursor: pointer;
}

.saySubmit:HOVER {
	font-weight: bold;
}
}
</style>
<div class="space_log_layout" id="space_log_layout">
	<%
		if (SimpleosUtil.isLogin(requestResponse)) {
	%>
	<div class="space_log_editor1">
		<textarea id="ta_space_log_editor" name="ta_space_log_editor"
			class="sayEditor"></textarea>
		<div class="saySubmit" id="saySubmit">
			我Say
		</div>
	</div>
	<%
		}
	%>
	<%
		final IQueryEntitySet<SapceLogBean> qs = MySpaceUtils.getTableEntityManager(SapceLogBean.class).query(
			new ExpressionValue("(refModule=? or refModule=?) and content is not null order by createdate desc", new Object[] {
					EFunctionModule.space_log, EFunctionModule.docu }), SapceLogBean.class);
			qs.setCount(5);
			SapceLogBean log;
			while ((log = qs.next()) != null) {
		IUser user = OrgUtils.um().queryForObjectById(log.getUserId());
		if (user == null) {
			continue;
		}
	%>
	<div class="item">
		<table style="width: 100%;" cellpadding="0" cellspacing="0"
			class="fixed_table">
			<tr>
				<td valign="top" width="40">
					<img class="photo_icon" style="width: 24px; height: 24px;"
						src="<%=OrgUtils.getPhotoSRC(request, user, 64, 64)%>">
				</td>
				<td>
					<div><%=MySpaceUtils.getAccountAware().wrapAccountHref(requestResponse, user)%><span
							style="margin-left: 10px;" class="gray-color"><%=DateUtils.getRelativeDate(log.getCreateDate())%></span>
					</div>
					<div style="padding: 4px 0 2px 0;" class="wrap_text"><%=SmileyUtils.replaceSmiley(SimpleosUtil.getShortContent(log.getContent(), 100, false))%></div>
				</td>
			</tr>
		</table>
	</div>
	<%
		}
	%>
	<div class="btn">
		<a href="/morespace.html">#(space_log_layout.0)</a>
	</div>
</div>
<style type="text/css">
.space_log_layout .item {
	border-bottom: 1px dashed #ccc;
	padding: 4px 0;
}

.space_log_layout .item:hover {
	background-color: #f8f8f8;
	-moz-transition: background-color 0.3s;
	-webkit-transition: background-color 0.3s;
	transition: background-color 0.3s;
}

.space_log_layout .btn {
	text-align: right;
	padding-top: 4px;
}
</style>
<script type="text/javascript">
function init(){
	$Comp.addBackgroundTitle($('ta_space_log_editor'), '是不是要说点什么呢!');
	$('saySubmit').observe("click", function(ev) {
			if($F('ta_space_log_editor')==''||$F('ta_space_log_editor')==null||$F('ta_space_log_editor')=='是不是要说点什么呢!'){
				alert('是不是要说点什么呢!');
			}else{
				$Actions['ajaxSpaceLogSave']();
			}
		});
}
init();
</script>
