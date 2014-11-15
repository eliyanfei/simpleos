package net.simpleframework.content.component.newspager;

import java.util.List;

import net.simpleframework.content.IContentPagerHandle;
import net.simpleframework.web.page.component.ComponentParameter;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface INewsPagerHandle extends IContentPagerHandle {
	/**
	 * 获取查看新闻的路径
	 * 
	 * @param compParameter
	 * @param news
	 * @return
	 */
	String getViewUrl(ComponentParameter compParameter, NewsBean news);

	String wrapOpenLink(ComponentParameter compParameter, NewsBean news);

	/**
	 * 获取Enum类型的Catalog
	 * 
	 * @param compParameter
	 * @return
	 */
	List<NewsCatalog> listNewsCatalog(final ComponentParameter compParameter);

	String getRemarkHandleClass(final ComponentParameter compParameter);

	boolean isRemarkNew(ComponentParameter compParameter, final NewsBean news);
}
