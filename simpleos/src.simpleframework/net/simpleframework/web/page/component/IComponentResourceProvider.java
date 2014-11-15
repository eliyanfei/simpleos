package net.simpleframework.web.page.component;

import java.util.Collection;

import net.simpleframework.web.page.IPageResourceProvider;
import net.simpleframework.web.page.IResourceProvider;
import net.simpleframework.web.page.PageRequestResponse;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface IComponentResourceProvider extends IResourceProvider {

	String[] getPageJavascriptPath(PageRequestResponse requestResponse,
			Collection<AbstractComponentBean> componentBeans);

	String[] getPageCssPath(PageRequestResponse requestResponse,
			Collection<AbstractComponentBean> componentBeans);

	String[] getDependentComponents(PageRequestResponse requestResponse,
			Collection<AbstractComponentBean> componentBeans);

	IComponentRegistry getComponentRegistry();

	IPageResourceProvider getPageResourceProvider();
}
