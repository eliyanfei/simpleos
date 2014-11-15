package net.simpleframework.web.page.component.ui.tree;

import org.dom4j.Element;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class TreeNode extends AbstractTreeNode {

	TreeNode(final Element element, final AbstractTreeBean treeBean, final AbstractTreeNode parent) {
		super(element, treeBean, parent, null);
	}

	public TreeNode(final AbstractTreeBean treeBean, final AbstractTreeNode parent, final Object data) {
		super(treeBean, parent, data);
	}
}
