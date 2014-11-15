package net.simpleframework.content.bbs;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.a.ItSiteUtil;
import net.simpleframework.ado.IDataObjectValue;
import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.IQueryEntitySet;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.ado.db.event.TableEntityAdapter;
import net.simpleframework.content.AbstractContentApplicationModule;
import net.simpleframework.content.ContentUtils;
import net.simpleframework.content.component.catalog.CatalogOwner;
import net.simpleframework.content.component.remark.RemarkItem;
import net.simpleframework.content.component.topicpager.ETopicQuery;
import net.simpleframework.content.component.topicpager.PostsBean;
import net.simpleframework.content.component.topicpager.PostsTextBean;
import net.simpleframework.content.component.topicpager.TopicBean;
import net.simpleframework.content.component.topicpager.TopicPagerBean;
import net.simpleframework.core.ExecutorRunnable;
import net.simpleframework.core.IInitializer;
import net.simpleframework.core.ITaskExecutorAware;
import net.simpleframework.core.ado.db.Table;
import net.simpleframework.organization.IUser;
import net.simpleframework.organization.OrgUtils;
import net.simpleframework.organization.account.AccountSession;
import net.simpleframework.organization.account.IAccount;
import net.simpleframework.organization.component.login.LoginUtils;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.util.DateUtils;
import net.simpleframework.util.HTMLBuilder;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.IWebApplicationModule;
import net.simpleframework.web.WebUtils;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.component.AbstractComponentBean;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.portal.module.PortalModuleRegistryFactory;
import net.simpleframework.web.page.component.ui.tabs.EMatchMethod;
import net.simpleframework.web.page.component.ui.tabs.TabHref;
import net.simpleframework.web.page.component.ui.tabs.TabsUtils;
import net.simpleframework.web.page.component.ui.tree.AbstractTreeBean;
import net.simpleframework.web.page.component.ui.tree.AbstractTreeNode;
import net.simpleframework.web.page.component.ui.tree.TreeNode;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class DefaultBbsApplicationModule extends AbstractContentApplicationModule implements IBbsApplicationModule {
	private static final String FORUM_ID = "forumId";

	@Override
	protected void putTables(final Map<Class<?>, Table> tables) {
		tables.put(BbsForum.class, new Table("simple_bbs_forum"));
		tables.put(CatalogOwner.class, new Table("simple_bbs_forum_owner", new String[] { "catalogid", "ownertype", "ownerid" }));
		tables.put(BbsForumStat.class, new Table("simple_bbs_forum_stat"));
		tables.put(BbsUser.class, new Table("simple_bbs_user"));
		tables.put(BbsTopic.class, new Table("simple_bbs_topic"));
		tables.put(PostsBean.class, new Table("simple_bbs_posts"));
		tables.put(PostsTextBean.class, new Table("simple_bbs_posts_text"));
	}

	@Override
	public String getCatalogIdName(final PageRequestResponse requestResponse) {
		return FORUM_ID;
	}

	@Override
	public String getTopicUrl(final PageRequestResponse requestResponse, final BbsForum forum) {
		return null;
	}

	@Override
	public String getTopicUrl2(final PageRequestResponse requestResponse, final IUser user, final ETopicQuery topicQuery) {
		String url = getTopicUrl(requestResponse, null);
		if (user != null) {
			if (topicQuery == ETopicQuery.onlyTopic || topicQuery == ETopicQuery.postsAndTopic) {
				url = WebUtils.addParameters(url, OrgUtils.um().getUserIdParameterName() + "=" + user.getId());
				if (topicQuery == ETopicQuery.postsAndTopic) {
					url = WebUtils.addParameters(url, "tq=posts");
				}
			}
		}
		return url;
	}

	@Override
	public String getPostUrl(final ComponentParameter compParameter, final BbsTopic topic) {
		return null;
	}

	@Override
	public String getUsersUrl(final PageRequestResponse requestResponse) {
		return null;
	}

	@Override
	public Collection<? extends AbstractTreeNode> getForumDictTree(final ComponentParameter compParameter, final AbstractTreeNode treeNode) {
		final AbstractTreeBean treeBean = (AbstractTreeBean) compParameter.componentBean;
		final ITableEntityManager temgr = BbsUtils.getTableEntityManager(BbsForum.class);
		final ArrayList<Object> al = new ArrayList<Object>();
		final StringBuilder sql = new StringBuilder();
		if (treeNode == null) {
			sql.append(Table.nullExpr(temgr.getTable(), "parentid"));
		} else {
			sql.append("parentid=?");
			al.add(((BbsForum) treeNode.getDataObject()).getId());
		}

		final String imgBase = BbsUtils.getCssPath(compParameter) + "/images/";
		final String treeName = treeBean.getName();
		final boolean bLink = !"forumInsertTree".equals(treeName);
		final boolean bAdd = "forumAddSelectTree".equals(treeName);
		final Collection<TreeNode> nodes = new ArrayList<TreeNode>();
		final IQueryEntitySet<BbsForum> qs = temgr.query(new ExpressionValue(sql.toString() + " order by oorder desc", al.toArray()), BbsForum.class);
		BbsForum forum;
		final ComponentParameter nComponentParameter = ComponentParameter.get(compParameter, getComponentBean(compParameter));
		while ((forum = qs.next()) != null) {
			final TreeNode node = new TreeNode(treeBean, treeNode, forum);
			if (bLink) {
				node.setOpened(true);
				if (node.getParent() != null) {
					String url = getTopicUrl(nComponentParameter, forum);
					if (bAdd) {
						url = WebUtils.addParameters(url, "op_act=add");
					}
					final StringBuilder sb = new StringBuilder();
					sb.append("<a style=\"vertical-align: middle;\" href=\"").append(compParameter.wrapContextPath(url)).append("\">")
							.append(forum.getText()).append("</a>");
					node.setText(sb.toString());
					final BbsForumStat stat = BbsUtils.getForumStat(forum);
					if (stat != null) {
						node.setPostfixText(WebUtils.enclose(stat.getTopics()));
					}
				} else {
					node.setJsDblclickCallback("return false;");
				}
			}
			if (node.getParent() != null) {
				node.setImage(imgBase + "forum_link.png");
			} else {
				node.setImage(imgBase + "forum_c.png");
			}
			nodes.add(node);
		}
		return nodes;
	}

	@Override
	public boolean isForumNewState(final BbsForum forum) {
		final Calendar cal = Calendar.getInstance();
		cal.add(Calendar.HOUR, -12);
		final ExpressionValue ev = new ExpressionValue("catalogid=? and createdate>?", new Object[] { forum.getId(), cal.getTime() });
		return BbsUtils.getTableEntityManager(TopicBean.class).query(ev).next() != null;
	}

	@Override
	public String getApplicationText() {
		return StringUtils.text(super.getApplicationText(), LocaleI18n.getMessage("DefaultBbsApplicationModule.0"));
	}

	@Override
	public String getManager(final Object... params) {
		return sj_bbs_manager;
	}

	static final String sj_bbs_manager = "bbs_manager";

	static final String deployName = "content/bbs";

	@Override
	public void init(final IInitializer initializer) {
		super.init(initializer);
		doInit(BbsUtils.class, deployName);

		PortalModuleRegistryFactory.regist(BbsPortalModule.class, "bbs", getApplicationText(), LocaleI18n.getMessage("BbsUtils.1"),
				BbsUtils.deployPath + "images/bbs.png", LocaleI18n.getMessage("BbsUtils.2"));

		createManagerJob(sj_bbs_manager, LocaleI18n.getMessage("BbsUtils.bbs_manager"));

		((ITaskExecutorAware) getApplication()).getTaskExecutor().addScheduledTask(ItSiteUtil.get0Time() + 30, DateUtils.DAY_PERIOD,
				new ExecutorRunnable() {
					@Override
					public void task() {
						BbsUtils.doStatRebuild();
					}
				});

		OrgUtils.um().addListener(new TableEntityAdapter() {
			@Override
			public void afterDelete(final ITableEntityManager manager, final IDataObjectValue dataObjectValue) {
				BbsUtils.getTableEntityManager(BbsUser.class).delete(dataObjectValue);
			}
		});
	}

	@Override
	public TopicPagerBean getComponentBean(final PageRequestResponse requestResponse) {
		return (TopicPagerBean) AbstractComponentBean.getComponentBeanByName(requestResponse, BbsUtils.deployPath + "jsp/bbs_topic_view.xml",
				"bbsTopicPager");
	}

	@Override
	public Class<?> getEntityBeanClass() {
		return BbsTopic.class;
	}

	@Override
	public String getPagerHandleClass() {
		return StringUtils.text(super.getPagerHandleClass(), BbsTopicPagerHandle.class.getName());
	}

	@Override
	public String tabs(final PageRequestResponse requestResponse) {
		final List<TabHref> tabHrefs = new ArrayList<TabHref>();
		final String topicUrl = getTopicUrl(requestResponse, null);
		TabHref tabHref;

		tabHrefs.add(new TabHref(getApplicationText(), getApplicationUrl(requestResponse)));

		BbsForum forum;
		if ((forum = BbsUtils.getForum(requestResponse)) != null) {
			tabHref = new TabHref("#(ContentUtils.0)", getTopicUrl(requestResponse, forum));
			tabHref.setMatchMethod(EMatchMethod.startsWith);
			tabHrefs.add(tabHref);
		}

		if (IWebApplicationModule.Utils.isManager(requestResponse, this)) {
			tabHrefs.add(new TabHref("#(bbs_topic_view.0)", getUsersUrl(requestResponse)));
		}

		tabHrefs.add(new TabHref("#(ContentUtils.all)", topicUrl));

		//		tabHrefs.add(new TabHref("#(ContentUtils.recommended)", WebUtils.addParameters(topicUrl, "tq=recommended")));

		tabHref = new TabHref("#(bbs_topic_view.1)", WebUtils.addParameters(topicUrl, "star=-1"));
		for (final int v : new int[] { 1, 2, 3, 4, 5 }) {
			tabHref.getChildren().add(new TabHref(null, WebUtils.addParameters(topicUrl, "star=" + v)));
		}
		tabHrefs.add(tabHref);

		tabHref = new TabHref("#(ContentUtils.time)", WebUtils.addParameters(topicUrl, "time=day"));
		for (final String str : new String[] { "day2", "week", "month", "month3" }) {
			tabHref.getChildren().add(new TabHref(null, WebUtils.addParameters(topicUrl, "time=" + str)));
		}
		tabHrefs.add(tabHref);

		final IAccount login = AccountSession.getLogin(requestResponse.getSession());
		//		tabHrefs.add(new TabHref("#(ContentUtils.attention)", login == null ? LoginUtils.getLocationPath() : WebUtils.addParameters(topicUrl,
		//				"tq=attention")));

		tabHrefs.add(createUserTab("#(bbs_topic_view.3)", login, topicUrl));
		final IAccount sAccount = ContentUtils.getAccountAware().getSpecifiedAccount(requestResponse);
		if (sAccount != null && login != null && !login.equals(sAccount)) {
			final String txt = StringUtils.substring(sAccount.user().getText(), 8);
			//			tabHrefs.add(createUserTab(txt, sAccount, topicUrl));
		}
		return TabsUtils.tabs(requestResponse, -1, tabHrefs.toArray(new TabHref[tabHrefs.size()]));
	}

	private TabHref createUserTab(final String txt, final IAccount account, final String topicUrl) {
		String uParams = null;
		final TabHref tabHref = new TabHref(txt, account == null ? LoginUtils.getLocationPath() : WebUtils.addParameters(topicUrl, uParams = OrgUtils
				.um().getUserIdParameterName() + "=" + account.getId()));
		if (uParams != null) {
			tabHref.getChildren().add(new TabHref(null, WebUtils.addParameters(topicUrl, uParams + "&tq=posts")));
		}
		return tabHref;
	}

	@Override
	public String tabs2(final PageRequestResponse requestResponse) {
		final StringBuilder sb = new StringBuilder();
		final String topicUrl = getTopicUrl(requestResponse, null);
		final String star = requestResponse.getRequestParameter("star");
		if (StringUtils.hasText(star)) {
			final int iStar = ConvertUtils.toInt(star, -1);
			sb.append("<a");
			if (iStar < 1) {
				sb.append(" class=\"a2 nav_arrow\"");
			}
			sb.append(" href=\"").append(WebUtils.addParameters(topicUrl, "star=-1")).append("\">").append(LocaleI18n.getMessage("bbs_topic_view.2"))
					.append("</a>");
			for (final int v : new int[] { 1, 2, 3, 4, 5 }) {
				sb.append(HTMLBuilder.SEP);
				sb.append("<a");
				if (iStar == v) {
					sb.append(" class=\"a2 nav_arrow\"");
				}
				sb.append(" href=\"").append(WebUtils.addParameters(topicUrl, "star=" + v)).append("\">").append(v).append("</a>");
			}
		}

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
				sb.append(" href=\"").append(WebUtils.addParameters(topicUrl, "time=" + str)).append("\">").append("#(ContentUtils.time.").append(i)
						.append(")").append("</a>");
			}
		}

		final IAccount aAccount = ContentUtils.getAccountAware().getSpecifiedAccount(requestResponse);
		if (aAccount != null) {
			final String uParams = OrgUtils.um().getUserIdParameterName() + "=" + aAccount.getId();
			final boolean posts = "posts".equals(requestResponse.getRequestParameter("tq"));
			sb.append("<a");
			if (!posts) {
				sb.append(" class=\"a2 nav_arrow\"");
			}
			sb.append(" href=\"").append(WebUtils.addParameters(topicUrl, uParams)).append("\">").append("#(topic_view_pager.14)").append("</a>");
			sb.append(HTMLBuilder.SEP).append("<a");
			if (posts) {
				sb.append(" class=\"a2 nav_arrow\"");
			}
			sb.append(" href=\"").append(WebUtils.addParameters(topicUrl, uParams + "&tq=posts")).append("\">").append("#(topic_view_pager.15)")
					.append("</a>");
		}
		return sb.toString();
	}

	@Override
	public void doAttentionSent(ComponentParameter compParameter, RemarkItem remark, Class<?> classBean) {
		// TODO Auto-generated method stub

	}
}
