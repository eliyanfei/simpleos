package net.simpleframework.organization.component.register;

import javax.servlet.ServletContext;

import net.simpleframework.web.page.component.AbstractComponentBean;
import net.simpleframework.web.page.component.AbstractComponentRegistry;
import net.simpleframework.web.page.component.AbstractComponentRender;
import net.simpleframework.web.page.component.AbstractComponentResourceProvider;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class UserRegisterRegistry extends AbstractComponentRegistry {
	public static final String userRegister = "userRegister";

	public UserRegisterRegistry(final ServletContext servletContext) {
		super(servletContext);
	}

	@Override
	public String getComponentName() {
		return userRegister;
	}

	@Override
	protected Class<? extends AbstractComponentBean> getBeanClass() {
		return UserRegisterBean.class;
	}

	@Override
	protected Class<? extends AbstractComponentRender> getRenderClass() {
		return UserRegisterRender.class;
	}

	@Override
	protected Class<? extends AbstractComponentResourceProvider> getResourceProviderClass() {
		return UserRegisterResourceProvider.class;
	}
}
