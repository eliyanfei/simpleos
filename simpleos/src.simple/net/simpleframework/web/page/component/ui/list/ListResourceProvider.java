package net.simpleframework.web.page.component.ui.list;

import java.util.Collection;

import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.component.AbstractComponentBean;
import net.simpleframework.web.page.component.AbstractComponentResourceProvider;
import net.simpleframework.web.page.component.IComponentRegistry;
import net.simpleframework.web.page.component.ui.tooltip.TooltipRegistry;

public class ListResourceProvider extends AbstractComponentResourceProvider {

	public ListResourceProvider(final IComponentRegistry componentRegistry) {
		super(componentRegistry);
	}

	@Override
	public String[] getCssPath(final PageRequestResponse requestResponse, final Collection<AbstractComponentBean> componentBeans) {
		return new String[] { getCssSkin(requestResponse, "list.css") };
	}

	private final static String[] PAGE_JAVASCRIPT_PATH = new String[] { EFFECTS_FILE, DRAGDROP_FILE };

	@Override
	public String[] getPageJavascriptPath(final PageRequestResponse requestResponse, final Collection<AbstractComponentBean> componentBeans) {
		return PAGE_JAVASCRIPT_PATH;
	}

	@Override
	public String[] getDependentComponents(final PageRequestResponse requestResponse, final Collection<AbstractComponentBean> componentBeans) {
		return new String[] { TooltipRegistry.tooltip };
	}

	private final static String[] JAVASCRIPT_PATH = new String[] { "/js/list-src.js" };

	@Override
	public String[] getJavascriptPath(final PageRequestResponse requestResponse, final Collection<AbstractComponentBean> componentBeans) {
		return JAVASCRIPT_PATH;
	}
}
