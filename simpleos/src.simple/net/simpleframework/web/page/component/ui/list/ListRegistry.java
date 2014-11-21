package net.simpleframework.web.page.component.ui.list;

import java.util.Collection;
import java.util.Iterator;

import javax.servlet.ServletContext;

import net.simpleframework.web.page.PageParameter;
import net.simpleframework.web.page.component.AbstractComponentBean;
import net.simpleframework.web.page.component.AbstractComponentRegistry;
import net.simpleframework.web.page.component.AbstractComponentRender;
import net.simpleframework.web.page.component.AbstractComponentResourceProvider;

import org.dom4j.Element;

public class ListRegistry extends AbstractComponentRegistry {
	public static final String list = "list";

	public ListRegistry(final ServletContext servletContext) {
		super(servletContext);
	}

	@Override
	protected Class<? extends AbstractComponentRender> getRenderClass() {
		return ListRender.class;
	}

	@Override
	public String getComponentName() {
		return list;
	}

	@Override
	protected Class<? extends AbstractComponentBean> getBeanClass() {
		return ListBean.class;
	}

	@Override
	protected Class<? extends AbstractComponentResourceProvider> getResourceProviderClass() {
		return ListResourceProvider.class;
	}

	@Override
	public AbstractComponentBean createComponentBean(final PageParameter pageParameter, final Element component) {
		final ListBean listBean = (ListBean) super.createComponentBean(pageParameter, component);
		final Iterator<?> it = component.elementIterator("listnode");
		while (it.hasNext()) {
			final Element element = (Element) it.next();
			setListNode(pageParameter, listBean, null, listBean.getListNodes(), element);
		}
		return listBean;
	}

	protected ListNode createListNode(final Element element, final ListBean treeBean, final ListNode parent) {
		return new ListNode(element, treeBean, parent, null);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void setListNode(final PageParameter pageParameter, final ListBean listBean, final ListNode parent, final Collection children,
			final Element element) {
		final ListNode node = createListNode(element, listBean, parent);
		if (node == null) {
			return;
		}
		node.parseElement(pageParameter.getScriptEval());
		children.add(node);
		final Iterator<?> it = element.elementIterator("listnode");
		while (it.hasNext()) {
			final Element ele = (Element) it.next();
			setListNode(pageParameter, listBean, node, node.getChildren(), ele);
		}
	}
}
