package net.simpleframework.applets.openid;

import java.util.Map;

import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.IForward;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.base.ajaxrequest.AbstractAjaxRequestHandle;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class OpenIDAction extends AbstractAjaxRequestHandle {

	public IForward op(final ComponentParameter compParameter) {
		final String url = OpenIDUtils.applicationModule.getOpUrl(compParameter);
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

	public IForward doSave(final ComponentParameter compParameter) {
		final OpenIDBean openIDBean = new OpenIDBean();
		openIDBean.email = compParameter.getRequestParameter("oi_email");
		openIDBean.text = compParameter.getRequestParameter("oi_text");
		openIDBean.name = compParameter.getRequestParameter("oi_name");
		openIDBean.password = compParameter.getRequestParameter("oi_password");
		final String url = OpenIDUtils.applicationModule.login(compParameter, openIDBean);
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
