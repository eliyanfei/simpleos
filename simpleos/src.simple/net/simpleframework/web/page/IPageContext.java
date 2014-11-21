package net.simpleframework.web.page;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import net.simpleframework.core.IApplication;
import net.simpleframework.core.IApplicationAware;
import net.simpleframework.core.Version;
import net.simpleframework.util.LocaleI18n;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface IPageContext extends IApplicationAware {
	void doInit(ServletContext servletContext) throws IOException;

	ServletContext getServletContext();

	void setApplication(IApplication application);

	IMultipartPageRequest createMultipartPageRequest(HttpServletRequest request);

	PageHtmlBuilder createPageHtmlBuilder();

	IPageResourceProvider createPageResourceProvider();

	PageConfig getPageConfig();

	Version getVersion();

	public static class Instance {

		public static IPageContext getPageContext() {
			return (IPageContext) servletContext.getAttribute(PAGE_CONTEXT_INSTANCE);
		}

		static ServletContext servletContext;

		public static void doInit(final ServletContext servletContext, final IPageContext pageContext)
				throws IOException {
			LocaleI18n.addBasename(IPageContext.class);

			Instance.servletContext = servletContext;
			servletContext.setAttribute(PAGE_CONTEXT_INSTANCE, pageContext);

			pageContext.doInit(servletContext);
		}

		static final String PAGE_CONTEXT_INSTANCE = "__page_context";
	}
}
