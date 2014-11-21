package net.simpleframework.content.blog;

import net.simpleframework.content.IContentApplicationModule;
import net.simpleframework.content.component.newspager.NewsPagerBean;
import net.simpleframework.organization.IUser;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.component.ComponentParameter;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface IBlogApplicationModule extends IContentApplicationModule {

	String getBlogUrl(PageRequestResponse requestResponse, IUser user);

	String getBlogViewUrl(ComponentParameter compParameter, Blog blog);

	String getTemplatePage(final ComponentParameter compParameter);

	@Override
	String getCatalogIdName(final PageRequestResponse requestResponse);

	NewsPagerBean getMyBlogPagerBean(PageRequestResponse requestResponse);
}
