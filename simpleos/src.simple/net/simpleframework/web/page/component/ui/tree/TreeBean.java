package net.simpleframework.web.page.component.ui.tree;

import net.simpleframework.web.page.PageDocument;
import net.simpleframework.web.page.component.AbstractComponentRegistry;
import net.simpleframework.web.page.component.IComponentRegistry;

import org.dom4j.Element;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class TreeBean extends AbstractTreeBean {

	public TreeBean(final IComponentRegistry componentRegistry, final PageDocument pageDocument,
			final Element element) {
		super(componentRegistry, pageDocument, element);
	}

	public TreeBean(final PageDocument pageDocument, final Element element) {
		this(AbstractComponentRegistry.getRegistry(TreeRegistry.tree), pageDocument, element);
	}
}
