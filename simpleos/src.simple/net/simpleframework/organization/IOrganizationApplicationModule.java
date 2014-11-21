package net.simpleframework.organization;

import net.simpleframework.organization.account.IAccount;
import net.simpleframework.web.IWebApplicationModule;
import net.simpleframework.web.page.PageRequestResponse;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface IOrganizationApplicationModule extends IWebApplicationModule {

	String getLoginUrl(PageRequestResponse requestResponse);

	String getAccountRuleUrl(PageRequestResponse requestResponse);

	/******************************* mail utils **********************************/

	void mailRegistActivation2(IAccount account);

	void mailNoLogin(IAccount account);
}