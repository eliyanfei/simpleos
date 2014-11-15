package net.itsite;

import net.itsite.openapi.OpenapiUtils;
import net.simpleframework.applets.openid.DefaultOpenIDApplicationModule;
import net.simpleframework.web.page.PageRequestResponse;

public class ItSiteOpenIDApplicationModule extends DefaultOpenIDApplicationModule {

	@Override
	protected String getReturnToURL(final PageRequestResponse requestResponse) {
		final StringBuilder url = new StringBuilder();
		url.append(getApplication().getApplicationConfig().getServerUrl());
		url.append(requestResponse.wrapContextPath("/openid.html"));
		return url.toString();
	}

	@Override
	public String getOpUrl(PageRequestResponse requestResponse) {
		final String op = requestResponse.getRequestParameter("op");
		if ("sina_t".equals(op)) {
			return OpenapiUtils.authorizeWeibo(requestResponse);
		} else if ("qq_t".equals(op)) {
			return OpenapiUtils.authorizeQQWeibo(requestResponse);
		} else if ("qq".equals(op)) {
			return OpenapiUtils.authorizeQQ(requestResponse);
		}
		return super.getOpUrl(requestResponse);
	}
}
