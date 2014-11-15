package net.simpleframework.my.friends;

import java.util.ArrayList;
import java.util.Collection;

import net.simpleframework.content.component.catalog.AbstractAccountCatalogHandle;
import net.simpleframework.core.bean.IIdBeanAware;
import net.simpleframework.organization.IJob;
import net.simpleframework.organization.OrgUtils;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.WebUtils;
import net.simpleframework.web.page.component.ComponentParameter;
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
public class FriendsGroupHandle extends AbstractAccountCatalogHandle {
	@Override
	public IFriendsApplicationModule getApplicationModule() {
		return FriendsUtils.applicationModule;
	}

	@Override
	public Class<? extends IIdBeanAware> getEntityBeanClass() {
		return FriendsGroup.class;
	}

	@Override
	public Object getBeanProperty(final ComponentParameter compParameter, final String beanProperty) {
		if (beanProperty.startsWith("job")) {
			return IJob.sj_account_normal;
		}
		return super.getBeanProperty(compParameter, beanProperty);
	}

	@Override
	public Collection<? extends AbstractTreeNode> getCatalogTreenodes(
			final ComponentParameter compParameter, final AbstractTreeBean treeBean,
			final AbstractTreeNode treeNode, final boolean dictionary) {
		final ArrayList<AbstractTreeNode> treeNodes = new ArrayList<AbstractTreeNode>();
		final Collection<? extends AbstractTreeNode> coll = super.getCatalogTreenodes(compParameter,
				treeBean, treeNode, dictionary);
		treeNodes.addAll(coll);
		final String imgBase = OrgUtils.getCssPath(compParameter) + "/images/";
		if (!dictionary) {
			if (treeNode == null) {
				final AbstractTreeNode treeNode2 = new TreeNode(treeBean, treeNode,
						LocaleI18n.getMessage("FriendsGroupHandle.0"));
				treeNode2.setJsClickCallback("$Actions['idMyFriendsPager']('op=online');");
				treeNode2.setImage(imgBase + "user_online.png");
				treeNodes.add(0, treeNode2);
			} else {
				treeNode.setOpened(true);
				final Object dataObject = treeNode.getDataObject();
				if (dataObject instanceof FriendsGroup) {
					final FriendsGroup fg = (FriendsGroup) dataObject;
					treeNode.setImage(imgBase + "users_nodept.png");
					treeNode.setJsClickCallback("$Actions['idMyFriendsPager']('op=&gid=" + fg.getId()
							+ "');");
					treeNode.setPostfixText(WebUtils.enclose(fg.getFriends()));
				} else {
					if (!StringUtils.hasText(treeNode.getJsClickCallback())) {
						treeNode.setJsClickCallback("$Actions['idMyFriendsPager']('op=&gid=');");
						treeNode.setImage(imgBase + "users.png");
					}
				}
			}
		}
		return treeNodes;
	}
}
