package net.simpleframework.web.page.component.ui.tree;

import java.util.Collection;
import java.util.Iterator;

import javax.servlet.ServletContext;

import net.simpleframework.web.page.PageParameter;
import net.simpleframework.web.page.component.AbstractComponentBean;
import net.simpleframework.web.page.component.AbstractComponentRegistry;
import net.simpleframework.web.page.component.AbstractComponentRender;
import net.simpleframework.web.page.component.AbstractComponentResourceProvider;

import org.dom4j.Element;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class AbstractTreeRegistry extends AbstractComponentRegistry {

	public AbstractTreeRegistry(final ServletContext servletContext) {
		super(servletContext);
	}

	@Override
	protected Class<? extends AbstractComponentRender> getRenderClass() {
		return TreeRender.class;
	}

	@Override
	protected Class<? extends AbstractComponentResourceProvider> getResourceProviderClass() {
		return TreeResourceProvider.class;
	}

	@Override
	public AbstractComponentBean createComponentBean(final PageParameter pageParameter,
			final Element component) {
		final AbstractTreeBean treeBean = (AbstractTreeBean) super.createComponentBean(pageParameter,
				component);
		final Iterator<?> it = component.elementIterator("treenode");
		while (it.hasNext()) {
			final Element element = (Element) it.next();
			setTreeNode(pageParameter, treeBean, null, treeBean.getTreeNodes(), element);
		}
		return treeBean;
	}

	protected abstract AbstractTreeNode createTreeNode(Element element, AbstractTreeBean treeBean,
			AbstractTreeNode parent);

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void setTreeNode(final PageParameter pageParameter, final AbstractTreeBean treeBean,
			final AbstractTreeNode parent, final Collection children, final Element element) {
		final AbstractTreeNode node = createTreeNode(element, treeBean, parent);
		if (node == null) {
			return;
		}
		node.parseElement(pageParameter.getScriptEval());
		children.add(node);
		final Iterator<?> it = element.elementIterator("treenode");
		while (it.hasNext()) {
			final Element ele = (Element) it.next();
			setTreeNode(pageParameter, treeBean, node, node.getChildren(), ele);
		}
	}
}
