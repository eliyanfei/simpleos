package net.simpleframework.content.bbs;

import java.util.Collection;

import net.simpleframework.content.IContentApplicationModule;
import net.simpleframework.content.component.topicpager.ETopicQuery;
import net.simpleframework.organization.IUser;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.tree.AbstractTreeNode;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface IBbsApplicationModule extends IContentApplicationModule {

	String getTopicUrl(PageRequestResponse requestResponse, BbsForum forum);

	String getTopicUrl2(PageRequestResponse requestResponse, IUser user, ETopicQuery topicQuery);

	String getPostUrl(ComponentParameter compParameter, BbsTopic topic);

	String getUsersUrl(PageRequestResponse requestResponse);

	Collection<? extends AbstractTreeNode> getForumDictTree(ComponentParameter compParameter,
			AbstractTreeNode treeNode);

	boolean isForumNewState(BbsForum forum);
}
