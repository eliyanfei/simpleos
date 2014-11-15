package net.simpleframework.example;

import net.simpleframework.content.component.newspager.INewsPagerHandle;
import net.simpleframework.content.component.newspager.NewsBean;
import net.simpleframework.content.news.DefaultNewsApplicationModule;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.component.ComponentParameter;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class MyNewsApplicationModule extends DefaultNewsApplicationModule {

	@Override
	public String getApplicationUrl(final PageRequestResponse requestResponse) {
		return "/developer/app/news/m.jsp";
	}

	@Override
	public String getViewUrl(final ComponentParameter compParameter, final NewsBean news) {
		final StringBuilder sb = new StringBuilder();
		sb.append("/developer/app/news/v.jsp?");
		sb.append(((INewsPagerHandle) compParameter.getComponentHandle())
				.getIdParameterName(compParameter));
		sb.append("=").append(news.getId());
		return sb.toString();
	}
}
