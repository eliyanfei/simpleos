package net.simpleframework.web.page;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.simpleframework.util.StringUtils;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class GzipFilter extends net.sf.ehcache.constructs.web.filter.GzipFilter {
	private String cacheControl;

	@Override
	protected void doInit(final FilterConfig filterConfig) throws Exception {
		super.doInit(filterConfig);
		cacheControl = filterConfig.getInitParameter("cacheControl");
	}

	@Override
	protected void doFilter(final HttpServletRequest httpRequest,
			final HttpServletResponse httpResponse, final FilterChain filterChain) throws Exception {
		if (StringUtils.hasText(cacheControl)) {
			httpResponse.setHeader("Cache-Control", cacheControl);
		}
		super.doFilter(httpRequest, httpResponse, filterChain);
	}
}
