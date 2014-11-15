package net.simpleframework.web.page.component.ui.validatecode;

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
public class ValidateCodeRegistry extends AbstractComponentRegistry {
	public static final String validateCode = "validateCode";

	public ValidateCodeRegistry(final ServletContext servletContext) {
		super(servletContext);
	}

	@Override
	public String getComponentName() {
		return validateCode;
	}

	@Override
	protected Class<? extends AbstractComponentBean> getBeanClass() {
		return ValidateCodeBean.class;
	}

	@Override
	protected Class<? extends AbstractComponentRender> getRenderClass() {
		return ValidateCodeRender.class;
	}

	@Override
	protected Class<? extends AbstractComponentResourceProvider> getResourceProviderClass() {
		return ValidateCodeResourceProvider.class;
	}
}
