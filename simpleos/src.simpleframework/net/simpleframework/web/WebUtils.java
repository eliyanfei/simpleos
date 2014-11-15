package net.simpleframework.web;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.core.bean.IViewsBeanAware;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.util.HTTPUtils;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class WebUtils {
	public static IWebApplication application = IWebApplication.Instance.getApplication();

	public static String toLocaleString(final String str) {
		return HTTPUtils.toLocaleString(str, application.getApplicationConfig().getCharset());
	}

	public static Map<String, Object> toQueryParams(final String queryString) {
		return HTTPUtils.toQueryParams(queryString, application.getApplicationConfig().getCharset());
	}

	public static String toQueryString(final Map<String, Object> params) {
		return HTTPUtils.toQueryString(params, application.getApplicationConfig().getCharset());
	}

	public static String addParameters(final String url, final String queryString) {
		return HTTPUtils.addParameters(url, queryString, application.getApplicationConfig()
				.getCharset());
	}

	public static String putIncludeParameters(final HttpServletRequest request, final String url) {
		final int p = url.indexOf("?");
		if (p < 0) {
			return url;
		}
		for (final Map.Entry<String, Object> entry : toQueryParams(url.substring(p + 1)).entrySet()) {
			HTTPUtils.putParameter(request, entry.getKey(), entry.getValue());
		}
		return url.substring(0, p);
	}

	public static String enclose(final Object n) {
		return "(" + n + ")";
	}

	private final static String TEMP_PATH = "/temp/";

	public static String getTempPath(final ServletContext servletContext) {
		return servletContext.getRealPath(TEMP_PATH);
	}

	public static void updateViews(final HttpSession httpSession, final ITableEntityManager tblmgr,
			final IViewsBeanAware viewsAware) {
		if (tblmgr == null || viewsAware == null) {
			return;
		}
		synchronized (tblmgr) {
			final String attributeName = "views_" + viewsAware.getId();
			final boolean views = ConvertUtils.toBoolean(httpSession.getAttribute(attributeName),
					false);
			if (!views) {
				viewsAware.setViews(viewsAware.getViews() + 1);
				tblmgr.update(new String[] { "views" }, viewsAware);
				httpSession.setAttribute(attributeName, Boolean.TRUE);
			}
		}
	}
	
	public static String getCookie(final HttpServletRequest request, final String key) {
		final Cookie cookie = org.springframework.web.util.WebUtils.getCookie(request, key);
		return cookie != null ? cookie.getValue() : null;
	}
}
