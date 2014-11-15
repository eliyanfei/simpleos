package net.simpleframework.organization.component.login;

import javax.servlet.ServletContext;

import net.simpleframework.util.LocaleI18n;
import net.simpleframework.web.page.component.AbstractComponentBean;
import net.simpleframework.web.page.component.AbstractComponentRegistry;
import net.simpleframework.web.page.component.AbstractComponentRender;
import net.simpleframework.web.page.component.AbstractComponentResourceProvider;
import net.simpleframework.web.page.component.ui.portal.module.PortalModuleRegistryFactory;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class LoginRegistry extends AbstractComponentRegistry {
	public static final String login = "login";

	public LoginRegistry(final ServletContext servletContext) {
		super(servletContext);

		PortalModuleRegistryFactory.regist(LoginPortalModule.class, "login", LocaleI18n.getMessage("LoginLayoutModule.0"), "信息",
				getComponentResourceProvider().getResourceHomePath() + "/images/login.png", null);
	}

	@Override
	public String getComponentName() {
		return login;
	}

	@Override
	protected Class<? extends AbstractComponentBean> getBeanClass() {
		return LoginBean.class;
	}

	@Override
	protected Class<? extends AbstractComponentRender> getRenderClass() {
		return LoginRender.class;
	}

	@Override
	protected Class<? extends AbstractComponentResourceProvider> getResourceProviderClass() {
		return LoginResourceProvider.class;
	}
}
