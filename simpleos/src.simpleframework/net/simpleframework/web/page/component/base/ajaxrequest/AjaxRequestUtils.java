package net.simpleframework.web.page.component.base.ajaxrequest;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.simpleframework.core.ALoggerAware;
import net.simpleframework.core.Logger;
import net.simpleframework.core.SimpleException;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.util.JSONUtils;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.IForward;
import net.simpleframework.web.page.JsonForward;
import net.simpleframework.web.page.PageUtils;
import net.simpleframework.web.page.ReflectUtils;
import net.simpleframework.web.page.component.AbstractComponentRegistry;
import net.simpleframework.web.page.component.ComponentException;
import net.simpleframework.web.page.component.ComponentParameter;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class AjaxRequestUtils {
	public static final String BEAN_ID = "ajax_@bid";

	static Logger logger = ALoggerAware.getLogger(AjaxRequestUtils.class);

	public static void doAjaxRequest(final HttpServletRequest request,
			final HttpServletResponse response) {
		final ComponentParameter compParameter = ComponentParameter.get(request, response, BEAN_ID);
		final Map<String, Object> json = new HashMap<String, Object>();
		IForward forward = null;
		boolean callback = true;
		if (compParameter.componentBean == null) {
			forward = createExceptionForward(new ComponentException(
					LocaleI18n.getMessage("AjaxRequestUtils.0")));
		} else {
			if (ReflectUtils.methodAccessForward != null) {
				final String jobExecute = (String) compParameter.getBeanProperty("jobExecute");
				try {
					forward = (IForward) ReflectUtils.methodAccessForward.invoke(null, compParameter,
							jobExecute, compParameter.getBeanProperty("name"));
				} catch (final Exception e) {
					logger.warn(e);
				}
			}
			if (forward != null) {
				callback = false;
			}

			if (forward == null) {
				final IAjaxRequestHandle ajaxRequestHandle = (IAjaxRequestHandle) compParameter
						.getComponentHandle();
				if (ajaxRequestHandle != null) {
					final String method = (String) compParameter.getBeanProperty("handleMethod");
					try {
						if (StringUtils.hasText(method)) {
							final Method methodObject = ajaxRequestHandle.getClass().getMethod(method,
									ComponentParameter.class);
							forward = (IForward) methodObject.invoke(ajaxRequestHandle, compParameter);
						} else {
							forward = ajaxRequestHandle.ajaxProcess(compParameter);
						}
					} catch (Throwable th) {
						th = SimpleException.convertThrowable(th);
						logger.error(th);
						forward = createExceptionForward(th);
					}
				}
			}

			final String id = StringUtils.text(
					(String) compParameter.getBeanProperty("ajaxRequestId"),
					(String) compParameter.getBeanProperty("name"));
			json.put("id", id);
		}
		final String responseText = forward != null ? forward.getResponseText(compParameter) : "";
		json.put("rt", responseText);
		json.put("cb", callback);
		json.put("isJSON", forward instanceof JsonForward);
		PrintWriter out;
		try {
			out = response.getWriter();
		} catch (final IOException e) {
			throw ComponentException.wrapComponentException(e);
		}
		out.write(JSONUtils.toJSON(json));
		out.flush();
	}

	private static IForward createExceptionForward(final Throwable th) {
		IForward forward;
		final HashMap<String, String> json = new HashMap<String, String>();
		json.put("title", PageUtils.pageConfig.getThrowableMessage(th));
		final String detail = ConvertUtils.toString(th);
		json.put("detail", detail);
		json.put("hash", StringUtils.hash(detail));
		((JsonForward) (forward = new JsonForward())).getMap().put("exception", json);
		return forward;
	}

	public static String getHomePath() {
		return AbstractComponentRegistry.getRegistry(AjaxRequestRegistry.ajaxRequest)
				.getResourceHomePath();
	}
}
