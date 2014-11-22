package net.simpleframework.content.news;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.ado.db.SQLValue;
import net.simpleframework.applets.attention.AttentionUtils;
import net.simpleframework.applets.tag.ITagApplicationModule;
import net.simpleframework.applets.tag.TagBean;
import net.simpleframework.applets.tag.TagUtils;
import net.simpleframework.content.ContentUtils;
import net.simpleframework.content.EContentStatus;
import net.simpleframework.content.EContentType;
import net.simpleframework.content.component.newspager.DefaultNewsPagerHandle;
import net.simpleframework.content.component.newspager.NewsBean;
import net.simpleframework.content.component.newspager.NewsCatalog;
import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.core.bean.IDataObjectBean;
import net.simpleframework.core.bean.IIdBeanAware;
import net.simpleframework.my.space.MySpaceUtils;
import net.simpleframework.organization.IJob;
import net.simpleframework.organization.OrgUtils;
import net.simpleframework.organization.account.AccountContext;
import net.simpleframework.organization.account.AccountSession;
import net.simpleframework.organization.account.IAccount;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.util.DateUtils;
import net.simpleframework.util.HTMLBuilder;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.EFunctionModule;
import net.simpleframework.web.IWebApplicationModule;
import net.simpleframework.web.WebUtils;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.pager.AbstractTablePagerData;
import net.simpleframework.web.page.component.ui.pager.EPagerPosition;
import net.simpleframework.web.page.component.ui.pager.TablePagerColumn;
import net.simpleos.SimpleosUtil;
import net.simpleos.utils.StringsUtils;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class NewsPagerHandle extends DefaultNewsPagerHandle {

	@Override
	public Object getBeanProperty(final ComponentParameter compParameter, final String beanProperty) {
		if ("showCheckbox".equals(beanProperty)) {
			return "false";
		} else {
			if ("jobView".equals(beanProperty)) {
				return IJob.sj_anonymous;
			} else if (beanProperty != null && beanProperty.startsWith("job")) {
				return IJob.sj_account_normal;
			}
		}
		return super.getBeanProperty(compParameter, beanProperty);
	}

	@Override
	public Class<? extends IIdBeanAware> getEntityBeanClass() {
		return News.class;
	}

	@Override
	public String getViewUrl(final ComponentParameter compParameter, final NewsBean news) {
		return getApplicationModule().getViewUrl(compParameter, news);
	}

	@Override
	public <T extends IDataObjectBean> void doBeforeAdd(final ComponentParameter compParameter, final ITableEntityManager temgr, final T t,
			final Map<String, Object> data, final Class<T> beanClazz) {
		super.doBeforeAdd(compParameter, temgr, t, data, beanClazz);
		final News news = (News) t;
		if (IWebApplicationModule.Utils.isManager(compParameter, getApplicationModule())) {
			news.setStatus(EContentStatus.publish);
		}
	}

	@Override
	public <T extends IDataObjectBean> void doEditCallback(final ComponentParameter compParameter, final ITableEntityManager temgr, final T t,
			final Map<String, Object> data, final Class<T> beanClazz) {
		super.doEditCallback(compParameter, temgr, t, data, beanClazz);

		if (data.get("status") == EContentStatus.publish) {
			final News news = (News) t;
			AccountContext.update(OrgUtils.am().queryForObjectById(news.getUserId()), "news_statusPublish", news.getId());
		}
	}

	@Override
	public <T extends IDataObjectBean> void doAddCallback(final ComponentParameter compParameter, final ITableEntityManager temgr, final T t,
			final Map<String, Object> data, final Class<T> beanClazz) {
		super.doAddCallback(compParameter, temgr, t, data, beanClazz);
		final News news = (News) t;
		final IAccount account = OrgUtils.am().queryForObjectById(news.getUserId());
		if (news.getStatus() == EContentStatus.publish) {
			AccountContext.update(account, "news_statusPublish", news.getId());
		}
		MySpaceUtils.addSapceLog(compParameter, null, EFunctionModule.news, news.getId());
	}

	@Override
	public String getNavigateHTML(final ComponentParameter compParameter) {
		final StringBuilder sb = new StringBuilder();
		sb.append("<a href=\"").append(getApplicationModule().getApplicationUrl(compParameter)).append("\">");
		sb.append(getApplicationModule().getApplicationText()).append("</a>");
		final NewsBean news = getEntityBeanByRequest(compParameter);
		if (news != null) {
			final NewsCatalog catalog = getCatalogById(compParameter, news.getCatalogId());
			if (catalog != null) {
				sb.append(HTMLBuilder.NAV).append("<a href=\"").append(getApplicationModule().getCatalogUrl(compParameter, catalog)).append("\">")
						.append(catalog.getText()).append("</a>");
			}
			sb.append(HTMLBuilder.NAV).append(LocaleI18n.getMessage("DefaultNewsPagerHandle.0"));
		}
		final String time = compParameter.getRequestParameter("time");
		if (StringUtils.hasText(time)) {
			sb.append(HTMLBuilder.NAV);
			if ("day".equals(time)) {
				sb.append(LocaleI18n.getMessage("ContentUtils.time.1"));
			} else if ("day2".equals(time)) {
				sb.append(LocaleI18n.getMessage("ContentUtils.time.2"));
			} else if ("week".equals(time)) {
				sb.append(LocaleI18n.getMessage("ContentUtils.time.3"));
			} else if ("month".equals(time)) {
				sb.append(LocaleI18n.getMessage("ContentUtils.time.4"));
			} else if ("month3".equals(time)) {
				sb.append(LocaleI18n.getMessage("ContentUtils.time.5"));
			}
		}
		final String status = compParameter.getRequestParameter("status");
		if (StringUtils.hasText(status)) {
			sb.append(HTMLBuilder.NAV);
			if (status.equals(EContentStatus.edit.name())) {
				sb.append(EContentStatus.edit);
			} else if (status.equals(EContentStatus.audit.name())) {
				sb.append(EContentStatus.audit);
			} else if (status.equals(EContentStatus.publish.name())) {
				sb.append(EContentStatus.publish);
			} else if (status.equals(EContentStatus.lock.name())) {
				sb.append(EContentStatus.lock);
			} else if (status.equals(EContentStatus.delete.name())) {
				sb.append(EContentStatus.delete);
			}
		}

		final int catalog = ConvertUtils.toInt(getCatalogId(compParameter), -1);
		final List<NewsCatalog> catalogs;
		if (catalog > -1 && (catalogs = listNewsCatalog(compParameter)) != null) {
			if (catalog < catalogs.size()) {
				sb.append(HTMLBuilder.NAV);
				sb.append(catalogs.get(catalog).getText());
			}
		}

		final TagBean tagBean = TagUtils.getTagBeanById(compParameter.getRequestParameter(ITagApplicationModule._TAG_ID));
		if (tagBean != null) {
			sb.append(HTMLBuilder.NAV);
			sb.append(tagBean.getTagText());
			TagUtils.updateViews(compParameter, tagBean);
		}
		wrapNavImage(compParameter, sb);
		return sb.toString();
	}

	@Override
	public Map<String, Object> getFormParameters(final ComponentParameter compParameter) {
		final Map<String, Object> parameters = super.getFormParameters(compParameter);
		putParameter(compParameter, parameters, "time");
		putParameter(compParameter, parameters, "c");
		putParameter(compParameter, parameters, "status");
		putParameter(compParameter, parameters, "p");
		putParameter(compParameter, parameters, "od");
		putParameter(compParameter, parameters, "t");
		putParameter(compParameter, parameters, ITagApplicationModule._TAG_ID);
		return parameters;
	}

	@Override
	public INewsApplicationModule getApplicationModule() {
		return NewsUtils.applicationModule;
	}

	@Override
	public IDataObjectQuery<?> createDataObjectQuery(final ComponentParameter compParameter) {
		final ITableEntityManager news_mgr = getTableEntityManager(compParameter);
		final StringBuilder sql = new StringBuilder();
		final ArrayList<Object> al = new ArrayList<Object>();
		final String tag = compParameter.getRequestParameter(ITagApplicationModule._TAG_ID);
		if (StringsUtils.isNotBlank(tag)) {
			appendTagsSQL(compParameter, sql, al, tag);
			sql.append(" where ");
			initStatusSQL(compParameter, sql, al, "a");
			sql.append(" order by  a.createdate desc");
			return news_mgr.query(new SQLValue(sql.toString(), al.toArray()), getEntityBeanClass());
		}

		final String t = compParameter.getRequestParameter("t");
		if ("my".equals(t)) {
			return news_mgr.query(AttentionUtils.attentionSQLValue(compParameter, getFunctionModule()), getEntityBeanClass());
		}
		return news_mgr.query(createSqlValue(compParameter), getEntityBeanClass());
	}

	@Override
	protected SQLValue createSqlValue(ComponentParameter compParameter) {
		final StringBuilder sql = new StringBuilder();
		final ArrayList<Object> al = new ArrayList<Object>();
		initStatusSQL(compParameter, sql, al, null);
		String limitWhere = sql.toString();
		sql.setLength(0);
		sql.append("select * from simple_app_news ");
		sql.append("where ");
		boolean aloneLimit = false;
		if (compParameter.getStart() > SimpleosUtil.aloneLimit && dataSplit()) {
			aloneLimit = true;
			sql.append("id <= (select id from simple_app_news where " + limitWhere + " order by createdate desc limit ")
					.append(compParameter.getStart()).append(",1) and ");
		}
		initStatusSQL(compParameter, sql, al, null);
		final String t = compParameter.getRequestParameter("t");
		final String time = compParameter.getRequestParameter("time");
		if (StringUtils.hasText(time)) {
			final Calendar cal = DateUtils.getTimeCalendar(time);
			sql.append(" and createdate>?");
			al.add(cal.getTime());
		}
		final int catalog = ConvertUtils.toInt(getCatalogId(compParameter), -1);
		final List<NewsCatalog> catalogs;
		if (catalog > -1 && (catalogs = listNewsCatalog(compParameter)) != null) {
			if (catalog < catalogs.size()) {
				sql.append(" and catalogid=?");
				al.add(catalogs.get(catalog).getId());
			}
		}
		if ("recommended".equals(t)) {
			sql.append(" and ttype=?");
			al.add(EContentType.recommended);
		}
		sql.append(" order by createdate desc");
		return new SQLValue(sql.toString(), al.toArray()).setAloneLimit(aloneLimit);
	}

	@Override
	protected boolean dataSplit() {
		return true;
	}

	private void initStatusSQL(final ComponentParameter compParameter, final StringBuilder sql, final ArrayList<Object> al, final String tbl) {
		final IAccount account = AccountSession.getLogin(compParameter.getSession());
		if (account == null) {
			if (tbl != null) {
				sql.append(tbl).append(".");
			}
			sql.append("status=" + EContentStatus.publish.ordinal());
		} else {
			if (IWebApplicationModule.Utils.isManager(compParameter, getApplicationModule())) {
				sql.append("1=1");
				final String status = compParameter.getRequestParameter("status");
				if (StringUtils.hasText(status)) {
					sql.append(" and ");
					if (tbl != null) {
						sql.append(tbl).append(".");
					}
					sql.append("status=" + EContentStatus.valueOf(status).ordinal());
				}
			} else {
				final String status = compParameter.getRequestParameter("status");
				sql.append("(");
				if (tbl != null) {
					sql.append(tbl).append(".");
				}
				if (StringUtils.hasText(status)) {
					sql.append("userid=" + account.getId() + " and ");
					if (tbl != null) {
						sql.append(tbl).append(".");
					}
					sql.append("status=" + EContentStatus.valueOf(status).ordinal());
				} else {
					if (tbl != null) {
						sql.append(tbl).append(".");
					}
					sql.append("userid=" + account.getId() + " or ");
					sql.append("status=" + EContentStatus.publish.ordinal());
				}
				sql.append(")");
			}
		}
	}

	public String getPagerUrl(ComponentParameter compParameter, EPagerPosition pagerPosition, int pageItems, Map<String, Integer> pageVar) {
		if (true || StringUtils.hasText(compParameter.getRequestParameter("c")) || StringUtils.hasText(compParameter.getRequestParameter("tagId"))
				|| StringUtils.hasText(compParameter.getRequestParameter("t")) || StringUtils.hasText(compParameter.getRequestParameter("od"))
				|| StringUtils.hasText(compParameter.getRequestParameter("time")) || StringUtils.hasText(compParameter.getRequestParameter("status"))
				|| pagerPosition == EPagerPosition.pageNumber) {
			return super.getPagerUrl(compParameter, pagerPosition, pageItems, pageVar);
		}
		if (StringUtils.hasText(compParameter.getRequestParameter("p"))) {
			final StringBuilder sb = new StringBuilder();
			sb.append("/news/p/");
			final int pageNumber = ConvertUtils.toInt(pageVar.get("pageNumber"), 0);
			final int currentPageNumber = ConvertUtils.toInt(pageVar.get("currentPageNumber"), 0);
			final int pageCount = ConvertUtils.toInt(pageVar.get("pageCount"), 0);
			if (pagerPosition == EPagerPosition.left2) {
				sb.append(1);
			} else if (pagerPosition == EPagerPosition.left) {
				sb.append(currentPageNumber > 1 ? (currentPageNumber - 1) : 1);
			} else if (pagerPosition == EPagerPosition.number) {
				sb.append(pageNumber);
			} else if (pagerPosition == EPagerPosition.right) {
				sb.append(currentPageNumber >= pageCount ? pageCount : currentPageNumber + 1);
			} else if (pagerPosition == EPagerPosition.right2) {
				sb.append(pageCount);
			} else if (pagerPosition == EPagerPosition.pageItems) {
				sb.append(1);
			}
			sb.append(".html");
			return sb.toString();
		}
		return super.getPagerUrl(compParameter, pagerPosition, pageItems, pageVar);
	}

	@Override
	public AbstractTablePagerData createTablePagerData(final ComponentParameter compParameter) {
		return new AppNewsTablePagerData(compParameter);
	}

	class AppNewsTablePagerData extends NewsTablePagerData {
		String c;

		public AppNewsTablePagerData(final ComponentParameter compParameter) {
			super(compParameter);
			c = WebUtils.toLocaleString(compParameter.getRequestParameter("c"));
		}

		@Override
		public Map<String, TablePagerColumn> getTablePagerColumns() {
			final Map<String, TablePagerColumn> columns = cloneTablePagerColumns();
			if (!IWebApplicationModule.Utils.isManager(compParameter, getApplicationModule())) {
			}
			columns.remove("action");
			columns.get("topic").setSort(false);
			columns.remove("status");
			columns.remove("createdate");
			columns.remove("author");
			columns.remove("remarks");
			columns.remove("views");
			return columns;
		}

		@Override
		protected Map<Object, Object> getRowData(final Object dataObject) {
			final NewsBean news = (NewsBean) dataObject;
			final Map<Object, Object> rowData = new HashMap<Object, Object>();
			rowData.put("topic", getTitle(news));
			rowData.put("remarks", wrapNum(news.getRemarks()));
			rowData.put("views", wrapNum(news.getViews()));
			rowData.put("action", ACTIONc);
			return rowData;
		}

		private String getTitle(final NewsBean news) {
			final StringBuilder sb = new StringBuilder();
			sb.append("<div class=\"lbl\">");
			final EContentStatus status = news.getStatus();
			if (status != EContentStatus.publish) {
				sb.append("<span style=\"margin-right: 6px;\" class=\"important-tip\">[ ").append(status).append(" ]</span>");
			}
			sb.append(SimpleosUtil.markContent(c, wrapOpenLink(compParameter, news)));
			sb.append(SimpleosUtil.buildTimeString(news.getCreateDate()));
			sb.append("<div class=\"nc\">");
			final String img = ContentUtils.getContentImage(compParameter, news, 64, 64);
			if (img != null) {
				sb.append(img);
			}
			sb.append(SimpleosUtil.markContent(c, ContentUtils.getShortContent(news, 200, true)));
			sb.append("<div class=\"nd\">");
			sb.append("<span class=\"num\">" + news.getRemarks() + "</span>&nbsp;评论");
			sb.append("<span style=\"margin: 0px 5px;\">|</span>");
			sb.append("<span class=\"num\">" + news.getViews() + "</span>&nbsp;阅读");
			sb.append("</div>");
			sb.append("</div>");
			sb.append("</div>");
			return sb.toString();
		}
	}

	@Override
	public String getCatalogIdName(final PageRequestResponse requestResponse) {
		return getApplicationModule().getCatalogIdName(requestResponse);
	}

	@Override
	public NewsCatalog getCatalogById(final ComponentParameter compParameter, final Object id) {
		final List<NewsCatalog> catalogs = listNewsCatalog(compParameter);
		if (catalogs != null) {
			for (final NewsCatalog catalog : catalogs) {
				if (catalog.getId().equals2(id)) {
					return catalog;
				}
			}
		}
		return null;
	}

	@Override
	public List<NewsCatalog> listNewsCatalog(final ComponentParameter compParameter) {
		final List<NewsCatalog> catalogs = getApplicationModule().listNewsCatalog(compParameter);
		if (catalogs != null) {
			return catalogs;
		}
		return super.listNewsCatalog(compParameter);
	}

	@Override
	public String getRemarkHandleClass(final ComponentParameter compParameter) {
		return NewsRemarkHandle.class.getName();
	}

	@Override
	public EFunctionModule getFunctionModule() {
		return EFunctionModule.news;
	}
}
