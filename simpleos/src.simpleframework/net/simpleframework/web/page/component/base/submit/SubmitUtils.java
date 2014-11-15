package net.simpleframework.web.page.component.base.submit;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.AbstractUrlForward;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.PageUtils;
import net.simpleframework.web.page.UrlForward;
import net.simpleframework.web.page.component.ComponentException;
import net.simpleframework.web.page.component.ComponentParameter;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class SubmitUtils {
	public static final String BEAN_ID = "submit_@bid";

	public static ComponentParameter getComponentParameter(final PageRequestResponse requestResponse) {
		return ComponentParameter.get(requestResponse, BEAN_ID);
	}

	public static ComponentParameter getComponentParameter(final HttpServletRequest request,
			final HttpServletResponse response) {
		return ComponentParameter.get(request, response, BEAN_ID);
	}

	public static void doSubmit(final HttpServletRequest request, final HttpServletResponse response) {
		final ComponentParameter nComponentParameter = getComponentParameter(request, response);
		final ISubmitHandle submitHandle = (ISubmitHandle) nComponentParameter.getComponentHandle();
		if (submitHandle != null) {
			final String method = (String) nComponentParameter.getBeanProperty("handleMethod");
			AbstractUrlForward forward;
			if ((Boolean) nComponentParameter.getBeanProperty("binary")) {
				nComponentParameter.request = PageUtils.pageContext.createMultipartPageRequest(request);
			}
			try {
				if (StringUtils.hasText(method)) {
					final Method methodObject = submitHandle.getClass().getMethod(method,
							ComponentParameter.class);
					forward = (UrlForward) methodObject.invoke(submitHandle, nComponentParameter);
				} else {
					forward = submitHandle.submit(nComponentParameter);
				}
				if (forward != null) {
					nComponentParameter.loc(PageUtils.addParameters(forward.getUrl(), AbstractUrlForward
							.putRequestData(nComponentParameter,
									(String) nComponentParameter.getBeanProperty("includeRequestData"))));
				}
			} catch (final Exception e) {
				throw ComponentException.wrapComponentException(e);
			}
		}
	}
}
