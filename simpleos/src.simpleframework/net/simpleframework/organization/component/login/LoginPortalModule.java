package net.simpleframework.organization.component.login;

import net.simpleframework.web.page.AbstractUrlForward;
import net.simpleframework.web.page.IForward;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.portal.PageletBean;
import net.simpleframework.web.page.component.ui.portal.module.AbstractPortalModuleHandle;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class LoginPortalModule extends AbstractPortalModuleHandle {
	public LoginPortalModule(final PageletBean pagelet) {
		super(pagelet);
	}

	@Override
	protected String[] getDefaultOptions() {
		return null;
	}

	@Override
	public IForward getPageletContent(final ComponentParameter compParameter) {
		return AbstractUrlForward.componentUrl(LoginRegistry.login, "/jsp/login_layout_module.jsp");
	}
}
