<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@page
	import="net.simpleframework.web.page.PageRequestResponse"%><%@page
	import="net.itsite.document.docu.DocuBean"%><%@page
	import="net.itsite.document.docu.DocuUtils"%><%@page
	import="net.simpleframework.util.IoUtils"%><%@page
	import="net.simpleframework.organization.OrgUtils"%><%@page
	import="net.a.ItSiteUtil"%><%@page
	import="net.simpleframework.organization.account.IAccount"%><%@page
	import="java.util.Random"%><%@page
	import="net.simpleframework.organization.account.Account"%>

<%
	final PageRequestResponse requestResponse = new PageRequestResponse(request, response);
	IAccount account = ItSiteUtil.getLoginAccount(requestResponse);
	final DocuBean docuBean = DocuUtils.applicationModule.getViewDocuBean(requestResponse);
	if (account == null) {
		account = new Account();
	}
	if (docuBean == null || account == null) {
		return;
	}
%>
<style>
.list51 {
	margin-top: 0px;
	padding: 0px;
}

.list51 li {
	font-weight: bold;
}

.list5 li {
	line-height: 24px;
	padding-left: 25px;
	font-size: 14px;
	color: black;
}

.padd1 {
	padding: 6px 0px 0px 24px;
	line-height: 25px;
	font-size: 14px;
	color: black;
}

#ad_000 {
	opacity: 0.0;
	filter: alpha(opacity =       0);
	-moz-opacity: 0.0;
}
</style>
<div id="docu_down" style="background: #EBF7FA; padding-bottom: 10px;">
	<div class="bor">
		<input type="hidden" id="docuShow"
			value="<%=new Random().nextInt(10) > 6%>">
		<div class="padd">
			<div class="padd1">
				<table>
					<tr>
						<td nowrap="nowrap" valign="top">
							#(Docu.down.0)：
						</td>
						<td>
							<%=docuBean.getTitle()%><img alt="<%=docuBean.getExtension()%>"
								style="margin-left: 5px;"
								src="<%=DocuUtils.getFileImage(requestResponse, docuBean)%>">
						</td>
					</tr>
					<tr>
						<td nowrap="nowrap">
							#(Docu.down.1)：
						</td>
						<td class="nnum">
							<%=IoUtils.toFileSize(docuBean.getFileSize())%>
						</td>
					</tr>
				</table>

			</div>
			<div class="padd2" align="center" style="position: relative;">
				<div style="position: absolute; left: 200px; z-index: 1;"
					onmouseover="adAdd();">
					<input type="button" value="#(Docu.down)" class="button2"
						style="font-weight: bold; color: #A41C05;"
						onclick="$Actions['docuDownloadAct']('docuId=<%=docuBean.getId()%>&s=<%=docuBean.getFileSize()%>');">
				</div>
				<div id="ad_000"
					style="position: absolute; display: none; z-index: 2;"></div>
			</div>
		</div>
	</div>
</div>
<div id="ad_001"></div>
<script type="text/javascript">
function adAdd() {
	if ($('docuShow').value == 'true') {
		$('docuShow').value = 'false';
		$('ad_000').style.display = '';
		new PeriodicalExecuter(function(executer) {
			$('ad_000').innerHTML = '';
			executer.stop();
		}, 3);
	}
}
$ready(function() {
	$('ad_001').innerHTML = $('ad_00').innerHTML;
	if ($('docuShow').value == 'true') {
		$('ad_000').innerHTML = $('ad_001').innerHTML;
	}
});
</script>