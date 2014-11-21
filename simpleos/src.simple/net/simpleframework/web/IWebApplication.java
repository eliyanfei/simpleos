package net.simpleframework.web;

import java.util.Collection;

import javax.servlet.ServletContext;

import net.simpleframework.core.IApplication;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.web.page.FilterUtils;
import net.simpleframework.web.page.IFilterListener;
import net.simpleframework.web.page.IPageContext;
import net.simpleframework.web.page.PageRequestResponse;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface IWebApplication extends IApplication {

	ServletContext getServletContext();

	/**
	 * 为了数据源的问题，需要二次加载数据
	 */
	void init2();

	boolean isOK();

	void setPageContext(final String pageContext);

	@Override
	WebApplicationConfig getApplicationConfig();

	boolean isSystemUrl(final PageRequestResponse requestResponse);

	public static class Instance {
		static final String WEB_APPLICATION_INSTANCE = "__web_application";

		static ServletContext servletContext;

		public static IWebApplication getApplication() {
			return servletContext != null ? (IWebApplication) servletContext.getAttribute(WEB_APPLICATION_INSTANCE) : null;
		}

		public static void doInit(final ServletContext servletContext, final IWebApplication application) {
			LocaleI18n.addBasename(IWebApplication.class);
			Instance.servletContext = servletContext;
			servletContext.setAttribute(WEB_APPLICATION_INSTANCE, application);
			IPageContext.Instance.getPageContext().setApplication(application);

			final Collection<IFilterListener> listeners = FilterUtils.getFilterListeners(servletContext);
			listeners.add(new LastUrlFilterListener(application));
		}
	}
}
