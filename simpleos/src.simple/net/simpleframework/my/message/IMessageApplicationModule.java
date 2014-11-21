package net.simpleframework.my.message;

import net.simpleframework.web.IWebApplicationModule;
import net.simpleframework.web.page.PageRequestResponse;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface IMessageApplicationModule extends IWebApplicationModule {

	Class<? extends ENotification> getNotificationClass();

	String getSystemMessageUrl(PageRequestResponse requestResponse);
}
