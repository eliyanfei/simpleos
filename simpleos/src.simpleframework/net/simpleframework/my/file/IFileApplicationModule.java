package net.simpleframework.my.file;

import net.simpleframework.web.IWebApplicationModule;
import net.simpleframework.web.page.PageRequestResponse;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface IFileApplicationModule extends IWebApplicationModule {

	long getDefaultFileSizeLimit(PageRequestResponse requestResponse);

	long getDefaultFileUploadLimit(PageRequestResponse requestResponse);

	String getOptionsPath(PageRequestResponse requestResponse);
}
