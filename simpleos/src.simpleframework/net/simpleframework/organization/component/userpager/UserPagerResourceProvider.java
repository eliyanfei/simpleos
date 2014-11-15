package net.simpleframework.organization.component.userpager;

import java.util.Collection;

import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.component.AbstractComponentBean;
import net.simpleframework.web.page.component.IComponentRegistry;
import net.simpleframework.web.page.component.base.ajaxrequest.AjaxRequestRegistry;
import net.simpleframework.web.page.component.ui.pager.PagerRegistry;
import net.simpleframework.web.page.component.ui.pager.TablePagerResourceProvider;
import net.simpleframework.web.page.component.ui.tooltip.TooltipRegistry;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class UserPagerResourceProvider extends TablePagerResourceProvider {

	public UserPagerResourceProvider(final IComponentRegistry componentRegistry) {
		super(componentRegistry);
	}

	@Override
	public String[] getDependentComponents(final PageRequestResponse requestResponse,
			final Collection<AbstractComponentBean> componentBeans) {
		return new String[] { AjaxRequestRegistry.ajaxRequest, PagerRegistry.pager,
				TooltipRegistry.tooltip };
	}

	@Override
	public String[] getCssPath(final PageRequestResponse requestResponse,
			final Collection<AbstractComponentBean> componentBeans) {
		return new String[] { getCssSkin(requestResponse, "userpager.css") };
	}
}
