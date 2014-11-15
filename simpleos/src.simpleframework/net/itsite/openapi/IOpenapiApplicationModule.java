package net.itsite.openapi;

import net.itsite.i.IItSiteApplicationModule;
import net.simpleframework.web.page.PageRequestResponse;

public interface IOpenapiApplicationModule extends IItSiteApplicationModule {
	OpenapiBean getOpenapiBean(final EOpenapi openapi, final Object openId);

	String login(final PageRequestResponse requestResponse, final OpenapiBean openapiBean);

	String login1(final PageRequestResponse requestResponse, final OpenapiBean openapiBean);
}
