package net.simpleframework.web.page;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.simpleframework.core.ALoggerAware;
import net.simpleframework.organization.IUser;
import net.simpleframework.util.HTTPUtils;
import net.simpleos.SimpleosUtil;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class PageRequestResponse extends ALoggerAware {
	public HttpServletRequest request;

	public HttpServletResponse response;

	public PageRequestResponse(final HttpServletRequest request, final HttpServletResponse response) {
		this.request = request;
		this.response = response;
	}

	public IUser getLoginUser() {
		return SimpleosUtil.getLoginUser(this);
	}

	private HttpServletRequest getPageRequest(HttpServletRequest request) {
		while (request instanceof HttpServletRequestWrapper) {
			if (!(request instanceof PageRequest)) {
				request = (HttpServletRequest) ((HttpServletRequestWrapper) request).getRequest();
			} else {
				return request;
			}
		}
		return request;
	}

	public Object getRequestAttribute(final String key) {
		return getPageRequest(request).getAttribute(key);
	}

	public void setRequestAttribute(final String key, final Object value) {
		getPageRequest(request).setAttribute(key, value);
	}

	public String getRequestParameter(final String key) {
		return request.getParameter(key);
	}

	public String getParameter(final String key) {
		return request.getParameter(key);
	}

	public HttpSession getSession() {
		return request.getSession(true);
	}

	public Object getSessionAttribute(final String key) {
		return getSession().getAttribute(key);
	}

	public void setSessionAttribute(final String key, final Object value) {
		getSession().setAttribute(key, value);
	}

	public void removeSessionAttribute(final String key) {
		getSession().removeAttribute(key);
	}

	public String getContextPath() {
		return request.getContextPath();
	}

	public ServletContext getServletContext() {
		return getSession().getServletContext();
	}

	public String getApplicationAbsolutePath(String path) {
		return getSession().getServletContext().getRealPath(path);
	}

	/********************************* utils *********************************/

	public boolean isHttpClientRequest() {
		return HTTPUtils.getUserAgent(request).indexOf("HttpClient") > -1;
	}

	public boolean isAjaxRequest() {
		return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
	}

	public boolean isHttpRequest() {
		return !isAjaxRequest() && !isHttpClientRequest();
	}

	public String wrapContextPath(final String url) {
		return HTTPUtils.wrapContextPath(request, url);
	}

	public boolean loc(final String url) throws IOException {
		return HTTPUtils.loc(request, response, url);
	}

	public OutputStream getFileOutputStream(final String filename, final long filesize) throws IOException {
		return HTTPUtils.getFileOutputStream(request, response, filename, filesize);
	}
}
