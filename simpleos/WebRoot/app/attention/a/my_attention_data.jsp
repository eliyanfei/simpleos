<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@page
	import="net.simpleframework.web.page.PageRequestResponse"%><%@page
	import="net.simpleframework.web.page.component.ui.pager.PagerUtils"%><%@page
	import="net.simpleframework.my.space.UserAttentionBean"%><%@page
	import="net.simpleframework.organization.OrgUtils"%><%@page
	import="net.simpleframework.organization.account.IAccount"%><%@page
	import="net.simpleframework.content.ContentUtils"%><%@page
	import="net.simpleframework.util.DateUtils"%><%@page
	import="net.simpleframework.organization.IUser"%><%@page
	import="net.simpleframework.util.StringUtils"%><%@page
	import="net.simpleframework.sysmgr.dict.DictUtils"%><%@page
	import="net.simpleframework.my.space.MySpaceUtils"%><%@page
	import="net.simpleframework.ado.db.IQueryEntitySet"%><%@page
	import="net.simpleframework.my.space.SapceLogBean"%><%@page
	import="net.simpleframework.ado.db.ExpressionValue"%><%@page
	import="net.simpleframework.web.page.component.ui.dictionary.SmileyUtils"%><%@page
	import="net.simpleos.SimpleosUtil"%><%@page
	import="net.simpleframework.web.EFunctionModule"%><%@page
	import="net.simpleframework.util.ConvertUtils"%><%@page
	import="net.simpleframework.my.space.SpaceStatBean"%><%@page import="net.simpleframework.util.HTMLBuilder"%>

<div class="my_message_list account_layout">
	<%
		final PageRequestResponse requestResponse = new PageRequestResponse(request, response);
			final String t = request.getParameter("t");
			for (Object o : PagerUtils.getPagerList(request)) {
		UserAttentionBean attentionBean = (UserAttentionBean) o;
		final IAccount account = OrgUtils.am().queryForObjectById(
				"fans".equals(t) ? attentionBean.getUserId() : attentionBean.getAttentionId());
		if (account == null)
			continue;
		final IUser user = account.user();
		final SapceLogBean log = MySpaceUtils.getTableEntityManager(SapceLogBean.class).queryForObject(
				new ExpressionValue("(refModule=? and userId=?) and content is not null order by createdate desc", new Object[] {
						EFunctionModule.space_log, user.getId() }), SapceLogBean.class);
		final String sex = "男".equals(user.getSex()) ? "他" : "她";
		final SpaceStatBean stat = MySpaceUtils.getSpaceStatById(user.getId());
	%>

	<table cellpadding="0" cellspacing="0" class="item">
		<tr>
			<td width="80px" align="right" valign="top">
				<div class="li">
					<%=ContentUtils.getAccountAware().wrapImgAccountHref(requestResponse, account)%>
				</div>
			</td>
			<td class="pbg"></td>
			<td valign="top">
				<div class="txt">
					<table cellpadding="0" cellspacing="0">
						<tr>
							<td>
								<%=ContentUtils.getAccountAware().wrapAccountHref(requestResponse, account)%>
								<span><%=StringUtils.text(DictUtils.buildSysDict(user.getHometown()), "其他")%></span>
							</td>
							<td align="right" nowrap="nowrap">
								<%=MySpaceUtils.buildUserAttention(requestResponse, user.getId())%>
								<%=HTMLBuilder.SEP%>
								<a class="a2"
									onclick="$Actions['myMessageSentWindow']('userid=<%=user.getId()%>');">发消息</a>
								<%=HTMLBuilder.SEP%>
								<a class="a2"
									onclick="$Actions['myDialogWindow']('userid=<%=user.getId()%>');">对话</a>
							</td>
						</tr>
					</table>
					<p style="color: #24691F;">
						<%=sex%>的关注
						<a href="/attention/<%=user.getId()%>.html?t=attentions">(<%=stat.getAttentions()%>)</a>，
						<%=sex%>的粉丝
						<a href="/attention/<%=user.getId()%>.html?t=fans">(<%=stat.getFans()%>)</a>
					</p>
					<%
						if (StringUtils.hasText(user.getDescription())) {
					%>
					<p>
						简介：<%=user.getDescription()%></p>
					<%
						}
					%>
					<div class="space_log_layout">
						<%
							if (log != null) {
						%>
						<div class="item">
							<table style="width: 100%;" cellpadding="0" cellspacing="0"
								class="fixed_table">
								<tr>
									<td>
										<div style="padding: 4px 0 2px 0;" class="wrap_text"><%=SmileyUtils.replaceSmiley(SimpleosUtil.getShortContent(log.getContent(), 100, false))%><%=ConvertUtils.toDateString(log.getCreateDate())%></div>
									</td>
								</tr>
							</table>
						</div>
						<%
							}
						%>
					</div>
				</div>
			</td>
		</tr>
	</table>
	<%
		}
	%>
</div>
