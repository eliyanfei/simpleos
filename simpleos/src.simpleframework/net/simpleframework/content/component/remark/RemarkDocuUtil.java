package net.simpleframework.content.component.remark;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.itsite.document.docu.DocuBean;
import net.itsite.document.docu.DocuRemark;
import net.itsite.document.docu.DocuUtils;
import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.organization.account.AccountSession;
import net.simpleframework.organization.account.IAccount;
import net.simpleframework.util.DateUtils;
import net.simpleframework.util.HTMLBuilder;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.component.ui.pager.PagerUtils;

public class RemarkDocuUtil {
	public static String itemsHtml(final PageRequestResponse requestResponse) {
		return itemsHtml(requestResponse, PagerUtils.getPagerList(requestResponse.request));
	}

	private static boolean isRemarkEdit(final IAccount account, final RemarkItem item) {
		final boolean edit = account.getId().equals2(item.getUserId());
		/* 一天内允许编辑 */
		final Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		return edit && item.getCreateDate().after(cal.getTime());
	}

	public static String itemsHtml(final PageRequestResponse requestResponse, final List<?> items) {
		if (items == null) {
			return "";
		}
		final StringBuilder sb = new StringBuilder();
		for (final Object object : items) {
			final RemarkItem item = (RemarkItem) object;
			sb.append("<div class='item'>");
			sb.append("<div class='d1'>");
			sb.append("<span class=\"ut\">").append(StringUtils.text(item.getUserText(), LocaleI18n.getMessage("remark.3")));
			sb.append(" (").append(item.getIp()).append(")</span>, ");
			sb.append("<span class=\"cd\">").append(DateUtils.getRelativeDate(item.getCreateDate()));
			sb.append("</span>");
			final DocuBean docuBean = DocuUtils.applicationModule.getBean(DocuBean.class, item.getDocumentId());
			if (docuBean != null) {
				sb.append(", <span>《");
				sb.append("<a target=\"_blank\"");
				sb.append(" href=\"" + DocuUtils.applicationModule.getViewUrl(docuBean.getId())).append("#idRemarks\"");
				sb.append(">").append(docuBean.getTitle()).append("</a>");
				sb.append("》</span>");
			}
			sb.append("</div>");
			sb.append("<div class='inherit_c wrap_text'>").append(item.getContent()).append("</div>");
			sb.append("<div class='d2'>");

			final ArrayList<String> al = new ArrayList<String>();
			final IAccount account = AccountSession.getLogin(requestResponse.getSession());
			if (account != null) {
				if (isRemarkEdit(account, item)) {
					final StringBuilder sb2 = new StringBuilder();
					sb2.append("<a onclick=\"__remark_window(this, 'itemId=");
					sb2.append(item.getId()).append("');\">");
					sb2.append(LocaleI18n.getMessage("remark.7"));
					sb2.append("</a>");
					al.add(sb2.toString());
				}
				if (true) {
					final StringBuilder sb2 = new StringBuilder();
					sb2.append("<a onclick=\"$Actions['ajaxRemarkDelete']('itemId=");
					sb2.append(item.getId()).append("');\">");
					sb2.append(LocaleI18n.getMessage("remark.8"));
					sb2.append("</a>");
					al.add(sb2.toString());
				}
			}

			final IDataObjectQuery<DocuRemark> qs = queryRemarkList(item.getDocumentId(), item.getId());
			final List<RemarkItem> items2 = new ArrayList<RemarkItem>();
			RemarkItem item2;
			while ((item2 = qs.next()) != null) {
				items2.add(item2);
			}

			if (true) {
				final StringBuilder sb2 = new StringBuilder();
				sb2.append("<a onclick=\"__remark_window(this, 'parentId=");
				sb2.append(item.getId()).append("&documentId=").append(docuBean == null ? 0 : docuBean.getId()).append("');\">");
				sb2.append(LocaleI18n.getMessage("remark.4"));
				sb2.append("</a><label>");
				if (items2.size() > 0) {
					sb2.append("(").append(items2.size()).append(")");
				}
				sb2.append("</label>");
				al.add(sb2.toString());
			}

			if (true) {
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
			sb.append("</div>");
			if (items2.size() > 0) {
				sb.append("<div class='d3'>");
				sb.append(itemsHtml(requestResponse, items2));
				sb.append("</div>");
			}
			sb.append("</div>");
		}
		return sb.toString();
	}

	private static IDataObjectQuery<DocuRemark> queryRemarkList(final Object documentId, final Object parentId) {
		final StringBuffer buf = new StringBuffer();
		final List<Object> ol = new ArrayList<Object>();
		buf.append("documentId=? and parentId=?");
		ol.add(documentId);
		ol.add(parentId);
		final ITableEntityManager tMgr = DocuUtils.applicationModule.getDataObjectManager(DocuRemark.class);
		return tMgr.query(new ExpressionValue(buf.toString(), ol.toArray()), DocuRemark.class);
	}
}
