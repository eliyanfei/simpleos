<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.util.StringUtils"%>
<%@ page import="net.simpleframework.util.ConvertUtils"%>
<%@ page import="net.simpleframework.organization.IUser"%>
<%@ page import="net.simpleframework.organization.account.IAccount"%>
<%@ page import="net.simpleframework.organization.OrgUtils"%>
<%@ page import="net.simpleframework.organization.account.Exp"%>
<%@ page
	import="net.simpleframework.organization.component.userpager.UserPagerUtils"%>
<%@ page
	import="net.simpleframework.organization.account.AccountContext"%>
<%@ page import="net.simpleframework.web.page.PageRequestResponse"%>
<%@ page import="net.simpleframework.my.space.MySpaceUtils"%>
<%@ page import="net.simpleframework.my.space.SpaceStatBean"%><%@page
	import="net.itsite.utils.StringsUtils"%><%@page
	import="net.a.ItSiteUtil"%>


<%
	final PageRequestResponse requestResponse = new PageRequestResponse(request, response);
	final IAccount account = MySpaceUtils.getAccountAware().getAccount(requestResponse);
	if (account == null) {
		return;
	}
	final IUser user = account.user();
	if (user == null) {
		return;
	}
	final boolean me = MySpaceUtils.getAccountAware().isMyAccount(requestResponse);
	final SpaceStatBean stat = MySpaceUtils.getSpaceStat(requestResponse);
	final Object id = user.getId();
%>
<style>
.infoDesc {
	color: #24698F
}

.infoTitle {
	font-weight: bold;
	white-space: nowrap;
}
</style>
<div class="space_user_layout" id="signatureBody">
	<div class="f2"><%=user.getText()%></div>
	<jsp:include page="user_nav_tooltip.jsp">
		<jsp:param value="<%=id%>" name="userId" />
	</jsp:include>
	<div class="attention">
		<table style="width: 180px; height: 40px;" cellpadding="4">
			<tr>
				<td>
					<div class="lbl">
						<a class="a2" href="/attention/<%=id%>.html?t=attentions"><%=stat.getAttentions()%></a>
						<span>#(space_user_layout.0)</span>
					</div>
				</td>
				<td>
					<div class="lbl">
						<a class="a2" href="/attention/<%=id%>.html?t=fans"><%=stat.getFans()%></a>
						<span>#(space_user_layout.1)</span>
					</div>
				</td>
				<td>
					<div class="lbl2">
						<label><%=stat.getViews()%></label>
						<span>#(space_user_layout.2)</span>
					</div>
				</td>
			</tr>
		</table>
	</div>

	<div class="signature signature_c" onclick="writeSignature();">
		<div style="padding-left: 64px;" class="signature_c">
			<div id="signature_value" class="signature_c"><%=StringsUtils.trimNull(user.getSignature(), "这家伙，什么也没写！")%></div>
			<div id="signature_text" style="display: none;" class="signature_c">
				<textarea id="user_signature" name="user_signature" rows="4"
					style="width: 96%;"><%=StringsUtils.trimNull(user.getSignature(), "这家伙，什么也没写！")%></textarea>
			</div>
		</div>
	</div>
	<div class="attr">
		<table cellpadding="1" cellspacing="1">
			<tr>
				<td class="infoTitle">
					#(space_user_layout.3)
				</td>
				<td><%=ConvertUtils.toDateString(account.getCreateDate())%></td>
			</tr>
			<tr>
				<td class="infoTitle" valign="top">
					所在地区
				</td>
				<td class="infoDesc"><%=StringUtils.text(user.getHometownText(), "<无>")%></td>
			</tr>
			<tr>
				<td class="infoTitle">
					个人主页
				</td>
				<td class="infoDesc"><%=StringUtils.text(user.getHomepage(), "<无>")%></td>
			</tr>
		</table>
	</div>
	<%
		if (me) {
	%>
	<div class="edit_btn">
		<a class="a2" href="/my.html">#(space_user_layout.4)</a>
	</div>
	<%
		}
	%>
</div>

<script type="text/javascript">
<!--
<%if (me) {%>
var show = false;
function writeSignature(){
	$('signature_value').style.display = 'none';
	$('signature_text').style.display = '';
	show = true;
}

$ready(function(){
	$(document).observe("click", function() {
			if(!show){
				$('signature_value').style.display = '';
				$('signature_text').style.display = 'none';
				if($('signature_value').innerText!=$('user_signature').value)
				$Actions['ajaxEditUserSignature']();
			}
			show = false;
		});
});
<%}%>
//-->
</script>