package net.simpleframework.web.page;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class UtilsFilterListener implements IFilterListener {

	@Override
	public boolean doFilter(final PageParameter pageParameter, final FilterChain filterChain)
			throws IOException {
		/* session cache */
		final HttpSession httpSession = pageParameter.getSession();
		GetSession.setSession(httpSession);

		final HttpServletRequest request = pageParameter.request;
		/* encoding */
		final String encoding = PageUtils.pageConfig.getCharset();
		if (encoding != null) {
			request.setCharacterEncoding(encoding);
		}
		return true;
	}
}