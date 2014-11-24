package net.simpleos.backend.function;

import net.simpleframework.util.StringUtils;
import net.simpleframework.web.WebUtils;
import net.simpleframework.web.page.IForward;
import net.simpleframework.web.page.UrlForward;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.base.ajaxrequest.AbstractAjaxRequestHandle;
import net.simpleos.module.ISimpleosModule;
import net.simpleos.module.SimpleosModuleUtils;

/**
 * @author yanfei.li
 * @email eliyanfei@126.com
 * 2013-12-3下午03:26:11
 */
public class FunctionAction extends AbstractAjaxRequestHandle {
	/**
	 * 调整页面
	 * @param compParameter
	 * @return
	 */
	public IForward functionUrl(final ComponentParameter compParameter) {
		final String type = compParameter.getRequestParameter("type");
		final ISimpleosModule module = SimpleosModuleUtils.moduleMap.get(type);
		String jsp = module.getBackendJsp();
		if (jsp == null) {
			jsp = "/simpleos/module/backend/function/function_" + type + ".jsp";
		}
		return new UrlForward(jsp, "pah");
	}
}
