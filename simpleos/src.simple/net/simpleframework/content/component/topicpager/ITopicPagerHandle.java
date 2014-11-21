package net.simpleframework.content.component.topicpager;

import java.util.Collection;
import java.util.Map;

import net.simpleframework.content.IContentPagerHandle;
import net.simpleframework.content.component.catalog.CatalogOwner;
import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.organization.IUser;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.menu.MenuBean;
import net.simpleframework.web.page.component.ui.menu.MenuItem;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface ITopicPagerHandle extends IContentPagerHandle {

	/************** URL **************/
	String getPostViewUrl(ComponentParameter compParameter, TopicBean topicBean);

	String wrapOpenLink(ComponentParameter compParameter, TopicBean topicBean);

	String getTopicUrl(PageRequestResponse requestResponse);

	String getBlogUrl(PageRequestResponse requestResponse, IUser user);

	/************** Data **************/
	IDataObjectQuery<? extends PostsBean> getPostsList(ComponentParameter compParameter,
			TopicBean topicBean);

	IDataObjectQuery<? extends CatalogOwner> catalogOwners(ComponentParameter compParameter);

	PostsBean getPostsBean(ComponentParameter compParameter, TopicBean topicBean);

	PostsTextBean getPostsText(ComponentParameter compParameter, Object objectBean);

	/************** Utils **************/

	boolean isShowTags(ComponentParameter compParameter);

	String[] getPostImportPages(ComponentParameter compParameter);

	Collection<MenuItem> getPostUserMenuItems(ComponentParameter compParameter, MenuBean menuBean);

	Map<String, Object> getUserViewPropertiesEx(ComponentParameter compParameter, PostsBean postsBean);

	String getPostActions(final ComponentParameter compParameter, final PostsBean postsBean);

	boolean isReplyNew(ComponentParameter compParameter, final TopicBean topic);
}