package net.simpleframework.web.page.component.ui.progressbar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.simpleframework.util.ConvertUtils;
import net.simpleframework.util.JSONUtils;
import net.simpleframework.util.JavascriptUtils;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.component.ComponentParameter;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class ProgressBarUtils {
	public static final String BEAN_ID = "progressbar_@bid";

	public static ComponentParameter getComponentParameter(final PageRequestResponse requestResponse) {
		return ComponentParameter.get(requestResponse, BEAN_ID);
	}

	public static ComponentParameter getComponentParameter(final HttpServletRequest request,
			final HttpServletResponse response) {
		return ComponentParameter.get(request, response, BEAN_ID);
	}

	public static String doProgressBarHandle(final HttpServletRequest request,
			final HttpServletResponse response) {
		final ComponentParameter nComponentParameter = getComponentParameter(request, response);
		if (nComponentParameter.componentBean != null) {
			final IProgressBarHandle handle = (IProgressBarHandle) nComponentParameter
					.getComponentHandle();
			if (handle != null) {
				final HttpSession httpSession = request.getSession();
				ProgressState state;
				final String beanId = nComponentParameter.componentBean.hashId();
				if (ConvertUtils.toBoolean(request.getParameter("starting"), false)) {
					httpSession.setAttribute(beanId, state = new ProgressState());
				} else {
					state = (ProgressState) httpSession.getAttribute(beanId);
				}
				if (state != null) {
					if (ConvertUtils.toBoolean(request.getParameter("messages"), false)) {
						return JSONUtils.toJSON(state.messages);
					}
					state.abort = ConvertUtils.toBoolean(request.getParameter("abort"), false);
					handle.doProgressState(nComponentParameter, state);
					final StringBuilder sb = new StringBuilder();
					sb.append("{");
					sb.append("\"step\" : ").append(state.step).append(",");
					sb.append("\"maxProgressValue\" : ").append(state.maxProgressValue).append(",");
					final int size = state.messages.size();
					if (size > 0) {
						sb.append("\"message\" : \"");
						sb.append(JavascriptUtils.escape(String.valueOf(state.messages.get(size - 1))));
						sb.append("\",");
					}
					sb.append("\"abort\" : ").append(state.abort);
					sb.append("}");
					return sb.toString();
				}
			}
		}
		return "{}";
	}
}
