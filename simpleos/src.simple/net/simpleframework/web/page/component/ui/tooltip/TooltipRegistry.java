package net.simpleframework.web.page.component.ui.tooltip;

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
public class TooltipRegistry extends AbstractComponentRegistry {
	public static final String tooltip = "tooltip";

	public TooltipRegistry(final ServletContext servletContext) {
		super(servletContext);
	}

	@Override
	protected Class<TooltipBean> getBeanClass() {
		return TooltipBean.class;
	}

	@Override
	protected Class<? extends AbstractComponentRender> getRenderClass() {
		return TooltipRender.class;
	}

	@Override
	protected Class<? extends AbstractComponentResourceProvider> getResourceProviderClass() {
		return TooltipResourceProvider.class;
	}

	@Override
	public AbstractComponentBean createComponentBean(final PageParameter pageParameter,
			final Element component) {
		final TooltipBean tooltipBean = (TooltipBean) super.createComponentBean(pageParameter,
				component);
		final IScriptEval scriptEval = pageParameter.getScriptEval();
		final Iterator<?> it = component.elementIterator("tip");
		while (it.hasNext()) {
			final Element element = (Element) it.next();
			final TipBean tip = new TipBean(element, tooltipBean);
			tip.parseElement(scriptEval);
			final String ajaxRequest = tip.getAjaxRequest();
			if (StringUtils.hasText(ajaxRequest)) {
				final AjaxRequestBean ajaxRequestBean = (AjaxRequestBean) pageParameter
						.getComponentBean(ajaxRequest);
				if (ajaxRequestBean == null) {
					if (!isComponentInCache(pageParameter, ajaxRequest)) {
						throw ComponentException.getComponentRefException();
					}
				} else {
					ajaxRequestBean.setShowLoading(false);
				}
			}
			final Element hideOnElement = element.element("hideOn");
			if (hideOnElement != null) {
				final TipBean.HideOn hideOn = new TipBean.HideOn(hideOnElement);
				hideOn.parseElement(scriptEval);
				tip.setHideOn(hideOn);
			}

			final Element hookElement = element.element("hook");
			if (hookElement != null) {
				final TipBean.Hook hook = new TipBean.Hook(hookElement);
				hook.parseElement(scriptEval);
				tip.setHook(hook);
			}

			tooltipBean.getTips().add(tip);
		}
		return tooltipBean;
	}

	@Override
	public String getComponentName() {
		return tooltip;
	}
}
