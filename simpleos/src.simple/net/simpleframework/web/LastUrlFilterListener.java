package net.simpleframework.web;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;

import net.simpleframework.util.ConvertUtils;
import net.simpleframework.util.HTTPUtils;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.IFilterListener;
import net.simpleframework.web.page.PageParameter;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class LastUrlFilterListener implements IFilterListener {
	public final static String SESSION_LAST_URL = "__last_url";

	private final IWebApplication application;

	public LastUrlFilterListener(final IWebApplication application) {
		this.application = application;
	}

	@Override
	public boolean doFilter(final PageParameter pageParameter, final FilterChain filterChain)
			throws IOException {
		final HttpServletRequest request = pageParameter.request;
		if (pageParameter.isHttpRequest()
				&& StringUtils.blank(request.getHeader("Accept")).startsWith("text/html")
				&& !ConvertUtils.toBoolean(pageParameter.getRequestParameter("iframe"), false)) {
			if (!application.isSystemUrl(pageParameter)) {
				request.getSession().setAttribute(SESSION_LAST_URL,
						HTTPUtils.getRequestAndQueryStringUrl(request));
			}
		}
		return true;
	}
}
