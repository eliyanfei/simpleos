package net.simpleframework.content.component.topicpager;

import java.io.File;
import java.util.Calendar;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.content.AbstractContentPagerHandle;
import net.simpleframework.content.component.catalog.CatalogOwner;
import net.simpleframework.content.component.catalog.CatalogUtils;
import net.simpleframework.content.component.vote.VoteUtils;
import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.core.ado.db.Table;
import net.simpleframework.core.bean.IDataObjectBean;
import net.simpleframework.my.file.component.fileselect.FileSelectUtils;
import net.simpleframework.my.friends.FriendsUtils;
import net.simpleframework.organization.IJob;
import net.simpleframework.organization.IUser;
import net.simpleframework.organization.OrgUtils;
import net.simpleframework.organization.account.AccountContext;
import net.simpleframework.organization.account.Exp;
import net.simpleframework.organization.account.IAccount;
import net.simpleframework.organization.component.userpager.UserPagerUtils;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.util.DateUtils;
import net.simpleframework.util.IPAndCity;
import net.simpleframework.util.IoUtils;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.IPageConstants;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.menu.MenuBean;
import net.simpleframework.web.page.component.ui.menu.MenuItem;
import net.simpleframework.web.page.component.ui.pager.PagerUtils;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class AbstractTopicPagerHandle extends AbstractContentPagerHandle implements ITopicPagerHandle {
	public static Table simple_topic = new Table("simple_topic");

	public static Table simple_posts = new Table("simple_posts");

	public static Table simple_posts_text = new Table("simple_posts_text");

	@Override
	public void putTables(final Map<Class<?>, Table> tables) {
		tables.put(TopicBean.class, simple_topic);
		tables.put(PostsBean.class, simple_posts);
		tables.put(PostsTextBean.class, simple_posts_text);
		VoteUtils.putTables(tables);
	}

	@Override
	public Object getBeanProperty(final ComponentParameter compParameter, final String beanProperty) {
		if ("title".equalsIgnoreCase(beanProperty)) {
			return getNavigateHTML(compParameter);
		} else if ("jobAdd".equals(beanProperty)) {
			return IJob.sj_account_normal;
		}
		return super.getBeanProperty(compParameter, beanProperty);
	}

	@Override
	protected String getBeanIdName() {
		return TopicPagerUtils.BEAN_ID;
	}

	@Override
	public Class<? extends IDataObjectBean> getEntityBeanClass() {
		return TopicBean.class;
	}

	@Override
	public String getPostViewUrl(final ComponentParameter compParameter, final TopicBean topicBean) {
		return null;
	}

	@Override
	public String getNavigateHTML(final ComponentParameter compParameter) {
		return null;
	}

	@Override
	public Map<String, Object> getUserViewPropertiesEx(final ComponentParameter compParameter, final PostsBean postsBean) {
		final Map<String, Object> properties = new LinkedHashMap<String, Object>();
		final IAccount account = OrgUtils.am().queryForObjectById(postsBean.getUserId());
		if (account != null) {
			properties.put("#(topic_view_pager.1)", ConvertUtils.toDateString(account.getCreateDate()));
			properties.put("#(topic_view_pager.10)", DateUtils.getRelativeDate(account.getLastLoginDate()));
			try {
				properties.put("#(topic_view_pager.11)", IPAndCity.getCity(account.getLastLoginIP(), false));
			} catch (final Exception e) {
			}
			Exp exp;
			if (account != null && (exp = AccountContext.getExp(account.getExp())) != null) {
				final StringBuilder sb = new StringBuilder();
				sb.append(exp).append(UserPagerUtils.getExpIcon(exp));
				properties.put("#(topic_view_pager.12)", sb.toString());
			}
		}
		return properties;
	}

	static final String TOPIC_ID = "__topic_Id";

	@Override
	public String getIdParameterName(final ComponentParameter compParameter) {
		return TOPIC_ID;
	}

	@Override
	public List<MenuItem> getContextMenu(final ComponentParameter compParameter, final MenuBean menuBean) {
		final List<MenuItem> menuItems = super.getContextMenu(compParameter, menuBean);
		final MenuItem menuItem = menuItems.get(2);
		if (!StringUtils.hasText(menuItem.getIcon())) {
			menuItem.setIcon(PagerUtils.getCssPath(compParameter) + "/images/star.png");
		}
		return menuItems;
	}

	// catalog
	@Override
	public IDataObjectQuery<? extends CatalogOwner> catalogOwners(final ComponentParameter compParameter) {
		final ITableEntityManager temgr = getTableEntityManager(compParameter, CatalogOwner.class);
		if (temgr != null) {
			final ExpressionValue ev = new ExpressionValue("catalogId=?", new Object[] { getCatalogId(compParameter) });
			return temgr.query(ev, CatalogOwner.class);
		} else {
			return null;
		}
	}

	@Override
	public String getManager(final ComponentParameter compParameter) {
		final StringBuilder sb = new StringBuilder();
		sb.append(super.getManager(compParameter));
		final String job = CatalogUtils.getCatalogJob(catalogOwners(compParameter));
		if (StringUtils.hasText(job)) {
			sb.append(";").append(job);
		}
		return sb.toString();
	}

	@Override
	public boolean isReplyNew(final ComponentParameter compParameter, final TopicBean topic) {
		if (topic.getReplies() == 0) {
			return false;
		}
		final Calendar cal = Calendar.getInstance();
		cal.add(Calendar.HOUR, -12);
		final ExpressionValue ev = new ExpressionValue("topicid=? and createdate>?", new Object[] { topic.getId(), cal.getTime() });
		return getTableEntityManager(compParameter, PostsBean.class).query(ev).next() != null;
	}

	@Override
	public TopicLuceneManager createLuceneManager(final ComponentParameter compParameter) {
		final File iPath = new File(compParameter.getServletContext().getRealPath(IPageConstants.DATA_HOME + "/" + getFunctionModule() + "_index"));
		IoUtils.createDirectoryRecursively(iPath);
		return new TopicLuceneManager(compParameter, iPath);
	}

	@Override
	public String getTopicUrl(final PageRequestResponse requestResponse) {
		return null;
	}

	@Override
	public String getBlogUrl(final PageRequestResponse requestResponse, final IUser user) {
		return null;
	}

	@Override
	public Collection<MenuItem> getPostUserMenuItems(final ComponentParameter compParameter, final MenuBean menuBean) {
		return menuBean.getMenuItems();
	}

	@Override
	public String[] getPostImportPages(final ComponentParameter compParameter) {
		return new String[] { UserPagerUtils.xmlUserutils(), FileSelectUtils.xmlDownload(), "/app/myfavorite/myfavorite_utils.xml",
				FriendsUtils.deployPath + "jsp/friends_utils.xml" };
	}
}
