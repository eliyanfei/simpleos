package net.simpleframework.content.component.newspager;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.simpleframework.ado.DataObjectManagerUtils;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.ado.db.SQLValue;
import net.simpleframework.ado.lucene.AbstractLuceneManager;
import net.simpleframework.applets.attention.AttentionUtils;
import net.simpleframework.applets.tag.TagUtils;
import net.simpleframework.content.ContentUtils;
import net.simpleframework.content.component.remark.RemarkItem;
import net.simpleframework.core.IApplicationModule;
import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.core.id.ID;
import net.simpleframework.organization.OrgUtils;
import net.simpleframework.organization.account.AccountSession;
import net.simpleframework.organization.account.IAccount;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.EFunctionModule;
import net.simpleframework.web.WebUtils;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.component.AbstractComponentRegistry;
import net.simpleframework.web.page.component.ComponentParameter;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class NewsPagerUtils {
	public static final String BEAN_ID = "news_@bid";

	public static ComponentParameter getComponentParameter(final PageRequestResponse requestResponse) {
		return ComponentParameter.get(requestResponse, BEAN_ID);
	}

	public static ComponentParameter getComponentParameter(final HttpServletRequest request, final HttpServletResponse response) {
		return ComponentParameter.get(request, response, BEAN_ID);
	}

	public static String getNewsContent(final ComponentParameter compParameter, final NewsBean news) {
		WebUtils.updateViews(compParameter.getSession(),
				((INewsPagerHandle) compParameter.getComponentHandle()).getTableEntityManager(compParameter), news);
		return news.getContent();
	}

	public static String getNewsContentUrl(final ComponentParameter compParameter, final NewsBean news) {
		final StringBuilder sb = new StringBuilder();
		sb.append("<a class=\"a2\" style=\"margin-left: 5px;\"");
		if ((Boolean) compParameter.getBeanProperty("openBlank")) {
			sb.append(" target=\"_blank\"");
		}
		sb.append(" href=\"").append(((INewsPagerHandle) compParameter.getComponentHandle()).getViewUrl(compParameter, news)).append("\">");
		sb.append(LocaleI18n.getMessage("NewsPagerUtils.4")).append("</a>");
		return sb.toString();
	}

	public static IDataObjectQuery<?> queryRelatedNews(final ComponentParameter compParameter) throws IOException {
		final INewsPagerHandle npHandle = (INewsPagerHandle) compParameter.getComponentHandle();
		final NewsBean news = npHandle.getEntityBeanByRequest(compParameter);
		if (news == null) {
			return null;
		}
		final AbstractLuceneManager nlmgr = npHandle.createLuceneManager(compParameter);
		String qt = StringUtils.blank(news.getKeywords());
		if (qt.length() < AbstractLuceneManager.QUERY_MIN_LENGTH) {
			qt += " " + news.getTopic();
		}
		qt = qt.trim();
		if (qt.length() < AbstractLuceneManager.QUERY_MIN_LENGTH) {
			qt += " " + ContentUtils.getShortContent(news, 50, false);
		}
		return nlmgr.getLuceneQuery(StringUtils.join(nlmgr.getQueryTokens(qt), " "));
	}

	static boolean isNewsEdit(final ComponentParameter compParameter, NewsBean news) {
		final IAccount login = AccountSession.getLogin(compParameter.getSession());
		if (login == null) {
			return false;
		}
		final INewsPagerHandle nHandle = (INewsPagerHandle) compParameter.getComponentHandle();
		if (news == null) {
			news = nHandle.getEntityBeanByRequest(compParameter);
		}
		if (news == null) {
			return false;
		}
		return login.getId().equals2(news.getUserId()) || OrgUtils.isMember(nHandle.getManager(compParameter), login.user());
	}

	public static void doNewsStatRebuild(final IApplicationModule module, final EFunctionModule attentionType) {
		final ITableEntityManager news_mgr = DataObjectManagerUtils.getTableEntityManager(module, NewsBean.class);
		final ITableEntityManager news_remark = DataObjectManagerUtils.getTableEntityManager(module, RemarkItem.class);
		final String news_name = news_mgr.getTablename();

		// remark rebuild
		final StringBuilder sql = new StringBuilder();
		if (news_remark != null) {
			final String news_remark_name = news_remark.getTablename();
			sql.append("update ").append(news_name).append(" t set remarks=(select count(id) from ");
			sql.append(news_remark_name).append(" where documentid=t.id)");
			news_mgr.execute(new SQLValue(sql.toString()));
		}
		sql.setLength(0);
		sql.append("update ").append(news_name).append(" t set attentions=(select count(id) from ");
		sql.append(AttentionUtils.getTableEntityManager().getTablename());
		sql.append(" where attentionid=t.id and vtype=").append(attentionType).append(")");
		news_mgr.reset();

		TagUtils.doTagsRebuild(EFunctionModule.news, ID.zero);
	}

	public static String getHomePath() {
		return AbstractComponentRegistry.getRegistry(NewsPagerRegistry.newsPager).getResourceHomePath();
	}

	public static String getCssPath(final PageRequestResponse requestResponse) {
		return AbstractComponentRegistry.getRegistry(NewsPagerRegistry.newsPager).getCssResourceHomePath(requestResponse);
	}

	public static String xmlNewsTemplate() {
		return getHomePath() + "/jsp/news_template.xml";
	}

	public static String cssNpedit(final PageRequestResponse requestResponse) {
		return getCssPath(requestResponse) + "/np_edit.css";
	}
}
