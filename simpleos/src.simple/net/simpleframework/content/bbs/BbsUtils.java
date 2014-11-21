package net.simpleframework.content.bbs;

import java.util.ArrayList;
import java.util.Calendar;

import net.itsite.utils.StringsUtils;
import net.simpleframework.ado.DataObjectManagerUtils;
import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.IQueryEntitySet;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.ado.db.SQLValue;
import net.simpleframework.ado.db.UniqueValue;
import net.simpleframework.applets.attention.AttentionUtils;
import net.simpleframework.content.ContentUtils;
import net.simpleframework.content.EContentStatus;
import net.simpleframework.content.EContentType;
import net.simpleframework.content.component.catalog.Catalog;
import net.simpleframework.content.component.catalog.CatalogOwner;
import net.simpleframework.content.component.topicpager.ETopicQuery;
import net.simpleframework.content.component.topicpager.PostsBean;
import net.simpleframework.content.component.topicpager.TopicPagerUtils;
import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.core.ado.db.Table;
import net.simpleframework.core.id.ID;
import net.simpleframework.organization.EMemberType;
import net.simpleframework.organization.IUser;
import net.simpleframework.organization.OrgUtils;
import net.simpleframework.organization.account.IAccount;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.util.DateUtils;
import net.simpleframework.util.DateUtils.TimeDistance;
import net.simpleframework.util.HTMLUtils;
import net.simpleframework.util.HTTPUtils;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.EFunctionModule;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.component.AbstractComponentBean;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class BbsUtils {

	public static IBbsApplicationModule applicationModule;

	public static String deployPath;

	public static String getCssPath(final PageRequestResponse requestResponse) {
		final StringBuilder sb = new StringBuilder();
		sb.append(deployPath).append("css/").append(applicationModule.getSkin(requestResponse));
		return sb.toString();
	}

	public static ITableEntityManager getTableEntityManager(final Class<?> beanClazz) {
		return DataObjectManagerUtils.getTableEntityManager(applicationModule, beanClazz);
	}

	public static ITableEntityManager getTableEntityManager() {
		return DataObjectManagerUtils.getTableEntityManager(applicationModule);
	}

	public static BbsForum getForumById(final Object id) {
		return getTableEntityManager(BbsForum.class).queryForObjectById(id, BbsForum.class);
	}

	public static BbsForum getForum(final PageRequestResponse requestResponse) {
		return getForumById(requestResponse.getRequestParameter(applicationModule.getCatalogIdName(requestResponse)));
	}

	public static IDataObjectQuery<BbsForum> forums(final Object parentId) {
		final ITableEntityManager tblmgr = getTableEntityManager(BbsForum.class);
		if (parentId == null) {
			return tblmgr.query(new ExpressionValue(Table.nullExpr(tblmgr.getTable(), "parentid") + " order by oorder desc"), BbsForum.class);
		} else {
			return tblmgr.query(new ExpressionValue("parentid=? order by oorder desc", new Object[] { parentId }), BbsForum.class);
		}
	}

	public static BbsUser getBbsUser(final ID userId) {
		final ITableEntityManager tblmgr = getTableEntityManager(BbsUser.class);
		BbsUser user = tblmgr.queryForObjectById(userId, BbsUser.class);
		if (user == null) {
			user = new BbsUser();
			user.setId(userId);
			tblmgr.insert(user);
		}
		return user;
	}

	public static BbsForumStat getForumStat(final BbsForum forum) {
		if (forum == null) {
			return null;
		}
		final ITableEntityManager tblmgr = getTableEntityManager(BbsForumStat.class);
		BbsForumStat forumStat = tblmgr.queryForObjectById(forum.getId(), BbsForumStat.class);
		if (forumStat == null) {
			forumStat = new BbsForumStat();
			forumStat.setId(forum.getId());
			tblmgr.insert(forumStat);
		}
		return forumStat;
	}

	public static IQueryEntitySet<?> queryTopics(final PageRequestResponse requestResponse, final Object forum, final EContentType contentType,
			final boolean star, final TimeDistance timeDistance, final ETopicQuery topicQuery, final int order) {
		return queryTopics(requestResponse, forum, contentType, star, timeDistance, topicQuery, order, -1);
	}

	public static IQueryEntitySet<?> queryTopics(final PageRequestResponse requestResponse, final Object forum, final EContentType contentType,
			final boolean star, final TimeDistance timeDistance, final ETopicQuery topicQuery, final int order, final int _tab_param) {
		final ArrayList<Object> al = new ArrayList<Object>();
		final StringBuilder sql = new StringBuilder();
		if (forum != null) {
			sql.append("catalogid=?");
			al.add(forum);
		} else {
			sql.append("1=1");
		}
		if (timeDistance != null) {
			sql.append(" and createdate>?");
			al.add(DateUtils.getTimeCalendar(timeDistance).getTime());
		}
		if (_tab_param == 3) {
			sql.append(" and lastPostUpdate>?");
			al.add(DateUtils.getTimeCalendar(TimeDistance.month).getTime());
		}
		if (contentType != null) {
			sql.append(" and ttype=?");
			al.add(contentType);
		}
		if (_tab_param == 1) {
			sql.append(" and star>0");
		}
		if (star) {
			sql.append(" and star>0");
		}
		sql.append(" and status=?");
		al.add(EContentStatus.publish);
		final ITableEntityManager topic_mgr = getTableEntityManager(BbsTopic.class);
		final IAccount login = ContentUtils.getAccountAware().getAccount(requestResponse);
		if (login != null) {
			if (topicQuery == ETopicQuery.onlyTopic) { // 我发布的
				sql.append(" and userid=?");
				al.add(login.getId());
			} else if (topicQuery == ETopicQuery.postsAndTopic) { // 我参与的
				sql.setLength(0);
				sql.append(mypostsSql());
				if (order == 0) {
					sql.append(" order by t.createdate desc");
				} else if (order == 1) {
					sql.append(" order by t.views desc");
				}
				return topic_mgr.query(new SQLValue(sql.toString(), new Object[] { login.getId() }), applicationModule.getEntityBeanClass());
			}
		} else if (topicQuery != null) {
			sql.append(" and 1=2");
		}
		if (_tab_param == 0) {
			sql.append(" order by createdate desc");
		} else if (_tab_param == 2) {
			sql.append(" order by views desc");
		} else if (_tab_param == 3) {
			sql.append(" order by replies desc");
		} else if (order == 0) {
			sql.append(" order by createdate desc");
		} else if (order == 1) {
			sql.append(" order by views desc");
		}
		return topic_mgr.query(new ExpressionValue(sql.toString(), al.toArray()), applicationModule.getEntityBeanClass());
	}

	static String mypostsSql() {
		final StringBuilder sb = new StringBuilder();
		sb.append("select distinct t.* from ").append(getTableEntityManager(BbsTopic.class).getTablename());
		sb.append(" t right join ").append(getTableEntityManager(PostsBean.class).getTablename());
		sb.append(" a on a.topicid=t.id where a.userid=?");
		return sb.toString();
	}

	static void doStatRebuild() {
		final ITableEntityManager stat_mgr = getTableEntityManager(BbsForumStat.class);
		final ITableEntityManager topic_mgr = getTableEntityManager(BbsTopic.class);
		final ITableEntityManager user_mgr = getTableEntityManager(BbsUser.class);

		final String stat_name = stat_mgr.getTablename();
		final String topic_name = topic_mgr.getTablename();
		final String user_name = user_mgr.getTablename();
		final String bbs_posts = getTableEntityManager(PostsBean.class).getTablename();

		final StringBuilder sql = new StringBuilder();
		sql.append("update ").append(topic_name).append(" t set replies=");
		sql.append("(select count(id) from ").append(bbs_posts);
		sql.append(" where topicid=t.id) - 1");
		topic_mgr.execute(new SQLValue(sql.toString()));

		sql.setLength(0);
		sql.append("update ").append(topic_name).append(" t set attentions=");
		sql.append("(select count(id) from ").append(AttentionUtils.getTableEntityManager().getTablename());
		sql.append(" where attentionid=t.id and vtype=").append(EFunctionModule.bbs.ordinal()).append(")");
		topic_mgr.execute(new SQLValue(sql.toString()));
		topic_mgr.reset();

		sql.setLength(0);
		sql.append("update ").append(stat_name).append(" t set topics=");
		sql.append("(select count(id) from ").append(topic_name);
		sql.append(" where catalogid=t.id)");
		stat_mgr.execute(new SQLValue(sql.toString()));

		sql.setLength(0);
		sql.append("update ").append(stat_name).append(" t set messages=");
		sql.append("topics + (select sum(replies) from ").append(topic_name);
		sql.append(" where catalogid=t.id)");
		stat_mgr.execute(new SQLValue(sql.toString()));
		stat_mgr.reset();

		sql.setLength(0);
		sql.append("update ").append(user_name).append(" t set topics=(select count(id) from ");
		sql.append(topic_name).append(" where t.id=userid), messages=(select count(id) from ");
		sql.append(bbs_posts).append(" where t.id=userid)");
		user_mgr.execute(new SQLValue(sql.toString()));
		user_mgr.reset();
	}

	public static void initTopicPager(final PageRequestResponse requestResponse) {
		final AbstractComponentBean componentBean = BbsUtils.applicationModule.getComponentBean(requestResponse);
		if (componentBean != null) {
			HTTPUtils.putParameter(requestResponse.request, TopicPagerUtils.BEAN_ID, componentBean.hashId());
		}
	}

	/************************** jsp utils **************************/
	public static String forumView(final PageRequestResponse requestResponse) {
		final StringBuilder sb = new StringBuilder();
		sb.append("<table style=\"width: 100%;\" cellspacing=\"1\"><tr>");
		sb.append("<th class=\"t_forums\">#(bbs_forum_view.1)</th>");
		sb.append("<th class=\"t_topics\">#(bbs_forum_view.2)</th>");
		sb.append("<th class=\"t_messages\">#(bbs_forum_view.3)</th>");
		sb.append("<th class=\"t_last_message\">#(bbs_forum_view.4)</th>");
		sb.append("</tr>");
		final ITableEntityManager postsmgr = getTableEntityManager(PostsBean.class);
		final IDataObjectQuery<BbsForum> doq = forums(null);
		BbsForum item;
		while ((item = doq.next()) != null) {
			sb.append("<tr><td colspan=\"4\" class=\"ctitle\">");
			if (OrgUtils.isMember(applicationModule.getManager(requestResponse), requestResponse.getSession())) {
				sb.append("<a href=\"");
				sb.append(applicationModule.getTopicUrl(requestResponse, item));
				sb.append("\">").append(item.getText()).append("</a>");
			} else {
				sb.append(item.getText());
			}
			sb.append("</td></tr>");
			final IDataObjectQuery<BbsForum> doq2 = BbsUtils.forums(item.getId());
			BbsForum item2;
			while ((item2 = doq2.next()) != null) {
				final BbsForumStat stat = getForumStat(item2);
				final PostsBean postsBean = postsmgr.queryForObject(new UniqueValue(stat.getLastpostId()), PostsBean.class);
				sb.append("<tr><td class=\"c_forums\">");
				sb.append("<table style=\"width: 100%;\" cellpadding=\"0\" cellspacing=\"0\"><tr>");
				sb.append("<td class=\"icon\"><img src=\"");
				sb.append(getForumIcon(requestResponse, item2));
				sb.append("\" /></td><td>");
				sb.append("<div class=\"c2\">");
				sb.append("<a href=\"");
				sb.append(applicationModule.getTopicUrl(requestResponse, item2));
				sb.append("\">").append(item2.getText()).append("</a>");
				final int c = todayTopics(item2);
				if (c > 0) {
					sb.append("<span class=\"today\">(#(bbs_forum_view.5)&nbsp;<span class=\"important-tip\">");
					sb.append(c).append("</span>)</span>");
				}
				sb.append("</div><div class=\"c3\">");
				sb.append(StringUtils.blank(HTMLUtils.convertHtmlLines(item2.getDescription())));
				sb.append("</div>");
				final String o = BbsUtils.getForumOwnerText(requestResponse, item2);
				if (StringUtils.hasText(o)) {
					sb.append("<div class=\"c4\">#(BbsUtils.0)：").append(o).append("</div>");
				}
				sb.append("</td>");
				sb.append("</tr></table></td>");
				sb.append("<td class=\"c_topics\">");
				sb.append(stat != null ? stat.getTopics() : 0).append("</td>");
				sb.append("<td class=\"c_messages\">");
				sb.append(stat != null ? stat.getMessages() : 0).append("</td>");
				sb.append("<td class=\"c_last_message\">");
				if (postsBean != null) {
					sb.append("<a>").append(postsBean.getUserText()).append("</a>");
					sb.append("<div style=\"margin-top: 3px; color: #999;\">");
					sb.append(ConvertUtils.toDateString(postsBean.getLastUpdate()));
					sb.append("</div>");
				}
				sb.append("</td>");
				sb.append("</tr>");
			}
		}
		sb.append("</table>");
		return sb.toString();
	}

	public static String forumView2(final PageRequestResponse requestResponse) {
		final StringBuilder sb = new StringBuilder();
		final IDataObjectQuery<BbsForum> doq = forums(null);
		BbsForum item;
		final ITableEntityManager postsmgr = getTableEntityManager(PostsBean.class);
		int i = 0;
		while ((item = doq.next()) != null) {
			sb.append("<div class=\"ctitle");
			sb.append(i++ == 0 ? " ctitle_border_radius\"" : "\" style=\"border-top: 1px solid #ccc;\"").append(">");
			sb.append("<img src=\"" + getCssPath(requestResponse) + "/images/toggle.png\"/>");
			if (OrgUtils.isMember(applicationModule.getManager(requestResponse), requestResponse.getSession())) {
				sb.append("<a href=\"");
				sb.append(applicationModule.getTopicUrl(requestResponse, item));
				sb.append("\">").append(item.getText()).append("</a>");
			} else {
				sb.append(item.getText());
			}

			sb.append("</div><div style=\"border-top: 1px solid #ccc;\" class='c_list'>");
			final IDataObjectQuery<BbsForum> doq2 = forums(item.getId());
			BbsForum item2;
			while ((item2 = doq2.next()) != null) {
				final BbsForumStat stat = getForumStat(item2);
				final PostsBean postsBean = postsmgr.queryForObject(new UniqueValue(stat.getLastpostId()), PostsBean.class);
				sb.append("<span class=\"c_forums2\">");
				sb.append("<table style=\"width: 100%; height: 100%;\">");
				sb.append("<tr><td class=\"icon\">");
				sb.append("<div class='icon_b icon_forum'/>");
				//				sb.append("<img src=\"").append(getForumIcon(requestResponse, item2)).append("\" />");
				sb.append("</td><td>");
				sb.append("<div class=\"c2\">");
				sb.append("<a href=\"").append(applicationModule.getTopicUrl(requestResponse, item2)).append("\">").append(item2.getText())
						.append("</a>");
				final int c = todayTopics(item2);
				if (c > 0) {
					sb.append("<span class=\"today\">").append("(<span class=\"important-tip\">").append(c).append("</span>)</span>");
				}
				sb.append("</div><div class='c22'><div>");
				sb.append("<span title=\"#(bbs_forum_view.2)/#(bbs_forum_view.3)\">主题：");
				sb.append(stat != null ? stat.getTopics() : 0).append(" , 贴数：").append(stat != null ? stat.getMessages() : 0).append("</span>");
				sb.append("</div>");
				sb.append("<div>最后发表：");
				if (postsBean != null) {
					sb.append(DateUtils.getRelativeDate(postsBean.getLastUpdate()));
				} else {
					sb.append("?");
				}
				sb.append("</div>");
				final String o = getForumOwnerText(requestResponse, item2);
				sb.append("<div>#(BbsUtils.0)：").append(StringsUtils.isNotBlank1(o) ? o : "?").append("</div></div>");
				sb.append("</td></tr></table></span>");
				final String desc = item2.getDescription();
				if (StringUtils.hasText(desc)) {
					sb.append("<div style=\"display: none;\">");
					sb.append(StringUtils.blank(HTMLUtils.convertHtmlLines(desc)));
					sb.append("</div>");
				}
			}
			sb.append("</div>");
		}
		return sb.toString();
	}

	private static String getForumIcon(final PageRequestResponse requestResponse, final BbsForum forum) {
		if (applicationModule.isForumNewState(forum)) {
			return getCssPath(requestResponse) + "/images/forum.png";
		} else {
			return getCssPath(requestResponse) + "/images/forum.png";
		}
	}

	private static int todayTopics(final BbsForum forum) {
		final Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		final Calendar calendar2 = Calendar.getInstance();
		calendar2.setTimeInMillis(calendar.getTimeInMillis());
		calendar2.add(Calendar.DATE, 1);
		return getTableEntityManager(BbsTopic.class).getCount(
				new ExpressionValue("catalogid=? and (createdate>? and createdate<?)", new Object[] { forum.getId(), calendar, calendar2 }));
	}

	private static String getForumOwnerText(final PageRequestResponse requestResponse, final Catalog catalog) {
		final IDataObjectQuery<CatalogOwner> doq = getTableEntityManager(CatalogOwner.class).query(
				new ExpressionValue("catalogId=?", new Object[] { catalog.getId() }), CatalogOwner.class);
		final StringBuilder sb = new StringBuilder();
		CatalogOwner catalogOwner;
		while ((catalogOwner = doq.next()) != null) {
			if (catalogOwner.getOwnerType() == EMemberType.user) {
				final IUser user = OrgUtils.um().queryForObjectById(catalogOwner.getOwnerId());
				if (user != null) {
					sb.append("; ");
					sb.append(ContentUtils.getAccountAware().wrapAccountHref(requestResponse, user.account()));
				}
			}
		}
		if (sb.length() > 1) {
			return sb.substring(2);
		} else {
			return "";
		}
	}
}
