package net.simpleframework.web.page;

import java.io.IOException;

import javax.servlet.http.Cookie;

import net.simpleframework.util.HTTPUtils;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class UrlForward extends AbstractUrlForward {

	public UrlForward(final String url, final String includeRequestData) {
		super(url, includeRequestData);
	}

	public UrlForward(final String url) {
		super(url);
	}

	@Override
	public String getResponseText(final PageRequestResponse requestResponse) {
		final String url = getRequestUrl(requestResponse);
		try {
			
			final Connection conn = Jsoup.connect(url)
					.userAgent("HttpClient-[" + HTTPUtils.getUserAgent(requestResponse.request) + "]")
					.timeout(0);
			final Cookie[] cookies = requestResponse.request.getCookies();
			if (cookies != null) {
				for (final Cookie cookie : cookies) {
					conn.cookie(cookie.getName(), cookie.getValue());
				}
			}
			return conn.execute().body();
		} catch (final IOException e) {
			throw convertRuntimeException(e, url);
		}
	}
}
