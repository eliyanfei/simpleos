package net.simpleframework.web.page.component.ui.syntaxhighlighter;

import javax.servlet.ServletContext;

import net.simpleframework.util.LocaleI18n;
import net.simpleframework.web.page.PageDocument;
import net.simpleframework.web.page.PageParameter;
import net.simpleframework.web.page.component.AbstractComponentBean;
import net.simpleframework.web.page.component.AbstractComponentRegistry;
import net.simpleframework.web.page.component.AbstractComponentRender;
import net.simpleframework.web.page.component.AbstractComponentResourceProvider;
import net.simpleframework.web.page.component.base.ajaxrequest.AjaxRequestBean;
import net.simpleframework.web.page.component.ui.window.WindowBean;

import org.dom4j.Element;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class SyntaxHighlighterRegistry extends AbstractComponentRegistry {
	public static final String syntaxHighlighter = "syntaxHighlighter";

	public SyntaxHighlighterRegistry(final ServletContext servletContext) {
		super(servletContext);
	}

	@Override
	public String getComponentName() {
		return syntaxHighlighter;
	}

	@Override
	protected Class<? extends AbstractComponentBean> getBeanClass() {
		return SyntaxHighlighterBean.class;
	}

	@Override
	protected Class<? extends AbstractComponentRender> getRenderClass() {
		return SyntaxHighlighterRender.class;
	}

	@Override
	protected Class<? extends AbstractComponentResourceProvider> getResourceProviderClass() {
		return SyntaxHighlighterResourceProvider.class;
	}

	@Override
	public AbstractComponentBean createComponentBean(final PageParameter pageParameter,
			final Element component) {
		final SyntaxHighlighterBean syntaxHighlighter = (SyntaxHighlighterBean) super
				.createComponentBean(pageParameter, component);

		final PageDocument pageDocument = syntaxHighlighter.getPageDocument();
		final String beanId = syntaxHighlighter.hashId();
		final AjaxRequestBean ajaxRequest = new AjaxRequestBean(pageDocument, null);
		ajaxRequest.setName("ajaxRequest_" + beanId);

		ajaxRequest.setUrlForward(getComponentResourceProvider().getResourceHomePath()
				+ "/jsp/sh_window.jsp?" + SyntaxHighlighterUtils.BEAN_ID + "=" + beanId);
		final WindowBean window = new WindowBean(pageDocument, null);
		window.setName("window_" + beanId);
		window.setPopup(true);
		window.setHeight(380);
		window.setWidth(500);
		window.setTitle(LocaleI18n.getMessage("SyntaxHighlighterRegistry.0"));
		window.setContentRef(ajaxRequest.getName());
		pageDocument.addComponentBean(ajaxRequest);
		pageDocument.addComponentBean(window);
		return syntaxHighlighter;
	}
}
