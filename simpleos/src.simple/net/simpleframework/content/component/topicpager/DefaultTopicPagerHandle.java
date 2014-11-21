package net.simpleframework.content.component.topicpager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.itsite.ItSiteUtil;
import net.prj.mvc.myfavorite.MyFavoriteUtils;
import net.simpleframework.ado.IDataObjectValue;
import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.ado.db.SQLValue;
import net.simpleframework.applets.attention.AttentionUtils;
import net.simpleframework.applets.tag.TagUtils;
import net.simpleframework.content.AbstractContentBase;
import net.simpleframework.content.EContentStatus;
import net.simpleframework.content.EContentType;
import net.simpleframework.content.bbs.BbsForum;
import net.simpleframework.content.bbs.BbsTopic;
import net.simpleframework.content.bbs.BbsUtils;
import net.simpleframework.content.bbs.IBbsApplicationModule;
import net.simpleframework.content.component.catalog.Catalog;
import net.simpleframework.content.component.vote.Vote;
import net.simpleframework.content.component.vote.VoteUtils;
import net.simpleframework.core.ado.DataObjectException;
import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.core.bean.IDataObjectBean;
import net.simpleframework.core.id.ID;
import net.simpleframework.my.message.MessageUtils;
import net.simpleframework.organization.IUser;
import net.simpleframework.organization.OrgUtils;
import net.simpleframework.organization.account.AccountSession;
import net.simpleframework.organization.account.IAccount;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.util.DateUtils;
import net.simpleframework.util.HTMLBuilder;
import net.simpleframework.util.HTTPUtils;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.EFunctionModule;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.PageUtils;
import net.simpleframework.web.page.component.AbstractComponentBean;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ado.IDbComponentHandle;
import net.simpleframework.web.page.component.ui.pager.AbstractTablePagerData;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class DefaultTopicPagerHandle extends AbstractTopicPagerHandle {
	@Override
	public void handleCreated(final PageRequestResponse requestResponse, final AbstractComponentBean componentBean) {
		if (TopicBean.class.equals(getEntityBeanClass())) {
			PageUtils.doDatabase(TopicBean.class, componentBean);
		}
	}

	@Override
	public IDataObjectQuery<? extends PostsBean> getPostsList(final ComponentParameter compParameter, final TopicBean topicBean) {
		final StringBuilder sql = new StringBuilder();
		final ArrayList<Object> al = new ArrayList<Object>();
		sql.append("topicId=?");
		al.add(topicBean.getId());
		final String userId = compParameter.getRequestParameter(OrgUtils.um().getUserIdParameterName());
		if (StringUtils.hasText(userId)) {
			sql.append(" and userid=?");
			al.add(userId);
		}
		sql.append(" order by firstpost desc, createdate");
		if (ConvertUtils.toBoolean(compParameter.getRequestParameter("dsort"), false)) {
			sql.append(" desc");
		}
		return getTableEntityManager(compParameter, PostsBean.class).query(new ExpressionValue(sql.toString(), al.toArray()), PostsBean.class);
	}

	@Override
	public long getPostInterval() {
		return 1000 * 60;
	}

	private void syncTopicMark(final ComponentParameter compParameter, final TopicBean topicBean, final String content, final boolean all) {
		final Document doc = Jsoup.parse(content);
		if (!doc.select("a[onclick^=$Actions['__my_folderfile_ajax_download']]").isEmpty()) {
			topicBean.setMark((short) (topicBean.getMark() | TopicBean.MARK_ATTACH));
		} else {
			if (all) {
				topicBean.setMark((short) (topicBean.getMark() & (TopicBean.MARK_ATTACH ^ 0xFF)));
			}
		}
		boolean isImage = false;
		final Elements imgs = doc.select("img[src]");
		for (int i = 0; i < imgs.size(); i++) {
			final String src = imgs.get(i).attr("src");
			if (!src.contains("/htmlEditor/smiley/")) {
				isImage = true;
				break;
			}
		}
		if (isImage) {
			topicBean.setMark((short) (topicBean.getMark() | TopicBean.MARK_IMAGE));
		} else {
			if (all) {
				topicBean.setMark((short) (topicBean.getMark() & (TopicBean.MARK_IMAGE ^ 0xFF)));
			}
		}
	}

	private void editTopicMark(final ComponentParameter compParameter, final TopicBean topicBean, final PostsTextBean postText) {
		final StringBuilder sb = new StringBuilder();
		sb.append(postText.getContent());
		sb.append(TopicPagerUtils.getAllTopicContent(compParameter, topicBean, postText));
		syncTopicMark(compParameter, topicBean, sb.toString(), true);
	}

	private void deleteTopicMark(final ComponentParameter compParameter, final TopicBean topicBean) {
		syncTopicMark(compParameter, topicBean, TopicPagerUtils.getAllTopicContent(compParameter, topicBean), true);
	}

	private void syncTopicContent(final ComponentParameter compParameter, final PostsTextBean postText, final Map<String, Object> data) {
		final String content = doDownloadContent(compParameter, postText.getContent(), postText.getId(), data);
		postText.setContent(content);
	}

	private static final String KEY_TOPIC_BEAN = "topic_bean";

	private static final String KEY_POST_BEAN = "post_bean";

	private static final String KEY_POST_TEXT_BEAN = "posttext_bean";

	private static final String KEY_VOTE_BEAN = "vote_bean";

	@Override
	public <T extends IDataObjectBean> void doBeforeAdd(final ComponentParameter compParameter, final ITableEntityManager temgr, final T t,
			final Map<String, Object> data, final Class<T> beanClazz) {
		super.doBeforeAdd(compParameter, temgr, t, data, beanClazz);
		if (TopicBean.class.isAssignableFrom(beanClazz)) {
			final TopicBean topicBean = (TopicBean) t;
			topicBean.initThis(compParameter);
			topicBean.setId(temgr.nextId("id"));
			topicBean.setStatus(EContentStatus.publish);
			topicBean.setCatalogId(ID.Utils.newID(getCatalogId(compParameter)));
			topicBean.setTopic(ConvertUtils.toString(data.get("topic")));
			topicBean.setKeywords(ConvertUtils.toString(data.get("keywords")));

			final PostsBean postsBean = new PostsBean();
			data.put(KEY_POST_BEAN, postsBean);
			postsBean.initThis(compParameter);
			postsBean.setId(temgr.nextId("id"));
			postsBean.setTopicId(topicBean.getId());
			postsBean.setFirstPost(true);
			postsBean.setIp(HTTPUtils.getRemoteAddr(compParameter.request));
			topicBean.setLastpostId(postsBean.getId());
			if (ConvertUtils.toBoolean(data.get("attention"), false)) {
				topicBean.setAttentions(topicBean.getAttentions() + 1);
			}

			final PostsTextBean postText = new PostsTextBean();
			data.put(KEY_POST_TEXT_BEAN, postText);
			postText.setId(postsBean.getId());
			postText.setContent((String) data.get("content"));
			syncTopicContent(compParameter, postText, data);
			syncTopicMark(compParameter, topicBean, postText.getContent(), false);

			createVote(compParameter, topicBean, data);
		} else if (PostsBean.class.isAssignableFrom(beanClazz)) {
			final PostsBean postsBean = (PostsBean) t;
			postsBean.setId(temgr.nextId("id"));
			postsBean.initThis(compParameter);
			final String quoteId = ConvertUtils.toString(data.get("quoteId"));
			PostsBean pb = null;
			PostsTextBean ptb = null;
			if (StringUtils.hasText(quoteId)) {
				final ID quote = getTableEntityManager(compParameter, PostsBean.class).getTable().newID(quoteId);
				postsBean.setQuoteId(quote);
				pb = getTableEntityManager(compParameter, PostsBean.class).queryForObjectById(quoteId, PostsBean.class);
				ptb = getTableEntityManager(compParameter, PostsTextBean.class).queryForObjectById(quoteId, PostsTextBean.class);
			}

			final TopicBean topicBean = getEntityBeanById(compParameter, data.get("topicId"));
			data.put(KEY_TOPIC_BEAN, topicBean);
			postsBean.setTopicId(topicBean.getId());
			postsBean.setIp(HTTPUtils.getRemoteAddr(compParameter.request));
			topicBean.setLastpostId(postsBean.getId());
			topicBean.setLastPostUpdate(new Date());
			topicBean.setReplies(topicBean.getReplies() + 1);

			final PostsTextBean postText = new PostsTextBean();
			data.put(KEY_POST_TEXT_BEAN, postText);
			postText.setId(postsBean.getId());
			postText.setSubject(ConvertUtils.toString(data.get("topic")));
			postText.setContent(ConvertUtils.toString(data.get("content")));

			if (topicBean != null) {
				if (pb == null) {
					try {
						if (!postsBean.getUserId().equals2(topicBean.getUserId())) {
							final StringBuffer textBody = new StringBuffer();
							textBody.append("《" + wrapOpenLink(compParameter, topicBean) + "》").append("<br/>");
							textBody.append(postText.getContent());
							final String subject = postsBean.getUserText() + "评论你的帖子";
							MessageUtils.createNotifation(compParameter, subject, textBody.toString(), postsBean.getUserId(), topicBean.getUserId());
						}
					} catch (Exception e) {
					}
				} else {
					try {
						if (!postsBean.getUserId().equals2(topicBean.getUserId())) {
							final StringBuffer textBody = new StringBuffer();
							textBody.append("《" + wrapOpenLink(compParameter, topicBean) + "》").append("<br/>");
							textBody.append("<blockquote>原评论<br/>").append(ptb.getContent()).append("</blockquote>");
							textBody.append("<blockquote>现评论<br/>").append(postText.getContent()).append("</blockquote>");
							final String subject = postsBean.getUserText() + "评论你的评论";
							MessageUtils.createNotifation(compParameter, subject, textBody.toString(), postsBean.getUserId(), pb.getUserId());
						}
					} catch (Exception e) {
					}
				}
			}

			syncTopicContent(compParameter, postText, data);
			syncTopicMark(compParameter, topicBean, postText.getContent(), false);
		}
	}

	private void createVote(final ComponentParameter compParameter, final TopicBean topicBean, final Map<String, Object> data) {
		if (!ConvertUtils.toBoolean(data.get("vote"), false)) {
			return;
		}
		final Vote vote = new Vote();
		vote.initThis(compParameter);
		vote.setDocumentId(topicBean.getId());
		data.put(KEY_VOTE_BEAN, vote);
		topicBean.setMark((short) (topicBean.getMark() | TopicBean.MARK_VOTE));
	}

	@Override
	public <T extends IDataObjectBean> void doAddCallback(final ComponentParameter compParameter, final ITableEntityManager temgr, final T t,
			final Map<String, Object> data, final Class<T> beanClazz) {
		super.doAddCallback(compParameter, temgr, t, data, beanClazz);
		if (TopicBean.class.isAssignableFrom(beanClazz)) {
			final TopicBean topicBean = (TopicBean) t;
			getTableEntityManager(compParameter, PostsBean.class).insert(data.get(KEY_POST_BEAN));
			getTableEntityManager(compParameter, PostsTextBean.class).insert(data.get(KEY_POST_TEXT_BEAN));
			getTableEntityManager(compParameter, Vote.class).insert(data.get(KEY_VOTE_BEAN));

			if (ConvertUtils.toBoolean(data.get("attention"), false)) {
				AttentionUtils.insert(compParameter, getFunctionModule(), topicBean.getId());
			}
			syncTags(topicBean, temgr.getTable().newID(getCatalogId(compParameter)));

			createLuceneManager(compParameter).objects2DocumentsBackground(topicBean);
		} else if (PostsBean.class.isAssignableFrom(beanClazz)) {
			final PostsBean postsBean = (PostsBean) t;
			getTableEntityManager(compParameter, PostsTextBean.class).insert(data.get(KEY_POST_TEXT_BEAN));
			getTableEntityManager(compParameter, TopicBean.class).update(data.get(KEY_TOPIC_BEAN));

			createLuceneManager(compParameter).objects2DocumentsBackground(postsBean);

			//			doAttentionSent(compParameter, postsBean, data.get(KEY_TOPIC_BEAN));
		}
	}

	private void syncTags(final TopicBean topicBean, final ID catalogId) {
		TagUtils.syncTags(getFunctionModule(), catalogId, topicBean.getKeywords(), topicBean.getId());
	}

	@Override
	public void doAttentionSent(final ComponentParameter compParameter, final Object... beans) {
		if (beans == null) {
			return;
		}
		final PostsBean postsBean = (PostsBean) beans[0];
		final TopicBean topicBean = (TopicBean) beans[1];
		if (topicBean == null) {
			return;
		}
		doAttentionMail(compParameter, new AttentionMail(compParameter, topicBean) {
			@Override
			public boolean isSent(final IUser toUser) {
				return !toUser.getId().equals2(postsBean.getUserId());
			}

			@Override
			public String getSubject(final IUser toUser) {
				final StringBuilder sb = new StringBuilder();
				sb.append("\"").append(postsBean.getUserText()).append("\"").append(LocaleI18n.getMessage("DefaultTopicPagerHandle.5"));
				return sb.toString();
			}

			@Override
			public String getBody(final IUser toUser) {
				final Map<String, Object> variable = new HashMap<String, Object>();
				variable.put("topicHref", linkUrl(getPostViewUrl(compParameter, topicBean)));
				variable.put("toUser", toUser);
				variable.put("createUser", topicBean.getUserText());
				variable.put("createDate", ConvertUtils.toDateString(topicBean.getCreateDate()));
				variable.put("fromUser", postsBean.getUserText());
				variable.put("topic", topicBean.getTopic());
				return getFromTemplate(variable, DefaultTopicPagerHandle.class);
			}
		});
	}

	@Override
	public PostsBean getPostsBean(final ComponentParameter compParameter, final TopicBean topicBean) {
		final ExpressionValue ev = new ExpressionValue("topicid=? and firstpost=?", new Object[] { topicBean.getId(), Boolean.TRUE });
		return getTableEntityManager(compParameter, PostsBean.class).queryForObject(ev, PostsBean.class);
	}

	@Override
	public PostsTextBean getPostsText(final ComponentParameter compParameter, final Object objectBean) {
		final ITableEntityManager text_mgr = getTableEntityManager(compParameter, PostsTextBean.class);
		if (objectBean instanceof TopicBean) {
			final String text_name = text_mgr.getTablename();
			final String post_name = getTableEntityManager(compParameter, PostsBean.class).getTablename();
			final StringBuilder sql = new StringBuilder();
			sql.append("select ").append(text_name).append(".* from ").append(text_name);
			sql.append(" inner join ").append(post_name).append(" on ");
			sql.append(post_name).append(".id=").append(text_name).append(".id where ");
			sql.append(post_name).append(".topicid=? and ").append(post_name).append(".firstpost=?");
			return text_mgr.queryForObject(new SQLValue(sql.toString(), new Object[] { ((TopicBean) objectBean).getId(), Boolean.TRUE }),
					PostsTextBean.class);
		} else {
			Object postId;
			if (objectBean instanceof PostsBean) {
				postId = ((PostsBean) objectBean).getId();
			} else {
				postId = objectBean;
			}
			return text_mgr.queryForObjectById(postId, PostsTextBean.class);
		}
	}

	@Override
	public <T extends IDataObjectBean> void doBeforeEdit(final ComponentParameter compParameter, final ITableEntityManager temgr, final T t,
			final Map<String, Object> data, final Class<T> beanClazz) {
		super.doBeforeEdit(compParameter, temgr, t, data, beanClazz);
		TopicBean topicBean = null;
		PostsBean postsBean = null;
		if (TopicBean.class.isAssignableFrom(beanClazz)) {
			topicBean = (TopicBean) t;
			Object v;
			if (data.get("top") != null) {
				topicBean.setTtop(!topicBean.isTtop());
			} else if ((v = data.get("catalog")) != null) {
				final Catalog oCatalog = getEntityBeanById(compParameter, topicBean.getCatalogId(), Catalog.class);
				final Catalog mCatalog = getEntityBeanById(compParameter, v, Catalog.class);
				if (oCatalog != null && mCatalog != null && !oCatalog.equals2(mCatalog)) {
					topicBean.setCatalogId(mCatalog.getId());
					data.put("oCatalog", oCatalog);
					data.put("mCatalog", mCatalog);
				}
			} else if ((v = data.get("star")) != null) {
				topicBean.setStar((Short) v);
				topicBean.setTtype((EContentType) data.get("ttype"));
				if (topicBean.getStatus() == EContentStatus.audit) {
					topicBean.setCreateDate(new Date());
				}
				topicBean.setStatus(EContentStatus.publish);
			} else {
				topicBean.setTopic(ConvertUtils.toString(data.get("topic")));
				topicBean.setKeywords(ConvertUtils.toString(data.get("keywords")));
				postsBean = getPostsBean(compParameter, topicBean);
				postsBean.updateLastUpdate(compParameter);
				data.put(KEY_POST_BEAN, postsBean);
			}
		} else if (PostsBean.class.isAssignableFrom(beanClazz)) {
			postsBean = (PostsBean) t;
			topicBean = (TopicBean) getEntityBeanById(compParameter, postsBean.getTopicId());
			data.put(KEY_TOPIC_BEAN, topicBean);
		}

		if (!PostsTextBean.class.isAssignableFrom(beanClazz)) {
			createVote(compParameter, topicBean, data);
		}

		if (postsBean != null) {
			postsBean.setIp(HTTPUtils.getRemoteAddr(compParameter.request));
			topicBean.setLastpostId(postsBean.getId());
			final PostsTextBean postText = getPostsText(compParameter, postsBean);
			if (postText != null) {
				data.put(KEY_POST_TEXT_BEAN, postText);
				final String topic = ConvertUtils.toString(data.get("topic"));
				if (postsBean.isFirstPost()) {
					topicBean.setTopic(topic);
					postText.setSubject(null);
				} else {
					postText.setSubject(topic);
				}
				postText.setContent((String) data.get("content"));
				syncTopicContent(compParameter, postText, data);
				editTopicMark(compParameter, topicBean, postText);
			}
		}
	}

	@Override
	public <T extends IDataObjectBean> void doEditCallback(final ComponentParameter compParameter, final ITableEntityManager temgr, final T t,
			final Map<String, Object> data, final Class<T> beanClazz) {
		super.doEditCallback(compParameter, temgr, t, data, beanClazz);
		getTableEntityManager(compParameter, TopicBean.class).update(data.get(KEY_TOPIC_BEAN));
		getTableEntityManager(compParameter, PostsBean.class).update(data.get(KEY_POST_BEAN));
		getTableEntityManager(compParameter, PostsTextBean.class).update(data.get(KEY_POST_TEXT_BEAN));

		final Object voteBean = data.get(KEY_VOTE_BEAN);
		if (voteBean != null) {
			getTableEntityManager(compParameter, Vote.class).insert(voteBean);
		}
		if ((TopicBean.class.isAssignableFrom(beanClazz) || PostsBean.class.isAssignableFrom(beanClazz))
				&& StringUtils.hasText((String) data.get("content"))) {
			TopicBean topicBean = null;
			if (t instanceof TopicBean) {
				topicBean = (TopicBean) t;
			} else {
				topicBean = (TopicBean) data.get(KEY_TOPIC_BEAN);
			}
			if (topicBean != null) {
				syncTags(topicBean, temgr.getTable().newID(getCatalogId(compParameter)));
			}
			createLuceneManager(compParameter).objects2DocumentsBackground(t);
		}
	}

	@Override
	public <T extends IDataObjectBean> void doDeleteCallback(final ComponentParameter compParameter, final IDataObjectValue dataObjectValue,
			final Class<T> beanClazz) {
		super.doDeleteCallback(compParameter, dataObjectValue, beanClazz);
		final Object[] values = dataObjectValue.getValues();
		final ITableEntityManager topic_mgr = getTableEntityManager(compParameter, BbsTopic.class);
		final ITableEntityManager post_mgr = getTableEntityManager(compParameter, PostsBean.class);
		final ITableEntityManager post_text_mgr = getTableEntityManager(compParameter, PostsTextBean.class);
		final String simple_posts_text = post_text_mgr.getTablename();
		if (TopicBean.class.isAssignableFrom(beanClazz)) {
			for (final Object v : values) {
				TopicBean topicBean = topic_mgr.queryForObjectById(v, BbsTopic.class);
				if (topicBean != null) {
					if (ItSiteUtil.isManageOrSelf(compParameter, BbsUtils.applicationModule, topicBean.getUserId())) {
						final String topic_where = "topicid=" + topicBean.getId();
						final String sql1 = "delete from " + simple_posts_text + " where id in (select id from " + post_mgr.getTablename()
								+ " where " + topic_where + ")";
						post_text_mgr.execute(new SQLValue(sql1, null));
						ItSiteUtil.update(compParameter, topicBean.getUserId(), topicBean.getId(), false);
						post_mgr.delete(new ExpressionValue(topic_where, null));
					} else {
						throw DataObjectException.wrapException("你不是管理员，没有权限删除!");
					}
				}
			}
			for (final Object value : values) {
				final Vote vote = TopicPagerUtils.getVote(compParameter, value);
				if (vote != null) {
					final SQLValue[] sqlValues = VoteUtils.getDeleteSQLs(compParameter, Vote.class, new Object[] { vote.getId() });
					for (final SQLValue sqlValue : sqlValues) {
						post_mgr.execute(sqlValue);
					}
				}
			}
			AttentionUtils.deleteByAttentionId(compParameter, getFunctionModule(), values);
			createLuceneManager(compParameter).deleteDocumentBackground(dataObjectValue.getValues());
		} else if (PostsBean.class.isAssignableFrom(beanClazz)) {
			for (final Object v : values) {
				final PostsBean postsBean = post_mgr.queryForObjectById(v, PostsBean.class);
				if (postsBean != null) {
					final String sql2 = "delete from " + simple_posts_text + " where id=?";
					post_text_mgr.execute(new SQLValue(sql2, new Object[] { v }));
					ItSiteUtil.update(compParameter, postsBean.getUserId(), postsBean.getId(), true);
				}
			}

			final TopicBean topicBean = ((TopicBean) getEntityBeanByRequest(compParameter));
			topicBean.setReplies(Math.max(topicBean.getReplies() - values.length, 0));
			deleteTopicMark(compParameter, topicBean);
			getTableEntityManager(compParameter).update(topicBean);

			createLuceneManager(compParameter).objects2DocumentsBackground(topicBean);
		}
	}

	@Override
	public IDataObjectQuery<?> createDataObjectQuery(final ComponentParameter compParameter) {
		final ExpressionValue ev = getBeansSQL(compParameter);
		final ExpressionValue ev2 = new ExpressionValue(ev.getExpression() + " order by ttop desc, lastpostupdate desc", ev.getValues());
		return getTableEntityManager(compParameter).query(ev2, getEntityBeanClass());
	}

	@Override
	public AbstractTablePagerData createTablePagerData(final ComponentParameter compParameter) {
		return new TopicTablePagerData(compParameter);
	}

	protected class TopicTablePagerData extends AbstractTablePagerData {
		public TopicTablePagerData(final ComponentParameter compParameter) {
			super(compParameter);
		}

		@Override
		protected Map<Object, Object> getRowAttributes(final Object dataObject) {
			final Map<Object, Object> attributes = super.getRowAttributes(dataObject);
			final TopicBean topic = (TopicBean) dataObject;
			attributes.put("top", topic.isTtop());
			return attributes;
		}

		private final int IMAGE_WIDTH = 22;

		@Override
		protected Map<Object, Object> getRowData(final Object dataObject) {
			final StringBuilder sb = new StringBuilder();
			final TopicBean topic = (TopicBean) dataObject;
			final Map<Object, Object> rowData = new HashMap<Object, Object>();
			final String imgBase = compParameter.componentBean.getCssResourceHomePath(compParameter) + "/images/";
			sb.append("<img style=\"vertical-align: middle;\" src=\"").append(imgBase);
			sb.append(topic.getTtype().name());
			if (isReplyNew(compParameter, topic)) {
				sb.append("_new.gif");
			} else {
				sb.append(".gif");
			}
			sb.append("\" />");
			rowData.put("icon", sb.toString());
			sb.setLength(0);
			final short mark = topic.getMark();
			final boolean nTopic = isNew(compParameter, topic);
			final boolean image = topic.isTtop() || mark > 0 || topic.getStar() > 0 || nTopic;
			if (image) {
				sb.append("<table style=\"\" cellpadding=\"0\" cellspacing=\"0\"><tr><td>");
			}
			final BbsForum forum = (BbsForum) getCatalogById(compParameter, topic.getCatalogId());
			if (forum != null) {
				sb.append("[<a class=\"\" style='color: #090;' href=\"")
						.append(((IBbsApplicationModule) getApplicationModule()).getTopicUrl(compParameter, forum)).append("\">")
						.append(forum.getText()).append("</a>]");
			}
			sb.append(wrapOpenLink(compParameter, topic));
			if (image) {
				final StringBuilder sb2 = new StringBuilder();
				int width = 0;
				if (topic.getStatus() == EContentStatus.audit) {
					sb.append("<span class='rred'>[审核").append("]</span>");
				}
				if (topic.isTtop()) {
					sb2.append("<span class=\"image_top\" title=\"#(DefaultTopicPagerHandle.3)\"></span>");
					width += IMAGE_WIDTH;
				}
				if (topic.getStar() > 0) {
					sb2.append("<span class=\"image_star").append(topic.getStar());
					sb2.append("\" title=\"#(DefaultTopicPagerHandle.2)\"></span>");
					width += IMAGE_WIDTH;
				}
				if (nTopic) {
					sb2.append("<span class=\"new_gif_image\"></span>");
					width += IMAGE_WIDTH;
				}
				if ((mark & TopicBean.MARK_VOTE) == TopicBean.MARK_VOTE) {
					sb2.append("<span class=\"image_vote\" title=\"#(DefaultTopicPagerHandle.1)\"></span>");
					width += IMAGE_WIDTH;
				}
				if ((mark & TopicBean.MARK_ATTACH) == TopicBean.MARK_ATTACH) {
					sb2.append("<span class=\"image_attach\" title=\"#(DefaultTopicPagerHandle.0)\"></span>");
					width += IMAGE_WIDTH;
				}
				if ((mark & TopicBean.MARK_IMAGE) == TopicBean.MARK_IMAGE) {
					sb2.append("<span class=\"image_pic\" title=\"#(DefaultTopicPagerHandle.4)\"></span>");
					width += IMAGE_WIDTH;
				}
				sb.append("</td><td width=\"").append(width).append("px;\" align=\"right\">");
				sb.append(sb2.toString());
				sb.append("</td></tr></table>");
			}

			rowData.put("topic", sb.toString());
			rowData.put("replies", wrapNum(topic.getReplies()));
			rowData.put("views", wrapNum(topic.getViews()));
			rowData.put("attentions", topic.getAttentions());
			final StringBuilder author = new StringBuilder();
			author.append("<a>").append(topic.getUserText()).append("</a>");
			author.append("<div style=\"margin-top: 3px; color: #999;\">");
			author.append(DateUtils.getRelativeDate(topic.getCreateDate()));
			author.append("</div>");
			rowData.put("author", author);

			final PostsBean postsBean = getEntityBeanById(compParameter, topic.getLastpostId(), PostsBean.class);
			if (postsBean != null) {
				final StringBuilder lastMessage = new StringBuilder();
				lastMessage.append("<a>").append(postsBean.getUserText()).append("</a>");
				lastMessage.append("<div style=\"margin-top: 3px; color: #999;\">");
				lastMessage.append(DateUtils.getRelativeDate(postsBean.getLastUpdate()));
				lastMessage.append("</div>");
				rowData.put("lastMessage", lastMessage.toString());
			}
			rowData.put("action", ACTIONc);
			return rowData;
		}
	}

	@Override
	public String getJavascriptCallback(final ComponentParameter compParameter, final String jsAction, final Object bean) {
		if ("add".equals(jsAction)) {
			final ITopicPagerHandle tHandle = (ITopicPagerHandle) compParameter.getComponentHandle();
			final TopicBean topic = (TopicBean) bean;
			final StringBuilder sb = new StringBuilder();
			final String pvUrl = tHandle.getPostViewUrl(compParameter, topic);
			if (StringUtils.hasText(pvUrl)) {
				sb.append("window.onbeforeunload = null;");
				sb.append("$Actions.loc(\"").append(pvUrl).append("\");");
			} else {
				sb.append("$Actions['").append(compParameter.componentBean.getName()).append("'].view(null, '");
				sb.append(getIdParameterName(compParameter)).append("=").append(topic.getId());
				sb.append("');");
			}
			return sb.toString();
		} else if ("load".equals(jsAction)) {
			return "$('fastReplyContent').clear();";
		} else {
			final String jsCallback = StringUtils.blank(super.getJavascriptCallback(compParameter, jsAction, bean));
			return jsCallback;
		}
	}

	@Override
	public String wrapOpenLink(final ComponentParameter compParameter, final TopicBean topic) {
		final StringBuilder sb = new StringBuilder();
		final String postViewUrl = getPostViewUrl(compParameter, topic);
		if (StringUtils.hasText(postViewUrl)) {
			sb.append("<a");
			if ((Boolean) compParameter.getBeanProperty("openBlank")) {
				sb.append(" target=\"_blank\"");
			}
			sb.append(" href=\"").append(postViewUrl).append("\"");
			if (compParameter.getRequestAttribute("home") != null && (Boolean) compParameter.getRequestAttribute("home")) {
				sb.append(" title=\"").append(topic.getTopic()).append("\"");
				sb.append(">");
				sb.append(ItSiteUtil.getShortString(topic.getTopic(), 35, true));
			} else {
				sb.append(">");
				sb.append(topic.getTopic());
			}
			sb.append("</a>");
		} else {
			sb.append("<a onclick=\"__pager_action(this).view(this, '");
			sb.append(getIdParameterName(compParameter)).append("=").append(topic.getId()).append("');\">").append(topic.getTopic()).append("</a>");
		}
		return sb.toString();
	}

	@Override
	public String getHtmlEditorToolbar(final ComponentParameter compParameter) {
		final StringBuilder sb = new StringBuilder();
		sb.append("<a class=\"simple_btn simple_btn_left\" ");
		sb.append("onclick=\"$Actions['__topic_myfileSelect']();\">#(TopicPagerUtils.0)</a>");
		sb.append("<a class=\"simple_btn simple_btn_right\" ");
		sb.append("onclick=\"$Actions['__topic_syntaxHighlighter'].editor();\">#(TopicPagerUtils.4)</a>");
		return sb.toString();
	}

	@Override
	public String getActionsHTML(final ComponentParameter compParameter, final AbstractContentBase contentBase) {
		final StringBuilder sb = new StringBuilder();
		final TopicBean topicBean = (TopicBean) contentBase;
		final IAccount account = ItSiteUtil.getLoginAccount(compParameter);
		if (account != null) {
			sb.append("<a class=\"a2\" id='favorite_act' onclick=\"");
			long c = MyFavoriteUtils.getFavorites(topicBean.getId(), account.getId());
			if (c > 0) {
				sb.append("$Actions['cancelFavorite']('refId=" + topicBean.getId() + "');\">");
				sb.append(LocaleI18n.getMessage("AbstractContentPagerHandle.2"));
			} else {
				sb.append("$Actions['addFavorite']('refId1=" + topicBean.getCatalogId() + "&refId=" + topicBean.getId() + "&title="
						+ topicBean.getTopic() + "&type=" + EFunctionModule.bbs.name() + "');\">");
				sb.append(LocaleI18n.getMessage("AbstractContentPagerHandle.1"));
			}
			sb.append("</a>");
			sb.append("&nbsp;(<a class=\"a2\"  id='favorite_num' onclick=\"$Actions['attentionUsersWindow']('attentionId=").append(topicBean.getId())
					.append("');\">").append(c).append("</a>) ");
		}

		sb.append(HTMLBuilder.SEP);
		// sort
		final String dsort = compParameter.getRequestParameter("dsort");
		sb.append("<a class=\"a2\" onclick=\"$Actions['__pager_postsId'].refresh");
		if (StringUtils.hasText(dsort)) {
			sb.append("('dsort=');\">");
			sb.append(LocaleI18n.getMessage("topic_view_pager.6"));
		} else {
			sb.append("('dsort=true');\">");
			sb.append(LocaleI18n.getMessage("topic_view_pager.5"));
		}
		sb.append("</a>");
		return sb.toString();
	}

	protected boolean isPostEdit(final IAccount account, final PostsBean postsBean) {
		final boolean edit = account.getId().equals2(postsBean.getUserId());
		/* 一天内允许编辑 */
		final Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		return edit && postsBean.getCreateDate().after(cal.getTime());
	}

	@Override
	public String getPostActions(final ComponentParameter compParameter, final PostsBean postsBean) {
		final StringBuilder sb = new StringBuilder();
		sb.append("<a onclick=\"$Actions['topicPagerReplyWindow']();\">#(TopicPagerUtils.1)</a>");
		sb.append(HTMLBuilder.SEP);
		sb.append("<a onclick=\"$Actions['topicPagerReplyWindow']('quoteId=");
		sb.append(postsBean.getId()).append("');\">#(TopicPagerUtils.3)</a>");
		final IAccount account = AccountSession.getLogin(compParameter.getSession());
		if (account != null) {
			final boolean manager = IDbComponentHandle.Utils.isManager(compParameter);
			if (manager || isPostEdit(account, postsBean)) {
				sb.append(HTMLBuilder.SEP);
				sb.append("<a onclick=\"$Actions['topicPagerReplyWindow']('postId=");
				sb.append(postsBean.getId()).append("')\">#(Edit)</a>");
			}
			if (manager && !postsBean.isFirstPost()) {
				sb.append(HTMLBuilder.SEP);
				sb.append("<a class=\"mgrbtn\">#(TopicPagerUtils.2)</a>");
			}
		}
		return sb.toString();
	}

	@Override
	public boolean isShowTags(final ComponentParameter compParameter) {
		return false;
	}
}
