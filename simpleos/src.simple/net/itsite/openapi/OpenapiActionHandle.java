package net.itsite.openapi;

import java.util.Map;

import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.IForward;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.base.ajaxrequest.AbstractAjaxRequestHandle;

/**
 * 第三方登入
 * 
 * @author 李岩飞
 *
 */
public class OpenapiActionHandle extends AbstractAjaxRequestHandle {
	/**
	 * 保存第三方登入信息
	 * @param compParameter
	 * @return
	 */
	public IForward saveOpenapi(final ComponentParameter compParameter) {
		final OpenapiBean openapi = new OpenapiBean();
		openapi.email = compParameter.getRequestParameter("oa_email");
		openapi.text = compParameter.getRequestParameter("oa_text");
		openapi.name = compParameter.getRequestParameter("oa_name");
		openapi.pass = compParameter.getRequestParameter("oa_password");
		final String url = OpenapiAppModule.applicationModule.login1(compParameter, openapi);
		if (StringUtils.hasText(url)) {
			return jsonForward(compParameter, new JsonCallback() {
				@Override
				public void doAction(final Map<String, Object> json) {
					json.put("url", url);
				}
			});
		} else {
			return null;
		}
	}
}
