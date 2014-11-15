package net.simpleframework.web.page;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.simpleframework.core.ALoggerAware;
import net.simpleframework.core.SimpleException;
import net.simpleframework.organization.JobFilterListener;
import net.simpleframework.util.BeanUtils;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.util.HTTPUtils;
import net.simpleframework.util.JSONUtils;
import net.simpleframework.util.JavascriptUtils;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.component.base.ajaxrequest.AjaxRequestUtils;
import net.simpleframework.web.page.parser.PageParser;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class PageFilter extends ALoggerAware implements Filter {
	public static final String PAGELOAD_TIME = "pageload_time";

	@Override
	public void init(final FilterConfig filterConfig) throws ServletException {
		try {
			IPageContext pageContext;
			final String initializerHandle = filterConfig.getInitParameter("pageContext");
			if (StringUtils.hasText(initializerHandle)) {
				pageContext = (IPageContext) BeanUtils.newInstance(initializerHandle);
			} else {
				pageContext = new PageContext();
			}
			IPageContext.Instance.doInit(filterConfig.getServletContext(), pageContext);

			final Collection<IFilterListener> listeners = FilterUtils.getFilterListeners(filterConfig.getServletContext());
			listeners.add(new UtilsFilterListener());
			listeners.add(new JobFilterListener());
		} catch (final IOException ex) {
			throw new ServletException(ex);
		}
	}

	public void doFilterSuccess(final HttpServletRequest request, final HttpServletResponse httpResponse, final FilterChain filterChain)
			throws IOException, ServletException {
		final PageRequest pageRequest = new PageRequest(request);
		final PageParameter pageParameter = new PageParameter(pageRequest, httpResponse, null);
		try {
			final HttpSession httpSession = pageRequest.getSession();
			final boolean bHttpRequest = pageParameter.isHttpRequest();
			if (bHttpRequest) {
				httpSession.setAttribute(PAGELOAD_TIME, System.currentTimeMillis());
			}
			/* page document */
			final PageDocument pageDocument = PageDocumentFactory.getPageDocument(new PageRequestResponse(pageRequest, httpResponse));
			if (pageDocument == null) {
				if (!doFilter(pageParameter, filterChain)) {
					return;
				}
				filterChain.doFilter(pageRequest, httpResponse);
			} else {
				/* after process */
				if (!doFilter(pageParameter, filterChain)) {
					return;
				}
				final PageResponse pageResponse = new PageResponse(httpResponse, isGzip(pageParameter));
				pageParameter.pageDocument = pageDocument;
				pageParameter.response = pageResponse;

				// extract resource
				final IPageResourceProvider pageResourceProvider = pageDocument.getPageResourceProvider();
				//					PageResourceProviderUtils.extract(pageResourceProvider,
				//							httpSession.getServletContext());
				if (bHttpRequest) {
					PageUtils.setBrowserLocationPageDocument(httpSession, pageDocument);
				}

				final AbstractPageView abstractPageView = pageDocument.getPageView();
				final IForward forward = abstractPageView != null ? abstractPageView.forward(pageParameter) : null;
				if (forward == null) {
					filterChain.doFilter(pageRequest, pageResponse);
				}
				if (pageResponse.isCommitted()) {
					return;
				}
				String responseString = forward != null ? forward.getResponseText(pageParameter) : pageResponse.toString();
				final String contentType = pageResponse.getContentType();
				if (!StringUtils.hasText(contentType) || contentType.toLowerCase().startsWith("text/html")) {
					responseString = new PageParser(pageParameter).parser(responseString).toHtml(pageParameter);
				}
				write(pageParameter, responseString);
			}
		} catch (final Throwable e) {
			e.printStackTrace();
			logger.error(e);
			doThrowable(e, pageParameter);
		}

	}

	public void doFilterFailue(final HttpServletRequest request, final HttpServletResponse httpResponse, final FilterChain filterChain)
			throws IOException, ServletException {
		final String url = HTTPUtils.getRequestURI(request);
		if (url.contains("db_config.jsp") || url.contains("db.html")) {
			filterChain.doFilter(request, httpResponse);
		} else {
			final PageParameter pageParameter = new PageParameter(request, httpResponse, null);
			pageParameter.loc(request.getContextPath() + "/db.html");
		}
	}

	@Override
	public void doFilter(final ServletRequest request1, final ServletResponse response, final FilterChain filterChain) throws IOException,
			ServletException {
		if (request1 instanceof HttpServletRequest && response instanceof HttpServletResponse) {
			HttpServletResponse httpResponse = (HttpServletResponse) response;
			HttpServletRequest request = (HttpServletRequest) request1;
			doFilterSuccess(request, httpResponse, filterChain);
			//			if (IWebApplication.Instance.getApplication().isOK()) {
			//			} else {
			//				doFilterFailue(request, httpResponse, filterChain);
			//			}
		} else {
			filterChain.doFilter(request1, response);
		}
	}

	private boolean isGzip(final PageParameter pageParameter) {
		final String browserEncodings = pageParameter.request.getHeader("accept-encoding");
		return ((browserEncodings != null) && (browserEncodings.indexOf("gzip") != -1)) && PageUtils.pageConfig.isGzipResponse()
				&& !pageParameter.isHttpClientRequest();
	}

	private boolean doFilter(final PageParameter pageParameter, final FilterChain filterChain) throws Exception {
		for (final IFilterListener listener : FilterUtils.getFilterListeners(pageParameter.getServletContext())) {
			if (!listener.doFilter(pageParameter, filterChain)) {
				return false;
			}
		}
		initResponse(pageParameter);
		return true;
	}

	private String getResponseCharset(final PageParameter pageParameter) {
		final PageDocument pageDocument = pageParameter.pageDocument;
		String encoding = null;
		if (pageDocument != null) {
			encoding = pageDocument.getPageBean().getResponseCharacterEncoding();
		}
		if (!StringUtils.hasText(encoding)) {
			encoding = PageUtils.pageConfig.getCharset();
		}
		return encoding;
	}

	private void initResponse(final PageParameter pageParameter) {
		final HttpServletResponse response = pageParameter.response;
		final String encoding = getResponseCharset(pageParameter);
		response.setCharacterEncoding(encoding);
		response.setContentType("text/html;charset=" + encoding);
		// WebUtils.setNoCache(httpResponse);
		if (pageParameter.isHttpRequest()) {
			final Long l = (Long) pageParameter.getSession().getAttribute(PAGELOAD_TIME);
			if (l != null) {
				HTTPUtils.addCookie(response, PAGELOAD_TIME, (System.currentTimeMillis() - l.longValue()) / 1000d);
			}
		}
	}

	private void write(final PageParameter pageParameter, final String html) throws IOException {
		initResponse(pageParameter);
		// resetBuffer() 只会清掉內容的部份(Body)，而不会去清 status code 和 header
		final HttpServletResponse response = pageParameter.response;
		response.resetBuffer();
		final PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), getResponseCharset(pageParameter)));
		if (response instanceof PageResponse) {
			((PageResponse) response).initOutputStream();
		}
		out.write(html);
		out.close();
	}

	private void doThrowable(Throwable th, final PageParameter pageParameter) throws IOException {
		th = SimpleException.convertThrowable(th);
		if (true || pageParameter.request.getRequestURI().endsWith(AjaxRequestUtils.getHomePath() + "/jsp/ajax_request.jsp")
				|| pageParameter.isAjaxRequest()) {
			final HashMap<String, String> json = new HashMap<String, String>();
			json.put("title", PageUtils.pageConfig.getThrowableMessage(th));
			final String detail = ConvertUtils.toString(th);
			json.put("detail", detail);
			json.put("hash", StringUtils.hash(detail));
			write(pageParameter, JavascriptUtils.wrapScriptTag("$Actions.showError(" + JSONUtils.toJSON(json) + ");"));
		} else {
			if (pageParameter.loc(PageUtils.getResourcePath() + "/jsp/error_template.jsp?systemErrorPage=" + PageUtils.pageConfig.getErrorPage())) {
				pageParameter.getSession().setAttribute(IPageConstants.THROWABLE, th);
			}
		}
	}

	@Override
	public void destroy() {
	}
}