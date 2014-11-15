package net.simpleframework.applets.notification;

import java.util.Map;

import net.simpleframework.web.page.IForward;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.base.ajaxrequest.AbstractAjaxRequestHandle;

public class NotificationAction extends AbstractAjaxRequestHandle {
	public IForward deleteNotification(final ComponentParameter compParameter) {
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				json.put("result",
						NotificationUtils.deleteMessageNotificationByNotificationId(compParameter.getRequestParameter("notificationId")) > 0);
			}
		});
	}
}
