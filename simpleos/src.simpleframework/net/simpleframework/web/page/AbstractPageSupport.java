package net.simpleframework.web.page;

import javax.servlet.ServletContext;

import net.simpleframework.core.ALoggerAware;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class AbstractPageSupport extends ALoggerAware {
	private final ServletContext servletContext;

	public AbstractPageSupport(final ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	public ServletContext getServletContext() {
		return servletContext;
	}

	public abstract IHomePathAware getHomePathAware();

	public String getResourceHomePath() {
		return getHomePathAware().getResourceHomePath();
	}

	public String getResourceHomePath(final PageRequestResponse requestResponse) {
		return getHomePathAware().getResourceHomePath(requestResponse);
	}

	public String getCssResourceHomePath(final PageRequestResponse requestResponse) {
		return getHomePathAware().getCssResourceHomePath(requestResponse);
	}
}
