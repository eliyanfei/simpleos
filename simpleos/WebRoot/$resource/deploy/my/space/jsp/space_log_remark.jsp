<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.ado.db.IQueryEntitySet"%>
<%@ page import="net.simpleframework.my.space.SapceRemarkBean"%>
<%@ page import="net.simpleframework.my.space.MySpaceUtils"%>
<%@ page import="net.simpleframework.organization.OrgUtils"%>
<%@ page import="net.simpleframework.organization.IUser"%>
<%@ page import="net.simpleframework.web.page.PageRequestResponse"%>
<%@ page import="net.simpleframework.util.DateUtils"%>
<%@ page import="net.simpleframework.organization.account.IAccount"%>
<%@ page
	import="net.simpleframework.organization.account.AccountSession"%>
<%@ page import="net.simpleframework.my.space.SapceLogBean"%>
<%
	final SapceLogBean sapceLog = MySpaceUtils.getBeanById(
			request.getParameter("logid"), SapceLogBean.class);
	if (sapceLog == null) {
		return;
	}
%>
<div class="space_log_remark_top"
	style="background-position: 96% center;"></div>
<div class="simple_toolbar space_log_remark">
	<input type="hidden" name="logid" value="<%=sapceLog.getId()%>" />
	<div class="ta">
		<textarea rows="3" name="ta_space_log_remark"></textarea>
	</div>
	<div class="btn">
		<table style="width: 100%;" cellpadding="0" cellspacing="0">
			<tr>
				<td align="left">
					<div>
						<input style="vertical-align: middle;" value="true" checked
							name="cb_space_log_opt1" type="checkbox" /><label
							onclick="var c = this.previous(); c.checked = !c.checked;">#(space_log_remark.1)</label>
					</div></td>
				<td align="right"><input type="button" class="button2"
					value="#(space_log_remark.0)"
					onclick="var form = this.up('.space_log_remark'); if ($F(form.down('textarea')).strip() == '') { return; } var act = $Actions['ajaxSpaceLogRemarkSave']; act.selector = form; act();" />
				</td>
			</tr>
		</table>
	</div>
	<%
		final IAccount login = AccountSession.getLogin(session);
		final PageRequestResponse requestResponse = new PageRequestResponse(
				request, response);
		final IQueryEntitySet<SapceRemarkBean> qs = MySpaceUtils.applicationModule
				.remarkList(sapceLog);
		SapceRemarkBean remark;
		while ((remark = qs.next()) != null) {
			final IUser user = OrgUtils.um()
					.queryForObjectById(remark.getUserId());
			if (user == null) {
				continue;
			}
			final boolean canDelete = OrgUtils.isManagerMember(session)
					|| (login != null && (login.getId().equals2(
							sapceLog.getUserId()) || login.getId().equals2(
							user.getId())));
	%>
	<div class="space_remark_item">
		<table style="width: 100%;" cellpadding="0" cellspacing="0">
			<tr>
				<td valign="top" width="40"><img class="photo_icon"
					style="width: 24px; height: 24px;"
					src="<%=OrgUtils.getPhotoSRC(request, user, 64, 64)%>">
				</td>
				<td>
					<table style="width: 100%;" cellpadding="0" cellspacing="0">
						<tr>
							<td><%=MySpaceUtils.getAccountAware().wrapAccountHref(
						requestResponse, user)%><span style="margin-left: 10px;"
								class="gray-color"><%=DateUtils.getRelativeDate(remark.getCreateDate())%></span>
							</td>
							<td align="right">
								<%
									if (canDelete) {
								%> <a class="a2"
								onclick="$Actions['ajaxSpaceLogRemarkDelete']('remarkid=<%=remark.getId()%>');">#(Delete)</a>
								<%
									}
								%>
							</td>
						</tr>
					</table>
					<div style="padding: 4px 0 2px 0;">
						<%=MySpaceUtils.getContent(remark.getContent())%></div></td>
			</tr>
		</table>
	</div>
	<%
		}
	%>
</div>