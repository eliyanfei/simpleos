<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.organization.OrgUtils"%>
<%@ page import="net.simpleframework.organization.IJobMember"%>
<%@ page import="net.simpleframework.organization.EMemberType"%>
<%@ page import="net.simpleframework.organization.IJob"%>
<%
	final IJob job = OrgUtils.jm()
			.queryForObjectById(
					request.getParameter(OrgUtils.jm()
							.getJobIdParameterName()));
	for (IJobMember jm : OrgUtils.jmm().members(job)) {
%>
<div class="sl">
	<table style="width: 100%;" cellpadding="0" cellspacing="0">
		<tr>
			<td style="width: 180px;" valign="top">
				<table style="width: 100%;" cellpadding="2" cellspacing="0">
					<tr>
						<td class="l">#(job_detail_m.0)</td>
						<td class="v">
							<%
								if (jm.getMemberType() == EMemberType.user) {
							%>#(job_detail_m.1)<%
								} else {
							%>#(job_detail_m.2)<%
								}
							%>
						</td>
					</tr>
					<tr>
						<td class="l">#(job_detail_m.3)</td>
						<td class="v"><%=jm.memberBean()%></td>
					</tr>
				</table></td>
			<td valign="top">
				<table style="width: 100%;" cellpadding="2" cellspacing="0">
					<tr>
						<td class="l">#(job_detail_m.4)</td>
						<td class="v">
							<%
								if (jm.isPrimaryJob()) {
							%>#(job_detail_m.5)<%
								} else {
							%>#(job_detail_m.6)<%
								}
							%>
						</td>
					</tr>
				</table></td>
			<td style="width: 30px;" align="center">
				<div class="delete_image"
					onclick="$Actions['ajaxJobmemberDelete']('memberType=<%=jm.getMemberType()%>&memberId=<%=jm.getMemberId()%>');"></div>
			</td>
		</tr>
	</table>
</div>
<%
	}
%>