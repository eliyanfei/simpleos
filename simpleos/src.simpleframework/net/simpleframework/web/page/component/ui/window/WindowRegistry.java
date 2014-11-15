package net.simpleframework.web.page.component.ui.window;

import javax.servlet.ServletContext;

import net.simpleframework.util.StringUtils;
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
public class WindowRegistry extends AbstractComponentRegistry {
	public static final String window = "window";

	public WindowRegistry(final ServletContext servletContext) {
		super(servletContext);
	}

	@Override
	public String getComponentName() {
		return window;
	}

	@Override
	protected Class<? extends AbstractComponentBean> getBeanClass() {
		return WindowBean.class;
	}

	@Override
	protected Class<? extends AbstractComponentResourceProvider> getResourceProviderClass() {
		return WindowResourceProvider.class;
	}

	@Override
	protected Class<? extends AbstractComponentRender> getRenderClass() {
		return WindowRender.class;
	}

	@Override
	public AbstractComponentBean createComponentBean(final PageParameter pageParameter,
			final Element component) {
		final WindowBean windowBean = (WindowBean) super
				.createComponentBean(pageParameter, component);
		final String contentRef = windowBean.getContentRef();
		if (StringUtils.hasText(contentRef)) {
			final AbstractComponentBean ref = pageParameter.getComponentBean(contentRef);
			if (ref == null) {
				if (!isComponentInCache(pageParameter, contentRef)) {
					throw ComponentException.getComponentRefException();
				}
			} else {
				ref.setRunImmediately(false);
				windowBean.setContent(getLoadingContent());
				if (ref instanceof AjaxRequestBean) {
					((AjaxRequestBean) ref).setShowLoading(false);
				}
			}
		}
		return windowBean;
	}
}
