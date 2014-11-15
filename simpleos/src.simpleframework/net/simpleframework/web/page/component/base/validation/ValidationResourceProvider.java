package net.simpleframework.web.page.component.base.validation;

import java.util.Collection;

import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.component.AbstractComponentBean;
import net.simpleframework.web.page.component.AbstractComponentResourceProvider;
import net.simpleframework.web.page.component.IComponentRegistry;
import net.simpleframework.web.page.component.ui.calendar.CalendarRegistry;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class ValidationResourceProvider extends AbstractComponentResourceProvider {
	private final static String[] JAVASCRIPT_PATH = new String[] { "/js/validation.js" };

	public ValidationResourceProvider(final IComponentRegistry componentRegistry) {
		super(componentRegistry);
	}

	@Override
	public String[] getJavascriptPath(final PageRequestResponse requestResponse,
			final Collection<AbstractComponentBean> componentBeans) {
		return JAVASCRIPT_PATH;
	}

	@Override
	public String[] getPageJavascriptPath(final PageRequestResponse requestResponse,
			final Collection<AbstractComponentBean> componentBeans) {
		return new String[] { "/" + CalendarRegistry.calendar + "/js/date.js" };
	}
}
