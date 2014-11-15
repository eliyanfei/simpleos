package net.simpleframework.applets.tag;

import net.simpleframework.content.IContentApplicationModule;
import net.simpleframework.core.id.ID;
import net.simpleframework.web.EFunctionModule;
import net.simpleframework.web.IWebApplicationModule;
import net.simpleframework.web.page.PageRequestResponse;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface ITagApplicationModule extends IWebApplicationModule {

	static final String _CATALOG_ID = "catalogId";

	static final String _TAG_ID = "tagId";

	String getTagUrl(PageRequestResponse requestResponse, TagBean tag);

	IContentApplicationModule getContentApplicationModule(PageRequestResponse requestResponse, EFunctionModule vtype);

	ID getCatalogId(final PageRequestResponse requestResponse);

	void reCreateAllTags(final PageRequestResponse requestResponse);
}
