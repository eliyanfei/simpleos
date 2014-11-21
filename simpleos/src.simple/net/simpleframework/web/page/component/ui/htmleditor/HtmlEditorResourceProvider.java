package net.simpleframework.web.page.component.ui.htmleditor;

import java.util.Collection;

import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.component.AbstractComponentBean;
import net.simpleframework.web.page.component.AbstractComponentResourceProvider;
import net.simpleframework.web.page.component.IComponentRegistry;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class HtmlEditorResourceProvider extends AbstractComponentResourceProvider {

	public HtmlEditorResourceProvider(final IComponentRegistry componentRegistry) {
		super(componentRegistry);
	}

	@Override
	public String[] getJavascriptPath(final PageRequestResponse pp, final Collection<AbstractComponentBean> componentBeans) {
		return new String[] { "/ckeditor/ckeditor.js?v=4.1.1" };
	}

	@Override
	public String[] getCssPath(final PageRequestResponse pp, final Collection<AbstractComponentBean> componentBeans) {
		return new String[] { "/css/default/ckeditor.css" };
	}
}
