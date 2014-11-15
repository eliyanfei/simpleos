package net.simpleframework.content.component.topicpager;

import java.util.ArrayList;
import java.util.Collection;

import net.simpleframework.core.bean.ITreeBeanAware;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.tree.AbstractTreeBean;
import net.simpleframework.web.page.component.ui.tree.AbstractTreeHandle;
import net.simpleframework.web.page.component.ui.tree.AbstractTreeNode;
import net.simpleframework.web.page.component.ui.tree.TreeNode;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class TopicMove2Tree extends AbstractTreeHandle {

	@Override
	public Collection<? extends AbstractTreeNode> getTreenodes(
			final ComponentParameter compParameter, final AbstractTreeNode treeNode) {
		final ComponentParameter nComponentParameter = TopicPagerUtils
				.getComponentParameter(compParameter);
		final ITopicPagerHandle tHandle = (ITopicPagerHandle) nComponentParameter
				.getComponentHandle();
		final ITreeBeanAware parent = treeNode != null ? (ITreeBeanAware) treeNode.getDataObject()
				: null;
		final Collection<? extends ITreeBeanAware> catalogs = tHandle.getMove2Catalogs(
				nComponentParameter, parent);
		if (catalogs != null) {
			final Collection<TreeNode> coll = new ArrayList<TreeNode>();
			for (final ITreeBeanAware catalog : catalogs) {
				final TreeNode treeNode2 = new TreeNode((AbstractTreeBean) compParameter.componentBean,
						treeNode, catalog);
				coll.add(treeNode2);
			}
			return coll;
		} else {
			return null;
		}
	}
}
