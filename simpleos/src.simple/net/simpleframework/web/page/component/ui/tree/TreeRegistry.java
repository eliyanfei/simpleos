package net.simpleframework.web.page.component.ui.tree;

import javax.servlet.ServletContext;

import net.simpleframework.web.page.component.AbstractComponentBean;

import org.dom4j.Element;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class TreeRegistry extends AbstractTreeRegistry {
	public static final String tree = "tree";

	public TreeRegistry(final ServletContext servletContext) {
		super(servletContext);
	}

	@Override
	public String getComponentName() {
		return tree;
	}

	@Override
	protected Class<? extends AbstractComponentBean> getBeanClass() {
		return TreeBean.class;
	}

	@Override
	protected AbstractTreeNode createTreeNode(final Element element,
			final AbstractTreeBean treeBean, final AbstractTreeNode parent) {
		return new TreeNode(element, treeBean, parent);
	}
}
