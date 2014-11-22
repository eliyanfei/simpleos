package net.simpleos;

import net.simpleframework.applets.openid.DefaultOpenIDApplicationModule;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleos.openapi.OpenapiUtils;
/**
 * 
 * @author 李岩飞 
 * @date 2014年11月19日 下午4:57:49 
 * @Description: TODO(这里用一句话描述这个类的作用)
 *
 */
public class SimpleosOpenIDApplicationModule extends DefaultOpenIDApplicationModule {

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
		} else if ("qq".equals(op)) {
			return OpenapiUtils.authorizeQQ(requestResponse);
		}
		return super.getOpUrl(requestResponse);
	}
}
