package net.simpleframework.web.page.component.ui.tabs;

import java.util.Iterator;

import javax.servlet.ServletContext;

import net.simpleframework.util.StringUtils;
import net.simpleframework.util.script.IScriptEval;
import net.simpleframework.web.page.PageParameter;
import net.simpleframework.web.page.component.AbstractComponentBean;
import net.simpleframework.web.page.component.AbstractComponentRegistry;
import net.simpleframework.web.page.component.AbstractComponentRender;
import net.simpleframework.web.page.component.AbstractComponentResourceProvider;
import net.simpleframework.web.page.component.ComponentException;
import net.simpleframework.web.page.component.base.ajaxrequest.AjaxRequestBean;

import org.dom4j.Element;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class TabsRegistry extends AbstractComponentRegistry {
	public static final String tabs = "tabs";

	public TabsRegistry(final ServletContext servletContext) {
		super(servletContext);
	}

	@Override
	public String getComponentName() {
		return tabs;
	}

	@Override
	protected Class<? extends AbstractComponentBean> getBeanClass() {
		return TabsBean.class;
	}

	@Override
	protected Class<? extends AbstractComponentRender> getRenderClass() {
		return TabsRender.class;
	}

	@Override
	protected Class<? extends AbstractComponentResourceProvider> getResourceProviderClass() {
		return TabsResourceProvider.class;
	}

	@Override
	public AbstractComponentBean createComponentBean(final PageParameter pageParameter,
			final Element component) {
		final TabsBean tabsBean = (TabsBean) super.createComponentBean(pageParameter, component);
		final IScriptEval scriptEval = pageParameter.getScriptEval();
		final Iterator<?> it = component.elementIterator("tab");
		while (it.hasNext()) {
			final Element element = (Element) it.next();
			final TabItem tab = new TabItem(element);
			tab.parseElement(scriptEval);

			final String contentRef = tab.getContentRef();
			if (StringUtils.hasText(contentRef)) {
				final AbstractComponentBean componentBean = pageParameter.getComponentBean(contentRef);
				if (componentBean == null) {
					if (!isComponentInCache(pageParameter, contentRef)) {
						throw ComponentException.getComponentRefException();
					}
				} else {
					componentBean.setRunImmediately(false);
					if (componentBean instanceof AjaxRequestBean) {
						((AjaxRequestBean) componentBean).setShowLoading(false);
					}
					tab.setContent(getLoadingContent());
				}
			} else {
				tab.setBeanFromElementAttributes(scriptEval, new String[] { "content" });
			}
			tabsBean.getTabItems().add(tab);
		}
		return tabsBean;
	}
}
