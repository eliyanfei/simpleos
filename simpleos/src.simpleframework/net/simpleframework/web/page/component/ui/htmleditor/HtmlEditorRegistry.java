package net.simpleframework.web.page.component.ui.htmleditor;

import javax.servlet.ServletContext;

import net.simpleframework.web.page.component.AbstractComponentBean;
import net.simpleframework.web.page.component.AbstractComponentRegistry;
import net.simpleframework.web.page.component.AbstractComponentRender;
import net.simpleframework.web.page.component.AbstractComponentResourceProvider;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class HtmlEditorRegistry extends AbstractComponentRegistry {
	public HtmlEditorRegistry(ServletContext servletContext) {
		super(servletContext);
	}

	public static final String HTMLEDITOR = "htmlEditor";

	@Override
	public String getComponentName() {
		return HTMLEDITOR;
	}

	@Override
	protected Class<? extends AbstractComponentBean> getBeanClass() {
		return HtmlEditorBean.class;
	}

	@Override
	protected Class<? extends AbstractComponentRender> getRenderClass() {
		return HtmlEditorRender.class;
	}

	@Override
	protected Class<? extends AbstractComponentResourceProvider> getResourceProviderClass() {
		return HtmlEditorResourceProvider.class;
	}
}
