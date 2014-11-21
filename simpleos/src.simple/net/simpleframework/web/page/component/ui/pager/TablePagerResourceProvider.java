package net.simpleframework.web.page.component.ui.pager;

import java.util.Collection;

import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.component.AbstractComponentBean;
import net.simpleframework.web.page.component.AbstractComponentResourceProvider;
import net.simpleframework.web.page.component.IComponentRegistry;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class TablePagerResourceProvider extends AbstractComponentResourceProvider {

	public TablePagerResourceProvider(final IComponentRegistry componentRegistry) {
		super(componentRegistry);
	}

	@Override
	public String[] getDependentComponents(final PageRequestResponse requestResponse,
			final Collection<AbstractComponentBean> componentBeans) {
		return new String[] { PagerRegistry.pager };
	}

	@Override
	public String[] getPageCssPath(final PageRequestResponse requestResponse,
			final Collection<AbstractComponentBean> componentBeans) {
		return new String[] { "/" + PagerRegistry.pager
				+ getCssSkin(requestResponse, "tablepager.css") };
	}

	final static String[] PAGER_UTILS_JAVASCRIPT_PATH = new String[] { "/" + PagerRegistry.pager
			+ "/js/tablepager.js" };

	@Override
	public String[] getPageJavascriptPath(final PageRequestResponse requestResponse,
			final Collection<AbstractComponentBean> componentBeans) {
		return PAGER_UTILS_JAVASCRIPT_PATH;
	}
}
