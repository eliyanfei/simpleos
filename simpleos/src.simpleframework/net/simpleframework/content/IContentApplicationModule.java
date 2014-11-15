package net.simpleframework.content;

import net.simpleframework.content.component.remark.RemarkItem;
import net.simpleframework.web.IWebApplicationModule;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.component.AbstractComponentBean;
import net.simpleframework.web.page.component.ComponentParameter;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface IContentApplicationModule extends IWebApplicationModule {

	String getCatalogIdName(PageRequestResponse requestResponse);

	AbstractComponentBean getComponentBean(PageRequestResponse requestResponse);

	/**
	 * 获取列表实现类 ，实现者可覆盖
	 * 
	 * @return
	 */
	String getPagerHandleClass();

	String tabs(PageRequestResponse requestResponse);

	String tabs2(PageRequestResponse requestResponse);

	void doAttentionSent(final ComponentParameter compParameter, final RemarkItem remark, final Class<?> classBean);

}
