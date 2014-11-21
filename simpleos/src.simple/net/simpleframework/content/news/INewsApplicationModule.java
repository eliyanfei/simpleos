package net.simpleframework.content.news;

import java.util.List;

import net.simpleframework.content.IContentApplicationModule;
import net.simpleframework.content.component.newspager.NewsBean;
import net.simpleframework.content.component.newspager.NewsCatalog;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.component.ComponentParameter;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface INewsApplicationModule extends IContentApplicationModule {

	String getCatalogUrl(PageRequestResponse requestResponse, Object catalog);

	String getViewUrl(ComponentParameter compParameter, NewsBean news);

	/**
	 * 获取新闻类目的数据源
	 * 
	 * @param compParameter
	 * @return
	 */
	List<NewsCatalog> listNewsCatalog(PageRequestResponse requestResponse);

	String getTemplatePage(final ComponentParameter compParameter);
}
