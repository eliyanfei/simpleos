package net.simpleframework.web.page.component.ui.menu;

import java.util.Collection;
import java.util.Iterator;

import javax.servlet.ServletContext;

import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.PageParameter;
import net.simpleframework.web.page.component.AbstractComponentBean;
import net.simpleframework.web.page.component.AbstractComponentRegistry;
import net.simpleframework.web.page.component.AbstractComponentRender;
import net.simpleframework.web.page.component.AbstractComponentResourceProvider;
import net.simpleframework.web.page.component.ComponentException;

import org.dom4j.Element;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class MenuRegistry extends AbstractComponentRegistry {
	public static final String menu = "menu";

	public MenuRegistry(final ServletContext servletContext) {
		super(servletContext);
	}

	@Override
	public String getComponentName() {
		return menu;
	}

	@Override
	protected Class<MenuBean> getBeanClass() {
		return MenuBean.class;
	}

	@Override
	protected Class<? extends AbstractComponentRender> getRenderClass() {
		return MenuRender.class;
	}

	@Override
	protected Class<? extends AbstractComponentResourceProvider> getResourceProviderClass() {
		return MenuResourceProvider.class;
	}

	@Override
	public AbstractComponentBean createComponentBean(final PageParameter pageParameter,
			final Element component) {
		final MenuBean menuBean = (MenuBean) super.createComponentBean(pageParameter, component);
		final Iterator<?> it = component.elementIterator("menuitem");
		while (it.hasNext()) {
			final Element element = (Element) it.next();
			initMenuItem(pageParameter, menuBean, null, menuBean.getMenuItems(), element);
		}
		return menuBean;
	}

	void initMenuItem(final PageParameter pageParameter, final MenuBean menuBean,
			final MenuItem parent, final Collection<MenuItem> children, final Element element) {
		final MenuItem menuItem = new MenuItem(element, menuBean, parent);
		menuItem.parseElement(pageParameter.getScriptEval());
		final String ref = menuItem.getRef();
		if (StringUtils.hasText(ref)) {
			final MenuBean menuRef = (MenuBean) pageParameter.getComponentBean(ref);
			if (menuRef == null) {
				if (!isComponentInCache(pageParameter, ref)) {
					throw ComponentException.getComponentRefException();
				}
			} else {
				children.addAll(menuRef.getMenuItems());
			}
		} else {
			children.add(menuItem);
		}
		final Iterator<?> it = element.elementIterator("menuitem");
		while (it.hasNext()) {
			final Element ele = (Element) it.next();
			initMenuItem(pageParameter, menuBean, menuItem, menuItem.getChildren(), ele);
		}
	}
}
