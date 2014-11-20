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
<%@page import="net.itsite.ItSiteUtil"%><%@page
	import="net.simpleframework.web.page.component.ui.dictionary.SmileyUtils"%><%@page
	import="net.simpleframework.util.ConvertUtils"%><%@page
	import="java.util.List"%><%@page
	import="net.simpleframework.core.id.ID"%><%@page
	import="net.simpleframework.util.StringUtils"%><%@page
	import="net.simpleframework.core.ado.IDataObjectQuery"%>

<%
	final PageRequestResponse requestResponse = new PageRequestResponse(request, response);
%>
<style type="text/css">
.space_log_layout .space_content_item {
	border-bottom: 1px dashed #ccc;
	padding: 4px 0;
}

.space_log_layout .space_content_item:hover {
	background-color: #f8f8f8;
	-moz-transition: background-color 0.3s;
	-webkit-transition: background-color 0.3s;
	transition: background-color 0.3s;
}

.sayEditor {
	border: 2px solid #5490C0;
	float: left;
	width: 200px;
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
	width: 49px;
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
</style>
<div class="space_log_editor1">
	<textarea id="ta_space_log_editor" name="ta_space_log_editor"
		class="sayEditor"></textarea>
	<div class="saySubmit" id="saySubmit">
		我Say
	</div>
</div>
<div class="space_log_layout" id="space_log_layout">
	<%
		final IDataObjectQuery<?> qs = (IDataObjectQuery<?>) request.getAttribute("__qs");
		if (qs == null) {
			return;
		}
		Object obj;
		IUser admin = OrgUtils.um().getUserByName("admin");

		while ((obj = qs.next()) != null) {
			SapceLogBean log = (SapceLogBean) obj;
			IUser user = OrgUtils.um().queryForObjectById(log.getUserId());
			final String content = MySpaceUtils.spaceLogContent(requestResponse, log);
			final boolean hasImg = ConvertUtils.toBoolean(log.getAttribute("hasImg"), false);
			final boolean isVote = "spacevote".hashCode() == ConvertUtils.toInt(log.getRefId(), 0);
			final List<ID> list = MySpaceUtils.getReplyFrom(log.getReplyFrom());
	%>

	<div class="space_content_item">
		<table style="width: 100%;">
			<tr>
				<td valign="top" width="50">
					<img class="photo_icon" style="width: 36px; height: 36px;"
						src="<%=OrgUtils.getPhotoSRC(request, user, 64, 64)%>">
				</td>
				<td valign="top">
					<table style="width: 100%;" cellpadding="0" cellspacing="0">
						<tr>
							<td class="gray-color"><%=user == null ? "<span title=''>匿名</span>" : MySpaceUtils.getAccountAware().wrapAccountHref(requestResponse, user)%>
								<%
									for (ID uid : list) {
								%>
								@<%=MySpaceUtils.getAccountAware().wrapAccountHref(requestResponse, OrgUtils.um().queryForObjectById(uid))%>
								<%
									}
								%>
								<span style="margin-left: 10px;" class="gray-color"><%=DateUtils.getRelativeDate(log.getCreateDate())%></span>
							</td>
							<td align="right">
								<%
									if (hasImg) {
								%>
								<a class="a2"
									href="<%=request.getContextPath()%>/space.html?logId=<%=log.getId()%>"><span
									class="space_img"></span> </a>
								<%
									}
								%>
								<%
									if (isVote) {
								%>
								<a class="a2"
									href="<%=request.getContextPath()%>/space.html?logId=<%=log.getId()%>"><span
									class="space_vote"></span> </a>
								<%
									}
								%>

								<span style="vertical-align: middle;"> <%
 	if (!list.isEmpty()) {
 %> <a class="a2"
									href="<%=request.getContextPath()%>/space.html?logId=<%=log.getReplyFrom()%>">@内容</a>
									<%
										}
									%> <a class="a2"
									href="<%=request.getContextPath()%>/space.html?logId=<%=log.getId()%>">评论</a>
									(<%=log.getRemarks()%>)</span>
							</td>
						</tr>
						<tr>
							<td colspan="2" class="wrapWord" style="word-wrap:break-word;word-break:break-all;"><%=SmileyUtils.replaceSmiley(ItSiteUtil.getShortContent(log.getContent(), 100, false))%></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</div>
	<%
		}
	%>
	
	<div style="float: right;"><a href="/morespace.html">更多»</a></div>
</div>
<script type="text/javascript">
function init() {
	$Comp.addBackgroundTitle($('ta_space_log_editor'), '是不是要说点什么呢!');
	$('saySubmit').observe(
			"click",
			function(ev) {
				if ($F('ta_space_log_editor') == ''
						|| $F('ta_space_log_editor') == null
						|| $F('ta_space_log_editor') == '是不是要说点什么呢!') {
					$IT.alert('是不是要说点什么呢!');
				} else {
					$Actions['ajaxSpaceLogSave']();
					$Comp.addBackgroundTitle($('ta_space_log_editor'),
							'是不是要说点什么呢!');
				}
			});
}
init();
</script>