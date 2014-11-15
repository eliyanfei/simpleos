package net.simpleframework.content;

import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.ado.db.event.TableEntityAdapter;
import net.simpleframework.ado.lucene.AbstractLuceneManager;
import net.simpleframework.applets.attention.AttentionUtils;
import net.simpleframework.applets.attention.ISentCallback;
import net.simpleframework.applets.tag.TagRBean;
import net.simpleframework.applets.tag.TagUtils;
import net.simpleframework.core.AAttributeAware;
import net.simpleframework.core.ExecutorRunnable;
import net.simpleframework.core.IApplicationModule;
import net.simpleframework.core.IApplicationModuleAware;
import net.simpleframework.core.ITaskExecutorAware;
import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.core.ado.db.Table;
import net.simpleframework.core.bean.IDataObjectBean;
import net.simpleframework.core.id.ID;
import net.simpleframework.my.file.component.fileselect.FileSelectUtils;
import net.simpleframework.organization.IJob;
import net.simpleframework.organization.IUser;
import net.simpleframework.organization.account.AccountSession;
import net.simpleframework.organization.account.IAccount;
import net.simpleframework.organization.account.IGetAccountAware;
import net.simpleframework.util.AlgorithmUtils;
import net.simpleframework.util.BeanUtils;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.util.ImageUtils;
import net.simpleframework.util.IoUtils;
import net.simpleframework.util.script.ScriptEvalUtils;
import net.simpleframework.web.EFunctionModule;
import net.simpleframework.web.FilePathWrapper;
import net.simpleframework.web.IWebApplicationModule;
import net.simpleframework.web.WebApplicationConfig;
import net.simpleframework.web.WebUtils;
import net.simpleframework.web.page.AbstractUrlForward;
import net.simpleframework.web.page.IPageConstants;
import net.simpleframework.web.page.PageUtils;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.HandleException;
import net.simpleframework.web.page.component.ui.menu.MenuBean;
import net.simpleframework.web.page.component.ui.menu.MenuItem;
import net.simpleframework.web.page.component.ui.pager.db.AbstractDbTablePagerHandle;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class AbstractContentPagerHandle extends AbstractDbTablePagerHandle implements IContentPagerHandle {

	@Override
	public IDataObjectQuery<?> createDataObjectQuery(final ComponentParameter compParameter) {
		final ExpressionValue ev = getBeansSQL(compParameter);
		return getTableEntityManager(compParameter).query(
				new ExpressionValue(ev.getExpression() + " order by ttop desc, oorder desc", ev.getValues()), getEntityBeanClass());
	}

	protected void appendTagsSQL(final ComponentParameter compParameter, final StringBuilder sql, final ArrayList<Object> al, final Object tag) {
		sql.append("select a.* from ");
		sql.append(getTableEntityManager(compParameter).getTablename());
		sql.append(" a inner join ");
		sql.append(TagUtils.getTableEntityManager(TagRBean.class).getTablename());
		sql.append(" b on a.id = b.rid and b.tagid=?");
		al.add(tag);
	}

	protected ExpressionValue getBeansSQL(final ComponentParameter compParameter) {
		final Object catalogId = getCatalogId(compParameter);
		if (catalogId == null) {
			return new ExpressionValue(Table.nullExpr(getTableEntityManager(compParameter).getTable(), "catalogid"));
		} else {
			final ArrayList<Object> al = new ArrayList<Object>();
			final StringBuilder sql = new StringBuilder();
			sql.append("catalogid=?");
			al.add(catalogId);
			return new ExpressionValue(sql.toString(), al.toArray());
		}
	}

	@Override
	public <T extends IDataObjectBean> void doBeforeEdit(final ComponentParameter compParameter, final ITableEntityManager temgr, final T t,
			final Map<String, Object> data, final Class<T> beanClazz) {
		if (t instanceof AbstractContentBase) {
			((AbstractContentBase) t).updateLastUpdate(compParameter);
		}
	}

	@Override
	public AbstractLuceneManager createLuceneManager(final ComponentParameter compParameter) {
		return null;
	}

	@Override
	public IGetAccountAware getAccountAware() {
		return ContentUtils.getAccountAware();
	}

	@Override
	public String getNavigateHTML(final ComponentParameter compParameter) {
		return null;
	}

	@Override
	public String getActionsHTML(final ComponentParameter compParameter, final AbstractContentBase contentBase) {
		return null;
	}

	@Override
	public String getHtmlEditorToolbar(final ComponentParameter compParameter) {
		return null;
	}

	@Override
	public FilePathWrapper getFileCache(final ComponentParameter compParameter) {
		return new FilePathWrapper(compParameter.getServletContext(), IPageConstants.DATA_HOME + "/file-cache/");
	}

	@Override
	public boolean isNew(final ComponentParameter compParameter, final AbstractContent content) {
		final Calendar cal = Calendar.getInstance();
		cal.add(Calendar.HOUR, -12);
		return content.getCreateDate().after(cal.getTime());
	}

	protected List<MenuItem> doManagerHeaderMenu(final ComponentParameter compParameter, final MenuBean menuBean,
			final IApplicationModuleAware aware, final String managerAction) {
		final IApplicationModule module = aware.getApplicationModule();
		if (module instanceof IWebApplicationModule && IWebApplicationModule.Utils.isManager(compParameter, (IWebApplicationModule) module)) {
			final List<MenuItem> headerMenu = new ArrayList<MenuItem>(super.getHeaderMenu(compParameter, menuBean));
			final MenuItem sep = new MenuItem(menuBean);
			sep.setTitle("-");
			headerMenu.add(sep);
			final MenuItem item = new MenuItem(menuBean);
			item.setTitle("#(manager_tools.0)");
			headerMenu.add(item);
			item.setJsSelectCallback("$Actions['" + managerAction + "']();");
			return headerMenu;
		}
		return super.getHeaderMenu(compParameter, menuBean);
	}

	protected String doDownloadContent(final ComponentParameter compParameter, final String content, final ID contentId,
			final Map<String, Object> data) {
		final Document doc = Jsoup.parse(content);
		final Elements atts = doc.select("a[href^=" + FileSelectUtils.DOWNLOAD_FLAG + "]");
		if (atts.isEmpty()) {
			return content;
		}
		for (int i = 0; i < atts.size(); i++) {
			final Element att = atts.get(i);
			String dl = att.attr("href").substring(FileSelectUtils.DOWNLOAD_FLAG.length());
			att.removeAttr("href");
			final int p = dl.indexOf("?");
			final Map<String, Object> params = WebUtils.toQueryParams(dl.substring(p + 1));
			if (ConvertUtils.toBoolean(data.get("att1"), false)) {
				params.put("job", IJob.sj_account_normal);

				final int points = ConvertUtils.toInt(data.get("att3"), 0);
				if (points > 0) {
					params.put("points", points);
					params.put("posttext", contentId);
				}
			} else {
				params.put("job", IJob.sj_anonymous);
			}

			final String nDL = AlgorithmUtils.base64Encode((dl.substring(0, p + 1) + WebUtils.toQueryString(params)).getBytes());
			att.attr("onclick", "$Actions['__my_folderfile_ajax_download']('dl=" + nDL + "');");

			if (ConvertUtils.toBoolean(data.get("att2"), false)) {
				dl = compParameter.wrapContextPath(dl);
				try {
					final URL url = new URL(AbstractUrlForward.getLocalhostUrl(compParameter.request) + dl);
					if (ImageUtils.isImage(url)) {
						final Element img = doc.createElement("img");
						img.attr("src", WebUtils.addParameters(dl, "loc=true")).attr("style",
								"padding: 1px; border: 1px solid #999; max-width: 600px;");
						att.replaceWith(img);
					}
					// else if (TikaUtils.isFlash(url)) {
					// final Element object = doc.createElement("object");
					// object.attr("classid",
					// "clsid:d27cdb6e-ae6d-11cf-96b8-444553540000");
					// object.attr("codebase",
					// "http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,40,0");
					// Element param = object.appendElement("param");
					// param.attr("name", "quality");
					// param.attr("value", "high");
					// param = object.appendElement("param");
					// param.attr("name", "movie");
					// param.attr("value", dl);
					// Element embed = object.appendElement("embed");
					// embed.attr("pluginspage",
					// "http://www.macromedia.com/go/getflashplayer");
					// embed.attr("quality", "high");
					// embed.attr("type", "application/x-shockwave-flash");
					// embed.attr("src", dl);
					// att.replaceWith(object);
					// }
				} catch (final Exception e) {
				}
			}
		}
		return doc.body().html();
	}

	@Override
	public EFunctionModule getFunctionModule() {
		return null;
	}

	@Override
	public void contentAttention(final ComponentParameter compParameter, final IAttentionsBeanAware attentionsBean, final boolean deleteAttention) {
		final IAccount login = AccountSession.getLogin(compParameter.getSession());
		if (login == null) {
			return;
		}

		final int attentions = attentionsBean.getAttentions();
		attentionsBean.setAttentions(deleteAttention ? attentions - 1 : attentions + 1);
		getTableEntityManager(compParameter).updateTransaction(new Object[] { "attentions" }, attentionsBean, new TableEntityAdapter() {
			@Override
			public void afterUpdate(final ITableEntityManager manager, final Object[] objects) {
				if (deleteAttention) {
					AttentionUtils.delete(compParameter, getFunctionModule(), attentionsBean.getId());
				} else {
					AttentionUtils.insert(compParameter, getFunctionModule(), attentionsBean.getId());
				}
			}
		});
	}

	@Override
	public void doAttentionSent(final ComponentParameter compParameter, final Object... beans) {
		// none
	}

	protected void doAttentionMail(final ComponentParameter compParameter, final ISentCallback attentionMail) {
		((ITaskExecutorAware) PageUtils.pageContext.getApplication()).getTaskExecutor().execute(new ExecutorRunnable() {
			@Override
			public void task() {
				AttentionUtils.sentMessage(compParameter, getFunctionModule(), attentionMail);
			}
		});
	}

	protected abstract class AttentionMail implements ISentCallback {
		private final ComponentParameter compParameter;

		private final IAttentionsBeanAware attentionsBean;

		protected AttentionMail(final ComponentParameter compParameter, final IAttentionsBeanAware attentionsBean) {
			this.compParameter = compParameter;
			this.attentionsBean = attentionsBean;
		}

		@Override
		public Object getId() {
			return attentionsBean.getId();
		}

		@Override
		public boolean isSent(final IUser toUser) {
			return true;
		}

		protected String linkUrl(final String url) {
			final StringBuilder sb = new StringBuilder();
			final String href = ((WebApplicationConfig) PageUtils.pageContext.getApplication().getApplicationConfig()).getServerUrl()
					+ compParameter.wrapContextPath(url);
			sb.append("<a target=\"_blank\" href=\"").append(href).append("\">");
			sb.append(href).append("</a>");
			return sb.toString();
		}

		protected String getFromTemplate(final Map<String, Object> variable, final Class<?> templateClazz) {
			try {
				final String readerString = IoUtils.getStringFromInputStream(templateClazz.getClassLoader().getResourceAsStream(
						BeanUtils.getResourceClasspath(templateClazz, "attention.html")));
				return ScriptEvalUtils.replaceExpr(variable, readerString);
			} catch (final Exception e) {
				throw HandleException.wrapException(e);
			}
		}
	}

	@Override
	public <T extends IDataObjectBean> void doEditCallback(final ComponentParameter compParameter, final ITableEntityManager temgr, final T t,
			final Map<String, Object> data, final Class<T> beanClazz) {
		super.doEditCallback(compParameter, temgr, t, data, beanClazz);
		((AAttributeAware) t).removeAttribute(ContentUtils.KEY_CONTENT_DOCUMENT);
	}
}
