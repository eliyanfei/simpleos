package net.simpleframework.web.page.component.ui.tree.db;

import javax.servlet.ServletContext;

import net.simpleframework.web.page.component.AbstractComponentBean;
import net.simpleframework.web.page.component.ui.tree.AbstractTreeBean;
import net.simpleframework.web.page.component.ui.tree.AbstractTreeNode;
import net.simpleframework.web.page.component.ui.tree.AbstractTreeRegistry;
import net.simpleframework.web.page.component.ui.tree.TreeRegistry;

import org.dom4j.Element;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class DbTreeRegistry extends AbstractTreeRegistry {
	public static final String dbTree = "dbTree";

	public DbTreeRegistry(final ServletContext servletContext) {
		super(servletContext);
	}

	@Override
	public String getComponentName() {
		return dbTree;
	}

	@Override
	public String getComponentDeploymentName() {
		return TreeRegistry.tree;
	}

	@Override
	protected AbstractTreeNode createTreeNode(final Element element,
			final AbstractTreeBean treeBean, final AbstractTreeNode parent) {
		return null;
	}

	@Override
	protected Class<? extends AbstractComponentBean> getBeanClass() {
		return DbTreeBean.class;
	}
}
