package net.simpleframework.organization.account;

import net.simpleframework.web.page.PageRequestResponse;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface IGetAccountAware {

	IAccount getSpecifiedAccount(PageRequestResponse requestResponse);

	boolean isMyAccount(PageRequestResponse requestResponse);

	IAccount getAccount(PageRequestResponse requestResponse);

	String wrapAccountHref(PageRequestResponse requestResponse, Object userObject, final String text);

	String wrapAccountHref(PageRequestResponse requestResponse, Object userObject);

	String wrapImgAccountHref(PageRequestResponse requestResponse, Object userObject);

	String wrapImgAccountHref(PageRequestResponse requestResponse, Object userObject, int width,
			int height);
}
