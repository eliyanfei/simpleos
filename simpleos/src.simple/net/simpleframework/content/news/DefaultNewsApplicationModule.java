package net.simpleframework.content.news;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.itsite.i.ISendMail;
import net.itsite.impl.AItSiteAppclicationModule;
import net.simpleframework.applets.attention.AttentionUtils;
import net.simpleframework.applets.attention.ISentCallback;
import net.simpleframework.applets.notification.MailMessageNotification;
import net.simpleframework.applets.notification.NotificationUtils;
import net.simpleframework.content.AbstractContentApplicationModule;
import net.simpleframework.content.EContentStatus;
import net.simpleframework.content.EContentType;
import net.simpleframework.content.component.newspager.INewsPagerHandle;
import net.simpleframework.content.component.newspager.NewsBean;
import net.simpleframework.content.component.newspager.NewsCatalog;
import net.simpleframework.content.component.newspager.NewsPagerBean;
import net.simpleframework.content.component.newspager.NewsPagerUtils;
import net.simpleframework.content.component.remark.RemarkItem;
import net.simpleframework.core.ExecutorRunnable;
import net.simpleframework.core.IInitializer;
import net.simpleframework.core.ITaskExecutorAware;
import net.simpleframework.core.ado.db.Table;
import net.simpleframework.organization.IUser;
import net.simpleframework.organization.account.AccountSession;
import net.simpleframework.organization.account.IAccount;
import net.simpleframework.organization.component.login.LoginUtils;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.util.HTMLBuilder;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.EFunctionModule;
import net.simpleframework.web.WebUtils;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.PageUtils;
import net.simpleframework.web.page.component.AbstractComponentBean;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.portal.module.PortalModuleRegistryFactory;
import net.simpleframework.web.page.component.ui.tabs.TabHref;
import net.simpleframework.web.page.component.ui.tabs.TabsUtils;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class DefaultNewsApplicationModule extends AbstractContentApplicationModule implements INewsApplicationModule {
	@Override
	protected void putTables(final Map<Class<?>, Table> tables) {
		tables.put(News.class, new Table("simple_app_news"));
		tables.put(NewsRemark.class, new Table("simple_app_news_remark"));
		tables.put(NewsExtCatalog.class, new Table("simple_app_news_catalog"));
	}

	@Override
	public String getViewUrl(final ComponentParameter compParameter, final NewsBean news) {
		return null;
	}

	@Override
	public String getCatalogUrl(final PageRequestResponse requestResponse, final Object catalog) {
		return WebUtils.addParameters(getApplicationUrl(requestResponse),
				"catalog=" + ((catalog instanceof NewsCatalog) ? ((NewsCatalog) catalog).getId() : catalog));
	}

	@Override
	public String getApplicationText() {
		return StringUtils.text(super.getApplicationText(), LocaleI18n.getMessage("DefaultNewsApplicationModule.0"));
	}

	@Override
	public String getManager(final Object... params) {
		return sj_news_manager;
	}

	@Override
	public List<NewsCatalog> listNewsCatalog(final PageRequestResponse requestResponse) {
		return NewsUtils.newsCatalogs;
	}

	static final String sj_news_manager = "news_manager";

	static final String deployName = "content/news";

	@Override
	public void init(final IInitializer initializer) {
		super.init(initializer);
		doInit(NewsUtils.class, deployName);

		PortalModuleRegistryFactory.regist(NewsPortalModule.class, "news", getApplicationText(), LocaleI18n.getMessage("NewsUtils.0"),
				NewsUtils.deployPath + "images/news.png", LocaleI18n.getMessage("NewsUtils.1"));

		createManagerJob(sj_news_manager, LocaleI18n.getMessage("NewsUtils.news_manager"));

		NewsUtils.initNewsCatalog();
	}

	@Override
	public String getTemplatePage(final ComponentParameter compParameter) {
		final StringBuilder sb = new StringBuilder();
		sb.append(NewsPagerUtils.getHomePath()).append("/jsp/news_template_");
		final News news = ((INewsPagerHandle) compParameter.getComponentHandle()).getEntityBeanByRequest(compParameter);
		sb.append(news != null ? news.getViewTemplate().ordinal() : 0).append(".jsp");
		return sb.toString();
	}

	@Override
	public NewsPagerBean getComponentBean(final PageRequestResponse requestResponse) {
		return (NewsPagerBean) AbstractComponentBean.getComponentBeanByName(requestResponse, NewsUtils.deployPath + "jsp/news.xml",
				"__news_app_pager");
	}

	@Override
	public Class<?> getEntityBeanClass() {
		return News.class;
	}

	@Override
	public String getPagerHandleClass() {
		return StringUtils.text(super.getPagerHandleClass(), NewsPagerHandle.class.getName());
	}

	@Override
	public String tabs(final PageRequestResponse requestResponse) {
		final List<TabHref> tabHrefs = new ArrayList<TabHref>();
		TabHref tabHref;

		final String applicationUrl = getApplicationUrl(requestResponse);
		tabHrefs.add(new TabHref("#(ContentUtils.all)", applicationUrl));

		final List<?> catalogs = listNewsCatalog(requestResponse);
		if (catalogs != null && catalogs.size() > 0) {
			tabHref = new TabHref("#(ContentUtils.0)", getCatalogUrl(requestResponse, catalogs.get(0)));
			for (int i = 1; i < catalogs.size(); i++) {
				final NewsCatalog catalog = (NewsCatalog) catalogs.get(i);
				tabHref.getChildren().add(new TabHref(null, getCatalogUrl(requestResponse, catalog)));
			}
			tabHrefs.add(tabHref);
		}

		tabHref = new TabHref("#(ContentUtils.time)", WebUtils.addParameters(applicationUrl, "time=day"));
		for (final String str : new String[] { "day2", "week", "month", "month3" }) {
			tabHref.getChildren().add(new TabHref(null, WebUtils.addParameters(applicationUrl, "time=" + str)));
		}
		tabHrefs.add(tabHref);

		//		if (IWebApplicationModule.Utils.isManager(requestResponse, this)) {
		//			tabHref = new TabHref("#(ContentUtils.status)", WebUtils.addParameters(applicationUrl, "status=publish"));
		//			for (final EContentStatus s : EContentStatus.values()) {
		//				tabHref.getChildren().add(new TabHref(null, WebUtils.addParameters(applicationUrl, "status=" + s.name())));
		//			}
		//			tabHrefs.add(tabHref);
		//		}

		tabHref = new TabHref(EContentType.recommended.toString(), WebUtils.addParameters(applicationUrl, "t=recommended"));
		tabHrefs.add(tabHref);

		final IAccount login = AccountSession.getLogin(requestResponse.getSession());
		tabHref = new TabHref("我的投递", login == null ? LoginUtils.getLocationPath() : WebUtils.addParameters(applicationUrl, "t=my"));
		tabHrefs.add(tabHref);

		return TabsUtils.tabs(requestResponse, tabHrefs.toArray(new TabHref[tabHrefs.size()]));
	}

	@Override
	public String tabs2(final PageRequestResponse requestResponse) {
		final StringBuilder sb = new StringBuilder();
		final String catalogId = requestResponse.getRequestParameter(getCatalogIdName(requestResponse));

		if (StringUtils.hasText(catalogId)) {
			final List<?> catalogs = listNewsCatalog(requestResponse);
			if (catalogs != null) {
				int i = 0;
				for (final Object obj : catalogs) {
					final NewsCatalog catalog = (NewsCatalog) obj;
					if (i++ > 0) {
						sb.append(HTMLBuilder.SEP);
					}
					sb.append("<a");
					if (catalog.getId().equals2(catalogId)) {
						sb.append(" class=\"a2 nav_arrow\"");
					}
					sb.append(" href=\"").append(getCatalogUrl(requestResponse, catalog)).append("\">").append(catalog.getText()).append("</a>");
				}
			}
		}
		final String applicationUrl = getApplicationUrl(requestResponse);
		final String time = requestResponse.getRequestParameter("time");
		if (StringUtils.hasText(time)) {
			int i = 0;
			for (final String str : new String[] { "day", "day2", "week", "month", "month3" }) {
				if (i++ > 0) {
					sb.append(HTMLBuilder.SEP);
				}
				sb.append("<a");
				if (str.equals(time)) {
					sb.append(" class=\"a2 nav_arrow\"");
				}
				sb.append(" href=\"").append(WebUtils.addParameters(applicationUrl, "time=" + str)).append("\">").append("#(ContentUtils.time.")
						.append(i).append(")").append("</a>");
			}
		}
		final String status = requestResponse.getRequestParameter("status");
		if (StringUtils.hasText(status)) {
			int i = 0;
			for (final EContentStatus s : EContentStatus.values()) {
				if (i++ > 0) {
					sb.append(HTMLBuilder.SEP);
				}
				sb.append("<a");
				if (s.name().equals(status)) {
					sb.append(" class=\"a2 nav_arrow\"");
				}
				sb.append(" href=\"").append(WebUtils.addParameters(applicationUrl, "status=" + s.name())).append("\">").append(s).append("</a>");
			}
		}
		//		if (sb.length() == 0) {
		//			sb.append(NewsUtils.tabs13(requestResponse));
		//		}
		return sb.toString();
	}

	protected void doAttentionMail(final ComponentParameter compParameter, final ISentCallback attentionMail) {
		((ITaskExecutorAware) PageUtils.pageContext.getApplication()).getTaskExecutor().execute(new ExecutorRunnable() {
			@Override
			public void task() {
				AttentionUtils.sentMessage(compParameter, EFunctionModule.blog, attentionMail);
			}
		});
	}

	protected void doAttentionMail(final ComponentParameter compParameter, final ISendMail sendMail) {
		((ITaskExecutorAware) getApplication()).getTaskExecutor().execute(new ExecutorRunnable() {
			@Override
			public void task() {
				final MailMessageNotification mailMessage = new MailMessageNotification();
				mailMessage.setHtmlContent(true);
				final IUser user = sendMail.getUser();
				if (sendMail.isSent(user)) {
					mailMessage.getTo().add(user);
					mailMessage.setSubject(sendMail.getSubject(user));
					mailMessage.setTextBody(sendMail.getBody(user));
					NotificationUtils.sendMessage(mailMessage);
				}
			}
		});
	}

	@Override
	public void doAttentionSent(final ComponentParameter compParameter, final RemarkItem remark, Class<?> classBean) {
		final News news = NewsUtils.getTableEntityManager(News.class).queryForObjectById(remark.getDocumentId(), News.class);

		doAttentionMail(compParameter, new AttentionContentMail(compParameter, news) {
			@Override
			public boolean isSent(final IUser toUser) {
				return !toUser.getId().equals2(remark.getUserId());
			}

			@Override
			public String getSubject(final IUser toUser) {
				final StringBuilder sb = new StringBuilder();
				sb.append("\"").append(remark.getUserText()).append("\"").append(LocaleI18n.getMessage("DefaultNewsPagerHandle.1"));
				return sb.toString();
			}

			@Override
			public String getBody(final IUser toUser) {
				final Map<String, Object> variable = new HashMap<String, Object>();
				variable.put("topicHref", linkUrl(getViewUrl(compParameter, news)));
				variable.put("toUser", toUser);
				variable.put("createUser", newsBean.getUserText());
				variable.put("createDate", ConvertUtils.toDateString(newsBean.getCreateDate()));
				variable.put("fromUser", remark.getUserText());
				variable.put("topic", newsBean.getTopic());
				variable.put("content", remark.getContent());
				return getFromTemplate(variable, AItSiteAppclicationModule.class);
			}
		});
	}
}
