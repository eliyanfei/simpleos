package net.simpleframework.content.bbs;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;

import net.simpleframework.ado.IDataObjectValue;
import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.IQueryEntitySet;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.ado.db.SQLValue;
import net.simpleframework.ado.db.event.TableEntityAdapter;
import net.simpleframework.applets.attention.AttentionUtils;
import net.simpleframework.applets.tag.ITagApplicationModule;
import net.simpleframework.applets.tag.TagBean;
import net.simpleframework.applets.tag.TagUtils;
import net.simpleframework.content.EContentStatus;
import net.simpleframework.content.EContentType;
import net.simpleframework.content.blog.BlogUtils;
import net.simpleframework.content.component.topicpager.DefaultTopicPagerHandle;
import net.simpleframework.content.component.topicpager.PostsBean;
import net.simpleframework.content.component.topicpager.TopicBean;
import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.core.ado.db.Table;
import net.simpleframework.core.bean.IDataObjectBean;
import net.simpleframework.core.bean.ITreeBeanAware;
import net.simpleframework.core.id.ID;
import net.simpleframework.my.space.MySpaceUtils;
import net.simpleframework.organization.IUser;
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
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.PageUtils;
import net.simpleframework.web.page.component.AbstractComponentBean;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ado.IDbComponentHandle;
import net.simpleframework.web.page.component.ui.pager.AbstractTablePagerData;
import net.simpleframework.web.page.component.ui.pager.TablePagerColumn;
import net.simpleos.SimpleosUtil;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class BbsTopicPagerHandle extends DefaultTopicPagerHandle {
	@Override
	public IBbsApplicationModule getApplicationModule() {
		return BbsUtils.applicationModule;
	}

	@Override
	public Class<? extends IDataObjectBean> getEntityBeanClass() {
		return BbsTopic.class;
	}

	@Override
	public EFunctionModule getFunctionModule() {
		return EFunctionModule.bbs;
	}

	@Override
	public <T extends IDataObjectBean> void doEditCallback(final ComponentParameter compParameter, final ITableEntityManager temgr, final T t,
			final Map<String, Object> data, final Class<T> beanClazz) {
		super.doEditCallback(compParameter, temgr, t, data, beanClazz);
		if (beanClazz.isAssignableFrom(BbsTopic.class)) {
			final TopicBean topicBean = (TopicBean) t;

			final BbsForum oCatalog = (BbsForum) data.get("oCatalog");
			final BbsForum mCatalog = (BbsForum) data.get("mCatalog");
			if (oCatalog != null && mCatalog != null) {
				final ITableEntityManager statMgr = getTableEntityManager(compParameter, BbsForumStat.class);
				final BbsForumStat oForumStat = BbsUtils.getForumStat(oCatalog);
				oForumStat.setTopics(oForumStat.getTopics() - 1);
				oForumStat.setMessages(oForumStat.getMessages() - topicBean.getReplies() - 1);
				final BbsForumStat mForumStat = BbsUtils.getForumStat(mCatalog);
				mForumStat.setTopics(mForumStat.getTopics() + 1);
				mForumStat.setMessages(mForumStat.getMessages() + topicBean.getReplies() + 1);
				statMgr.update(oForumStat);
				statMgr.update(mForumStat);
			}

			if (data.get("ttype") == EContentType.recommended) {
				AccountContext.update(OrgUtils.am().queryForObjectById(topicBean.getUserId()), "bbs_typeRecommended", topicBean.getId());
			}
		}
	}

	@Override
	public <T extends IDataObjectBean> void doAddCallback(final ComponentParameter compParameter, final ITableEntityManager temgr, final T t,
			final Map<String, Object> data, final Class<T> beanClazz) {
		super.doAddCallback(compParameter, temgr, t, data, beanClazz);
		final ITableEntityManager statMgr = getTableEntityManager(compParameter, BbsForumStat.class);
		final BbsForumStat forumStat = BbsUtils.getForumStat(BbsUtils.getForum(compParameter));
		final IAccount account = AccountSession.getLogin(compParameter.getSession());
		if (beanClazz.isAssignableFrom(BbsTopic.class)) {
			forumStat.setTopics(forumStat.getTopics() + 1);
			final TopicBean topicBean = (TopicBean) t;
			forumStat.setLastpostId(topicBean.getLastpostId());
			AccountContext.update(account, "bbs_newTopic", topicBean.getId());
			MySpaceUtils.addSapceLog(compParameter, null, EFunctionModule.bbs, topicBean.getId());
		} else {
			final PostsBean postsBean = (PostsBean) t;
			forumStat.setLastpostId(postsBean.getId());
			AccountContext.update(account, "bbs_replyTopic", postsBean.getId());
			MySpaceUtils.addSapceLog(compParameter, null, EFunctionModule.bbs_posts, postsBean.getId());
		}
		forumStat.setMessages(forumStat.getMessages() + 1);
		statMgr.update(forumStat);
	}

	@Override
	public <T extends IDataObjectBean> void doDeleteCallback(final ComponentParameter compParameter, final IDataObjectValue dataObjectValue,
			final Class<T> beanClazz) {
		super.doDeleteCallback(compParameter, dataObjectValue, beanClazz);
		final ITableEntityManager statMgr = getTableEntityManager(compParameter, BbsForumStat.class);
		final ITableEntityManager topicMgr = getTableEntityManager(compParameter, TopicBean.class);
		final ITableEntityManager postMgr = getTableEntityManager(compParameter, PostsBean.class);
		final BbsForumStat forumStat = BbsUtils.getForumStat(BbsUtils.getForum(compParameter));
		if (forumStat != null) {
			final Object[] params = new Object[] { forumStat.getId() };
			forumStat.setTopics(topicMgr.getCount(new ExpressionValue("catalogid=?", params)));
			final ExpressionValue ev2 = new ExpressionValue("topicid in (select id from " + topicMgr.getTablename() + " where catalogid=?)", params);
			forumStat.setMessages(postMgr.getCount(ev2));
			statMgr.update(forumStat);
		}
	}

	@Override
	public String getPostViewUrl(final ComponentParameter compParameter, final TopicBean topicBean) {
		if (topicBean instanceof BbsTopic) {
			return compParameter.wrapContextPath(getApplicationModule().getPostUrl(compParameter, (BbsTopic) topicBean));
		}
		return "";
	}

	@Override
	public Map<String, Object> getUserViewPropertiesEx(final ComponentParameter compParameter, final PostsBean postsBean) {
		final Map<String, Object> properties = super.getUserViewPropertiesEx(compParameter, postsBean);
		final BbsUser bbsUser = BbsUtils.getBbsUser(postsBean.getUserId());
		if (bbsUser != null) {
			properties.put("#(BbsTopicPagerHandle.4)", bbsUser.getTopics());
			properties.put("#(BbsTopicPagerHandle.0)", bbsUser.getMessages());
		}
		return properties;
	}

	@Override
	public Collection<? extends ITreeBeanAware> getMove2Catalogs(final ComponentParameter compParameter, final ITreeBeanAware parent) {
		final ITableEntityManager temgr = getTableEntityManager(compParameter, BbsForum.class);
		final StringBuilder sql = new StringBuilder();
		final ArrayList<Object> al = new ArrayList<Object>();
		if (parent == null) {
			sql.append(Table.nullExpr(temgr.getTable(), "parentid"));
		} else {
			sql.append("parentid=?");
			al.add(parent.getId());
		}
		sql.append(" order by oorder desc");
		return IDataObjectQuery.Utils.toList(temgr.query(new ExpressionValue(sql.toString(), al.toArray()), BbsForum.class));
	}

	@Override
	public String getCatalogIdName(final PageRequestResponse requestResponse) {
		return getApplicationModule().getCatalogIdName(requestResponse);
	}

	@Override
	public BbsForum getCatalogById(final ComponentParameter compParameter, final Object id) {
		return getTableEntityManager(compParameter, BbsForum.class).queryForObjectById(id, BbsForum.class);
	}

	@Override
	public Object getBeanProperty(final ComponentParameter compParameter, final String beanProperty) {
		if ("showCheckbox".equals(beanProperty)) {
			return "false";
		} else if ("pageItems".equals(beanProperty)) {
			return 30;
		}
		return super.getBeanProperty(compParameter, beanProperty);
	}

	@Override
	public Map<String, Object> getFormParameters(final ComponentParameter compParameter) {
		final Map<String, Object> parameters = super.getFormParameters(compParameter);
		putParameter(compParameter, parameters, OrgUtils.um().getUserIdParameterName());
		putParameter(compParameter, parameters, "star");
		putParameter(compParameter, parameters, "time");
		putParameter(compParameter, parameters, "tq");
		putParameter(compParameter, parameters, "c");
		putParameter(compParameter, parameters, "_s_topic");
		putParameter(compParameter, parameters, "_s_author");
		putParameter(compParameter, parameters, "_s_startDate");
		putParameter(compParameter, parameters, "_s_endDate");
		putParameter(compParameter, parameters, "_s_catalog");
		putParameter(compParameter, parameters, ITagApplicationModule._TAG_ID);
		return parameters;
	}

	private void appendSelectImage(final ComponentParameter compParameter, final StringBuilder sb) {
		sb.append("<img style=\"margin-left: 4px; cursor: pointer;\" src=\"");
		sb.append(compParameter.getPageDocument().getPageResourceProvider().getCssResourceHomePath(compParameter));
		sb.append("/images/down.gif\" onclick=\"$Actions['forumSelectDict']();\" />");
	}

	@Override
	public String getNavigateHTML(final ComponentParameter compParameter) {
		final StringBuilder sb = new StringBuilder();
		sb.append("<a href=\"").append(getApplicationModule().getApplicationUrl(compParameter));
		sb.append("\">").append(getApplicationModule().getApplicationText()).append("</a>");
		sb.append(HTMLBuilder.NAV);
		if (ConvertUtils.toBoolean(compParameter.getRequestAttribute("_s_flag"), false)) {
			sb.append(LocaleI18n.getMessage("BbsTopicPagerHandle.2"));
			appendSelectImage(compParameter, sb);
		} else {
			final BbsForum forum = BbsUtils.getForum(compParameter);
			if (forum != null) {
				final String topicUrl = getApplicationModule().getTopicUrl(compParameter, forum);
				sb.append("<a href=\"").append(topicUrl);
				sb.append("\">").append(forum.getText()).append("</a>");
				appendSelectImage(compParameter, sb);
				if (StringUtils.hasText(compParameter.getRequestParameter(getIdParameterName(compParameter)))) {
					sb.append(HTMLBuilder.NAV).append(LocaleI18n.getMessage("BbsTopicPagerHandle.3"));
				}

				final TagBean tagBean = TagUtils.getTagBeanById(compParameter.getRequestParameter(ITagApplicationModule._TAG_ID));
				if (tagBean != null) {
					sb.append(HTMLBuilder.NAV);
					sb.append(tagBean.getTagText());
					TagUtils.updateViews(compParameter, tagBean);
				}
			} else {
				sb.append(LocaleI18n.getMessage("BbsTopicPagerHandle.2"));
				appendSelectImage(compParameter, sb);
				sb.append(" - ");
				final String userId, star, time, tq;
				if (StringUtils.hasText(userId = compParameter.getRequestParameter(OrgUtils.um().getUserIdParameterName()))) {
					sb.append(OrgUtils.um().queryForObjectById(userId));
				} else if (StringUtils.hasText(star = compParameter.getRequestParameter("star"))) {
					if ("-1".equals(star)) {
						sb.append(LocaleI18n.getMessage("bbs_topic_view.2"));
					} else if ("1".equals(star)) {
						sb.append(1);
					} else if ("2".equals(star)) {
						sb.append(2);
					} else if ("3".equals(star)) {
						sb.append(3);
					} else if ("4".equals(star)) {
						sb.append(4);
					} else if ("5".equals(star)) {
						sb.append(5);
					}
				} else if (StringUtils.hasText(time = compParameter.getRequestParameter("time"))) {
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
				} else if ("recommended".equals(tq = compParameter.getRequestParameter("tq"))) {
					sb.append(LocaleI18n.getMessage("ContentUtils.recommended"));
				} else if ("attention".equals(tq)) {
					sb.append(LocaleI18n.getMessage("ContentUtils.attention"));
				} else {
					sb.append(LocaleI18n.getMessage("ContentUtils.all"));
				}
			}
		}
		wrapNavImage(compParameter, sb);
		return sb.toString();
	}

	@Override
	public IDataObjectQuery<?> createDataObjectQuery(final ComponentParameter compParameter) {
		final String c = PageUtils.toLocaleString(compParameter.getRequestParameter("c"));
		if (StringUtils.hasText(c)) {
			return createLuceneManager(compParameter).getLuceneQuery(c);
		}
		final ITableEntityManager topic_mgr = getTableEntityManager(compParameter);
		final StringBuilder sql = new StringBuilder();
		final ArrayList<Object> al = new ArrayList<Object>();
		final String tag = compParameter.getRequestParameter(ITagApplicationModule._TAG_ID);
		if (StringUtils.hasText(tag)) {
			appendTagsSQL(compParameter, sql, al, tag);
			sql.append(" order by a.status,a.ttop desc, a.lastpostupdate desc");
			return topic_mgr.query(new SQLValue(sql.toString(), al.toArray()), getEntityBeanClass());
		}
		final String tq = compParameter.getRequestParameter("tq");
		if ("attention".equals(tq)) {
			return topic_mgr.query(AttentionUtils.attentionSQLValue(compParameter, getFunctionModule()), getEntityBeanClass());
		}
		sql.append("1=1");
		final String _s_topic = PageUtils.toLocaleString(compParameter.getRequestParameter("_s_topic"));
		final String _s_author = PageUtils.toLocaleString(compParameter.getRequestParameter("_s_author"));
		final String _s_startDate = compParameter.getRequestParameter("_s_startDate");
		final String _s_endDate = compParameter.getRequestParameter("_s_endDate");
		final String _s_catalog = compParameter.getRequestParameter("_s_catalog");
		if (StringUtils.hasText(_s_topic) || StringUtils.hasText(_s_author) || StringUtils.hasText(_s_startDate) || StringUtils.hasText(_s_endDate)
				|| StringUtils.hasText(_s_catalog)) {
			if (StringUtils.hasText(_s_topic)) {
				sql.append(" and topic like '%").append(_s_topic).append("%'");
			}
			if (StringUtils.hasText(_s_startDate)) {
				sql.append(" and createdate>?");
				final Date startDate = ConvertUtils.toDate(_s_startDate, "yyyy-MM-dd");
				al.add(startDate);
			}
			if (StringUtils.hasText(_s_endDate)) {
				sql.append(" and createdate<?");
				final Date endDate = ConvertUtils.toDate(_s_endDate + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
				al.add(endDate);
			}
			if (StringUtils.hasText(_s_author)) {
				sql.append(" and userid in (select id from ");
				sql.append(OrgUtils.um().tblname()).append(" where text=?)");
				al.add(_s_author);
			}
			if (StringUtils.hasText(_s_catalog)) {
				sql.append(" and (");
				int i = 0;
				for (final String forumId : StringUtils.split(_s_catalog)) {
					if (i++ > 0) {
						sql.append(" or ");
					}
					sql.append("catalogid=").append(forumId);
				}
				sql.append(")");
			}
			compParameter.setRequestAttribute("_s_flag", Boolean.TRUE);
		} else {
			final BbsForum forum = BbsUtils.getForum(compParameter);
			if (forum != null) {
				sql.append(" and catalogid=?");
				al.add(forum.getId());
			} else {
				final String userId, star, time;
				if (StringUtils.hasText(userId = compParameter.getRequestParameter(OrgUtils.um().getUserIdParameterName()))) {
					if ("posts".equals(tq)) {
						sql.setLength(0);
						sql.append(BbsUtils.mypostsSql());
						sql.append(" order by t.status,t.ttop desc, t.lastpostupdate desc");
						return topic_mgr.query(new SQLValue(sql.toString(), new Object[] { userId }), getEntityBeanClass());
					} else {
						sql.append(" and userid=?");
					}
					al.add(userId);
				} else if ("recommended".equals(tq)) {
					sql.append(" and ttype=?");
					al.add(EContentType.recommended);
				} else if (StringUtils.hasText(star = compParameter.getRequestParameter("star"))) {
					final int starInt = ConvertUtils.toInt(star, 0);
					if (starInt > 0) {
						sql.append(" and star=?");
						al.add(starInt);
					} else if (starInt < 0) {
						sql.append(" and star>0");
					}
				} else if (StringUtils.hasText(time = compParameter.getRequestParameter("time"))) {
					final Calendar cal = DateUtils.getTimeCalendar(time);
					sql.append(" and createdate>?");
					al.add(cal.getTime());
				}
			}
		}
		if (!SimpleosUtil.isManage(compParameter, BbsUtils.applicationModule)) {
			sql.append(" and status=?");
			al.add(EContentStatus.publish);
		}
		sql.append(" order by status ,ttop desc, lastpostupdate desc");
		final ExpressionValue ev2 = new ExpressionValue(sql.toString(), al.toArray());
		return topic_mgr.query(ev2, getEntityBeanClass());
	}

	@Override
	public AbstractTablePagerData createTablePagerData(final ComponentParameter compParameter) {
		return new TopicTablePagerData(compParameter) {
			String userId = compParameter.getParameter(OrgUtils.um().getUserIdParameterName());
			IUser user = SimpleosUtil.getLoginUser(compParameter);

			@Override
			public Map<String, TablePagerColumn> getTablePagerColumns() {
				final Map<String, TablePagerColumn> columns = cloneTablePagerColumns();
				if (!IDbComponentHandle.Utils.isManager(compParameter)) {
				}
				if (!user.getId().equals2(userId)) {
					columns.remove("action");
				}
				if (BbsUtils.getForum(compParameter) == null) {
					//					final TablePagerColumn forum = new TablePagerColumn("forum");
					//					forum.setColumnSqlName("catalogId");
					//					forum.setColumnText(LocaleI18n.getMessage("BbsTopicPagerHandle.1"));
					//					forum.setHeaderStyle("width: 150px;");
					//					forum.setStyle("width: 150px;text-align: center;");
					//					forum.setSeparator(true);
					//					putColumn(columns, 2, "forum", forum);
					if (StringUtils.hasText(compParameter.getRequestParameter(OrgUtils.um().getUserIdParameterName()))
							&& !ConvertUtils.toBoolean(compParameter.getRequestParameter("r"), false)) {
						columns.remove("author");
					}
				}
				return columns;
			}

			@Override
			protected Map<Object, Object> getRowData(final Object dataObject) {
				final Map<Object, Object> rowData = super.getRowData(dataObject);
				//				final BbsForum forum = getCatalogById(compParameter, ((BbsTopic) dataObject).getCatalogId());
				//				if (forum != null) {
				//					final StringBuilder sb = new StringBuilder();
				//					sb.append("<a class=\"a2\" href=\"").append(getApplicationModule().getTopicUrl(compParameter, forum)).append("\">")
				//							.append(forum.getText()).append("</a>");
				//					rowData.put("forum", sb.toString());
				//				}
				return rowData;
			}
		};
	}

	@Override
	public void handleCreated(final PageRequestResponse requestResponse, final AbstractComponentBean componentBean) {
		super.handleCreated(requestResponse, componentBean);
		BbsUtils.getTableEntityManager(PostsBean.class).addListener(new TableEntityAdapter() {
			final ITableEntityManager userMgr = BbsUtils.getTableEntityManager(BbsUser.class);
			final ITableEntityManager topicMgr = BbsUtils.getTableEntityManager(TopicBean.class);
			final ITableEntityManager postMgr = BbsUtils.getTableEntityManager(PostsBean.class);

			@Override
			public void afterInsert(final ITableEntityManager manager, final Object[] objects) {
				for (final Object object : objects) {
					final PostsBean postsBean = (PostsBean) object;
					final BbsUser bbsUser = BbsUtils.getBbsUser(postsBean.getUserId());
					if (postsBean.isFirstPost()) {
						bbsUser.setTopics(bbsUser.getTopics() + 1);
					}
					bbsUser.setMessages(bbsUser.getMessages() + 1);
					userMgr.update(bbsUser);
				}
			}

			@Override
			public void beforeDelete(final ITableEntityManager manager, final IDataObjectValue dataObjectValue) {
				final IQueryEntitySet<PostsBean> qs = postMgr.query(dataObjectValue, PostsBean.class);
				final HashSet<ID> userIds = new HashSet<ID>();
				PostsBean post;
				while ((post = qs.next()) != null) {
					userIds.add(post.getUserId());
				}
				dataObjectValue.setAttribute("userIds", userIds);
			}

			@Override
			public void afterDelete(final ITableEntityManager manager, final IDataObjectValue dataObjectValue) {
				@SuppressWarnings("unchecked")
				final HashSet<ID> userIds = (HashSet<ID>) dataObjectValue.getAttribute("userIds");
				for (final ID id : userIds) {
					final BbsUser bbsUser = BbsUtils.getBbsUser(id);
					final ExpressionValue ev2 = new ExpressionValue("userId=?", new Object[] { id });
					bbsUser.setTopics(topicMgr.getCount(ev2));
					bbsUser.setMessages(postMgr.getCount(ev2));
					userMgr.update(bbsUser);
				}
			}
		});
	}

	@Override
	public String getTopicUrl(final PageRequestResponse requestResponse) {
		return requestResponse.wrapContextPath(getApplicationModule().getTopicUrl(requestResponse, (BbsForum) null));
	}

	@Override
	public String getBlogUrl(final PageRequestResponse requestResponse, final IUser user) {
		return requestResponse.wrapContextPath(BlogUtils.applicationModule.getBlogUrl(requestResponse, user));
	}

	@Override
	public boolean isShowTags(final ComponentParameter compParameter) {
		final BbsForum forum = BbsUtils.getForum(compParameter);
		return forum != null && forum.isShowTags();
	}
}
