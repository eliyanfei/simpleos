package net.simpleframework.content.component.remark;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.itsite.ItSiteUtil;
import net.simpleframework.content.blog.BlogRemarkHandle;
import net.simpleframework.content.news.NewsRemarkHandle;
import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.core.ado.db.Table;
import net.simpleframework.organization.OrgUtils;
import net.simpleframework.organization.account.AccountSession;
import net.simpleframework.organization.account.IAccount;
import net.simpleframework.util.DateUtils;
import net.simpleframework.util.HTMLBuilder;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.EFunctionModule;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.pager.PagerUtils;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class RemarkUtils {

	public static final String BEAN_ID = "remark_@bid";

	public static ComponentParameter getComponentParameter(final PageRequestResponse requestResponse) {
		return ComponentParameter.get(requestResponse, BEAN_ID);
	}

	public static ComponentParameter getComponentParameter(final HttpServletRequest request, final HttpServletResponse response) {
		return ComponentParameter.get(request, response, BEAN_ID);
	}

	public static Table table_remark = new Table("simple_remark");

	public static String itemsHtml(final ComponentParameter compParameter) {
		return itemsHtml(compParameter, PagerUtils.getPagerList(compParameter.request));
	}

	private static String itemsHtml(final ComponentParameter compParameter, final List<?> items) {
		if (items == null) {
			return "";
		}
		final StringBuilder sb = new StringBuilder();
		final IRemarkHandle remarkHandle = (IRemarkHandle) compParameter.getComponentHandle();
		for (final Object object : items) {
			final RemarkItem item = (RemarkItem) object;
			sb.append("<div class='item'>");
			sb.append("<div class='d1'>");
			sb.append("<span class=\"ut\">").append(StringUtils.text(item.getUserText(), LocaleI18n.getMessage("remark.3")));
			sb.append(" (").append(item.getIp()).append(")</span>, ");
			sb.append("<span class=\"cd\">").append(DateUtils.getRelativeDate(item.getCreateDate()));
			sb.append("</span>");
			sb.append("</div>");
			sb.append("<div class='inherit_c wrap_text'>").append(item.getContent()).append("</div>");
			sb.append("<div class='d2'>");

			final ArrayList<String> al = new ArrayList<String>();
			final IAccount account = AccountSession.getLogin(compParameter.getSession());
			if (account != null) {
				final String jobEdit = (String) compParameter.getBeanProperty("jobEdit");
				//TODO 不需要编辑功能
				/*if (OrgUtils.isMember(jobEdit, account.user()) || isRemarkEdit(account, item)) {
					final StringBuilder sb2 = new StringBuilder();
					sb2.append("<a onclick=\"__remark_window(this, 'itemId=");
					sb2.append(item.getId()).append("');\">");
					sb2.append(LocaleI18n.getMessage("remark.7"));
					sb2.append("</a>");
					al.add(sb2.toString());
				}*/
				final String jobDelete = (String) compParameter.getBeanProperty("jobDelete");
				if (OrgUtils.isMember(jobDelete, account.user())) {
					final StringBuilder sb2 = new StringBuilder();
					sb2.append("<a onclick=\"$Actions['ajaxRemarkDelete']('itemId=");
					sb2.append(item.getId()).append("');\">");
					sb2.append(LocaleI18n.getMessage("remark.8"));
					sb2.append("</a>");
					al.add(sb2.toString());
				}
			}

			final IDataObjectQuery<RemarkItem> qs = remarkHandle.getRemarkItems(compParameter, item.getId());
			final List<RemarkItem> items2 = new ArrayList<RemarkItem>();
			RemarkItem item2;
			while ((item2 = qs.next()) != null) {
				items2.add(item2);
			}

			if ((Boolean) compParameter.getBeanProperty("showReply")) {
				final StringBuilder sb2 = new StringBuilder();
				sb2.append("<a onclick=\"__remark_window(this, 'parentId=");
				sb2.append(item.getId()).append("');\">");
				sb2.append(LocaleI18n.getMessage("remark.4"));
				sb2.append("</a><label>");
				if (items2.size() > 0) {
					sb2.append("(").append(items2.size()).append(")");
				}
				sb2.append("</label>");
				al.add(sb2.toString());
			}

			if ((Boolean) compParameter.getBeanProperty("showSupportOpposition")) {
				StringBuilder sb2 = new StringBuilder();
				sb2.append("<a onclick=\"$Actions['ajaxRemarkSupport']('itemId=");
				sb2.append(item.getId()).append("');\">");
				sb2.append(LocaleI18n.getMessage("remark.5"));
				sb2.append("</a><label id='remark_support_").append(item.getId()).append("'>");
				if (item.getSupport() > 0) {
					sb2.append("(").append(item.getSupport()).append(")");
				}
				sb2.append("</label>");
				al.add(sb2.toString());
				sb2 = new StringBuilder();
				sb2.append("<a onclick=\"$Actions['ajaxRemarkOpposition']('itemId=");
				sb2.append(item.getId()).append("');\">");
				sb2.append(LocaleI18n.getMessage("remark.6"));
				sb2.append("</a><label id='remark_opposition_").append(item.getId()).append("'>");
				if (item.getOpposition() > 0) {
					sb2.append("(").append(item.getOpposition()).append(")");
				}
				sb2.append("</label>");
				al.add(sb2.toString());
			}

			sb.append(StringUtils.join(al, HTMLBuilder.SEP));
			if (remarkHandle instanceof NewsRemarkHandle) {
				sb.append(HTMLBuilder.SEP);
				sb.append(ItSiteUtil.buildComplaint(compParameter, EFunctionModule.news_remark, item.getId(), null));
			} else if (remarkHandle instanceof BlogRemarkHandle) {
				sb.append(HTMLBuilder.SEP);
				sb.append(ItSiteUtil.buildComplaint(compParameter, EFunctionModule.blog_remark, item.getId(), null));
			}
			sb.append("</div>");
			if (items2.size() > 0) {
				sb.append("<div class='d3'>");
				sb.append(itemsHtml(compParameter, items2));
				sb.append("</div>");
			}
			sb.append("</div>");
		}
		return sb.toString();
	}

	private static boolean isRemarkEdit(final IAccount account, final RemarkItem item) {
		final boolean edit = account.getId().equals2(item.getUserId());
		/* 一天内允许编辑 */
		final Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		return edit && item.getCreateDate().after(cal.getTime());
	}
}
