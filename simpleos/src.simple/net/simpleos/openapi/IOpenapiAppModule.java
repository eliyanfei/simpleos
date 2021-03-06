package net.simpleos.openapi;

import net.simpleframework.web.page.PageRequestResponse;
import net.simpleos.i.ISimpleosApplicationModule;

/**
 * 
 * @author 李岩飞 
 * @email eliyanfei@126.com
 * @date 2014年11月20日 上午11:48:31 
 * @Description: TODO(这里用一句话描述这个类的作用)
 *
 */
public interface IOpenapiAppModule extends ISimpleosApplicationModule {
	OpenapiBean getOpenapiBean(final EOpenapi openapi, final Object openId);

	String login(final PageRequestResponse requestResponse, final OpenapiBean openapiBean);

	String login1(final PageRequestResponse requestResponse, final OpenapiBean openapiBean);
}
