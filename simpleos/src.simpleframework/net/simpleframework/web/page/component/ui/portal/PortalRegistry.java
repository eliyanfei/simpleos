package net.simpleframework.web.page.component.ui.portal;

import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.ServletContext;

import net.simpleframework.util.script.IScriptEval;
import net.simpleframework.web.page.PageParameter;
import net.simpleframework.web.page.component.AbstractComponentBean;
import net.simpleframework.web.page.component.AbstractComponentRegistry;
import net.simpleframework.web.page.component.AbstractComponentRender;
import net.simpleframework.web.page.component.AbstractComponentResourceProvider;
import net.simpleframework.web.page.component.ui.portal.module.PortalModuleRegistryFactory;

import org.dom4j.Element;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class PortalRegistry extends AbstractComponentRegistry {

	public static final String portal = "portal";

	public PortalRegistry(final ServletContext servletContext) {
		super(servletContext);
		PortalModuleRegistryFactory.getInstance().init(servletContext);
	}

	@Override
	public String getComponentName() {
		return portal;
	}

	@Override
	protected Class<? extends AbstractComponentBean> getBeanClass() {
		return PortalBean.class;
	}

	@Override
	protected Class<? extends AbstractComponentRender> getRenderClass() {
		return PortalRender.class;
	}

	@Override
	protected Class<? extends AbstractComponentResourceProvider> getResourceProviderClass() {
		return PortalResourceProvider.class;
	}

	@Override
	public AbstractComponentBean createComponentBean(final PageParameter pageParameter,
			final Element component) {
		final PortalBean portalBean = (PortalBean) super
				.createComponentBean(pageParameter, component);
		portalBean.getColumns().addAll(loadBean(pageParameter, portalBean, component));
		return portalBean;
	}

	public ArrayList<ColumnBean> loadBean(final PageParameter pageParameter,
			final PortalBean portalBean, final Element component) {
		final IScriptEval scriptEval = pageParameter.getScriptEval();
		final ArrayList<ColumnBean> al = new ArrayList<ColumnBean>();
		final Iterator<?> it = component.elementIterator("column");
		while (it.hasNext()) {
			final Element element = (Element) it.next();
			final ColumnBean column = new ColumnBean(element, portalBean);
			al.add(column);
			column.parseElement(scriptEval);

			final Iterator<?> it2 = element.elementIterator("pagelet");
			while (it2.hasNext()) {
				final Element element2 = (Element) it2.next();
				final PageletBean pagelet = new PageletBean(element2, column);
				pagelet.parseElement(scriptEval);
				column.getPagelets().add(pagelet);

				final Element titleElement = element2.element("title");
				if (titleElement != null) {
					final PageletTitle title = new PageletTitle(titleElement, pagelet);
					title.parseElement(scriptEval);
					pagelet.setTitle(title);
				}
			}
		}
		return al;
	}
}
