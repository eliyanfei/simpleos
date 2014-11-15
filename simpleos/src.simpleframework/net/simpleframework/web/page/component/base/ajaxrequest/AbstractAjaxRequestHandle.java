package net.simpleframework.web.page.component.base.ajaxrequest;

import java.util.HashMap;
import java.util.Map;

import net.simpleframework.web.page.IForward;
import net.simpleframework.web.page.JsonForward;
import net.simpleframework.web.page.component.AbstractComponentHandle;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.HandleException;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class AbstractAjaxRequestHandle extends AbstractComponentHandle implements
		IAjaxRequestHandle {
	public static final String JK_ERROR = "error";

	@Override
	public IForward ajaxProcess(final ComponentParameter compParameter) {
		return null;
	}

	protected IForward jsonForward(final ComponentParameter compParameter,
			final JsonCallback jsonCallback) {
		String methodkey = null;
		if (!((AjaxRequestBean) compParameter.componentBean).isParallel()) {
			methodkey = getClass().getName() + "#"
					+ Thread.currentThread().getStackTrace()[2].getMethodName() + "_doing";
			final Boolean bObj = (Boolean) compParameter.getSessionAttribute(methodkey);
			if (bObj != null && bObj.booleanValue()) {
				return null;
			}
			compParameter.setSessionAttribute(methodkey, Boolean.TRUE);
		}
		try {
			final Map<String, Object> json = new HashMap<String, Object>();
			if (jsonCallback != null) {
				try {
					jsonCallback.doAction(json);
				} catch (final Exception e) {
					throw HandleException.wrapException(e);
				}
			}
			return new JsonForward(json);
		} finally {
			if (methodkey != null) {
				compParameter.removeSessionAttribute(methodkey);
			}
		}
	}

	protected interface JsonCallback {

		void doAction(Map<String, Object> json) throws Exception;
	}
}
