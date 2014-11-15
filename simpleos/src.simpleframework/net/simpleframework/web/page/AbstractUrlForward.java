package net.simpleframework.web.page;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.simpleframework.core.ALoggerAware;
import net.simpleframework.core.SimpleException;
import net.simpleframework.util.GenId;
import net.simpleframework.util.LinkedCaseInsensitiveMap;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.component.AbstractComponentRegistry;
import net.simpleframework.web.page.component.IComponentHandle;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class AbstractUrlForward extends ALoggerAware implements IForward {
	private String url;

	private final String includeRequestData;

	public AbstractUrlForward(final String url, final String includeRequestData) {
		this.url = url;
		this.includeRequestData = includeRequestData;
	}

	public AbstractUrlForward(final String url) {
		this(url, null);
	}

	protected String getRequestUrl(final PageRequestResponse requestResponse) {
		final StringBuilder sb = new StringBuilder();
		sb.append(getLocalhostUrl(requestResponse.request)).append(
				requestResponse.request.getContextPath());
		final Map<String, Object> qMap = PageUtils.toQueryParams(putRequestData(requestResponse,
				includeRequestData));
		final String url = getUrl();
		final int qp = url.indexOf("?");
		if (qp > -1) {
			sb.append(url.substring(0, qp));
			qMap.putAll(PageUtils.toQueryParams(url.substring(qp + 1)));
		} else {
			sb.append(url);
		}
		sb.append(";jsessionid=").append(requestResponse.getSession().getId());
		sb.append("?").append(PageUtils.toQueryString(qMap));
		return sb.toString();
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(final String url) {
		this.url = url;
	}

	public String getIncludeRequestData() {
		return includeRequestData;
	}

	protected RuntimeException convertRuntimeException(final Exception ex, final String url) {
		logger.warn(ex);
		return SimpleException.wrapException(PageException.class, "url: " + url, ex);
	}

	public static String putRequestData(final PageRequestResponse requestResponse,
			final String includeRequestData) {
		final String requestId = GenId.genUID();
		SessionCache.put(requestResponse.getSession(), requestId, new RequestData(requestResponse,
				includeRequestData));
		return IForward.REQUEST_ID + "=" + requestId;
	}

	public static RequestData getRequestDataByRequest(final HttpServletRequest request) {
		return (RequestData) SessionCache.remove(request.getSession(),
				request.getParameter(IForward.REQUEST_ID));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static class RequestData {
		final Map parameters = new HashMap();

		final Map attributes = new HashMap();

		final Map headers = new LinkedCaseInsensitiveMap();

		public RequestData(final PageRequestResponse requestResponse, String includeRequestData) {
			final HttpServletRequest request = requestResponse.request;
			includeRequestData = StringUtils.text(includeRequestData, "p").toLowerCase();

			// parameters
			if (includeRequestData.contains("p")) {
				parameters.putAll(request.getParameterMap());
				PageUtils.removeSystemParameters(parameters);
			}

			// attributes
			final Enumeration<String> attributeNames = request.getAttributeNames();
			while (attributeNames.hasMoreElements()) {
				final String name = attributeNames.nextElement();
				if (includeRequestData.contains("a")
						|| name.startsWith(IComponentHandle.REQUEST_HANDLE_KEY)) {
					final Object value = request.getAttribute(name);
					if (value != null) {
						attributes.put(name, value);
					}
				}
			}

			// headers
			if (includeRequestData.contains("h")) {
				final Enumeration<String> headerNames = request.getHeaderNames();
				while (headerNames.hasMoreElements()) {
					final String name = headerNames.nextElement();
					final Enumeration e = request.getHeaders(name);
					if (e != null) {
						headers.put(name, e);
					}
				}
			}
		}
	}

	public static String getLocalhostUrl(final HttpServletRequest request) {
		final StringBuilder sb = new StringBuilder();
		sb.append(request.getScheme()).append("://localhost:")
				.append(PageUtils.pageConfig.getServletPort(request));
		return sb.toString();
	}

	public static AbstractUrlForward componentUrl(final String componentRegistry, final String url) {
		return new UrlForward(AbstractComponentRegistry.getRegistry(componentRegistry)
				.getResourceHomePath() + url);
	}
}
