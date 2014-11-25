package net.simpleframework.example;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import net.simpleframework.util.ConvertUtils;
import net.simpleframework.util.HTMLUtils;
import net.simpleframework.web.page.IForward;
import net.simpleframework.web.page.JsonForward;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.TextForward;
import net.simpleframework.web.page.UrlForward;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.HandleException;
import net.simpleframework.web.page.component.base.ajaxrequest.AbstractAjaxRequestHandle;

public class MyAjaxRequest extends AbstractAjaxRequestHandle {

	public IForward demo2(final ComponentParameter compParameter) {
		return new TextForward(HTMLUtils.convertHtmlLines(compParameter.getRequestParameter("ta")));
	}

	public IForward demo3(final ComponentParameter compParameter) {
		final Map<String, Object> json = new HashMap<String, Object>();
		json.put("t1", compParameter.getRequestParameter("t1"));
		json.put("t2", compParameter.getRequestParameter("t2"));
		json.put("t3", compParameter.getRequestParameter("t3"));
		json.put("t4", ConvertUtils.toBoolean(compParameter.getRequestParameter("t4"), false));
		return new JsonForward(json);
	}

	public IForward exception(final ComponentParameter compParameter) {
		throw HandleException.wrapException("这是在Ajax请求中，直接抛出的异常。");
	}

	public IForward ajaxCode(final ComponentParameter compParameter) {
		return new UrlForward(getUrl(compParameter, "code"));
	}

	public IForward ajaxDoc(final ComponentParameter compParameter) {
		return new UrlForward(getUrl(compParameter, "doc"));
	}

	public IForward ajaxDemo(final ComponentParameter compParameter) {
		return new UrlForward(getUrl(compParameter, "demo"));
	}

	private String getUrl(final PageRequestResponse requestResponse, final String n) {
		final String d = requestResponse.getRequestParameter("p");
		final File f = new File(requestResponse.getServletContext().getRealPath("app/demo/comps/" + d + "/" + n + ".jsp"));
		if (!f.exists()) {
			return "/app/demo/comps/null.jsp";
		} else {
			return "/app/demo/comps/" + d + "/" + n + ".jsp";
		}
	}
}
