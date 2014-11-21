package net.simpleframework.web.page.component.ui.picshow;

import java.util.Collection;

import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.component.AbstractComponentBean;
import net.simpleframework.web.page.component.AbstractComponentResourceProvider;
import net.simpleframework.web.page.component.IComponentRegistry;

public class PicShowResourceProvider extends AbstractComponentResourceProvider {
	private final static String[] JAVASCRIPT_PATH = new String[] { "/js/picShow.js" };

	public PicShowResourceProvider(final IComponentRegistry componentRegistry) {
		super(componentRegistry);
	}

	@Override
	public String[] getCssPath(final PageRequestResponse requestResponse,
			final Collection<AbstractComponentBean> componentBeans) {
		return new String[] { getCssSkin(requestResponse, "picShow.css") };
	}

	@Override
	public String[] getJavascriptPath(final PageRequestResponse requestResponse, final Collection<AbstractComponentBean> componentBeans) {
		return JAVASCRIPT_PATH;
	}
}