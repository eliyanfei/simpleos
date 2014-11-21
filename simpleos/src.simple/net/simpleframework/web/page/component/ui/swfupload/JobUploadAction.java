package net.simpleframework.web.page.component.ui.swfupload;

import java.util.Map;

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
public class JobUploadAction extends AbstractAjaxRequestHandle {
	@Override
	public Object getBeanProperty(final ComponentParameter compParameter, final String beanProperty) {
		if ("jobExecute".equals(beanProperty)) {
			final ComponentParameter nComponentParameter = SwfUploadUtils
					.getComponentParameter(compParameter);
			return nComponentParameter.getBeanProperty("jobUpload");
		}
		return super.getBeanProperty(compParameter, beanProperty);
	}

	@Override
	public IForward ajaxProcess(final ComponentParameter compParameter) {
		final ComponentParameter nComponentParameter = SwfUploadUtils
				.getComponentParameter(compParameter);
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				json.put("name", nComponentParameter.getBeanProperty("name"));
			}
		});
	}
}
