package net.simpleframework.content.component.newspager;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.simpleframework.ado.IDataObjectValue;
import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.applets.attention.AttentionUtils;
import net.simpleframework.applets.tag.TagUtils;
import net.simpleframework.content.AbstractContentBase;
import net.simpleframework.content.EContentStatus;
import net.simpleframework.content.EContentType;
import net.simpleframework.content.IContentPagerHandle;
import net.simpleframework.content.blog.Blog;
import net.simpleframework.content.component.remark.RemarkItem;
import net.simpleframework.content.component.vote.Vote;
import net.simpleframework.content.news.NewsUtils;
import net.simpleframework.core.ado.DataObjectException;
import net.simpleframework.core.bean.IDataObjectBean;
import net.simpleframework.core.id.ID;
import net.simpleframework.my.space.MySpaceUtils;
import net.simpleframework.organization.IUser;
import net.simpleframework.organization.account.IAccount;
import net.simpleframework.util.BeanUtils;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.util.HTMLBuilder;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.util.StringUtils;
import net.simpleframework.util.db.DbUtils;
import net.simpleframework.web.EFunctionModule;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.PageUtils;
import net.simpleframework.web.page.component.AbstractComponentBean;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.pager.AbstractTablePagerData;
import net.simpleos.SimpleosUtil;
import net.simpleos.mvc.myfavorite.MyFavoriteUtils;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class DefaultNewsPagerHandle extends AbstractNewsPagerHandle {
	@Override
	public void handleCreated(final PageRequestResponse requestResponse, final AbstractComponentBean componentBean) {
		if (NewsBean.class.equals(getEntityBeanClass())) {
			PageUtils.doDatabase(NewsBean.class, componentBean);
		}
	}

	@Override
	public Map<String, Object> getFormParameters(final ComponentParameter compParameter) {
		final Map<String, Object> parameters = super.getFormParameters(compParameter);
		final NewsBean newsBean = getEntityBeanByRequest(compParameter);
		if (newsBean != null) {
			parameters.put(getIdParameterName(compParameter), newsBean.getId());
		}
		return parameters;
	}

	@Override
	public <T extends IDataObjectBean> void doBeforeAdd(final ComponentParameter compParameter, final ITableEntityManager temgr, final T t,
			final Map<String, Object> data, final Class<T> beanClazz) {
		super.doBeforeAdd(compParameter, temgr, t, data, beanClazz);
		final NewsBean newsBean = (NewsBean) t;
		newsBean.initThis(compParameter);
		for (final String attri : new String[] { "topic", "status", "author", "source", "keywords", "content", "description", "allowComments",
				"catalogId", "mark" }) {
			final Object v = data.get(attri);
			if (v != null) {
				BeanUtils.setProperty(newsBean, attri, v);
			}
		}

		syncNewsContent(compParameter, newsBean, data);
		if (ConvertUtils.toBoolean(data.get("attention"), false)) {
			newsBean.setAttentions(newsBean.getAttentions() + 1);
		}
	}

	@Override
	public <T extends IDataObjectBean> void doAddCallback(final ComponentParameter compParameter, final ITableEntityManager temgr, final T t,
			final Map<String, Object> data, final Class<T> beanClazz) {
		super.doAddCallback(compParameter, temgr, t, data, beanClazz);

		final NewsBean newsBean = (NewsBean) t;
		TagUtils.syncTags(getFunctionModule(), ID.zero, newsBean.getKeywords(), newsBean.getId());

		if (ConvertUtils.toBoolean(data.get("attention"), false)) {
			AttentionUtils.insert(compParameter, getFunctionModule(), newsBean.getId());
		}
		addVote(newsBean);
		createLuceneManager(compParameter).objects2DocumentsBackground(t);
	}

	private void addVote(final NewsBean newsBean) {
		try {
			if (newsBean == null)
				return;
			if (newsBean.getMark() == 1) {
				try {
					ITableEntityManager tMgr = MySpaceUtils.getTableEntityManager(Vote.class);
					Vote vote = tMgr.queryForObject(new ExpressionValue("documentid=" + newsBean.getId()), Vote.class);
					if (vote == null) {
						vote = new Vote();
						vote.setDocumentId(newsBean.getId());
						tMgr.insert(vote);
					}
				} catch (Exception e) {
				}
			}
		} catch (Exception e) {
		}
	}

	@Override
	public <T extends IDataObjectBean> void doBeforeEdit(final ComponentParameter compParameter, final ITableEntityManager temgr, final T t,
			final Map<String, Object> data, final Class<T> beanClazz) {
		super.doBeforeEdit(compParameter, temgr, t, data, beanClazz);
		final NewsBean newsBean = (NewsBean) t;
		if (newsBean.getStatus() == EContentStatus.audit) {
			newsBean.setCreateDate(new Date());
		}
		final Map<String, Object> data2 = new HashMap<String, Object>();
		data2.put("att2", data.remove("att2"));
		data.put("content", data.get("content"));
		setObjectFromMap(newsBean, data);
		syncNewsContent(compParameter, newsBean, data2);
	}

	private void syncNewsContent(final ComponentParameter compParameter, final NewsBean newsBean, final Map<String, Object> data) {
		final String content = doDownloadContent(compParameter, newsBean.getContent(), newsBean.getId(), data);
		newsBean.setContent(content);
	}

	@Override
	public <T extends IDataObjectBean> void doEditCallback(final ComponentParameter compParameter, final ITableEntityManager temgr, final T t,
			final Map<String, Object> data, final Class<T> beanClazz) {
		super.doEditCallback(compParameter, temgr, t, data, beanClazz);
		final NewsBean newsBean = (NewsBean) t;
		TagUtils.syncTags(getFunctionModule(), ID.zero, newsBean.getKeywords(), newsBean.getId());
		createLuceneManager(compParameter).objects2DocumentsBackground(t);
		addVote(newsBean);
	}

	@Override
	public <T extends IDataObjectBean> void doDeleteCallback(final ComponentParameter compParameter, final IDataObjectValue dataObjectValue,
			final Class<T> beanClazz) {
		super.doDeleteCallback(compParameter, dataObjectValue, beanClazz);
		final Object[] values = dataObjectValue.getValues();
		final ITableEntityManager temgr_new = getTableEntityManager(compParameter);
		for (final Object v : values) {
			NewsBean newsBean = (NewsBean) temgr_new.queryForObjectById(v, getEntityBeanClass());
			if (newsBean != null) {
				if (SimpleosUtil.isManageOrSelf(compParameter, NewsUtils.applicationModule, newsBean.getUserId())) {
					SimpleosUtil.update(compParameter, newsBean.getUserId(), newsBean.getId(), false);
				} else {
					throw DataObjectException.wrapException("你不是管理员，没有权限删除!");
				}
			}
		}
		final int l = values.length;
		final ITableEntityManager temgr = getTableEntityManager(compParameter, RemarkItem.class);
		if (temgr != null) {
			temgr.delete(new ExpressionValue(DbUtils.getIdsSQLParam("documentid", l), values));
		}
		TagUtils.deleteRTags(values);
		createLuceneManager(compParameter).deleteDocumentBackground(values);
	}

	@Override
	public AbstractTablePagerData createTablePagerData(final ComponentParameter compParameter) {
		return new NewsTablePagerData(compParameter);
	}

	protected class NewsTablePagerData extends AbstractTablePagerData {
		public NewsTablePagerData(final ComponentParameter compParameter) {
			super(compParameter);
		}

		@Override
		protected Map<Object, Object> getRowData(final Object dataObject) {
			final NewsBean news = (NewsBean) dataObject;
			final Map<Object, Object> rowData = new HashMap<Object, Object>();
			rowData.put("topic", getTitle(news));
			rowData.put("author", news.getAuthor());
			rowData.put("createdate", ConvertUtils.toDateString(news.getCreateDate(), "yyyy-MM-dd"));
			rowData.put("status", String.valueOf(news.getStatus()));
			rowData.put("views", wrapNum(news.getViews()));
			rowData.put("remarks", wrapNum(news.getRemarks()));
			rowData.put("action", ACTIONc);
			return rowData;
		}

		private String getTitle(final NewsBean news) {
			return wrapOpenLink(compParameter, news);
		}

		private final Map<EContentType, String> images = new ConcurrentHashMap<EContentType, String>();
		{
			images.put(EContentType.recommended, "recommended.gif");
			images.put(EContentType.image, "pic.gif");
			images.put(EContentType.announce, "announce.gif");
		}

		protected String getImageByType(final EContentType ttype) {
			final StringBuilder sb = new StringBuilder();
			final String image = images.get(ttype);
			if (StringUtils.hasText(image)) {
				sb.append("<img style=\"margin-left: 5px;\" src=\"");
				sb.append(NewsPagerUtils.getCssPath(compParameter));
				sb.append("/images/").append(image).append("\" />");
			}
			return sb.toString();
		}

		protected String deleteAttention(final EFunctionModule aModule, final NewsBean news) {
			final StringBuilder sb = new StringBuilder();
			sb.append("<a class=\"a2 nav_arrow\" style=\"margin-left: 5px;\" ");
			sb.append("onclick=\"$Actions['ajax").append(aModule.name()).append("Attention']('").append(IContentPagerHandle._VTYPE).append("=")
					.append(aModule).append("&").append(getIdParameterName(compParameter)).append("=").append(news.getId()).append("');\">");
			sb.append("#(AttentionUtils.0)</a>");
			return sb.toString();
		}
	}

	@Override
	public boolean isRemarkNew(final ComponentParameter compParameter, final NewsBean news) {
		if (news.getRemarks() == 0) {
			return false;
		}
		final Calendar cal = Calendar.getInstance();
		cal.add(Calendar.HOUR, -12);
		final ExpressionValue ev = new ExpressionValue("documentid=? and createdate>?", new Object[] { news.getId(), cal.getTime() });
		return getTableEntityManager(compParameter, RemarkItem.class).getCount(ev) > 0;
	}

	@Override
	public String getJavascriptCallback(final ComponentParameter compParameter, final String jsAction, final Object bean) {
		if ("add".equals(jsAction) || "edit".equals(jsAction)) {
			final StringBuilder js = new StringBuilder();
			js.append("var act = $Actions['").append(compParameter.componentBean.getName());
			js.append("']; if (act) act.refresh('");
			js.append(getIdParameterName(compParameter)).append("='); else { window.onbeforeunload = null; $Actions.loc('");
			js.append(getViewUrl(compParameter, (NewsBean) bean));
			js.append("'); }");
			return js.toString();
		}
		return super.getJavascriptCallback(compParameter, jsAction, bean);
	}

	@Override
	public String getHtmlEditorToolbar(final ComponentParameter compParameter) {
		final StringBuilder sb = new StringBuilder();
		sb.append("<a class=\"simple_btn simple_btn_left\" ");
		sb.append("onclick=\"$Actions['__news_myfileSelect']();\">#(TopicPagerUtils.0)</a>");
		sb.append("<a class=\"simple_btn simple_btn_right\" ");
		sb.append("onclick=\"$Actions['__news_syntaxHighlighter'].editor();\">#(TopicPagerUtils.4)</a>");
		return sb.toString();
	}

	@Override
	public String getActionsHTML(final ComponentParameter compParameter, final AbstractContentBase contentBase) {
		final NewsBean news = (NewsBean) contentBase;
		final StringBuilder sb = new StringBuilder();
		final IAccount account = SimpleosUtil.getLoginAccount(compParameter);
		if (account != null) {
			sb.append("<a class=\"a2\" id='favorite_act' onclick=\"");
			long c = MyFavoriteUtils.getFavorites(news.getId(), account.getId());
			if (c > 0) {
				sb.append("$Actions['cancelFavorite']('refId=" + news.getId() + "');\">");
				sb.append(LocaleI18n.getMessage("AbstractContentPagerHandle.2"));
			} else {
				sb.append("$Actions['addFavorite']('refId=" + news.getId() + "&title=" + news.getTopic() + "&type="
						+ ((news instanceof Blog) ? EFunctionModule.blog.name() : EFunctionModule.news.name()) + "');\">");
				sb.append(LocaleI18n.getMessage("AbstractContentPagerHandle.1"));
			}
			sb.append("</a>");
			sb.append("&nbsp;(<a class=\"a2\"  id='favorite_num' onclick=\"$Actions['attentionUsersWindow']('attentionId=").append(news.getId())
					.append("');\">").append(c).append("</a>) ");
		}
		if (NewsPagerUtils.isNewsEdit(compParameter, news)) {
			sb.append(HTMLBuilder.SEP);
			sb.append("<a class=\"a2\" onclick=\"$Actions['addNewspagerWindow']();\">");
			sb.append(LocaleI18n.getMessage("Edit")).append("</a> ");
		}
		if (news instanceof Blog) {
			//			sb.append(HTMLBuilder.SEP);
		}
		return sb.toString();
	}

	@Override
	public void doAttentionSent(final ComponentParameter compParameter, final Object... beans) {
		final RemarkItem remark = (RemarkItem) beans[0];
		final INewsPagerHandle nHandle = (INewsPagerHandle) compParameter.getComponentHandle();
		final NewsBean news = nHandle.getEntityBeanByRequest(compParameter);

		doAttentionMail(compParameter, new AttentionMail(compParameter, news) {
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
				variable.put("createUser", news.getUserText());
				variable.put("createDate", ConvertUtils.toDateString(news.getCreateDate()));
				variable.put("fromUser", remark.getUserText());
				variable.put("topic", news.getTopic());
				variable.put("content", remark.getContent());
				return getFromTemplate(variable, DefaultNewsPagerHandle.class);
			}
		});
	}
}
